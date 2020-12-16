//
//  ItemImageSel
//
//  Created by ryan on 2017-12-27 15:45:35
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
import android.widget.RelativeLayout;
import com.mdx.framework.widget.MImageView;
import android.widget.TextView;
import android.widget.CheckBox;

import com.example.autotest.card.CardItemImageSel;


public class ItemImageSel extends BaseItem{
    public RelativeLayout panel_content;
    public MImageView image;
    public View maxview;
    public TextView tv;
    public CheckBox checkbox;



    public ItemImageSel(View itemView, Context context) {
        super(itemView);
        this.context=context;
        initView();
    }

    @SuppressLint("InflateParams")
    public static ItemImageSel getView(Context context, ViewGroup parent) {
        LayoutInflater flater = LayoutInflater.from(context);
        View convertView;
        if (parent == null) {
            convertView = flater.inflate(R.layout.item_image_sel, null);
        } else {
            convertView = flater.inflate(R.layout.item_image_sel, parent, false);
        }
        return new ItemImageSel(convertView, context);
    }

    private void initView() {
    	findVMethod();
    }

    private void findVMethod(){
        panel_content=(RelativeLayout)findViewById(R.id.panel_content);
        image=(MImageView)findViewById(R.id.image);
        maxview=(View)findViewById(R.id.maxview);
        tv=(TextView)findViewById(R.id.tv);
        checkbox=(CheckBox)findViewById(R.id.checkbox);


    }

    public void set(int posion,CardItemImageSel card){
        this.card=card;

    }
    
    

}