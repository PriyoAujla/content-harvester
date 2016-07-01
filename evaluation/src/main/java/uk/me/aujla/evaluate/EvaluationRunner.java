package uk.me.aujla.evaluate;

import uk.me.aujla.Dataset;
import uk.me.aujla.DatasetGenerator;
import uk.me.aujla.classifiers.ADTreeClassifier;
import uk.me.aujla.feature.TextBlockFeatures;
import uk.me.aujla.io.ClassifiedBlocks;
import uk.me.aujla.io.ClassifiedBlocksReader;
import uk.me.aujla.io.ClassifierPaths;

import java.io.File;
import java.util.Collection;
import java.util.List;

public class EvaluationRunner {

    private static final String WEBSITE_CLASSIFIER = "bbc";

    public static void main(String[] args) throws Exception {

        ClassifierPaths classifierPaths = new ClassifierPaths(new File("data/src/main/resources/classifiers"));
        ClassifiedBlocksReader classifiedBlocksReader = new ClassifiedBlocksReader(classifierPaths);
        List<ClassifiedBlocks> trainingClassifiedBlocks = classifiedBlocksReader.readAllTraining(WEBSITE_CLASSIFIER);
        List<ClassifiedBlocks> testClassifiedBlocks = classifiedBlocksReader.readAllTest(WEBSITE_CLASSIFIER);

        DatasetGenerator datasetGenerator = new DatasetGenerator();
        Dataset trainingDataset = datasetGenerator.generate(trainingClassifiedBlocks);
        Dataset testDataset = datasetGenerator.generate(testClassifiedBlocks);

        ADTreeClassifier adTreeClassifier = new ADTreeClassifier();
        adTreeClassifier.train(trainingDataset);
        adTreeClassifier.evaluate(testDataset);

        System.out.println("Evaluating article text blocks");
        Collection<TextBlockFeatures> articleTextBlockFeatures = testDataset.getArticleTextBlocks();
        for(TextBlockFeatures textBlockFeatures: articleTextBlockFeatures) {
            String classification = adTreeClassifier.classify(textBlockFeatures);
            if(ADTreeClassifier.NON_ARTICLE_TEXT_BLOCK_CLASS_ATTRIBUTE.equals(classification)) {
                System.out.println("Article text block ["+textBlockFeatures+"] incorrectly classified as non text block");
            }
        }

        System.out.println("Evaluating non article text blocks");
        Collection<TextBlockFeatures> nonArticleTextBlockFeatures = testDataset.getNonArticleTextBlocks();
        for(TextBlockFeatures textBlockFeatures: nonArticleTextBlockFeatures) {
            String classification = adTreeClassifier.classify(textBlockFeatures);
            if(ADTreeClassifier.ARTICLE_TEXT_BLOCK_CLASS_ATTRIBUTE.equals(classification)) {
                System.out.println("Non Article text block ["+textBlockFeatures+"] incorrectly classified as text block");
            }
        }

        //System.out.println(adTreeClassifier.printClassifier());
    }



}
