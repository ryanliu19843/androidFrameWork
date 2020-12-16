/*
 * 文件名: ParamsText.java 版 权： Copyright XCDS Tech. Co. Ltd. All Rights Reserved.
 * 描 述: [该类的简要描述] 创建人: ryan 创建时间:2013-12-2
 *
 * 修改人： 修改时间: 修改内容：[修改内容]
 */
package com.mdx.framework.commons;

import android.text.TextUtils;

import com.mdx.framework.autofit.AutoFitUtil;
import com.mdx.framework.config.BaseConfig;
import com.mdx.framework.config.LogConfig;
import com.mdx.framework.log.MLog;
import com.mdx.framework.server.api.base.Msg_TempFile;
import com.mdx.framework.server.api.base.Msg_TempFiles;
import com.mdx.framework.utility.Helper;
import com.mdx.framework.utility.Util;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 *
 * @author ryan
 * @version [2013-12-2 下午1:28:52]
 */
public class ParamsManager {
    public static String PARAM_VIEW_WIDTH = "view_width";

    public static String PARAM_VIEW_HEIGHT = "view_height";

    public static String PARAM_DATA_OBJ = "data_obj";

    public static String PARAM_DATA_USERID = "data_userid";

    public static String PARAM_DATA_APPID = "data_appid";

    public static String PARAM_SERVER_URI = "uri";

    public static String PARAM_SERVER_UPDATE = "update";

    public static String PARAM_APP_PACKAGE = "app_package";

    public static String PARAM_APP_PACKAGEMD5 = "app_packagemd5";

    public static String PARAM_APP_VERSION = "app_version";
    public static String PARAM_WEBSERVICE_NAMESPACE = "WebServiceNamespace";
    public static String PARAM_WEBSERVICE_URL = "WebServiceUrl";

    public static Msg_TempFiles.Builder PARAM_SAVE = new Msg_TempFiles.Builder();

    public static boolean isneedsave = false;

    public static HashMap<String, Object> PARAM_HASHMAP = new HashMap();

    public static void put(String key, Object value) {
        key = key.toLowerCase(Locale.ENGLISH);
        if (key.startsWith("_serveruri")) {
            BaseConfig.setUri(value.toString());
            save(key.substring(1), value);
        } else if (key.startsWith("_")) {
            save(key.substring(1), value);
        } else if (key.startsWith("@")) {
            put(key.substring(1), value);
        } else {
            PARAM_HASHMAP.put(key, value);
        }
    }

    public static void clear() {
        PARAM_HASHMAP.clear();
    }

    public static <T> T get(String key) {
        Object obj = getValue(key);
        if (obj instanceof String) {
            return (T) obj;
        }
        return null;
    }

    public static <T> T get(String key, Object def) {
        return getValue(key, def);
    }

    public static <T> T getValue(String key) {
        if ("_serveruri".equals(key)) {
            key = PARAM_SERVER_URI;
        }
        if (key.startsWith("_") || key.startsWith("@")) {
            key = key.substring(1);
        }

        return (T) PARAM_HASHMAP.get(key.toLowerCase(Locale.ENGLISH));
    }

    public static <T> T getValue(String key, Object def) {
        Object retn = getValue(key);
        return (T) (retn != null ? retn : def);
    }

    public static long getLongValue(String key) {
        if (PARAM_HASHMAP.get(key.toLowerCase(Locale.ENGLISH)) == null) {
            return 0;
        }
        try {
            return Long.valueOf(get(key.toLowerCase(Locale.ENGLISH)));
        } catch (Exception e) {
            return 0;
        }
    }

    public static long getLongValue(String key, long def) {
        if (PARAM_HASHMAP.get(key.toLowerCase(Locale.ENGLISH)) == null) {
            return def;
        }
        try {
            return Long.valueOf(get(key.toLowerCase(Locale.ENGLISH)));
        } catch (Exception e) {
            return def;
        }
    }

    public static int getIntValue(String key) {
        if (PARAM_HASHMAP.get(key.toLowerCase(Locale.ENGLISH)) == null) {
            return 0;
        }
        try {
            return Integer.valueOf(get(key.toLowerCase(Locale.ENGLISH)));
        } catch (Exception e) {
            return 0;
        }
    }

    public static int getIntValue(String key, int def) {
        if (PARAM_HASHMAP.get(key.toLowerCase(Locale.ENGLISH)) == null) {
            return def;
        }
        try {
            return Integer.valueOf(get(key.toLowerCase(Locale.ENGLISH)));
        } catch (Exception e) {
            return def;
        }
    }

    public static String readUrlParam(String str, String key) {
        String p = get(key);
        if (Util.isFileUrl(p)) {
            return p;
        }
        if (str.trim().startsWith("[")) {
            Pattern pattern = Pattern.compile("\\[([A-Za-z0-9=\\-_]*?)\\]");
            Matcher matcher = pattern.matcher(str);
            String name = "";
            while (matcher.find()) {
                if (matcher.groupCount() > 0) {
                    name = matcher.group(1);
                    break;
                }
            }
            return BaseConfig.getUri(name) + p + str.replaceAll("\\[([A-Za-z0-9=\\-_]*?)\\]", "");
        } else {
            return BaseConfig.getUri() + p + str;
        }
    }

    public static String getString(String pstr) {
        return getString(pstr, null);
    }

