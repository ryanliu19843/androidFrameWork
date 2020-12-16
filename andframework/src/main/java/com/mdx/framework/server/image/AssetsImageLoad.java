package com.mdx.framework.server.image;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.mdx.framework.Frame;
import com.mdx.framework.commons.MException;
import com.mdx.framework.commons.ParamsManager;
import com.mdx.framework.log.MLog;
import com.mdx.framework.server.image.impl.ImageRead;
import com.mdx.framework.server.image.impl.ImgUtil;
import com.mdx.framework.utility.Util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class AssetsImageLoad implements ImageRead {

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
        String path = ParamsManager.getString(mUrl, mParamsMap);
        path = path.substring(Util.ASSETS_START.length());
        MLog.D(MLog.SYS_RUN, "load image form assets:"+path);
        if (width == 0 && height == 0) {
            return getImageFromAssetsFile(path);
        }
        InputStream bitmapin;
        try {
            bitmapin = Frame.CONTEXT.getAssets().open(path);
            return ImgUtil.ReadGifOrBitmap(bitmapin,width,height);
        } catch (IOException e) {
            MLog.D(MLog.SYS_RUN, e);
        }
        return null;
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

    private Bitmap getImageFromAssetsFile(String fileName) {
        Bitmap image = null;
        AssetManager am = Frame.CONTEXT.getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;

    }

    public boolean needCache() {
        return false;
    }
}
