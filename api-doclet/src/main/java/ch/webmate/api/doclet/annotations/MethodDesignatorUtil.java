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

package ch.webmate.api.doclet.annotations;

import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.AnnotationTypeDoc;
import com.sun.javadoc.MethodDoc;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class MethodDesignatorUtil {

    public static final MethodDesignatorUtil INSTANCE = new MethodDesignatorUtil();

    protected final List<String> knownMethodDesignators = new ArrayList<>(Arrays.asList(
            GET.class.getCanonicalName(),
            POST.class.getCanonicalName(),
            PUT.class.getCanonicalName(),
            DELETE.class.getCanonicalName(),
            OPTIONS.class.getCanonicalName(),
            HEAD.class.getCanonicalName()
    ));

    private MethodDesignatorUtil() {
    }

    public List<String> getHttpMethods(MethodDoc operationMethod) {

        List<String> httpMethods = new ArrayList<String>();

        for (AnnotationDesc annotationDesc : operationMethod.annotations()) {

            String httpMethod = AnnotationUtil.INSTANCE.getAnnotationValue(annotationDesc.annotationType(), HttpMethod.class, "value", String.class);

            if (httpMethod != null) {
                httpMethods.add(httpMethod);
            }

        }


        return httpMethods;
    }

    public boolean hasHttpMethodDesignator(MethodDoc methodDoc) {

        for (AnnotationDesc annotationDesc : methodDoc.annotations()) {

            AnnotationTypeDoc annotationTypeDoc = annotationDesc.annotationType();

            // cache the known method designator annotations to boost performance
            if (knownMethodDesignators.contains(annotationTypeDoc.qualifiedName())) {
                return true;
            }

            // check for unknown method designators
            if (AnnotationUtil.INSTANCE.hasAnnotation(annotationTypeDoc, HttpMethod.class)) {

                knownMethodDesignators.add(annotationTypeDoc.qualifiedName());

                return true;

            }

        }

        return false;
    }

}