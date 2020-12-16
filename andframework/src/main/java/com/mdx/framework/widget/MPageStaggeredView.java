package com.mdx.framework.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.WrapperListAdapter;
import com.mdx.framework.adapter.MAdapter;
import com.mdx.framework.adapter.NullAdapter;
import com.mdx.framework.server.api.ApiUpdate;
import com.mdx.framework.server.api.Son;
import com.mdx.framework.widget.staggered.StaggeredGridView;
import com.mdx.framework.widget.util.DataFormat;
import com.mdx.framework.widget.util.MScrollAble;

public class MPageStaggeredView extends StaggeredGridView implements MScrollAble, AbsListView.OnScrollListener {
    protected View mFootShowView;
    
    protected boolean havepage = false;
    
    protected boolean scrollAble = true,InterceptAble=false;
    
    protected boolean isLoading = false;
    
    protected boolean reload = true;
    
    protected ApiUpdate mApiUpdate;
    
    protected DataFormat mDataFormat;
    
    protected int page = 1;
    
    private boolean useCache = true;
    
    private boolean oneUseCaches = true;
    
    protected MListView.OnDataLoaded mOnDataLoaded;
    
    protected MListView.OnReLoad onReLoad;
    
    private OnScrollListener mScrollListener;
    
    public MPageStaggeredView(Context context) {
        super(context);
        setLoadView(new FootLoadingView(getContext()));
        super.setOnScrollListener(this);
    }
    
