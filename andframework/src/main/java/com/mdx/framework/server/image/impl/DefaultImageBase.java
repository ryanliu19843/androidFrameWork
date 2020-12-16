/*
 * 文件名: DefaultImageBase.java 版 权： Copyright XCDS Tech. Co. Ltd. All Rights
 * Reserved. 描 述: [该类的简要描述] 创建人: ryan 创建时间:2014年6月4日
 * 
 * 修改人： 修改时间: 修改内容：[修改内容]
 */
package com.mdx.framework.server.image.impl;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 *
 * @author ryan
 * @version [2014年6月4日 下午7:20:56]
 */
public class DefaultImageBase implements ImageBase {
    private Object object;
    public boolean isPalette = false;
    private OnLoadedListener mOnLoadedListener;

    private int mWidth = 0, mHeight = 0;

    private boolean isCache = true, autosize = true;

    protected int blur = 0;

    protected boolean isCircle = false;

    public DefaultImageBase(Object obj, OnLoadedListener onLoadedListener) {
        this.object = obj;
        this.mOnLoadedListener = onLoadedListener;
    }

    public DefaultImageBase(Object obj, OnLoadedListener onLoadedListener, int width, int height, boolean cache) {
        this.object = obj;
        this.mOnLoadedListener = onLoadedListener;
        this.mWidth = width;
        this.mHeight = height;
        this.isCache = cache;
    }

    @Override
    public Object getObj() {
        return object;
    }


    @Override
    public int getImageWidth() {
        return mWidth;
    }

    @Override
    public int getImageHeight() {
        return mHeight;
    }

    @Override
    public boolean isCache() {
        return isCache;
    }

    @Override
    public boolean isReload() {
        return false;
    }

    @Override
    public int getLoadType() {
        return 0;
    }

    @Override
    public boolean isChange() {
        return true;
    }

    @Override
    public void loaded(MBitmap drawable, String loadingid, int type) {
        if (mOnLoadedListener != null) {
            mOnLoadedListener.onLoaded(drawable, loadingid);
        }
    }

    @Override
    public void setLoadid(String id) {

    }

    @Override
    public boolean isPalette() {
        return isPalette;
    }

    public interface OnLoadedListener {
        public void onLoaded(MBitmap drawable, String loadingid);
    }

    @Override
    public int getBlur() {
        return blur;
    }

    @Override
    public boolean isCircle() {
        return isCircle;
    }

    public boolean isAutosize() {
        return autosize;
    }

    public void setAutosize(boolean autosize) {
        this.autosize = autosize;
    }

    public void setBlur(int blur) {
        this.blur = blur;
    }

    public void setCircle(boolean isCircle) {
        this.isCircle = isCircle;
    }

    /**
     * [一句话功能简述]<BR>
     * [功能详细描述]
     *
     * @return
     * @see ImageBase#getLoadContext()
     */

    @Override
    public Object getLoadContext() {
        return this;
    }

    @Override
    public int getRound() {
        return 0;
    }
}
