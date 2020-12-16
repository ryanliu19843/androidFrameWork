package com.mdx.framework.config;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mdx.framework.commons.ParamsManager;

/**
 * @author ryan
 */
public class BaseConfig {
    
    /**
     * 基本url
     */
    private static String mUri = "";
    private static Map<String,String> mUriMap=new HashMap<String,String>();
    /**
     * 更新地址
     */
    private static String mUpdateUri = "";
    
    private static String mAppid = "";
    
    /**
     * 缓存路径
     */
    private static String mTempPath = "[app_packagemd5]";
    
    /**
     * 是否使用sdcard
     */
    private static boolean mUseSdcard = true;
    
    /**
     * 设置uri基地址
     * 
     * @return
     */
    public synchronized static String getUri() {
        return mUri;
    }
    
    
    public synchronized static String getUri(String key) {
        if(mUriMap.containsKey(key)){
            return mUriMap.get(key);
        }
        return mUri;
    }

    public synchronized static String getNUri(String key) {
        if(mUriMap.containsKey(key)){
            return mUriMap.get(key);
        }
        return null;
    }
    
    /**
     * 设置uri基地址
     * 
     */
    public synchronized static void setUri(String Uri) {
        mUri = Uri;
        ParamsManager.put(ParamsManager.PARAM_SERVER_URI, mUri);
    }
    
    /**
     * 设置更新地址
     * 
     * @author ryan
     * @Title: getUpdateUri
     * @Description: TODO
     * @return
     * @throws
     */
    public synchronized static String getUpdateUri() {
        return ParamsManager.getString(mUpdateUri);
    }
    
    /**
     * 设置更新地址
     * 
     * @author ryan
     * @Title: setUpdateUri
     * @Description: TODO
     * @param UpdateUri
     * @throws
     */
    public synchronized static void setUpdateUri(String UpdateUri) {
        mUpdateUri = UpdateUri;
        ParamsManager.put(ParamsManager.PARAM_SERVER_UPDATE, mUpdateUri);
    }
    
    public synchronized static String getAppid() {
        return mAppid;
    }
    
    public synchronized static void setAppid(String Appid) {
        BaseConfig.mAppid = Appid;
        ParamsManager.put(ParamsManager.PARAM_DATA_APPID, mAppid);
    }
    
    /**
     * 获取缓存和临时文件路径
     * 
     * @return
     */
    public synchronized static String getTempPath() {
        return ParamsManager.getString(mTempPath);
    }
    
    /**
     * 缓存和临时文件路径
     */
    public synchronized static void setTempPath(String TempPath) {
        mTempPath = TempPath;
    }
    
    /**
     * 是否使用sd卡
     * @author ryan
     * @Title: isUseSdcard 
     * @Description: TODO
     * @return
     * @throws
     */
    public synchronized static boolean isUseSdcard() {
        return mUseSdcard;
    }
    
    /**
     * 缓存是否使用sd卡
     * @author ryan
     * @Title: setUseSdcard 
     * @Description: TODO
     * @param UseSdcard
     * @throws
     */
    public synchronized static void setUseSdcard(boolean UseSdcard) {
        BaseConfig.mUseSdcard = UseSdcard;
    }
    
    public synchronized static void addUri(String name,String uri){
        mUriMap.put(name, uri);
    }

    public static String readUrI(String str){
        if(str.trim().startsWith("[")){
            Pattern pattern = Pattern.compile("\\[([A-Za-z0-9=\\-_]*?)\\]");
            Matcher matcher = pattern.matcher(str);
            String name="";
            while (matcher.find()) {
                if (matcher.groupCount() > 0) {
                    name=matcher.group(1);
                    break;
                }
            }
            return getUri(name);
        }else{
            return getUri();
        }
    }
}
