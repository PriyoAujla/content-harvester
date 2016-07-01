package uk.me.aujla.parse;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

class HtmlTagHandlerChain {

    private HtmlTagHandler beginningOfChain = new DefaultHtmlTagHandler();

    public HtmlTagHandlerChain() {
        Set<String> nonTextBlockHtmlTags = ImmutableSet.of(
                "SCRIPT",
                "STYLE",
                "OPTION",
                "OBJECT",
                "EMBED",
                "APPLET",
                "LINK",
                "IMG"
        );

        for(String tagName: nonTextBlockHtmlTags) {
            beginningOfChain = new HtmlTagHandler(beginningOfChain, tagName, new HtmlTagConfiguration(false));
        }
    }

    public HtmlTagConfiguration getTagConfiguration(String tagName) {
        return beginningOfChain.getTagConfiguration(tagName);
    }
}
