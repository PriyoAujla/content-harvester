package uk.me.aujla.io;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class ClassifierPaths {

    private final File CLASSIFIERS_FOLDER_PATH;

    public ClassifierPaths(File classifiersFolderPath) {
        CLASSIFIERS_FOLDER_PATH = classifiersFolderPath;
    }

    public File getOrCreateTrainingPath(String classifierName) throws IOException {
        return createFoldersIfAbsent(createFolderPath(classifierName, "training"));
    }

    public File getOrCreateTestPath(String classifierName) throws IOException {
        return createFoldersIfAbsent(createFolderPath(classifierName, "test"));
    }

    public File getOrCreateClassifierPath(String classifierName) throws IOException {
        return createFoldersIfAbsent(createFolderPathToClassifier(classifierName));
    }

    private String createFolderPathToClassifier(String classifierName) {
        return CLASSIFIERS_FOLDER_PATH + "/" + classifierName;
    }

    private String createFolderPath(String classifierName,String dataType) {
        return createFolderPathToClassifier(classifierName) + "/"+ dataType;
    }

    private File createFoldersIfAbsent(String path) throws IOException {
        File pathToTrainingFolder = new File(path);

        FileUtils.forceMkdir(pathToTrainingFolder);

        return pathToTrainingFolder;
    }
}
