package com.mdx.framework.cache;

import com.mdx.framework.server.image.impl.MBitmap;

public class ImageCache extends Cache<Object, MBitmap, Float, Float> {


    public ImageCache() {
        super();
    }

    public void removeAndFile(Object key) {
        super.remove(key);
        final Object k = key;
        synchronized (this) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ImageStoreCacheManage.delete(k.toString());
                }
            }).start();
        }
    }
    
    
    public void removeCacheAndFile(final Object key, final Float qtion, final Float detail) {
        super.removeCache(key, qtion, detail);
        final Object k = key;
        final float q = qtion, d = detail;
        synchronized (this) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ImageStoreCacheManage.delete(k.toString(), q, d);
                }
            }).start();
        }
    }
    
}
