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

package test;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by sventschui on 07.05.15.
 *
 * SOme
 *
 * long text dawd awd awdawd
 *
 * @apiDescription Test resource one
 * @apiExternalDocs [Some Ext Docs on class](http://foobar.com)
 */
@Path("/resource/")
@Produces(MediaType.APPLICATION_JSON)
public class TestResourceOne {

    private static final String PATH = "/operation/{foobar}";

    /**
     * Foobar description
     *
     * @apiOperationSummary Test summary
     * @apiExternalDocs [Some Ext Docs on method](http://foobar.com)
     * @apiResponse [200](test.TestResponsePojoOne) Blah blah *foo* __bar__
     * @deprecated Blubb
     * @param someHeader Test 123
     * @param test 123 <br />
     * awdawd
     *
     *     Cody
     */
    @Path(PATH)
    @POST
    @Produces({"foo/bar", "test/prod"})
    @Consumes({"bar/foo", "test/cons"})
    public void testMethodOneOne(@PathParam("foobar") @DefaultValue("fooBarDefault") String foobar, @HeaderParam("foo") String someHeader, TestBodyParam test) {

    }

    @Deprecated
    @GET
    public void testMethodOneTwo() {

    }

}
