package com.mdx.framework.widget.pagerecycleview.autofit;

import androidx.recyclerview.widget.RecyclerView;

import com.mdx.framework.autofit.AutoFitBase;
import com.mdx.framework.widget.pagerecycleview.ada.Card;
import com.mdx.framework.widget.pagerecycleview.viewhold.MViewHold;

/**
 * Created by ryan on 2017/10/17.
 */

public class AutoCard extends Card {
    public AutoFitBase autoFit;

    public AutoCard(int resId,Object item,AutoFitBase autoFit){
        this.setItem(item);
        this.autoFit=autoFit;
        this.resid=resId;
        this.type=resId;
    }

    @Override
    public void bind(RecyclerView.ViewHolder viewHolder, int posion) {
        MViewHold item = (MViewHold) viewHolder;
        if(item instanceof AutoMViewHold){
            ((AutoMViewHold) item).set(posion,this);
        }else {
            item.set(posion, this);
        }
        this.lastitem = null;
    }
}
