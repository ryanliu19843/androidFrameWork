//
//  CardItemImageSel
//
//  Created by ryan on 2017-12-27 15:45:35
//  Copyright (c) ryan All rights reserved.


/**
   
*/

package com.mdx.framework.widget.selectphotos.item;

import androidx.recyclerview.widget.RecyclerView;

import com.mdx.framework.R;
import com.mdx.framework.widget.pagerecycleview.ada.Card;
import com.mdx.framework.widget.selectphotos.utils.PhotosUtil;

public class CardItemImageSel extends Card<PhotosUtil.ImageItem>{

	public CardItemImageSel(PhotosUtil.ImageItem item) {
	    this.setItem(item);
    	this.type = R.string.id_defaultitemimagesel;
    }

     @Override
     public void bind(RecyclerView.ViewHolder viewHolder, int posion) {
        DefaultItemImageSel item = (DefaultItemImageSel) viewHolder;
        item.set(posion, this);
        this.lastitem = null;
     }
    
    

}


