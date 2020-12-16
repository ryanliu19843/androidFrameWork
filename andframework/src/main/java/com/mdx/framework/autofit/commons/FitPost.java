package com.mdx.framework.autofit.commons;

import com.mdx.framework.server.api.ApiUpdate;

import java.util.HashMap;

/**
 * Created by ryan on 2017/10/19.
 */

public class FitPost {
    public int resid;
    public int recycleviewid;
    public Object value;
    public HashMap<String,Object> params=new HashMap<>();
    public ApiUpdate apiUpdate;


    public Object get(String key){
        return params.get(key);
    }

    public void put(String key,Object value){
        params.put(key,value);
    }

}
