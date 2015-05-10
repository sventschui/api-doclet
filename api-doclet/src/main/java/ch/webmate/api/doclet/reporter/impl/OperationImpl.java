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
import ch.webmate.api.doclet.annotations.MethodDesignatorUtil;
import ch.webmate.api.doclet.immutable.ImmutableListUtil;
import ch.webmate.api.doclet.javadoc.Tag;
import ch.webmate.api.doclet.javadoc.TagUtil;
import ch.webmate.api.doclet.reporter.model.ExternalDocs;
import ch.webmate.api.doclet.reporter.model.Operation;
import ch.webmate.api.doclet.reporter.model.Parameter;
import ch.webmate.api.doclet.reporter.model.Resource;
import ch.webmate.api.doclet.reporter.model.Response;
import com.google.common.collect.ImmutableList;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.RootDoc;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

/**
 * Created by sventschui on 09.05.15.
 */
public class OperationImpl implements Operation {

    protected final Resource resource;

    protected final MethodDoc operationMethod;

    protected final String path;

    protected final String qualifiedPath;

    protected final ImmutableList<String> produces;

    protected final ImmutableList<String> consumes;

    protected final ImmutableList<String> httpMethods;

    protected final String id;

    protected final String description;

    protected final boolean deprecated;

    protected final String summary;

    protected final ExternalDocsImpl externalDocs;

    protected final ImmutableList<Response> responses;

    protected final ImmutableList<Parameter> parameters;

    public OperationImpl(RootDoc root, Resource resource, MethodDoc operationMethod) {

        this.resource = resource;

        this.operationMethod = operationMethod;

        String pathAnnotationValue = AnnotationUtil.INSTANCE.getAnnotationValue(operationMethod, Path.class, JaxRsAnnotationValueNames.PATH_VALUE, String.class);

        path = pathAnnotationValue == null ? "/" : pathAnnotationValue;

        qualifiedPath = resource.getQualifiedPath() + path;

        if(AnnotationUtil.INSTANCE.hasAnnotation(operationMethod, Produces.class)) {
            produces = ImmutableListUtil.INSTANCE.createImmutableListFromArray(AnnotationUtil.INSTANCE.getAnnotationValue(operationMethod, Produces.class, JaxRsAnnotationValueNames.PRODUCES_VALUE, String[].class));
        } else {
            produces = null;
        }

        if(AnnotationUtil.INSTANCE.hasAnnotation(operationMethod, Consumes.class)) {
            consumes = ImmutableListUtil.INSTANCE.createImmutableListFromArray(AnnotationUtil.INSTANCE.getAnnotationValue(operationMethod, Consumes.class, JaxRsAnnotationValueNames.CONSUMES_VALUE, String[].class));
        } else {
            consumes = null;
        }

        httpMethods = ImmutableList.copyOf(MethodDesignatorUtil.INSTANCE.getHttpMethods(operationMethod));

        id = operationMethod.qualifiedName();

        description = operationMethod.commentText();

        deprecated = operationMethod.tags(Tag.DEPRECATED.getTagName()).length > 0 || AnnotationUtil.INSTANCE.hasAnnotation(operationMethod, Deprecated.class);

        summary = TagUtil.INSTANCE.getSingleTagContent(operationMethod, Tag.OPERATION_SUMMARY);

        externalDocs = ExternalDocsImpl.parseString(TagUtil.INSTANCE.getSingleTagContent(operationMethod, Tag.EXTERNAL_DOCS));

        List<String> responseIdentifiers = TagUtil.INSTANCE.getMultipleTagContents(operationMethod, Tag.RESPONSE);

        if(responseIdentifiers != null && !responseIdentifiers.isEmpty()) {

            ImmutableList.Builder<Response> responsesBuilder = ImmutableList.builder();

            for (String responseIdentifier : responseIdentifiers) {

                ResponseImpl response = ResponseImpl.parseString(root, responseIdentifier);

                responsesBuilder.add(response);

            }

            this.responses = responsesBuilder.build();

        } else {
            responses = null;
        }

        ImmutableList.Builder<Parameter> parametersBuilder = ImmutableList.builder();

        for (com.sun.javadoc.Parameter parameter : operationMethod.parameters()) {

            parametersBuilder.add(new ParameterImpl(operationMethod, parameter));

        }

        this.parameters = parametersBuilder.build();


    }

    @Override
    public String getSummary() {
        return summary;
    }

    @Override
    public ExternalDocs getExternalDocs() {
        return externalDocs;
    }

    @Override
    public Boolean getDeprecated() {
        return deprecated;
    }

    @Override
    public List<Response> getResponses() {
        return responses;
    }

    @Override
    public List<Parameter> getParameters() {
        return parameters;
    }

    @Override
    public MethodDoc getOperationMethod() {
        return operationMethod;
    }

    @Override
    public Resource getResource() {
        return resource;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getQualifiedPath() {
        return qualifiedPath;
    }

    @Override
    public ImmutableList<String> getProduces() {
        return produces;
    }

    @Override
    public ImmutableList<String> getConsumes() {
        return consumes;
    }

    @Override
    public ImmutableList<String> getHttpMethods() {
        return httpMethods;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Boolean isDeprecated() {
        return deprecated;
    }

}
