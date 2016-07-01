package net.smartology.server.services;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.me.aujla.Dataset;
import uk.me.aujla.DatasetGenerator;
import uk.me.aujla.classifiers.ADTreeClassifier;
import uk.me.aujla.io.ClassifiedBlocks;
import uk.me.aujla.io.ClassifiedBlocksReader;
import uk.me.aujla.io.ClassifierPaths;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

class ClassifierTrainAndSaveRunnable implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassifierTrainAndSaveRunnable.class);

    private ClassifiedBlocksReader classifiedBlocksReader;
    DatasetGenerator datasetGenerator;

    private String websiteClassifierName;

    public ClassifierTrainAndSaveRunnable(String websiteClassifierName, String pathToClassifiersFolder) throws IOException {
        this.websiteClassifierName = websiteClassifierName;
        ClassifierPaths classifierPaths = new ClassifierPaths(new File(pathToClassifiersFolder));
        classifiedBlocksReader = new ClassifiedBlocksReader(classifierPaths);
        datasetGenerator = new DatasetGenerator();
    }

    @Override
    public void run() {
        try {
            trainAndSave(websiteClassifierName);
        } catch (Exception e) {
            LOGGER.error("Unable to train and save website classifier for [{}]", websiteClassifierName, e);
        }
    }


    public void trainAndSave(String websiteClassifierName) throws Exception {
        long start = System.nanoTime();
        LOGGER.info("Training classifier [{}].....", websiteClassifierName);

        List<ClassifiedBlocks> classifiedBlocks = classifiedBlocksReader.readAllTraining(websiteClassifierName);
        LOGGER.info("classified blocks {}", classifiedBlocks);

        ADTreeClassifier adTreeClassifier = new ADTreeClassifier();
        Dataset dataset = datasetGenerator.generate(classifiedBlocks);
        adTreeClassifier.train(dataset);

        long end = System.nanoTime();
        long timeInMilli = (end-start)/1_000_000;
        double timeInSeconds = (end-start)/1_000_000_000;
        LOGGER.info("Finished training classifier [{}] time: [{}]ms [{}]s", websiteClassifierName, timeInMilli, timeInSeconds);


        save(websiteClassifierName, adTreeClassifier);
    }

    private void save(String websiteClassifierName, ADTreeClassifier adTreeClassifier) throws Exception {
        LOGGER.info("Saving classifier [{}].....", websiteClassifierName);
        String saveToFilePath = WebsiteScraperService.PATH_TO_CLASSIFIERS_FOLDER + "/" +websiteClassifierName + "/classifier";

        try(
                FileOutputStream fileOutputStream = new FileOutputStream(saveToFilePath);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)
        ){
            objectOutputStream.writeObject(adTreeClassifier);
        }
        LOGGER.info("Finished saving classifier [{}]", websiteClassifierName);
    }

}
