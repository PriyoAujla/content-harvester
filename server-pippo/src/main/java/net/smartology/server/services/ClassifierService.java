package net.smartology.server.services;


import com.google.common.util.concurrent.MoreExecutors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.me.aujla.classifiers.ADTreeClassifier;
import uk.me.aujla.feature.TextBlockFeatures;
import uk.me.aujla.feature.TextBlockFeaturesGenerator;
import uk.me.aujla.model.TextBlock;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

public class ClassifierService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassifierService.class);

    private ExecutorService executorService;

    public ClassifierService() {
        executorService = MoreExecutors.getExitingExecutorService((ThreadPoolExecutor) Executors.newFixedThreadPool(1));
    }

    public void trainAndSave(String websiteClassifierName) throws Exception {
        ClassifierTrainAndSaveRunnable runnable = new ClassifierTrainAndSaveRunnable(websiteClassifierName, WebsiteScraperService.PATH_TO_CLASSIFIERS_FOLDER);
        executorService.execute(runnable);
    }


    public static String createPathToClassifier(String websiteClassifierName) {
        return WebsiteScraperService.PATH_TO_CLASSIFIERS_FOLDER+"/"+websiteClassifierName+"/classifier";
    }

    public String getText(String websiteClassifierName, List<TextBlock> textBlocks) throws Exception {

        String classifierFilePath = createPathToClassifier(websiteClassifierName);
        ADTreeClassifier classifier;
        try(
                FileInputStream fileOutputStream = new FileInputStream(classifierFilePath);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileOutputStream)
        ){
            classifier = (ADTreeClassifier) objectInputStream.readObject();
        }

        TextBlockFeaturesGenerator textBlockFeaturesFactory = new TextBlockFeaturesGenerator();
        List<TextBlockFeatures> textBlockFeatures = textBlockFeaturesFactory.create(textBlocks, "");

        List<String> articleTextBlocks = textBlockFeatures.stream()
                .filter(b -> {
                            try {
                                return classifier.isArticleText(b);
                            } catch (Exception e) {
                                LOGGER.error("Unable to classify block feature [{}]", b, e);
                            }

                            return false;
                        }
                )
                .map(TextBlockFeatures::getText)
                .collect(Collectors.toList());

        StringBuilder articleText = new StringBuilder();
        articleTextBlocks.stream().forEach(textBlock -> {
            articleText.append(textBlock);
            articleText.append("\n\n");
        });

        return articleText.toString();

    }

}
