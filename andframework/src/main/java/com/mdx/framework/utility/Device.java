package com.mdx.framework.utility;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera.Size;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import com.mdx.framework.Frame;
import com.mdx.framework.commons.verify.Md5;
import com.mdx.framework.log.MLog;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@SuppressWarnings("deprecation")
public class Device {
    /**
     * 统计appkey 和渠道
     */

    public static boolean navigationbarEnable = false, statusBarEnable = false;
    public static int navigationbarh = 0, statush = 0, actionbarh = 0;

    public static String LOGID = UUID.randomUUID().toString();

    private static String IMSI, PROVIDERSNAME, MACADDRESS, NETWORKTYPE, NETWORKSUBTYPE, NETWORKSUBNAME, METICS_S;

    private static int NETWORKSPEED = 0, TOUCHSLOP = 10;

    private static long LASTGETNETWORK = 0;

    private static int METICS_W = 720, METICS_H = 1280;

    private static float SCREEN_W = 360, SCREEN_H = 640;

    private static float DENSITY = 2;

    private static String ID;

    private static long versioncode;
    private static String versionname = null;

    /**
     * 获取屏幕宽度 单位px
     *
     * @return
     * @throws
     * @author ryan
     * @Title: getMeticsW
     * @Description: TODO
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static synchronized int getMeticsW() {
        Context context = Frame.CONTEXT;
        if (METICS_S == null) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display dis = wm.getDefaultDisplay();
            if (getSdkVersion() > Build.VERSION_CODES.JELLY_BEAN_MR1) {
                DisplayMetrics sdm = new DisplayMetrics();
                dis.getRealMetrics(sdm);
                METICS_W = sdm.widthPixels;
                METICS_H = sdm.heightPixels;
                DENSITY = sdm.density;
                METICS_S = "";
            } else {
                DisplayMetrics dm = new DisplayMetrics();
                dis.getMetrics(dm);
                METICS_W = dm.widthPixels;
                METICS_H = dm.heightPixels;
                DENSITY = dm.density;
                METICS_S = "";
            }
            SCREEN_W = METICS_W / DENSITY;
            SCREEN_H = METICS_H / DENSITY;
        }
        ViewConfiguration configuration = ViewConfiguration.get(context);
        TOUCHSLOP = configuration.getScaledTouchSlop();
        return METICS_W;
    }

    /**
     * 获取屏幕高度 单位px
     *
     * @return
     * @throws
     * @author ryan
     * @Title: getMeticsH
     * @Description: TODO
     */
    public synchronized static int getMeticsH() {
        getMeticsW();
        return METICS_H;
    }

    public synchronized static float getDensity() {
        getMeticsW();
        return DENSITY;
    }

    /**
     * 设备MODEL
     *
     * @return
     */
    public synchronized static String getModel() {
        return Build.MODEL;
    }

    public static String getId() {
        if (ID == null) {
            StringBuffer sb = new StringBuffer();
            String ANDROID_ID = Settings.System.getString(Frame.CONTEXT.getContentResolver(), Settings.System.ANDROID_ID);
            SharedPreferences sup = Frame.CONTEXT.getSharedPreferences("default_device_id", Context.MODE_PRIVATE);
            String deviceid = sup.getString("deviceid", null);
            if (deviceid == null) {
                deviceid = UUID.randomUUID().toString();
                sup.edit().putString("deviceid", deviceid).commit();
            }
            sb.append("&&");
            sb.append(ANDROID_ID);
            sb.append("&&");
            sb.append(deviceid);
            sb.append("&&");
            sb.append(android.os.Build.SERIAL);
            sb.append("&&");
            sb.append(getImsi());
            try {
                ID = Md5.md5(sb.toString());
                return ID;
            } catch (Exception e) {
                return deviceid;
            }
        }
        return ID;
    }

    /**
     * 供应商
     *
     * @return
     */
    public synchronized static String getBrand() {
        return Build.BRAND;
    }

    /**
     * 设备名称
     *
     * @return
     */
    public synchronized static String getDevice() {
        return Build.DEVICE;
    }

    /**
     * 制造商
     *
     * @return
     */
    public synchronized static String getFacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * sdk版本
     *
     * @return
     */
    public synchronized static String getReleaseVersion() {
        String version = Build.VERSION.RELEASE;
        if (version.toUpperCase(Locale.ENGLISH).indexOf("ANDROID") < 0) {
            version = "android " + version;
        }
        return version;
    }

