/*
 * ==========================License-Start=============================
 * wikipedia-core : WikipediaFromDump
 *
 * Copyright © 2017 Lambda³
 *
 * MIT License
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * ==========================License-End==============================
 */

package org.lambda3.data.wikipedia;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import de.tudarmstadt.ukp.wikipedia.api.DatabaseConfiguration;
import de.tudarmstadt.ukp.wikipedia.api.Page;
import de.tudarmstadt.ukp.wikipedia.api.WikiConstants;
import de.tudarmstadt.ukp.wikipedia.api.Wikipedia;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiApiException;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiInitializationException;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import de.tudarmstadt.ukp.wikipedia.parser.mediawiki.FlushTemplates;
import de.tudarmstadt.ukp.wikipedia.parser.mediawiki.MediaWikiParser;
import de.tudarmstadt.ukp.wikipedia.parser.mediawiki.MediaWikiParserFactory;
import org.lambda3.data.wikipedia.exceptions.WikipediaAccessException;
import org.lambda3.data.wikipedia.exceptions.WikipediaArticleNotFoundException;
import org.lambda3.data.wikipedia.model.WikipediaArticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 */
public class WikipediaFromDump implements WikipediaAPI {

    private final static Logger log = LoggerFactory.getLogger(WikipediaFromDump.class);

    private final Config config;

    private final Wikipedia wiki;
    private final MediaWikiParser parser;

    public WikipediaFromDump(Config config) {

        this.config = config
                .withFallback(ConfigFactory.load())
                .getConfig("wiki-api.jwpl");

        try {
            this.wiki = connect(this.config);
        } catch (WikiInitializationException e) {
            log.error("JWPL could not establish database connection.", e);
            throw new RuntimeException("Could not connect to database.");
        }

        MediaWikiParserFactory pf = new MediaWikiParserFactory();
        pf.setTemplateParserClass(FlushTemplates.class);
        pf.getImageIdentifers().add("Image");

        this.parser = pf.createParser();

        log.info("WikipediaFromDump module initialized by using JWPL database dump");
    }

    private static Wikipedia connect(Config config) throws WikiInitializationException {
        DatabaseConfiguration dbConfig = new DatabaseConfiguration();
        dbConfig.setHost(config.getString("host"));
        dbConfig.setDatabase(config.getString("name"));
        dbConfig.setUser(config.getString("user"));
        dbConfig.setPassword(config.getString("password"));
        dbConfig.setLanguage(WikiConstants.Language.valueOf(config.getString("language")));
        Wikipedia wiki = new Wikipedia(dbConfig);

        log.info("Database connection established.");

        return wiki;
    }

    /**
     * Returns an instance of WikipediaArticle with the given title as the title.
     * Return null if something else happens which didn't trigger an internal Exception.
     *
     * @param title Title of the WikipediaFromAPI article to get.
     * @return instance of WikipediaArticle corresponding to the given title
     * @throws WikipediaArticleNotFoundException Thrown if the title is not found within WikipediaFromAPI
     * @throws WikipediaAccessException          Thrown if the connection to WikipediaFromAPI breaks.
     */
    @Override
    public WikipediaArticle fetchArticle(String title) throws WikipediaArticleNotFoundException, WikipediaAccessException {

        final Page page;
        try {
            page = wiki.getPage(title);
        } catch (WikiApiException e) {
            throw new WikipediaArticleNotFoundException();
        }

        try {
            String plainText = page.getPlainText();

            ParsedPage parsedPage = parser.parse(plainText);
            String articleText = parsedPage.getText();

            return new WikipediaArticle(page.getTitle().getPlainTitle(), articleText, null, page.getPageId());

        } catch (WikiApiException e) {
            log.error("The article could not be loaded.", e);
            throw new WikipediaAccessException();
        }

    }


}
