package uk.me.aujla.parse;

class DefaultHtmlTagHandler extends HtmlTagHandler {

    public DefaultHtmlTagHandler() {
        super(null, null, null);
    }

    @Override
    public HtmlTagConfiguration getTagConfiguration(String tagName) {
        return new HtmlTagConfiguration(true);
    }
}
