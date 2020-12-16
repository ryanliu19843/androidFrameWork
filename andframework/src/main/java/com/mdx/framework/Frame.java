package com.mdx.framework;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.mdx.framework.cache.DataStoreCacheManage;
import com.mdx.framework.cache.FileStoreCacheManage;
import com.mdx.framework.cache.ImageCache;
import com.mdx.framework.cache.ImageStoreCacheManage;
import com.mdx.framework.commons.OnResumeListener;
import com.mdx.framework.commons.ParamsManager;
import com.mdx.framework.commons.verify.Md5;
import com.mdx.framework.config.InitConfig;
import com.mdx.framework.config.LogConfig;
import com.mdx.framework.log.MLog;
import com.mdx.framework.prompt.SelfUpdateDialog;
import com.mdx.framework.server.LogUpdateService;
import com.mdx.framework.server.ServerParamsInit;
import com.mdx.framework.server.api.Son;
import com.mdx.framework.server.file.FileLoad;
import com.mdx.framework.server.image.ImageLoad;
import com.mdx.framework.utility.Helper;
import com.mdx.framework.utility.Util;
import com.mdx.framework.utility.application.App;
import com.mdx.framework.utility.handle.Handles;
import com.mdx.framework.utility.permissions.PermissionsHelper;

import java.io.File;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.HashMap;

public class Frame {
    /**
     * 全局上下文
     */
    public static Context CONTEXT;
    private static ArrayList<Activity> NowShowActivity = new ArrayList<>();
    public static OnResumeListener onResumeListener;
    /**
     * 全局句柄
     */
    public static Handles HANDLES = new Handles();

    public static String NprocessName = "";

    /**
     * 全局图片缓存
     */
    public static ImageCache IMAGECACHE = new ImageCache();

    /**
     * 全局图片加载器
     */
    public static ImageLoad IMAGELOAD = new ImageLoad(IMAGECACHE);

    public static FileLoad FILELOAD = new FileLoad();

    /**
     * dip和px的转换率
     */

    public static int AnimationDurationMillis = 150;
    public static int AnimationDelayMillis = 10;
    public static float DENSITY = 1.5f;

    public static String INITLOCK = "";

    private static OnFinish OnFinishListener;
    private static OnApiLoad onApiLoadListener;

    public static File LOGFILE;

    public static void finish() {
        HANDLES.closeAll();
        if (OnFinishListener != null) {
            OnFinishListener.onFinish(Frame.CONTEXT);
        }
    }

    public static Runnable ONINIT;

    public static OnFinish getOnFinishListener() {
        return OnFinishListener;
    }

    public static void setOnFinishListener(OnFinish onFinishListener) {
        OnFinishListener = onFinishListener;
    }

    public static void setOnApiLoadListener(OnApiLoad onApiLoad) {
        onApiLoadListener = onApiLoad;
    }

    public static OnApiLoad getOnApiLoadListener() {
        return onApiLoadListener;
    }

    public interface OnFinish {
        public void onFinish(Context context);
    }


    public interface OnApiLoad {
        public void onApiLoad(Context context, Object parent, Son son);
    }


    public static void setNowShowActivity(Activity activity) {
        NowShowActivity.add(0, activity);
        PermissionsHelper.onActivityLoaded(getNowShowActivity());
    }

    public static void removNowShowActivity(Activity activity) {
        NowShowActivity.remove(activity);
    }

    public static Activity getNowShowActivity() {
        if (NowShowActivity.size() == 0) {
            return null;
        }
        return NowShowActivity.get(0);
    }


    /**
     * 框架初始化必须在程序运行是先调用
     *
     * @param context 上下文,必须是ApplicationContext
     * @throws
     * @author ryan
     * @Title: init
     * @Description: TODO 框架初始化必须在程序运行是先调用
     */
    public static void init(final Context context) {
        CONTEXT = context.getApplicationContext();
        LOGFILE = Util.getPath(context, "log", "runlog.log");
        final SelfUpdateDialog sud = new SelfUpdateDialog(Frame.CONTEXT);

        final ServerParamsInit serverParamsInit = new ServerParamsInit();

        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) Frame.CONTEXT.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                Frame.NprocessName = appProcess.processName;
            }
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (INITLOCK) {
                    try {
                        InitConfig.initConfig();
                        ParamsManager.put(ParamsManager.PARAM_APP_PACKAGE, CONTEXT.getPackageName());
                        int version = App.getApp(CONTEXT, CONTEXT.getPackageName()).getVersion();
                        ParamsManager.put(ParamsManager.PARAM_APP_VERSION, version + "");
                        ParamsManager.init();
                        if (LogConfig.getLoglev() == 5) {
                            MLog.D("paramsmanager", "" + ParamsManager.PARAM_HASHMAP.size());
                        }
                    } catch (Exception e) {
                    }
                }

                try {
                    DENSITY = Frame.CONTEXT.getResources().getDisplayMetrics().density;
                    ParamsManager.put(ParamsManager.PARAM_APP_PACKAGEMD5, Md5.md5(CONTEXT.getPackageName()));
                    DataStoreCacheManage.FILEMANAGER.TEMPFILES = Helper.readBuilder(DataStoreCacheManage.FILEDATASTR, DataStoreCacheManage.FILEMANAGER.TEMPFILES);
                    FileStoreCacheManage.FILEMANAGER.TEMPFILES = Helper.readBuilder(FileStoreCacheManage.FILEDATASTR, FileStoreCacheManage.FILEMANAGER.TEMPFILES);
                    ImageStoreCacheManage.FILEMANAGER.TEMPFILES = Helper.readBuilder(ImageStoreCacheManage.FILEDATASTR, ImageStoreCacheManage.FILEMANAGER.TEMPFILES);
                    if (!NprocessName.endsWith(":com.mdx.framework.process")) {
//                        sud.DataLoad(new int[]{0});
                        serverParamsInit.DataLoad(CONTEXT);
                    }
                } catch (Exception e) {
                    MLog.D(e);
                }
                LOGFILE = Util.getPath(Frame.CONTEXT, "log", "runlog.log");
                MLog.initLodfile();
                if (ONINIT != null) {
                    ONINIT.run();
                }

            }
        }).start();

        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                MLog.E("*********uncaughtException***************", "**********end uncaughtException***********", ex);
                MLog.writenow();
                ParamsManager.savebuild();
                try {
                    while (MLog.ThreadPool.getWatrun().size() > 0) {
                        Thread.sleep(100);
                    }
                    LogUpdateService.startServer(Frame.CONTEXT, true);
                } catch (Exception e) {
                }
                System.exit(0);
            }
        });
    }

    public static String getAppkey() {
        ApplicationInfo appInfo;
        try {
            appInfo = Frame.CONTEXT.getPackageManager().getApplicationInfo(Frame.CONTEXT.getPackageName(), PackageManager.GET_META_DATA);
            return appInfo.metaData.getString("UDOWS_APPKEY").trim();
        } catch (NameNotFoundException e) {
        }
        return "";
    }

    public static void UpdateSelf(Context context, boolean isShowload) {
        SelfUpdateDialog sud = new SelfUpdateDialog(context);
        sud.Update(isShowload);
    }
}
