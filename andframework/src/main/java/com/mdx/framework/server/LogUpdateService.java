package com.mdx.framework.server;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.util.Log;

import com.mdx.framework.Frame;
import com.mdx.framework.log.MLog;
import com.mdx.framework.server.api.Son;
import com.mdx.framework.server.api.base.ApiUpdateApi;
import com.mdx.framework.utility.AppManager;

import java.io.File;

public class LogUpdateService extends Service {
    public static long lastcptime = -1;

    public static void startServer(Context context, boolean start) {
        if (lastcptime != -1 && System.currentTimeMillis() - lastcptime < 3600000 && !start) {
            return;
        }

        SharedPreferences sp = context.getSharedPreferences("logupserver_state", Context.MODE_PRIVATE);
        if (lastcptime == -1) {
            lastcptime = sp.getLong("lastuptime", 0);
        }
        if (System.currentTimeMillis() - lastcptime < 3600000 && !start) {
            return;
        }
        Editor editor = sp.edit()
                .putBoolean("server", true)
                .putLong("lastuptime", start ? 0 : System.currentTimeMillis());
        lastcptime = start ? 0 : System.currentTimeMillis();
        editor.commit();
        context.startService(new Intent(context, LogUpdateService.class));
    }

    public static void stopServer(Context context) {
        SharedPreferences sp = context.getSharedPreferences("logupserver_state", Context.MODE_PRIVATE);
        sp.edit().putBoolean("server", false).commit();
        context.stopService(new Intent(context, LogUpdateService.class));
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Frame.init(this);

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    int version = 0;
                    String appid = "default";
                    try {
                        version = AppManager.getApp(LogUpdateService.this, LogUpdateService.this.getPackageName()).versionCode;
                        ApplicationInfo appInfo = LogUpdateService.this.getPackageManager()
                                .getApplicationInfo(LogUpdateService.this.getPackageName(),
                                        PackageManager.GET_META_DATA);
                        appid = appInfo.metaData.getString("UDOWS_APPKEY").trim();
                    } catch (Exception e) {
                    }
                    String error = MLog.readLog();
                    if (error == null || error.length() == 0) {
                        LogUpdateService.stopServer(LogUpdateService.this);
                    }
                    Son son = new ApiUpdateApi().get("MUpdateError",
                            Frame.CONTEXT,
                            "",
                            "",
                            LogUpdateService.this.getPackageName(),
                            version,
                            "android",
                            appid,
                            "0",
                            error).getSon();

                    if (son.getError() == 0) {
                        Log.e("mdx", son.getError() + " up error ok");
                        MLog.initLodfile();
                    }
                    LogUpdateService.stopServer(LogUpdateService.this);
                } catch (Exception e) {
                } finally {
                }
            }
        }).start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.exit(1);
    }

}
