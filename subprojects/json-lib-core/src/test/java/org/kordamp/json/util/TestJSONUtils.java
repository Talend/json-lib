/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2006-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kordamp.json.util;

import junit.framework.TestCase;
import org.kordamp.json.JSONArray;
import org.kordamp.json.JSONException;
import org.kordamp.json.JSONObject;
import org.kordamp.json.JsonConfig;
import org.kordamp.json.JsonStandard;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Andres Almiray
 */
public class TestJSONUtils extends TestCase {

    private static Map<String, String> valuesMap;
    public TestJSONUtils(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
        valuesMap = new LinkedHashMap<>();

        valuesMap.put("key1", "null");
        valuesMap.put("key2", "not_null");
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(TestJSONUtils.class);
    }

    public void testDoubleToString_infinite() {
        assertEquals("null", JSONUtils.doubleToString(Double.POSITIVE_INFINITY));
    }

    public void testDoubleToString_nan() {
        assertEquals("null", JSONUtils.doubleToString(Double.NaN));
    }

    public void testDoubleToString_trailingZeros() {
        assertEquals("200.0", JSONUtils.doubleToString(200.00000));
    }

    public void testDoubleToString() {
        Map<String, Double> expected = new HashMap<>();
        expected.put("200.0", 200.0d);
        expected.put("200.0", 200.000d);
        expected.put("200.1", 200.1d);
        expected.put("200.1", 200.10d);
        expected.put("200.1", 200.1000d);
        expected.put("200.12345", 200.12345d);
        expected.put("200.12345", 200.123450000d);
        expected.put("200.101", 200.101d);
        expected.put("1.0E-8", 1.0E-8);
        expected.put("200.0", 200d);

        for(String key : expected.keySet()){
            assertEquals(key,JSONUtils.doubleToString(expected.get(key)));
        }
    }

    public void testGetFunctionParams() {
        assertEquals("", JSONUtils.getFunctionParams("function()"));
        assertEquals("a", JSONUtils.getFunctionParams("function(a)"));
        assertEquals("a,b", JSONUtils.getFunctionParams("function(a,b)"));
        assertEquals("", JSONUtils.getFunctionParams("notAFunction"));
    }

    public void testIsArray() {
        assertTrue(JSONUtils.isArray(new Object[0]));
        assertTrue(JSONUtils.isArray(new boolean[0]));
        assertTrue(JSONUtils.isArray(new byte[0]));
        assertTrue(JSONUtils.isArray(new char[0]));
        assertTrue(JSONUtils.isArray(new short[0]));
        assertTrue(JSONUtils.isArray(new int[0]));
        assertTrue(JSONUtils.isArray(new long[0]));
        assertTrue(JSONUtils.isArray(new float[0]));
        assertTrue(JSONUtils.isArray(new double[0]));

        // two dimensions
        assertTrue(JSONUtils.isArray(new Object[0][0]));
        assertTrue(JSONUtils.isArray(new boolean[0][0]));
        assertTrue(JSONUtils.isArray(new byte[0][0]));
        assertTrue(JSONUtils.isArray(new char[0][0]));
        assertTrue(JSONUtils.isArray(new short[0][0]));
        assertTrue(JSONUtils.isArray(new int[0][0]));
        assertTrue(JSONUtils.isArray(new long[0][0]));
        assertTrue(JSONUtils.isArray(new float[0][0]));
        assertTrue(JSONUtils.isArray(new double[0][0]));

        // collections
        assertTrue(JSONUtils.isArray(Collections.EMPTY_SET));
        assertTrue(JSONUtils.isArray(Collections.EMPTY_LIST));

        // jsonArray
        assertTrue(JSONUtils.isArray(new JSONArray()));
    }

    public void testIsFunction() {
        assertTrue(JSONUtils.isFunction("function(){ return a; }"));
        assertTrue(JSONUtils.isFunction("function (){ return a; }"));
        assertTrue(JSONUtils.isFunction("function() { return a; }"));
        assertTrue(JSONUtils.isFunction("function () { return a; }"));
        assertTrue(JSONUtils.isFunction("function(a){ return a; }"));
    }

    public void testNumberToString_null() {
        try {
            JSONUtils.numberToString(null);
            fail("Should have thrown a JSONException");
        } catch (JSONException expected) {
            // ok
        }
    }

