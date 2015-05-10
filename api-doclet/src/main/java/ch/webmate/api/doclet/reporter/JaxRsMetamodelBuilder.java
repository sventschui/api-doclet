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

package ch.webmate.api.doclet.reporter;

import ch.webmate.api.doclet.reporter.impl.ApplicationImpl;
import ch.webmate.api.doclet.reporter.impl.OperationImpl;
import ch.webmate.api.doclet.reporter.impl.ResourceImpl;
import ch.webmate.api.doclet.reporter.model.Application;
import ch.webmate.api.doclet.reporter.model.Operation;
import ch.webmate.api.doclet.reporter.model.Resource;
import com.google.common.collect.ImmutableList;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.RootDoc;

/**
 * Created by sventschui on 09.05.15.
 */
public class JaxRsMetamodelBuilder {

    protected final ApplicationImpl application;

    protected final  RootDoc root;

    public JaxRsMetamodelBuilder(RootDoc root, ClassDoc applicationClass) {

        this.root = root;

        application = new ApplicationImpl(applicationClass);

    }

    public Resource registerResource(ClassDoc resourceClass) {

        Resource resource = null;

        for (Resource resourceToCheck : application.getResources()) {

            if(resourceClass.equals(resourceToCheck.getResourceClass())) {
                resource = resourceToCheck;
            }
        }

        if(resource == null) {

            resource = new ResourceImpl(application, resourceClass);

            application.setResources(ImmutableList.<Resource>builder().addAll(application.getResources()).add(resource).build());

        }

        return resource;


    }

    public Operation registerOperation(MethodDoc operationMethod) {

        ClassDoc resourceClass = operationMethod.containingClass();

        ResourceImpl resource = (ResourceImpl) registerResource(resourceClass);

        Operation operation = null;

        for (Operation operationToCheck : resource.getOperations()) {

            if(operationToCheck.getOperationMethod().equals(operationMethod)) {
                operation = operationToCheck;
            }

        }

        if(operation == null) {

            operation = new OperationImpl(root, resource, operationMethod);

            resource.setOperations(ImmutableList.<Operation>builder().addAll(resource.getOperations()).add(operation).build());

        }

        return operation;

    }

    public Application getApplication() {
        return application;
    }
}
