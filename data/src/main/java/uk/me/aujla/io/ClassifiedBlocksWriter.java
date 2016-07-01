package uk.me.aujla.io;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class ClassifiedBlocksWriter {

    public static final Logger LOGGER = LoggerFactory.getLogger(ClassifiedBlocksWriter.class);

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private final ClassifierPaths classifierPaths;

    public ClassifiedBlocksWriter(ClassifierPaths classifierPaths) {
        this.classifierPaths = classifierPaths;
    }


    public void writeTraining(ClassifiedBlocks classifiedBlocks, String classifierName) throws IOException {
        File trainingPath = classifierPaths.getOrCreateTrainingPath(classifierName);
        write(classifiedBlocks, trainingPath);
    }

    public void writeTest(ClassifiedBlocks classifiedBlocks, String classifierName) throws IOException {
        File testPath = classifierPaths.getOrCreateTestPath(classifierName);
        write(classifiedBlocks, testPath);
    }

    private void write(ClassifiedBlocks classifiedBlocks, File pathToFolder) throws IOException {
        File saveToFilePath = new File(pathToFolder.getPath() + "/" + classifiedBlocks.getId());
        if(saveToFilePath.exists()) {
            LOGGER.warn("overwriting classified blocks file [{}]", saveToFilePath.getCanonicalPath());
        }
        FileUtils.writeStringToFile(saveToFilePath, gson.toJson(classifiedBlocks.getClassifiedBlocks()));
    }
}
