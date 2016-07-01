<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Text block tagging</title>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">

    <script src="https://code.jquery.com/jquery-2.2.0.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js" integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/knockout/3.4.0/knockout-min.js"></script>

    <style>
      .alert-unknown {
          color: #4C4646;
          background-color:#D1D0CE;
          border-color: #B6B6B4;
        }
    </style>

</head>

<body style="height=80vh">

    <div class = "jumbotron">
        <label class="control-label col-sm-1" for="site-url">Site url:</label>
        <div class="col-sm-9">
            <input data-bind="value: siteUrl" type="text" class="form-control" id="site-url">

        </div>

    </div>

    <div class="col-sm-12" style="margin-bottom: 1%;">
        <button data-bind="click: unTaggedTextBlocksViewModel.markUntaggedNonText" type="button" class="btn btn-danger btn-xs">Mark Untagged Blocks As Not Article</button>
        <button type="button" class="btn btn-primary btn-xs" data-toggle="modal" data-target="#saveModal">Save Tagged Blocks</button>

        <div id="saveModal" class="modal fade" role="dialog">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h4 class="modal-title">Create new website classifier</h4>
                    </div>
                        <div class="modal-body">
                            <label for="name">Filename prefix:</label>
                            <input type="text" id="fileNamePrefix" name="fileNamePrefix">
                        </div>
                        <div class="modal-footer">
                            <button data-bind="click: saveBlocks" type="button" class="btn btn-default">Save</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                </div>

            </div>
        </div>
    </div>

    <div style = "color: #fff;">
        <div class = "row" style="height:80vh">
            <div class = "col-md-4" data-bind="foreach: textBlocks" style=" height: 100%; overflow: scroll;">
                    <div class="alert" data-bind="css: cssClass">
                        <div data-bind="text: text"></div>
                        <button data-bind="click: isArticleText" type="button" class="btn btn-success btn-xs">Article Text</button>
                        <button data-bind="click: isNonArticleText" type="button" class="btn btn-danger btn-xs">Not Article Text</button>
                    </div>
            </div>
            <div class = "col-md-8" style="height:100%">
                <iframe data-bind="attr: {src: siteUrl}" height="100%" width="100%"></iframe>
            </div>
        </div>
    </div>


<script>
    function TextBlockViewModel(textBlock, cssClass) {
        var self = this;

        this.textBlock = textBlock;
        this.text = textBlock.text;
        this.cssClass = ko.observable(cssClass);
        this.isTagged = false;
        this.isArticleTextBlock = false;
        this.isArticleText = function () {
            this.isTagged = true;
            this.isArticleTextBlock = true;
            self.cssClass("alert-success");
        };
        this.isNonArticleText = function () {
            this.isTagged = true;
            self.cssClass("alert-danger");
        };
    }

    function TextBlockEntity(textBlock, isArticleTextBlock) {
        var self = this;

        this.textBlock = textBlock;
        this.isArticleTextBlock = isArticleTextBlock;
    }

    function UnTaggedTextBlocks() {
        var self = this;

        this.textBlocks = ko.observableArray()
        this.addTextBlock = function(textBlock) {
            this.textBlocks.push(new TextBlockViewModel(textBlock, "alert-unknown"));
        }
        this.clear = function() {
            this.textBlocks.destroyAll();
        }
        this.markUntaggedNonText = function() {
            ko.utils.arrayForEach(this.textBlocks(), function(textBlock) {
                if(!textBlock.isTagged) {
                    textBlock.isNonArticleText();
                }
            });
        }
    }

    function SiteUrlViewModel() {
        var self = this;

        this.siteUrl = ko.observable("");
    }

    var unTaggedTextBlocksViewModel = new UnTaggedTextBlocks();
    var siteUrlViewModel = new SiteUrlViewModel();
    siteUrlViewModel.siteUrl.subscribe(function(url){
        if(url !== undefined || url !== null) {
            unTaggedTextBlocksViewModel.clear();
            var urlEncoded = encodeURI(url);
            $.get("/text-blocks?url=" + urlEncoded, function (data) {
                $.each(data, function (index, textBlock) {
                    unTaggedTextBlocksViewModel.addTextBlock(textBlock);
                });
            });
        }
    });

    ko.applyBindings({
        textBlocks: unTaggedTextBlocksViewModel.textBlocks,
        siteUrl: siteUrlViewModel.siteUrl
    });

    function saveBlocks() {
        var fileNamePrefix = $("#fileNamePrefix").val();
        var postUrl = "/text-blocks?websiteClassifierName=${websiteClassifierName}&dataType=${dataType}&fileNamePrefix="+fileNamePrefix;
        var taggedTextBlocks =  [];
        ko.utils.arrayForEach(unTaggedTextBlocksViewModel.textBlocks(), function(textBlockModel) {
            taggedTextBlocks.push(new TextBlockEntity(textBlockModel.textBlock, textBlockModel.isArticleTextBlock));
        });

        $.ajax({
            'type': 'POST',
            'url': postUrl,
            'contentType': 'application/json',
            'data': JSON.stringify(taggedTextBlocks),
            'dataType': 'json',
            'success': function(data) {}
        });

    }


</script>
</body>

</html>