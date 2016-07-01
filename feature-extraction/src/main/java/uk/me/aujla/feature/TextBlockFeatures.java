package uk.me.aujla.feature;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Set;

public class TextBlockFeatures {

    private final String textBlockId;
    private final String text;

    private final double wordDensity;
    private final double linkDensity;
    private final double punctuationDensity;
    private final double capitalisedWordsDensity;
    private final double relativePosition;
    private final double previousTextBlockQuotient;
    private final double nextTextBlockQuotient;
    private final Set<String> classNames;

    public TextBlockFeatures(String textBlockId,
                             String text,
                             double wordDensity,
                             double linkDensity,
                             double punctuationDensity,
                             double capitalisedWordsDensity,
                             double relativePosition,
                             double previousTextBlockQuotient,
                             double nextTextBlockQuotient,
                             Set<String> classNames) {
        this.textBlockId = textBlockId;
        this.text = text;
        this.wordDensity = wordDensity;
        this.linkDensity = linkDensity;
        this.punctuationDensity = punctuationDensity;
        this.capitalisedWordsDensity = capitalisedWordsDensity;
        this.relativePosition = relativePosition;
        this.previousTextBlockQuotient = previousTextBlockQuotient;
        this.nextTextBlockQuotient = nextTextBlockQuotient;
        this.classNames = classNames;
    }

    public String getTextBlockId() {
        return textBlockId;
    }

    public String getText() {
        return text;
    }

    public double getWordDensity() {
        return wordDensity;
    }

    public double getLinkDensity() {
        return linkDensity;
    }

    public double getPunctuationDensity() {
        return punctuationDensity;
    }

    public double getCapitalisedWordsDensity() {
        return capitalisedWordsDensity;
    }

    public double getRelativePosition() {
        return relativePosition;
    }

    public double getPreviousTextBlockQuotient() {
        return previousTextBlockQuotient;
    }

    public double getNextTextBlockQuotient() {
        return nextTextBlockQuotient;
    }

    public Set<String> getClassNames() {
        return classNames;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
