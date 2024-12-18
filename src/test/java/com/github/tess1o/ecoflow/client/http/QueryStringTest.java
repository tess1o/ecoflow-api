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
    void testToQueryStringSimpleFlatJson() {
        JSONObject input = new JSONObject();
        input.put("key2", "value2");
        input.put("key1", "value1");

        QueryString queryString = new QueryString(input);
        String result = queryString.toQueryString();

        assertEquals("key1=value1&key2=value2", result);
    }

    /**
     * Test with a null JSON object.
     */
    @Test
    void testToQueryStringNullJson() {
        assertThrows(EcoflowInvalidParameterException.class, () -> new QueryString((JSONObject) null).toQueryString());
    }

    /**
     * Test with an empty JSON object.
     */
    @Test
    void testToQueryStringEmptyJson() {
        JSONObject input = new JSONObject();

        QueryString queryString = new QueryString(input);
        assertThrows(EcoflowInvalidParameterException.class, queryString::toQueryString);
    }

    /**
     * Test with nested JSON objects.
     */
    @Test
    void testToQueryStringWithNestedJsonObject() {
        JSONObject nested = new JSONObject();
        nested.put("key2", "value2");

        JSONObject input = new JSONObject();
        input.put("key1", nested);

        QueryString queryString = new QueryString(input);
        String result = queryString.toQueryString();

        assertEquals("key1.key2=value2", result);
    }

    /**
     * Test with a JSON object containing an array.
     */
    @Test
    void testToQueryStringWithJsonArray() {
        JSONArray array = new JSONArray();
        array.put("value1");
        array.put("value2");

        JSONObject input = new JSONObject();
        input.put("key", array);

        QueryString queryString = new QueryString(input);
        String result = queryString.toQueryString();

        assertEquals("key[0]=value1&key[1]=value2", result);
    }

    /**
     * Test with a JSON object containing both arrays and nested objects.
     */
    @Test
    void testToQueryStringWithMixedJsonStructure() {
        JSONArray array = new JSONArray();
        array.put("value1");

        JSONObject nested = new JSONObject();
        nested.put("innerKey", "innerValue");

        JSONObject input = new JSONObject();
        input.put("key1", array);
        input.put("key2", nested);

        QueryString queryString = new QueryString(input);
        String result = queryString.toQueryString();

        assertEquals("key1[0]=value1&key2.innerKey=innerValue", result);
    }

    /**
     * Test with a mixed-type JSONArray.
     */
    @Test
    void testToQueryStringWithMixedTypeJsonArray() {
        JSONArray array = new JSONArray();
        array.put("value1");
        array.put(5);
        array.put(true);

        JSONObject input = new JSONObject();
        input.put("key", array);

        QueryString queryString = new QueryString(input);
        String result = queryString.toQueryString();

        assertEquals("key[0]=value1&key[1]=5&key[2]=true", result);
    }

    /**
     * Test with deeply nested JSON objects.
     */
    @Test
    void testToQueryStringWithDeeplyNestedJson() {
        JSONObject level3 = new JSONObject();
        level3.put("key3", "value3");

        JSONObject level2 = new JSONObject();
        level2.put("key2", level3);

        JSONObject input = new JSONObject();
        input.put("key1", level2);

        QueryString queryString = new QueryString(input);
        String result = queryString.toQueryString();

        assertEquals("key1.key2.key3=value3", result);
    }

    @Test
    void testToQueryStringWithNestedParams(){
        String jsonString = """
                {
                    "sn" : "123456789",
                    "params" :{
                        "cmdSet" : 11,
                        "id" : 24,
                        "eps" : 0
                    }
                }
                """;
    
        JSONObject input = new JSONObject(jsonString);
        QueryString queryString = new QueryString(input);
        String result = queryString.toQueryString();

        assertEquals("params.cmdSet=11&params.eps=0&params.id=24&sn=123456789", result);
    }

    @Test
    void testToQueryStringWithNestedArrays(){
        String jsonString = """
                {
                    "name" : "demo1" ,
                    "ids" :[1, 2, 3],
                    "deviceInfo" :{
                        "id" :1
                    },
                    "deviceList" :[
                        {
                            "id" :1
                        },
                        {
                            "id" :2
                        }
                    ]
                }
                """;
        JSONObject input = new JSONObject(jsonString);
        QueryString queryString = new QueryString(input);
        String result = queryString.toQueryString();

        assertEquals("deviceInfo.id=1&deviceList[0].id=1&deviceList[1].id=2&ids[0]=1&ids[1]=2&ids[2]=3&name=demo1", result);
    }
}