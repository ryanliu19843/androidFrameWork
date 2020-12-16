/*
 * 文件名: MBaseAdapter.java 版 权： Copyright XCDS Tech. Co. Ltd. All Rights
 * Reserved. 描 述: [该类的简要描述] 创建人: ryan 创建时间:2014-4-14
 * 
 * 修改人： 修改时间: 修改内容：[修改内容]
 */
package com.mdx.framework.adapter;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.SpinnerAdapter;

import com.mdx.framework.Frame;
import com.mdx.framework.R;

import java.util.HashMap;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 * 
 * @author ryan
 * @version [2014-4-14 上午10:01:04]
 */
public abstract class MBaseAdapter extends BaseAdapter implements ListAdapter, SpinnerAdapter {
    
    private final DataSetObservable mDataSetObservable = new DataSetObservable();

    protected long nowAnimiCurry = -1;
    public int mAnimation=R.anim.animation_list_enter;
    public boolean useAnmin = false;
    public HashMap<Object, Object> params=new HashMap<Object, Object>();

    public boolean hasStableIds() {
        return false;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.registerObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.unregisterObserver(observer);
    }

    /**
     * Notifies the attached observers that the underlying data has been changed
     * and any View reflecting the data set should refresh itself.
     */
    public void notifyDataSetChanged() {

        mDataSetObservable.notifyChanged();
    }

    /**
     * Notifies the attached observers that the underlying data is no longer
     * valid or available. Once invoked this adapter is no longer valid and
     * should not report further data set changes.
     */
    public void notifyDataSetInvalidated() {
        mDataSetObservable.notifyInvalidated();
    }

    public boolean areAllItemsEnabled() {
        return true;
    }

    public boolean isEnabled(int position) {
        return true;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    public int getItemViewType(int position) {
        return 0;
    }

    public int getViewTypeCount() {
        return 1;
    }

    public boolean isEmpty() {
        return getCount() == 0;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        if (useAnmin) {
            View v=getview(position, convertView, parent);
            if (position > nowAnimiCurry) {
                nowAnimiCurry = position;
                v.startAnimation(AnimationUtils.loadAnimation(Frame.CONTEXT, mAnimation));
            }
            return v;
        }
        return getview(position, convertView, parent);
    }
    
    public View getview(int position, View convertView, ViewGroup parent) {
        return null;
    }
    
    public void setUseAnmin(boolean useAnmin) {
        this.useAnmin = useAnmin;
    }
}