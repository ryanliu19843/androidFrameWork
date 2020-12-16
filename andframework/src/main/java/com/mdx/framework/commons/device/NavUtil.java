package com.mdx.framework.commons.device;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.mdx.framework.widget.systembartint.SystemBarTintManager;

public class NavUtil {
    protected SystemBarTintManager tintManager;
    public Activity activity;

    public NavUtil(Activity activity) {
        this.activity = activity;
        tintManager = new SystemBarTintManager(activity);
    }


    public boolean hasNavigationBar() {
        boolean retn = false;
        int var2;
        Resources var4;
        if ((var2 = (var4 = activity.getResources()).getIdentifier("config_showNavigationBar", "bool", "android")) > 0) {
            retn = var4.getBoolean(var2);
        }

        try {
            Class var5;
            String var6 = (String) (var5 = Class.forName("android.os.SystemProperties")).getMethod("get", new Class[]{String.class}).invoke(var5, new Object[]{"qemu.hw.mainkeys"});
            if ("1".equals(var6)) {
                retn = false;
            } else if ("0".equals(var6)) {
                retn = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return retn;
    }

    public boolean isNavigationBarShowing() {
        if (!hasNavigationBar()) {
            return false;
        } else {
            if (Build.VERSION.SDK_INT >= 17) {
                String var1;
                if (RomUtil.isMiui()) {
                    var1 = "force_fsg_nav_bar";
                    if (Settings.Global.getInt(activity.getContentResolver(), var1, 0) != 0) {
                        return false;
                    }

                    return true;
                }

                if (RomUtil.isVivo()) {
                    var1 = "navigation_gesture_on";
                    if (Settings.Secure.getInt(activity.getContentResolver(), var1, 0) != 0) {
                        return true;
                    }

                    return false;
                }
            }
            return true;
        }
    }

    public int getPixelInsetBottom() {
        return tintManager.getConfig().getPixelInsetBottom();
    }

    public int getPixelInsetTop(boolean withActionBar) {
        return tintManager.getConfig().getPixelInsetTop(withActionBar);
    }

    public boolean getStatusBarEnable() {
        return tintManager.getConfig().getPixelInsetTop(false) != 0;
    }

    public boolean getNavigationbarEnable() {
        return isNavigationBarShowing();
    }

    public int getNavigationBarHeight() {
        return tintManager.getConfig().getNavigationBarHeight();
    }

    public int getStatusBarHeight() {
        return tintManager.getConfig().getStatusBarHeight();
    }

    public void setStatusBarEnabled(boolean enabled) {
        tintManager.setStatusBarTintEnabled(enabled);
    }

    public void setNavigationBarEnabled(boolean enabled) {
        tintManager.setNavigationBarTintEnabled(enabled);
    }

    public void setStatusBarResource(int res) {
        tintManager.setStatusBarTintResource(res);
    }

    public void setNavigationBarResource(int res) {
        tintManager.setNavigationBarTintResource(res);
    }

    private int getNavBarHeight() {
        Resources res = Resources.getSystem();
        int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId != 0) {
            return res.getDimensionPixelSize(resourceId);
        } else {
            return 0;
        }
    }

    public static void transparencyAllBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public static void transparencyBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}
