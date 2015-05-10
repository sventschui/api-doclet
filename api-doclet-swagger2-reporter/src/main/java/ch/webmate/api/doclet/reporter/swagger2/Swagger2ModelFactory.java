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

package ch.webmate.api.doclet.reporter.swagger2;

import ch.webmate.api.doclet.reporter.model.Application;
import ch.webmate.api.doclet.reporter.model.Operation;
import ch.webmate.api.doclet.reporter.model.Parameter;
import ch.webmate.api.doclet.reporter.model.Resource;
import com.wordnik.swagger.models.Contact;
import com.wordnik.swagger.models.ExternalDocs;
import com.wordnik.swagger.models.Info;
import com.wordnik.swagger.models.License;
import com.wordnik.swagger.models.Path;
import com.wordnik.swagger.models.Response;
import com.wordnik.swagger.models.Scheme;
import com.wordnik.swagger.models.Swagger;
import com.wordnik.swagger.models.Tag;
import com.wordnik.swagger.models.parameters.BodyParameter;
import com.wordnik.swagger.models.parameters.FormParameter;
import com.wordnik.swagger.models.parameters.HeaderParameter;
import com.wordnik.swagger.models.parameters.PathParameter;
import com.wordnik.swagger.models.parameters.QueryParameter;

import javax.ws.rs.HttpMethod;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sventschui on 10.05.15.
 */
public final class Swagger2ModelFactory {

    public static final Swagger2ModelFactory INSTANCE = new Swagger2ModelFactory();

    private Swagger2ModelFactory() {

    }

    public Swagger createSwaggerTreeFromApplication(Application application) {

        Swagger swagger = new Swagger();

        swagger.setInfo(createSwaggerInfoFromApplication(application));

        swagger.setHost(application.getHost());
        swagger.setBasePath(application.getBasePath());
        swagger.setSchemes(Collections.singletonList(Scheme.HTTP)); // TODO: remove static values
        swagger.setExternalDocs(createSwaggerExternalDocsFromExternalDocs(application.getExternalDocs()));
        swagger.setTags(createSwaggerTagsFromResources(application.getResources()));
        swagger.setPaths(createSwaggerPathsFromResources(application.getResources()));
        swagger.setProduces(application.getProduces());
        swagger.setConsumes(application.getConsumes());

        // TODO
//        swagger.setDefinitions();
//        swagger.setParameters();
//        swagger.setSecurityDefinitions();
//        swagger.setSecurityRequirement();

        return swagger;

    }

    protected Map<String, Path> createSwaggerPathsFromResources(List<Resource> resources) {

        Map<String, Path> swaggerPaths = new HashMap<>();

        for (Resource resource : resources) {

            for (Operation operation : resource.getOperations()) {

                swaggerPaths.put(operation.getQualifiedPath(), createSwaggerPathFromOperation(operation));

            }
        }

        return swaggerPaths;

    }

    protected Path createSwaggerPathFromOperation(Operation operation) {

        Path swaggerPath = new Path();

        List<String> httpMethods = operation.getHttpMethods();

        if(httpMethods == null || httpMethods.isEmpty()) {
            System.out.println("WARNING: no http method for op " + operation.getQualifiedPath());
        } else {

            com.wordnik.swagger.models.Operation swaggerOperation = createSwaggerOperationFromOperation(operation);

            for (String httpMethod : httpMethods) {
                setOperationOnPathForHttpMethods(swaggerOperation, swaggerPath, httpMethod);
            }

        }

        return swaggerPath;

    }

    protected void setOperationOnPathForHttpMethods(com.wordnik.swagger.models.Operation swaggerOperation, Path swaggerPath, String httpMethod) {

        switch(httpMethod.toUpperCase()) {
            case HttpMethod.GET:
                swaggerPath.setGet(swaggerOperation);
                break;

            case HttpMethod.POST:
                swaggerPath.setPost(swaggerOperation);
                break;

            case HttpMethod.DELETE:
                swaggerPath.setDelete(swaggerOperation);
                break;

            case HttpMethod.PUT:
                swaggerPath.setPut(swaggerOperation);
                break;

            case "PATCH":
                swaggerPath.setPatch(swaggerOperation);
                break;

            case HttpMethod.OPTIONS:
                swaggerPath.setOptions(swaggerOperation);
                break;

            case HttpMethod.HEAD:
            default:

                throw new IllegalStateException("Swagger does not support httpMethod: " + httpMethod);

        }

    }

