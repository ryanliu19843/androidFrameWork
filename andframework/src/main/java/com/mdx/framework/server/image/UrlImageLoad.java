package com.mdx.framework.server.image;

import com.mdx.framework.commons.MException;
import com.mdx.framework.commons.ParamsManager;
import com.mdx.framework.config.ImageConfig;
import com.mdx.framework.log.MLog;
import com.mdx.framework.server.image.impl.ImageRead;
import com.mdx.framework.utility.Device;

import java.util.HashMap;

public class UrlImageLoad implements ImageRead {
    public final static int URLIMAGELOAD_TYPE_POST = 0;
    
    public final static int URLIMAGELOAD_TYPE_GET = 1;
    
    public final static int URLIMAGELOAD_TYPE_NOPARAMS = 2;
    
    public final static int URLIMAGELOAD_TYPE_PARAMS = 0;
    
    private HttpImageRead mHttpImageRead;
    
    private String mUrl;
    
    private Object mObj;
    
    private int mImageType;
    
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
     * @see ImageRead#loadImageFromUrl(int,
     *      int)
     */
    @Override
    public Object loadImageFromUrl(int width, int height) throws MException {
        MLog.D(MLog.SYS_RUN, "load image form media:"+mUrl);
        if (ImageConfig.getMinLoadImage() > Device.getNetWorkSpeed()) {
            return null;
        }
        Object drawable = null;
        mParamsMap.put(ParamsManager.PARAM_VIEW_HEIGHT, String.valueOf(height));
        mParamsMap.put(ParamsManager.PARAM_VIEW_WIDTH, String.valueOf(width));
        String param = ParamsManager.getString(ImageConfig.getImageParam(), mParamsMap);
        String url = ParamsManager.getString(mUrl, mParamsMap);
        String[][] params = getStr2Params(param);
        if (params != null && params.length > 0 && params[0].length == 1) {
            url += params[0][0];
        }
        if (((mImageType >> 0) & 1) == 1) {
            if (((mImageType >> 1) & 1) == 1) {
                drawable = mHttpImageRead.get(url, mObj, mImageType, mUseCache, width, height, null);
            } else {
                drawable = mHttpImageRead.get(url, mObj, mImageType, mUseCache, width, height, params);
            }
        } else {
            if (((mImageType >> 1) & 1) == 1) {
                drawable = mHttpImageRead.post(url, mObj, mImageType, mUseCache, width, height, null);
            } else {
                drawable = mHttpImageRead.post(url, mObj, mImageType, mUseCache, width, height, params);
            }
        }
        return drawable;
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
     * @see ImageRead#createRead(String,
     *      Object, int, boolean)
     */
    @Override
    public void createRead(String url, Object object, int imageType, boolean useCache) {
        mParamsMap.clear();
        this.mUrl = url;
        this.mObj = object;
        this.mImageType = imageType;
        this.mUseCache = useCache;
        this.mHttpImageRead = new HttpImageRead();
        mParamsMap.put(ParamsManager.PARAM_DATA_OBJ, object.toString());
        mParamsMap.put("imageType", String.valueOf(imageType));
        mParamsMap.put("reload", String.valueOf(mUseCache));
    }
    
    /**
     * 中断加载<BR>
     * 
     * @see com.mdx.framework.commons.CanIntermit#intermit()
     */
    @Override
    public void intermit() {
        if (mHttpImageRead != null) {
            mHttpImageRead.intermit();
        }
    }
    
    public boolean needCache() {
        return true;
    }
}
