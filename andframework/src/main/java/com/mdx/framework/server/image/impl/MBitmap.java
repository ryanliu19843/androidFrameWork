package com.mdx.framework.server.image.impl;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.mdx.framework.Frame;
import com.mdx.framework.utility.Util;

import pl.droidsonroids.gif.GifDrawable;

/**
 * Created by ryan on 2016/4/15.
 */
public class MBitmap {
    private Bitmap bitmap;
    public int dark = 0xffffffff, vibrant = 0xffffffff, light = 0xffffffff, muted = 0xffffffff;
    public boolean isGif = false;
    private GifDrawable gifDrawable;
    private Drawable drawable;

    public void recycle() {
        if (bitmap != null) {
            bitmap.recycle();
        }
        if (gifDrawable != null) {
            gifDrawable.recycle();
        }
        if (drawable != null) {

        }
    }

    public void set(Object object) {
        if (object instanceof Bitmap) {
            this.setBitmap((Bitmap) object);
        } else if (object instanceof GifDrawable) {
            this.setGifDrawable((GifDrawable) object);
        } else if (object instanceof Drawable) {
            this.drawable = (Drawable) object;
        }
    }

    public boolean isRecycled() {
        if (isGif) {
            if (gifDrawable != null) {
                return gifDrawable.isRecycled();
            }
        } else {
            if (bitmap != null) {
                return bitmap.isRecycled();
            }
        }
        return false;
    }


    public long sizeOf() {
        long retn = 0;
        if (isGif) {
            if (gifDrawable != null) {
                retn = gifDrawable.getMetadataAllocationByteCount();
            }
        } else {
            if (bitmap != null) {
                retn = Util.sizeOfBitmap(bitmap);
            }
        }
        return retn;
    }

    public GifDrawable getGifDrawable() {
        return gifDrawable;
    }

    public void setGifDrawable(GifDrawable gifDrawable) {
        isGif = true;
        this.gifDrawable = gifDrawable;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        isGif = false;
        this.bitmap = bitmap;
    }


    public int getWidth() {
        if (isGif) {
            return gifDrawable.getIntrinsicWidth();
        } else if (bitmap != null) {
            return bitmap.getWidth();
        } else {
            return drawable.getIntrinsicHeight();
        }
    }

    public int getHeight() {
        if (isGif) {
            return gifDrawable.getIntrinsicHeight();
        } else if (bitmap != null) {
            return bitmap.getHeight();
        } else {
            return drawable.getIntrinsicHeight();
        }
    }

    public Drawable getDrawable() {
        if (isGif) {
            return new MGifDrawable(gifDrawable);
        } else if (bitmap != null) {
            return new BitmapDrawable(Frame.CONTEXT.getResources(), bitmap);
        } else {
            return drawable;
        }
    }

    public boolean isempty() {
        if (isGif) {
            return gifDrawable == null;
        } else {
            return bitmap == null && drawable == null;
        }
    }

}
