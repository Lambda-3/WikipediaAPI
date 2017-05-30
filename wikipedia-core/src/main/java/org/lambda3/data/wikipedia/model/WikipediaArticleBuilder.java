/*
 * ==========================License-Start=============================
 * wikipedia-core : WikipediaArticleBuilder
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

import java.util.Date;

/**
 *
 */
public class WikipediaArticleBuilder {

    private String title;
    private Integer pageId;
    private String text;
    private Date date;

    public WikipediaArticleBuilder() {
    }

    public WikipediaArticleBuilder addTitle(String title) {
        this.title = title;
        return this;
    }

    public WikipediaArticleBuilder addPageId(Integer pageId) {
        this.pageId = pageId;
        return this;
    }

    public WikipediaArticleBuilder addText(String text) {
        this.text = text;
        return this;
    }

    public WikipediaArticleBuilder addDate(Date date) {
        this.date = date;
        return this;
    }

    String getTitle() {
        return title;
    }

    Integer getPageId() {
        return pageId;
    }

    String getText() {
        return text;
    }

    Date getDate() {
        return date;
    }

    public WikipediaArticle build() {
        return new WikipediaArticle(this);
    }
}
