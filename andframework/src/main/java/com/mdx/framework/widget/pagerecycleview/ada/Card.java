/*
 * 文件名: Card.java
 * 版    权：  Copyright XCDS Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: ryan
 * 创建时间:2014年6月27日
 *
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.mdx.framework.widget.pagerecycleview.ada;

import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;

import com.mdx.framework.autofit.commons.AutoParams;
import com.mdx.framework.widget.pagerecycleview.autofit.AutoMViewHold;
import com.mdx.framework.widget.pagerecycleview.viewhold.HasVHparam;
import com.mdx.framework.widget.pagerecycleview.viewhold.MViewHold;
import com.mdx.framework.widget.pagerecycleview.viewhold.ViewHodeParam;

import java.util.HashMap;
import java.util.Map;


/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 *
 * @author ryan
 * @version [2014年6月27日 下午2:00:07]
 */
public abstract class Card<T> implements HasVHparam {
    public static final int NOSET = -99;

    protected int type = 0, span = 1;
    protected int resid = 0;
    protected MAdapter mAdapter;
    public ViewHodeParam viewHodeParam;
    public BaseAdapter oAdapter;
    public boolean isanimation = false;
    public boolean useanimation = true;
    public boolean reanimation = false;
    public MViewHold overViewHold;
    public RecyclerView.ViewHolder viewHold;
    public int visibility = View.VISIBLE;
    public int state = 0;
    public T item, lastitem;
    public int itemposion = 0;
    public boolean checked = false;
    public AutoParams params = new AutoParams();


    public void setItem(T item) {
        this.lastitem = this.item;
        this.item = item;
    }

    public MAdapter getAdapter() {
        return mAdapter;
    }

    public Card<T> setAdapter(MAdapter adapter) {
        this.mAdapter = adapter;
        return this;
    }

    public int getSpan() {
        return span <= 1 ? 1 : span;
    }

    public Card<T> setSpan(int span) {
        this.span = span;
        return this;
    }


    public int getCardType() {
//        if (viewHodeParam != null && viewHodeParam.showType == 1) {
//            return -88;
//        }
        return type;
    }

    public int getResId() {
        return resid;
    }

    public void dispbind(RecyclerView.ViewHolder viewHolder, int posion) {
        setLayoutpareams(viewHolder);
        bind(viewHolder, posion);
        if (viewHolder instanceof MViewHold) {
            ((MViewHold) viewHolder).viewHodeParam = viewHodeParam;
            ((MViewHold) viewHolder).card = this;
            ((MViewHold) viewHolder).posion = posion;
            ((MViewHold) viewHolder).card.isanimation = isanimation;
            if (viewHodeParam != null) {
                if (viewHodeParam.showType == 1) {
                    bindOver(posion);
                }
            }
        }
        if (viewHodeParam != null && viewHodeParam.showType == 1) {
            viewHolder.itemView.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.itemView.setVisibility(this.visibility);
        }

    }

    public int getShowType() {
        if (viewHodeParam != null) {
            return viewHodeParam.showType;
        }
        return 0;
    }

    public void setLayoutpareams(RecyclerView.ViewHolder viewHolder) {
        ViewGroup.LayoutParams layoutParams = viewHolder.itemView.getLayoutParams();
        boolean isnset = false;
        if (layoutParams != null) {
            layoutParams = new RecyclerView.LayoutParams(layoutParams);
        } else {
            layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        if (viewHodeParam != null) {
            if (viewHodeParam.width != NOSET) {
                layoutParams.width = viewHodeParam.width;
                isnset = true;
            }
            if (viewHodeParam.height != NOSET) {
                layoutParams.height = viewHodeParam.height;
                isnset = true;
            }
        }
        if (isnset) {
            viewHolder.itemView.setLayoutParams(layoutParams);
        } else {
            if (viewHolder instanceof MViewHold) {
                if (layoutParams != ((MViewHold) viewHolder).layoutParams) {
                    viewHolder.itemView.setLayoutParams(((MViewHold) viewHolder).layoutParams);
                }
            }
        }

    }

    public void bindOver(int posion) {
        if (overViewHold == null) {
            if (resid != 0) {
                overViewHold = AutoMViewHold.getView(mAdapter.getContext(), mAdapter.recyclerView, resid);
            } else {
                overViewHold = CardIDS.CreateViewHolde(type, mAdapter.getContext(), mAdapter.recyclerView);
            }
            setLayoutpareams(overViewHold);
            overViewHold.viewHodeParam = viewHodeParam;
            overViewHold.card = this;
            overViewHold.posion = posion;
            bind(overViewHold, posion);
            if (overViewHold.itemView.getLayoutParams() == null) {
                overViewHold.itemView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            } else {
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(overViewHold.itemView.getLayoutParams().width, overViewHold.itemView.getLayoutParams().height);
                overViewHold.itemView.setLayoutParams(layoutParams);
            }
        }
    }

    public abstract void bind(RecyclerView.ViewHolder viewHolder, int posion);

    /**
     * @param type 1悬浮
     * @return
     */
    public Card setShowType(int type) {
        if (viewHodeParam == null) {
            viewHodeParam = new ViewHodeParam();
        }
        this.useanimation = false;
        int nowtype = getShowType();
        viewHodeParam.showType = type;
        if ((type == 1 || nowtype == 1) && type != nowtype && mAdapter != null) {
            mAdapter.resetOverCard();
        }
        return this;
    }

    public Card setLayoutparams(int width, int height) {
        if (viewHodeParam == null) {
            viewHodeParam = new ViewHodeParam();
        }
        this.viewHodeParam.width = width;
        this.viewHodeParam.height = height;
        return this;
    }

    @Override
    public ViewHodeParam getViewHodeParam() {
        return viewHodeParam;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}