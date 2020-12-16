/*
 * 文件名: ApiConfig.java 版 权： Copyright XCDS Tech. Co. Ltd. All Rights Reserved. 描
 * 述: [该类的简要描述] 创建人: ryan 创建时间:2013-12-24
 *
 * 修改人： 修改时间: 修改内容：[修改内容]
 */
package com.mdx.framework.config;

import android.content.Context;

import com.mdx.framework.log.MLog;
import com.mdx.framework.prompt.ErrorDialog;
import com.mdx.framework.prompt.ErrorPrompt;
import com.mdx.framework.server.api.ErrorMsg;
import com.mdx.framework.server.api.Son;
import com.mdx.framework.utility.Verify;

import java.lang.reflect.Constructor;
import java.util.HashMap;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 *
 * @author ryan
 * @version [2013-12-24 下午12:36:47]
 */
public class ErrorConfig extends BaseConfig {
    /**
     * 默认错误显示类
     */
    private static String mErrorClass = "";

    /**
     * 匹配方式 0只匹配id 1只匹配name 2匹配id或name 3匹配id和name
     */
    private static int mJudgeType = 0;

    /**
     * 错误包
     */
    private static String mPackage = "";

    /**
     * 错误信息
     */
    private static HashMap<String, HashMap<Object, HashMap<Object, ErrorMsg>>> mErrorMsg = new HashMap<String, HashMap<Object, HashMap<Object, ErrorMsg>>>();

    public synchronized static ErrorMsg getErrorMsg(Son son) {
        String method = son.mErrMethod;
        if (Verify.isNull(method)) {
            method = "all";
        }
        HashMap<Object, HashMap<Object, ErrorMsg>> map = null;
        if (mErrorMsg.containsKey(method)) {
            map = mErrorMsg.get(method);
        } else {
            map = mErrorMsg.get("all");
        }
        ErrorMsg em = null;
        if (map != null) {
            em = getErrorMsg(son.getError(), son.getMsg(), method, mJudgeType);
        }
        if (em == null) {
            em = new ErrorMsg();
            em.id = son.getError();
            em.name = son.getMethod();
            em.title = son.getMsg();
            em.method = son.getMethod();
            em.value = son.getMsg();
        }
        if (son != null) {
            em.method = son.getServerMethod();
            em.object = son;
            if (em.value != null && em.value.indexOf("%m") >= 0) {
                em.value = em.value.replace("%m", son.msg);
            }
            son.msg = em.value;
        }
        return em;
    }

    public synchronized static ErrorMsg getErrorMsg(int id, String name, String method, int type) {
        ErrorMsg retn = null;
        HashMap<Object, HashMap<Object, ErrorMsg>> map = null;
        HashMap<Object, HashMap<Object, ErrorMsg>> allMap = null;
        if (Verify.isNull(method)) {
            method = "all";
        }
        if (!"all".equals(method) && mErrorMsg.containsKey(method)) {
            map = mErrorMsg.get(method);
        }
        allMap = mErrorMsg.get("all");
        if (map == null && allMap == null) {
            return null;
        }
        if (type == 2) {
            retn = getPErrorMsg(map, allMap, id, name, 0);
            if (retn == null) {
                retn = getPErrorMsg(map, allMap, id, name, 1);
            }
        } else {
            retn = getPErrorMsg(map, allMap, id, name, type);
        }
        return retn == null ? null : retn.clone();
    }

    public static ErrorPrompt getMsgPrompt(Context context) {
        if (mErrorClass != null && mErrorClass.length() > 0) {
            try {
                Class<?> clazz = Class.forName(mErrorClass);
                Constructor<?> clst = clazz.getConstructor(Context.class);
                Object obj = clst.newInstance(context);
                if (obj instanceof ErrorPrompt) {
                    return (ErrorPrompt) obj;
                } else {
                    throw new Exception("Error Class type! not MsgDialog!");
                }
            } catch (Exception e) {
                MLog.D("", e);
                return new ErrorDialog(context);
            }
        } else {
            return new ErrorDialog(context);
        }
    }

    private synchronized static ErrorMsg getPErrorMsg(HashMap<Object, HashMap<Object, ErrorMsg>> map,
                                                      HashMap<Object, HashMap<Object, ErrorMsg>> allmap, int id, String name, int type) {
        String key;
        Object value;
        switch (type) {
            case 1:
                key = "namemap";
                value = name;
                break;
            case 3:
                key = "idnmap";
                value = getKey(id, name);
                break;
            default:
                key = "idmap";
                value = id;
                break;
        }
        if (map != null && map.get(key).containsKey(value)) {
            return map.get(key).get(value);
        } else if (allmap != null && allmap.get(key).containsKey(value)) {
            return allmap.get(key).get(value);
        }
        return null;
    }

    public synchronized static void put(ErrorMsg errmsg) {
        if (errmsg == null) {
            return;
        }
        String method = errmsg.method;
        if (Verify.isNull(method)) {
            method = "all";
        }

        HashMap<Object, HashMap<Object, ErrorMsg>> map = null;
        HashMap<Object, ErrorMsg> idmap = null;
        HashMap<Object, ErrorMsg> namemap = null;
        HashMap<Object, ErrorMsg> idnmap = null;

        if (mErrorMsg.containsKey(method)) {
            map = mErrorMsg.get(method);
            idmap = map.get("idmap");
            namemap = map.get("namemap");
            idnmap = map.get("idnmap");
        } else {
            map = new HashMap<Object, HashMap<Object, ErrorMsg>>();
            idmap = new HashMap<Object, ErrorMsg>();
            map.put("idmap", idmap);
            namemap = new HashMap<Object, ErrorMsg>();
            map.put("namemap", namemap);
            idnmap = new HashMap<Object, ErrorMsg>();
            map.put("idnmap", idnmap);
            mErrorMsg.put(method, map);
        }
        idnmap.put(getKey(errmsg.id, errmsg.name), errmsg);
        idmap.put(errmsg.id, errmsg);
        if (errmsg.name != null) {
            namemap.put(errmsg.name, errmsg);
        }
    }

    private synchronized static String getKey(int id, String name) {
        return id + "**" + name;
    }

    public synchronized static String getErrorClass() {
        return mErrorClass;
    }

    public synchronized static void setErrorClass(String mErrorClass) {
        ErrorConfig.mErrorClass = mErrorClass;
    }

    public synchronized static int getJudgeType() {
        return mJudgeType;
    }

    public synchronized static void setJudgeType(int judgeType) {
        mJudgeType = judgeType;
    }

    public synchronized static String getPackage() {
        return mPackage;
    }

    public synchronized static void setPackage(String mPackage) {
        ErrorConfig.mPackage = mPackage;
    }
}
