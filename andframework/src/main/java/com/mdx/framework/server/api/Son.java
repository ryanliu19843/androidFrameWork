package com.mdx.framework.server.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Son implements Serializable {
    private static final long serialVersionUID = 1L;

    public Son cacheSon = null;
    public String Md5str;
    public String sonMd5="";

    public boolean isCacheSon = false;
    /**
     * 请求接口id
     */
    public String mMethod;

    public String mErrMethod;
    /**
     * 返回的具体类
     */
    public Object mBuild;
    /**
     * 接口调用错误
     */
    public int mError = 0;
    /**
     * 具体信息
     */
    public String msg = "";
    /**
     * 服务端返回接口名称
     */
    public String mServerMethod = "";
    /**
     * 错误类型
     */
    public int mErrorType = 0;
    /**
     * 上传的参数
     */
    public Map<String, String> mParams;

    public Map<String, Object> sonParam = new HashMap<String, Object>();

    public List<Son> sons = new ArrayList<Son>();

    /**
     * 接口类型
     */
    public int type = 0;

    public String sonId = "";

    public Son() {
    }

    public Son(int error,String msg) {
        this.mError = error;
        this.msg = msg;
    }


    public Son(int error, String msg, String method, int errortype, int type) {
        this.mError = error;
        this.msg = msg;
        this.mMethod = method;
        this.mErrMethod = mMethod;
        this.mServerMethod = method;
        this.mErrorType = errortype;
        this.type = type;
    }


    public Son(int error, String msg, Son son) {
        this.mError = error;
        this.msg = msg;
        this.mMethod = son.getMethod();
        this.mErrMethod = mMethod;
        this.mServerMethod = son.mServerMethod;
        this.mErrorType = son.mErrorType;
        this.type = son.getType();
        this.sonId = son.getSonId();
    }

    public Son(int error, String msg, UpdateOne updateone) {

        this.mError = error;
        this.msg = msg;
        this.mErrMethod = mMethod;

        this.mMethod = updateone.getId();
        this.mErrMethod = mMethod;
        this.mErrorType = updateone.getErrorType();
        this.type = updateone.getType();
        this.mParams = updateone.getMap();
        this.sonId = updateone.getSonId();
    }


    public String getParam(String key) {
        if (mParams != null) {
            return mParams.get(key);
        } else {
            return null;
        }
    }

    public boolean checkParam(String key, String value) {
        String v = getParam(key);
        if (v == null) {
            return false;
        } else {
            return v.equals(value);
        }
    }

    public boolean checkServerMethod(String method) {
        if (mServerMethod == null || mServerMethod.trim().length() == 0) {
            return false;
        } else if (mServerMethod.equals(method)) {
            return true;
        } else if (mServerMethod.indexOf("," + method + ",") >= 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkMethod(String method) {
        if (mMethod == null || mMethod.trim().length() == 0) {
            return false;
        } else if (mMethod.equals(method)) {
            return true;
        } else if (mMethod.indexOf("," + method + ",") >= 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkAll(String method) {
        return checkMethod(method) || checkServerMethod(method);
    }

    public String getMethod() {
        return mMethod;
    }

    public <T> T getBuild() {
        return (T) mBuild;
    }

    public int getError() {
        return mError;
    }

    public String getMsg() {
        return msg;
    }

    public String getServerMethod() {
        return mServerMethod;
    }

    public Map<String, String> getParams() {
        return mParams;
    }

    public int getType() {
        return type;
    }

    public String getSonId() {
        return sonId;
    }

    public Object getSonParam(String key) {
        return this.sonParam.get(key);
    }

//	public int getErrorType() {
//		return mErrorType;
//	}
//	public void setSonId(String sonId) {
//		this.sonId = sonId;
//	}
//
//	public void setMethod(String Method) {
//		this.mMethod = Method;
//	}
//
//	public void setBuild(Object Build) {
//		this.mBuild = Build;
//	}
//
//	public void setError(int Error) {
//		this.mError = Error;
//	}
//
//	public void setMsg(String msg) {
//		this.msg = msg;
//	}
//	public void setErrorType(int ErrorType) {
//		this.mErrorType = ErrorType;
//	}
//
//	public void setParams(Map<String, String> Params) {
//		this.mParams = Params;
//	}
//
//	public void setType(int type) {
//		this.type = type;
//	}
//
//	public void setSonParam(HashMap<String, Object> params) {
//		this.sonParam = params;
//	}
}
