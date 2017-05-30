/*
 * ==========================License-Start=============================
 * wikipedia-rest : WikipediaSingleDumpRequest
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
public class WikipediaSingleDumpRequest extends ServerTest {

    @Test
    public void testRequestPassauArticleFromDump() {

        WikipediaArticle expectedArticle = new WikipediaArticleBuilder()
                .addTitle("Passau")
                .addPageId(227880)
                .build();

        WikipediaRequest request = new WikipediaRequest();
        request.setArticleName("Passau");
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
        Assert.assertNull(actual.getDate()); // articles from dump have no date

        Assert.assertEquals(actual.getTitle(), expectedArticle.getTitle());
        Assert.assertEquals(actual.getPageId(), expectedArticle.getPageId());
    }

}
