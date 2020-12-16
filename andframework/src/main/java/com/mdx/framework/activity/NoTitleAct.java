package com.mdx.framework.activity;

import com.mdx.framework.R;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.RelativeLayout;

public class NoTitleAct extends TitleTransparentAct {
    @Override
    public void create(Bundle savedInstanceState) {
        setContentView(R.layout.default_act_no_title);
    }

    @SuppressLint("NewApi")
    protected void actionBarInit() { // 初始化 actionbar
        super.actionBarInit();
        actionbar.setVisibility(View.GONE);
    }

}
