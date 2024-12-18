package com.github.tess1o.ecoflow.client.http;

import com.github.tess1o.ecoflow.exceptions.EcoflowInvalidParameterException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QueryStringTest {

    /**
     * Test for a simple flat JSON object with key-value pairs.
     */
    @Test
    void testParseSimpleFlatJson() {
        JSONObject input = new JSONObject();
        input.put("key2", "value2");
        input.put("key1", "value1");

        QueryString queryString = new QueryString(input);
        String result = queryString.parse();

        assertEquals("key1=value1&key2=value2", result);
    }

    /**
     * Test with a null JSON object.
     */
    @Test
    void testParseNullJson() {
        assertThrows(EcoflowInvalidParameterException.class, () -> new QueryString(null).parse());
    }

    /**
     * Test with an empty JSON object.
     */
    @Test
    void testParseEmptyJson() {
        JSONObject input = new JSONObject();

        QueryString queryString = new QueryString(input);
        assertThrows(EcoflowInvalidParameterException.class, queryString::parse);
    }

    /**
     * Test with nested JSON objects.
     */
    @Test
    void testParseWithNestedJsonObject() {
        JSONObject nested = new JSONObject();
        nested.put("key2", "value2");

        JSONObject input = new JSONObject();
        input.put("key1", nested);

        QueryString queryString = new QueryString(input);
        String result = queryString.parse();

        assertEquals("key1.key2=value2", result);
    }

    /**
     * Test with a JSON object containing an array.
     */
    @Test
    void testParseWithJsonArray() {
        JSONArray array = new JSONArray();
        array.put("value1");
        array.put("value2");

        JSONObject input = new JSONObject();
        input.put("key", array);

        QueryString queryString = new QueryString(input);
        String result = queryString.parse();

        assertEquals("key[0]=value1&key[1]=value2", result);
    }

    /**
     * Test with a JSON object containing both arrays and nested objects.
     */
    @Test
    void testParseWithMixedJsonStructure() {
        JSONArray array = new JSONArray();
        array.put("value1");

        JSONObject nested = new JSONObject();
        nested.put("innerKey", "innerValue");

        JSONObject input = new JSONObject();
        input.put("key1", array);
        input.put("key2", nested);

        QueryString queryString = new QueryString(input);
        String result = queryString.parse();

        assertEquals("key1[0]=value1&key2.innerKey=innerValue", result);
    }

    /**
     * Test with invalid parameters, such as null keys in a nested JSON object.
     */
    @Test
    void testParseWithInvalidParameters() {
        JSONObject nested = new JSONObject();
        nested.put(null, "value");

        JSONObject input = new JSONObject();
        input.put("key1", nested);

        QueryString queryString = new QueryString(input);
        assertThrows(EcoflowInvalidParameterException.class, queryString::parse);
    }

    /**
     * Test with a mixed-type JSONArray.
     */
    @Test
    void testParseWithMixedTypeJsonArray() {
        JSONArray array = new JSONArray();
        array.put("value1");
        array.put(5);
        array.put(true);

        JSONObject input = new JSONObject();
        input.put("key", array);

        QueryString queryString = new QueryString(input);
        String result = queryString.parse();

        assertEquals("key[0]=value1&key[1]=5&key[2]=true", result);
    }

    /**
     * Test with deeply nested JSON objects.
     */
    @Test
    void testParseWithDeeplyNestedJson() {
        JSONObject level3 = new JSONObject();
        level3.put("key3", "value3");

        JSONObject level2 = new JSONObject();
        level2.put("key2", level3);

        JSONObject input = new JSONObject();
        input.put("key1", level2);

        QueryString queryString = new QueryString(input);
        String result = queryString.parse();

        assertEquals("key1.key2.key3=value3", result);
    }
}