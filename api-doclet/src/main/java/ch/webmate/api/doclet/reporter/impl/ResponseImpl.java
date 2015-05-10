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

import ch.webmate.api.doclet.reporter.model.Response;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sventschui on 10.05.15.
 */
public class ResponseImpl implements Response {

    protected static final Pattern PATTERN = Pattern.compile("\\A\\[([^\\]]*)\\](\\(([^\\)]*)\\))?\\s?(.*)\\z");

    protected static final int PATTERN_GROUP_CLASS_NAME = 3;

    protected static final int PATTERN_GROUP_STATUS_CODE = 1;

    protected static final int PATTERN_GROUP_DESCRIPTION = 4;

    protected final String statusCode;

    protected final String description;

    protected final ClassDoc responseClass;

    public ResponseImpl(String statusCode, String description, ClassDoc responseClass) {
        this.statusCode = statusCode;
        this.description = description;
        this.responseClass = responseClass;
    }

    public static ResponseImpl parseString(RootDoc root, String responseIdentifier) {

        Matcher matcher = PATTERN.matcher(responseIdentifier.trim());

        if(!matcher.matches()) {
            throw new IllegalStateException("apiResponse tag malformed");
        }

        String className = matcher.group(PATTERN_GROUP_CLASS_NAME);

        System.out.println(root.classNamed(className));

        return new ResponseImpl(matcher.group(PATTERN_GROUP_STATUS_CODE), matcher.group(PATTERN_GROUP_DESCRIPTION), root.classNamed(className));

    }

    @Override
    public String getStatusCode() {
        return statusCode;
    }

    @Override
    public ClassDoc getResponseClass() {
        return responseClass;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
