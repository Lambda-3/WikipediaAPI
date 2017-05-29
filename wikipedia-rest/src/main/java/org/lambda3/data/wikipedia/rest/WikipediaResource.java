/*
 * ==========================License-Start=============================
 * wikipedia-rest : WikipediaResource
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

import com.typesafe.config.Config;
import org.lambda3.data.wikipedia.WikipediaAPI;
import org.lambda3.data.wikipedia.WikipediaFromAPI;
import org.lambda3.data.wikipedia.WikipediaFromDump;
import org.lambda3.data.wikipedia.exceptions.WikipediaAccessException;
import org.lambda3.data.wikipedia.exceptions.WikipediaArticleNotFoundException;
import org.lambda3.data.wikipedia.model.WikipediaArticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 */
@Singleton
@Path("/article")
public class WikipediaResource {
    private Config config;
    private Logger log = LoggerFactory.getLogger(getClass());

    private WikipediaAPI liveResource;
    private WikipediaAPI dumpResource;

    WikipediaResource(Config config) {
        this.config = config;

        liveResource = new WikipediaFromAPI(config);
        dumpResource = new WikipediaFromDump(config);
    }

    @GET
    @Consumes({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getArticleContent(@Valid @BeanParam WikipediaRequest request) {

        try {
            WikipediaArticle article = null;
            switch (request.getSourceType()) {
                case DUMP:
                    log.debug("Requesting article '{}' from live API.", request.getArticleName());
                    article = this.dumpResource.fetchArticle(request.getArticleName());
                    break;
                case LIVE:
                    log.debug("Requesting article '{}' from dump API.", request.getSourceType());
                    article = this.liveResource.fetchArticle(request.getArticleName());
                    break;
            }
            log.debug("Article found and loaded, sending response.");
            return Response
                    .status(Response.Status.OK)
                    .entity(article)
                    .build();
        } catch (WikipediaAccessException e) {

            log.error("Wikipedia could not be reached, request was: article_name={}, type={}", request.getArticleName(), request.getSourceType());
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Wikipedia could not be reached.")
                    .build();

        } catch (WikipediaArticleNotFoundException e) {

            log.info("User requested article that was not found: article_name={}, type={}", request.getArticleName(), request.getSourceType());
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("The article was not found.")
                    .build();
        }

    }

}
