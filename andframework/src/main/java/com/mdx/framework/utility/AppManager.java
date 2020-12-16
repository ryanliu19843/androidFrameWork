package com.mdx.framework.utility;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.core.content.FileProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AppManager {
    private static ActivityManager mActivityManager;

    /**
     * 获得属于桌面的应用的应用包名称
     *
     * @return 返回包含所有包名的字符串列表
     */
    public static List<String> getHomes(Context context) {
        List<String> names = new ArrayList<String>();
        PackageManager packageManager = context.getPackageManager();
        // 属性
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo ri : resolveInfo) {
            names.add(ri.activityInfo.packageName);
        }
        return names;
    }

    /**
     * 删除应用
     *
     * @param context
     * @param packag
     */
    public static void deleteApp(Context context, String packag) {
        Uri packageURI = Uri.parse("package:" + packag);
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
        uninstallIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(uninstallIntent);
    }

    /**
     * 安装应用
     *
     * @param context
     * @param path    apk位置
     */
    public static void install(Context context, String path) {
        Intent install = new Intent(Intent.ACTION_VIEW);

        if (Build.VERSION.SDK_INT >= 24) {
            Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", new File(path));
            install.setDataAndType(contentUri, "application/vnd.android.package-archive");

            List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(install, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                context.grantUriPermission(packageName, contentUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } else {
            Uri uri = Uri.fromFile(new File(path));
            install.setDataAndType(uri, "application/vnd.android.package-archive");
        }
        context.startActivity(install);
    }

    /**
     * 打开某个app
     *
     * @param context
     * @param packag
     */
    public static void open(Context context, String packag) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packag);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 获取app图片
     *
     * @param context
     * @param packageName
     * @return
     * @throws NameNotFoundException
     */
    public static Drawable getIcon(Context context, String packageName) throws NameNotFoundException {
        PackageInfo pack = context.getPackageManager().getPackageInfo(packageName, 0);
        ApplicationInfo app = pack.applicationInfo;
        return app.loadIcon(context.getPackageManager());
    }

    /**
     * 打开app的应用信息
     *
     * @param context
     * @param packageName
     * @return
     * @throws NameNotFoundException
     */
    @SuppressLint("InlinedApi")
    public static void openAppInfo(Context context, String packageName) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", packageName, null);
        intent.setData(uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 获取app信息
     *
     * @param context
     * @param packageName
     * @return
     * @throws NameNotFoundException
     */
    public static PackageInfo getApp(Context context, String packageName) throws NameNotFoundException {
        PackageInfo pack = context.getPackageManager().getPackageInfo(packageName, 0);
        return pack;
    }

    /**
     * 获取我的app的process;
     *
     * @param context
     * @return
     * @throws
     * @author ryan
     * @Title: getCurProcess
     * @Description: TODO
     */
    public static RunningAppProcessInfo getCurProcess(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess;
            }
        }
        return null;
    }

    /**
     * 获取全部应用
     *
     * @param context
     * @return
     */
    public static List<PackageInfo> getAppList(Context context) {
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        List<PackageInfo> list = new ArrayList<PackageInfo>();
        for (PackageInfo packinfo : packages) {
            ApplicationInfo app = packinfo.applicationInfo;
            if ((app.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                list.add(packinfo);
            }
        }
        return list;
    }

    /**
     * 获取运行的activity列表
     *
     * @param context
     * @param maxnum
     * @return
     */
    public List<RunningTaskInfo> getRuningTasks(Context context, int maxnum) {
        if (mActivityManager == null) {
            mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        }
        @SuppressWarnings("deprecation")
        List<RunningTaskInfo> rti = mActivityManager.getRunningTasks(maxnum);
        return rti;
    }

    /**
     * 获取当前显示应用
     *
     * @param context
     * @return
     */
    public RunningTaskInfo getRunningTask(Context context) {
        List<RunningTaskInfo> list = getRuningTasks(context, 1);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
}
