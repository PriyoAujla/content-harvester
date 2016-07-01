package uk.me.aujla.feature;

import uk.me.aujla.model.TextBlock;

import java.util.List;

public interface TextBlockProcessor {

    List<TextBlock> process(List<TextBlock> textBlocks);
}
