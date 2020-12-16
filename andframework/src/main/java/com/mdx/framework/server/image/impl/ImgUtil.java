package com.mdx.framework.server.image.impl;

import com.mdx.framework.utility.BitmapRead;

import java.io.IOException;
import java.io.InputStream;

import pl.droidsonroids.gif.GifDrawable;

/**
 * Created by ryan on 2017/5/16.
 */

public class ImgUtil {
    public static Object ReadGifOrBitmap(byte[] bytes,float w,float h) throws IOException {
        Object retn;
        if (isGif(bytes)) {
            GifDrawable gifDrawable = new GifDrawable(bytes);
            if (gifDrawable.getNumberOfFrames() > 1) {
                retn = gifDrawable;
            } else {
                gifDrawable.recycle();
                retn = BitmapRead.decodeSampledBitmapFromByte(bytes, w, h);
            }
        }else{
            retn = BitmapRead.decodeSampledBitmapFromByte(bytes, w, h);
        }
        return retn;
    }

    public static Object ReadGifOrBitmap(InputStream in, float w, float h) throws IOException {
        Object retn;
        if (isGif(in)) {
            in.reset();
            GifDrawable gifDrawable = new GifDrawable(in);
            if (gifDrawable.getNumberOfFrames() > 1) {
                retn = gifDrawable;
            } else {
                in.reset();
                gifDrawable.recycle();
                retn = BitmapRead.decodeSampledBitmapFromStream(in, w, h);
            }
        }else{
            in.reset();
            retn = BitmapRead.decodeSampledBitmapFromStream(in, w, h);
        }
        return retn;
    }

    public static boolean isGif(byte[] bytes) {
        if (bytes[0] == 71 && bytes[1] == 73 && bytes[2] == 70) {
            return true;
        }
        return false;
    }


    public static boolean isGif(InputStream in) throws IOException {
        byte[] bytes=new byte[3];
        in.read(bytes);

        return isGif(bytes);
    }
}
