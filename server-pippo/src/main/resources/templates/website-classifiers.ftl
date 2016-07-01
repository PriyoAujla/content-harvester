<#import "base.ftl" as base/>

<@base.content "Website Classifiers">

<div class = "jumbotron ">
</div>

<div class="container">

    <div class="row">
        <div class="col-md-offset-11">
            <button type="button" class="btn btn-primary btn-sm" data-toggle="modal" data-target="#createNewModal">Create</button>

            <div id="createNewModal" class="modal fade" role="dialog">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h4 class="modal-title">Create new website classifier</h4>
                        </div>
                        <form role="form" action="/" method="post">
                            <div class="modal-body">
                                <label for="name">Name:</label>
                                <input type="text" id="name" name="name">
                            </div>
                            <div class="modal-footer">
                                <button type="submit" class="btn btn-default">Submit</button>
                                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                            </div>
                        </form>
                    </div>

                </div>
            </div>
        </div>
    </div>

    <div class="row" style="padding-top: 1%;">

        <div>
            <#list websiteClassifiers as classifier>
                <div class="panel panel-default">
                    <div class="panel-heading">
                        ${classifier.name}
                    </div>
                    <div class="panel-body">
                        <a href="/website-classifier/${classifier.name}/training/tag" class="btn btn-primary btn-sm">Add new training data</a>
                        <a href="/website-classifier/${classifier.name}/test/tag" type="button" class="btn btn-primary btn-sm">Add new test data</a>
                        <button data-bind="click: trainClassifier.bind($data, '${classifier.name}')" type="button" class="btn btn-primary btn-sm">Train classifier</button>
                        <a role="button" href="/scrape/${classifier.name}" class="btn btn-primary btn-sm <#if !classifier.classifierFilePresent>disabled</#if>">Scrape</a>

                    </div>

                </div>
            <#else>
                <span> No classifiers found for any websites, please check the training folder </span>
            </#list>
        </div>
    </div>
</div>

<script>

    var viewModel = {
        trainClassifier: function(name) {
            $.ajax({
                'type': 'POST',
                'url': '/classifier/'+name+'/train',
                'contentType': 'application/json',
                'dataType': 'json',
                'success': function(data) {}
            });
        }
    };
    ko.applyBindings(viewModel);

</script>

</@base.content>