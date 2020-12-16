package com.mdx.framework.utility.permissions;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.content.ContextCompat;

import com.mdx.framework.Frame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ryan on 2016/8/15.
 */
public class PermissionsHelper {

    public static HashMap<Integer, PermissionRequest> permissionRequestHashMap = new HashMap<>();
    public static ArrayList<PermissionRun> permissionRuns = new ArrayList<>();
    public static int nowMaxCode = 10;

    public static class PermissionRun {
        public String[] permissions;
        public PermissionRequest runnable;

        public PermissionRun(String[] permissions, PermissionRequest runnable) {
            this.permissions = permissions;
            this.runnable = runnable;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static synchronized void requestPermissions(String[] permissions, PermissionRequest runnable) {
        Activity activity = Frame.getNowShowActivity();
        if (activity == null) {
            permissionRuns.add(new PermissionRun(permissions, runnable));
            return;
        }
        List<String> noPromissions = new ArrayList<>();
        int[] grantresults = new int[permissions.length];
        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(activity, permission);
            grantresults[i] = checkCallPhonePermission;
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                noPromissions.add(permission);
            }
        }
        if (noPromissions.size() == 0) {
            if (runnable != null) {
                runnable.onRequest(0, permissions, grantresults);
            }
        } else if (Build.VERSION.SDK_INT >= 23) {
            int requestCode = nowMaxCode;
            nowMaxCode++;
            permissionRequestHashMap.put(requestCode, runnable);
            activity.requestPermissions(permissions, requestCode);
        } else {
            if (runnable != null) {
                runnable.onRequest(0, permissions, grantresults);
            }
        }
    }


    public static synchronized void onRequestPermissions(int requestCode, String[] permissions, int[] grantResults) {
        PermissionRequest permissionRequest = PermissionsHelper.permissionRequestHashMap.remove(requestCode);
        if (permissionRequest != null) {
            permissionRequest.onRequest(1, permissions, grantResults);
        }
    }

    public static synchronized void onActivityLoaded(Activity activity) {
        while (permissionRuns.size() > 0) {
            PermissionRun pr = permissionRuns.remove(0);
            requestPermissions(pr.permissions, pr.runnable);
        }
    }
}
