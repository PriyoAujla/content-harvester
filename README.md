# Content Harvest
#### A machine learning approach to scraping webpages

Welcome to the content harvester project. This project has started really as a learning exercise to trying out different
algorithms for classifying main webpage article text blocks and non article text blocks (navigation, comments section,
non article links, adverts etc).

The project builds upon other documented approaches in particular [boilerpipe](http://www.l3s.de/~kohlschuetter/boilerplate/) and the following Stanford [paper](http://cs229.stanford.edu/proj2013/YaoZuo-AMachineLearningApproachToWebpageContentExtraction.pdf).
Where this project differs is to use "over fitting" as an advantage to easily train a scraper on a particular website domain
without having to inspect the html and write xpath/css queries. This ensure you get exactly the text you want so long as
the structure of the page allows for it, if you can't write a css/xpath query to get exactly what you want for a given
page it's highly unlikely this tool will do any better.

#### Try it out

The module server is an example web application which allows you to create a distinct training set, train the classifier 
on that training set and use the classifier on arbitrary urls.

Navigate to the server folder and execute the following command

```
gradle run
```

The server will start on port 8338 and the index page can be reached by going to the following url

```
http://localhost:8338
```
