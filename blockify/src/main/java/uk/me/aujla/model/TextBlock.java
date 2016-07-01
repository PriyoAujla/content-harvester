package uk.me.aujla.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Set;

/**
 * A block is a sequence of contiguous words within an html element [div,p,span etc].
 * It also has meta-data like class declarations.
 */
public class TextBlock {

    private String text;
    private String innerHrefTagText;

    // css class name declarations including inherited class names
    private Set<String> classNames;

    public TextBlock() {
        // to satisfy object to json serialiser
    }

    public TextBlock(String text,
                     String innerHrefTagText,
                     Set<String> classNames) {
        this.text = text;
        this.innerHrefTagText = innerHrefTagText;
        this.classNames = classNames;
    }

    public String getText() {
        return text;
    }

    public String getInnerHrefTagText() {
        return innerHrefTagText;
    }

    public Set<String> getClassNames() {
        return classNames;
    }

    public void merge(TextBlock mergeWith) {
        this.text += mergeWith.getText();
        this.innerHrefTagText += " "+ mergeWith.getInnerHrefTagText();
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
