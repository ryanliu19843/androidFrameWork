package com.mdx.framework.server.image;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;

import androidx.palette.graphics.Palette;

import com.mdx.framework.Frame;
import com.mdx.framework.broadcast.BIntent;
import com.mdx.framework.broadcast.BroadCast;
import com.mdx.framework.cache.ImageCache;
import com.mdx.framework.cache.ImageCacheItem;
import com.mdx.framework.cache.ImageStoreCacheManage;
import com.mdx.framework.commons.MException;
import com.mdx.framework.commons.threadpool.PRunable;
import com.mdx.framework.commons.threadpool.ThreadPool;
import com.mdx.framework.log.MLog;
import com.mdx.framework.server.image.impl.ImageBase;
import com.mdx.framework.server.image.impl.ImageRead;
import com.mdx.framework.server.image.impl.ImgUtil;
import com.mdx.framework.server.image.impl.MBitmap;
import com.mdx.framework.utility.Helper;
import com.mdx.framework.utility.Util;

import java.util.ArrayList;
import java.util.HashMap;


public class ImageLoad {
    private ThreadPool threadpool;

    public static final int LOADTYPE_URL = 0;

    public static final int LOADTYPE_CACHE = 1;

    protected HashMap<ImageBase, Trun> objectImages = new HashMap<ImageBase, Trun>();

    protected HashMap<String, Trun> imageObjectReadMap = new HashMap<String, Trun>();

    protected ImageCache cache;

    public synchronized void stop(ImageBase imagebase) {
        Trun imagerun = (Trun) objectImages.get(imagebase);
        if (imagerun != null) {
            imagerun.removeImageBase(imagebase);
        }
    }

    public synchronized Drawable loadDrawable(ImageBase imagebase) {
        stop(imagebase);
        if (!imagebase.isChange() && !imagebase.isReload()) {
            return null;
        }
        String key = getImageString(imagebase);
        if (imageObjectReadMap.containsKey(key)) {
            Trun run = (Trun) imageObjectReadMap.get(key);
            imagebase.setLoadid((new StringBuilder(String.valueOf(run.hashCode()))).toString());
            run.addImageBase(imagebase);
        } else {
            Trun run = new Trun(key);
            imagebase.setLoadid((new StringBuilder(String.valueOf(run.hashCode()))).toString());
            run.addImageBase(imagebase);
            imageObjectReadMap.put(key, run);
            threadpool.execute(run);
        }
        return null;
    }

    private class Trun extends PRunable {
        private Object mObject;

        private ImageRead mCanRead;

        private int mImageType;

        private int mWidth;

        private int mHeight;

        private boolean mUseCache;

        private boolean mReload;

        private boolean isCircle;

        private int blur, round;

        private String imageobj;

        private int loadType;

        private String objKey;

        private boolean usePalette;

        private ArrayList<ImageBase> imagebases = new ArrayList<ImageBase>();

        public void removeImageBase(ImageBase ib) {
            if (imagebases.contains(ib)) {
                imagebases.remove(ib);
                objectImages.remove(ib);
                if (imagebases.size() == 0) {
                    intermit();
                }
            }
        }

        public void addImageBase(ImageBase ib) {
            if (imagebases.size() == 0) {
                set(ib);
            }
            imagebases.add(ib);
            objectImages.put(ib, this);
        }

        private void clear() {
            for (ImageBase ib : imagebases) {
                objectImages.remove(ib);
            }
            imagebases.clear();
            imageObjectReadMap.remove(objKey);
        }

        public void set(ImageBase imagebase) {
            mObject = imagebase.getObj();
            mImageType = imagebase.getLoadType();
            mUseCache = imagebase.isCache();
            mReload = imagebase.isReload();
            mWidth = imagebase.getImageWidth();
            mHeight = imagebase.getImageHeight();
            blur = imagebase.getBlur();
            round = imagebase.getRound();
            isCircle = imagebase.isCircle();
            usePalette = imagebase.isPalette();
            imageobj = getname(imagebase);
        }

        private Object loadImageFromUrl(int width, int height) throws MException {
            if (mCanRead.needCache()) {
                if (mUseCache && !mReload && ImageStoreCacheManage.check(mObject.toString(), width, height) != null) {
                    postBroadcast(mObject, 0, 100);
                    byte[] bytes = ImageStoreCacheManage.read(mObject.toString(), width, height);
                    postBroadcast(mObject, 100, 100);
                    if (bytes != null) {
                        try {
                            Object retn = ImgUtil.ReadGifOrBitmap(bytes, width, height);
                            loadType = LOADTYPE_CACHE;
                            if (retn != null) {
                                return retn;
                            } else {
                                ImageStoreCacheManage.delete(mObject.toString());
                                return null;
                            }
                        } catch (Exception e) {
                            ImageStoreCacheManage.delete(mObject.toString());
                        }
                    }
                }
            }
            return mCanRead.loadImageFromUrl(width, height);
        }

