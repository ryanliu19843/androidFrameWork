//
//  ActRecycletest
//
//  Created by ryan on 2017-05-11 11:16:06
//  Copyright (c) ryan All rights reserved.


/**
 *
 */

package com.example.autotest.act;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.autotest.R;
import com.example.autotest.card.CardComrn;
import com.example.autotest.card.CardGroup;
import com.mdx.framework.widget.pagerecycleview.MFRecyclerView;
import com.mdx.framework.widget.pagerecycleview.ada.Card;
import com.mdx.framework.widget.pagerecycleview.ada.CardAdapter;
import com.mdx.framework.widget.pagerecycleview.widget.OnSyncPageSwipListener;
import com.mdx.framework.widget.pagerecycleview.widget.SwipSyncTask;

import java.util.ArrayList;
import java.util.List;

public class ActRecycletest extends BaseAct {

    public Button reload;
    public Button error;
    public Button group;
    public Button clean;
    public Button haspage;
    public MFRecyclerView recycleview;

    public SwipSyncTask swipSyncTask;
    public OnSyncPageSwipListener onPageSwipListener;
    public boolean isgroup = false, ispage = false;
    public int iserror = 0;


    @Override
    public void create(Bundle savedInstanceState) {
        setContentView(R.layout.act_recycletest);
        initView();
        loaddata();
    }


    private void initView() {
        findVMethod();
    }

    private void findVMethod() {
        reload = (Button) findViewById(R.id.reload);
        error = (Button) findViewById(R.id.error);
        group = (Button) findViewById(R.id.group);
        clean = (Button) findViewById(R.id.clean);
        haspage = (Button) findViewById(R.id.haspage);
        recycleview = (MFRecyclerView) findViewById(R.id.recycleview);
        recycleview.setLayoutManager(new LinearLayoutManager(this));
    }


    public void loaddata() {

        swipSyncTask = new SwipSyncTask() {
            @Override
            protected CardAdapter doInBackground(Object... params) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                List<Card> list = new ArrayList<>();
                for (int i = 0; i < 25; i++) {

                    if (i % 10 == 0 && isgroup) {
                        CardGroup card = new CardGroup("group" + i);
                        card.setShowType(1);
                        list.add(card);
                    } else {
                        CardComrn card = new CardComrn("1111" + i);
                        list.add(card);
                    }
                }
                if (iserror == 1) {
                    error = "加载错误";
                    return null;
                } else if (iserror == 2) {
                    error = "加载错误";
                }
                this.haspage = ispage;
                return new CardAdapter(ActRecycletest.this, list);
            }

            @Override
            public void intermit() {

            }
        };
        onPageSwipListener = new OnSyncPageSwipListener(swipSyncTask);

        recycleview.setOnSwipLoadListener(onPageSwipListener);
        recycleview.pullloadelay();

        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recycleview.pullLoad();
            }
        });

        error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iserror = (iserror + 1) % 3;
                recycleview.pullLoad();
            }
        });

        group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isgroup) {
                    isgroup = false;
                } else {
                    isgroup = true;
                }
                recycleview.pullLoad();
            }
        });


        haspage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ispage) {
                    ispage = false;
                    recycleview.getRecyclerView().endPage();
                } else {
                    ispage = true;
                    recycleview.getRecyclerView().showPage();
                }

            }
        });

        clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recycleview.clearAdapter();
            }
        });
    }
}
