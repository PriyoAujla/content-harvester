package uk.me.aujla.feature

import spock.lang.Specification
import spock.lang.Unroll
import uk.me.aujla.model.TextBlock

class TextBlockMergerTest extends Specification {

    private static final List<TextBlock> SENTENCE_2_BLOCKS = [
            new TextBlock(text: "Hello "),
            new TextBlock(text: "world.")
    ]

    private static final List<TextBlock> SENTENCE_5_BLOCKS = [
            new TextBlock(text: "Mary "),
            new TextBlock(text: "had "),
            new TextBlock(text: "a"),
            new TextBlock(text: " little"),
            new TextBlock(text: " lamb.")
    ]

    private static final List<TextBlock> SENTENCE_SPLIT_BETWEEN_COMPLETE_SENTENCES = [
            new TextBlock(text: "This is a complete sentence."),
            new TextBlock(text: "This is a broken"),
            new TextBlock(text: " sentence."),
            new TextBlock(text: " Another complete setence."),
            new TextBlock(text: " And another.")
    ]

    def underTest = new TextBlockMerger();

    @Unroll
    def "merge text blocks if #description"() {

        when:
        def result = underTest.process(textBlocks)
        then:
            result.size() == expSize
            result[expIndex].text == expText

        where:
            description                             | textBlocks                                 | expSize | expIndex | expText
            "sentence split by 2 blocks"            | SENTENCE_2_BLOCKS                          | 1       | 0        | "Hello world."
            "sentence split by 5 blocks"            | SENTENCE_5_BLOCKS                          | 1       | 0        | "Mary had a little lamb."
            "multiple sentences 1 split by 2 blocks"| SENTENCE_SPLIT_BETWEEN_COMPLETE_SENTENCES  | 4       | 1        | "This is a broken sentence."

    }
}
