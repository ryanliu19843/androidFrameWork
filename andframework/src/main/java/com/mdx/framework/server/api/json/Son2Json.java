package com.mdx.framework.server.api.json;

import android.util.Log;

import com.mdx.framework.commons.MException;
import com.mdx.framework.commons.verify.Md5;
import com.mdx.framework.log.MLog;
import com.mdx.framework.server.api.Son;
import com.mdx.framework.server.api.UpdateOne;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Son2Json extends Son {
    private static final long serialVersionUID = 1L;
    
	public Son2Json(String ins, UpdateOne updateone) {
		
		
    	String metod=updateone.getId();
    	JsonData jsonData=(JsonData) updateone.mbuild; 
    	int type=updateone.getType();
        int errorType=updateone.getErrorType();
        this.Md5str=updateone.getMd5str();
        this.sonMd5= Md5.mD5(ins);
        HashMap<String, String> map=updateone.getMap();
        
        
        ins = ins.trim();
        if(ins.startsWith("[")){
            ins="{\"data\":"+ins+"}";
        }
        MLog.D(MLog.SYS_RUN,ins);
        this.mMethod=metod;
        this.mErrMethod=mMethod;
        this.mServerMethod=metod;
        this.mBuild=jsonData;
        this.mErrorType=errorType;
        this.type=type;
        this.mParams=map;
        try {
            JSONObject json = new JSONObject(ins);
            if (jsonData == null) {
                if (json.has("error")) {
                    String str = json.getString("error");
                    this.mError=Integer.parseInt(str == null ? "0" : str);
                }
                if (json.has("errormsg")) {
                    this.msg=json.getString("errormsg");
                }
            }
            if (this.getBuild() == null) {
                this.mBuild=(json);
            } else {
                ((JsonData) this.getBuild()).build(json);
            }
            if (jsonData != null) {
                if (jsonData.autoError()) {
                    if (json.has("error")) {
                        String str = json.getString("error");
                        this.mError=Integer.parseInt(str == null ? "0" : str);
                    }
                    if (json.has("errormsg")) {
                        this.msg=json.getString("errormsg");
                    }
                } else {
                    this.mError=jsonData.getErrorCode(json);
                    this.msg=jsonData.getErrorMsg(json);
                }
            }
        }
        catch (JSONException e) {
            Log.e("frame", Log.getStackTraceString(e));
            this.mError=96;
            this.msg="JsonError";
        }
        catch (MException e) {
            Log.e("frame", Log.getStackTraceString(e));
            this.mError=e.getCode();
            this.msg=e.getMessage();
        }
        catch (Exception e) {
            Log.e("frame", Log.getStackTraceString(e));
            this.mError=97;
            this.msg="error";
        }
    }
}
