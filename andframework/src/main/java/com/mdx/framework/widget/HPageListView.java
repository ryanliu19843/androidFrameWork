/*
 * 文件名: HPageListView.java 版 权： Copyright XCDS Tech. Co. Ltd. All Rights
 * Reserved. 描 述: [该类的简要描述] 创建人: ryan 创建时间:2014年5月4日
 * 
 * 修改人： 修改时间: 修改内容：[修改内容]
 */
package com.mdx.framework.widget;

import com.mdx.framework.adapter.MAdapter;
import com.mdx.framework.adapter.NullAdapter;
import com.mdx.framework.server.api.ApiUpdate;
import com.mdx.framework.server.api.Son;
import com.mdx.framework.widget.MListView.OnDataLoaded;
import com.mdx.framework.widget.util.DataFormat;
import com.mdx.framework.widget.util.MScrollAble;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 * 
 * @author ryan
 * @version [2014年5月4日 下午4:40:19]
 */
public class HPageListView extends HorizontalListView implements MScrollAble, ListView.OnScrollListener {
    
    protected boolean scrollAble = true, isLoading = false, reload = true,InterceptAble=true;
    
    protected View mFootShowView;
    
    protected boolean havepage = false;
    
    protected ApiUpdate mApiUpdate;
    
    private ListView.OnScrollListener mScrollListener;
    
    protected DataFormat mDataFormat;
    
    protected int page = 1;
    
    private boolean useCache = true, oneUseCaches = true;
    
    protected OnDataLoaded mOnDataLoaded;
    
    public HPageListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    
    public HPageListView(Context context) {
        super(context);
        init(context);
    }
    
    public void setApiUpdate(ApiUpdate api) {
        this.mApiUpdate = api;
        this.mApiUpdate.setContext(getContext());
        this.mApiUpdate.setParent(this);
        this.mApiUpdate.setHavaPage(true);
        useCache = this.mApiUpdate.isSaveAble();
        this.mApiUpdate.setMethod("getMessage");
    }
    
    public void loadApiFrom(long page) {
        if (this.mApiUpdate == null) {
            return;
        }
        if(getAdapter()==null){
            this.setAdapter(new NullAdapter(getContext()));
        }
        if(!havepage){
            oneUseCaches=useCache;
        }
        this.mApiUpdate.setPage(page);
        String[][] params=this.mDataFormat.getPageNext();
        if(params!=null){
            this.mApiUpdate.setPageParams(params);
        }
        this.mApiUpdate.setSaveAble(oneUseCaches);
        if (this.mApiUpdate.getUpdateOne() == null) {
            throw new IllegalAccessError("no updateone exit");
        } else {
            this.mApiUpdate.loadUpdateOne();
        }
    }
    
    public void loadUpdate(ApiUpdate api) {
        setApiUpdate(api);
        loadApiFrom(1);
    }
    
    public DataFormat getDataFormat() {
        return mDataFormat;
    }
    
    public void setDataFormat(DataFormat dataFormat) {
        this.mDataFormat = dataFormat;
    }
    
    public void getMessage(Son son) {
        MAdapter<?> adapter = mDataFormat.getAdapter(getContext(), son, page);
        if (adapter != null) {
            addData(adapter);
        }
        if (son.getError() == 0) {
            if (!mDataFormat.hasNext()) {
                this.endPage();
            } else {
                havepage = true;
                this.showFooter();
            }
        } else {
            addData(null);
            this.endPage();
        }
        if(mOnDataLoaded!=null){
            mOnDataLoaded.onDataLoaded(son);
        }
    }
    
    protected void startLoad() {
        isLoading = true;
    }
    
    protected void endLoad() {
        isLoading = false;
    }
    
    public void reload() {
        page = 1;
        reload = true;
        havepage = false;
        isLoading = true;
        loadApiFrom(1);
        if (this.mOnDataLoaded != null) {
            this.mOnDataLoaded.onReload();
        }
    }
    
    public void reloadWithOutCatch() {
        this.oneUseCaches = false;
        reload();
    }
    
    public void showFooter() {
    }
    
    public void hideFooter() {
    }
    
    public ListAdapter getListAdapter() {
        ListAdapter hvla = getAdapter();
        if (hvla == null) {
            return null;
        }
        if (hvla instanceof MAdapter<?>) {
            return hvla;
        } else if (hvla instanceof HeaderViewListAdapter) {
            HeaderViewListAdapter hvlb = (HeaderViewListAdapter) hvla;
            return (ListAdapter) hvlb.getWrappedAdapter();
        } else {
            return hvla;
        }
    }
    
    public void endPage() {
        this.hideFooter();
        havepage = false;
        isLoading = false;
    }
    
    public void addData(MAdapter<?> list) {
        isLoading = false;
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
        adapter.AddAll(list);
    }
    
    protected void init(Context context) {
        super.setOnScrollListener(this);
    }
    
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (this.mScrollListener != null) {
            this.mScrollListener.onScrollStateChanged(view, scrollState);
        }
    }
    
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (this.mScrollListener != null) {
            this.mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
        if (firstVisibleItem + visibleItemCount == totalItemCount && havepage && !isLoading) {
            int pos = getLastVisiblePosition();
            View lv = getChildAt(pos - firstVisibleItem);
            if (lv != null && lv.getTop() <= getHeight()) {
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
    
    public void setOnScrollListener(ListView.OnScrollListener scrollListener) {
        this.mScrollListener = scrollListener;
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (scrollAble) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return InterceptAble;
        }
    }
    
    @Override
    public void setScrollAble(boolean bol) {
        scrollAble = bol;
    }
    
    @Override
    public boolean isScrollAble() {
        return scrollAble;
    }

    public void setOnDataLoaded(OnDataLoaded onDataLoaded) {
        this.mOnDataLoaded = onDataLoaded;
    }

    
    @Override
    public void setIntercept(boolean bol) {
        this.InterceptAble=bol;
    }
    
    
}
