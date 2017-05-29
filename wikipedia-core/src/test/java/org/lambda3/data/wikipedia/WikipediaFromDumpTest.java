/*
 * ==========================License-Start=============================
 * wikipedia-core : WikipediaFromDumpTest
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
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lambda3.data.wikipedia.exceptions.WikipediaAccessException;
import org.lambda3.data.wikipedia.exceptions.WikipediaArticleNotFoundException;
import org.lambda3.data.wikipedia.model.WikipediaArticle;

public class WikipediaFromDumpTest {

    private static WikipediaAPI wikipediaAPI;

    @BeforeClass
    public static void setup() {
        Config config = ConfigFactory.parseResources("wiki-api.local.conf");

        wikipediaAPI = new WikipediaFromDump(config);
    }

    @Test
    public void testFetchArticlePassau() throws WikipediaAccessException, WikipediaArticleNotFoundException {

        WikipediaArticle article = wikipediaAPI.fetchArticle("Passau");

        Assert.assertNotNull(article.getTitle());
        Assert.assertNotNull(article.getText());
        Assert.assertNull(article.getDate());

        Assert.assertEquals("Passau", article.getTitle());
        Assert.assertTrue(article.getText().length() > 1000);

    }

    @Test(expected = WikipediaArticleNotFoundException.class)
    public void testNonExistentArticle() throws WikipediaAccessException, WikipediaArticleNotFoundException {
        wikipediaAPI.fetchArticle("JunitException");
    }


}
