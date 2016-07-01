package uk.me.aujla;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.me.aujla.feature.TextBlockFeatures;
import uk.me.aujla.feature.TextBlockFeaturesGenerator;
import uk.me.aujla.io.ClassifiedBlocks;
import uk.me.aujla.model.ClassifiedTextBlock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DatasetGenerator {

    private static final Logger LOGGER  = LoggerFactory.getLogger(DatasetGenerator.class);

    private TextBlockFeaturesGenerator textBlockFeaturesGenerator;

    public DatasetGenerator() throws IOException {
        textBlockFeaturesGenerator = new TextBlockFeaturesGenerator();
    }

    public Dataset generate(List<ClassifiedBlocks> classifiedBlocks) {

        Collection<TextBlockFeatures> articleBlocks = new ArrayList<>();
        Collection<TextBlockFeatures> nonArticleBlocks = new ArrayList<>();

        classifiedBlocks.parallelStream().forEach( blocks -> {
            String blockIdPrefix = blocks.getId();
            List<ClassifiedTextBlock> classifiedTextBlocks = blocks.getClassifiedBlocks();
            try {
                List<TextBlockFeatures> textBlockFeatures = textBlockFeaturesGenerator.create(classifiedTextBlocks, blockIdPrefix);
                for(int i=0; i< classifiedTextBlocks.size();i++) {
                    TextBlockFeatures featuresVector = textBlockFeatures.get(i);
                    ClassifiedTextBlock classifiedTextBlock = classifiedTextBlocks.get(i);
                    if(classifiedTextBlock.isArticleTextBlock()) {
                        articleBlocks.add(featuresVector);
                    } else {
                        nonArticleBlocks.add(featuresVector);
                    }
                }
            } catch (IOException e) {
                LOGGER.error("Unable to convert classified text blocks into Dataset", e);
            }
        });

        return new Dataset(articleBlocks, nonArticleBlocks);
    }
}
