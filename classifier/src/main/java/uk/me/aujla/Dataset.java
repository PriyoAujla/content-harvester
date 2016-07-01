package uk.me.aujla;

import uk.me.aujla.feature.TextBlockFeatures;

import java.util.Collection;
import java.util.List;

public class Dataset {

    private final Collection<TextBlockFeatures> articleTextBlocks;
    private final Collection<TextBlockFeatures> nonArticleTextBlocks;

    public Dataset(Collection<TextBlockFeatures> articleTextBlocks,
                   Collection<TextBlockFeatures> nonArticleTextBlocks) {
        this.articleTextBlocks = articleTextBlocks;
        this.nonArticleTextBlocks = nonArticleTextBlocks;
    }

    public Collection<TextBlockFeatures> getArticleTextBlocks() {
        return articleTextBlocks;
    }

    public Collection<TextBlockFeatures> getNonArticleTextBlocks() {
        return nonArticleTextBlocks;
    }
}
