package uk.me.aujla.parse

import com.google.common.io.Resources
import spock.lang.Specification

class TextBlocksExtractorTest extends Specification {

    def underTest = new TextBlocksExtractor()

    InputStream htmlPageStream = Resources.getResource("web-page.html").openStream()

    def "test all text blocks are correctly distinguished from each other"() {
        when:
            def textBlocks = underTest.create(htmlPageStream)
        then:
            textBlocks.size() == 29
            textBlocks[3].text.split(" ").size() == 26
            textBlocks[5].text.split(" ").size() == 21
            textBlocks[8].text.split(" ").size() == 24
            textBlocks[18].text.split(" ").size() == 23
            textBlocks[27].text.split(" ").size() == 1
    }


    def "test <a> tags data is added to parent text block"() {
        when:
            def textBlocks = underTest.create(htmlPageStream)

        then:
            textBlocks[9].text.split(" ").size() == 152
            textBlocks[9].innerHrefTagText.split(" ").size() == 65
            textBlocks[10].text.split(" ").size() == 49
            textBlocks[10].innerHrefTagText.split(" ").size() == 49
            textBlocks[24].text.split(" ").size() == 80
            textBlocks[24].innerHrefTagText.split(" ").size() == 23
    }

    def "test class name declarations are correctly extracted including all inherited class names from parent, grandparent etc"() {
        when:
            def textBlocks = underTest.create(htmlPageStream)

        then:
            textBlocks[7].classNames == ["ad", "first-p", "hlfmpu", "first-span"] as Set
    }

    def "test text block continuity is preserved and text is ordered as it appears"() {
        when:
            def textBlocks = underTest.create(htmlPageStream)

        then:
            textBlocks[25].text == "Life is really simple, but we insist on making it complicated."
            textBlocks[26].text == "Choose a job you love, and you will never have to work a day in your life."
            textBlocks[27].text == "Never give a sword to a man who can't dance."
            textBlocks[28].text == "Confucius"
    }
}
