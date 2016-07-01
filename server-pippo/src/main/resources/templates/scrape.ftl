<#import "base.ftl" as base/>

<@base.content "Website Classifiers">

<div class = "jumbotron">
    <label class="control-label col-sm-1" for="site-url">Site url:</label>
    <div class="col-sm-9">
        <input data-bind="value: siteUrl" type="text" class="form-control" id="site-url">

    </div>

</div>

<div class="col-sm-12" style="margin-bottom: 1%;">

</div>

<div style = "color: #fff;">
    <div class = "row" style="height:80vh">
        <div class = "col-md-4"  style=" height: 100%; overflow: scroll;">
            <div data-bind="text: articleText" class="col-sm-offset-1" style="color: black; white-space:pre-wrap;"></div>
        </div>
        <div class = "col-md-8" style="height:100%">
            <iframe data-bind="attr: {src: siteUrl}" height="100%" width="100%"></iframe>
        </div>
    </div>
</div>

<script>

    function ArticleViewModel() {
        var self = this;

        this.articleText = ko.observable();
    }

    function SiteUrlViewModel() {
        var self = this;

        this.siteUrl = ko.observable("");
    }

    var articleViewModel = new ArticleViewModel();
    var siteUrlViewModel = new SiteUrlViewModel();
    siteUrlViewModel.siteUrl.subscribe(function(url){
        if(url !== undefined || url !== null) {
            var urlEncoded = encodeURI(url);
            $.ajax({
                'type': 'GET',
                'url': "/classifier/${websiteClassifierName}/scrape?url=" + urlEncoded,
                'contentType': 'application/json',
                'dataType': 'json',
                'success': function(data) {
                    console.log(data.text);
                    articleViewModel.articleText(data.text);

                }
            });
        }
    });

    ko.applyBindings({
        siteUrl: siteUrlViewModel.siteUrl,
        articleText: articleViewModel.articleText
    });

</script>

</@base.content>