//
//  DfComrn
//
//  Created by ryan on 2017-05-11 11:13:27
//  Copyright (c) ryan All rights reserved.


/**
   
*/

package com.example.autotest.dataformat;

import android.content.Context;

import com.example.autotest.card.CardComrn;
import com.mdx.framework.server.api.Son;
import com.mdx.framework.widget.pagerecycleview.ada.Card;
import com.mdx.framework.widget.pagerecycleview.ada.CardAdapter;
import com.mdx.framework.widget.util.DataFormat;

import java.util.ArrayList;
import java.util.List;

public class DfComrn extends DataFormat{
    int size = 1;

	@Override
	public boolean hasNext() {
		return size >= Integer.MAX_VALUE;
	}

    public CardAdapter getCardAdapter(Context context, Son son, int page) {
        List<Card> list = new ArrayList<>();
//        for(Object o:){
               CardComrn card=new CardComrn("");
               list.add(card);
//        }
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
