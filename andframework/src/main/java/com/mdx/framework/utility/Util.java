package com.mdx.framework.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.TextUtils;
import android.view.View;

import com.mdx.framework.config.BaseConfig;
import com.mdx.framework.config.ErrorConfig;
import com.mdx.framework.config.ImageConfig;
import com.mdx.framework.prompt.ErrorPrompt;
import com.mdx.framework.server.api.ErrorMsg;
import com.mdx.framework.server.api.Son;
import com.mdx.framework.widget.util.MScrollAble;

import java.io.File;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 系统常用方法以及参数<BR>
 * 系统常用方法与阐述
 *
 * @author ryan
 * @version [2013-11-11 上午10:28:10]
 */
public class Util {
    public static Handler HANDLER = new Handler(Looper.getMainLooper());

    public static final String FILE_START = "FILE:";

    public static final String JAR_START = "JAR:";

    public static final String ASSETS_START = "ASSETS:";

    public static final String MEDIA_START = "MEDIA:";

    public static final String MINMEDIA_START = "MINMEDIA:";

    public static void post(Runnable run) {
        HANDLER.post(run);
    }

    /**
     * @param url 需要判断的url
     * @return boolean
     * @throws
     * @author ryan
     * @Title: 是否是完整的url
     * @Description: TODO 判断是否是完整的url
     */
    public static boolean isFullUrl(String url) {
        return (url.toUpperCase(Locale.ENGLISH).startsWith("HTTP://") || url.toUpperCase(Locale.ENGLISH).startsWith("HTTPS://") || url.toUpperCase(Locale.ENGLISH).startsWith("FTP://"));
    }

    public static DefaultClickListener newClickLitener(Object parent, String click) {
        return new DefaultClickListener(parent, click);
    }

    public static boolean isMedia(String url) {
        return url.toUpperCase(Locale.ENGLISH).startsWith("MEDIA:");
    }

    public static boolean isContent(String url) {
        return url.toUpperCase(Locale.ENGLISH).startsWith("CONTENT:");
    }

    public static boolean isMinMedia(String url) {
        return url.toUpperCase(Locale.ENGLISH).startsWith("MINMEDIA:");
    }

    /**
     * @param url 需要判断的url
     * @return boolean
     * @throws
     * @author ryan
     * @Title: 是否是本地的文件
     * @Description: TODO 判断是否是完整的url
     */
    public static boolean isFileUrl(String url) {
        return (url.toUpperCase(Locale.ENGLISH).startsWith(FILE_START));
    }

    /**
     * @param url 需要判断的url
     * @return boolean
     * @throws
     * @author ryan
     * @Title: 是否是Assets中的文件
     * @Description: TODO 判断是否是完整的url
     */
    public static boolean isAssets(String url) {
        return (url.toUpperCase(Locale.ENGLISH).startsWith(ASSETS_START));
    }

    /**
     * @param url 需要判断的url
     * @return boolean
     * @throws
     * @author ryan
     * @Title: 是否是本地的文件
     * @Description: TODO 判断是否是完整的url
     */
    public static boolean isJarUrl(String url) {
        return (url.toUpperCase(Locale.ENGLISH).startsWith(JAR_START));
    }

    @SuppressLint("NewApi")
    public static long sizeOfBitmap(Bitmap bitmap) {
        if (VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        }
        return bitmap.getRowBytes() * bitmap.getHeight();

    }

