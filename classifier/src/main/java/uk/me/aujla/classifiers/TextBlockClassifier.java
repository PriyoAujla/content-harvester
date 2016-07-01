package uk.me.aujla.classifiers;

import uk.me.aujla.Dataset;
import uk.me.aujla.feature.TextBlockFeatures;

public interface TextBlockClassifier {

    void train(Dataset dataset) throws Exception;
    boolean isArticleText(TextBlockFeatures textBlockFeatures) throws Exception;
}
