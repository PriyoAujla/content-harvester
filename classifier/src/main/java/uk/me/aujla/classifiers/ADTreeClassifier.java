package uk.me.aujla.classifiers;

import uk.me.aujla.Dataset;
import uk.me.aujla.feature.TextBlockFeatures;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.ADTree;
import weka.core.*;

import java.io.Serializable;
import java.util.Collection;

public class ADTreeClassifier implements TextBlockClassifier, Serializable {

    private static final int FEATURE_VECTOR_SIZE = 7;

    public static final String WORD_DENSITY = "wordDensity";
    public static final String LINK_DENSITY = "linkDensity";
    public static final String PUNCTUATION_DENSITY = "punctuationDensity";
    public static final String CAPITALISED_WORDS_DENSITY = "capitalisedWordsDensity";
    public static final String RELATIVE_POSITION = "relativePosition";
    public static final String PREVIOUS_TEXT_BLOCK_QUOTIENT = "previousTextBlockQuotient";
    public static final String NEXT_TEXT_BLOCK_QUOTIENT = "nextTextBlockQuotient";

    public static final String ARTICLE_TEXT_BLOCK_CLASS_ATTRIBUTE = "article-text-block";
    public static final String NON_ARTICLE_TEXT_BLOCK_CLASS_ATTRIBUTE = "non-article-text-block";
    public static final String THE_CLASS_ATTRIBUTE = "theClass";

    private FastVector featureVector = new FastVector(FEATURE_VECTOR_SIZE);
    private ADTree model = new ADTree();
    private Instances trainingSet;


    public ADTreeClassifier() {
        Attribute wordDensity = new Attribute(WORD_DENSITY);
        Attribute linkDensity = new Attribute(LINK_DENSITY);
        Attribute punctuationDensity = new Attribute(PUNCTUATION_DENSITY);
        Attribute capitalisedWordsDensity = new Attribute(CAPITALISED_WORDS_DENSITY);
        Attribute relativePosition = new Attribute(RELATIVE_POSITION);
        Attribute previousTextBlockQuotient = new Attribute(PREVIOUS_TEXT_BLOCK_QUOTIENT);
        Attribute nextTextBloxkQuotient = new Attribute(NEXT_TEXT_BLOCK_QUOTIENT);


        FastVector classVector = new FastVector(2);
        classVector.addElement(ARTICLE_TEXT_BLOCK_CLASS_ATTRIBUTE);
        classVector.addElement(NON_ARTICLE_TEXT_BLOCK_CLASS_ATTRIBUTE);
        Attribute classAttribute = new Attribute(THE_CLASS_ATTRIBUTE, classVector);

        featureVector.addElement(classAttribute);
        featureVector.addElement(wordDensity);
        featureVector.addElement(linkDensity);
        featureVector.addElement(punctuationDensity);
        featureVector.addElement(capitalisedWordsDensity);
        featureVector.addElement(relativePosition);
        featureVector.addElement(previousTextBlockQuotient);
        featureVector.addElement(nextTextBloxkQuotient);
    }

    @Override
    public void train(Dataset dataset) throws Exception {

        trainingSet = createInstances(dataset);

        model.buildClassifier(trainingSet);
    }

    private Instances createInstances(Dataset dataset) {


        Instances trainingSet = new Instances("training-dataset", featureVector, 10);
        trainingSet.setClassIndex(0);

        addInstanceVectors(trainingSet, dataset.getArticleTextBlocks(), ARTICLE_TEXT_BLOCK_CLASS_ATTRIBUTE);
        addInstanceVectors(trainingSet, dataset.getNonArticleTextBlocks(), NON_ARTICLE_TEXT_BLOCK_CLASS_ATTRIBUTE);
        return trainingSet;
    }

    private void addInstanceVectors(Instances instances,
                                    Collection<TextBlockFeatures> textBlockFeatures,
                                    String classificattion)  {

        for(TextBlockFeatures textBlockFeature: textBlockFeatures) {
            Instance instance = createInstance(classificattion, textBlockFeature);

            instances.add(instance);
        }

    }

    private Instance createInstance(String classification, TextBlockFeatures textBlockFeature) {
        Instance instance = new SparseInstance(FEATURE_VECTOR_SIZE);
        instance.setValue((Attribute)featureVector.elementAt(0), classification);
        instance.setValue((Attribute)featureVector.elementAt(1), textBlockFeature.getWordDensity());
        instance.setValue((Attribute)featureVector.elementAt(2), textBlockFeature.getLinkDensity());
        instance.setValue((Attribute)featureVector.elementAt(3), textBlockFeature.getPunctuationDensity());
        instance.setValue((Attribute)featureVector.elementAt(4), textBlockFeature.getCapitalisedWordsDensity());
        instance.setValue((Attribute)featureVector.elementAt(5), textBlockFeature.getRelativePosition());
        instance.setValue((Attribute)featureVector.elementAt(6), textBlockFeature.getPreviousTextBlockQuotient());
        instance.setValue((Attribute)featureVector.elementAt(7), textBlockFeature.getNextTextBlockQuotient());
        return instance;
    }

    public void evaluate(Dataset dataset) throws Exception {
        Instances testingSet = createInstances(dataset);

        Evaluation evaluation = new Evaluation(trainingSet);
        evaluation.evaluateModel(model, testingSet);

        String strSummary = evaluation.toSummaryString();

        System.out.println(strSummary);
        System.out.println(model);
    }

    public String classify(TextBlockFeatures textBlockFeatures) throws Exception {

        Instance instance = createInstance(ARTICLE_TEXT_BLOCK_CLASS_ATTRIBUTE, textBlockFeatures);
        instance.setDataset(trainingSet);
        instance.setClassMissing();


        double predictionValue = model.classifyInstance(instance);
        return trainingSet.classAttribute().value((int) predictionValue);
    }

    @Override
    public boolean isArticleText(TextBlockFeatures textBlockFeatures) throws Exception {
        return ARTICLE_TEXT_BLOCK_CLASS_ATTRIBUTE.equals(classify(textBlockFeatures));
    }

    public String printClassifier() {
        return model.toString();
    }
}