    public static String getString(String pstr, HashMap<String, String> parammap) {
        ParamsText pt = new ParamsText();
        if (parammap != null) {
            for (String key : parammap.keySet()) {
                pt.putParam(key, parammap.get(key));
            }
        }
        for (String key : PARAM_HASHMAP.keySet()) {
            String str = get(key);
            if (str != null) {
                pt.putParam(key, str);
            }
        }
        pt.setText(pstr);
        return pt.toString();
    }


    public synchronized static boolean save(String key, Object object) {
        if (object == null) {
            return false;
        }
        long savetype = 0L;
        String value = object.getClass().getName();
        Msg_TempFile msgtemp = null;
        if (object instanceof String) {
            savetype = 1;
            value = (String) object;
        } else if (object instanceof Boolean) {
            savetype = 2;
            value = String.valueOf(object);
        } else if (object instanceof Double) {
            savetype = 3;
            value = String.valueOf(object);
        } else {
            Helper.saveBuilder("LPM_" + key, object);
        }
        if (PARAM_SAVE.files == null) {
            PARAM_SAVE.files = new ArrayList<>();
        }
        for (Msg_TempFile file : PARAM_SAVE.files) {
            if (file.filename.equals(key)) {
                PARAM_SAVE.files.remove(file);
                break;
            }
        }

        if (msgtemp == null) {
            msgtemp = new Msg_TempFile(key, savetype, value, 0L, 0L);
        }
        PARAM_SAVE.files.add(msgtemp);
        isneedsave = true;
        ParamsManager.put(key, object);
        return true;
    }

    public synchronized static void savebuild() {
        if (isneedsave) {
            isneedsave = false;
            if (PARAM_SAVE == null || PARAM_SAVE.files.size() == 0) {
                init();
            }
            if (LogConfig.getLoglev() >= 5) {
                MLog.D("L_P_MLIST", "L_P_MLIST write " + PARAM_SAVE.files.size() + "   ");
            }
            Helper.saveBuilder("L_P_MLIST", PARAM_SAVE.build());
            if (LogConfig.getLoglev() >= 5) {
                Msg_TempFiles ps = new Msg_TempFiles();
                ps = Helper.readBuilder("L_P_MLIST", ps);
                MLog.D("L_P_MLIST", "L_P_MLIST read " + ps.files.size() + "   ");
            }
        }
    }

    public synchronized static void init() {
        PARAM_SAVE = new Msg_TempFiles.Builder(Helper.readBuilder("L_P_MLIST", PARAM_SAVE.build()));
        if (LogConfig.getLoglev() >= 5) {
            MLog.D("L_P_MLIST", "L_P_MLIST read " + PARAM_SAVE.files.size() + "   ");
        }
        if (PARAM_SAVE == null) {
            PARAM_SAVE = new Msg_TempFiles.Builder();
        }
        if (PARAM_SAVE.files == null) {
            PARAM_SAVE.files = new ArrayList<>();
        }
        for (Msg_TempFile file : PARAM_SAVE.files) {
            String cla = file.filepath;
            String key = file.filename;
            Object value = null;
            if (file.filesize == 0) {
                if (!TextUtils.isEmpty(cla)) {
                    try {
                        Class clas = Class.forName(cla);
                        Object obj = clas.newInstance();
                        value = Helper.readBuilder("LPM_" + key, obj);
                    } catch (Exception e) {
                        MLog.D(e);
                    }
                }
            } else if (file.filesize == 1) {
                value = cla;
            } else if (file.filesize == 2) {
                value = Boolean.valueOf(cla);
            } else if (file.filesize == 3) {
                value = Double.valueOf(cla);
            }
            PARAM_HASHMAP.put(file.filename, value);
        }
        String url = getValue("serveruri");
        if (!TextUtils.isEmpty(url) && !url.equals(BaseConfig.getUri())) {
            BaseConfig.setUri(url);
        }
    }


    /**
     * @param value
     * @param localparams
     * @return
     */
    public static Object getValue(String value, Map<String, Object> localparams) {
        if (!(value instanceof String)) {
            return value;
        }
        return getV((String) value, localparams);
    }

    public static Object getV(String key, Map<String, Object> localparams) {
        if ("_serveruri".equals(key)) {
            key = PARAM_SERVER_URI;
        }
        if (key.startsWith("_") || key.startsWith("@")) {
            return ParamsManager.getValue(key.substring(1));
        } else if (key.startsWith("#")) {
            return localparams.get(key.substring(1));
        }
        Object retn = localparams.get(key);
        if (retn != null) {
            return retn;
        } else {
            return ParamsManager.getValue(key);
        }
    }

    public static void putValue(String key, Object obj, PutHas localput) {
        String pkey = key.toLowerCase(Locale.ENGLISH);
        if (key.startsWith("_serveruri")) {
            BaseConfig.setUri(obj.toString());
            ParamsManager.save(pkey.substring(1), obj);
        } else if (key.startsWith("_")) {
            ParamsManager.save(pkey.substring(1), obj);
        } else if (key.startsWith("@")) {
            ParamsManager.put(pkey.substring(1), obj);
        } else if (key.startsWith(" ")) {
            localput.put(key.substring(1), obj);
        } else {
            localput.put(key, obj);
        }
    }

    public interface PutHas {
        public void put(String key, Object value);
    }

}
