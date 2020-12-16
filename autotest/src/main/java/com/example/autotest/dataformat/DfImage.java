//
//  DfImage
//
//  Created by ryan on 2017-05-19 13:12:12
//  Copyright (c) ryan All rights reserved.


/**
   
*/

package com.example.autotest.dataformat;

import android.content.Context;

import com.example.autotest.card.CardImage;
import com.mdx.framework.server.api.Son;
import com.mdx.framework.widget.pagerecycleview.ada.Card;
import com.mdx.framework.widget.pagerecycleview.ada.CardAdapter;
import com.mdx.framework.widget.util.DataFormat;

import java.util.ArrayList;
import java.util.List;

public class DfImage extends DataFormat{
    int size = 1;

	public String strings[]=new String[]{"http://ww3.sinaimg.cn/large/85cccab3gw1etdg20apw5g20dw0791kx.jpg",
										"http://ww1.sinaimg.cn/large/85cccab3gw1etdg1rk3olg20cs077hdt.jpg",
										"http://ww2.sinaimg.cn/large/85cccab3gw1etdg1c65bog20go08x1kz.jpg"
									};

	@Override
	public boolean hasNext() {
		return size >= Integer.MAX_VALUE;
	}

    public CardAdapter getCardAdapter(Context context, Son son, int page) {
        List<Card> list = new ArrayList<>();
        for(String o:strings){
               CardImage card=new CardImage(o);
               list.add(card);
        }
        return new CardAdapter(context,list);
    }

	@Override
	public String[][] getPageNext() {
		return null;
	}

	@Override
	public void reload() {

	}
}
