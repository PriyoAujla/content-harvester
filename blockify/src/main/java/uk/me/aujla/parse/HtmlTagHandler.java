package uk.me.aujla.parse;


class HtmlTagHandler {

    private HtmlTagHandler nextTagHandler;

    private String tagName;
    private HtmlTagConfiguration htmlTagConfiguration;

    public HtmlTagHandler(HtmlTagHandler nextTagHandler,
                          String tagName,
                          HtmlTagConfiguration htmlTagConfiguration) {
        this.nextTagHandler = nextTagHandler;
        this.tagName = tagName;
        this.htmlTagConfiguration = htmlTagConfiguration;
    }

    public HtmlTagConfiguration getTagConfiguration(String tagName) {
        if(this.tagName.equals(tagName)){
            return htmlTagConfiguration;
        } else {
            return nextTagHandler.getTagConfiguration(tagName);
        }
    }
}
