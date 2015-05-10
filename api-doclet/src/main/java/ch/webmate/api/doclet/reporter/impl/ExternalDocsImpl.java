/*
 * Copyright (c) 2015 Sven Tschui
 *
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
 */

package ch.webmate.api.doclet.reporter.impl;

import ch.webmate.api.doclet.reporter.model.ExternalDocs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sventschui on 09.05.15.
 */
public class ExternalDocsImpl implements ExternalDocs {

    protected static final Pattern URL_PATTERN = Pattern.compile("\\A\\[([^\\]]*)\\]\\(([^\\)]*)\\)\\z");

    protected final String description;

    protected final String url;

    public ExternalDocsImpl(String url, String description) {
        this.url = url;
        this.description = description;
    }

    public static ExternalDocsImpl parseString(String docs) {

        if(docs == null) {
            return null;
        }

        Matcher matcher = URL_PATTERN.matcher(docs.trim());

        if(!matcher.matches()) {
            throw new IllegalStateException("Could not parse ExternalDocs reference: " + docs.trim());
        }

        return new ExternalDocsImpl(matcher.group(2), matcher.group(1));

    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getUrl() {
        return url;
    }

}
