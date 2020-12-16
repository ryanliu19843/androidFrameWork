//
//  FrgComn
//
//  Created by ryan on 2017-06-02 09:31:06
//  Copyright (c) ryan All rights reserved.


/**
 *
 */

package com.example.autotest.frg;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.autotest.R;
import com.example.autotest.dataformat.DfSimple;
import com.example.autotest.item.Simple;
import com.mdx.framework.activity.BaseActivity;
import com.mdx.framework.activity.TitleTransStatusAct;
import com.mdx.framework.activity.TitleTransparentAct;
import com.mdx.framework.server.api.ApiUpdate;
import com.mdx.framework.server.api.UpdateOne;
import com.mdx.framework.server.api.base.ApiUpdateApi;
import com.mdx.framework.widget.MImageView;
import com.mdx.framework.widget.pagerecycleview.MFRecyclerView;
import com.mdx.framework.widget.pagerecycleview.ada.CardAdapter;
import com.mdx.framework.widget.pagerecycleview.pullanimview.SPView;
import com.mdx.framework.widget.pagerecycleview.widget.OnPageSwipListener;
import com.mdx.framework.widget.pagerecycleview.widget.OnSyncPageSwipListener;
import com.mdx.framework.widget.pagerecycleview.widget.SwipSyncTask;
import com.udows.canyin.proto.ApisFactory;


public class FrgComn extends BaseFrg {

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

    @Override
    protected void create(Bundle savedInstanceState) {
        setContentView(R.layout.frg_comn);
        initView();
        loaddata();
        this.setId("ActMain");
    }


    public static FrgComn Instance() {
        return new FrgComn();
    }

    private void initView() {
        findVMethod();
    }

    private void findVMethod() {


        title = findViewById(R.id.title);
        recyclerview = findViewById(R.id.recyclerview);
        icon1 = findViewById(R.id.icon1);
        txt = findViewById(R.id.txt);
        icon2 = findViewById(R.id.icon2);
        txt2 = findViewById(R.id.txt2);
        icon4 = findViewById(R.id.icon4);
        txt3 = findViewById(R.id.txt3);
        icon3 = findViewById(R.id.icon3);
        txt4 = findViewById(R.id.txt4);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    @Override
    public void disposeMsg(int type, Object obj) {
        if (obj instanceof Simple.SimpleSet) {
            Simple.SimpleSet aitem = (Simple.SimpleSet) obj;
            SPView spView = (SPView) recyclerview.getRecyclerView().getSwipRefreshView().getSwipRefresh();
            setBackground((BaseActivity) getActivity(), title, txt3, icon4, spView, aitem.bg, aitem.tbg, aitem.litbg);
            spView.bcolor = aitem.tbg;
            spView.scolor = aitem.tbg;
            spView.tcolor = aitem.tbg;
        }
    }


    public void loaddata() {
//        recyclerview.setOnSwipLoadListener(new OnPageSwipListener(getContext(), new ApiUpdateApi().set("com.udows.xdt", 1, "ios", "sss", "0", ""), new DfSimple()));  //通过接口
//        new ApiUpdateApi().set("com.udows.xdt", 1, "ios", "sss", "0", "").setOnApiInitListeners(new ApiUpdate.OnApiInitListener() {
//            @Override
//            public ApiUpdate[] onApiInitListener(long page, UpdateOne updateone) {
//                return new ApiUpdate[0];
//            }
//        });

        SwipSyncTask swipSyncTask = new SwipSyncTask() {  //自定义调用
            @Override
            protected CardAdapter doInBackground(Object... params) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return new DfSimple().getCardAdapter(getContext(), null, 0);
            }

            @Override
            public void intermit() {

            }
        };
        OnSyncPageSwipListener onPageSwipListener = new OnSyncPageSwipListener(swipSyncTask);

        recyclerview.setOnSwipLoadListener(onPageSwipListener);
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }

        recyclerview.pullloadelay();
    }


    @SuppressLint("ObjectAnimatorBinding")
    public static void setBackground(final BaseActivity activity, final TextView title, final TextView txt3, final MImageView icon4, final SPView spView, final int bg, final int tg, final int ltg) {
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

        anm = ObjectAnimator.ofInt(title, "ss", 0, 100);

        anm.setDuration(200);
        anm.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int x = (int) animation.getAnimatedValue();
                int ts = getColor(nbg, bg, x);
                int backcolor = getColor(nbg, bg, x);
                title.setBackgroundColor(backcolor);

                if (activity != null) {
                    activity.setTintColor(backcolor);
                    if (!(activity instanceof TitleTransparentAct) && !(activity instanceof TitleTransStatusAct)) {
                        activity.getActionbar().setBackgroundColor(backcolor);
                    }
                }
                ts = getColor(cbg, tg, x);
                title.setTextColor(ts);
                spView.lcolor = getColor(spView.lcolor, bg, x);
                spView.scolor = getColor(spView.scolor, bg, x);
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