    public synchronized static String apkVersionName() {
        if (versionname == null) {
            PackageManager packageManager = Frame.CONTEXT.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = null;
            try {
                packInfo = packageManager.getPackageInfo(Frame.CONTEXT.getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            versionname = packInfo.versionName;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                versioncode = packInfo.getLongVersionCode();
            } else {
                versioncode = packInfo.versionCode;
            }
        }
        return versionname;
    }


    @RequiresApi(api = Build.VERSION_CODES.P)
    public synchronized static long apkVersion() {
        apkVersionName();
        return versioncode;
    }

    public synchronized static int getSdkVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取网络类型
     *
     * @param context
     * @return
     */
    public synchronized static String getNetWorkSubName(Context context) {
        getNetworkType();
        return NETWORKSUBNAME;
    }

    /**
     * 获取网络类型描述
     *
     * @return
     */
    public synchronized static String getNetWorkSubType() {
        getNetworkType();
        return NETWORKSUBTYPE;
    }

    /**
     * 获取手机串号
     *
     * @return
     */
    @SuppressLint("MissingPermission")
    public synchronized static String getImsi() {
        Context context = Frame.CONTEXT;
        try {
            if (IMSI == null) {
                TelephonyManager telephonemanage = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                IMSI = telephonemanage.getDeviceId();
                PROVIDERSNAME = telephonemanage.getNetworkOperatorName();
            }
        } catch (Exception e) {
            PROVIDERSNAME = "";
            return "";
        }
        return IMSI;
    }

    /**
     * 获取运营商
     *
     * @return
     */
    public synchronized static String getProvidersName() {
        getImsi();
        return PROVIDERSNAME;
    }

    /**
     * 获取mac地址
     *
     * @return
     */
    public synchronized static String getMacAddress() {
        Context context = Frame.CONTEXT;
        try {
            if (MACADDRESS == null) {
                WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo info = wifi.getConnectionInfo();
                MACADDRESS = info.getMacAddress();
            }
        } catch (Exception e) {
            return "";
        }
        return MACADDRESS;
    }

    /**
     * 获取网络类型
     *
     * @return
     */
    public synchronized static String getNetworkType() {
        if (System.currentTimeMillis() - LASTGETNETWORK > 50) {
            LASTGETNETWORK = System.currentTimeMillis();
        } else {
            return NETWORKTYPE;
        }
        Context context = Frame.CONTEXT;
        try {
            ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = conMan.getActiveNetworkInfo();
            if (info == null) {
                NETWORKTYPE = "NONE";
                NETWORKSUBNAME = "NONE";
                NETWORKSUBTYPE = "NONE";
                NETWORKSPEED = 0;
                return NETWORKTYPE;
            }
            switch (info.getType()) {
                case ConnectivityManager.TYPE_MOBILE:
                    NETWORKTYPE = "MOBILE";
                    NETWORKSUBTYPE = info.getSubtypeName();
                    switch (info.getSubtype()) {
                        case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                            NETWORKSUBNAME = "unknown";
                            NETWORKSPEED = 1;
                            break;
                        case TelephonyManager.NETWORK_TYPE_1xRTT:
                            NETWORKSUBNAME = "2G 1xRTT";
                            NETWORKSPEED = 2;
                            break;
                        case TelephonyManager.NETWORK_TYPE_CDMA:
                            NETWORKSUBNAME = "2G CDMA";
                            NETWORKSPEED = 2;
                            break;
                        case TelephonyManager.NETWORK_TYPE_EDGE:
                            NETWORKSUBNAME = "2G EDGE";
                            NETWORKSPEED = 2;
                            break;
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                            NETWORKSUBNAME = "3G EVDO_0";
                            NETWORKSPEED = 3;
                            break;
                        case TelephonyManager.NETWORK_TYPE_EVDO_A:
                            NETWORKSUBNAME = "3G EVDO_A";
                            NETWORKSPEED = 3;
                            break;
                        case TelephonyManager.NETWORK_TYPE_GPRS:
                            NETWORKSUBNAME = "2G GPRS";
                            NETWORKSPEED = 2;
                            break;
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                            NETWORKSUBNAME = "HSDPA";
                            NETWORKSPEED = 3;
                            break;
                        case TelephonyManager.NETWORK_TYPE_HSPA:
                            NETWORKSUBNAME = "HSPA";
                            NETWORKSPEED = 2;
                            break;
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                            NETWORKSUBNAME = "HSUPA";
                            NETWORKSPEED = 2;
                            break;
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                            NETWORKSUBNAME = "UMTS";
                            NETWORKSPEED = 3;
                            break;
                        default:
                            NETWORKSUBNAME = "UMTS";
                            NETWORKSPEED = 3;
                            break;
                    }
                    break;
                case ConnectivityManager.TYPE_WIFI:
                    NETWORKTYPE = "WIFI";
                    NETWORKSUBNAME = "WIFI";
                    NETWORKSUBTYPE = "WIFI";
                    NETWORKSPEED = 10;
                    break;
                default:
                    NETWORKTYPE = "NONE";
                    NETWORKSUBNAME = "NONE";
                    NETWORKSUBTYPE = "NONE";
                    NETWORKSPEED = 0;
                    break;
            }
        } catch (Exception e) {
            NETWORKTYPE = "";
            NETWORKSUBNAME = "";
            NETWORKSUBTYPE = "";
            NETWORKSPEED = 1;
        }
        return NETWORKTYPE;
    }

    public static Size getNearSize(List<Size> sizes, int nw, int nh) {
        Size retn = null;
        double cs = 0.0D;
        for (Size s : sizes) {
            if (retn == null) {
                cs = s.width / nw;
                retn = s;
            } else if (s.width > nw && cs > (double) (s.width / nw)) {
                cs = s.width / nw;
                retn = s;
            }
        }
        return retn;
    }

    public synchronized static int getNetWorkSpeed() {
        getNetworkType();
        if (NETWORKSPEED == 0) {
            MLog.D(MLog.SYS_RUN, "end:" + NETWORKSPEED);
        }
        return NETWORKSPEED;
    }

    public synchronized static long getRxBytes() {
        Context context = Frame.CONTEXT;
        int uid = context.getApplicationInfo().uid;
        long rxbytes = 0;
        try {
            Class<?> clazz = Class.forName("android.net.TrafficStats");

            Method method = clazz.getMethod("getUidRxBytes", int.class);
            rxbytes = (Long) method.invoke(clazz, uid);
        } catch (Exception e) {
            rxbytes = -2;
        }
        return rxbytes;
    }

    public synchronized static long getTxBytes() {
        Context context = Frame.CONTEXT;
        int uid = context.getApplicationInfo().uid;
        long txbytes = 0;
        try {
            Class<?> clazz = Class.forName("android.net.TrafficStats");

            Method method = clazz.getMethod("getUidTxBytes", int.class);
            txbytes = (Long) method.invoke(clazz, uid);
        } catch (Exception e) {
            txbytes = -2;
        }
        return txbytes;
    }

    public static int getTouchSlop() {
        return TOUCHSLOP;
    }

    public static void sendSms(String phone_number, String sms_content) {
        SmsManager smsManager = SmsManager.getDefault();
        if (sms_content.length() > 70) {
            ArrayList<String> contents = smsManager.divideMessage(sms_content);
            smsManager.sendMultipartTextMessage(phone_number, null, contents, null, null);
        } else {
            smsManager.sendTextMessage(phone_number, null, sms_content, null, null);
        }
    }

    public static void setTouchSlop(int tOUCHSLOP) {
        TOUCHSLOP = tOUCHSLOP;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static int getSimSize() {
        TelecomManager telecomManager = (TelecomManager) Frame.CONTEXT.getSystemService(Context.TELECOM_SERVICE);
        if (telecomManager != null) {
            List<PhoneAccountHandle> phoneAccountHandleList = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(Frame.CONTEXT, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return 0;
                }
                phoneAccountHandleList = telecomManager.getCallCapablePhoneAccounts();
                return phoneAccountHandleList.size();
            }
        }
        return 0;
    }


    public static String getDeviceName(Context context) {
        String name = null;
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null) {
            name = adapter.getName();
        }

        if (name == null) {
            name = Settings.Secure.getString(context.getContentResolver(), "bluetooth_name");
        }
        if (name == null) {
            name = Build.MANUFACTURER + " " + Build.MODEL;
        }
        return name;
    }

    public static float getScreenW() {
        getMeticsW();
        return SCREEN_W;
    }


    public static float getScreenH() {
        getMeticsW();
        return SCREEN_H;
    }

}
