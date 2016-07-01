package net.smartology.server.models;

import org.apache.commons.lang3.builder.ToStringBuilder;
import uk.me.aujla.model.TextBlock;

public class TaggedTextBlock {

    private TextBlock textBlock;
    private boolean isArticleTextBlock;

    public TaggedTextBlock() {

    }

    public TaggedTextBlock(TextBlock textBlock,
                           boolean isArticleTextBlock) {
        this.textBlock = textBlock;
        this.isArticleTextBlock = isArticleTextBlock;
    }

    public boolean isArticleTextBlock() {
        return isArticleTextBlock;
    }

    public TextBlock getTextBlock() {
        return textBlock;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
