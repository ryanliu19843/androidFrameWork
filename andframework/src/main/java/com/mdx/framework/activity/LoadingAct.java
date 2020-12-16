package com.mdx.framework.activity;

import android.os.Bundle;

import com.mdx.framework.R;

public class LoadingAct extends BaseActivity {


    @Override
    public void create(Bundle savedInstanceState) {
        setContentView(R.layout.default_act_loading);
        this.setSwipeBackEnable(false);
        this.isSkipNavigation = true;
        this.isSkipStatus = true;
    }

    protected void actionBarInit() {

    }
}
