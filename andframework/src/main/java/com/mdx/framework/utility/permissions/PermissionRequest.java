package com.mdx.framework.utility.permissions;

import android.content.pm.PackageManager;

import com.mdx.framework.Frame;
import com.mdx.framework.utility.Helper;

/**
 * Created by ryan on 2016/8/12.
 */
public abstract class PermissionRequest {

    public void onRequest(int code, String[] permissions, int[] grantResults) {
        boolean retn = true;
        if (code == 1) {
            int i = 0;
            for (int grant : grantResults) {
                onGrant(permissions[i], grantResults[i]);
                i++;
                if (PackageManager.PERMISSION_GRANTED != grant) {
                    retn = false;
                    break;
                }
            }
        }
        if (retn) {
            onGrant(permissions, grantResults);
        } else {
            onUngrant(permissions, grantResults);
        }
    }

    public void onGrant(String permission, int grantResult) {

    }

    public void onGrant(String[] permissions, int[] grantResults) {

    }

    public void onUngrant(String[] permissions, int[] grantResults) {
        Helper.toast("部分权限被拒绝", Frame.CONTEXT);
    }

}
