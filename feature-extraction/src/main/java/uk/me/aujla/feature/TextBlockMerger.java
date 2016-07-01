package uk.me.aujla.feature;

import uk.me.aujla.model.TextBlock;

import java.util.ArrayList;
import java.util.List;

public class TextBlockMerger implements TextBlockProcessor {


    @Override
    public List<TextBlock> process(List<TextBlock>  textBlocks) {

        List<TextBlock> result = new ArrayList<>();
        List<TextBlock> splitTextBlocks = new ArrayList<>();

        for(TextBlock currentTextBlock: textBlocks) {
            if(isSentenceSplit(currentTextBlock.getText())){

                splitTextBlocks.add(currentTextBlock);

            } else if(!splitTextBlocks.isEmpty()) {

                splitTextBlocks.add(currentTextBlock);

                if(hasFullStopBeforeNextSentence(currentTextBlock.getText())) {
                    result.add(mergeBlocks(splitTextBlocks));
                    splitTextBlocks.clear();
                }

            } else {
                result.add(currentTextBlock);
            }
        }

        return result;
    }

    private TextBlock mergeBlocks(List<TextBlock> splitTextBlocks) {
        TextBlock result = splitTextBlocks.remove(0);
        for (TextBlock textBlock: splitTextBlocks) {
            result.merge(textBlock);
        }

        return result;
    }

    private boolean hasFullStopBeforeNextSentence(String text) {
        if(Character.isUpperCase(text.charAt(0))) {
            return false;
        }

        boolean hasFullStopBeforeNextSentence = false;

        for(char character: text.toCharArray()) {
            if(character == '.'){
                hasFullStopBeforeNextSentence = true;
            }
        }

        return hasFullStopBeforeNextSentence;
    }

    private boolean isSentenceSplit(String text) {
        boolean sentenceSplit = false;
        for(char character: text.toCharArray()){
            if(sentenceSplit == true && character == '.') {
                sentenceSplit = false;
            }

            if(sentenceSplit == false) {
                sentenceSplit = Character.isUpperCase(character);
            }
        }
        return sentenceSplit;
    }
}
