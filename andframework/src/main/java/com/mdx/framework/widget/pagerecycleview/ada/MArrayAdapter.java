package com.mdx.framework.widget.pagerecycleview.ada;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.mdx.framework.R;
import com.mdx.framework.adapter.*;

/**
 * Created by ryan on 2017/11/10.
 */

public class MArrayAdapter extends MBaseAdapter {
    public CardAdapter cardAdapter;

    public MArrayAdapter(Context context,CardAdapter cardAdapter){
        this.cardAdapter=cardAdapter;
    }

    public View getview(int position, View convertView, ViewGroup parent) {
        RecyclerView.ViewHolder viewHolder = null;
        if(convertView!=null){
            viewHolder= (RecyclerView.ViewHolder) convertView.getTag(R.id.viewholdtag);
            cardAdapter.onBindViewHolder(viewHolder, position);

        }else{
            int viewtype = cardAdapter.getItemViewType(position);
            viewHolder=cardAdapter.onCreateViewHolder(parent,viewtype);
            viewHolder.itemView.setTag(R.id.viewholdtag,viewHolder);
            cardAdapter.onBindViewHolder(viewHolder, position);
        }
        return viewHolder.itemView;
    }

    @Override
    public int getCount() {
        return cardAdapter.getItemCount();
    }

    @Override
    public Object getItem(int position) {
        return cardAdapter.get(position);
    }

    @Override
    public long getItemId(int position) {
        return cardAdapter.getItemId(position);
    }

    public int getItemViewType(int position) {
        return cardAdapter.getItemViewType(position);
    }

}
