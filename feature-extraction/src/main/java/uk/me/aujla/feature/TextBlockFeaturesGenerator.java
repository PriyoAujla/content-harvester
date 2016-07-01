package uk.me.aujla.feature;

import com.google.common.io.Resources;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import uk.me.aujla.model.TextBlock;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class TextBlockFeaturesGenerator {

    private SentenceModel englishLanguageModel;

    public TextBlockFeaturesGenerator() throws IOException {
        englishLanguageModel = new SentenceModel(Resources.getResource("en-sent.bin").openStream());
    }

    public List<TextBlockFeatures> create(List<? extends TextBlock> textBlocks, String textBlockIdPrefix) throws IOException {

        List<TextBlockFeatures> textBlockFeatures = new ArrayList<>();
        double previousTextBlockSize = 0;
        double nextTextBlockSize;
        for(int i = 0; i < textBlocks.size(); i++){
            TextBlock textBlock = textBlocks.get(i);
            if(i<textBlocks.size()-1) {
                nextTextBlockSize = countWords(textBlocks.get(i + 1).getText());
            } else {
                nextTextBlockSize = 0;
            }

            double relativePosition = ((double) i / textBlocks.size()) * 50;
            double textBlockSize = countWords(textBlock.getText());
            double previousTextBlockQuotient = previousTextBlockSize / textBlockSize;
            double nextTextBlockQuotient = nextTextBlockSize / textBlockSize;
            String textBlockId = textBlockIdPrefix + "-" + i;
            TextBlockFeatures blockFeatures = create(textBlock, textBlockId, relativePosition, previousTextBlockQuotient,
                    nextTextBlockQuotient);
            textBlockFeatures.add(blockFeatures);
            previousTextBlockSize = textBlockSize;
        }

        return textBlockFeatures;
    }

    private TextBlockFeatures create(TextBlock textBlock, String textBlockId,
                                     double relativePosition, double previousTextBlockQuotient, double nextTextBlockQuotient) throws IOException {

        String text = textBlock.getText();

        return new TextBlockFeatures(textBlockId,
                                     text,
                                     calculateWordDensity(text),
                                     calculateLinkDensity(textBlock.getInnerHrefTagText(), text),
                                     calculatePunctuationDensity(text),
                                     calculateCapitalisedWordsDensity(text),
                                     relativePosition,
                                     previousTextBlockQuotient,
                                     nextTextBlockQuotient,
                                     textBlock.getClassNames()
        );
    }

    /**
     * Word density is (total number of words in text block / total num of sentences).
     *
     * @param text - the data to calculate word density against
     * @return - word density value
     */
    double calculateWordDensity(String text) throws IOException {

        SentenceDetectorME sentenceDetector = new SentenceDetectorME(englishLanguageModel);
        double totalSentences = sentenceDetector.sentDetect(text).length;

        return countWords(text)/totalSentences;
    }

    /**
     * Link density is (total number of words inside any <a> child elements /  total num words in text block)
     *
     * @param wordsInHrefTag
     * @param wordsInTotal
     * @return
     */
    double calculateLinkDensity(String wordsInHrefTag, String wordsInTotal) {
        return countWords(wordsInHrefTag)/countWords(wordsInTotal);
    }

    /**
     * Punctuation density is (number of punctuation marks /  total num words in text block)
     *
     * @param text
     * @return
     */
    double calculatePunctuationDensity(String text) {
        Pattern pattern = Pattern.compile("[!\"#$%'\\(\\)\\*\\+,\\-\\./:;<=>\\?\\[\\]_`\\{\\}~]+");
        Matcher matcher = pattern.matcher(text);

        double occurenceOfPunctuationMarks = 0.00;
        while (matcher.find())
            occurenceOfPunctuationMarks++;

        return occurenceOfPunctuationMarks/countWords(text);
    }

    /**
     * Finds all distinct classnames from the given text blocks.
     *
     * @param textBlocks
     * @return - sorted set of all classname declarations found in each text block
     */
    Set<String> findAllClassNames(List<TextBlock> textBlocks) {
        return textBlocks.parallelStream()
                .flatMap(textBlock -> textBlock.getClassNames().stream())
                .collect(Collectors.toCollection(TreeSet::new));
    }

    /**
     * Capitalised words density is (number of words with capital letters / total number of words)
     * @param text
     * @return
     */
    double calculateCapitalisedWordsDensity(String text) {

        String[] words = text.replaceAll("\\n|\\r", " ").split(" |\\t");
        double numWordsStartingWithCapitalLetter = Arrays.stream(words)
                .filter(word -> isWordCapitalised(word))
                .collect(Collectors.toList()).size();

        return numWordsStartingWithCapitalLetter / words.length;
    }

    private boolean isWordCapitalised(String word) {
        String trimmedWord = word.trim();
        if(trimmedWord.length() > 0) {
            char character = trimmedWord.charAt(0);
            return Character.isUpperCase(character);
        } else {
            return false;
        }
    }


    /*
        Performs a simple split on string and will count non alphanumeric characters
        as a word.
     */
    private double countWords(String text) {
        if(text.isEmpty()){
            return 0.0;
        }
        return text.split(" ").length;
    }
}
