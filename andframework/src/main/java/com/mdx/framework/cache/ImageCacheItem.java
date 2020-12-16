package com.mdx.framework.cache;

import android.util.Log;

import com.mdx.framework.config.ToolsConfig;
import com.mdx.framework.server.image.impl.MBitmap;

import java.util.List;

public class ImageCacheItem extends CacheItem<MBitmap, Float, Float> {
    
    public ImageCacheItem(MBitmap item) {
        super(item);
        this.tag = ToolsConfig.TAG_IMAGE_MEMORY_CACHE;
        this.init();
    }
    
    @Override
    public boolean isDirty() {
        return false;
    }
    
    @Override
    protected long calcMemSize() {
        if (this.getItem() == null || this.getItem() == null) {
            return 0;
        }
        if (getItem() != null) {
            return getItem().sizeOf();
        }
        return 2000;
    }
    
    @Override
    public void destory() {
        if (getItem() == null || getItem() == null) {
            return;
        }
        synchronized (getItem()) {
            try {
                Log.d("test","recycle"+getItem());
                getItem().recycle();
            }
            catch (Exception e) {}
        }
    }
    
    @Override
    public int compare(Float width, Float height) {
        if (this.getQualification() != null && this.getQualification() >= width) {
            return 0;
        }
        return 1;
    }
    
    public void clearList(List<?> l) {
        for (Object obj : l) {
            if (obj instanceof ImageCacheItem) {
                ((ImageCacheItem) obj).destory();
            }
        }
        l.clear();
    }
}
