# API Doclet

This project provides a JavaDoc doclet that collects various information 
about an API and uses one or more exchangeable reporters to create an
API documentation. 

## Sources of information

### JAX-RS 2.0 annotations ([JSR339 2.0 rev A](https://jcp.org/aboutJava/communityprocess/mrel/jsr339/index.html))

Supported annotations: 

- [`@ApplicationPath`](http://docs.oracle.com/javaee/6/api/javax/ws/rs/ApplicationPath.html)
- [`@Path`](http://docs.oracle.com/javaee/6/api/javax/ws/rs/Path.html)
- [`@Produces`](http://docs.oracle.com/javaee/6/api/javax/ws/rs/Produces.html)
- [`@Consumes`](http://docs.oracle.com/javaee/6/api/javax/ws/rs/Consumes.html)
- standard method designators 
    - [`@GET`](http://docs.oracle.com/javaee/6/api/javax/ws/rs/GET.html)
    - [`@POST`](http://docs.oracle.com/javaee/6/api/javax/ws/rs/POST.html)
    - [`@DELETE`](http://docs.oracle.com/javaee/6/api/javax/ws/rs/DELETE.html)
    - [`@HEAD`](http://docs.oracle.com/javaee/6/api/javax/ws/rs/HEAD.html)
    - [`@OPTIONS`](http://docs.oracle.com/javaee/6/api/javax/ws/rs/OPTIONS.html)
    - [`@POST`](http://docs.oracle.com/javaee/6/api/javax/ws/rs/POST.html)
    - [`@PUT`](http://docs.oracle.com/javaee/6/api/javax/ws/rs/PUT.html)
- custom method designators (annotations annotated [`@HttpMethod`](http://docs.oracle.com/javaee/6/api/javax/ws/rs/HttpMethod.html))
- params
    - [`@PathParam`](http://docs.oracle.com/javaee/6/api/javax/ws/rs/PathParam.html)
    - [`@QueryParam`](http://docs.oracle.com/javaee/6/api/javax/ws/rs/QueryParam.html)
    - [`@FormParam`](http://docs.oracle.com/javaee/6/api/javax/ws/rs/FormParam.html)
    - [`@HeaderParam`](http://docs.oracle.com/javaee/6/api/javax/ws/rs/HeaderParam.html)
    - [`@CookieParam`](http://docs.oracle.com/javaee/6/api/javax/ws/rs/CookieParam.html)
    - [`@MatrixParam`](http://docs.oracle.com/javaee/6/api/javax/ws/rs/MatrixParam.html)
- [`@DefaultValue`](http://docs.oracle.com/javaee/6/api/javax/ws/rs/DefaultValue.html)
- HTTP body payload
  
Not yet supported:

- [`@BeanParam`](http://docs.oracle.com/javaee/6/api/javax/ws/rs/BeanParam.html)

### BeanValidation ([JSR303 1.0 final](http://beanvalidation.org/1.0/spec/))

Currently **not** supported

### JavaDoc tags

#### Tags per javax.ws.rs.Application

annotation | format | description
--- | --- | --- 
@apiTitle | `string` | 
@apiVersion | `string` | 
@apiHost | `string` | 
@apiBasePath | `string` | 
@apiDescription | `string` |
@apiExternalDocs | `[description](url)` |
@apiTermsOfService | `string` | 
@apiContactName | `string` | 
@apiContactEmail | `string` | 
@apiContactUrl | `url` | 
@apiLicense |  `[name](url)` | 

#### Tags per resource class

annotation | format | description
--- | --- | --- 
@apiExternalDocs | `[description](url)` | 
@apiDescription | `string` | 

#### Tags per resource class' method

annotation | format | description
--- | --- | --- 
@apiOperationSummary | `string` | 
@apiDescription | `string` | 
@apiResponse | `[httpStatusCode](responseClass) description` | `(responseClass)` is optional

## Reporter

The doclet itself does not write any report but invokes any reporter on 
the class path.
 
A reporter implements ch.webmate.api.doclet.reporter.ApiReporter and is 
registered as a Java 6 service.

### Known reporters

#### Swagger 2

```xml
<groupId>ch.webmate.api.doclet</groupId>
<artifactId>api-doclet-swagger2-reporter</artifactId>
```

Generates reports conforming to the swagger spec v2.0

## Limitations

### MatrixParam

Swagger 2 does not support matrix style parameters 
([`@MatrixParam`](http://docs.oracle.com/javaee/6/api/javax/ws/rs/MatrixParam.html)).
The reporter will throw an error if it encounters a matrix style parameter.

### HTTP methods

Swagger 2 supports the http methods `GET`, `POST`, `DELETE`, `PUT` and `PATCH`. The 
reporter will throw an error if it encounters any other http method (e.g. `OPTIONS`).

## Usage

Simply add the doclet and the reporter of your choice to your build. 

The following example shows how to add the doclet and the swagger2 
reporter to a maven build.

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-javadoc-plugin</artifactId>
            <version>2.10.3</version>
            <executions>
                <execution>
                    <id>generate-swagger2-spec</id>
                    <goals>
                        <goal>javadoc</goal>
                    </goals>
                    <phase>generate-resources</phase>
                </execution>
            </executions>
            <configuration>
                <doclet>ch.webmate.api.doclet.ApiDoclet</doclet>
                <docletArtifact>
                    <groupId>ch.webmate.api.doclet</groupId>
                    <artifactId>api-doclet</artifactId>
                    <version>${project.version}</version>
                </docletArtifact>
                <!-- TODO: this currently doesn't work -->
                <additionalDependencies>
                    <additionalDependency>
                        <groupId>ch.webmate.api.doclet</groupId>
                        <artifactId>api-doclet-swagger2-reporter</artifactId>
                        <version>${project.version}</version>
                    </additionalDependency>
                </additionalDependencies>
                <useStandardDocletOptions>false</useStandardDocletOptions>
            </configuration>
        </plugin>
    </plugins>
</build>
```

## Contribution

Do not hesitate, we appreciate every contribution. Do not fear 
non-perfect Pull Requests, nobody is perfect.

When contributing please stick to the following guides:

### Branching

Please add every feature/bugfix in a separate branch and create a 
pull request to the develop branch.

We stick to the following naming conventions for branches:

- `feature/...` for new features
- `bugfix/...` for bugfixes
- `improvement/...` for improvements on existing features

### Commit messages

- Begin your commit message with a verb in the imperative. 
  (E.g. `Introduce foo bar`, `Fix baz`, ...)
- Try to have small, atomic commits.
- First line of a commit message should sum up your changes and should 
  not be longer than 50 characters.
- Second line should be empty
- Third and following lines can optionally contain a longer description

### Code style guidelines

TBD

The source is validated against a slightly customized `Sonar way` profile of SonarQube 5.1.

#### Customization

##### Classes from "sun.*" packages should not be used

Excludes com.sun.javadoc since there is no non-sun API.

## License

The MIT License (MIT)

Copyright (c) 2015 Sven Tschui

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.