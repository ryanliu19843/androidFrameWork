package com.mdx.framework.server.image;

import android.graphics.Rect;

import com.mdx.framework.Frame;
import com.mdx.framework.broadcast.BIntent;
import com.mdx.framework.broadcast.BroadCast;
import com.mdx.framework.cache.ImageStoreCacheManage;
import com.mdx.framework.commons.MException;
import com.mdx.framework.config.ToolsConfig;
import com.mdx.framework.server.HttpRead;
import com.mdx.framework.server.image.impl.ImgUtil;
import com.mdx.framework.utility.BitmapRead;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * @author ryan
 */
public class HttpImageRead extends HttpRead<Object> {
    private Object obj;

    private float w, h;

    private boolean mUseCache;

    private int loadtype;


    /**
     * 用get方法读取图片
     *
     * @param url    读取url
     * @param obj    参数名称
     * @param width  控件宽度
     * @param height 控件高度
     * @param params 传递参数
     * @return
     * @throws MException
     */
    public Object get(String url, Object obj, int loadtype, boolean useCache, float width, float height, String[][] params) throws MException {
        this.statisticsTag = ToolsConfig.TAG_IMAGE_NET_DOWN;
        this.obj = obj;
        this.w = width;
        this.h = height;
        this.mUseCache = useCache;
        this.loadtype = loadtype;
        postBroadcast(obj, 0, 2048);
        return super.get(url, params);
    }

    /**
     * 用get方法读取图片
     *
     * @param url    读取url
     * @param obj    参数名称
     * @param width  控件宽度
     * @param height 控件高度
     * @param params 传递参数
     * @return
     * @throws MException
     */
    public Object post(String url, Object obj, int loadtype, boolean useCache, float width, float height, String[][] params) throws MException {
        this.statisticsTag = ToolsConfig.TAG_IMAGE_NET_DOWN;
        this.obj = obj;
        this.w = width;
        this.h = height;
        this.mUseCache = useCache;
        postBroadcast(obj, 0, 2048);
        return super.post(url, params);
    }

    @Override
    public Object read(HttpURLConnection response, String url, String[][] params) throws MException {
        try {
            int code = response.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK) {
                long fileSize = response.getContentLength();
                if (fileSize <= 0) {
                    fileSize = 2048;
                }
                postBroadcast(obj, 512, fileSize);
                long now = System.currentTimeMillis();
                ByteArrayOutputStream fos = new ByteArrayOutputStream();
                byte[] bt = new byte[1024 * 16];
                InputStream in = response.getInputStream();
                int ind = 0;
                while ((ind = in.read(bt)) >= 0 && !this.stop) {
                    fos.write(bt, 0, ind);
                    if (fos.size() > fileSize) {
                        fileSize = fos.size();
                    }
                    if ((System.currentTimeMillis() - now) > 500) {
                        postBroadcast(obj, fos.size(), fileSize);
                        now = System.currentTimeMillis();
                    }
                }
                postBroadcast(obj, fos.size(), fileSize);
                fos.flush();
                fos.close();
                in.close();
                if (ind != -1 || this.stop) {
                    throw new MException(97, "stop");
                }
                postBroadcast(obj, fileSize, fileSize);
                this.statisticsResponse(response);
                if (this.mUseCache) {
                    if (((loadtype >> 1) & 1) == 1) {
                        w = 0;
                        h = 0;
                    } else {
                        Rect r = BitmapRead.getRectFromByte(fos.toByteArray());
                        if (r.right < w / Frame.DENSITY && r.height() < h / Frame.DENSITY) {
                            w = 0;
                            h = 0;
                        }
                    }
                }
                byte[] bytes = fos.toByteArray();
                Object retn = ImgUtil.ReadGifOrBitmap(bytes,w,h);
                if (retn != null) {
                    ImageStoreCacheManage.save(obj.toString(), bytes, w, h); // 写入硬盘缓存
                    postBroadcast(obj, fileSize, fileSize);
                    return retn;
                }
                return null;
            } else {
                throw new MException(98);
            }
        } catch (MException e) {
            throw e;
        } catch (Exception e) {
            throw new MException(98, e);
        }
    }

    private void postBroadcast(Object obj, long size, long length) {
        BIntent bi = new BIntent(BroadCast.BROADLIST_IMAGEDOWNLOAD, obj, null, 0, null);
        bi.size = size;
        bi.lenth = length;
        BroadCast.PostBroad(bi);
    }
}
