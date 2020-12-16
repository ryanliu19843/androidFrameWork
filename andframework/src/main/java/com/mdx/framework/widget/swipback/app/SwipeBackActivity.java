package com.mdx.framework.widget.swipback.app;

import com.mdx.framework.activity.MFragmentActivity;
import com.mdx.framework.widget.swipback.SwipeBackLayout;
import com.mdx.framework.widget.swipback.Utils;

import android.os.Bundle;
import androidx.annotation.IdRes;
import android.view.View;

public abstract class SwipeBackActivity extends MFragmentActivity implements SwipeBackActivityBase {
    private SwipeBackActivityHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
        super.onCreate(savedInstanceState);
        afterCreate(savedInstanceState);
    }

    protected void afterCreate(Bundle savedInstanceState) {
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public <T extends View> T findViewById(@IdRes int id) {
        T v = super.findViewById(id);
        if (v == null && mHelper != null)
            return (T) mHelper.findViewById(id);
        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }

}
