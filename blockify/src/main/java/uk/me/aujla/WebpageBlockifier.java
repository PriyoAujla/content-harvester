package uk.me.aujla;

import uk.me.aujla.exceptions.BlockifyException;
import uk.me.aujla.model.TextBlock;
import uk.me.aujla.parse.TextBlocksExtractor;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class WebpageBlockifier {

    private TextBlocksExtractor textBlocksExtractor = new TextBlocksExtractor();

    public List<TextBlock> blockify(String url) throws IOException {
        return blockify(new URL(url).openStream());
    }

    public List<TextBlock> blockify(InputStream inputStream)  {

        try(BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {

            return textBlocksExtractor.create(inputStream);

        } catch (Exception e) {
            throw new BlockifyException("Unable to parse html into textblocks",e);
        }
    }
}
