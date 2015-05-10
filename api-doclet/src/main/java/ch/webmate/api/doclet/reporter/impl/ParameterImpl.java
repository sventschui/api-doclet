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

import ch.webmate.api.doclet.annotations.AnnotationUtil;
import ch.webmate.api.doclet.annotations.JaxRsAnnotationValueNames;
import ch.webmate.api.doclet.reporter.model.Parameter;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.ParamTag;
import com.sun.javadoc.Type;

import javax.ws.rs.CookieParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

/**
 * Created by sventschui on 10.05.15.
 */
public class ParameterImpl implements Parameter {

    protected final ParameterType parameterType;

    protected final String name;

    protected final Type type;

    protected final String description;

    protected final Boolean required;

    protected final String defaultValue;

    public ParameterImpl(MethodDoc methodDoc, com.sun.javadoc.Parameter methodParameter) {

        if(AnnotationUtil.INSTANCE.hasAnnotation(methodParameter.annotations(), PathParam.class)) {

            this.parameterType = ParameterType.PATH;

            this.required = true;

            this.name = AnnotationUtil.INSTANCE.getAnnotationValue(methodParameter.annotations(), PathParam.class, JaxRsAnnotationValueNames.PATH_PARAM_VALUE, String.class);

            this.defaultValue = AnnotationUtil.INSTANCE.getAnnotationValue(methodParameter.annotations(), DefaultValue.class, JaxRsAnnotationValueNames.DEFAULT_VALUE_VALUE, String.class);

        } else if(AnnotationUtil.INSTANCE.hasAnnotation(methodParameter.annotations(), HeaderParam.class)) {

            this.parameterType = ParameterType.HEADER;

            this.required = true; // TODO

            this.name = AnnotationUtil.INSTANCE.getAnnotationValue(methodParameter.annotations(), HeaderParam.class, JaxRsAnnotationValueNames.HEADER_PARAM_VALUE, String.class);

            this.defaultValue = AnnotationUtil.INSTANCE.getAnnotationValue(methodParameter.annotations(), DefaultValue.class, JaxRsAnnotationValueNames.DEFAULT_VALUE_VALUE, String.class);

        } else if(AnnotationUtil.INSTANCE.hasAnnotation(methodParameter.annotations(), CookieParam.class)) {

            this.parameterType = ParameterType.COOKIE;

            this.required = true; // TODO

            this.name = AnnotationUtil.INSTANCE.getAnnotationValue(methodParameter.annotations(), CookieParam.class, JaxRsAnnotationValueNames.COOKIE_PARAM_VALUE, String.class);

            this.defaultValue = AnnotationUtil.INSTANCE.getAnnotationValue(methodParameter.annotations(), DefaultValue.class, JaxRsAnnotationValueNames.DEFAULT_VALUE_VALUE, String.class);

        } else if(AnnotationUtil.INSTANCE.hasAnnotation(methodParameter.annotations(), QueryParam.class)) {

            this.parameterType = ParameterType.QUERY;

            this.required = true; // TODO

            this.name = AnnotationUtil.INSTANCE.getAnnotationValue(methodParameter.annotations(), QueryParam.class, JaxRsAnnotationValueNames.QUERY_PARAM_VALUE, String.class);

            this.defaultValue = AnnotationUtil.INSTANCE.getAnnotationValue(methodParameter.annotations(), DefaultValue.class, JaxRsAnnotationValueNames.DEFAULT_VALUE_VALUE, String.class);

        } else if(AnnotationUtil.INSTANCE.hasAnnotation(methodParameter.annotations(), FormParam.class))  {

            this.parameterType = ParameterType.FORM;

            this.required = true; // TODO

            this.name = AnnotationUtil.INSTANCE.getAnnotationValue(methodParameter.annotations(), FormParam.class, JaxRsAnnotationValueNames.FORM_PARAM_VALUE, String.class);

            this.defaultValue = AnnotationUtil.INSTANCE.getAnnotationValue(methodParameter.annotations(), DefaultValue.class, JaxRsAnnotationValueNames.DEFAULT_VALUE_VALUE, String.class);

        } else if(AnnotationUtil.INSTANCE.hasAnnotation(methodParameter.annotations(), MatrixParam.class))  {

            this.parameterType = ParameterType.MATRIX;

            this.required = true; // TODO

            this.name = AnnotationUtil.INSTANCE.getAnnotationValue(methodParameter.annotations(), MatrixParam.class, JaxRsAnnotationValueNames.MATRIX_PARAM_VALUE, String.class);

            this.defaultValue = AnnotationUtil.INSTANCE.getAnnotationValue(methodParameter.annotations(), DefaultValue.class, JaxRsAnnotationValueNames.DEFAULT_VALUE_VALUE, String.class);

        } else {

            this.parameterType = ParameterType.BODY;

            this.required = true; // TODO

            this.name = methodParameter.name();

            this.defaultValue = null;

        }

        this.type = methodParameter.type();

        ParamTag paramTag = null;

        for (ParamTag paramTagToCheck : methodDoc.paramTags()) {
            if(paramTagToCheck.parameterName().equals(methodParameter.name())) {
                paramTag = paramTagToCheck;
                break;
            }
        }

        if(paramTag != null) {
            this.description = paramTag.parameterComment();
        } else {
            this.description = null;
        }

    }

    @Override
    public ParameterType getParameterType() {
        return parameterType;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Boolean isRequired() {
        return required;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public String getDefaultValue() {
        return defaultValue;
    }
}
