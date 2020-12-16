/*
 * 文件名: MPageListView.java 版 权： Copyright XCDS Tech. Co. Ltd. All Rights
 * Reserved. 描 述: [该类的简要描述] 创建人: ryan 创建时间:2014-4-11
 * 
 * 修改人： 修改时间: 修改内容：[修改内容]
 */
package com.mdx.framework.widget;

import com.mdx.framework.adapter.MAdapter;
import com.mdx.framework.server.api.Son;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;

public class MNPageListView extends MPageListView {
    public OnTopViewLoad onTopViewLoad;
    
    public boolean isInTop = false;
    
    public boolean canTopPage = true;
    
    public MNPageListView(Context context) {
        super(context);
    }
    
    public MNPageListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public MNPageListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }
    
    public void setLoadView(View mFootShowView) {
    }
    
    public void getMessage(Son son) {
        MAdapter<?> adapter = mDataFormat.getAdapter(getContext(), son, page);
        if (adapter != null) {
            addData(adapter);
        }
        // if (son.getError() == 0) {
        if (!mDataFormat.hasNext()) {
            havepage = false;
        } else {
            havepage = true;
        }
        // } else {
        // addData(null);
        // this.endPage();
        // }
        if (mOnDataLoaded != null) {
            mOnDataLoaded.onDataLoaded(son);
        }
        isLoading = false;
    }
    
    public void addData(MAdapter<?> list) {
        page++;
        if (reload) {
            reload = false;
            setAdapter(list);
            return;
        }
        MAdapter<?> adapter = null;
        ListAdapter hvla = getAdapter();
        if (hvla == null) {
            setAdapter(list);
            hvla = list;
        }
        if (hvla instanceof MAdapter) {
            adapter = (MAdapter<?>) hvla;
        } else {
            HeaderViewListAdapter hvlb = (HeaderViewListAdapter) hvla;
            if (hvlb.getWrappedAdapter() instanceof MAdapter<?>) {
                adapter = (MAdapter<?>) hvlb.getWrappedAdapter();
            }
            if (adapter == null) {
                setAdapter(list);
                return;
            }
        }
        int ind = 0;
        boolean ismove = false;
        if (getFirstVisiblePosition() <= 0) {
            ismove = true;
            for (int i = 0; i < getHeaderViewsCount(); i++) {
                ind += getChildAt(i).getMeasuredHeight();
            }
        }
        adapter.AddAllOnBegin(list);
        if (ismove && list.getCount() > 0) {
            this.setSelectionFromTop(list.getCount() + this.getHeaderViewsCount(), ind);
        }
    }
    
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (canTopPage) {
            if (firstVisibleItem == 0 && havepage && !isLoading) {
                View lv = getChildAt(0);
                if (lv != null && lv.getTop() == 0) {
                    loadata();
                }
            }
        }
    }
    
    private synchronized void loadata() {
        isLoading = true;
        loadApiFrom(page);
    }
    
    public void pshow() {
        super.pshow();
        isLoading = true;
    }
    
    public void pdismiss() {
        super.pdismiss();
        isLoading = false;
    }
    
    public interface OnTopViewLoad {
        public void onTopViewLoad();
    }
    
    public void setOnTopViewLoad(OnTopViewLoad onTopViewLoad) {
        this.onTopViewLoad = onTopViewLoad;
    }
}