    public void testNumberToString() {
        Map<String, Number> expected = new HashMap<>();

        expected.put("0", Integer.valueOf("00000"));
        expected.put("123", Integer.valueOf("123"));
        expected.put("-123", Integer.valueOf("-123"));

        expected.put("0.0", Double.valueOf("0"));
        expected.put("0.0", Double.valueOf("0.00000"));
        expected.put("120.0001", Double.valueOf("120.0001000"));
        expected.put("-120.0001", Double.valueOf("-120.0001000"));

        expected.put("0.0", Float.valueOf("0"));
        expected.put("0.0", Float.valueOf("0.00000"));
        expected.put("120.0001", Float.valueOf("120.0001000"));
        expected.put("-120.0001", Float.valueOf("-120.0001000"));

        for(String key : expected.keySet()) {
            assertEquals(key, JSONUtils.numberToString(expected.get(key)));
        }
    }

    public void testNumberToString_double() {
        assertEquals("1.0", JSONUtils.numberToString(1.0d));
    }

    public void testNumberToString_float() {
        assertEquals("1.0", JSONUtils.numberToString(1.0f));
    }

    public void testQuote_emptyString() {
        assertEquals("\"\"", JSONUtils.quote(""));
    }

    public void testQuote_escapeChars() {
        assertEquals("\"\\b\\t\\n\\r\\f\"", JSONUtils.quote("\b\t\n\r\f"));
    }

    public void testQuote_nullString() {
        assertEquals("\"\"", JSONUtils.quote(null));
    }

    public void testStripQuotes_singleChar_doubleeQuote() {
        String quoted = "\"";
        String actual = JSONUtils.stripQuotes(quoted);
        assertEquals(quoted, actual);
    }

    public void testStripQuotes_singleChar_singleQuote() {
        String quoted = "'";
        String actual = JSONUtils.stripQuotes(quoted);
        assertEquals(quoted, actual);
    }

    public void testStripQuotes_twoChars_doubleeQuote() {
        String quoted = "\"\"";
        String actual = JSONUtils.stripQuotes(quoted);
        assertEquals("", actual);
    }

    public void testStripQuotes_twoChars_singleQuote() {
        String quoted = "''";
        String actual = JSONUtils.stripQuotes(quoted);
        assertEquals("", actual);
    }

    public void testValidity_infiniteDouble() {
        try {
            JSONUtils.testValidity(Double.valueOf(Double.POSITIVE_INFINITY));
            fail("Should have thrown a JSONException");
        } catch (JSONException expected) {
            // ok
        }
    }

    public void testValidity_infiniteFloat() {
        try {
            JSONUtils.testValidity(Float.valueOf(Float.POSITIVE_INFINITY));
            fail("Should have thrown a JSONException");
        } catch (JSONException expected) {
            // ok
        }
    }

    public void testValidity_nanDouble() {
        try {
            JSONUtils.testValidity(Double.valueOf(Double.NaN));
            fail("Should have thrown a JSONException");
        } catch (JSONException expected) {
            // ok
        }
    }

    public void testValidity_nanFloat() {
        try {
            JSONUtils.testValidity(Float.valueOf(Float.NaN));
            fail("Should have thrown a JSONException");
        } catch (JSONException expected) {
            // ok
        }
    }

    public void testNullStringsWrapped() {
        JsonConfig config = new JsonConfig();
        config.setJsonStandard(JsonStandard.WRAP_NULL_STRINGS);

        JSONObject jsonObject = new JSONObject();
        jsonObject.putAll(valuesMap, config);

        String resultingString = JSONUtils.jsonToStandardizedString(jsonObject, JsonStandard.WRAP_NULL_STRINGS);
        assertFalse("Wrapping null strings standard's broken", Objects.equals(jsonObject.toString(), resultingString));
        assertTrue(resultingString.contains("\"null\""));
    }

    public void testNullStringsUnwrapped() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.putAll(valuesMap);

        String resultingString = JSONUtils.jsonToStandardizedString(jsonObject, JsonStandard.WRAP_NULL_STRINGS);
        assertEquals(jsonObject.toString(), resultingString);
    }

    public void testNullStringsOnArrayWrapped() {
        JsonConfig config = new JsonConfig();
        config.setJsonStandard(JsonStandard.WRAP_NULL_STRINGS);

        JSONArray jsonArray = new JSONArray();
        jsonArray.add("abc");
        jsonArray.add("null", config);
        jsonArray.add(null);

        String resultingString = JSONUtils.jsonToStandardizedString(jsonArray, JsonStandard.WRAP_NULL_STRINGS);;
        assertFalse("Wrapping null strings standard's broken", Objects.equals(jsonArray.toString(), resultingString));
        assertTrue(resultingString.contains("\"null\""));
    }

    public void testNullStringsOnArrayUnwrapped() {
        JSONArray jsonArray = new JSONArray();
        jsonArray.add("abc");
        jsonArray.add("null");
        jsonArray.add(null);

        String resultingString = JSONUtils.jsonToStandardizedString(jsonArray, JsonStandard.LEGACY);
        assertEquals(jsonArray.toString(), resultingString);
    }
}