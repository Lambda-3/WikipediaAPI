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

import java.text.DateFormat;
import java.util.Date;

/**
 * Represents an article from WikipediaAPI.
 */
public class WikipediaArticle {

    private final static Gson GSON = new GsonBuilder().create();

    private final String title;
    private final String text;
    private final Date date;
    private final Integer pageId;

    public WikipediaArticle(String title, String text) {
        this(title, text, null);
    }

    public WikipediaArticle(String title, String text, Date date) {
        this(title, text, null, null);
    }

    public WikipediaArticle(String title, String text, Date date, Integer pageId) {
        this.title = title;
        this.text = text;
        this.date = date;
        this.pageId = pageId;
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
                ", text='" + text + '\'' +
                ", date=" + DateFormat.getDateTimeInstance().format(date) +
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
