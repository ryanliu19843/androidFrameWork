/*
 * 文件名: InitConfig.java 版 权： Copyright XCDS Tech. Co. Ltd. All Rights Reserved.
 * 描 述: [该类的简要描述] 创建人: ryan 创建时间:2013-12-25
 *
 * 修改人： 修改时间: 修改内容：[修改内容]
 */
package com.mdx.framework.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.mdx.framework.Frame;
import com.mdx.framework.R;
import com.mdx.framework.commons.ParamsManager;
import com.mdx.framework.log.MLog;
import com.mdx.framework.server.api.ApiUrl;
import com.mdx.framework.server.api.ErrorMsg;
import com.mdx.framework.server.image.UrlImageLoad;
import com.mdx.framework.utility.Util;

import org.xmlpull.v1.XmlPullParser;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 初始化<BR>
 *
 * @author ryan
 * @version [2013-12-25 下午4:24:10]
 */
public class InitConfig {

    public static void initConfig() {
        try {
            initBase();
        } catch (Exception e) {
            MLog.E(MLog.SYS_RUN, e);
        }

        try {
            initLog();
        } catch (Exception e) {
            MLog.E(MLog.SYS_RUN, e);
        }

        try {
            if (!ApiConfig.hasApi("APP_UPDATE_SELF")) {
                String ltime = Frame.CONTEXT.getString(R.string.update_once_time);
                ApiUrl iu = new ApiUrl();
                iu.url = BaseConfig.getUpdateUri();
                iu.cacheTime = Long.valueOf(ltime);
                iu.errorType = 11;
                iu.dataType = "DEFAULT";
                iu.type = 4;
                iu.configName = "UpdateUri";
                iu.configururl = "";
                iu.methodUrl = "APP_UPDATE_SELF";
                iu.className = "com.mdx.framework.server.api.base.Msg_Update";
                ApiConfig.putApiUrl("APP_UPDATE_SELF", iu);
            }
            if (!ApiConfig.hasApi("MUpdateError")) {
                ApiUrl iu = new ApiUrl();
                iu.url = ParamsManager.get("ErrorUpdateUrl");
                iu.cacheTime = 0L;
                iu.errorType = 11;
                iu.dataType = "DEFAULT";
                iu.type = 4;
                iu.configName = "ErrorUpdateUrl";
                iu.configururl = "";
                iu.methodUrl = "MUpdateError";
                iu.className = "com.mdx.framework.server.api.base.Msg_Key";
                ApiConfig.putApiUrl("MUpdateError", iu);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        try {
            initImage();
        } catch (Exception e) {
            MLog.E(MLog.SYS_RUN, e);
        }
        try {
            initTools();
        } catch (Exception e) {
            MLog.E(MLog.SYS_RUN, e);
        }
        try {
            initApi();
        } catch (Exception e) {
            MLog.E(MLog.SYS_RUN, e);
        }
        try {
            initError();
        } catch (Exception e) {
            MLog.E(MLog.SYS_RUN, e);
        }
    }

    public static void initError() throws Exception {
        String[] errnameMap = Frame.CONTEXT.getResources().getStringArray(R.array.errorname);
        HashMap<String, String> errmap = new HashMap<String, String>();
        for (String name : errnameMap) {
            int dv = name.indexOf(":");
            if (dv > 0) {
                errmap.put("[" + name.substring(0, dv) + "]", name.substring(dv + 1));
            }
        }
        XmlPullParser xpp = Frame.CONTEXT.getResources().getXml(R.xml.error_config);// getRxml("xml", "error_config");
        if (xpp != null) {
            int eventType = xpp.getEventType();
            String reading = "", name = "", value = "", id = "", method = "", classname = "", type = "", title = "";
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_DOCUMENT) {
                } else if (eventType == XmlPullParser.END_DOCUMENT) {
                } else if (eventType == XmlPullParser.START_TAG) {
                    reading = xpp.getName();
                    if (xpp.getName().toUpperCase(Locale.ENGLISH).equals("MSG")) {
                        name = id = method = classname = title = type = value = "";
                        for (int i = 0; i < xpp.getAttributeCount(); i++) {
                            if (cpc(xpp.getAttributeName(i), "ID")) {
                                id = xpp.getAttributeValue(i);
                            } else if (cpc(xpp.getAttributeName(i), "NAME")) {
                                name = xpp.getAttributeValue(i);
                            } else if (cpc(xpp.getAttributeName(i), "TYPE")) {
                                type = xpp.getAttributeValue(i);
                            } else if (cpc(xpp.getAttributeName(i), "METHOD")) {
                                method = xpp.getAttributeValue(i);
                            } else if (cpc(xpp.getAttributeName(i), "CLASSNAME")) {
                                classname = xpp.getAttributeValue(i);
                            } else if (cpc(xpp.getAttributeName(i), "TITLE")) {
                                title = xpp.getAttributeValue(i);
                            }
                        }
                    } else if (cpc(xpp.getName(), "ERRORS")) {
                        for (int i = 0; i < xpp.getAttributeCount(); i++) {
                            if (cpc(xpp.getAttributeName(i), "DEFAULTERROR")) {
                                ErrorConfig.setErrorClass(xpp.getAttributeValue(i));
                            } else if (cpc(xpp.getAttributeName(i), "JUDGETYPE")) {
                                ErrorConfig.setJudgeType(Integer.parseInt(xpp.getAttributeValue(i)));
                            } else if (cpc(xpp.getAttributeName(i), "ERRORPACKAGE")) {
                                ErrorConfig.setPackage(xpp.getAttributeValue(i));
                            }
                        }
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if (cpc(xpp.getName(), "MSG")) {
                        ErrorMsg em = new ErrorMsg();
                        if (type.length() > 0) {
                            em.type = Integer.parseInt(type);
                        } else {
                            em.type = 0;
                        }
                        if (id.length() > 0) {
                            em.id = Integer.parseInt(id);
                        } else {
                            em.id = 0;
                        }
                        em.name = name;
                        em.title = title;
                        em.className = classname;
                        if (errmap.containsKey(value)) {
                            value = errmap.get(value);
                        }
                        em.value = value;
                        em.method = method;
                        ErrorConfig.put(em);
                    }
                    reading = null;
                } else if (eventType == XmlPullParser.TEXT) {
                    if (cpc(reading, "MSG")) {
                        value = xpp.getText();
                    }
                }
                eventType = xpp.next();
            }
        }
    }

    public static void initApi() throws Exception {
        XmlPullParser xpp = Frame.CONTEXT.getResources().getXml(R.xml.api_config);// getRxml("xml", "api_config");
        if (xpp != null) {
            int eventType = xpp.getEventType();
            String reading = "", name = "", value = "", type = "", cachetime = "", classname = "", errorType = "", saveError = "", datatype = "", encode = "", upencode = "";
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_DOCUMENT) {
                } else if (eventType == XmlPullParser.END_DOCUMENT) {
                } else if (eventType == XmlPullParser.START_TAG) {
                    reading = xpp.getName();
                    if (xpp.getName().toUpperCase(Locale.ENGLISH).equals("URL")) {
                        name = saveError = errorType = classname = cachetime = value = cachetime = "";
                        for (int i = 0; i < xpp.getAttributeCount(); i++) {
                            if (cpc(xpp.getAttributeName(i), "NAME")) {
                                name = xpp.getAttributeValue(i);
                            } else if (cpc(xpp.getAttributeName(i), "TYPE")) {
                                type = xpp.getAttributeValue(i);
                            } else if (cpc(xpp.getAttributeName(i), "CACHETIME")) {
                                cachetime = xpp.getAttributeValue(i);
                            } else if (cpc(xpp.getAttributeName(i), "CLASSNAME")) {
                                classname = xpp.getAttributeValue(i);
                            } else if (cpc(xpp.getAttributeName(i), "ERRORTYPE")) {
                                errorType = xpp.getAttributeValue(i);
                            } else if (cpc(xpp.getAttributeName(i), "SAVEERROR")) {
                                saveError = xpp.getAttributeValue(i);
                            } else if (cpc(xpp.getAttributeName(i), "DATATYPE")) {
                                datatype = xpp.getAttributeValue(i);
                            } else if (cpc(xpp.getAttributeName(i), "UPENCODE")) {
                                upencode = xpp.getAttributeValue(i);
                            } else if (cpc(xpp.getAttributeName(i), "ENCODE")) {
                                encode = xpp.getAttributeValue(i);
                            }
                        }
                    } else if (cpc(xpp.getName(), "API")) {
                        for (int i = 0; i < xpp.getAttributeCount(); i++) {
                            if (cpc(xpp.getAttributeName(i), "APIURL")) {
                                ApiConfig.setUrl(xpp.getAttributeValue(i));
                            } else if (cpc(xpp.getAttributeName(i), "PACKAGE")) {
                                ApiConfig.setPackage(xpp.getAttributeValue(i));
                            } else if (cpc(xpp.getAttributeName(i), "UPENCODE")) {
                                ApiConfig.setUpEncode(xpp.getAttributeValue(i));
                            } else if (cpc(xpp.getAttributeName(i), "ENCODE")) {
                                ApiConfig.setEncode(xpp.getAttributeValue(i));
                            } else if (cpc(xpp.getAttributeName(i), "DATATYPE")) {
                                ApiConfig.setDataType(xpp.getAttributeValue(i));
                            } else if (cpc(xpp.getAttributeName(i), "NAME")) {
                                ApiConfig.setApiName(xpp.getAttributeValue(i));
                            }
                        }
                    } else if (cpc(xpp.getName(), "APIS")) {
                        for (int i = 0; i < xpp.getAttributeCount(); i++) {
                            if (cpc(xpp.getAttributeName(i), "APIURL")) {
                                ApiConfig.setUrl(xpp.getAttributeValue(i));
                            } else if (cpc(xpp.getAttributeName(i), "PACKAGE")) {
                                ApiConfig.setPackage(xpp.getAttributeValue(i));
                            } else if (cpc(xpp.getAttributeName(i), "UPENCODE")) {
                                ApiConfig.setUpEncode(xpp.getAttributeValue(i));
                            } else if (cpc(xpp.getAttributeName(i), "ENCODE")) {
                                ApiConfig.setEncode(xpp.getAttributeValue(i));
                            } else if (cpc(xpp.getAttributeName(i), "DATATYPE")) {
                                ApiConfig.setDataType(xpp.getAttributeValue(i));
                            }
                        }
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if (cpc(xpp.getName(), "URL")) {
                        ApiUrl apiurl = new ApiUrl();
                        if (type.length() > 0) {
                            apiurl.type = Integer.parseInt(type);
                        } else {
                            apiurl.type = 0;
                        }
                        if (errorType.length() > 0) {
                            apiurl.errorType = Integer.parseInt(errorType);
                        } else {
                            apiurl.errorType = 0;
                        }
                        if (saveError.length() > 0) {
                            apiurl.setSaveError(saveError);
                        }
                        if (cachetime.length() > 0) {
                            apiurl.cacheTime = Long.parseLong(cachetime);
                        } else {
                            apiurl.cacheTime = 86400000L;
                        }
                        if (datatype.length() > 0) {
                            apiurl.dataType = datatype;
                        } else {
                            apiurl.dataType = ApiConfig.getDataType();
                        }
                        if (TextUtils.isEmpty(encode)) {
                            apiurl.encode = encode;
                        } else {
                            apiurl.encode = ApiConfig.getEncode();
                        }
                        if (TextUtils.isEmpty(upencode)) {
                            apiurl.upEncode = upencode;
                        } else {
                            apiurl.upEncode = ApiConfig.getUpEncode();
                        }
                        apiurl.className = classname;
                        apiurl.url = value;
                        ApiConfig.putApiUrl(name, apiurl);
                    }
                    reading = null;
                } else if (eventType == XmlPullParser.TEXT) {
                    if (cpc(reading, "URL")) {
                        value = xpp.getText().trim();
                    }
                }
                eventType = xpp.next();
            }
        }
    }

