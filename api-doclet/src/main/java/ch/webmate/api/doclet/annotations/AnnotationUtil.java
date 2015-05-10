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
import com.sun.javadoc.AnnotationValue;
import com.sun.javadoc.ProgramElementDoc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;

public final class AnnotationUtil {

    public static final AnnotationUtil INSTANCE = new AnnotationUtil();

    private AnnotationUtil() {

    }

    public AnnotationDesc getAnnotation(ProgramElementDoc doc, Class<? extends Annotation> annotation) {
        return getAnnotation(doc.annotations(), annotation);
    }

    public AnnotationDesc getAnnotation(AnnotationDesc[] annotations, Class<? extends Annotation> annotation) {

        for (AnnotationDesc annotationDesc : annotations) {
            if (annotationDesc.annotationType().qualifiedName().equals(annotation.getCanonicalName())) {
                return annotationDesc;
            }
        }

        return null;

    }

    public <T> T getAnnotationValue(ProgramElementDoc doc, Class<? extends Annotation> annotation, String name, Class<T> valueClazz) {
        return getAnnotationValue(doc.annotations(), annotation, name, valueClazz);
    }

    public <T> T getAnnotationValue(AnnotationDesc[] annotations, Class<? extends Annotation> annotation, String name, Class<T> valueClazz) {
        AnnotationDesc annotationDesc = getAnnotation(annotations, annotation);

        if (annotationDesc != null) {

            for (AnnotationDesc.ElementValuePair elementValuePair : annotationDesc.elementValues()) {

                if (elementValuePair.element().name().equals(name)) {

                    return getAnnotationValue(elementValuePair, valueClazz);

                }

            }

        }

        return null;

    }

    protected <T> T getAnnotationValue(AnnotationDesc.ElementValuePair elementValuePair, Class<T> valueClazz) {

        Object value = elementValuePair.value().value();

        // since null cannot be passed to an annotation value it's safe to assume that null means default value
        if (value == null) {
            value = elementValuePair.element().defaultValue().value();
        }

        value = castAnnotationValue(value, valueClazz);

        if (valueClazz.isInstance(value)) {
            return valueClazz.cast(value);
        } else {
            throw new IllegalStateException("Value " + value + " is not of type " + valueClazz);
        }

    }

    private <T> Object castAnnotationValue(Object value, Class<T> clazz) {

        if(AnnotationValue.class.isInstance(value)) {
            return ((AnnotationValue) value).value();
        }

        if(clazz.isArray() && AnnotationValue[].class.isInstance(value)) {

            Class<?> componentClazz = clazz.getComponentType();

            Object newValueArray = Array.newInstance(componentClazz, Array.getLength(value));

            AnnotationValue[] valueArray = (AnnotationValue[]) value;

            for (int i = 0; i < (valueArray).length; i++) {
                AnnotationValue annotationValue = valueArray[i];

                Object realValue = annotationValue.value();

                if (componentClazz.isInstance(realValue)) {
                    Array.set(newValueArray, i, realValue);
                } else {
                    System.out.println("WARN: Cast to " + componentClazz + " failed. Element is of class " + realValue.getClass());
                    return value;
                }

            }

            return newValueArray;

        }

        return value;

    }

    public boolean hasAnnotation(ProgramElementDoc doc, Class<? extends Annotation> annotation) {
        return getAnnotation(doc, annotation) != null;
    }

    public boolean hasAnnotation(AnnotationDesc[] annotations, Class<? extends Annotation> annotation) {
        return getAnnotation(annotations, annotation) != null;
    }
}