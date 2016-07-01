package uk.me.aujla.io;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import uk.me.aujla.model.ClassifiedTextBlock;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ClassifiedBlocksReader {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private final ClassifierPaths classifierPaths;

    public ClassifiedBlocksReader(ClassifierPaths classifierPaths) {
        this.classifierPaths = classifierPaths;
    }

    public List<ClassifiedBlocks> readAllTraining(String classifierName) throws IOException {
        File trainingDataFolder = classifierPaths.getOrCreateTrainingPath(classifierName);
        System.out.println("trainingDataFolder: "+ trainingDataFolder.getCanonicalPath());
        return getClassifiedBlocks(trainingDataFolder);

    }

    public List<ClassifiedBlocks> readAllTest(String classifierName) throws IOException {
        File testDataFolder = classifierPaths.getOrCreateTestPath(classifierName);
        return getClassifiedBlocks(testDataFolder);

    }

    private List<ClassifiedBlocks> getClassifiedBlocks(File trainingDataFolder) throws IOException {
        Collection<File> textBlockFiles = FileUtils.listFiles(trainingDataFolder, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        List<ClassifiedBlocks> allTrainingBlocks = new ArrayList<>();
        for(File file : textBlockFiles) {
            String json = FileUtils.readFileToString(file, StandardCharsets.UTF_8);

            Type listType = new TypeToken<ArrayList<ClassifiedTextBlock>>() {}.getType();
            List<ClassifiedTextBlock> classifiedBlocks = gson.fromJson(json, listType);

            allTrainingBlocks.add(new ClassifiedBlocks(file.getName(), classifiedBlocks));
        }

        return allTrainingBlocks;
    }
}
