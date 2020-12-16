//
//  CardGroup
//
//  Created by ryan on 2017-05-11 11:13:53
//  Copyright (c) ryan All rights reserved.


/**
 *
 */

package com.example.autotest.card;


import androidx.recyclerview.widget.RecyclerView;

import com.example.autotest.item.Group;
import com.mdx.framework.widget.pagerecycleview.ada.Card;

public class CardGroup extends Card<String> {

    public CardGroup(String item) {
        this.setItem(item);
        this.type = com.example.autotest.R.string.id_group;
    }

    @Override
    public void bind(RecyclerView.ViewHolder viewHolder, int posion) {
        Group item = (Group) viewHolder;
        item.set(posion, this);
        this.lastitem = null;
    }


}


