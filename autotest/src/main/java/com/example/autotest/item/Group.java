//
//  Group
//
//  Created by ryan on 2017-05-11 11:13:53
//  Copyright (c) ryan All rights reserved.


/**
   
*/

package com.example.autotest.item;

import com.example.autotest.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.mdx.framework.widget.pagerecycleview.viewhold.MViewHold;
import com.mdx.framework.widget.pagerecycleview.viewhold.ViewHodeParam;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.autotest.card.CardGroup;


public class Group extends BaseItem{
    public LinearLayout contentView;
    public TextView text;



    public Group(View itemView, Context context) {
        super(itemView);
        this.context=context;
        initView();
    }

    @SuppressLint("InflateParams")
    public static Group getView(Context context, ViewGroup parent) {
        LayoutInflater flater = LayoutInflater.from(context);
        View convertView;
        if (parent == null) {
            convertView = flater.inflate(R.layout.item_group, null);
        } else {
            convertView = flater.inflate(R.layout.item_group, parent, false);
        }
        return new Group(convertView, context);
    }

    private void initView() {
    	findVMethod();
    }

    private void findVMethod(){
        contentView=(LinearLayout)findViewById(R.id.contentView);
        text=(TextView)findViewById(R.id.text);


    }

    public void set(int posion,CardGroup card){
        this.card=card;
        this.text.setText(card.item);
    }
    
    

}