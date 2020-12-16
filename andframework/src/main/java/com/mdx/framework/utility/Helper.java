/*
 * 文件名: Helper.java 版 权： Copyright XCDS Tech. Co. Ltd. All Rights Reserved. 描 述:
 * [该类的简要描述] 创建人: ryan 创建时间:2014年6月4日
 *
 * 修改人： 修改时间: 修改内容：[修改内容]
 */
package com.mdx.framework.utility;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.preference.PreferenceManager;

import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListener;

import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.protobuf.GeneratedMessage;
import com.mdx.framework.Frame;
import com.mdx.framework.R;
import com.mdx.framework.autofit.AutoFitFragment;
import com.mdx.framework.commons.data.Method;
import com.mdx.framework.config.LogConfig;
import com.mdx.framework.log.MLog;
import com.mdx.framework.server.image.impl.DefaultImageBase;
import com.mdx.framework.server.image.impl.DefaultImageBase.OnLoadedListener;
import com.mdx.framework.utility.permissions.PermissionRequest;
import com.mdx.framework.utility.permissions.PermissionsHelper;
import com.mdx.framework.widget.getphoto.PopUpdataPhoto;
import com.mdx.framework.widget.getphoto.PopUpdataPhoto.OnReceiverPhoto;
import com.mdx.framework.widget.getphoto.PopUpdataPhoto.OnReceiverPhotos;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 *
 * @author ryan
 * @version [2014年6月4日 下午7:51:53]
 */
public class Helper {
    private final static int kSystemRootStateUnknow = -1;

    private final static int kSystemRootStateDisable = 0;

    private final static int kSystemRootStateEnable = 1;

    private static int systemRootState = kSystemRootStateUnknow;

    public synchronized static void loadImage(Object obj, OnLoadedListener onListener) {
        DefaultImageBase dib = new DefaultImageBase(obj, onListener);
        Frame.IMAGELOAD.loadDrawable(dib);
    }