    public static void initBase() throws Exception {
        SharedPreferences sup = Frame.CONTEXT.getSharedPreferences("default_param", Context.MODE_PRIVATE);
        XmlPullParser xpp = Frame.CONTEXT.getResources().getXml(R.xml.base_config);// getRxml("xml", "base_config");
        if (xpp != null) {
            int eventType = xpp.getEventType();
            String reading = "", name = "", value = "", uriName = "", uriValue = "";
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_DOCUMENT) {
                } else if (eventType == XmlPullParser.END_DOCUMENT) {
                } else if (eventType == XmlPullParser.START_TAG) {
                    reading = xpp.getName();
                    if (xpp.getName().toUpperCase(Locale.ENGLISH).equals("PARAM")) {
                        name = value = "";
                        for (int i = 0; i < xpp.getAttributeCount(); i++) {
                            if (cpc(xpp.getAttributeName(i), "NAME")) {
                                name = xpp.getAttributeValue(i);
                            }
                        }
                    } else if (cpc(xpp.getName(), "BASE")) {
                        for (int i = 0; i < xpp.getAttributeCount(); i++) {
                            if (cpc(xpp.getAttributeName(i), "BASEURI")) {
                                String baseuri = sup.getString("serverurl", "");
                                if (N(baseuri)) {
                                    BaseConfig.setUri(xpp.getAttributeValue(i));
                                } else {
                                    BaseConfig.setUri(baseuri);
                                }
                            } else if (cpc(xpp.getAttributeName(i), "UPDATEURI")) {
                                BaseConfig.setUpdateUri(xpp.getAttributeValue(i));
                            } else if (cpc(xpp.getAttributeName(i), "TEMPPATH")) {
                                BaseConfig.setTempPath(xpp.getAttributeValue(i));
                            } else if (cpc(xpp.getAttributeName(i), "APPID")) {
                                BaseConfig.setAppid(xpp.getAttributeValue(i));
                            } else if (cpc(xpp.getAttributeName(i), "USESDCARD")) {
                                BaseConfig.setUseSdcard(Boolean.valueOf(xpp.getAttributeValue(i)));
                            }
                        }
                    } else if (cpc(xpp.getName(), "URL")) {
                        uriName = uriValue = "";
                        for (int i = 0; i < xpp.getAttributeCount(); i++) {
                            if (cpc(xpp.getAttributeName(i), "NAME")) {
                                uriName = xpp.getAttributeValue(i);
                            }
                        }
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if (cpc(reading, "PARAM")) {
                        ParamsManager.put(name.toLowerCase(Locale.ENGLISH), value);
                    } else if (cpc(reading, "URL")) {
                        String baseuri = sup.getString(uriName, "");
                        if (N(baseuri)) {
                            BaseConfig.addUri(uriName, uriValue);
                        } else {
                            BaseConfig.addUri(uriName, baseuri);
                        }
                    }
                    reading = null;
                } else if (eventType == XmlPullParser.TEXT) {
                    if (cpc(reading, "PARAM")) {
                        value = xpp.getText();
                    } else if (cpc(reading, "URL")) {
                        uriValue = xpp.getText();
                    }
                }
                eventType = xpp.next();
            }
        }
        SharedPreferences sp = Frame.CONTEXT.getSharedPreferences("default_param", Context.MODE_PRIVATE);
        Map<String, ?> map = sp.getAll();
        for (String key : map.keySet()) {
            ParamsManager.put(key.toLowerCase(Locale.ENGLISH), map.get(key).toString());
        }
    }

    public static void initLog() throws Exception {
        XmlPullParser xpp = Frame.CONTEXT.getResources().getXml(R.xml.log_config); // getRxml("xml", "log_config");
        if (xpp != null) {
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_DOCUMENT) {
                } else if (eventType == XmlPullParser.END_DOCUMENT) {
                } else if (eventType == XmlPullParser.START_TAG) {
                    if (cpc(xpp.getName(), "LOG")) {
                        for (int i = 0; i < xpp.getAttributeCount(); i++) {
                            if (cpc(xpp.getAttributeName(i), "SHOWLOG")) {
                                LogConfig.setShowLog(Boolean.valueOf(xpp.getAttributeValue(i)));
                            }
                            if (cpc(xpp.getAttributeName(i), "LOGLEV")) {
                                LogConfig.setLoglev(Integer.valueOf(xpp.getAttributeValue(i)));
                            }
                        }
                    }
                }
                eventType = xpp.next();
            }
        }
    }

    public static void initImage() throws Exception {
        XmlPullParser xpp = Frame.CONTEXT.getResources().getXml(R.xml.image_config);// getRxml("xml", "image_config");
        if (xpp != null) {
            int eventType = xpp.getEventType();
            String value = "", name = "", reading = "";
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_DOCUMENT) {
                } else if (eventType == XmlPullParser.END_DOCUMENT) {
                } else if (eventType == XmlPullParser.START_TAG) {
                    reading = xpp.getName();
                    if (cpc(xpp.getName(), "IMAGE")) {
                        for (int i = 0; i < xpp.getAttributeCount(); i++) {
                            if (cpc(xpp.getAttributeName(i), "IMAGEMINLOAD")) {
                                String minload = xpp.getAttributeValue(i);
                                if (Util.isPatten(minload, "^[0-9]*$")) {
                                    ImageConfig.setMinLoadImage(Integer.valueOf(minload));
                                }
                            } else if (cpc(xpp.getAttributeName(i), "IMAGEPARAM")) {
                                ImageConfig.setImageParam(xpp.getAttributeValue(i));
                            } else if (cpc(xpp.getAttributeName(i), "IMAGEURL")) {
                                ImageConfig.setImageUrl(xpp.getAttributeValue(i));
                            } else if (cpc(xpp.getAttributeName(i), "IMAGELOADTYPE")) {
                                String type = xpp.getAttributeValue(i);
                                if (Util.isPatten(type, "^[0-9]*$")) {
                                    ImageConfig.setImageLoadType(Integer.valueOf(type));
                                } else {
                                    int ti = 0;
                                    String[] typs = type.split("\\\\\\|");
                                    for (String t : typs) {
                                        if (cpc(t, "GET")) {
                                            ti = ti | UrlImageLoad.URLIMAGELOAD_TYPE_GET;
                                        } else if (cpc(t, "NOPARAM")) {
                                            ti = ti | UrlImageLoad.URLIMAGELOAD_TYPE_NOPARAMS;
                                        }
                                    }
                                    ImageConfig.setImageLoadType(ti);
                                }
                            }
                        }
                    } else if (xpp.getName().toUpperCase(Locale.ENGLISH).equals("URL")) {
                        name = value = "";
                        for (int i = 0; i < xpp.getAttributeCount(); i++) {
                            if (cpc(xpp.getAttributeName(i), "NAME")) {
                                name = xpp.getAttributeValue(i);
                            }
                        }
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if (cpc(reading, "URL")) {
                        ImageConfig.putUrl(name, value);
                    }
                    reading = null;
                } else if (eventType == XmlPullParser.TEXT) {
                    if (cpc(reading, "URL")) {
                        value = xpp.getText();
                    }
                }
                eventType = xpp.next();
            }
        }
    }

    public static void initTools() throws Exception {
        XmlPullParser xpp = Frame.CONTEXT.getResources().getXml(R.xml.tools_config); // getRxml("xml", "tools_config");
        if (xpp != null) {
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_DOCUMENT) {
                } else if (eventType == XmlPullParser.END_DOCUMENT) {
                } else if (eventType == XmlPullParser.START_TAG) {
                    if (cpc(xpp.getName(), "TOOLS")) {
                        for (int i = 0; i < xpp.getAttributeCount(); i++) {
                            if (cpc(xpp.getAttributeName(i), "STATISTICSTAGS")) {
                                ToolsConfig.setStatisticsTags(xpp.getAttributeValue(i));
                            } else if (cpc(xpp.getAttributeName(i), "STATISTICSABLE")) {
                                ToolsConfig.setStatistics(Boolean.valueOf(xpp.getAttributeValue(i)));
                            } else if (cpc(xpp.getAttributeName(i), "TOOLSSHOW")) {
                                ToolsConfig.setLogToolsShow(Boolean.valueOf(xpp.getAttributeValue(i)));
                            } else if (cpc(xpp.getAttributeName(i), "STATISTICSSHOWTYPE")) {
                                String type = xpp.getAttributeValue(i);
                                ToolsConfig.setStatisticsShowType(Integer.valueOf(type));
                            }
                        }
                    }
                }
                eventType = xpp.next();
            }
        }
    }

    private static boolean N(String check) {
        return TextUtils.isEmpty(check);
    }

    private static boolean cpc(String check, String str) {
        if (check != null) {
            return check.toUpperCase(Locale.ENGLISH).equals(str);
        }
        return false;
    }
}
