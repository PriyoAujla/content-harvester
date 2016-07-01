package net.smartology.server.services;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WebsiteScraperService {

    public static final String PATH_TO_CLASSIFIERS_FOLDER = "../data/src/main/resources/classifiers";

    public List<DisplayWebsiteClassifier> findAll() {

        List<File> websiteClassifierFolders = Arrays.stream(new File(PATH_TO_CLASSIFIERS_FOLDER).listFiles())
                                                        .filter(file -> file.isDirectory())
                                                        .collect(Collectors.toList());

        List<DisplayWebsiteClassifier> displayWebsiteClassifiers = websiteClassifierFolders.stream().map(file -> {
            boolean hasClassifierFile = hasClassifierFile(file);
            return new DisplayWebsiteClassifier(hasClassifierFile, file.getName());
        }).collect(Collectors.toList());

        return displayWebsiteClassifiers;
    }

    public void create(String name) {
        String classifierFolderPath = PATH_TO_CLASSIFIERS_FOLDER + "/" + name;
        new File(classifierFolderPath).mkdirs();
        new File(classifierFolderPath + "/training").mkdirs();
        new File(classifierFolderPath + "/test").mkdirs();
    }

    private boolean hasClassifierFile(File websiteClassifierFolder) {
        return Arrays.asList(websiteClassifierFolder.listFiles()).stream()
                .anyMatch(file -> "classifier".equals(file.getName()));
    }


    public static class DisplayWebsiteClassifier {
        private boolean classifierFilePresent;
        private String name;

        public DisplayWebsiteClassifier(boolean classifierFilePresent, String name) {
            this.classifierFilePresent = classifierFilePresent;
            this.name = name;
        }

        public boolean isClassifierFilePresent() {
            return classifierFilePresent;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }
    }
}
