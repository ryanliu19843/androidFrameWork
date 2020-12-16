//
//  ActColtest
//
//  Created by ryan on 2017-05-31 14:57:17
//  Copyright (c) ryan All rights reserved.


/**
 *
 */

package com.example.autotest.act;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.autotest.R;
import com.example.autotest.dataformat.DfSimple;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mdx.framework.widget.pagerecycleview.MRecyclerView;
import com.mdx.framework.widget.pagerecycleview.ada.CardAdapter;
import com.mdx.framework.widget.pagerecycleview.widget.OnSyncPageSwipListener;
import com.mdx.framework.widget.pagerecycleview.widget.SwipSyncTask;

public class ActColtest extends BaseAct {


    public Toolbar toolbar;
    public AppBarLayout appbar;
    public MRecyclerView viewpager;
    public FloatingActionButton fab;
    public CoordinatorLayout main_content;

    public SwipSyncTask swipSyncTask;
    public OnSyncPageSwipListener onPageSwipListener;

    @Override
    public void create(Bundle savedInstanceState) {
        setContentView(R.layout.act_coltest);
        initView();
        loaddata();
    }


    private void initView() {
        findVMethod();
    }

    private void findVMethod() {

        toolbar = findViewById(R.id.toolbar);
        appbar = findViewById(R.id.appbar);
        viewpager = findViewById(R.id.viewpager);
        fab = findViewById(R.id.fab);
        main_content = findViewById(R.id.main_content);

        viewpager.setLayoutManager(new LinearLayoutManager(getContext()));
        swipSyncTask = new SwipSyncTask() {
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
        onPageSwipListener = new OnSyncPageSwipListener(swipSyncTask);

        viewpager.setOnSwipLoadListener(onPageSwipListener);
        viewpager.pullloadelay();
    }


    public void loaddata() {

    }


}