    public MPageStaggeredView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLoadView(new FootLoadingView(getContext()));
        super.setOnScrollListener(this);
    }
    
    public MPageStaggeredView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setLoadView(new FootLoadingView(getContext()));
        super.setOnScrollListener(this);
    }
    
    public DataFormat getDataFormat() {
        return this.mDataFormat;
    }
    
    public void setDataFormat(DataFormat dataFormat) {
        this.mDataFormat = dataFormat;
    }
    
    public void setApiUpdate(ApiUpdate api) {
        this.mApiUpdate = api;
        this.mApiUpdate.setContext(getContext());
        this.mApiUpdate.setParent(this);
        this.mApiUpdate.setHavaPage(true);
        this.useCache = this.mApiUpdate.isSaveAble();
        this.mApiUpdate.setMethod("getMessage");
    }
    
    @SuppressLint({ "NewApi" })
    public void loadApiFrom(long page) {
        if (this.mApiUpdate == null) {
            return;
        }
        if (getAdapter() == null) {
            setAdapter(new NullAdapter(getContext()));
        }
        if (!this.havepage) {
            this.oneUseCaches = this.useCache;
        }
        this.mApiUpdate.setPage(page);
        String[][] params = this.mDataFormat.getPageNext();
        if (params != null) {
            this.mApiUpdate.setPageParams(params);
        }
        this.mApiUpdate.setSaveAble(this.oneUseCaches);
        if (this.mApiUpdate.getUpdateOne() == null) {
            throw new IllegalAccessError("no updateone exit");
        }
        this.mApiUpdate.loadUpdateOne();
    }
    
    public void loadUpdate(ApiUpdate api) {
        setApiUpdate(api);
        loadApiFrom(1L);
    }
    
    protected void startLoad() {
        this.isLoading = true;
    }
    
    protected void endLoad() {
        this.isLoading = false;
    }
    
    public void reload() {
        this.page = 1;
        this.reload = true;
        this.havepage = false;
        this.isLoading = true;
        this.mDataFormat.reload();
        loadApiFrom(1L);
        if (this.mOnDataLoaded != null) {
            this.mOnDataLoaded.onReload();
        }
    }
    
    public void reloadWithOutCatch() {
        if (this.onReLoad != null) {
            if (this.onReLoad.onReLoad()) {
                this.page = this.onReLoad.getPage();
                loadApiFrom(this.page);
            } else {
                this.oneUseCaches = false;
                reload();
            }
        } else {
            this.oneUseCaches = false;
            reload();
        }
    }
    
    public void setLoadView(View mFootShowView) {
        if (this.mFootShowView != null) {
            hideFooter();
            this.mFootShowView = null;
        }
        this.mFootShowView = mFootShowView;
    }
    
    public void setOnReLoad(MListView.OnReLoad onReLoad) {
        this.onReLoad = onReLoad;
    }
    
    public void showFooter() {
        if ((getFooterViewsCount() == 0) && (this.mFootShowView != null)) {
            try {
                this.mFootShowView.setVisibility(View.VISIBLE);
                addFooterView(this.mFootShowView);
            }
            catch (Exception localException) {}
        }
    }
    
    public void hideFooter() {
        if ((getFooterViewsCount() > 0) && (this.mFootShowView != null)) {
            try {
                this.mFootShowView.setVisibility(View.GONE);
                removeFooterView(this.mFootShowView);
            }
            catch (Exception localException) {}
        }
    }
    
    @SuppressLint({ "NewApi" })
    public void addData(MAdapter<?> list) {
        this.isLoading = false;
        this.page += 1;
        if (this.reload) {
            this.reload = false;
            setAdapter(list);
            return;
        }
        MAdapter<?> adapter = null;
        ListAdapter hvla = getAdapter();
        if ((hvla == null) || ((adapter instanceof NullAdapter))) {
            setAdapter(list);
            hvla = list;
        }
        if ((hvla instanceof MAdapter)) {
            adapter = (MAdapter<?>) hvla;
        } else {
            WrapperListAdapter hvlb = (WrapperListAdapter) hvla;
            if ((hvlb.getWrappedAdapter() instanceof MAdapter)) {
                adapter = (MAdapter<?>) hvlb.getWrappedAdapter();
            }
            if ((adapter == null) || ((adapter instanceof NullAdapter))) {
                setAdapter(list);
                return;
            }
        }
        adapter.AddAll(list);
    }
    
    public void getMessage(Son son) {
        MAdapter<?> adapter = this.mDataFormat.getAdapter(getContext(), son, this.page);
        if (adapter != null) {
            addData(adapter);
        }
        if (!this.mDataFormat.hasNext()) {
            endPage();
        } else {
            this.havepage = true;
            showFooter();
        }
        if (this.mOnDataLoaded != null) {
            this.mOnDataLoaded.onDataLoaded(son);
        }
    }
    
    public void endPage() {
        hideFooter();
        this.havepage = false;
        this.isLoading = false;
    }
    
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (this.mScrollListener != null) {
            this.mScrollListener.onScrollStateChanged(view, scrollState);
        }
    }
    
    public void setOnScrollListener(OnScrollListener scrollListener) {
        this.mScrollListener = scrollListener;
    }
    
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if ((firstVisibleItem + visibleItemCount == totalItemCount) && (this.havepage) && (!this.isLoading)) {
            int pos = getLastVisiblePosition();
            View lv = getChildAt(pos - firstVisibleItem);
            if ((lv != null) && (lv.getTop() <= getHeight() - getPaddingBottom())) {
                loadata();
            }
        }
        if (!this.havepage) {
            hideFooter();
        } else {
            showFooter();
        }
    }
    
    private synchronized void loadata() {
        this.isLoading = true;
        loadApiFrom(this.page);
    }
    
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (this.scrollAble) {
            return super.onInterceptTouchEvent(ev);
        }
        return InterceptAble;
    }
    
    public void setScrollAble(boolean bol) {
        this.scrollAble = bol;
    }
    
    public boolean isScrollAble() {
        return this.scrollAble;
    }

    /**
     * [一句话功能简述]<BR>
     * [功能详细描述]
     * @param bol
     * @see MScrollAble#setIntercept(boolean)
     */
    
    @Override
    public void setIntercept(boolean bol) {
        InterceptAble=bol;
    }
    
    
}
