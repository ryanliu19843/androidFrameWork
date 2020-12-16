package com.mdx.framework.server.api.webservice;

import android.util.Log;

import com.mdx.framework.commons.MException;
import com.mdx.framework.commons.verify.Md5;
import com.mdx.framework.log.MLog;
import com.mdx.framework.server.api.Son;
import com.mdx.framework.server.api.UpdateOne;
import com.mdx.framework.server.api.json.JsonData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Son2WebService extends Son {
	private static final long serialVersionUID = 1L;

	public Son2WebService(String ins, UpdateOne updateone) {

		String metod = updateone.getId();
		JsonData jsonData = (JsonData) updateone.mbuild;
		this.Md5str=updateone.getMd5str();
		int type = updateone.getType();
		int errorType = updateone.getErrorType();
		this.sonMd5= Md5.mD5(ins);
		HashMap<String, String> map = updateone.getMap();

		ins = ins.replace("\n", "\\n").replace("\t", "\\t").trim();
		if (ins.startsWith("[")) {
			ins = "{\"data\":" + ins + "}";
		}
		MLog.D(MLog.SYS_RUN, ins);
		this.mMethod = metod;
		this.mErrMethod = mMethod;
		this.mServerMethod = metod;
		this.mBuild = jsonData;
		this.type = type;
		this.mErrorType = errorType;
		this.mParams = map;
		try {
			JSONObject json = new JSONObject(ins);
			if (jsonData == null) {
				if (json.has("error")) {
					String str = json.getString("error");
					mError = Integer.parseInt(str == null ? "0" : str);
				}
				if (json.has("errormsg")) {
					msg = json.getString("errormsg");
				}
			}
			if (this.getBuild() == null) {
				this.mBuild = json;
			} else {
				((JsonData) this.getBuild()).build(json);
			}
			if (jsonData != null) {
				if (jsonData.autoError()) {
					if (json.has("error")) {
						String str = json.getString("error");
						mError = Integer.parseInt(str == null ? "0" : str);
					}
					if (json.has("errormsg")) {
						msg = json.getString("errormsg");
					}
				} else {
					mError = jsonData.getErrorCode(json);
					msg = jsonData.getErrorMsg(json);
				}
			}
		} catch (JSONException e) {
			Log.e("frame", Log.getStackTraceString(e));
			mError = 96;
			msg = "JsonError";
		} catch (MException e) {
			Log.e("frame", Log.getStackTraceString(e));
			mError = e.getCode();
			msg = e.getMessage();
		} catch (Exception e) {
			Log.e("frame", Log.getStackTraceString(e));
			mError = 97;
			msg = "error";
		}
	}
}
