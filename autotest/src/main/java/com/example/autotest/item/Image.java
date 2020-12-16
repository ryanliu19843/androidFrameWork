//
//  Image
//
//  Created by ryan on 2017-05-19 13:12:12
//  Copyright (c) ryan All rights reserved.


/**
   
*/

package com.example.autotest.item;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.autotest.R;
import com.example.autotest.card.CardImage;
import com.example.autotest.frg.FrgRecyclerviewTest;
import com.mdx.framework.activity.TitleAct;
import com.mdx.framework.utility.Helper;
import com.mdx.framework.widget.MImageView;


public class Image extends BaseItem{
    public MImageView imageView;



    public Image(View itemView, Context context) {
        super(itemView);
        this.context=context;
        initView();
    }

    @SuppressLint("InflateParams")
    public static Image getView(Context context, ViewGroup parent) {
        LayoutInflater flater = LayoutInflater.from(context);
        View convertView;
        if (parent == null) {
            convertView = flater.inflate(R.layout.item_image, null);
        } else {
            convertView = flater.inflate(R.layout.item_image, parent, false);
        }
        return new Image(convertView, context);
    }

    private void initView() {
    	findVMethod();
    }

    private void findVMethod(){
        imageView=(MImageView)findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.startActivity(context, FrgRecyclerviewTest.class, TitleAct.class);
            }
        });

    }

    public void set(int posion,CardImage card){
        this.card=card;

        imageView.setObj(card.item,true);
    }
    
    

}