    /**
     * 校验字符串是否符合表达式
     *
     * @param str    字符串
     * @param patten 表达式
     * @return
     * @throws
     * @author ryan
     * @Title: isPatten 是否符合表达式
     * @Description: TODO
     */
    public static boolean isPatten(String str, String patten) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile(patten);
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return true;
        }
        return false;
    }

    /**
     * 获取缓存路径并建立文件
     *
     * @param context
     * @param filename
     * @return
     * @throws
     * @author ryan
     * @Title: getPath
     * @Description: TODO
     */
    public static File getPath(Context context, String path, String filename) {
        if (path != null && path.startsWith("/")) {
            path = path.substring(1);
        }
        File file = getDPath(context, path);
        if (file != null && file.isDirectory()) {
            return new File(file, filename);
        }
        return null;
    }

    /**
     * 获取缓存路径
     *
     * @param context
     * @return
     * @throws
     * @author ryan
     * @Title: getDPath
     * @Description: TODO
     */
    public static File getDPath(Context context, String path) {
        if (path != null && path.startsWith("/")) {
            path = path.substring(1);
        }
        File file = null;
//        int checkCallPhonePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);&& PackageManager.PERMISSION_GRANTED == checkCallPhonePermission
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && BaseConfig.isUseSdcard()) {
            File fparent = new File(context.getExternalCacheDir(), "");
            file = new File(fparent.getPath() + "/" + ((path == null || path.length() == 0) ? "" : (path)));
        } else {
            File fparent = context.getCacheDir();
            if (!fparent.exists()) {
                fparent.mkdir();
            }
            file = new File(fparent.getPath() + "/" + ((path == null || path.length() == 0) ? "" : ("/" + path)));
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /***
     * 获取手机内部地址
     *
     * @param context
     * @param path
     * @return
     */

    public static File getMPath(Context context, String path) {
        if (path != null && path.startsWith("/")) {
            path = path.substring(1);
        }
        File file = null;
        File fparent = context.getDir("frame", Context.MODE_PRIVATE);
        if (!fparent.exists()) {
            fparent.mkdir();
        }
        file = new File(fparent.getPath() + "/" + ((path == null || path.length() == 0) ? "" : ("/" + path)));
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static File getMPath(Context context, String path, String filename) {
        if (path != null && path.startsWith("/")) {
            path = path.substring(1);
        }
        File file = getMPath(context, path);
        if (file != null && file.isDirectory()) {
            return new File(file, filename);
        }
        return null;
    }

    public static String getUrl(String imgUrl) {
        if (Util.isFullUrl(imgUrl))
            return imgUrl;
        if (Util.isFileUrl(imgUrl))
            return imgUrl;
        if (Util.isJarUrl(imgUrl))
            return imgUrl;
        if (Util.isAssets(imgUrl))
            return imgUrl;
        if (Util.isMedia(imgUrl))
            return imgUrl;
        if (Util.isMinMedia(imgUrl))
            return imgUrl;
        if (Util.isMinMedia(imgUrl))
            return imgUrl;
        if (Util.isContent(imgUrl))
            return imgUrl;
        String url = ImageConfig.readImageUrl(imgUrl);
        return url;
    }

    public static String number2String(Double d) {
        if (d == null) {
            return null;
        }
        if (d % 1 != 0) {
            return String.valueOf(d);
        } else {
            return String.valueOf(Math.round(d));
        }
    }

    public static void sleep(long sleep) {
        try {
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
        }
    }

    public synchronized static Boolean setScrollAbleParent(View view, boolean bol) {
        while (view.getParent() instanceof View) {
            view = (View) view.getParent();
            if (view instanceof MScrollAble) {
                MScrollAble scable = (MScrollAble) view;
                scable.setScrollAble(bol);
            }
        }
        return bol;
    }

    public static synchronized ErrorMsg ShowError(Son son, Context mContext) {
        ErrorMsg error = ErrorConfig.getErrorMsg(son);
        if (error.type % 100 / 10 == 0) {
            final ErrorPrompt ed = error.getMsgPrompt(mContext);
            ed.setMsg(error);
            synchronized (Util.HANDLER) {

            }
            Util.HANDLER.post(new Runnable() {
                @Override
                public void run() {
                    ed.show();
                }
            });
        }
        return error;
    }


    @SuppressLint("NewApi")
    public static Bitmap fastblur(Context context, Bitmap sentBitmap, int radius, boolean copy) {

        if (VERSION.SDK_INT > 16) {
            Bitmap bitmap = sentBitmap;
            if (copy) {
                bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
            }

            final RenderScript rs = RenderScript.create(context);
            final Allocation input = Allocation.createFromBitmap(rs, sentBitmap, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
            final Allocation output = Allocation.createTyped(rs, input.getType());
            final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setRadius(radius /* e.g. 3.f */);
            script.setInput(input);
            script.forEach(output);
            output.copyTo(bitmap);
            return bitmap;
        }
        Bitmap bitmap = sentBitmap;
        if (copy) {
            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        }

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return (bitmap);
    }

}
