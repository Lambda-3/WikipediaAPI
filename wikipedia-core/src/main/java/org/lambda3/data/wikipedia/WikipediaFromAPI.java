/*
 * ==========================License-Start=============================
 * wikipedia-core : WikipediaFromAPI
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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonStreamParser;
import com.typesafe.config.Config;
import org.joda.time.DateTime;
import org.lambda3.data.wikipedia.exceptions.WikipediaAccessException;
import org.lambda3.data.wikipedia.exceptions.WikipediaArticleNotFoundException;
import org.lambda3.data.wikipedia.model.WikipediaArticle;
import org.lambda3.data.wikipedia.model.WikipediaArticleBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Date;
import java.util.zip.GZIPInputStream;


public class WikipediaFromAPI implements WikipediaAPI {

    private final static Logger log = LoggerFactory.getLogger(WikipediaFromAPI.class);

    private final static String PARAMETER = "?action=query&prop=extracts|info&format=json&explaintext=1&titles=";
    private static final String ENCODING = "UTF-8";

    private String apiUrl;
    private String userAgent;

    public WikipediaFromAPI(Config config) {
        this.apiUrl = "https://" + config.getString("wiki-api.api.url") + "/w/api.php";
        this.userAgent = config.getString("wiki-api.api.user-agent");

        log.info("WikipediaFromAPI module initialized with URL: {}", this.apiUrl);
    }

    private static Date convertDate(String dateString) {
        return new DateTime(dateString).toDate();
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
        try {
            return internalFetchArticle(title);
        } catch (MalformedURLException | UnsupportedEncodingException e) {
            throw new WikipediaAccessException();
        } catch (WikipediaArticleNotFoundException e) {
            throw new WikipediaArticleNotFoundException();
        }
    }

    private WikipediaArticle internalFetchArticle(String title)
            throws UnsupportedEncodingException, MalformedURLException, WikipediaArticleNotFoundException {

        StringBuilder sb = new StringBuilder(this.apiUrl);
        sb.append(PARAMETER);
        sb.append(URLEncoder.encode(title, ENCODING));

        URL url = new URL(sb.toString());

        log.info("Making request to '{}'", sb.toString());
        try {
            URLConnection connection = url.openConnection();
            connection.addRequestProperty("Accept-encoding", "gzip");
            connection.addRequestProperty("User-agent", this.userAgent);
            connection.connect();

            BufferedReader in =
                    new BufferedReader(
                            new InputStreamReader(
                                    new GZIPInputStream(connection.getInputStream()), ENCODING));


            JsonElement jsonTree = null;
            JsonStreamParser jsonParser = new JsonStreamParser(in);

            while (jsonParser.hasNext()) {
                jsonTree = jsonParser.next();
            }

            if (jsonTree == null) {
                throw new WikipediaArticleNotFoundException();
            }

            JsonObject response = jsonTree.getAsJsonObject();

            JsonObject article = response.get("query").getAsJsonObject();
            article = article.get("pages").getAsJsonObject();
            article = article.entrySet().iterator().next().getValue().getAsJsonObject();

            if (!(article.has("title")
                    && article.has("extract")
                    && article.has("touched"))) {
                throw new WikipediaArticleNotFoundException();
            }


            return new WikipediaArticleBuilder()
                    .addPageId(article.get("pageid").getAsInt())
                    .addTitle(article.get("title").getAsString())
                    .addText(article.get("extract").getAsString())
                    .addDate(convertDate(article.get("touched").getAsString()))
                    .build();

        } catch (UnsupportedEncodingException e) {
            log.error("The given encoding is not supported", e);
        } catch (IOException e) {
            log.error("Something else has happened", e);
        }

        return null;
    }

}
