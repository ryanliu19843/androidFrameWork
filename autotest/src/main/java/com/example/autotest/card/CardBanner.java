//
//  CardBanner
//
//  Created by ryan on 2017-05-19 13:50:42
//  Copyright (c) ryan All rights reserved.


/**
 *
 */

package com.example.autotest.card;


import androidx.recyclerview.widget.RecyclerView;

import com.example.autotest.item.Banner;
import com.mdx.framework.widget.pagerecycleview.ada.Card;
import com.mdx.framework.widget.pagerecycleview.ada.CardAdapter;

public class CardBanner extends Card<CardAdapter> {

    public CardBanner(CardAdapter item) {
        this.setItem(item);
        this.type = com.example.autotest.R.string.id_banner;
    }

    @Override
    public void bind(RecyclerView.ViewHolder viewHolder, int posion) {
        Banner item = (Banner) viewHolder;
        item.set(posion, this);
        this.lastitem = null;
    }


}


