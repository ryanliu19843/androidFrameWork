package com.mdx.framework.prompt;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.core.content.FileProvider;

import com.mdx.framework.Frame;
import com.mdx.framework.R;
import com.mdx.framework.commons.ParamsManager;
import com.mdx.framework.log.MLog;
import com.mdx.framework.server.api.Son;
import com.mdx.framework.server.api.base.ApiUpdateApi;
import com.mdx.framework.server.api.base.Msg_Key;
import com.mdx.framework.server.api.base.Msg_Update;
import com.mdx.framework.server.file.DownloadFile;
import com.mdx.framework.server.file.FileDwonRead.ProgressListener;
import com.mdx.framework.server.file.NetFile;
import com.mdx.framework.utility.AppManager;
import com.mdx.framework.utility.Helper;

import java.io.File;
import java.util.List;
import java.util.Locale;

public class SelfUpdateDialog extends MDialog {
    private AlertDialog.Builder builder;

    private AlertDialog dialog;

    private NetFile net;

    private String url = "";

    private DownloadFile down;

    private setProcess setp = new setProcess();

    private Handler handle = new Handler();

    private NotificationManager updateNotificationManager = null;

    private Notification.Builder updateNotification = null;

    private PendingIntent pendingIntent;

    private boolean isupdateing = false;

    private Msg_Update update;

    public SelfUpdateDialog(Context context) {
        super(context);
        builder = new AlertDialog.Builder(context);
        this.setCancelable(false);
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
    }

    private void setNetFile(Msg_Update update) {
        down = new DownloadFile(this.getContext(), false, 1);
        if (update != null) {
            url = update.url;
            String packagename = getContext().getPackageName();
            net = new NetFile(packagename, url, packagename, packagename, "0", packagename, new ProgressListener() {
                private static final long serialVersionUID = 1L;

                public void onProgress(long now, long all, int type) {
                    handle.post(setp);
                }
            });
            down.download(net);
        }
    }

