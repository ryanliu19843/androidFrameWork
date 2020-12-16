package com.mdx.framework.server.image;

import android.graphics.BitmapFactory;

import com.mdx.framework.commons.MException;
import com.mdx.framework.commons.ParamsManager;
import com.mdx.framework.log.MLog;
import com.mdx.framework.server.image.impl.ImageRead;
import com.mdx.framework.utility.BitmapRead;
import com.mdx.framework.utility.Util;

import java.io.InputStream;
import java.util.HashMap;

public class JarImageLoad implements ImageRead {

    private String mUrl;

    private Object mObj;

    private int mImageType;

    private HashMap<String, String> mParamsMap = new HashMap<String, String>();

    /**
     * 开始加载图片<BR>
     * [功能详细描述]
     *
     * @param width  控件宽度
     * @param height 控件高度
     * @return
     * @throws MException
     * @see ImageRead#loadImageFromUrl(int,
     * int)
     */
    @Override
    public Object loadImageFromUrl(int width, int height) throws MException {
        mParamsMap.put(ParamsManager.PARAM_VIEW_HEIGHT, String.valueOf(height));
        mParamsMap.put(ParamsManager.PARAM_VIEW_WIDTH, String.valueOf(width));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            return null;
        }
        String path = ParamsManager.getString(mUrl, mParamsMap);
        MLog.D(MLog.SYS_RUN, "load image form jar:"+path);
        path = path.substring(Util.JAR_START.length());
        if (width == 0 && height == 0) {
            return BitmapFactory.decodeFile(path);
        }
        InputStream bitmapin = this.getClass().getResourceAsStream(path);
        return BitmapRead.decodeSampledBitmapFromStream(bitmapin, width, height);
    }

    /**
     * 初始化图片加载<BR>
     *
     * @param url
     * @param object
     * @param imageType
     * @return
     * @see ImageRead#createRead(String,
     * Object, int, boolean)
     */
    @Override
    public void createRead(String url, Object object, int imageType, boolean useCache) {
        mParamsMap.clear();
        this.mUrl = url;
        this.mObj = object;
        this.mImageType = imageType;
        mParamsMap.put(ParamsManager.PARAM_DATA_OBJ, this.mObj.toString());
        mParamsMap.put("imageType", String.valueOf(imageType));
        mParamsMap.put("reload", String.valueOf(this.mImageType));
    }

    /**
     * 中断加载<BR>
     *
     * @see com.mdx.framework.commons.CanIntermit#intermit()
     */
    @Override
    public void intermit() {

    }

    public boolean needCache() {
        return false;
    }
}
