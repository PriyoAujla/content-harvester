<#ftl encoding='UTF-8'>

# enforce datetime format, different jvm versions will choose a different datetime format, it's not deterministic
# from one jvm to another
<#setting datetime_format="dd-MMM-yyyy HH:mm:ss">
<#setting number_format=",###">

<#macro content title jsAssets="" cssAssets="">

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>${title}</title>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">
    ${cssAssets}

    <script src="https://code.jquery.com/jquery-2.2.0.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js" integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/knockout/3.4.0/knockout-min.js"></script>
    ${jsAssets}
</head>
<body>

    <#nested />

</body>
</html>

</#macro>