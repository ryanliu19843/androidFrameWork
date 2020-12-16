package com.mdx.framework.server.file.read;

import java.io.File;
import java.util.HashMap;
import com.mdx.framework.commons.MException;
import com.mdx.framework.commons.ParamsManager;
import com.mdx.framework.config.ImageConfig;
import com.mdx.framework.server.file.impl.FileRead;
import com.mdx.framework.utility.Device;

public class UrlFileLoad implements FileRead {
    public final static int URLIMAGELOAD_TYPE_POST = 0;
    
    public final static int URLIMAGELOAD_TYPE_GET = 1;
    
    public final static int URLIMAGELOAD_TYPE_NOPARAMS = 2;
    
    public final static int URLIMAGELOAD_TYPE_PARAMS = 0;
    
    private HttpFileRead mHttpFileRead;
    
    private String mUrl;
    
    private Object mObj;
    
    private int mloadType;
    
    private boolean mUseCache;
    
    private HashMap<String, String> mParamsMap = new HashMap<String, String>();
    
    /**
     * 开始加载图片<BR>
     * [功能详细描述]
     * 
     * @param width 控件宽度
     * @param height 控件高度
     * @return
     * @throws MException
     * @see com.mdx.framework.server.image.impl.ImageRead#loadImageFromUrl(int,
     *      int)
     */
    @Override
    public File loadFileFromUrl() throws MException {
        // MLog.D("loadimage1:"+mUrl+"  "+Device.getNetWorkSpeed()+"  "+(ImageConfig.getMinLoadImage()>Device.getNetWorkSpeed()));
        if (ImageConfig.getMinLoadImage() > Device.getNetWorkSpeed()) {
            return null;
        }
        File retn = null;
        String param = ParamsManager.getString(ImageConfig.getImageParam(), mParamsMap);
        String url = ParamsManager.getString(mUrl, mParamsMap);
        String[][] params = getStr2Params(param);
        if (params != null && params.length > 0 && params[0].length == 1) {
            url += params[0][0];
        }
        if (((mloadType >> 0) & 1) == 1) {
            if (((mloadType >> 1) & 1) == 1) {
                retn = mHttpFileRead.get(url, mObj, mloadType, mUseCache, null);
            } else {
                retn = mHttpFileRead.get(url, mObj, mloadType, mUseCache, params);
            }
        } else {
            if (((mloadType >> 1) & 1) == 1) {
                retn = mHttpFileRead.post(url, mObj, mloadType, mUseCache, null);
            } else {
                retn = mHttpFileRead.post(url, mObj, mloadType, mUseCache, params);
            }
        }
        return retn;
    }
    
    private String[][] getStr2Params(String sparam) {
        if (sparam == null || sparam.length() == 0) {
            return new String[][] {};
        }
        String[] strs = sparam.split("[\\&\\?]");
        String[][] retn = new String[strs.length][];
        int i = 0, ind = 0;
        for (String s : strs) {
            if (s != null && s.length() > 0) {
                ind = s.indexOf("=");
                if (ind > 0) {
                    retn[i] = new String[] { s.substring(0, ind), s.substring(ind + 1, s.length()) };
                } else if (i == 0) {
                    retn[i] = new String[] { s };
                }
            }
            i++;
        }
        return retn;
    }
    
    /**
     * 
     * 初始化图片加载<BR>
     * 
     * @param url
     * @param object
     * @param imageType
     * @param reload
     * @return
     * @see com.mdx.framework.server.image.impl.ImageRead#createRead(String,
     *      Object, int, boolean)
     */
    @Override
    public void createRead(String url, Object object, int loadtype, boolean useCache) {
        mParamsMap.clear();
        this.mUrl = url;
        this.mObj = object;
        this.mloadType = loadtype;
        this.mUseCache = useCache;
        this.mHttpFileRead = new HttpFileRead();
        mParamsMap.put(ParamsManager.PARAM_DATA_OBJ, object.toString());
        mParamsMap.put("loadType", String.valueOf(loadtype));
        mParamsMap.put("reload", String.valueOf(mUseCache));
    }
    
    /**
     * 中断加载<BR>
     * 
     * @see com.mdx.framework.commons.CanIntermit#intermit()
     */
    @Override
    public void intermit() {
        if (mHttpFileRead != null) {
            mHttpFileRead.intermit();
        }
    }
}
