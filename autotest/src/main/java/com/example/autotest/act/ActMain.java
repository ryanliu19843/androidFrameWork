//
//  ActMain
//
//  Created by ryan on 2017-05-09 15:00:42
//  Copyright (c) ryan All rights reserved.


/**
 *
 */

package com.example.autotest.act;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.autotest.R;
import com.example.autotest.dataformat.DfSimple;
import com.example.autotest.item.Simple;
import com.mdx.framework.server.api.base.ApiUpdateApi;
import com.mdx.framework.widget.MImageView;
import com.mdx.framework.widget.pagerecycleview.MFRecyclerView;
import com.mdx.framework.widget.pagerecycleview.pullanimview.SPView;
import com.mdx.framework.widget.pagerecycleview.widget.OnPageSwipListener;
import com.mdx.framework.widget.systembartint.SystemBarTintManager;


public class ActMain extends BaseAct {


    public TextView title;
    public MFRecyclerView recyclerview;
    public MImageView icon1;
    public TextView txt;
    public MImageView icon2;
    public TextView txt2;
    public MImageView icon4;
    public TextView txt3;
    public MImageView icon3;
    public TextView txt4;
    public static ObjectAnimator anm;
    private SystemBarTintManager tintManager = null;

    @Override
    public void create(Bundle savedInstanceState) {
        setContentView(R.layout.frg_comn);
        initView();
        loaddata();
        this.setId("ActMain");
    }


    private void initView() {
        findVMethod();
    }

    private void findVMethod() {


        title = (TextView) findViewById(R.id.title);
        recyclerview = (com.mdx.framework.widget.pagerecycleview.MFRecyclerView) findViewById(R.id.recyclerview);
        icon1 = (MImageView) findViewById(R.id.icon1);
        txt = (TextView) findViewById(R.id.txt);
        icon2 = (MImageView) findViewById(R.id.icon2);
        txt2 = (TextView) findViewById(R.id.txt2);
        icon4 = (MImageView) findViewById(R.id.icon4);
        txt3 = (TextView) findViewById(R.id.txt3);
        icon3 = (MImageView) findViewById(R.id.icon3);
        txt4 = (TextView) findViewById(R.id.txt4);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        initWindow();

    }


    @TargetApi(19)
    private void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }


        tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
    }

    @Override
    public void disposeMsg(int type, Object obj) {
        Simple.SimpleSet aitem = (Simple.SimpleSet) obj;
        SPView spView = (SPView) recyclerview.getRecyclerView().getSwipRefreshView().getSwipRefresh();
        setBackground(tintManager, title, txt3, icon4, spView, aitem.bg, aitem.tbg, aitem.litbg);
        spView.bcolor = aitem.tbg;
        spView.scolor = aitem.tbg;
        spView.tcolor = aitem.tbg;
    }


    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public void loaddata() {
        recyclerview.setOnSwipLoadListener(new OnPageSwipListener(getContext(), new ApiUpdateApi().set("com.udows.xdt", 1, "ios", "sss", "0", ""), new DfSimple()));
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        recyclerview.pullloadelay();
    }


    @SuppressLint("ObjectAnimatorBinding")
    public static void setBackground(final SystemBarTintManager tintManager, final TextView title, final TextView txt3, final MImageView icon4, final SPView spView, final int bg, final int tg, final int ltg) {
        final int nbg = ((ColorDrawable) title.getBackground()).getColor();
        final int cbg = title.getCurrentTextColor();
        final int scolor = txt3.getCurrentTextColor();
        if (nbg == bg) {
            return;
        }
        if (anm != null && anm.isRunning()) {
            anm.cancel();
        }
        // 自定义颜色
        tintManager.setTintColor(Color.parseColor("#ff0000"));

        anm = ObjectAnimator.ofInt(title, "ss", 0, 100);

        anm.setDuration(500);
        anm.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int x = (int) animation.getAnimatedValue();
                int ts = getColor(nbg, bg, x);
                int backcolor = getColor(nbg, bg, x);
                title.setBackgroundColor(getColor(nbg, bg, x));

                if (tintManager != null) {
                    tintManager.setStatusBarTintColor(backcolor);
                }

                ts = getColor(cbg, tg, x);
                title.setTextColor(ts);
                spView.lcolor = getColor(spView.lcolor, bg, x);
//                spView.scolor=getColor(spView.scolor,bg,x);
                ts = getColor(scolor, ltg, x);
                txt3.setTextColor(ts);
                icon4.setTocolor(ts);
            }
        });
        anm.start();
    }


    public static int getColor(int f, int t, int x) {
        int r = Color.red(f);
        int g = Color.green(f);
        int b = Color.blue(f);
        int a = Color.alpha(f);

        int nr = r - Color.red(t);
        int ng = g - Color.green(t);
        int nb = b - Color.blue(t);
        int na = a - Color.alpha(t);

        int sr = (int) (nr * (x / 100f));
        int sg = (int) (ng * (x / 100f));
        int sb = (int) (nb * (x / 100f));
        int sa = (int) (na * (x / 100f));
        return Color.argb(a - sa, r - sr, g - sg, b - sb);
    }


}
