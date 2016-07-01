package net.smartology.server.routes;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.smartology.server.models.TaggedTextBlock;
import net.smartology.server.services.ClassifierService;
import net.smartology.server.services.WebsiteScraperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.pippo.core.Application;
import uk.me.aujla.WebpageBlockifier;
import uk.me.aujla.io.ClassifiedBlocks;
import uk.me.aujla.io.ClassifiedBlocksWriter;
import uk.me.aujla.io.ClassifierPaths;
import uk.me.aujla.model.ClassifiedTextBlock;
import uk.me.aujla.model.TextBlock;
import uk.me.aujla.parse.TextBlocksExtractor;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WebApp extends Application {

    public static final Logger LOGGER = LoggerFactory.getLogger(WebApp.class);

    private ClassifierService classifierService = new ClassifierService();


    @Override
    protected void onInit() {

        GET("/", (routeContext) -> {
            WebsiteScraperService websiteScraperService = new WebsiteScraperService();

            routeContext.setLocal("websiteClassifiers", websiteScraperService.findAll());
            routeContext.render("website-classifiers");
        });

        POST("/", (routeContext) -> {
            String name = routeContext.getParameter("name").toString();
            WebsiteScraperService websiteScraperService = new WebsiteScraperService();

            websiteScraperService.create(name);
            routeContext.redirect("/");
        });

        GET("/website-classifier/{name}/{dataType}/tag", (routeContext) -> {
            String name = routeContext.getParameter("name").toString();
            String dataType = routeContext.getParameter("dataType").toString();

            routeContext.setLocal("websiteClassifierName", name);
            routeContext.setLocal("dataType", dataType);
            routeContext.render("text-block-tagging");
        });

        GET("/text-blocks", (routeContext) -> {
            String url = routeContext.getParameter("url").toString();
            WebpageBlockifier webpageBlockifier = new WebpageBlockifier();
            List<TextBlock> textBlocks = new ArrayList<TextBlock>();
            try {
                textBlocks = webpageBlockifier.blockify(url);
            } catch (IOException e) {
                LOGGER.error("Unable to create text blocks for ul [{}]", url, e);
            }

            routeContext.json().send(textBlocks);

        });

        POST("/text-blocks", (routeContext) -> {
            String name = routeContext.getParameter("websiteClassifierName").toString();
            String dataType = routeContext.getParameter("dataType").toString();
            String fileNamePrefix = routeContext.getParameter("fileNamePrefix").toString();

            String json = routeContext.getRequest().getBody();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Type listType = new TypeToken<ArrayList<TaggedTextBlock>>() {}.getType();
            List<TaggedTextBlock> taggedTextBlocks = gson.fromJson(json, listType);

            File classifiersFolder = new File(WebsiteScraperService.PATH_TO_CLASSIFIERS_FOLDER);
            ClassifierPaths classifierPaths = new ClassifierPaths(classifiersFolder);
            ClassifiedBlocksWriter classifiedBlocksWriter = new ClassifiedBlocksWriter(classifierPaths);

            List<ClassifiedTextBlock> classifiedTextBlocks = taggedTextBlocks.stream()
                    .map(b -> new ClassifiedTextBlock(b.getTextBlock(), b.isArticleTextBlock()))
                    .collect(Collectors.toList());

            ClassifiedBlocks classifiedBlocks = new ClassifiedBlocks(fileNamePrefix, classifiedTextBlocks);

            if ("training".equals(dataType)) {
                try {
                    classifiedBlocksWriter.writeTraining(classifiedBlocks, name);
                } catch (IOException e) {
                    LOGGER.error("Unable to save training data for classifier [{}]", name, e);
                }
            } else {
                try {
                    classifiedBlocksWriter.writeTest(classifiedBlocks, name);
                } catch (IOException e) {
                    LOGGER.error("Unable to save test data for classifier [{}]", name, e);
                }
            }

            routeContext.send("");

        });

        POST("/classifier/{websiteClassifierName}/train", (routeContext) -> {
            String name = routeContext.getParameter("websiteClassifierName").toString();

            try {
                classifierService.trainAndSave(name);
            } catch (Exception e) {
                LOGGER.error("Unable to train classifier [{}]", name, e);
            }

            routeContext.send("");
        });

        GET("/classifier/{websiteClassifierName}/scrape", (routeContext) -> {
            TextBlocksExtractor textBlocksExtractor = new TextBlocksExtractor();

            String name = routeContext.getParameter("websiteClassifierName").toString();
            String url = routeContext.getParameter("url").toString();

            List<TextBlock> textBlocks = null;
            try {
                textBlocks = textBlocksExtractor.create(new URL(url).openStream());
            } catch (Exception e) {
                LOGGER.error("Unable to convert webpage [{}] to text blocks", url, e);
            }

            String text = null;
            try {
                text = classifierService.getText(name, textBlocks);
            } catch (Exception e) {
                LOGGER.error("Unable to extract article text from text blocks from [{}] using classifiers [{}]", url, name, e);
            }

            routeContext.json().send(new ArticleText(text));
        });

        GET("/scrape/{websiteClassifier}", (routeContext) -> {
            String name = routeContext.getParameter("websiteClassifier").toString();

            routeContext.setLocal("websiteClassifierName", name);
            routeContext.render("scrape");
        });

    }

    public static class ArticleText {
        private String text;

        public ArticleText(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }
}
