//
//  CardComrn
//
//  Created by ryan on 2017-05-11 11:13:27
//  Copyright (c) ryan All rights reserved.


/**
   
*/

package com.example.autotest.card;


import androidx.recyclerview.widget.RecyclerView;

import com.example.autotest.item.Comrn;
import com.mdx.framework.widget.pagerecycleview.ada.Card;

public class CardComrn extends Card<String>{
	
	public CardComrn(String item) {
	    this.setItem(item);
    	this.type = com.example.autotest.R.string.id_comrn;
    }

     @Override
     public void bind(RecyclerView.ViewHolder viewHolder, int posion) {
        Comrn item = (Comrn) viewHolder;
        item.set(posion, this);
        this.lastitem = null;
     }
    
    

}


