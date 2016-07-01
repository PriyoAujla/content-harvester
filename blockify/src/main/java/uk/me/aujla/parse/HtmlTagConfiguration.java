package uk.me.aujla.parse;

class HtmlTagConfiguration {

    private final boolean shouldCollectText;

    public HtmlTagConfiguration(boolean shouldCollectText) {
        this.shouldCollectText = shouldCollectText;
    }

    public boolean shouldCollectText() {
        return shouldCollectText;
    }
}
