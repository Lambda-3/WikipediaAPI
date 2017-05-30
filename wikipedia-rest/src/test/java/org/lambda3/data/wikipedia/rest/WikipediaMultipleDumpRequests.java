/*
 * ==========================License-Start=============================
 * wikipedia-rest : WikipediaMultipleDumpRequests
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

package org.lambda3.data.wikipedia.rest;

import org.lambda3.data.wikipedia.model.WikipediaArticle;
import org.lambda3.data.wikipedia.model.WikipediaArticleBuilder;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 *
 */
public class WikipediaMultipleDumpRequests extends ServerTest {

    @Test
    public void testFetchMultipleArticles() {
        String[] articles = {
                "George Washington", "John Adams", "Thomas Jefferson", "James Madison", "James Monroe",
                "John Quincy Adams", "Andrew Jackson", "Martin Van Buren", "William Henry Harrison", "John Tyler",
                "James K. Polk", "Zachary Taylor", "Millard Fillmore", "Franklin Pierce", "James Buchanan",
                "Abraham Lincoln", "Andrew Johnson", "Ulysses S. Grant", "Rutherford B. Hayes", "James A. Garfield",
                "Chester A. Arthur", "Grover Cleveland", "Benjamin Harrison", "Grover Cleveland", "William McKinley",
                "Theodore Roosevelt", "William Howard Taft", "Woodrow Wilson", "Warren G. Harding", "Calvin Coolidge",
                "Herbert Hoover", "Franklin D. Roosevelt", "Harry S. Truman", "Dwight D. Eisenhower",
                "John F. Kennedy", "Lyndon B. Johnson", "Richard Nixon", "Gerald Ford", "Jimmy Carter",
                "Ronald Reagan", "George H. W. Bush", "Bill Clinton", "George W. Bush", "Barack Obama", "Donald Trump"
        };

        for (String articleName : articles) {

            WikipediaArticle expectedArticle = new WikipediaArticleBuilder()
                    .addTitle(articleName)
                    .build();

            WikipediaRequest request = new WikipediaRequest();
            request.setArticleName(articleName);
            request.setSourceType(SourceType.DUMP);

            final Response response = target("/")
                    .path("article")
                    .queryParam("articleName", request.getArticleName())
                    .queryParam("sourceType", request.getSourceType())
                    .request(MediaType.TEXT_PLAIN)
                    .accept(MediaType.APPLICATION_JSON_TYPE)
                    .get();

            Assert.assertEquals(response.getStatus(), 200);
            Assert.assertTrue(response.hasEntity());

            WikipediaArticle actual = response.readEntity(WikipediaArticle.class);

            Assert.assertNotNull(actual);
            Assert.assertNotNull(actual.getText());
            Assert.assertTrue(actual.getText().length() > 1000);
            Assert.assertNotNull(actual.getPageId());
            Assert.assertTrue(actual.getPageId() > 0, "An article's page id is a positive integer.");
            Assert.assertNull(actual.getDate()); // articles from dump have no date

            Assert.assertEquals(actual.getTitle(), expectedArticle.getTitle());
        }

    }

}
