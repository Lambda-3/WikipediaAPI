# WikipediaAPI

## Building

Requirements:
* Java 8
* Maven

Optional requirement (but recommended)
* JWPL Pro dump accessible as SQL-Server

Build with 

    mvn package
    
Disable Unit tests with
    
    mvn package -DskipTests

## Usage as Library
Install first with (currently not in any maven repo) 

    mvn install
    
Then, in your code initialize a new `Wikipedia` instance
and get a new article:

    Config config = ConfigFactory.load() // load your config
    WikipediaAPI dump = new WikipediaFromDump(config);
    WikipediaArticle article = dump.fetchArticle("Passau");

Or you could query the live Wikipedia API (but be aware of rate limiting)

    Config config = ConfigFactory.load() // load your config
    WikipediaAPI live = new WikipediaFromAPI(config);
    WikipediaArticle article = live.fetchArticle("Passau");

You then have a few possibilities to interact with the article object.
    
    // get article's text
    String text = article.getText()
    
    // get article's title
    String title = article.getTitle()
  
    // get article's fetch-date
    Date date = article.getDate()
    
Small configuration can be done in `reference.conf`


## As a service

You can either build and run your own docker image:
    
    mvn clean -P server package
    docker build -t "wikiapi" -f wikipedia-rest/target/classes/Dockerfile-wikipedia .
    docker run \
        -p 8080:8080 \
        -v $(pwd)/conf:/usr/share/wikipedia/conf \
        -v $(pwd)/logs:/usr/share/wikipedia/logs \
        wikiapi

Or use the `docker-compose.yml` file to pull the prebuilt image from hub.docker.com
and run it immediately. If you do this, you only need this file and a config file in `conf/wikipedia.local.conf` and a `conf/logback.xml` to configure logging.

    docker-compose up

## Contributors (alphabetical order)
- Bernhard Bermeitinger
- Siegfried Handschuh