    public class setProcess implements Runnable {
        @SuppressLint("NewApi")
        public void run() {
            if (updateNotification != null) {
                if (net.file.downstate == 4) {
                    try {
                        Intent installIntent = new Intent(Intent.ACTION_VIEW);
                        if (Build.VERSION.SDK_INT >= 24) {
                            Uri contentUri = FileProvider.getUriForFile(getContext(), getContext().getPackageName() + ".fileprovider", new File(net.getApk().getPath()));
                            installIntent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                            List<ResolveInfo> resInfoList = getContext().getPackageManager().queryIntentActivities(installIntent, PackageManager.MATCH_DEFAULT_ONLY);
                            for (ResolveInfo resolveInfo : resInfoList) {
                                String packageName = resolveInfo.activityInfo.packageName;
                                getContext().grantUriPermission(packageName, contentUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            }
                        } else {
                            Uri uri = Uri.fromFile(new File(net.getApk().getPath()));
                        installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
                    }
                    PendingIntent updatePendingIntent = PendingIntent.getActivity(getContext(), 0, installIntent, 0);
                    isupdateing = false;
                    updateNotification = new Notification.Builder(getContext()).setContentTitle(getContext().getString(R.string.update_startdownload))
                            .setContentText(Frame.CONTEXT.getString(R.string.update_downloaded)).setSmallIcon(getContext().getApplicationInfo().icon).setContentIntent(updatePendingIntent)
                            .setDefaults(Notification.DEFAULT_SOUND).setAutoCancel(true);
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                            updateNotificationManager.notify(0, updateNotification.build());
                        } else {
                            updateNotificationManager.notify(0, updateNotification.getNotification());
                        }
//                        if (update.flag == 1) {
                            handle.postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    updateNotificationManager.cancelAll();
                                }
                            }, 2000);
                            AppManager.install(getContext(), net.getApk().getPath());
//                        }
                    } catch (Exception e) {
                        MLog.D(e);
                    }
                } else {
                    int persint = (int) (net.file.nlength * 1d / net.file.length * 100);

                    updateNotification = new Notification.Builder(getContext()).setContentTitle(getContext().getString(R.string.update_startdownload)).setContentText(persint + "%")
                            .setSmallIcon(getContext().getApplicationInfo().icon).setContentIntent(pendingIntent);
                }
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                    updateNotificationManager.notify(0, updateNotification.build());
                } else {
                    updateNotificationManager.notify(0, updateNotification.getNotification());
                }
            }
        }
    }

    public void DataLoad(int[] types) {
        int version = 0;
        String appid = "default";
        try {
            version = AppManager.getApp(getContext(), getContext().getPackageName()).versionCode;
            ApplicationInfo appInfo = getContext().getPackageManager().getApplicationInfo(getContext().getPackageName(), PackageManager.GET_META_DATA);
            appid = appInfo.metaData.getString("UDOWS_APPKEY").trim();
        } catch (Exception e) {
        }
        ApiUpdateApi apiupdate = new ApiUpdateApi();

        if (types != null) {
            apiupdate.setSonId("0");
        } else {
            apiupdate.setSonId("1");
        }
        SharedPreferences sup = getContext().getSharedPreferences("default_update_param", Context.MODE_PRIVATE);
        String source = sup.getInt("updatesource", -2) == -2 ? null : (sup.getInt("updatesource", -2) + "");
        apiupdate.load("APP_UPDATE_SELF", getContext(), this, "disposeMessage", getContext().getPackageName(), version, "android", appid, source, "0");
    }

    public void Update(boolean isShowLoad) {
        this.LoadingShow = isShowLoad;
        DataLoad(null);
    }

    public void disposeMessage(Son son) throws Exception {
        if (son.getError() == 0) {
            Msg_Update bu = (Msg_Update) son.getBuild();
            this.update = bu;
            SharedPreferences sup = getContext().getSharedPreferences("default_update_param", Context.MODE_PRIVATE);
            if (bu.sourceUPdate == 1) {
                Editor editor = sup.edit();
                editor.putInt("updatesource", bu.source);
                editor.commit();
            } else {
                if (sup.getInt("updatesource", -2) == -2) {
                    Editor editor = sup.edit();
                    editor.putInt("updatesource", bu.source);
                    editor.commit();
                }
            }

            if (son.getSonId().equals("0")) {
                SharedPreferences sp = getContext().getSharedPreferences("default_param", Context.MODE_PRIVATE);
                Editor editor = sp.edit();
                for (Msg_Key msg : update.keys) {
                    editor.putString(msg.code, msg.value);
                    ParamsManager.put(msg.code.toLowerCase(Locale.ENGLISH), msg.value);
                }
                editor.commit();
                return;
            }
            if (this.update.state == 1) {
                String packagename = getContext().getPackageName();
                final NetFile net = new NetFile(packagename, update.url, packagename, packagename, "0", packagename, null);
                File file=net.getApk();
                if(file.exists()){
                    if (isupdateing) {
                        builder.setMessage(getContext().getString(R.string.update_downloading));
                        if (update.flag != 0) {
                            builder.setMessage(getContext().getString(R.string.update_waitfinish));
                        } else {
                            return;
                        }
                    } else {
                        builder.setMessage(getContext().getString(R.string.update_readyinsall));
                    }

                    if (update.flag == 0) {
                        builder.setPositiveButton(getContext().getString(R.string.cancle), new OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                    } else {
                        builder.setCancelable(false);
                    }
                    builder.setNegativeButton(getContext().getString(R.string.submit), new OnClickListener() {
                        @SuppressLint("NewApi")
                        @SuppressWarnings("deprecation")
                        public void onClick(DialogInterface dialog, int which) {
                            if (isupdateing) {
                                Frame.HANDLES.closeAll();
                                return;
                            }
                            AppManager.install(getContext(), net.getApk().getPath());
                        }
                    });
                }else{
                    if (isupdateing) {
                        builder.setMessage(getContext().getString(R.string.update_downloading));
                        if (update.flag != 0) {
                            builder.setMessage(getContext().getString(R.string.update_waitfinish));
                        } else {
                            return;
                        }
                    } else {
                        builder.setMessage(getContext().getString(R.string.update_willdownload));
                    }

                    if (update.flag == 0) {
                        builder.setPositiveButton(getContext().getString(R.string.cancle), new OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                    } else {
                        builder.setCancelable(false);
                    }
                    builder.setNegativeButton(getContext().getString(R.string.submit), new OnClickListener() {
                        @SuppressLint("NewApi")
                        @SuppressWarnings("deprecation")
                        public void onClick(DialogInterface dialog, int which) {
                            if (isupdateing) {
                                Frame.HANDLES.closeAll();
                                return;
                            }
                            updateNotificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                            updateNotification = new Notification.Builder(getContext()).setContentTitle(getContext().getString(R.string.update_startdownload)).setContentText("0%")
                                    .setSmallIcon(getContext().getApplicationInfo().icon).setContentIntent(pendingIntent);

                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                                updateNotificationManager.notify(0, updateNotification.build());
                            } else {
                                updateNotificationManager.notify(0, updateNotification.getNotification());
                            }
                            setNetFile(update);
                            dialog.dismiss();
                            isupdateing = true;
                            if (update.flag == 1) {
                                Frame.HANDLES.closeAll();
                            }
                        }
                    });
                }
                show();
            } else {
                if (LoadingShow) {
                    Helper.toast(getContext().getString(R.string.update_newest), getContext());
                }
            }
        }
    }

    public void show() {
        dialog = builder.create();

        dialog.show();
    }

    @Override
    protected void create(Bundle savedInstanceState) {
        // TODO Auto-generated method stub

    }
}
