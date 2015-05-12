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

import ch.webmate.api.doclet.reporter.ApiReporter;
import ch.webmate.api.doclet.reporter.model.Application;
import ch.webmate.api.doclet.reporter.util.OptionsStore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.wordnik.swagger.models.Swagger;
import com.wordnik.swagger.models.auth.SecuritySchemeDefinition;
import com.wordnik.swagger.models.parameters.Parameter;
import com.wordnik.swagger.util.SecurityDefinitionDeserializer;
import org.dozer.DozerBeanMapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.loader.api.TypeMappingOptions;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by sventschui on 10.05.15.
 */
public class Swagger2Reporter implements ApiReporter {

    public static final String OPTION_BASE_FILE = "--swagger2BaseFile";

    protected OptionsStore optionsStore = createOptionsStore();

    @Override
    public boolean writeReport(File outputDirectory, Application application) {

        ObjectMapper objectMapper = createObjectMapper();

        String baseFile = optionsStore.getSingleOption(OPTION_BASE_FILE);

        Swagger baseSwagger = null;

        System.out.println("BaseFile: " + baseFile);

        if(baseFile != null) {

            System.out.println(new File(baseFile).getAbsolutePath());

            try {
                baseSwagger = objectMapper.readValue(new File(baseFile), Swagger.class);
            } catch (IOException e) {
                e.printStackTrace();
                throw new IllegalStateException("Failed to read base swagger.json");
            }
        }

        // TODO: support reading existing swagger.json by reading it
        // using jackson and dozer pojo merge

        Swagger swagger = Swagger2ModelFactory.INSTANCE.createSwaggerTreeFromApplication(application);

        if(baseSwagger != null) {

            DozerBeanMapper mapper = createBeanMapper();

            mapper.map(swagger, baseSwagger);

            swagger = baseSwagger;

        }

        try {
            objectMapper.writer().writeValue(System.out, swagger);
            objectMapper.writer().writeValue(new File(outputDirectory, "swagger.json"), swagger);
            System.out.println("");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;

    }

    private DozerBeanMapper createBeanMapper() {

        DozerBeanMapper beanMapper = new DozerBeanMapper();

        beanMapper.addMapping(new BeanMappingBuilder() {
            @Override
            protected void configure() {
                mapping(Swagger.class, Swagger.class, TypeMappingOptions.oneWay(), TypeMappingOptions.mapNull(false));
                mapping(Parameter.class, Parameter.class, TypeMappingOptions.beanFactory("ch.webmate.api.doclet.reporter.swagger2.ParameterBeanFactory"));
            }
        });

        return beanMapper;

    }

    protected OptionsStore createOptionsStore() {

        OptionsStore optionsStore = new OptionsStore();

        optionsStore.registerKnownOption(OPTION_BASE_FILE, 2);

        return optionsStore;

    }

    protected ObjectMapper createObjectMapper() {

        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        SimpleModule module = new SimpleModule();

        module.addDeserializer(SecuritySchemeDefinition.class, new SecurityDefinitionDeserializer());

        objectMapper.registerModule(module);

        return objectMapper;
        
    }

    @Override
    public Map<String, Integer> getKnownOptions() {
        return optionsStore.getKnownOptions();
    }

    @Override
    public void setOptions(Map<String, List<String>> options) {
        optionsStore.setOptions(options);
    }

}
