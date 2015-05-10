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
import ch.webmate.api.doclet.immutable.ImmutableListUtil;
import ch.webmate.api.doclet.javadoc.TagUtil;
import ch.webmate.api.doclet.javadoc.Tag;
import ch.webmate.api.doclet.annotations.AnnotationUtil;
import ch.webmate.api.doclet.reporter.model.Application;
import ch.webmate.api.doclet.reporter.model.Contact;
import ch.webmate.api.doclet.reporter.model.ExternalDocs;
import ch.webmate.api.doclet.reporter.model.License;
import ch.webmate.api.doclet.reporter.model.Resource;
import com.google.common.collect.ImmutableList;
import com.sun.javadoc.ClassDoc;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import java.util.List;

/**
 * Created by sventschui on 09.05.15.
 *
 */
public class ApplicationImpl implements Application {

    protected final ClassDoc applicationClass;

    protected final String path;

    protected final String version;

    protected final String title;

    protected final String termsOfService;

    protected final Contact contact;

    protected final License license;

    protected final String host;

    protected final String basePath;

    protected ImmutableList<Resource> resources = ImmutableList.<Resource>builder().build();

    protected final String description;

    protected final ExternalDocs externalDocs;

    protected final ImmutableList<String> consumes;

    protected final ImmutableList<String> produces;

    public ApplicationImpl(ClassDoc applicationClass) {

        this.applicationClass = applicationClass;

        if(applicationClass == null) {

            path = "/";

            version = null;

            title = null;

            termsOfService = null;

            contact = null;

            license = null;

            host = null;

            basePath = null;

            description = null;

            externalDocs = null;

            consumes = null;

            produces = null;

        } else {

            path = AnnotationUtil.INSTANCE.getAnnotationValue(applicationClass, ApplicationPath.class, JaxRsAnnotationValueNames.APPLICATION_PATH_VALUE, String.class);

            version = TagUtil.INSTANCE.getSingleTagContent(applicationClass, Tag.VERSION);

            title = TagUtil.INSTANCE.getSingleTagContent(applicationClass, Tag.TITLE);

            termsOfService = TagUtil.INSTANCE.getSingleTagContent(applicationClass, Tag.TERMS_OF_SERVICE);

            host = TagUtil.INSTANCE.getSingleTagContent(applicationClass, Tag.HOST);

            basePath = TagUtil.INSTANCE.getSingleTagContent(applicationClass, Tag.BASE_PATH);

            license = LicenseImpl.parseString(TagUtil.INSTANCE.getSingleTagContent(applicationClass, Tag.LICENSE));

            externalDocs = ExternalDocsImpl.parseString(TagUtil.INSTANCE.getSingleTagContent(applicationClass, Tag.EXTERNAL_DOCS));

            description = TagUtil.INSTANCE.getSingleTagContent(applicationClass, Tag.DESCRIPTION);

            produces = ImmutableListUtil.INSTANCE.createImmutableListFromArray(AnnotationUtil.INSTANCE.getAnnotationValue(applicationClass, Produces.class, JaxRsAnnotationValueNames.PRODUCES_VALUE, String[].class));

            consumes = ImmutableListUtil.INSTANCE.createImmutableListFromArray(AnnotationUtil.INSTANCE.getAnnotationValue(applicationClass, Consumes.class, JaxRsAnnotationValueNames.CONSUMES_VALUE, String[].class));

            String contactName = TagUtil.INSTANCE.getSingleTagContent(applicationClass, Tag.CONTACT_NAME);

            String contactEmail = TagUtil.INSTANCE.getSingleTagContent(applicationClass, Tag.CONTACT_EMAIL);

            String contactUrl = TagUtil.INSTANCE.getSingleTagContent(applicationClass, Tag.CONTACT_URL);

            if(contactName != null || contactEmail != null || contactUrl != null) {
                contact = new ContactImpl(contactName, contactEmail, contactUrl);
            } else {
                contact = null;
            }

        }

    }

    public void setResources(ImmutableList<Resource> resources) {
        this.resources = resources;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getTermsOfService() {
        return termsOfService;
    }

    @Override
    public Contact getContact() {
        return contact;
    }

    @Override
    public License getLicense() {
        return license;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public String getBasePath() {
        return basePath;
    }

    @Override
    public ClassDoc getApplicationClass() {
        return applicationClass;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public ImmutableList<Resource> getResources() {
        return resources;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public ExternalDocs getExternalDocs() {
        return externalDocs;
    }

    @Override
    public List<String> getProduces() {
        return produces;
    }

    @Override
    public List<String> getConsumes() {
        return consumes;
    }
}
