package com.mdx.framework.autofit.commons;

import com.mdx.framework.commons.ParamsManager;
import com.mdx.framework.widget.pagerecycleview.autofit.AutoCard;

import java.util.HashMap;

public class AutoParams extends HashMap<String, Object> {

    public static AutoParams create() {
        return new AutoParams();
    }

    public Object V(String key, Object def) {
        Object obj = ParamsManager.getValue(key, this);
        return obj != null ? obj : def;
    }

    public Object V(String key) {
        Object obj = ParamsManager.getValue(key, this);
        return obj;
    }


    public Object get(String key) {
        Object retn = super.get(key);
        if (retn == null) {
            return ParamsManager.getValue(key);
        }
        return retn;
    }
}
