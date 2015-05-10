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

package ch.webmate.api.doclet;

import ch.webmate.api.doclet.annotations.AnnotationUtil;
import ch.webmate.api.doclet.annotations.MethodDesignatorUtil;
import ch.webmate.api.doclet.reporter.ApiReporter;
import ch.webmate.api.doclet.reporter.JaxRsMetamodelBuilder;
import ch.webmate.api.doclet.reporter.model.Application;
import ch.webmate.api.doclet.reporter.util.OptionsStore;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.RootDoc;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.TimeUnit;

/**
 * Created by sventschui on 09.05.15.
 */
public class ApiDoclet {

    protected static final ServiceLoader<ApiReporter> API_REPORTER_SERVICE_LOADER = ServiceLoader.load(ApiReporter.class);

    protected static final OptionsStore OPTIONS_STORE = createOptionsStore();

    private static OptionsStore createOptionsStore() {
        OptionsStore optionsStore = new OptionsStore();

        for (ApiReporter apiReporter : API_REPORTER_SERVICE_LOADER) {

            optionsStore.registerKnownOptions(apiReporter.getKnownOptions());

        }

        return optionsStore;

    }

    protected final RootDoc root;

    public ApiDoclet(RootDoc root) {
        this.root = root;
    }

    public static boolean start(RootDoc root) {
        return new ApiDoclet(root).process();
    }

    public boolean process() {
        
        propagateOptions();

        Application application = createJaxRsMetamodel();

        return generateReport(application);

    }

    private void propagateOptions() {

        if(root.options() == null) {
            return;
        }

        Map<String, List<String>> options = new HashMap<>();

        for (String[] option : root.options()) {

            if(option == null) { // should actually never happen
                continue;
            }


            if (option.length == 0) { // should acutally never happen
                throw new IllegalStateException("Option with length 0");
            }

            String optionName = option[0];

            List<String> optionValues = Arrays.asList(Arrays.copyOfRange(option, 1, option.length));

            options.put(optionName, optionValues);

        }

        for (ApiReporter apiReporter : API_REPORTER_SERVICE_LOADER) {
            apiReporter.setOptions(options);
        }


    }

    protected boolean generateReport(Application application) {

        long startTime = System.nanoTime();

        File outputDir = createOutputDir();

        Iterator<ApiReporter> apiReporterServiceIterator = API_REPORTER_SERVICE_LOADER.iterator();

        if(!apiReporterServiceIterator.hasNext()) {
            throw new IllegalStateException("Could not find a ApiReporter service");
        }

        while(apiReporterServiceIterator.hasNext()) {

            boolean success = invokeReporter(application, outputDir, apiReporterServiceIterator.next());

            if (!success) {
                return false;
            }

        }

        long elapsedTime = System.nanoTime() - startTime;

        System.out.println("Generating reports took " + TimeUnit.NANOSECONDS.toMillis(elapsedTime) + "ms (" + elapsedTime + "ns)");

        System.out.flush();

        return true;

    }

    protected boolean invokeReporter(Application application, File outputDir, ApiReporter apiReporter) {

        long currentReporterStartTime = System.nanoTime();

        boolean success = apiReporter.writeReport(outputDir, application);

        if(!success) {

            System.out.println("Generating report " + apiReporter.getClass().getCanonicalName() + " failed");

            return false;

        }

        long currentReporterElapsedTime = System.nanoTime() - currentReporterStartTime;

        System.out.println("Generating report " + apiReporter.getClass().getCanonicalName() + " took " + TimeUnit.NANOSECONDS.toMillis(currentReporterElapsedTime) + "ms (" + currentReporterElapsedTime + "ns)");

        return true;

    }

    protected File createOutputDir() {

        File outputDir = new File(".");

        if(!outputDir.exists()) {

            if(!outputDir.mkdirs()) {
                throw new IllegalStateException("Could not create outputDirectory");
            }

        } else if(!outputDir.isDirectory()) {
            throw new IllegalStateException("OutputDir is not a directory");
        }

        return outputDir;

    }

    protected Application createJaxRsMetamodel() {

        long startTime = System.nanoTime();

        JaxRsMetamodelBuilder jaxRsMetamodelBuilder = createJaxRsMetamodelBuilder();

        for (ClassDoc classDoc : root.classes()) {
            checkClassForJaxRsAnnotations(jaxRsMetamodelBuilder, classDoc);
        }

        long elapsedTime = System.nanoTime() - startTime;

        System.out.println("Creating JAX RS metamodel took "+ TimeUnit.NANOSECONDS.toMillis(elapsedTime)+"ms ("+elapsedTime+"ns)");

        return jaxRsMetamodelBuilder.getApplication();

    }

    protected void checkClassForJaxRsAnnotations(JaxRsMetamodelBuilder jaxRsMetamodelBuilder, ClassDoc classDoc) {
        // TODO: Is this check needed / correct?
        if(classDoc.isOrdinaryClass() || classDoc.isInterface()) {

            if(AnnotationUtil.INSTANCE.hasAnnotation(classDoc, Path.class)) {

                jaxRsMetamodelBuilder.registerResource(classDoc);

            }

            for (MethodDoc methodDoc : classDoc.methods()) {

                if(AnnotationUtil.INSTANCE.hasAnnotation(methodDoc, Path.class) || MethodDesignatorUtil.INSTANCE.hasHttpMethodDesignator(methodDoc)) {

                    jaxRsMetamodelBuilder.registerOperation(methodDoc);

                }

            }

        }
    }

    protected JaxRsMetamodelBuilder createJaxRsMetamodelBuilder() {

        JaxRsMetamodelBuilder jaxRsMetamodelBuilder = null;

        for (ClassDoc classDoc : root.classes()) {
            if(AnnotationUtil.INSTANCE.hasAnnotation(classDoc, ApplicationPath.class)) {
                jaxRsMetamodelBuilder = new JaxRsMetamodelBuilder(root, classDoc);
                break;
            }
        }

        if(jaxRsMetamodelBuilder == null) {

            System.out.println("WARN: Found no JAX-RS Application, falling back to \"/\"");

            jaxRsMetamodelBuilder = new JaxRsMetamodelBuilder(root, null);

        }

        return jaxRsMetamodelBuilder;
    }

    public static int optionLength(String option) {
        return OPTIONS_STORE.getOptionLength(option);
    }

}
