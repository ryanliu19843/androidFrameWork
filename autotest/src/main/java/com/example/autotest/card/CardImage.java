//
//  CardImage
//
//  Created by ryan on 2017-05-19 13:12:12
//  Copyright (c) ryan All rights reserved.


/**
 *
 */

package com.example.autotest.card;

import androidx.recyclerview.widget.RecyclerView;

import com.mdx.framework.widget.pagerecycleview.ada.Card;

import com.example.autotest.item.Image;

public class CardImage extends Card<Object> {

    public CardImage(String item) {
        this.setItem(item);
        this.type = com.example.autotest.R.string.id_image;
    }

    @Override
    public void bind(RecyclerView.ViewHolder viewHolder, int posion) {
        Image item = (Image) viewHolder;
        item.set(posion, this);
        this.lastitem = null;
    }


}


