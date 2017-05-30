/*
 * ==========================License-Start=============================
 * wikipedia-core : WikipediaArticle
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

package org.lambda3.data.wikipedia.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

/**
 * Represents an article from WikipediaAPI.
 */
public class WikipediaArticle {

    private final static Gson GSON = new GsonBuilder().create();

    private String title;
    private String text;
    private Date date;
    private Integer pageId;

    // for json deserializer
    private WikipediaArticle() {
    }

    WikipediaArticle(WikipediaArticleBuilder builder) {
        this.title = builder.getTitle();
        this.text = builder.getText();
        this.date = builder.getDate();
        this.pageId = builder.getPageId();
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public Date getDate() {
        return date;
    }

    public Integer getPageId() {
        return pageId;
    }

    @Override
    public String toString() {
        return "WikipediaArticle{" +
                "title='" + title + '\'' +
                ", pageId='" + pageId + '\'' +
                ", text='" + text + '\'' +
                '}';
    }


    /**
     * Returns this WikipediaFromAPI as a JSON-encoded string.
     *
     * @return This article as JSON
     */
    public String toJSON() {
        return GSON.toJson(this);
    }

}
