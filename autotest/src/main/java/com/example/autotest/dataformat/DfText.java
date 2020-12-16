//
//  DfText
//
//  Created by ryan on 4047-04-44 46:49:47
//  Copyright (c) ryan All rights reserved.


/**
   
*/

package com.example.autotest.dataformat;

import android.content.Context;

import com.example.autotest.R;
import com.example.autotest.card.CardBanner;
import com.example.autotest.card.CardImage;
import com.mdx.framework.server.api.Son;
import com.mdx.framework.server.api.base.Msg_Key;
import com.mdx.framework.widget.pagerecycleview.autofit.AutoCard;
import com.mdx.framework.widget.pagerecycleview.ada.Card;
import com.mdx.framework.widget.pagerecycleview.ada.CardAdapter;
import com.mdx.framework.widget.util.DataFormat;

import java.util.ArrayList;
import java.util.List;

public class DfText extends DataFormat{
    int size = 4;

	public static DfText getDF(){
		return new DfText();
	}

	@Override
	public boolean hasNext() {
		return false;
	}

    public CardAdapter getCardAdapter(Context context, Son son, int page) {
        List<Card> list = new ArrayList<>();
		CardBanner cardBanner=new CardBanner(new DfImage().getCardAdapter(context,null,0));  //banner
		cardBanner.setSpan(8);
		list.add(cardBanner);

		for(int i=1;i<9;i++) {
			list.add(new CardImage("ASSETS:"+i+".gif").setSpan(4));
		}

		list.add(new CardImage("http://ubmcmm.baidustatic.com/media/v1/0f000ZosO4F2Z6OM9XSPjf.jpg").setSpan(4).setLayoutparams(Card.NOSET,200));
		list.add(new CardImage("https://imgsa.baidu.com/baike/c0%3Dbaike92%2C5%2C5%2C92%2C30/sign=e1038a433f6d55fbd1cb7e740c4b242f/9825bc315c6034a8994b5e1cc913495408237686.jpg").setSpan(4).setLayoutparams(Card.NOSET,200));


		list.add(new CardImage("https://imgsa.baidu.com/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=ac96722965380cd7f213aabfc02dc651/a5c27d1ed21b0ef4073d15c7ddc451da80cb3e55.jpg").setSpan(8).setShowType(1).setLayoutparams(Card.NOSET,400));


		list.add(new CardImage("http://ww2.sinaimg.cn/large/85cccab3tw1esjmgbv5o6g20i209k1kx.jpg").setSpan(4).setLayoutparams(Card.NOSET,400));
		list.add(new CardImage("http://ww2.sinaimg.cn/large/85cccab3tw1esjmcag9tjg20dw0644nc.jpg").setSpan(4).setLayoutparams(Card.NOSET,400));
		for(int i=9;i<100;i++) {
			list.add(new CardImage("ASSETS:"+i+".gif").setSpan(4));
		}
//
//		list.add(new CardImage("ASSETS:ezgif.gif").setSpan(2));



		for(int i=0;i<8;i++) {
			list.add(new AutoCard(R.layout.item_text,new Msg_Key("code"+i,i+"http://ww3.sinaimg.cn/large/85cccab3gw4etdg40apw5g40dw0794kx.jpg"),null).setSpan(4));
			list.add(new AutoCard(R.layout.item_text,new Msg_Key("code"+i,i+"http://ww3.sinaimg.cn/large/85cccab3gw4etdg40apw5g40dw0794kx.jpg"),null).setSpan(4));
		}

		list.add(new CardImage("http://ww3.sinaimg.cn/large/85cccab3gw4etetge3cmig40r30a34qr.jpg").setSpan(2));
		list.add(new CardImage("http://ww3.sinaimg.cn/large/85cccab3gw4etdg40apw5g40dw0794kx.jpg").setSpan(2));
		list.add(new CardImage("http://ww3.sinaimg.cn/large/85cccab3gw4etetge3cmig40r30a34qr.jpg").setSpan(2));
		list.add(new CardImage("http://ww3.sinaimg.cn/large/85cccab3gw4etdg40apw5g40dw0794kx.jpg").setSpan(2));



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
