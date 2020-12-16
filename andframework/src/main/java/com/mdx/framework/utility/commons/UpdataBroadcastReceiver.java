package com.mdx.framework.utility.commons;


import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import java.util.List;

/**
 * Created by ryan on 2016/12/24.
 */

public class UpdataBroadcastReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        long myDwonloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

        SharedPreferences sPreferences = context.getSharedPreferences("updatemyself_id", 0);
        long refernece = sPreferences.getLong("download_id", 0);
        if (refernece == myDwonloadID) {
            String serviceString = Context.DOWNLOAD_SERVICE;
            DownloadManager dManager = (DownloadManager) context.getSystemService(serviceString);
            Uri downloadFileUri = dManager.getUriForDownloadedFile(myDwonloadID);
            installApk(downloadFileUri, context);
        }
    }


    public void installApk(Uri downloadFileUri, Context context) {
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");

        List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(install, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            context.grantUriPermission(packageName, downloadFileUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(install);
    }
}
