package com.example.autotest.ada;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.example.autotest.card.CardSimple;
import com.example.autotest.item.Simple;
import com.mdx.framework.adapter.MAdapter;

import java.util.List;

/**
 * Created by ryan on 2017/6/7.
 */

public class TAdapter extends MAdapter<CardSimple>{
    public TAdapter(Context context, List list, int Resoure) {
        super(context, list, Resoure);
    }

    public TAdapter(Context context, List list) {
        super(context, list);
    }

    @Override
    public View getview(int position, View convertView, ViewGroup parent) {
        CardSimple item = get(position);
        if (convertView == null) {
            Simple viewHolder=Simple.getView(getContext(), parent);
            viewHolder.itemView.setTag(viewHolder);
            convertView = viewHolder.itemView;
        }
        Simple mMain=(Simple) convertView.getTag();
        mMain.set(position,item);
        return convertView;
    }


}
