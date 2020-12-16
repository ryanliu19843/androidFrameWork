/*
 * 文件名: MPageListView.java 版 权： Copyright XCDS Tech. Co. Ltd. All Rights
 * Reserved. 描 述: [该类的简要描述] 创建人: ryan 创建时间:2014-4-11
 * 
 * 修改人： 修改时间: 修改内容：[修改内容]
 */
package com.mdx.framework.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;

import com.mdx.framework.adapter.MAdapter;
import com.mdx.framework.server.api.Son;

public class MPageListView extends MPullListView {
    
    public MPageListView(Context context) {
        super(context);
    }
    
    public MPageListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public MPageListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        super.onScrollStateChanged(view, scrollState);
    }
    
    public void getMessage(Son son) {
        MAdapter<?> adapter = mDataFormat.getAdapter(getContext(), son, page);
        if (adapter != null) {
            addData(adapter);
        }
        // if (son.getError() == 0) {
        if (!mDataFormat.hasNext()) {
            this.endPage();
        } else {
            havepage = true;
            this.showFooter();
        }
        // } else {
        // addData(null);
        // this.endPage();
        // }
        if (mOnDataLoaded != null) {
            mOnDataLoaded.onDataLoaded(son);
        }
    }
    
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        if (firstVisibleItem + visibleItemCount == totalItemCount && havepage && !isLoading) {
            int pos = getLastVisiblePosition();
            View lv = getChildAt(pos - firstVisibleItem);
            if (lv != null && lv.getTop() <= getHeight() - getPaddingBottom()-mfootHeight) {
                loadata();
            }
        }
        if (!havepage) {
            this.hideFooter();
        } else {
            this.showFooter();
        }
    }
    
    private synchronized void loadata() {
        isLoading = true;
        loadApiFrom(page);
    }
    
    public void pshow() {
        super.pshow(false);
        isLoading = true;
    }
    
    public void pdismiss() {
        super.pdismiss(false);
        isLoading = false;
    }
}