        public MBitmap resetBitmap(Object object) {
            MBitmap mb = new MBitmap();
            if (object == null) {
                return mb;
            }
            if (object instanceof Bitmap) {
                Bitmap bitmap = (Bitmap) object;
                int min = Math.min(bitmap.getWidth(), bitmap.getHeight());
                if (blur > 0) {
                    object = Util.fastblur(Frame.CONTEXT, bitmap, blur, true);
                } else if (blur < 0) {
                    object = Util.fastblur(Frame.CONTEXT, bitmap, -blur, true);
                }
                if (usePalette) {
                    try {
                        Palette pl = Palette.from(bitmap).generate();
                        mb.dark = pl.getDarkMutedColor(0xffffffff);
                        mb.vibrant = pl.getVibrantColor(0xffffffff);
                        mb.light = pl.getLightMutedColor(0xffffffff);
                        mb.muted = pl.getMutedColor(0xffffffff);
                    } catch (Throwable e) {
                    }
                }
            }
            mb.set(object);
            return mb;
        }

        public void onIntermit() {
            if (mCanRead != null) {
                mCanRead.intermit();
            }
            clear();
        }

        public void run() {
            try {   //快速滑动是不加载
                Thread.sleep(50);
            } catch (InterruptedException e1) {
                MLog.D(MLog.SYS_RUN, "image load cancle");
                return;
            }
            try {
                synchronized (Frame.INITLOCK) {
                }
                if (stoped) {
                    return;
                }
                MBitmap drawable = null;
                if (mObject instanceof Integer) {
                    Drawable dra = Frame.CONTEXT.getResources().getDrawable((Integer) mObject);
                    if (dra instanceof BitmapDrawable) {
                        drawable = resetBitmap(((BitmapDrawable) dra).getBitmap());
                    } else {
                        drawable = new MBitmap();
                        drawable.set(dra);
                    }
                } else {
                    mCanRead = createRead(mObject, mImageType, mUseCache);
                    postBroadcast(mObject, 0, 100);
//                long time=System.currentTimeMillis();
                    drawable = resetBitmap(loadImageFromUrl(mWidth, mHeight));
                }
                if (drawable != null && !drawable.isempty()) {
//                Log.d("gest long","longimg"+(System.currentTimeMillis()-time));
                    postBroadcast(mObject, 100, 100);
                    if (mReload) {
                        cache.remove(imageobj);
                    }
                    if (!CheckCache(imageobj, drawable, loadType, mWidth, mHeight, mObject) || mObject != null) {
                        for (ImageBase ib : imagebases) {
                            if (ib.getObj() != null && ib.getObj().equals(this.mObject)) {
                                ib.loaded(drawable, this.hashCode() + "", loadType);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                MLog.D(MLog.SYS_RUN, e);
            } finally {
                clear();
            }
        }

        public Trun(String objKey) {
            mUseCache = false;
            mReload = false;
            isCircle = false;
            blur = 0;
            loadType = 0;
            this.objKey = objKey;
        }
    }

    public ImageLoad() {
        threadpool = new ThreadPool(5);
        cache = new ImageCache();
    }

    public ImageLoad(ImageCache cache) {
        threadpool = new ThreadPool(5);
        if (cache != null) {
            this.cache = cache;
        }
    }

    public static String getname(ImageBase imagebase) {
        return new StringBuilder().append(imagebase.getObj()).append("_").append(imagebase.getBlur()).append("_").append(imagebase.isCircle() ? "1" : "0").append("_").append(imagebase.isPalette() ? "1" : "0").append("_").append(imagebase.getRound()).toString();
    }

    public MBitmap get(ImageBase imagebase) {
        ImageCacheItem imagecache = (ImageCacheItem) cache.get(getname(imagebase), Float.valueOf((float) imagebase.getImageWidth() * 1.0F), Float.valueOf((float) imagebase.getImageHeight() * 1.0F));
        if (imagecache != null)
            return imagecache.getItem();
        else
            return null;
    }

    private String getImageString(ImageBase imagebase) {
        return (new StringBuilder(String.valueOf(imagebase.getObj().toString()))).append("_").append(imagebase.getImageWidth()).append("_").append(imagebase.getImageHeight()).append("_")
                .append(imagebase.getBlur()).append("_").append(imagebase.isCircle() ? "1" : "0").append("_").append(imagebase.getRound()).append("_").append(imagebase.getLoadType()).append("_")
                .append(imagebase.isCache()).append("_").append(imagebase.isReload()).toString();
    }

    protected boolean CheckCache(Object obj, MBitmap drawable, int loadType, int width, int height, Object iobj) {
        if (drawable == null) {
            return false;
        }
        boolean retn = false;
        if (drawable != null && !drawable.isempty()) {
            ImageCacheItem imagecache = new ImageCacheItem(drawable);
            if (drawable.getWidth() < width / 2) {
                imagecache.setQualification(Float.MAX_VALUE);
                imagecache.setDetails(Float.MAX_VALUE);
            } else {
                imagecache.setQualification(Float.valueOf((float) width * 1.0F));
                imagecache.setDetails(Float.valueOf((float) height * 1.0F));
            }
            imagecache.obj = iobj;
            cache.put(obj, imagecache);
            retn = true;
        }
        return retn;
    }

    protected ImageRead createRead(Object object, int imageType, boolean useCache) {
        String url = Util.getUrl(object.toString());
        return ImageReadFactory.createImageRead(url, object, imageType, useCache);
    }

    private void postBroadcast(Object obj, long size, long length) {
        BIntent bi = new BIntent(BroadCast.BROADLIST_IMAGEDOWNLOAD, obj, null, 0, null);
        bi.size = size;
        bi.lenth = length;
        bi.type = 101;
        BroadCast.PostBroad(bi);
    }
}
