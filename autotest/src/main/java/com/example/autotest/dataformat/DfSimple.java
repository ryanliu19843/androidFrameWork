//
//  DfSimple
//
//  Created by ryan on 2017-05-09 14:53:09
//  Copyright (c) ryan All rights reserved.


/**
 *
 */

package com.example.autotest.dataformat;

import android.content.Context;

import com.example.autotest.card.CardSimple;
import com.example.autotest.item.Simple;
import com.mdx.framework.server.api.Son;
import com.mdx.framework.widget.pagerecycleview.ada.Card;
import com.mdx.framework.widget.pagerecycleview.ada.CardAdapter;
import com.mdx.framework.widget.util.DataFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DfSimple extends DataFormat {

    public static DfSimple getDF() {
        return new DfSimple();
    }

    int size = 1;

    Simple.SimpleSet simpleSets[] = new Simple.SimpleSet[]{
            new Simple.SimpleSet("ASSETS:tylt_bg_youdoushi_n.png", "ASSETS:led7.gif", "魔兽世界1", "110", "220", 0xffff0000, 0xffff0000),
            new Simple.SimpleSet("ASSETS:tylt_bg_moshou_n.png", "ASSETS:led7.gif", "魔兽世界2", "110", "220", 0xffffff00, 0xffffff00),
            new Simple.SimpleSet("ASSETS:tylt_bg_zhanzheng_n.png", "ASSETS:led7.gif", "魔兽世界3", "110", "220", 0xffffffff, 0xffffffff),
            new Simple.SimpleSet("ASSETS:tylt_bg_yingxiong_n.png", "ASSETS:anim_flag_chile.gif", "魔兽世界4", "110", "220", 0xff00ff00, 0xff00ff00),
            new Simple.SimpleSet("ASSETS:tylt_bg_yongheng_n.png", "ASSETS:anim_flag_england.gif", "魔兽世界5", "110", "220", 0xff0000ff, 0xff0000ff),
            new Simple.SimpleSet("ASSETS:tylt_bg_guaiwu_n.png", "ASSETS:anim_flag_greenland.gif", "魔兽世界6", "110", "220", 0xff00ffff, 0xff00ffff),
            new Simple.SimpleSet("ASSETS:tylt_bg_youdoushi_n.png", "ASSETS:anim_flag_iceland.gif", "魔兽世界7", "110", "220", 0xff0000ff, 0xff0000ff),
            new Simple.SimpleSet("ASSETS:tylt_bg_guaiwu_n.png", "ASSETS:tylt_ic_guaiwu_n.png", "魔兽世界12", "110", "220", 0xffff0000, 0xffff0000),
            new Simple.SimpleSet("ASSETS:tylt_bg_youdoushi_n.png", "ASSETS:tylt_ic_youdoushi_n.png", "魔兽世界13", "110", "220", 0xffff0000, 0xffff0000),
            new Simple.SimpleSet("ASSETS:tylt_bg_moshou_n.png", "ASSETS:tylt_ic_moshou_n.png", "魔兽世界14", "110", "220", 0xffff0000, 0xffff0000),
            new Simple.SimpleSet("ASSETS:tylt_bg_zhanzheng_n.png", "ASSETS:tylt_ic_zhenzheng_n.png", "魔兽世界15", "110", "220", 0xffff0000, 0xffff0000),
            new Simple.SimpleSet("ASSETS:tylt_bg_yingxiong_n.png", "ASSETS:tylt_ic_yingxiong_n.png", "魔兽世界16", "110", "220", 0xffff0000, 0xffff0000),
            new Simple.SimpleSet("ASSETS:tylt_bg_yongheng_n.png", "ASSETS:tylt_ic_yongheng_n.png", "魔兽世界17", "110", "220", 0xffff0000, 0xffff0000),
            new Simple.SimpleSet("ASSETS:tylt_bg_guaiwu_n.png", "ASSETS:tylt_ic_guaiwu_n.png", "魔兽世界18", "110", "220", 0xffff0000, 0xffff0000),

            new Simple.SimpleSet("ASSETS:tylt_bg_youdoushi_n.png", "ASSETS:led7.gif", "魔兽世界1", "110", "220", 0xffff0000, 0xffff0000),
            new Simple.SimpleSet("ASSETS:tylt_bg_moshou_n.png", "ASSETS:led7.gif", "魔兽世界2", "110", "220", 0xffffff00, 0xffffff00),
            new Simple.SimpleSet("ASSETS:tylt_bg_zhanzheng_n.png", "ASSETS:led7.gif", "魔兽世界3", "110", "220", 0xffffffff, 0xffffffff),
            new Simple.SimpleSet("ASSETS:tylt_bg_yingxiong_n.png", "ASSETS:anim_flag_chile.gif", "魔兽世界4", "110", "220", 0xff00ff00, 0xff00ff00),
            new Simple.SimpleSet("ASSETS:tylt_bg_yongheng_n.png", "ASSETS:anim_flag_england.gif", "魔兽世界5", "110", "220", 0xff0000ff, 0xff0000ff),
            new Simple.SimpleSet("ASSETS:tylt_bg_guaiwu_n.png", "ASSETS:anim_flag_greenland.gif", "魔兽世界6", "110", "220", 0xff00ffff, 0xff00ffff),
            new Simple.SimpleSet("ASSETS:tylt_bg_youdoushi_n.png", "ASSETS:anim_flag_iceland.gif", "魔兽世界7", "110", "220", 0xff0000ff, 0xff0000ff),
            new Simple.SimpleSet("ASSETS:tylt_bg_guaiwu_n.png", "ASSETS:tylt_ic_guaiwu_n.png", "魔兽世界12", "110", "220", 0xffff0000, 0xffff0000),
            new Simple.SimpleSet("ASSETS:tylt_bg_youdoushi_n.png", "ASSETS:tylt_ic_youdoushi_n.png", "魔兽世界13", "110", "220", 0xffff0000, 0xffff0000),
            new Simple.SimpleSet("ASSETS:tylt_bg_moshou_n.png", "ASSETS:tylt_ic_moshou_n.png", "魔兽世界14", "110", "220", 0xffff0000, 0xffff0000),
            new Simple.SimpleSet("ASSETS:tylt_bg_zhanzheng_n.png", "ASSETS:tylt_ic_zhenzheng_n.png", "魔兽世界15", "110", "220", 0xffff0000, 0xffff0000),
            new Simple.SimpleSet("ASSETS:tylt_bg_yingxiong_n.png", "ASSETS:tylt_ic_yingxiong_n.png", "魔兽世界16", "110", "220", 0xffff0000, 0xffff0000),
            new Simple.SimpleSet("ASSETS:tylt_bg_yongheng_n.png", "ASSETS:tylt_ic_yongheng_n.png", "魔兽世界17", "110", "220", 0xffff0000, 0xffff0000),
            new Simple.SimpleSet("ASSETS:tylt_bg_guaiwu_n.png", "ASSETS:tylt_ic_guaiwu_n.png", "魔兽世界18", "110", "220", 0xffff0000, 0xffff0000),

            new Simple.SimpleSet("ASSETS:tylt_bg_youdoushi_n.png", "ASSETS:led7.gif", "魔兽世界1", "110", "220", 0xffff0000, 0xffff0000),
            new Simple.SimpleSet("ASSETS:tylt_bg_moshou_n.png", "ASSETS:led7.gif", "魔兽世界2", "110", "220", 0xffffff00, 0xffffff00),
            new Simple.SimpleSet("ASSETS:tylt_bg_zhanzheng_n.png", "ASSETS:led7.gif", "魔兽世界3", "110", "220", 0xffffffff, 0xffffffff),
            new Simple.SimpleSet("ASSETS:tylt_bg_yingxiong_n.png", "ASSETS:anim_flag_chile.gif", "魔兽世界4", "110", "220", 0xff00ff00, 0xff00ff00),
            new Simple.SimpleSet("ASSETS:tylt_bg_yongheng_n.png", "ASSETS:anim_flag_england.gif", "魔兽世界5", "110", "220", 0xff0000ff, 0xff0000ff),
            new Simple.SimpleSet("ASSETS:tylt_bg_guaiwu_n.png", "ASSETS:anim_flag_greenland.gif", "魔兽世界6", "110", "220", 0xff00ffff, 0xff00ffff),
            new Simple.SimpleSet("ASSETS:tylt_bg_youdoushi_n.png", "ASSETS:anim_flag_iceland.gif", "魔兽世界7", "110", "220", 0xff0000ff, 0xff0000ff),
            new Simple.SimpleSet("ASSETS:tylt_bg_guaiwu_n.png", "ASSETS:tylt_ic_guaiwu_n.png", "魔兽世界12", "110", "220", 0xffff0000, 0xffff0000),
            new Simple.SimpleSet("ASSETS:tylt_bg_youdoushi_n.png", "ASSETS:tylt_ic_youdoushi_n.png", "魔兽世界13", "110", "220", 0xffff0000, 0xffff0000),
            new Simple.SimpleSet("ASSETS:tylt_bg_moshou_n.png", "ASSETS:tylt_ic_moshou_n.png", "魔兽世界14", "110", "220", 0xffff0000, 0xffff0000),
            new Simple.SimpleSet("ASSETS:tylt_bg_zhanzheng_n.png", "ASSETS:tylt_ic_zhenzheng_n.png", "魔兽世界15", "110", "220", 0xffff0000, 0xffff0000),
            new Simple.SimpleSet("ASSETS:tylt_bg_yingxiong_n.png", "ASSETS:tylt_ic_yingxiong_n.png", "魔兽世界16", "110", "220", 0xffff0000, 0xffff0000),
            new Simple.SimpleSet("ASSETS:tylt_bg_yongheng_n.png", "ASSETS:tylt_ic_yongheng_n.png", "魔兽世界17", "110", "220", 0xffff0000, 0xffff0000),
            new Simple.SimpleSet("ASSETS:tylt_bg_guaiwu_n.png", "ASSETS:tylt_ic_guaiwu_n.png", "魔兽世界18", "110", "220", 0xffff0000, 0xffff0000),

            new Simple.SimpleSet("ASSETS:tylt_bg_youdoushi_n.png", "ASSETS:led7.gif", "魔兽世界1", "110", "220", 0xffff0000, 0xffff0000),
            new Simple.SimpleSet("ASSETS:tylt_bg_moshou_n.png", "ASSETS:led7.gif", "魔兽世界2", "110", "220", 0xffffff00, 0xffffff00),
            new Simple.SimpleSet("ASSETS:tylt_bg_zhanzheng_n.png", "ASSETS:led7.gif", "魔兽世界3", "110", "220", 0xffffffff, 0xffffffff),
            new Simple.SimpleSet("ASSETS:tylt_bg_yingxiong_n.png", "ASSETS:anim_flag_chile.gif", "魔兽世界4", "110", "220", 0xff00ff00, 0xff00ff00),
            new Simple.SimpleSet("ASSETS:tylt_bg_yongheng_n.png", "ASSETS:anim_flag_england.gif", "魔兽世界5", "110", "220", 0xff0000ff, 0xff0000ff),
            new Simple.SimpleSet("ASSETS:tylt_bg_guaiwu_n.png", "ASSETS:anim_flag_greenland.gif", "魔兽世界6", "110", "220", 0xff00ffff, 0xff00ffff),
            new Simple.SimpleSet("ASSETS:tylt_bg_youdoushi_n.png", "ASSETS:anim_flag_iceland.gif", "魔兽世界7", "110", "220", 0xff0000ff, 0xff0000ff),
            new Simple.SimpleSet("ASSETS:tylt_bg_guaiwu_n.png", "ASSETS:tylt_ic_guaiwu_n.png", "魔兽世界12", "110", "220", 0xffff0000, 0xffff0000),
            new Simple.SimpleSet("ASSETS:tylt_bg_youdoushi_n.png", "ASSETS:tylt_ic_youdoushi_n.png", "魔兽世界13", "110", "220", 0xffff0000, 0xffff0000),
            new Simple.SimpleSet("ASSETS:tylt_bg_moshou_n.png", "ASSETS:tylt_ic_moshou_n.png", "魔兽世界14", "110", "220", 0xffff0000, 0xffff0000),
            new Simple.SimpleSet("ASSETS:tylt_bg_zhanzheng_n.png", "ASSETS:tylt_ic_zhenzheng_n.png", "魔兽世界15", "110", "220", 0xffff0000, 0xffff0000),
            new Simple.SimpleSet("ASSETS:tylt_bg_yingxiong_n.png", "ASSETS:tylt_ic_yingxiong_n.png", "魔兽世界16", "110", "220", 0xffff0000, 0xffff0000),
            new Simple.SimpleSet("ASSETS:tylt_bg_yongheng_n.png", "ASSETS:tylt_ic_yongheng_n.png", "魔兽世界17", "110", "220", 0xffff0000, 0xffff0000),
            new Simple.SimpleSet("ASSETS:tylt_bg_guaiwu_n.png", "ASSETS:tylt_ic_guaiwu_n.png", "魔兽世界18", "110", "220", 0xffff0000, 0xffff0000),

    };


    @Override
    public boolean hasNext() {
        return true;
    }

    public CardAdapter getCardAdapter(Context context, Son son, int page) {
        List<Card> list = new ArrayList<>();
        int ind = Math.abs(new Random().nextInt()) % 5;
        for (int k = 0; k < 5; k++) {
            for (int i = ind; i < simpleSets.length; i++) {

                Simple.SimpleSet item = simpleSets[i];
                CardSimple card = new CardSimple(item);
                list.add(card);
            }
        }
        return new CardAdapter(context, list);
    }

    @Override
    public String[][] getPageNext() {
        return null;
    }

    @Override
    public void reload() {

    }
}