    public synchronized static void toast(final CharSequence text, final Context context) {
        Util.HANDLER.post(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(context, text, Toast.LENGTH_LONG).show();
                Log.d("toast","toast********************************:"+text);
            }
        });
    }

    public synchronized static void toaste(final Exception e, final Context context) {
        Util.HANDLER.post(new Runnable() {

            @Override
            public void run() {
                MLog.D(e);
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public int getActionbarHeight(Context context) {
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            int actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
            return actionBarHeight;
        }
        return 0;
    }

    public static DelayClickListener delayClickLitener(View.OnClickListener click) {
        return new DelayClickListener(click);
    }

    public int getStatuHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(x);
            return sbar;
        } catch (Exception e1) {
            MLog.E("get status bar height fail");
            e1.printStackTrace();
        }
        return 0;
    }

    public static void requestPermissions(String[] permissions, PermissionRequest runnable) {
        PermissionsHelper.requestPermissions(permissions, runnable);
    }

    public static void requestPermissions(PermissionRequest runnable, String... permissions) {
        PermissionsHelper.requestPermissions(permissions, runnable);
    }

    public static boolean checkPermission(String permission) {
        int granted = Frame.CONTEXT.getPackageManager().checkPermission(permission, Frame.CONTEXT.getPackageName());
        return granted == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 判断键盘是否显示
     *
     * @param view
     * @param bol
     */
    public static String changePasswdShow(View view, boolean bol) {
        if (!(view instanceof TextView)) {
            return "";
        }

        TextView passwd = (TextView) view;
        int ind = passwd.getText().length();
        if (passwd instanceof EditText) {
            ind = ((EditText) view).getSelectionStart();
        }
        if (bol) {
            passwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            passwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        if (passwd instanceof EditText) {
            ((EditText) passwd).setSelection(ind);
        }
        return "";
    }

    /**
     * 获取多图 不剪裁
     *
     * @param context          上下文
     * @param onReceiverPhotos 反悔图片监听
     * @param size             获取数量
     * @throws
     * @author Administrator
     * @Title: getPhotos
     * @Description: TODO
     */
    @SuppressWarnings("deprecation")
    public static void getPhotos(final Context context, final OnReceiverPhotos onReceiverPhotos, final int size) {
        Helper.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, new PermissionRequest() {
            @Override
            public void onGrant(String[] permissions, int[] grantResults) {
                PopUpdataPhoto pup = new PopUpdataPhoto(context, null, onReceiverPhotos);
                pup.putParams("Max", size);
                pup.shown();
            }
        });
    }

    /**
     * 获取图片并剪裁
     *
     * @param context         上下文
     * @param onReceiverPhoto 监听
     * @param aspectX         剪裁比例 宽度
     * @param aspectY         剪裁比例 高度
     * @param outputX         输出像素 宽度
     * @param outputY         输出像素 高度
     * @throws
     * @author Administrator
     * @Title: getPhoto
     * @Description: TODO
     */
    @SuppressWarnings("deprecation")
    public static void getPhoto(final Context context, final OnReceiverPhoto onReceiverPhoto, final Integer aspectX, final Integer aspectY, final Integer outputX, final Integer outputY) {
        Helper.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, new PermissionRequest() {
            @Override
            public void onGrant(String[] permissions, int[] grantResults) {
                PopUpdataPhoto pup = new PopUpdataPhoto(context, onReceiverPhoto, null);
                if (aspectX != null && aspectX != -1) {
                    pup.putParams("aspectX", aspectX);
                }
                if (aspectY != null && aspectY != -1) {
                    pup.putParams("aspectY", aspectY);
                }
                if (outputX != null && outputX != 0) {
                    pup.putParams("outputX", outputX);
                }
                if (outputY != null && outputY != 0) {
                    pup.putParams("outputY", outputY);
                }
                pup.show();
            }
        });
    }

    public synchronized static String saveFile(String name, byte[] bytes) {
        File file = Util.getMPath(Frame.CONTEXT, "/file/");
        MLog.D("save Builder to:" + file.getPath());
        try {
            String retn = "file:" + file.getPath() + "/" + name;
            FileOutputStream fos = new FileOutputStream(file.getPath() + "/" + name);
            fos.write(bytes);
            fos.close();
            return retn;
        } catch (Exception e) {
            MLog.D(e);
        }
        return null;
    }

    public synchronized static void saveBuilder(String name, Object builder) {
        File file = Util.getMPath(Frame.CONTEXT, "/save/");
        MLog.D("saveBuilder " + name + " " + file.getPath() + "/" + name);
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            Method.protobufSeralizeDes(builder, new FileOutputStream(file.getPath() + "/" + name));
        } catch (Exception e) {
            MLog.D(name, e);
        }
    }

    @SuppressWarnings("unchecked")
    public synchronized static <T> T readBuilder(String name, T builder) {
        File file = Util.getMPath(Frame.CONTEXT, "/save/");
        File readfile = new File(file.getPath() + "/" + name);
        if (LogConfig.getLoglev() == 5) {
            MLog.D("readBuilder " + name + " " + file.getPath() + "/" + name);
        }
        if (!file.exists() || !readfile.exists()) {
            return null;
        }
        try {
            builder = (T) Method.unprotobufSeralizeDes(new FileInputStream(file.getPath() + "/" + name), builder);
        } catch (Exception e) {
            MLog.D(name, e);
        }
        return builder;
    }

    public synchronized static void deleteBuilder(String name) {
        File file = Util.getMPath(Frame.CONTEXT, "/save/" + name);
        if (LogConfig.getLoglev() == 5) {
            MLog.D("name:" + file.getPath());
        }
        try {
            file.delete();
        } catch (Exception e) {
            MLog.D(e);
        }
    }

    public static String UnicodeToString(String str) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{2,4}))");
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            str = str.replace(matcher.group(1), ch + "");
        }
        return str;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (Exception e) {
        }
        return degree;
    }

    /*
     * 旋转图片
     *
     * @param angle
     *
     * @param bitmap
     *
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    @SuppressWarnings("resource")
    public static byte[] getByte(File file) throws Exception {
        byte[] bytes = null;
        if (file != null) {
            InputStream is = new FileInputStream(file);
            int length = (int) file.length();
            if (length > Integer.MAX_VALUE) // 当文件的长度超过了int的最大值
            {
                return null;
            }
            bytes = new byte[length];
            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }
            // 如果得到的字节长度和file实际的长度不一致就可能出错了
            if (offset < bytes.length) {
                return null;
            }
            is.close();
        }
        return bytes;
    }

    /**
     * 根据原图和变长绘制圆形图片
     *
     * @param source
     * @param min
     * @return
     */
    public static Bitmap createCircleImage(Bitmap source, int min) {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(min, min, Config.ARGB_8888);
        /**
         * 产生一个同样大小的画布
         */
        Canvas canvas = new Canvas(target);
        /**
         * 首先绘制圆形
         */

        // paint.setARGB(55, 167, 0, 0);

        canvas.drawCircle(min / 2, min / 2, min / 2, paint);
        /**
         * 使用SRC_IN
         */
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        /**
         * 绘制图片
         */
        canvas.drawBitmap(source, 0, 0, paint);

        return target;
    }

    public static Bitmap createRoundConerImage(Bitmap source, int w, int h, int r) {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        RectF rect = new RectF(0, 0, source.getWidth(), source.getHeight());
        canvas.drawRoundRect(rect, r, r, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }

    // 获取ApiKey
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) {

        }
        return apiKey;
    }


    public static void startActivity(Context context, Class<?> activity) {
        startActivity(context, 0, "", activity, null);
    }

    public static void startActivity(Context context, int flag, Class<?> activity, Map<String, Object> params) {
        startActivity(context, flag, "", activity, params);
    }

    public static void startActivity(Context context, Class<?> fragment, Class<?> activity, Map<String, Object> params) {
        startActivity(context, 0, fragment.getName(), activity, params);
    }

    public static void startActivity(Context context, int flag, Class<?> fragment, Class<?> activity, Map<String, Object> params) {
        startActivity(context, flag, fragment.getName(), activity, params);
    }

    public static void startActivity(Context context, int flag, String fragment, Class<?> activity, Map<String, Object> params) {
        Class<?> useAction = activity;
        Intent i = new Intent(context, useAction);
        i.setFlags(flag);
        if (fragment != null && fragment.length() > 0) {
            i.putExtra("className", fragment);
        }
        if (params != null) {
            for (String key : params.keySet()) {
                Object obj = params.get(key);
                if ((obj instanceof Boolean)) {
                    i.putExtra(key, (Boolean) obj);
                } else if ((obj instanceof Integer)) {
                    i.putExtra(key, (Integer) obj);
                } else if ((obj instanceof Float)) {
                    i.putExtra(key, (Float) obj);
                } else if ((obj instanceof Double)) {
                    i.putExtra(key, (Double) obj);
                } else if ((obj instanceof Long)) {
                    i.putExtra(key, (Long) obj);
                } else if ((obj instanceof String)) {
                    i.putExtra(key, (String) obj);
                } else if ((obj instanceof Serializable)) {
                    i.putExtra(key, (Serializable) obj);
                } else if ((obj instanceof Byte[])) {
                    i.putExtra(key, (Byte[]) obj);
                } else if ((obj instanceof String[])) {
                    i.putExtra(key, (String[]) obj);
                } else if ((obj instanceof Parcelable)) {
                    i.putExtra(key, (Parcelable) obj);
                } /*else if ((obj instanceof GeneratedMessage.Builder)) {
                    try {
                        GeneratedMessage msg = (GeneratedMessage) ((GeneratedMessage.Builder<?>) obj).build();
                        i.putExtra(key, msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } */ else {
                    MLog.D(obj.getClass().getName() + " unsuppt class type");
                }
            }
        }
        context.startActivity(i);
    }


    public static void startActivity(Context context, int flag, Integer resid, Class<?> activity, Object... params) {
        startActivity(context, flag, AutoFitFragment.class.getName(), activity, resid, params);
    }

    public static void startActivity(Context context, Integer resid, Class<?> activity, Object... params) {
        startActivity(context, 0, AutoFitFragment.class.getName(), activity, resid, params);
    }

    public static void startActivity(Context context, int flag, Class<?> fragment, Integer resid, Class<?> activity, Object... params) {
        startActivity(context, flag, fragment.getName(), activity, resid, params);
    }


    public static void startActivity(Context context, Class<?> fragment, Class<?> activity, Object... params) {
        startActivity(context, 0, fragment.getName(), activity, null, params);
    }

    public static void startActivity(Context context, int flag, Class<?> fragment, Class<?> activity, Object... params) {
        startActivity(context, flag, fragment.getName(), activity, null, params);
    }

    public static void startActivity(Context context, int flag, String fragment, Class<?> activity, Integer resid, Object... params) {
        Class<?> useAction = activity;
        Intent i = new Intent(context, useAction);
        i.setFlags(flag);
        i.putExtra("className", fragment);
        if (resid != null && resid != 0) {
            i.putExtra("layout", resid);
        }
        if (params != null) {
            for (int ind = 0; ind < params.length; ind++) {
                String key = params[ind].toString();
                if (params.length > ind + 1) {
                    Object obj = params[(ind + 1)];
                    if ((obj instanceof Boolean)) {
                        i.putExtra(key, (Boolean) obj);
                    } else if ((obj instanceof Integer)) {
                        i.putExtra(key, (Integer) obj);
                    } else if ((obj instanceof Float)) {
                        i.putExtra(key, (Float) obj);
                    } else if ((obj instanceof Double)) {
                        i.putExtra(key, (Double) obj);
                    } else if ((obj instanceof Long)) {
                        i.putExtra(key, (Long) obj);
                    } else if ((obj instanceof String)) {
                        i.putExtra(key, (String) obj);
                    } else if ((obj instanceof Serializable)) {
                        i.putExtra(key, (Serializable) obj);
                    } else if ((obj instanceof Byte[])) {
                        i.putExtra(key, (Byte[]) obj);
                    } else if ((obj instanceof String[])) {
                        i.putExtra(key, (String[]) obj);
                    } else if ((obj instanceof Parcelable)) {
                        i.putExtra(key, (Parcelable) obj);
                    } /*else if ((obj instanceof GeneratedMessage.Builder)) {
                        try {
                            GeneratedMessage msg = (GeneratedMessage) ((GeneratedMessage.Builder<?>) obj).build();
                            i.putExtra(key, msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } */ else {
                        MLog.D(obj.getClass().getName() + " unsuppt class type");
                    }
                }
                ind++;
            }
        }
        context.startActivity(i);
    }

    public static void startActivityForResult(Activity context, int requestCode, Class<?> fragment, Class<?> activity, Map<String, Object> params) {
        startActivityForResult(context, requestCode, fragment.getName(), activity, params);
    }

    public static void startActivityForResult(Activity context, int requestCode, String fragment, Class<?> activity, Map<String, Object> params) {
        Class<?> useAction = activity;
        Intent i = new Intent(context, useAction);
        i.putExtra("className", fragment);
        if (params != null) {
            for (String key : params.keySet()) {
                Object obj = params.get(key);
                if ((obj instanceof Boolean)) {
                    i.putExtra(key, (Boolean) obj);
                } else if ((obj instanceof Integer)) {
                    i.putExtra(key, (Integer) obj);
                } else if ((obj instanceof Float)) {
                    i.putExtra(key, (Float) obj);
                } else if ((obj instanceof Double)) {
                    i.putExtra(key, (Double) obj);
                } else if ((obj instanceof Long)) {
                    i.putExtra(key, (Long) obj);
                } else if ((obj instanceof String)) {
                    i.putExtra(key, (String) obj);
                } else if ((obj instanceof Serializable)) {
                    i.putExtra(key, (Serializable) obj);
                } else if ((obj instanceof Byte[])) {
                    i.putExtra(key, (Byte[]) obj);
                } else if ((obj instanceof String[])) {
                    i.putExtra(key, (String[]) obj);
                } else if ((obj instanceof Parcelable)) {
                    i.putExtra(key, (Parcelable) obj);
                } /*else if ((obj instanceof GeneratedMessage.Builder)) {
                    try {
                        GeneratedMessage msg = (GeneratedMessage) ((GeneratedMessage.Builder<?>) obj).build();
                        i.putExtra(key, msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } */ else {
                    MLog.D(obj.getClass().getName() + " unsuppt class type");
                }
            }
        }
        context.startActivityForResult(i, requestCode);
    }

    public static void startActivityForResult(Activity context, int requestCode, Class<?> activity, Object... params) {
        startActivityForResult(context, requestCode, "", activity, params);
    }

    public static void startActivityForResult(Activity context, int requestCode, Class<?> fragment, Class<?> activity, Object... params) {
        startActivityForResult(context, requestCode, fragment.getName(), activity, params);
    }

    public static void startActivityForResult(Activity context, int requestCode, String fragment, Class<?> activity, Object... params) {
        Class<?> useAction = activity;
        Intent i = new Intent(context, useAction);
        i.putExtra("className", fragment);
        if (params != null) {
            for (int ind = 0; ind < params.length; ind++) {
                String key = params[ind].toString();
                if (params.length > ind + 1) {
                    Object obj = params[(ind + 1)];
                    if ((obj instanceof Boolean)) {
                        i.putExtra(key, (Boolean) obj);
                    } else if ((obj instanceof Integer)) {
                        i.putExtra(key, (Integer) obj);
                    } else if ((obj instanceof Float)) {
                        i.putExtra(key, (Float) obj);
                    } else if ((obj instanceof Double)) {
                        i.putExtra(key, (Double) obj);
                    } else if ((obj instanceof Long)) {
                        i.putExtra(key, (Long) obj);
                    } else if ((obj instanceof String)) {
                        i.putExtra(key, (String) obj);
                    } else if ((obj instanceof Serializable)) {
                        i.putExtra(key, (Serializable) obj);
                    } else if ((obj instanceof Byte[])) {
                        i.putExtra(key, (Byte[]) obj);
                    } else if ((obj instanceof String[])) {
                        i.putExtra(key, (String[]) obj);
                    } else if ((obj instanceof Parcelable)) {
                        i.putExtra(key, (Parcelable) obj);
                    } /*else if ((obj instanceof GeneratedMessage.Builder)) {
                        try {
                            GeneratedMessage msg = (GeneratedMessage) ((GeneratedMessage.Builder<?>) obj).build();
                            i.putExtra(key, msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }*/ else {
                        MLog.D(obj.getClass().getName() + " unsuppt class type");
                    }
                }
                ind++;
            }
        }
        context.startActivityForResult(i, requestCode);
    }

    @SuppressLint("SimpleDateFormat")
    public static String formatDateTime(Calendar cal, String format) {
        if (format == null) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat dateformat1 = new SimpleDateFormat(format);
        String ts = dateformat1.format(new Date(cal.getTimeInMillis()));
        return ts;
    }

    @SuppressWarnings("deprecation")
    public static String getShowApp(Context context) {
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
        return rti.get(0).topActivity.getPackageName();
    }

    public static String getShare(Context context, String id) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(id, "");
    }

    public static void setShare(Context context, String id, String value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putString(id, value);
        editor.commit();
    }

    public static String getShare(Context context, String id, String defaultvalue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(id, defaultvalue);
    }

    public static void closeSoftKey(Context act, View v) {
        InputMethodManager localInputMethodManager = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (localInputMethodManager.isActive()) {
            IBinder localIBinder = v.getWindowToken();
            localInputMethodManager.hideSoftInputFromWindow(localIBinder, 0);
        }
    }

    public static void openSoftKey(Context act, View v) {
        InputMethodManager localInputMethodManager = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (!localInputMethodManager.isActive()) {
            localInputMethodManager.showSoftInput(v, InputMethodManager.SHOW_FORCED);
        }
    }

    public static boolean isRootSystem() {
        if (systemRootState == kSystemRootStateEnable) {
            return true;
        } else if (systemRootState == kSystemRootStateDisable) {
            return false;
        }
        File f = null;
        final String kSuSearchPaths[] = {"/system/bin/", "/system/xbin/", "/system/sbin/", "/sbin/", "/vendor/bin/"};
        try {
            for (int i = 0; i < kSuSearchPaths.length; i++) {
                f = new File(kSuSearchPaths[i] + "su");
                if (f != null && f.exists()) {
                    systemRootState = kSystemRootStateEnable;
                    return true;
                }
            }
        } catch (Exception e) {
        }
        systemRootState = kSystemRootStateDisable;
        return false;
    }


    public static void setValue(final TextView view, final CharSequence value) {
        view.setTag(R.string.anim_change_text, value);
        ViewCompat.setRotationX(view, 0);
        view.setText(value);
    }

    public static void animationChangeValue(final TextView view, final CharSequence value) {
        if (value != null && value.toString().equals(String.valueOf(view.getTag(R.string.anim_change_text)))) {
            return;
        }
        view.setTag(R.string.anim_change_text, value);
        ViewCompat.setRotationX(view, 0);
        ViewCompat.animate(view).rotationX(90).setListener(new ViewPropertyAnimatorListener() {

            @Override
            public void onAnimationStart(View view) {

            }

            @Override
            public void onAnimationEnd(View v) {
                view.setText(value);
                ViewCompat.setRotationX(view, 270);
                ViewCompat.animate(view).rotationX(360).setListener(null).setDuration(200).start();
            }

            @Override
            public void onAnimationCancel(View view) {

            }
        }).setDuration(200).start();
    }


    public static boolean isNotificationEnabled(boolean openset, String... mids) {
        boolean isOpened = true;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationManager notificationManager = (NotificationManager) Frame.CONTEXT.getSystemService(Context.NOTIFICATION_SERVICE);
                if (notificationManager.getImportance() == NotificationManager.IMPORTANCE_NONE) {
                    isOpened = false;
                } else {
                    List<NotificationChannel> channels = notificationManager.getNotificationChannels();
                    for (NotificationChannel channel : channels) {
                        if (channel.getImportance() == NotificationManager.IMPORTANCE_NONE) {
                            if (mids == null || mids.length == 0) {
                                isOpened = false;
                                break;
                            } else {
                                for (String mid : mids) {
                                    if (mid.equals(channel.getId())) {
                                        isOpened = false;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                isOpened = NotificationManagerCompat.from(Frame.CONTEXT).areNotificationsEnabled();
            }
        } catch (Exception e) {
            e.printStackTrace();
            isOpened = false;
        }
        if (!isOpened && openset) {
            Intent intent = new Intent();
            if (Build.VERSION.SDK_INT >= 26) {
                // android 8.0引导
                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                intent.putExtra("android.provider.extra.APP_PACKAGE", Frame.CONTEXT.getPackageName());
            } else if (Build.VERSION.SDK_INT >= 21) {
                // android 5.0-7.0
                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                intent.putExtra("app_package", Frame.CONTEXT.getPackageName());
                intent.putExtra("app_uid", Frame.CONTEXT.getApplicationInfo().uid);
            } else {
                // 其他
                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.setData(Uri.fromParts("package", Frame.CONTEXT.getPackageName(), null));
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Frame.CONTEXT.startActivity(intent);
        }
        return isOpened;
    }
}
