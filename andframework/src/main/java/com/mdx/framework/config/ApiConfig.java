/*
 * 文件名: ApiConfig.java 版 权： Copyright XCDS Tech. Co. Ltd. All Rights Reserved. 描
 * 述: [该类的简要描述] 创建人: ryan 创建时间:2013-12-24
 *
 * 修改人： 修改时间: 修改内容：[修改内容]
 */
package com.mdx.framework.config;

import android.text.TextUtils;

import com.mdx.framework.commons.ParamsManager;
import com.mdx.framework.log.MLog;
import com.mdx.framework.server.api.ApiUrl;
import com.mdx.framework.server.api.OnApiInitListener;
import com.mdx.framework.utility.Util;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 *
 * @author ryan
 * @version [2013-12-24 下午12:36:47]
 */
public class ApiConfig extends BaseConfig {
    /**
     * api接口地址
     */
    private static String mUrl = "";
    private static String mPackage = "";
    private static String mEncode = "utf-8";
    private static String mUpEncode = "utf-8";
    private static String mDataType = "";
    private static String mApiName = "";

    /**
     * api接口列表
     */
    private static HashMap<String, ApiUrl> mApiMap = new HashMap<String, ApiUrl>();
    /**
     * api自动添加参数表
     */
    private static HashMap<String, OnApiInitListener> APIINITLISTENERS = new HashMap<String, OnApiInitListener>();

    /**
     * 获取自动添加的所有参数
     *
     * @param id
     * @return
     * @throws
     * @author ryan
     * @Title: getAutoApiInitParams
     * @Description: TODO
     */
    public synchronized static String[][] getAutoApiInitParams(String id, Object... objects) {
        ArrayList<String[]> list = new ArrayList<String[]>();
        if (APIINITLISTENERS == null || APIINITLISTENERS.size() == 0) {
            return new String[][]{};
        }
        if (APIINITLISTENERS.containsKey("ALL")) {
            String[][] strs = APIINITLISTENERS.get("ALL").onApiInitListener(objects);
            for (String[] s : strs) {
                list.add(s);
            }
        }
        if (APIINITLISTENERS.containsKey(id)) {
            String[][] strs = APIINITLISTENERS.get(id).onApiInitListener(objects);
            for (String[] s : strs) {
                list.add(s);
            }
        }
        String[][] retn = new String[list.size()][];
        list.toArray(retn);
        return retn;
    }

    /**
     * 设置所有接口调用时的参数
     *
     * @param listener
     * @throws
     * @author ryan
     * @Title: setAutoApiInitParams
     * @Description: TODO
     */
    public synchronized static void setAutoApiInitParams(OnApiInitListener listener) {
        APIINITLISTENERS.put("ALL", listener);
    }

    /**
     * 根据接口设置调用参数
     *
     * @param apiid
     * @param listener
     * @throws
     * @author ryan
     * @Title: setAutoApiInitParams
     * @Description: TODO
     */
    public synchronized static void setAutoApiInitParams(String apiid, OnApiInitListener listener) {
        APIINITLISTENERS.put(apiid, listener);
    }

    /**
     * 删除接口的调用参数
     *
     * @param apiid
     * @throws
     * @author ryan
     * @Title: removeAutoApiInitParams
     * @Description: TODO
     */
    public synchronized static void removeAutoApiInitParams(String apiid) {
        APIINITLISTENERS.remove(apiid);
    }

    /**
     * 删除全局参数
     *
     * @throws
     * @author ryan
     * @Title: removeAutoApiInitParams
     * @Description: TODO
     */
    public synchronized static void removeAutoApiInitParams() {
        APIINITLISTENERS.remove("ALL");
    }

    /**
     * 清空接口参数表
     *
     * @param apiid
     * @throws
     * @author ryan
     * @Title: clearAutoApiInitParams
     * @Description: TODO
     */
    public synchronized static void clearAutoApiInitParams(String apiid) {
        APIINITLISTENERS.clear();
    }

    public synchronized static ApiUrl getApiUrl(String id) {
        if (mApiMap.containsKey(id)) {
            ApiUrl url = new ApiUrl();
            ApiUrl nurl = mApiMap.get(id);
            String strurl = nurl.url;
            if (Util.isPatten(strurl, "\\[.?\\]")) {
                strurl = ParamsManager.getString(strurl);
            }
            if (Util.isFullUrl(strurl)) {
                url.url = strurl;
            } else {
                url.url = getUrl(nurl.configName,nurl.configururl) + strurl;
            }
            url.className = nurl.className;
            url.type = nurl.type;
            url.errorType = nurl.errorType;
            url.dataType = nurl.dataType;
            url.cacheTime = nurl.cacheTime;
            url.saveError = nurl.saveError;
            url.encode = nurl.encode;
            url.upEncode = nurl.upEncode;
            url.configName = nurl.configName;
            url.methodUrl = nurl.methodUrl;
            return url;
        } else {
            MLog.E("system.err", "Api error,Api id '" + id + "' not exit");
            throw new IllegalAccessError("Api error,Api id '" + id + "' not exit");
        }
    }

    public synchronized static boolean hasApi(String id) {
        return (mApiMap.containsKey(id));
    }

    public synchronized static String getPackage() {
        return mPackage;
    }

    public synchronized static void setPackage(String Package) {
        ApiConfig.mPackage = Package;
    }

    public synchronized static void setApiMap(HashMap<String, ApiUrl> ApiMap) {
        mApiMap = ApiMap;
    }

    public synchronized static void putApiUrl(String key, ApiUrl apiurl) {
        if (!Util.isFullUrl(apiurl.url)) {
            apiurl.methodUrl = apiurl.url;
            apiurl.configururl = mUrl;
        }
        apiurl.configName = mApiName;
        if (apiurl.className.startsWith(".")) {
            apiurl.className = getPackage() + apiurl.className;
        }
        if (TextUtils.isEmpty(apiurl.dataType)) {
            apiurl.dataType = mDataType;
        }
        if (TextUtils.isEmpty(apiurl.encode)) {
            apiurl.encode = mEncode;
        }
        if (TextUtils.isEmpty(apiurl.upEncode)) {
            apiurl.upEncode = mUpEncode;
        }
        mApiMap.put(key, apiurl);
    }

    public synchronized static String getUrl(String apiname, String configururl) {
        if (configururl == null || configururl.length() == 0) {
            return "";
        }
        String url = configururl;
        if (url.startsWith("/")) {
            url = getUri(apiname) + url;
        }
        return url;
    }

    public synchronized static void setUrl(String url) {
        mUrl = url;
    }

    public static String getEncode() {
        return mEncode;
    }

    public static void setEncode(String Encode) {
        ApiConfig.mEncode = Encode;
    }

    public static String getUpEncode() {
        return mUpEncode;
    }

    public static void setUpEncode(String UpEncode) {
        ApiConfig.mUpEncode = UpEncode;
    }

    public static String getDataType() {
        return mDataType;
    }

    public static void setDataType(String DataType) {
        ApiConfig.mDataType = DataType;
    }

    public static String getApiName() {
        return mApiName;
    }

    public static void setApiName(String apiName) {
        ApiConfig.mApiName = apiName;
    }
}
