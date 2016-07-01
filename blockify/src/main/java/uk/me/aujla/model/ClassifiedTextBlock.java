package uk.me.aujla.model;

import java.util.Set;

public class ClassifiedTextBlock extends TextBlock {

    private boolean isArticleTextBlock;

    public ClassifiedTextBlock() {
        // satisfy object to json serialiser
        super();
    }

    public ClassifiedTextBlock(String text, String innerHrefTagText, Set<String> classNames,
                               boolean isArticleTextBlock) {
        super(text, innerHrefTagText, classNames);
        this.isArticleTextBlock = isArticleTextBlock;
    }

    public ClassifiedTextBlock(TextBlock textBlock, boolean isArticleTextBlock) {
        this(textBlock.getText(), textBlock.getInnerHrefTagText(), textBlock.getClassNames(), isArticleTextBlock);
    }

    public boolean isArticleTextBlock() {
        return isArticleTextBlock;
    }
}
