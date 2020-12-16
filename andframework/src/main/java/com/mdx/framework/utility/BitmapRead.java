// Decompiled by DJ v3.12.12.98 Copyright 2014 Atanas Neshkov Date: 2015/1/28
// 17:41:48
// Home Page: http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3)
// Source File Name: BitmapRead.java

package com.mdx.framework.utility;

import android.content.res.Resources;
import android.graphics.*;
import android.util.Log;

import com.mdx.framework.log.MLog;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BitmapRead {
    
    public BitmapRead() {
    }
    
    public static int calculateInSampleSize(BitmapFactory.Options options, float reqWidth,
            float reqHeight) {
        int inSampleSize = 1;
        if (reqWidth < options.outWidth || reqHeight < options.outHeight) {
            int widthRadio = Math.round(options.outWidth * 1.0f / reqWidth);
            int heightRadio = Math.round(options.outHeight * 1.0f / reqHeight);
            inSampleSize = Math.min(widthRadio, heightRadio);
        }
        return inSampleSize;
    }
    
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }



    public static Bitmap decodeSampledBitmapFromFile(String filePath, float reqWidth, float reqHeight) {
        if(reqWidth==0 && reqHeight==0){
            return BitmapFactory.decodeFile(filePath);
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }
    
    public static Bitmap decodeSampledBitmapFromByte(byte bytes[], float reqWidth, float reqHeight) {
        return decodeSampledBitmapFromByte(bytes, 0, bytes.length, reqWidth, reqHeight);
    }
    
    public static Rect getRectFromByte(byte bytes[]) {
        return getRectFromByte(bytes, 0, bytes.length);
    }
    
    public static Rect getRectFromByte(byte bytes[], int offset, int length) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, offset, length, options);
        Rect retn = new Rect(0, 0, options.outWidth, options.outHeight);
        return retn;
    }
    
    public static Bitmap decodeSampledBitmapFromByte(byte bytes[], int offset, int length, float reqWidth,
            float reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        if (reqWidth != 0.0F || reqHeight != 0.0F) {
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(bytes, offset, length, options);
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;
        }
        return BitmapFactory.decodeByteArray(bytes, offset, length, options);
    }
    
    public static Bitmap decodeSampledBitmapFromStream(InputStream is, float reqWidth, float reqHeight) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte bytes[] = new byte[1024];
        int c = 0;
        try {
            while ((c = is.read(bytes)) != -1)
                baos.write(bytes, 0, c);
        }
        catch (Exception e) {
            MLog.D("system.err", e);
        }
        return decodeSampledBitmapFromByte(baos.toByteArray(), reqWidth, reqHeight);
    }
    
    public static byte[] getByte(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
    
    public static void saveImage(Bitmap bit, OutputStream out) {
        bit.compress(Bitmap.CompressFormat.JPEG, 90, out);
        try {
            out.flush();
            out.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    public static Bitmap rotationImg(Bitmap bm, final int orientationDegree) {
        if(orientationDegree!=0){
            int w = bm.getWidth(), h = bm.getHeight();
            Matrix m = new Matrix();
            m.setRotate(orientationDegree, (float) w / 2, (float) h / 2);
            Bitmap bitmap = Bitmap.createBitmap(bm, 0, 0, w, h, m, true);
            if(bm!=bitmap){
                bm.recycle();
            }
            bm=bitmap;
        }
        return bm;
    }
    
    public static Bitmap decodeBitmapSize(Bitmap bitmap, float reqWidth, float reqHeight) {
        byte byts[] = getByte(bitmap);
        bitmap.recycle();
        return decodeSampledBitmapFromByte(byts, reqWidth, reqHeight);
    }
}
