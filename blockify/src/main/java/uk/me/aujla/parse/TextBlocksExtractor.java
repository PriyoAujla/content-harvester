package uk.me.aujla.parse;

import org.apache.xerces.xni.parser.XMLDocumentFilter;
import org.cyberneko.html.filters.Purifier;
import org.cyberneko.html.parsers.SAXParser;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import uk.me.aujla.model.TextBlock;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Uses nekoHtml to parse the HTML, ensuring that text which is broken up between html container tags like div, span etc
 * are still ordered correctly when turned into text blocks. For example
 *
 * <div>
 *     Hello
 *     <span>
 *
 *         World.
 *
 *          <p>nekoHtml</p>
 *
 *     </span>
 *
 *     text blocks rock!
 * </div>
 *
 * Is 4 text blocks;
 *  1. Hello
 *  2. World.
 *  3. nekoHtml
 *  4. text blocks rock!
 */
public class TextBlocksExtractor {

    public List<TextBlock> create(InputStream inputStream) throws SAXException, IOException {

        // TODO create neko html provider
        SAXParser saxParser = new SAXParser();

        XMLDocumentFilter[] filters = { new Purifier()};
        saxParser.setProperty("http://cyberneko.org/html/properties/filters", filters);

        HtmlToTextBlocksContentHandler contentHandler = new HtmlToTextBlocksContentHandler();
        saxParser.setContentHandler(contentHandler);

        InputSource inputSource = new InputSource(inputStream);
        inputSource.setEncoding(StandardCharsets.UTF_8.toString());
        saxParser.parse(inputSource);

        return contentHandler.getTextBlocks();
    }
}
