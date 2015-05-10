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

import ch.webmate.api.doclet.annotations.JaxRsAnnotationValueNames;
import ch.webmate.api.doclet.javadoc.TagUtil;
import ch.webmate.api.doclet.javadoc.Tag;
import ch.webmate.api.doclet.annotations.AnnotationUtil;
import ch.webmate.api.doclet.reporter.model.Application;
import ch.webmate.api.doclet.reporter.model.ExternalDocs;
import ch.webmate.api.doclet.reporter.model.Operation;
import ch.webmate.api.doclet.reporter.model.Resource;
import ch.webmate.api.doclet.immutable.ImmutableListUtil;
import com.google.common.collect.ImmutableList;
import com.sun.javadoc.ClassDoc;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Created by sventschui on 09.05.15.
 */
public class ResourceImpl implements Resource {

    protected final ClassDoc resourceClass;

    protected final Application application;

    protected final String path;

    protected final String qualifiedPath;

    protected final ImmutableList<String> produces;

    protected final ImmutableList<String> consumes;

    protected final boolean deprecated;

    protected final String description;

    protected ImmutableList<Operation> operations = ImmutableList.of();

    private final ExternalDocs externalDocs;

    public ResourceImpl(Application application, ClassDoc resourceClass) {

        this.resourceClass = resourceClass;

        this.application = application;

        String pathAnnotationValue = AnnotationUtil.INSTANCE.getAnnotationValue(resourceClass, Path.class, JaxRsAnnotationValueNames.PATH_VALUE, String.class);

        path = pathAnnotationValue == null ? "/" : pathAnnotationValue;

        qualifiedPath = application.getPath() + path;

        if(AnnotationUtil.INSTANCE.hasAnnotation(resourceClass, Produces.class)) {
            produces = ImmutableListUtil.INSTANCE.createImmutableListFromArray(AnnotationUtil.INSTANCE.getAnnotationValue(resourceClass, Produces.class, JaxRsAnnotationValueNames.PRODUCES_VALUE, String[].class));
        } else {
            produces = null;
        }

        if(AnnotationUtil.INSTANCE.hasAnnotation(resourceClass, Produces.class)) {
            consumes = ImmutableListUtil.INSTANCE.createImmutableListFromArray(AnnotationUtil.INSTANCE.getAnnotationValue(resourceClass, Consumes.class, JaxRsAnnotationValueNames.CONSUMES_VALUE, String[].class));
        } else {
            consumes = null;
        }

        description = TagUtil.INSTANCE.getSingleTagContent(resourceClass, Tag.DESCRIPTION);

        deprecated = resourceClass.tags("deprecated").length > 0 || AnnotationUtil.INSTANCE.hasAnnotation(resourceClass, Deprecated.class);

        externalDocs = ExternalDocsImpl.parseString(TagUtil.INSTANCE.getSingleTagContent(resourceClass, Tag.EXTERNAL_DOCS));

    }

    public void setOperations(ImmutableList<Operation> operations) {
        this.operations = operations;
    }

    @Override
    public ClassDoc getResourceClass() {
        return resourceClass;
    }

    @Override
    public Application getApplication() {
        return application;
    }

    @Override
    public ImmutableList<Operation> getOperations() {
        return operations;
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
    public String getDescription() {
        return description;
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
    public boolean isDeprecated() {
        return deprecated;
    }

    @Override
    public ExternalDocs getExternalDocs() {
        return externalDocs;
    }
}
