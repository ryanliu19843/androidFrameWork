package com.mdx.framework.server.api.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.List;

public abstract class JsonData implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public JsonData() {
        
    }
    
    public JsonData(JSONObject object) throws Exception {
        this.build(object);
    }
    
    public int getErrorCode(JSONObject object) {
        return 0;
    }
    
    public String getErrorMsg(JSONObject object) {
        return "";
    }
    
    public boolean autoError() {
        return true;
    }
    
    public abstract void build(JSONObject object) throws Exception;
    
    public static String getJsonString(JSONObject json, String key) throws JSONException {
        if (json.has(key)) {
            return json.getString(key);
        }
        return null;
    }
    
    public static boolean getJsonBoolean(JSONObject json, String key) throws JSONException {
        if (json.has(key)) {
            return json.getBoolean(key);
        }
        return false;
    }
    
    public static long getJsonLong(JSONObject json, String key) throws JSONException {
        if (json.has(key)) {
            return json.getLong(key);
        }
        return 0;
    }
    
    public static int getJsonInt(JSONObject json, String key) throws JSONException {
        if (json.has(key)) {
            return json.getInt(key);
        }
        return 0;
    }
    
    public static double getJsonDouble(JSONObject json, String key) throws JSONException {
        if (json.has(key)) {
            return json.getDouble(key);
        }
        return 0;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void getJsonArray(JSONArray jsonarray, Class<?> clas, List list) throws Exception {
        Constructor<?> cst;
        cst = clas.getDeclaredConstructor(JSONObject.class);
        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject object = jsonarray.getJSONObject(i);
            list.add(cst.newInstance(object));
        }
    }
    
    @SuppressWarnings("rawtypes")
    public static void getJsonArray(JSONObject json, String key, Class<?> clas, List list) throws Exception {
        if (json.has(key) && json.get(key) instanceof JSONArray) {
            JSONArray jsona = json.getJSONArray(key);
            getJsonArray(jsona, clas, list);
        }
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void getJsonArray(JSONArray jsonarray, List list) throws Exception {
        for (int i = 0; i < jsonarray.length(); i++) {
            Object object = jsonarray.get(i);
            list.add(object.toString());
        }
    }
    
    @SuppressWarnings("rawtypes")
    public static void getJsonArray(JSONObject json, String key, List list) throws Exception {
        if (json.has(key) && json.get(key) instanceof JSONArray) {
            JSONArray jsona = json.getJSONArray(key);
            getJsonArray(jsona, list);
        }
    }
}
