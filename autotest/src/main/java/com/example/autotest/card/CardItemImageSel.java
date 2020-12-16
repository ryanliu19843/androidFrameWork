//
//  CardItemImageSel
//
//  Created by ryan on 2017-12-27 15:45:35
//  Copyright (c) ryan All rights reserved.


/**
   
*/

package com.example.autotest.card;

import com.mdx.framework.widget.pagerecycleview.ada.Card;

import androidx.recyclerview.widget.RecyclerView;

import com.example.autotest.item.ItemImageSel;

public class CardItemImageSel extends Card<String>{
	
	public CardItemImageSel(String item) {
	    this.setItem(item);
    	this.type = com.example.autotest.R.string.id_itemimagesel;
    }

     @Override
     public void bind(RecyclerView.ViewHolder viewHolder, int posion) {
        ItemImageSel item = (ItemImageSel) viewHolder;
        item.set(posion, this);
        this.lastitem = null;
     }
    
    

}


