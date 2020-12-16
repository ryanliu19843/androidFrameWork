package com.mdx.framework.widget.swipback.app;

import android.os.Bundle;
import android.view.View;

import com.mdx.framework.activity.MFragmentActivity;
import com.mdx.framework.widget.swipback.SwipeBackLayout;
import com.mdx.framework.widget.swipback.Utils;

public abstract class SwipeBackActivityActionBar extends MFragmentActivity implements SwipeBackActivityBase {
    private SwipeBackActivityHelper mHelper;
    private boolean swipBackAble = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
        super.onCreate(savedInstanceState);
        afterCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public <T extends View> T findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return (T) v;
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

    public boolean isSwipBackAble() {
        return swipBackAble;
    }

    protected void afterCreate(Bundle savedInstanceState) {

    }
}
