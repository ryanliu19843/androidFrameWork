package com.udows.wgh.proto;

import com.mdx.framework.server.api.json.JsonData;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ryan on 2017/11/4.
 */

public abstract class MJsonData extends JsonData{


    public int getErrorCode(JSONObject json) {
        if (json.has("code")) {
            int code = 0;
            try {
                code = json.getInt("code");
            } catch (Exception e) {
            }
            return code==200?0:code;
        }
        return 0;
    }

    public String getErrorMsg(JSONObject json) {
        if (json.has("result")) {
            try {
                return json.getString("result");
            } catch (JSONException e) {
            }
        }
        return "";
    }

    public boolean autoError() {
        return false;
    }
}
