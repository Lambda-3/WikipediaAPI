# WikipediaAPI

## Building

Requirements:
* Java 8
* Maven

Build with 

    mvn package
    
Disable Unit tests with
    
    mvn package -DskipTests

## Usage
Install first with (currently not in any maven repo) 

    mvn install
    
Then, in your code initialize a new `Wikipedia` instance
and get a new article:

    Wikipedia w = new Wikipedia();
    WikipediaArticle article = w.fetchArticle("Passau");

You then have a few possibilities to interact with the article object.
    
    // get article's text
    String text = article.getText()
    
    // get article's title
    String title = article.getTitle()
  
    // get article's fetch-date
    Date date = article.getDate()
    
Small configuration can be done in `reference.conf`

## Contributors (alphabetical order)
- Bernhard Bermeitinger
- Siegfried Handschuh
