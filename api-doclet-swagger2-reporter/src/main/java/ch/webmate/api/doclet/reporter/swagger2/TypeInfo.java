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

import com.sun.javadoc.Type;

import java.util.List;

/**
 * Created by sventschui on 10.05.15.
 */
public class TypeInfo {

    protected String type = null;

    protected String format = null;

    protected Boolean allowEmptyValue = null;

    protected TypeInfo items = null;

    protected String collectionFormat = null;

    protected Object defaultValue = null;

    protected Double maximum = null;

    protected Boolean exclusiveMaximum = null;

    protected Double minimum = null;

    protected Boolean exclusiveMinimum = null;

    protected Integer maxLength = null;

    protected Integer minLength = null;

    protected String pattern = null;

    protected Integer maxItems = null;

    protected Integer minItems = null;

    protected Boolean uniqueItems = null;

    protected List<String> allowedValues = null;

    protected Double multipleOf = null;

    public static TypeInfo createTypeInfoFromType(Type type) {
        return createTypeInfoFromType(type, false);
    }

    public static TypeInfo createTypeInfoFromType(Type type, boolean ignoreArray) {

        TypeInfo typeInfo = new TypeInfo();

        typeInfo.setType("string");

        return typeInfo;

//        // TODO: implement
//
//        if(isCollection(type)) {
//            // TODO
//        } else if(!ignoreArray && isArray(type)) {
//
//            typeInfo.setType("array");
//            typeInfo.setItems(createTypeInfoFromType(type, true));
//
//        } else if (isString(type)) {
//
//            typeInfo.setType("string");
//
//            // TODO:
//            typeInfo.setAllowEmptyValue(null);
//            typeInfo.setDefaultValue(null);
//            typeInfo.setAllowedValues(null);
//            typeInfo.setMinLength(null);
//            typeInfo.setMaxLength(null);
//            typeInfo.setPattern(null);
//
//        } else if(isDouble(type) || isFloat(type)) {
//
//            typeInfo.setType("number");
//
//            if(isDouble(type)) {
//                typeInfo.setFormat("double");
//            } else {
//                typeInfo.setFormat("float");
//            }
//
//            // TODO:
//            typeInfo.setAllowEmptyValue(null);
//            typeInfo.setDefaultValue(null);
//            typeInfo.setAllowedValues(null);
//            typeInfo.setMaximum(null);
//            typeInfo.setExclusiveMaximum(null);
//            typeInfo.setMinimum(null);
//            typeInfo.setExclusiveMinimum(null);
//            typeInfo.setMultipleOf(null);
//
//        } else if (isInteger(type) || isLong(type)) {
//
//            typeInfo.setType("integer");
//
//            if (isLong(type)) {
//                typeInfo.setFormat("int64");
//            } else {
//                typeInfo.setFormat("int32");
//            }
//
//            // TODO:
//            typeInfo.setAllowEmptyValue(null);
//            typeInfo.setDefaultValue(null);
//            typeInfo.setAllowedValues(null);
//            typeInfo.setMaximum(null);
//            typeInfo.setExclusiveMaximum(null);
//            typeInfo.setMinimum(null);
//            typeInfo.setExclusiveMinimum(null);
//            typeInfo.setMultipleOf(null);
//
//        } else if(isByte(type)) {
//
//            typeInfo.setType("string");
//            typeInfo.setFormat("byte");
//
//            // TODO:
//            typeInfo.setAllowEmptyValue(null);
//            typeInfo.setDefaultValue(null);
//            typeInfo.setAllowedValues(null);
//            typeInfo.setMinLength(null);
//            typeInfo.setMaxLength(null);
//            typeInfo.setPattern(null);
//
//        } else if(isBool(type)) {
//
//            typeInfo.setType("boolean");
//
//            // TODO:
//            typeInfo.setAllowEmptyValue(null);
//            typeInfo.setDefaultValue(null);
//            typeInfo.setAllowedValues(null);
//
//        } else if(isDate(type)) {
//
//            typeInfo.setType("string");
//            typeInfo.setFormat("date-time");
//
//            // TODO:
//            typeInfo.setAllowEmptyValue(null);
//            typeInfo.setDefaultValue(null);
//            typeInfo.setAllowedValues(null);
//            typeInfo.setMinLength(null);
//            typeInfo.setMaxLength(null);
//            typeInfo.setPattern(null);
//
//        }

    }

//    private static boolean isCollection(Type type) {
//        // TODO
//        return false;
//    }
//
//    private static boolean isArray(Type type) {
//        return type.dimension() == null || "".equals(type.dimension());
//    }
//
//    private static boolean isBool(Type type) {
//        return Arrays.asList(
//                "boolean",
//                Boolean.class.getCanonicalName()
//        ).contains(type.toString());
//    }
//
//    private static boolean isByte(Type type) {
//        return Arrays.asList(
//                "byte",
//                Short.class.getCanonicalName()
//        ).contains(type.toString());
//    }
//
//    private static boolean isLong(Type type) {
//        return Arrays.asList(
//                "long",
//                Long.class.getCanonicalName(),
//                BigInteger.class.getCanonicalName()
//        ).contains(type.toString());
//    }
//
//    private static boolean isInteger(Type type) {
//        return Arrays.asList(
//                "int",
//                Integer.class.getCanonicalName()
//        ).contains(type.toString());
//    }
//
//    private static boolean isDouble(Type type) {
//        return Arrays.asList(
//                "double",
//                Double.class.getCanonicalName(),
//                BigDecimal.class.getCanonicalName()
//        ).contains(type.toString());
//    }
//
//    private static boolean isFloat(Type type) {
//        return Arrays.asList(
//                "float",
//                Float.class.getCanonicalName()
//        ).contains(type.toString());
//    }
//
//    private static boolean isString(Type type) {
//        return "java.lang.String".equals(type.toString());
//    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Boolean isAllowEmptyValue() {
        return allowEmptyValue;
    }

    public void setAllowEmptyValue(Boolean allowEmptyValue) {
        this.allowEmptyValue = allowEmptyValue;
    }

    public TypeInfo getItems() {
        return items;
    }

    public void setItems(TypeInfo items) {
        this.items = items;
    }

    public String getCollectionFormat() {
        return collectionFormat;
    }

    public void setCollectionFormat(String collectionFormat) {
        this.collectionFormat = collectionFormat;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Double getMaximum() {
        return maximum;
    }

    public void setMaximum(Double maximum) {
        this.maximum = maximum;
    }

    public Boolean isExclusiveMaximum() {
        return exclusiveMaximum;
    }

    public void setExclusiveMaximum(Boolean exclusiveMaximum) {
        this.exclusiveMaximum = exclusiveMaximum;
    }

    public Double getMinimum() {
        return minimum;
    }

    public void setMinimum(Double minimum) {
        this.minimum = minimum;
    }

    public Boolean isExclusiveMinimum() {
        return exclusiveMinimum;
    }

    public void setExclusiveMinimum(Boolean exclusiveMinimum) {
        this.exclusiveMinimum = exclusiveMinimum;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public Integer getMinLength() {
        return minLength;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Integer getMaxItems() {
        return maxItems;
    }

    public void setMaxItems(Integer maxItems) {
        this.maxItems = maxItems;
    }

    public Integer getMinItems() {
        return minItems;
    }

    public void setMinItems(Integer minItems) {
        this.minItems = minItems;
    }

    public Boolean isUniqueItems() {
        return uniqueItems;
    }

    public void setUniqueItems(Boolean uniqueItems) {
        this.uniqueItems = uniqueItems;
    }

    public List<String> getAllowedValues() {
        return allowedValues;
    }

    public void setAllowedValues(List<String> allowedValues) {
        this.allowedValues = allowedValues;
    }

    public Double getMultipleOf() {
        return multipleOf;
    }

    public void setMultipleOf(Double multipleOf) {
        this.multipleOf = multipleOf;
    }

}