    protected com.wordnik.swagger.models.Operation createSwaggerOperationFromOperation(Operation operation) {

        com.wordnik.swagger.models.Operation swaggerOperation = new com.wordnik.swagger.models.Operation();

        List<String> tags = Collections.singletonList(getSwaggerTagNameFromResource(operation.getResource()));

        List<String> produces = operation.getProduces();

        if(produces == null) {
            produces = operation.getResource().getProduces();
        }

        // TODO: does swagger do this inheritance?
//        if(produces == null) {
//            produces = operation.getResource().getApplication().getProduces();
//        }

        List<String> consumes = operation.getConsumes();

        if(produces == null) {
            consumes = operation.getResource().getConsumes();
        }

        // TODO: does swagger do this inheritance?
//        if(produces == null) {
//            consumes = operation.getResource().getApplication().getConsumes();
//        }

        swaggerOperation.setTags(tags);
        swaggerOperation.setOperationId(operation.getId());
        swaggerOperation.setDescription(operation.getDescription());
        swaggerOperation.setConsumes(consumes);
        swaggerOperation.setProduces(produces);
        swaggerOperation.setSchemes(Collections.singletonList(Scheme.HTTP)); // TODO: collect schemes information. Think this is not available in JAX-RS 2, may use JavaDoc tag
        swaggerOperation.setSummary(operation.getSummary());
        swaggerOperation.setDeprecated(operation.getDeprecated());
        swaggerOperation.setSecurity(Collections.<Map<String, List<String>>>emptyList()); // TODO: collect security information
        swaggerOperation.setDeprecated(operation.isDeprecated());
        swaggerOperation.setResponses(createSwaggerResponsesFromOperation(operation));

        if(operation.getParameters() != null && !operation.getParameters().isEmpty()) {

            List<com.wordnik.swagger.models.parameters.Parameter> swaggerParameters = new ArrayList<>();

            swaggerOperation.setParameters(swaggerParameters);

            for (Parameter parameter : operation.getParameters()) {

                swaggerParameters.add(createSwaggerParameterFromParameter(parameter));

            }


        }

        if(operation.getExternalDocs() != null) {
            swaggerOperation.setExternalDocs(new ExternalDocs(operation.getExternalDocs().getDescription(), operation.getExternalDocs().getUrl()));
        }
        return swaggerOperation;
    }

    protected com.wordnik.swagger.models.parameters.Parameter createSwaggerParameterFromParameter(Parameter parameter) {

        com.wordnik.swagger.models.parameters.Parameter swaggerParameter = null;

        TypeInfo typeInfo = TypeInfo.createTypeInfoFromType(parameter.getType());

        switch(parameter.getParameterType()) {
            case QUERY:
                swaggerParameter = createSwaggerQueryParameterFromParameterAndTypeInfo(parameter, typeInfo);
                break;

            case HEADER:
                swaggerParameter = createSwaggerHeaderParameterFromParameterAndTypeInfo(parameter, typeInfo);
                break;

            case PATH:
                swaggerParameter = createSwaggerPathParameterFromParameterAndTypeInfo(parameter, typeInfo);
                break;

            case FORM:
                swaggerParameter = createSwaggerFormParameterFromParameterAndTypeInfo(parameter, typeInfo);
                break;

            case BODY:
                swaggerParameter = createSwaggerBodyParameterFromParameterAndTypeInfo();
                break;

            case MATRIX:
                throw new IllegalStateException("Swagger 2 doesn't support matrix style parameters");

            default:
                throw new IllegalStateException("Unknown param tpye " + parameter.getParameterType().toString());

        }

        swaggerParameter.setPattern(typeInfo.getPattern());
        swaggerParameter.setDescription(parameter.getDescription());
        swaggerParameter.setRequired(parameter.isRequired());
        swaggerParameter.setName(parameter.getName());

        return swaggerParameter;

    }

    private com.wordnik.swagger.models.parameters.Parameter createSwaggerBodyParameterFromParameterAndTypeInfo() {

        com.wordnik.swagger.models.parameters.Parameter swaggerParameter;

        BodyParameter bodyParameter = new BodyParameter();

        bodyParameter.setSchema(null); // TODO

        swaggerParameter = bodyParameter;

        return swaggerParameter;
    }

    protected com.wordnik.swagger.models.parameters.Parameter createSwaggerFormParameterFromParameterAndTypeInfo(Parameter parameter, TypeInfo typeInfo) {

        com.wordnik.swagger.models.parameters.Parameter swaggerParameter;

        FormParameter formParameter = new FormParameter();

        formParameter.setType(typeInfo.getType());
        formParameter.setFormat(typeInfo.getFormat());
        formParameter.setItems(null); // TODO: typeInfo.getItems()
        formParameter.setCollectionFormat(null); // TODO
        formParameter.setDefaultValue(parameter.getDefaultValue());
        formParameter.setEnum(typeInfo.getAllowedValues());

        swaggerParameter = formParameter;

        return swaggerParameter;

    }

