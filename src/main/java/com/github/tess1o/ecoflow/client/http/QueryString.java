package com.github.tess1o.http;

import com.github.tess1o.exceptions.EcoflowInvalidParameterException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class QueryString {

    private final JSONObject request;

    public QueryString(JSONObject request) {
        this.request = request;
    }

    public String get() {
        SortedMap<String, Object> query = requestToMap(this.request);
        if (query.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        query.keySet().forEach(key -> builder.append(key)
                .append("=")
                .append(query.get(key))
                .append("&")
        );

        if (builder.isEmpty()) {
            return "";
        }
        return builder.substring(0, builder.length() - 1);
    }
//    public String get() {
//        SortedMap<String, Object> query = requestToMap(this.request);
//        if (query.isEmpty()) {
//            return "";
//        }
//
//        return query.entrySet().stream()
//                .map(entry -> entry.getKey() + "=" + entry.getValue())
//                .collect(Collectors.joining("&"));
//    }

    private SortedMap<String, Object> requestToMap(JSONObject jsonObject) {
        if (jsonObject == null || jsonObject.isEmpty()) {
            throw new EcoflowInvalidParameterException("parameter invalid");
        }
        SortedMap<String, Object> map = new TreeMap<>();
        for (String key : jsonObject.keySet()) {
            Object value = jsonObject.get(key);
            map.putAll(getByObject(key, value));
        }
        return map;
    }

    private Map<String, Object> getByObject(String key, Object value) {
        if (key == null || key.isEmpty() || value == null) {
            throw new EcoflowInvalidParameterException("parameter invalid");
        }
        if (value instanceof JSONArray) {
            return getByJsonArray(key, (JSONArray) value);
        } else if (value instanceof JSONObject) {
            return getByJSONObject(key, (JSONObject) value);
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put(key, value);
            return map;
        }
    }

    private Map<String, Object> getByJsonArray(String key, JSONArray value) {
        if (key == null || key.isEmpty() || value == null || value.isEmpty()) {
            throw new EcoflowInvalidParameterException("parameter invalid");
        }
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < value.length(); i++) {
            map.putAll(getByObject(getArrayKey(key, i), value.get(i)));
        }
        return map;
    }

    private Map<String, Object> getByJSONObject(String key, JSONObject value) {
        if (key == null || key.isEmpty() || value == null || value.isEmpty()) {
            throw new EcoflowInvalidParameterException("parameter invalid");
        }
        Map<String, Object> map = new HashMap<>();
        for (String innerKey : value.keySet()) {
            map.putAll(getByObject(getObjectKey(key, innerKey), value.get(innerKey)));
        }
        return map;
    }

    private String getObjectKey(String key, String innerKey) {
        return key + "." + innerKey;
    }

    private String getArrayKey(String key, int index) {
        return key + "[" + index + "]";
    }
}
