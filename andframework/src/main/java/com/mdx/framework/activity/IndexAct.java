package com.mdx.framework.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.RelativeLayout;

import com.mdx.framework.Frame;
import com.mdx.framework.R;
import com.mdx.framework.utility.Helper;

public class IndexAct extends BaseActivity {
    private long exitTime = 0;
    
    long timestart = 0, timeend = 0;
    
    @Override
    public void create(Bundle savedInstanceState) {
        setContentView(R.layout.default_act_index);
        this.setSwipeBackEnable(false);
    }
    

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Helper.toast(getContext().getResources().getString(R.string.click_exit), getApplicationContext());
                exitTime = System.currentTimeMillis();
            } else {
                Frame.finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
