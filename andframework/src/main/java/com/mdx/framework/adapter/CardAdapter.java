/*
 * 文件名: CardAdapter.java 版 权： Copyright XCDS Tech. Co. Ltd. All Rights Reserved.
 * 描 述: [该类的简要描述] 创建人: ryan 创建时间:2014年6月27日
 * 
 * 修改人： 修改时间: 修改内容：[修改内容]
 */
package com.mdx.framework.adapter;

import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 * 
 * @author ryan
 * @version [2014年6月27日 下午1:32:54]
 */
public class CardAdapter extends MAdapter<Card<?>> {
    
    public CardAdapter(Context context, List<Card<?>> list) {
        super(context, list);
        init(context, list);
    }
    
    public CardAdapter(Context context, List<Card<?>> list, int Resoure) {
        super(context, list, Resoure);
        init(context, list);
    }
    
    @SuppressLint("UseSparseArrays")
    public void init(Context context, List<Card<?>> list) {
        resetGroup();
        this.setUseAnmin(true);
    }
    
    public void resetGroup() {
    }
    
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
    
    public int getItemViewType(int position) {
        return get(position).getCardType();
    }
    
    public int getViewTypeCount() {
        return 10000;
    }
    
    public View getview(int position, View convertView, ViewGroup parent) {
        Card<?> card = get(position);
        convertView = card.getView(getContext(), convertView);
        return convertView;
    }
}
