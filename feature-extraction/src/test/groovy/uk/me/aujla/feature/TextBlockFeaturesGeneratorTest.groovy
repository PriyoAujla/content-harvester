package uk.me.aujla.feature

import spock.lang.Specification
import spock.lang.Unroll
import uk.me.aujla.model.TextBlock

class TextBlockFeaturesGeneratorTest extends Specification {

    def underTest = new TextBlockFeaturesGenerator()

    def "test word density is calculated successfully"() {
        given:
            def sentences = """
                Pierre Vinken, 61 years old, will join the board as a nonexecutive director Nov. 29. Mr. Vinken is
                chairman of Elsevier N.V., the Dutch publishing group. Rudolph Agnew, 55 years
                old and former chairman of Consolidated Gold Fields PLC, was named a director of this
                British industrial conglomerate.
            """
        when:
            def result = underTest.calculateWordDensity(sentences)
        then:
            result == 36.333333333333336
    }

    def "test set of all classname declarations is returned succesfully"() {
        given:

            def textBlocks = [
                    new TextBlock(null,null,["class1", "class2", "class3"] as Set),
                    new TextBlock(null,null,["class2"] as Set),
                    new TextBlock(null,null,["class3"] as Set)
            ]
        when:
            def result = underTest.findAllClassNames(textBlocks)
        then:
            result.size() == 3
    }

    @Unroll
    def "test link density is correctly calculated"() {
        when:
            def result = underTest.calculateLinkDensity(wordsInHref,wordsInTotal)
        then:
            result == expectedResult

        where:

            wordsInHref      | wordsInTotal                     | expectedResult
            "word1"          | "word1"                          | 1
            "w1 w2 w3 w4 w5" | "w1 w2 w3 w4 w5 w6 w7 w8 w9 w10" | 0.5
            "w1 w2 w3"       | "w1 w2 w3 w4 w5 w6 w7 w8 w9"     | 0.3333333333333333
    }


    /*
        Needs a better sentence which has all punctuation marks in it to properly test the regexp,
        I'm lazy so I can't be bothered right now.
     */
    def "test ratio of punctuation to words is correctly calculated"() {
        given:
            def sentences = """
                    Pierre Vinken, 61 years old, will join the board as a nonexecutive director Nov. 29. Mr. Vinken is
                    chairman of Elsevier N.V., the Dutch publishing group. Rudolph Agnew, 55 years
                    old and former chairman of Consolidated Gold Fields PLC, was named a director of this
                    British industrial conglomerate.
                """

        when:
            def result = underTest.calculatePunctuationDensity(sentences)
        then:
            result == 0.088
    }

    def "test ratio of capitalised words to words is correctly calculated"() {
        given:
            def text = "Capital words test\nCapital word on newline"

        when:
            def result = underTest.calculateCapitalisedWordsDensity(text)
        then:
            result == 0.2857142857142857
    }

    def "test text block is correctly transformed into feature vector"() {
        given:
            def classNames = ["class1", "class2"] as Set
            def textBlock = [new TextBlock("Hello World.", "", classNames)]
        when:
            def result = underTest.create(textBlock, "text-block-1")[0]
        then:
            result.classNames == classNames
            result.getWordDensity() == 2.0
            result.getLinkDensity() == 0.0
            result.getPunctuationDensity() == 0.5
            result.getCapitalisedWordsDensity() == 1.0
    }

    // todo add test for list of blocks

}
