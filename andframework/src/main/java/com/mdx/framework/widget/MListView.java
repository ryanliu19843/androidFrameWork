/*
 * 文件名: MListView.java 版 权： Copyright XCDS Tech. Co. Ltd. All Rights Reserved. 描
 * 述: [该类的简要描述] 创建人: ryan 创建时间:2014-4-11
 * 
 * 修改人： 修改时间: 修改内容：[修改内容]
 */
package com.mdx.framework.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.HeaderViewListAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.mdx.framework.adapter.MAdapter;
import com.mdx.framework.adapter.NullAdapter;
import com.mdx.framework.server.api.ApiUpdate;
import com.mdx.framework.server.api.Son;
import com.mdx.framework.widget.util.DataFormat;
import com.mdx.framework.widget.util.MScrollAble;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 * 
 * @author ryan
 * @version [2014-4-11 上午8:54:19]
 */
public class MListView extends ListView implements MScrollAble, ListView.OnScrollListener {
    protected boolean scrollAble = true, isLoading = false, reload = true,InterceptAble=false;
    
    protected View mFootShowView;
    
    protected LinearLayout mFoot;
    
    private OnScrollListener mScrollListener;

    protected boolean havepage = false;

    protected ApiUpdate mApiUpdate;

    protected DataFormat mDataFormat;
    
    protected int page = 1, mfootHeight = 0;
    
    private boolean useCache = true, oneUseCaches = true;
    
    protected OnDataLoaded mOnDataLoaded;
    
    protected OnReLoad onReLoad;
    
    public MListView(Context context) {
        super(context);
        init(context);
    }
    
    public MListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    
    public MListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }
    
    protected void init(Context context) {
        super.setOnScrollListener(this);
        mFoot = new LinearLayout(context);
        this.addFooterView(mFoot);
        setLoadView(new FootLoadingView(context));
    }
    
    public void setPageAble(boolean bol) {
        if (bol) {
            this.havepage = true;
            showFooter();
        } else {
            endPage();
        }
    }
    
    public void addHeaderView(View v) {
        ListAdapter adapter = getAdapter();
        if (adapter != null) {
            setAdapter(null);
            super.addHeaderView(v);
            setAdapter(adapter);
        } else {
            super.addHeaderView(v);
        }
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
        if (getAdapter() == null) {
            this.setAdapter(new NullAdapter(getContext()));
        }
        if (!havepage) {
            oneUseCaches = useCache;
        }
        this.mApiUpdate.setPage(page);
        String[][] params = this.mDataFormat.getPageNext();
        if (params != null) {
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
        mDataFormat.reload();
        loadApiFrom(1);
        if (this.mOnDataLoaded != null) {
            this.mOnDataLoaded.onReload();
        }
    }
    
    public void reloadWithOutCatch() {
        if (onReLoad != null) {
            if (onReLoad.onReLoad()) {
                this.page = onReLoad.getPage();
                loadApiFrom(page);
            } else {
                this.oneUseCaches = false;
                reload();
            }
        } else {
            this.oneUseCaches = false;
            reload();
        }
    }
    
    public void showFooter() {
        if (mFootShowView.getVisibility() == View.GONE && mFootShowView != null) {
            mFootShowView.setVisibility(View.VISIBLE);
        }
    }
    
    public void hideFooter() {
        
        if (mFoot == null) {
            return;
        }
        if (mFootShowView.getVisibility() == View.VISIBLE && mFootShowView != null) {
            mFootShowView.setVisibility(View.GONE);
        }
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (scrollAble) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return InterceptAble;
        }
    }
    
    public void setScrollAble(boolean bol) {
        this.scrollAble = bol;
    }
    
    public boolean isScrollAble() {
        return this.scrollAble;
    }
    
    public void getMessage(Son son) {
        MAdapter<?> adapter = mDataFormat.getAdapter(getContext(), son, page);
        addData(adapter);
        if (mOnDataLoaded != null) {
            mOnDataLoaded.onDataLoaded(son);
        }
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
    }
    
    public void setOnScrollListener(OnScrollListener scrollListener) {
        this.mScrollListener = scrollListener;
    }
    
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
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
    
    @Override
    public void setAdapter(ListAdapter adapter) {
        if (adapter == null) {
            adapter = new NullAdapter(getContext());
        }
        super.setAdapter(adapter);
    }
    
    public DataFormat getDataFormat() {
        return mDataFormat;
    }
    
    public void setDataFormat(DataFormat dataFormat) {
        this.mDataFormat = dataFormat;
    }
    
    protected void measureChild(View view, int widthMeasureSpec, int heightMeasureSpec) {
        if (view != null) {
            ViewGroup.LayoutParams vlp = view.getLayoutParams();
            if (vlp == null) {
                vlp = new MarginLayoutParams(MarginLayoutParams.MATCH_PARENT, 3);
            } else if (!(vlp instanceof MarginLayoutParams)) {
                vlp = new MarginLayoutParams(vlp.width, vlp.height);
            }
            
            final MarginLayoutParams lp = (MarginLayoutParams) vlp;
            int childWidthMeasureSpec;
            int childHeightMeasureSpec;
            
            if (lp.width == LayoutParams.MATCH_PARENT) {
                childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingLeft()
                        - getPaddingRight() - lp.leftMargin - lp.rightMargin, MeasureSpec.EXACTLY);
            } else {
                childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, getPaddingLeft() + getPaddingRight()
                        + lp.leftMargin + lp.rightMargin, lp.width);
            }
            
            if (lp.height == LayoutParams.MATCH_PARENT) {
                childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight() - getPaddingTop()
                        - getPaddingBottom() - lp.topMargin - lp.bottomMargin, MeasureSpec.EXACTLY);
            } else {
                childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, getPaddingTop() + getPaddingBottom()
                        + lp.topMargin + lp.bottomMargin, lp.height);
            }
            
            view.measure(childWidthMeasureSpec, childHeightMeasureSpec);
        }
    }
    
    public static interface OnDataLoaded {
        public void onDataLoaded(Son son);
        
        public void onReload();
    }
    
    public interface OnReLoad {
        public boolean onReLoad();
        
        public int getPage();
    }
    
    public void setOnReLoad(OnReLoad onReLoad) {
        this.onReLoad = onReLoad;
    }
    
    public void setOnDataLoaded(OnDataLoaded onDataLoaded) {
        this.mOnDataLoaded = onDataLoaded;
    }
    
    public void setLoadView(View mFootShowView) {
        if (this.mFootShowView != null) {
            this.hideFooter();
            this.mFoot.removeAllViews();
            this.mFootShowView = null;
        }
        this.mFootShowView = mFootShowView;
        this.mFoot.addView(mFootShowView, 0);
        
        this.mFoot.setPadding(0, 0, 0, mfootHeight);
    }
    
    public void setFootHeed(int footHeight) {
        this.mfootHeight = footHeight;
        if (this.mFoot != null) {
            this.mFoot.setPadding(0, 0, 0, footHeight);
        }
    }

    /**
     * [一句话功能简述]<BR>
     * [功能详细描述]
     * @param bol
     * @see MScrollAble#setIntercept(boolean)
     */
    
    @Override
    public void setIntercept(boolean bol) {
       this.InterceptAble=bol;
    }

    /**
     * @return the page
     */
    public int getPage() {
        return page;
    }

    /**
     * @return the havepage
     */
    public boolean isHavepage() {
        return havepage;
    }
}
