package uk.me.aujla.parse;


import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import uk.me.aujla.model.TextBlock;

import java.util.*;
import java.util.stream.Collectors;

class HtmlToTextBlocksContentHandler implements ContentHandler {

    private static final HtmlTagHandlerChain htmlTagHandlerChain = new HtmlTagHandlerChain();

    private List<TextBlock> textBlocks = new ArrayList<>();

    private StringBuilder htmlTagText = new StringBuilder();
    private StringBuilder innerHrefTagText = new StringBuilder();

    /*
    * Remembering what parent tag's text collection decision is, is important to
    * correctly collect wanted text but not unwanted text. This is where tag configuration
    * contexts are stored.
    *
    * <not-text-tag>
    *     Don't collect me 1
    *     <text-tag> Collect me! <text-tag>
    *   Don't collect me 2
    * </not-text-tag>
    */
    private Deque<HtmlTagConfiguration> htmlTagConfiguration = new LinkedList<>();

    private Deque<Set<String>> inheritedClassNames = new LinkedList<>();

    private boolean inHtmlBody = false;
    private boolean insideHref = false;


    @Override
    public void setDocumentLocator(Locator locator) {
        // not required to implement
    }

    @Override
    public void startDocument() throws SAXException {
        // not required to implement
    }

    @Override
    public void endDocument() throws SAXException {
        // not required to implement
    }


    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        // not required to implement
    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {
        // not required to implement
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        if("BODY".equals(qName)){
            inHtmlBody = true;
        }

        if("A".equals(qName)) {
            insideHref = true;
        }

        if(inHtmlBody) {
            if(!insideHref) {
                saveCurrentTextAsNewBlock();
            }
            inheritedClassNames.add(extractHtmlClassNames(atts.getValue("class")));
            htmlTagConfiguration.addFirst(htmlTagHandlerChain.getTagConfiguration(qName));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        if(inHtmlBody) {
            saveCurrentTextAsNewBlock();

            if(!htmlTagConfiguration.isEmpty()) {

                // discard current tag context
                htmlTagConfiguration.removeFirst();
                inheritedClassNames.removeFirst();
            }
        }

        if("A".equals(qName)) {
            insideHref = false;
        }
    }

    private void saveCurrentTextAsNewBlock() {
        if (htmlTagText.length() > 0) {
            String trimmedText = htmlTagText.toString().trim();
            if(trimmedText.length() > 0) {
                Set<String> flattenedClassNames = inheritedClassNames.stream().flatMap(set -> set.stream()).collect(Collectors.toSet());
                String innerHrefTextTrimmed = innerHrefTagText.toString().trim();
                textBlocks.add(new TextBlock(trimmedText, innerHrefTextTrimmed, flattenedClassNames));
            }
            htmlTagText = new StringBuilder();
            innerHrefTagText = new StringBuilder();
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if(inHtmlBody == true && htmlTagConfiguration.peekFirst().shouldCollectText()) {

            if(insideHref) {
                innerHrefTagText.append(ch, start, length);
            }

            htmlTagText.append(ch, start, length);
        }
    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        // not required to implement
    }

    @Override
    public void processingInstruction(String target, String data) throws SAXException {
        // not required to implement
    }

    @Override
    public void skippedEntity(String name) throws SAXException {

    }

    private Set<String> extractHtmlClassNames(String classNameString) {
        if(classNameString != null && classNameString.length()>0) {
            return new HashSet<>(Arrays.asList(classNameString.split(" ")));
        } else {
            return new HashSet<>();
        }
    }

    public List<TextBlock> getTextBlocks() {
        return textBlocks;
    }
}
