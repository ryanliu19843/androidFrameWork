//
//  ItemImageSel
//
//  Created by ryan on 2017-12-27 15:45:35
//  Copyright (c) ryan All rights reserved.


/**
   
*/

package com.mdx.framework.widget.selectphotos.item;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mdx.framework.R;
import com.mdx.framework.widget.MImageView;


public class DefaultItemImageSel extends BaseItem{
    public RelativeLayout panel_content;
    public MImageView image;
    public View maxview;
    public TextView tv;
    public CheckBox checkbox;



    public DefaultItemImageSel(View itemView, Context context) {
        super(itemView);
        this.context=context;
        initView();
    }

    @SuppressLint("InflateParams")
    public static DefaultItemImageSel getView(Context context, ViewGroup parent) {
        LayoutInflater flater = LayoutInflater.from(context);
        View convertView;
        if (parent == null) {
            convertView = flater.inflate(R.layout.default_item_image_sel, null);
        } else {
            convertView = flater.inflate(R.layout.default_item_image_sel, parent, false);
        }
        return new DefaultItemImageSel(convertView, context);
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
        image.setObj("file:"+card.item.path);
    }
    
    

}