    protected com.wordnik.swagger.models.parameters.Parameter createSwaggerPathParameterFromParameterAndTypeInfo(Parameter parameter, TypeInfo typeInfo) {

        com.wordnik.swagger.models.parameters.Parameter swaggerParameter;

        PathParameter pathParameter = new PathParameter();

        pathParameter.setType(typeInfo.getType());
        pathParameter.setFormat(typeInfo.getFormat());
        pathParameter.setItems(null); // TODO: typeInfo.getItems()
        pathParameter.setCollectionFormat(null); // TODO
        pathParameter.setDefaultValue(parameter.getDefaultValue());
        pathParameter.setEnum(typeInfo.getAllowedValues());

        swaggerParameter = pathParameter;

        return swaggerParameter;

    }

    protected com.wordnik.swagger.models.parameters.Parameter createSwaggerHeaderParameterFromParameterAndTypeInfo(Parameter parameter, TypeInfo typeInfo) {

        com.wordnik.swagger.models.parameters.Parameter swaggerParameter;

        HeaderParameter headerParameter = new HeaderParameter();

        headerParameter.setType(typeInfo.getType());
        headerParameter.setFormat(typeInfo.getFormat());
        headerParameter.setItems(null); // TODO: typeInfo.getItems()
        headerParameter.setCollectionFormat(null); // TODO
        headerParameter.setDefaultValue(parameter.getDefaultValue());
        headerParameter.setEnum(typeInfo.getAllowedValues());

        swaggerParameter = headerParameter;

        return swaggerParameter;

    }

    protected com.wordnik.swagger.models.parameters.Parameter createSwaggerQueryParameterFromParameterAndTypeInfo(Parameter parameter, TypeInfo typeInfo) {

        com.wordnik.swagger.models.parameters.Parameter swaggerParameter;

        QueryParameter queryParameter= new QueryParameter();

        queryParameter.setType(typeInfo.getType());
        queryParameter.setFormat(typeInfo.getFormat());
        queryParameter.setItems(null); // TODO: typeInfo.getItems()
        queryParameter.setCollectionFormat(null); // TODO
        queryParameter.setDefaultValue(parameter.getDefaultValue());
        queryParameter.setEnum(typeInfo.getAllowedValues());

        swaggerParameter = queryParameter;

        return swaggerParameter;

    }

    protected Map<String, Response> createSwaggerResponsesFromOperation(Operation operation) {

        if(operation.getResponses() != null && !operation.getResponses().isEmpty()) {

            Map<String, Response> swaggerResponses = new HashMap<>();

            for (ch.webmate.api.doclet.reporter.model.Response response : operation.getResponses()) {

                Response swaggerResponse = new Response();

                swaggerResponse.setDescription(response.getDescription());
                // TODO
//                swaggerResponse.setHeaders();
//                swaggerResponse.setSchema();

                swaggerResponses.put(response.getStatusCode(), swaggerResponse);

            }

            return swaggerResponses;

        }

        return null;

    }


    protected List<Tag> createSwaggerTagsFromResources(List<Resource> resources) {

        List<Tag> tags = new ArrayList<>();

        for (Resource resource : resources) {

            Tag swaggerTag = new Tag();

            swaggerTag.setName(getSwaggerTagNameFromResource(resource));

            swaggerTag.setDescription(resource.getDescription());

            swaggerTag.setExternalDocs(createSwaggerExternalDocsFromExternalDocs(resource.getExternalDocs()));

            tags.add(swaggerTag);

        }

        return tags;
    }

    protected ExternalDocs createSwaggerExternalDocsFromExternalDocs(ch.webmate.api.doclet.reporter.model.ExternalDocs docs) {

        if(docs == null) {
            return null;
        } else {
            return new ExternalDocs(docs.getDescription(), docs.getUrl());
        }

    }

    protected String getSwaggerTagNameFromResource(Resource resource) {
        return resource.getQualifiedPath();
    }

    protected Info createSwaggerInfoFromApplication(Application application) {

        Info swaggerInfo = new Info();

        swaggerInfo.setVersion(application.getVersion());
        swaggerInfo.setTitle(application.getTitle());
        swaggerInfo.setTermsOfService(application.getTermsOfService());
        swaggerInfo.setDescription(application.getDescription());

        if(application.getContact() != null) {

            Contact contact = new Contact();
            swaggerInfo.setContact(contact);

            contact.setName(application.getContact().getName());
            contact.setUrl(application.getContact().getUrl());
            contact.setEmail(application.getContact().getEmail());

        }

        if(application.getLicense() != null) {

            License license = new License();
            swaggerInfo.setLicense(license);

            license.setUrl(application.getLicense().getUrl());
            license.setName(application.getLicense().getName());

        }

        return swaggerInfo;

    }


}
