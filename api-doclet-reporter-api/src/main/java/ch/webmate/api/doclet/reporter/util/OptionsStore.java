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

package ch.webmate.api.doclet.reporter.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sventschui on 11.05.15.
 */
public class OptionsStore {

    protected Map<String, Integer> knownOptions = new HashMap<>();

    protected Map<String, List<String>> options = new HashMap<>();

    public List<String> getOption(String name) {
        return options.get(name);
    }

    public String getSingleOption(String name) {
        List<String> values = getOption(name);

        if(values == null || values.isEmpty()) {
            return null;
        }

        if(values.size() > 1) {
            throw new IllegalStateException("More that one option for name " + name);
        }

        return values.get(0);

    }

    public void registerKnownOptions(Map<String, Integer> knownOptions) {

        if(knownOptions == null) {
            return;
        }

        for (String name : knownOptions.keySet()) {

            registerKnownOption(name, knownOptions.get(name));

        }

    }

    public void registerKnownOption(String name, Integer length) {

        if(knownOptions.containsKey(name)) {
            throw new IllegalStateException("Option already registered: " + name);
        }

        knownOptions.put(name, length);

    }

    public Map<String, List<String>> getOptions() {
        return options;
    }

    public void setOptions(Map<String, List<String>> options) {
        this.options = options;
    }

    public Map<String, Integer> getKnownOptions() {
        return knownOptions;
    }

    public void setKnownOptions(Map<String, Integer> knownOptions) {
        this.knownOptions = knownOptions;
    }

    public int getOptionLength(String option) {

        Integer length = knownOptions.get(option);

        if(length == null) {
            return 0;
        }

        return length;

    }
}
