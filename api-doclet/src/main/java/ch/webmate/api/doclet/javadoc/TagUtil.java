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

package ch.webmate.api.doclet.javadoc;

import com.sun.javadoc.ProgramElementDoc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by sventschui on 09.05.15.
 */
public final class TagUtil {

    public static final TagUtil INSTANCE = new TagUtil();

    private TagUtil() {

    }

    public String getSingleTagContent(ProgramElementDoc programElementDoc, Tag tag) {
        return getSingleTagContent(programElementDoc, tag.getTagName());
    }

    public String getSingleTagContent(ProgramElementDoc programElementDoc, String tagName) {

        com.sun.javadoc.Tag[] tags = programElementDoc.tags(tagName);

        if(tags.length > 1) {
            throw new IllegalStateException("Got multiple tags with name " + tagName + " on " + programElementDoc.qualifiedName());
        }

        if(tags.length == 0) {
            return null;
        }

        return tags[0].text();

    }

    public List<String> getMultipleTagContents(ProgramElementDoc programElementDoc, Tag tag) {
        return getMultipleTagContents(programElementDoc, tag.getTagName());
    }

    public List<String> getMultipleTagContents(ProgramElementDoc programElementDoc, String tagName) {

        com.sun.javadoc.Tag[] tags = programElementDoc.tags(tagName);

        if(tags == null || tags.length == 0) {
            return Collections.emptyList();
        }

        List<String> tagContents = new ArrayList<>();

        for (com.sun.javadoc.Tag tag : tags) {

            tagContents.add(tag.text());

        }

        return tagContents;

    }
}
