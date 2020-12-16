/*
 * 文件名: Purple.java 版 权： Copyright Huawei Tech. Co. Ltd. All Rights Reserved. 描
 * 述: [该类的简要描述] 创建人: ryan 创建时间:2014年5月30日
 *
 * 修改人： 修改时间: 修改内容：[修改内容]
 */
package com.mdx.framework.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mdx.framework.Frame;
import com.mdx.framework.R;
import com.mdx.framework.activity.MFragment.OnFragmentCreateListener;
import com.mdx.framework.autofit.AutoFitFragment;
import com.mdx.framework.commons.ParamsManager;
import com.mdx.framework.commons.device.NavUtil;
import com.mdx.framework.log.MLog;
import com.mdx.framework.prompt.Prompt;
import com.mdx.framework.utility.Device;
import com.mdx.framework.utility.MAsyncTask;
import com.mdx.framework.widget.ActionBar;
import com.mdx.framework.widget.systembartint.SystemBarTintManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public abstract class BaseActivity extends MActivityActionbar implements Prompt {
    public static final String EXTRA_CLASSNAME = "className";

    public NavUtil navUtil;

    protected Fragment mFragment;

    protected View topWidget;

    protected View bottomWidget, defautltContainer;

    public boolean navigationbarEnable = false, statusBarEnable = false;
    public int navigationbarh = 0, statush = 0, actionbarh = 0;

    protected ActionBar actionbar;

    public String classname;

    private boolean isInited = false;

    protected boolean isSkipStatus = false, isSkipNavigation = false;

    // protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    // if (this.mFragment != null) {
    // this.mFragment.onActivityResult(requestCode, resultCode, data);
    // }
    // super.onActivityResult(requestCode, resultCode, data);
    // }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FragmentManager fm = getSupportFragmentManager();
        for (Fragment frag : fm.getFragments()) {
            if (frag == null) {
            } else {
                handleResult(frag, requestCode, resultCode, data);
            }
        }
    }

    /**
     * 递归调用，对所有子Fragement生效
     *
     * @param frag
     * @param requestCode
     * @param resultCode
     * @param data
     */
    private void handleResult(Fragment frag, int requestCode, int resultCode, Intent data) {
        frag.onActivityResult(requestCode & 0xffff, resultCode, data);
        @SuppressLint("RestrictedApi") List<Fragment> frags = frag.getChildFragmentManager().getFragments();
        if (frags != null) {
            for (Fragment f : frags) {
                if (f != null)
                    handleResult(f, requestCode, resultCode, data);
            }
        }
    }

    @SuppressLint({"InlinedApi", "NewApi"})
    @Override
    protected void afterCreate(Bundle savedInstanceState) { // 初始化activity
        topWidget = findViewById(R.id.topWidget);
        bottomWidget = findViewById(R.id.bottomWidget); // 防止actionbar留白
        actionbar = findViewById(R.id.ActionbarWidget);
        defautltContainer = findViewById(R.id.defautlt_container);
        if (actionbar != null) {
            actionbarh = actionbar.getLayoutParams().height;
        }

        if (isSkipStatus && !isSkipNavigation) {
            navUtil.transparencyBar(this);
        } else if (isSkipNavigation && isSkipStatus) {
            navUtil.transparencyAllBar(this);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && !isInited) { // 判断
            // 系统版本
            navUtil = new NavUtil(this);


            navUtil.getPixelInsetBottom();
            navUtil.getPixelInsetTop(false);
            navigationbarEnable = navUtil.getNavigationbarEnable();
            statusBarEnable = navUtil.getStatusBarEnable();

            navigationbarh = navUtil.getNavigationBarHeight();
            statush = navUtil.getStatusBarHeight();
            if (navigationbarh <= statush) {
                navigationbarh = 0;
            }
            navUtil.setStatusBarEnabled(true);
            navUtil.setNavigationBarEnabled(true);
            navUtil.setStatusBarResource(0x00000000);
            navUtil.setNavigationBarResource(0x00000000);
            Device.statush = statush;
            Device.navigationbarEnable = navigationbarEnable;
            Device.statusBarEnable = statusBarEnable;
            Device.navigationbarh = navigationbarh;
            Device.statush = statush;
            Device.actionbarh = actionbarh;
        }
        actionBarInit();
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Device.statush = statush;
        Device.navigationbarEnable = navigationbarEnable;
        Device.statusBarEnable = statusBarEnable;
        Device.navigationbarh = navigationbarh;
        Device.statush = statush;
        Device.actionbarh = actionbarh;
    }

    public void init() {
        classname = getIntent().getStringExtra(EXTRA_CLASSNAME);

        if (TextUtils.isEmpty(classname)) {
            new MAsyncTask<Object, Object, Object>() {

                @Override
                protected Object doInBackground(Object... params) {
                    synchronized (Frame.INITLOCK) {
                    }
                    classname = ParamsManager.get("DefaultFragment");
                    return null;
                }

                public void finish(Object result) {
                    initFrament(classname);
                }
            }.execute("");
        } else {
            initFrament(classname);
        }
    }

    /**
     * @return the actionbar
     */
    public ActionBar getActionbar() {
        return actionbar;
    }

    protected void initFrament(String classname) {

        Class<?> cls;
        try {
            Object fragment;
            if (TextUtils.isEmpty(classname)) {
                fragment = new AutoFitFragment();
            } else {
                cls = Class.forName(classname); //
                fragment = cls.newInstance();
            }
            if (fragment instanceof Fragment) {
                if (fragment instanceof MFragment) {
                    MFragment obj = (MFragment) fragment;
                    if (obj != null) {
                        MLog.D(obj.getClass().getName());
                    }
                    obj.addOnFragmentCreateListener(new OnFragmentCreateListener() {

                        @Override
                        public void onFragmentCreateListener(MFragment mFragment) {
                            setDefaultActionBar();
                            try {
                                Method m1 = mFragment.getClass().getDeclaredMethod("setActionBar", ActionBar.class, Context.class);
                                m1.invoke(mFragment, getActionbar(), getContext());
                            } catch (NoSuchMethodException e) {
//                                 e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                MLog.E(e);
                            } catch (InvocationTargetException e) {
                                MLog.E(e);
                            }
                        }
                    });
                } else {
                    setDefaultActionBar();
                    Object obj = fragment;
                    Method m1 = obj.getClass().getDeclaredMethod("setActionBar", ActionBar.class, Context.class);
                    m1.invoke(obj, getActionbar(), this);
                }
                showFragment((Fragment) fragment);
            }
        } catch (Exception e) {
            MLog.D(e);
        }
    }

    protected void setDefaultActionBar() {
        if (getActionbar().getChildCount() == 0) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View v = inflater.inflate(R.layout.default_actionbar, getActionbar());
            getActionbar().setBackView(v.findViewById(R.id.action_back));
            getActionbar().setTitleView((TextView) v.findViewById(R.id.action_title));
            getActionbar().setButtonsView((LinearLayout) v.findViewById(R.id.action_buttons));
            getActionbar().init(getActivity());
        }
    }

    protected void actionBarInit() { // 初始化 actionbar
        if (topWidget != null) {
            MarginLayoutParams rlp = (MarginLayoutParams) topWidget.getLayoutParams();
            rlp.width = LayoutParams.MATCH_PARENT;
            rlp.height = statusBarEnable && isSkipStatus ? statush : 0;
            topWidget.setLayoutParams(rlp);
        }
        if (bottomWidget != null) {
            MarginLayoutParams rlp = (MarginLayoutParams) bottomWidget.getLayoutParams();
            rlp.width = LayoutParams.MATCH_PARENT;
            rlp.height = navigationbarEnable && isSkipNavigation ? navigationbarh : 0;
            bottomWidget.setLayoutParams(rlp);
        }
    }

    public int getStatusH() {
        return statusBarEnable && isSkipStatus ? statush : 0;
    }

    public int getNavbarH() {
        return navigationbarEnable && isSkipNavigation ? navigationbarh : 0;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mFragment != null) {
            Object obj = mFragment;
            Method m1;
            try {
                m1 = obj.getClass().getDeclaredMethod("onOptionsItemSelected", MenuItem.class);
                return (Boolean) m1.invoke(obj, item);
            } catch (Exception e) {
                if (item.getItemId() == android.R.id.home) {
                    finish();
                    return true;
                }
            }
        } else {
            if (item.getItemId() == android.R.id.home) {
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public abstract void create(Bundle savedInstanceState);

    public void showFragment(Fragment fragment) {
        MLog.D("showfragment");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (mFragment != null) {
            fragmentTransaction.remove(mFragment);
        }
        fragmentTransaction.replace(R.id.defautlt_container, fragment);
        mFragment = fragment;
        fragmentTransaction.commit();
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (mFragment != null && mFragment instanceof MFragment) {
            MFragment obj = (MFragment) mFragment;
            if (obj.onKeyLongPress(keyCode, event)) {
                return true;
            }
        }
        return super.onKeyLongPress(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (mFragment != null && mFragment instanceof MFragment) {
            MFragment obj = (MFragment) mFragment;
            if (obj.onKeyUp(keyCode, event)) {
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        if (mFragment != null && mFragment instanceof MFragment) {
            MFragment obj = (MFragment) mFragment;
            if (obj.onKeyMultiple(keyCode, repeatCount, event)) {
                return true;
            }
        }
        return super.onKeyMultiple(keyCode, repeatCount, event);
    }

    @SuppressLint("NewApi")
    @Override
    public boolean onKeyShortcut(int keyCode, KeyEvent event) {
        if (mFragment != null && mFragment instanceof MFragment) {
            MFragment obj = (MFragment) mFragment;
            if (obj.onKeyShortcut(keyCode, event)) {
                return true;
            }
        }
        return super.onKeyShortcut(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mFragment != null && mFragment instanceof MFragment) {
            MFragment obj = (MFragment) mFragment;
            if (obj.onKeyDown(keyCode, event)) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void setNavTintColor(@ColorInt int color) {
        if (topWidget != null) {
            topWidget.setBackgroundColor(color);
        }
        if (!isSkipNavigation) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setNavigationBarColor(color);
            }
        }
    }

    @Override
    public void setStaTintColor(@ColorInt int color) {
        if (bottomWidget != null) {
            bottomWidget.setBackgroundColor(color);
        }
        if (!isSkipStatus) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(color);
            }
        }
    }

}
