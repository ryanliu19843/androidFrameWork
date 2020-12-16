//
//  Comrn
//
//  Created by ryan on 2017-05-11 11:13:27
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

import com.example.autotest.card.CardComrn;


public class Comrn extends BaseItem{
    public LinearLayout contentView;
    public TextView text;
    public TextView textView;
    public TextView textView2;



    public Comrn(View itemView, Context context) {
        super(itemView);
        this.context=context;
        initView();
    }

    @SuppressLint("InflateParams")
    public static Comrn getView(Context context, ViewGroup parent) {
        LayoutInflater flater = LayoutInflater.from(context);
        View convertView;
        if (parent == null) {
            convertView = flater.inflate(R.layout.item_comrn, null);
        } else {
            convertView = flater.inflate(R.layout.item_comrn, parent, false);
        }
        return new Comrn(convertView, context);
    }

    private void initView() {
    	findVMethod();
    }

    private void findVMethod(){
        contentView=(LinearLayout)findViewById(R.id.contentView);
        text=(TextView)findViewById(R.id.text);
        textView=(TextView)findViewById(R.id.textView);
        textView2=(TextView)findViewById(R.id.textView2);


    }

    public void set(int posion,CardComrn card){
        this.card=card;
        this.text.setText(card.item);
    }
    
    

}