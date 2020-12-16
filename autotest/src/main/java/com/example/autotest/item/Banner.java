//
//  Banner
//
//  Created by ryan on 2017-05-19 13:50:42
//  Copyright (c) ryan All rights reserved.


/**
   
*/

package com.example.autotest.item;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.autotest.R;
import com.example.autotest.card.CardBanner;
import com.mdx.framework.widget.banner.CirleCurr;


public class Banner extends BaseItem{
    public LinearLayout contentView;
    public CirleCurr ciclecurr;



    public Banner(View itemView, Context context) {
        super(itemView);
        this.context=context;
        initView();
    }

    @SuppressLint("InflateParams")
    public static Banner getView(Context context, ViewGroup parent) {
        LayoutInflater flater = LayoutInflater.from(context);
        View convertView;
        if (parent == null) {
            convertView = flater.inflate(R.layout.item_banner, null);
        } else {
            convertView = flater.inflate(R.layout.item_banner, parent, false);
        }
        return new Banner(convertView, context);
    }

    private void initView() {
    	findVMethod();
    }

    private void findVMethod(){
        contentView=(LinearLayout)findViewById(R.id.contentView);
        ciclecurr=(CirleCurr)findViewById(R.id.ciclecurr);


    }

    public void set(int posion,CardBanner card){
        this.card=card;
        this.ciclecurr.setAdapter(card.item);
    }
    
    

}