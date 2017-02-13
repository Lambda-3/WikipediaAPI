/*
 * ==========================License-Start=============================
 * wiki-api : WikipediaTest
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

import org.junit.Assert;
import org.junit.Test;
import org.lambda3.data.wikipedia.exceptions.WikipediaAccessException;
import org.lambda3.data.wikipedia.exceptions.WikipediaArticleNotFoundException;
import org.lambda3.data.wikipedia.model.WikipediaArticle;

import java.util.Date;

public class WikipediaTest {

    @Test
    public void testFetchArticlePassau() throws WikipediaAccessException, WikipediaArticleNotFoundException {
        Wikipedia w = new Wikipedia();
        WikipediaArticle article = w.fetchArticle("Passau");

        Assert.assertNotNull(article.getTitle());
        Assert.assertNotNull(article.getText());
        Assert.assertNotNull(article.getDate());

        Assert.assertEquals("Passau", article.getTitle());
        Assert.assertTrue(article.getText().length() > 1000);
        Assert.assertTrue(article.getDate().before(new Date()));

    }

    @Test(expected = WikipediaArticleNotFoundException.class)
    public void testNonExistentArticle() throws WikipediaAccessException, WikipediaArticleNotFoundException {
        Wikipedia w = new Wikipedia();
        w.fetchArticle("JunitException");
    }


}