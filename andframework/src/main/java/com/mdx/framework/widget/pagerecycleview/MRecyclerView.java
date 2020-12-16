package com.mdx.framework.widget.pagerecycleview;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.mdx.framework.R;
import com.mdx.framework.autofit.AutoFitUtil;
import com.mdx.framework.widget.pagerecycleview.ada.CardAdapter;
import com.mdx.framework.widget.pagerecycleview.ada.HAdapter;
import com.mdx.framework.widget.pagerecycleview.pullanimview.SPView;
import com.mdx.framework.widget.pagerecycleview.viewhold.MViewHold;
import com.mdx.framework.widget.pagerecycleview.viewhold.ViewHodeParam;
import com.mdx.framework.widget.pagerecycleview.widget.OnSwipLoadListener;
import com.mdx.framework.widget.pagerecycleview.widget.SwipRefreshLoadingView;
import com.mdx.framework.widget.pagerecycleview.widget.SwipRefreshView;

import static com.mdx.framework.widget.pagerecycleview.widget.SwipRefreshView.SRV_STATE_PULL;

/**
 * Created by ryan on 2016/4/16.
 */
public class MRecyclerView extends RecyclerView implements IRecyclerView {
    private boolean canPull = true;
    public float lm = -999, mv = 0, mt = 0, loadingType = 1, swipType = 0;
    private HAdapter hAdapter;
    private SwipRefreshView swipRefreshView;
    public SwipRefreshLoadingView swipRefreshLoadingView;
    private MViewHold mhswipRefreshView, mhswipRefreshLoadingView;
    private int orientation;
    public OnSwipLoadListener onSwipLoadListener;
    private OnDataLoaded onDataLoaded;
    private OnAdaLoad onAdaLoad;
    private CardAdapter mcardAdapter;
    private MViewHold paddingFootview;
    private int loadingshowType = 0;
    private int overScrollModel = -999;
    public StickyRecyclerHeadersDecoration mstickr;
    private float xoffset = -1, yoffset = -1, lastx = -1, lasty = -1;
    public OnPullListener onPullListener;
    public String defaultapi, defaultdataformat, defaultswiplistener;
    public int itemres;


    public interface OnPullListener {
        void onPullListener(float mv, float mt, int orientation, float over);
    }


    public MRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public MRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
        getAttr(context, attrs, 0);
    }

    public MRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
        getAttr(context, attrs, defStyle);
    }

    public void getAttr(Context context, AttributeSet attrs, int defStyle) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MFRecyclerView, defStyle, 0);
        defaultapi = a.getString(R.styleable.MFRecyclerView_api);
        defaultdataformat = a.getString(R.styleable.MFRecyclerView_dataformat);
        defaultswiplistener = a.getString(R.styleable.MFRecyclerView_swipListener);
        itemres = a.getResourceId(R.styleable.MFRecyclerView_itemres, 0);
        int column = a.getInt(R.styleable.MFRecyclerView_column, 1);
        int orientation = a.getInt(R.styleable.MFRecyclerView_morientation, VERTICAL);
        int lp = a.getInt(R.styleable.MFRecyclerView_layoutparams, 0);
        setLayoutManager(AutoFitUtil.getLayoutManager(lp, column, orientation));
        a.recycle();
    }


    public void setApi(String api) {
        this.defaultapi = api;
    }

    public void setDataformat(String dataformat) {
        this.defaultdataformat = dataformat;
    }

    public void setSwipListener(String swipListener) {
        this.defaultswiplistener = swipListener;
    }


    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public boolean isCanPull() {
        return canPull && this.onSwipLoadListener != null;
    }

    private void init(Context context) {
        if (this.getLayoutParams() instanceof MarginLayoutParams) {
            MarginLayoutParams lp = (MarginLayoutParams) this.getLayoutParams();
            lp.setMargins(0, -1, 0, 0);
            this.setLayoutParams(lp);
        }
        hAdapter = new HAdapter(context, null);
        hAdapter.recyclerView = this;
        if (this.getLayoutManager() instanceof GridLayoutManager) {
            ((GridLayoutManager) this.getLayoutManager()).setSpanSizeLookup(hAdapter.SpanSizeLookup);
        }
        SwipRefreshView sr = new SwipRefreshView(context);
        sr.setSwipRefresh(new SPView(context));
        setSwipRefreshView(sr, 0);

        SwipRefreshLoadingView swv = new SwipRefreshLoadingView(context);
        setSwipRefreshLoadingView(swv, 0);
        mcardAdapter = new CardAdapter(getContext());
        hAdapter.setAdapter(mcardAdapter);
        hAdapter.setOnLoadingLast(new HAdapter.OnLoadingLast() {
            @Override
            public void onLoadingLast(HAdapter adapter, int posion) {
                if (onSwipLoadListener != null) {
                    onSwipLoadListener.onPageLoad();
                }
            }
        });
        mstickr = new StickyRecyclerHeadersDecoration();
        this.addItemDecoration(mstickr);
        super.setAdapter(hAdapter);
        hidePage();
    }

    public void setDecoration(int left, int top, int right, int bottom) {
        if (mstickr != null) {
            mstickr.setDecoration(left, top, right, bottom);
        }
    }


    public void setSwipPadding(int swipPadding) {
        swipRefreshView.setSwipPadding(swipPadding);
    }

    public SwipRefreshView getSwipRefreshView() {
        return swipRefreshView;
    }

    public void setLoadingPadding(int loadingPadding) {
        if (paddingFootview != null) {
            hAdapter.removeFoot(paddingFootview);
        }
        if (loadingPadding == 0) {
            hAdapter.notifyDataSetChanged();
            return;
        }
        if (paddingFootview == null) {
            paddingFootview = new MViewHold(new View(getContext()));
        }
        StaggeredGridLayoutManager.LayoutParams lp = new StaggeredGridLayoutManager.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, loadingPadding);
        paddingFootview.itemView.setLayoutParams(lp);
        hAdapter.addFoot(paddingFootview);
    }

    public void setOnSwipLoadListener(OnSwipLoadListener onSwipLoadListener) {
        swipRefreshView.loadend();
        if (this.onSwipLoadListener != null) {
            this.onSwipLoadListener.clear();
        }
        this.onSwipLoadListener = onSwipLoadListener;
        if (this.onSwipLoadListener != null) {
            this.onSwipLoadListener.setRecyclerView(this);
            this.onSwipLoadListener.setOnDataLoaded(onSdataload);
        }
    }

    public OnDataLoaded onSdataload = new OnDataLoaded() {
        @Override
        public void onDataLoaded(Object son, int page) {
            if (onDataLoaded != null) {
                onDataLoaded.onDataLoaded(son, page);
            }
        }

        @Override
        public void onReload(int page) {
            if (onDataLoaded != null) {
                onDataLoaded.onReload(page);
            }
        }

        @Override
        public void onDataLoaded(Adapter page) {
            if (onDataLoaded != null) {
                onDataLoaded.onDataLoaded(page);
            }
            if (onAdaLoad != null) {
                onAdaLoad.onAdaLoad(page);
            }
        }

        @Override
        public void onPageLoad(int page) {
            if (onDataLoaded != null) {
                onDataLoaded.onPageLoad(page);
            }
        }
    };

    public void setOnDataLoaded(OnDataLoaded onDataLoaded) {
        this.onDataLoaded = onDataLoaded;
    }


    public void setSwipRefreshView(SwipRefreshView swipRefreshView, int type) {
        if (this.swipRefreshView != null && hAdapter.getHeadviews().contains(this.swipRefreshView)) {
            hAdapter.removeHead(this.mhswipRefreshView);
        }
        if (swipRefreshView == null) {
            return;
        }
        swipType = type;
        this.swipRefreshView = swipRefreshView;
        mhswipRefreshView = new MViewHold(swipRefreshView, new ViewHodeParam(2, null));
        if (type == 0) {
            hAdapter.addHead(mhswipRefreshView, 0);
            hAdapter.notifyDataSetChanged();
        }
        swipRefreshView.mRecyclerView = this;
    }


    public void setSwipRefreshLoadingView(SwipRefreshLoadingView swipRefreshLoadingView, int type) {
        if (this.swipRefreshLoadingView != null && hAdapter.getFootviews().contains(this.swipRefreshLoadingView)) {
            hAdapter.removeFoot(this.mhswipRefreshLoadingView);
        }
        if (swipRefreshLoadingView == null) {
            return;
        }
        this.swipRefreshLoadingView = swipRefreshLoadingView;
        mhswipRefreshLoadingView = new MViewHold(swipRefreshLoadingView, new ViewHodeParam(2, null));
        swipRefreshLoadingView.mRecyclerView = this;
    }

    public void setLoadingType(int type) {
        this.loadingType = type;
        if (loadingshowType == 1) {
            showPage();
            endPage();
        }
    }

    public void setLoadingViewState(int state, String msg) {
        this.swipRefreshLoadingView.setState(state, msg);
    }

    public void endPage() {
        if (loadingshowType == 3) {
            showPage();
        }
        if (this.onSwipLoadListener != null) {
            this.onSwipLoadListener.setHavepage(false);
        }
        if (loadingshowType == 1) {
            this.setLoadingViewState(1, null);
            return;
        }
        if (loadingType == 0) {
            hAdapter.removeFoot(mhswipRefreshLoadingView);
        } else {
            this.setLoadingViewState(1, null);
        }
        loadingshowType = 1;
    }

    public void hidePage() {
        if (loadingshowType == 3) {
            return;
        }
        hAdapter.removeFoot(mhswipRefreshLoadingView);
        loadingshowType = 3;
    }

    public void showPage() {
        if (this.onSwipLoadListener != null) {
            this.onSwipLoadListener.setHavepage(true);
            this.hAdapter.mnotifyDataSetChanged();
        }
        if (loadingshowType == 2) {
            this.setLoadingViewState(0, null);
            return;
        }
        if (loadingType == 0) {
            int ind = 0;
            for (int i = 0; i < hAdapter.getFootviews().size(); i++) {
                if (paddingFootview == hAdapter.getFootviews().get(i)) {
                    ind = i;
                    break;
                }
            }
            this.swipRefreshLoadingView.setState(0, null);
            if (!hAdapter.getFootviews().contains(this.mhswipRefreshLoadingView)) {
                hAdapter.addFoot(mhswipRefreshLoadingView, ind);
            }
        } else {
            if (!hAdapter.getFootviews().contains(this.mhswipRefreshLoadingView)) {
                int ind = 0;
                for (int i = 0; i < hAdapter.getFootviews().size(); i++) {
                    if (paddingFootview == hAdapter.getFootviews().get(i)) {
                        ind = i;
                        break;
                    }
                }
                hAdapter.addFoot(mhswipRefreshLoadingView, ind);
            }
            this.setLoadingViewState(0, null);
        }
        loadingshowType = 2;
    }

    public Adapter getMAdapter() {
        return hAdapter.getAdapter();
    }


    public View getFootView(int ind) {
        return hAdapter.getFootviews().get(ind).itemView;
    }

    public View getHeadView(int ind) {
        return hAdapter.getHeadviews().get(ind).itemView;
    }

    public void addHeadView(MViewHold viewHold, int ind) {
        if (ind == -1) {
            hAdapter.addHead(viewHold);
        } else {
            hAdapter.addHead(viewHold, ind);
        }
    }

    public void addFootView(MViewHold viewHold, int ind) {
        if (ind == -1) {
            hAdapter.addFoot(viewHold);
        } else {
            hAdapter.addFoot(viewHold, ind);
        }
    }

    public void setAdapter(Adapter adapter) {
        if (adapter instanceof HAdapter) {
            hAdapter = (HAdapter) adapter;
            super.setAdapter(adapter);
            if (this.getLayoutManager() instanceof GridLayoutManager) {
                hAdapter.setRecyclerView(this);
                ((GridLayoutManager) this.getLayoutManager()).setSpanSizeLookup(hAdapter.SpanSizeLookup);
            }
        } else {
            hAdapter.setAdapter(adapter);
            if (adapter instanceof CardAdapter) {
                mcardAdapter = (CardAdapter) adapter;
            }
            hAdapter.notifyDataSetChanged();
        }
    }

    public void addAdapter(CardAdapter cardAdapter) {
        if (cardAdapter != null) {
            mcardAdapter.AddAll(cardAdapter.getList());
        }
    }

    public void clearAdapter() {
        mcardAdapter.clear();
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        if (this.getLayoutManager() instanceof GridLayoutManager) {
            ((GridLayoutManager) this.getLayoutManager()).setSpanSizeLookup(hAdapter.SpanSizeLookup);
        }
        if (layout instanceof LinearLayoutManager) {
            if (((LinearLayoutManager) layout).getOrientation() == LinearLayoutManager.VERTICAL) {
                if (this.getLayoutParams() instanceof MarginLayoutParams) {
                    MarginLayoutParams lp = (MarginLayoutParams) this.getLayoutParams();
                    lp.setMargins(0, -1, 0, 0);
                    this.setLayoutParams(lp);
                }
                orientation = LinearLayoutManager.VERTICAL;
            } else {
                if (this.getLayoutParams() instanceof MarginLayoutParams) {
                    MarginLayoutParams lp = (MarginLayoutParams) this.getLayoutParams();
                    lp.setMargins(-1, 0, 0, 0);
                    this.setLayoutParams(lp);
                }
                orientation = LinearLayoutManager.HORIZONTAL;
            }
        } else if (layout instanceof StaggeredGridLayoutManager) {
            if (((StaggeredGridLayoutManager) layout).getOrientation() == StaggeredGridLayoutManager.VERTICAL) {
                if (this.getLayoutParams() instanceof MarginLayoutParams) {
                    MarginLayoutParams lp = (MarginLayoutParams) this.getLayoutParams();
                    lp.setMargins(0, -1, 0, 0);
                    this.setLayoutParams(lp);
                }
                orientation = LinearLayoutManager.VERTICAL;
            } else {
                if (this.getLayoutParams() instanceof MarginLayoutParams) {
                    MarginLayoutParams lp = (MarginLayoutParams) this.getLayoutParams();
                    lp.setMargins(-1, 0, 0, 0);
                    this.setLayoutParams(lp);
                }
                orientation = LinearLayoutManager.HORIZONTAL;
            }
        }
        swipRefreshView.loadend();
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isCanPull() || swipRefreshView.getState() == swipRefreshView.SRV_STATE_REFE) {
            return super.onTouchEvent(event);
        }
        if (this.mv <= 0) {
            this.startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
        }

        int act = event.getAction();
        if (act == MotionEvent.ACTION_DOWN || act == MotionEvent.ACTION_MOVE) {
            if (swipRefreshView != null) {
                swipRefreshView.setTouch(mv);
            }
            if (xoffset == -1) {
                xoffset = event.getRawX() - event.getX();
                yoffset = event.getRawY() - event.getY();
            }
            if (lastx == -1) {
                lastx = event.getRawX();
                lasty = event.getRawY();
            }
        }
        boolean retn = super.onTouchEvent(event);
        if (xoffset + event.getX() == event.getRawX() && yoffset + event.getY() == event.getRawY()) {
//            if(this.mv<=0) {
//                if (event.getRawX() - lastx > 0 || event.getRawY() - lasty > 0) {
//                    doPull(event);
//                }else{
//                    retn = super.onTouchEvent(event);
//                }
//            }else{
//                doPull(event);
//            }
            doPull(event);
            lastx = event.getRawX();
            lasty = event.getRawY();
        }
        if (act == MotionEvent.ACTION_UP || act == MotionEvent.ACTION_CANCEL) {
            lm = -999;
            xoffset = yoffset = -1;
            if (mv > 0) {
                if (swipRefreshView != null) {
                    swipRefreshView.setRelease(mv);
                }
            }
        }

        if (this.mv > 0) {
            this.stopNestedScroll();
        }

        return retn;
    }

    public void reload() {
        clearAdapter();
        hidePage();
        startLoad(0);
    }

    public void update() {
        update(0);
    }

    public void update(int type) {
        if (onSwipLoadListener != null) {
            onSwipLoadListener.onUpdateLoad(0);
        }
    }


    public void startLoad(int type) {
        if (onSwipLoadListener != null) {
            onSwipLoadListener.onSwipLoad(type);
        }
    }

    public void pullLoad() {
        if (swipRefreshView != null && isCanPull()) {
            hidePage();
            swipRefreshView.pullReload();
        }
    }

    public void resetLoad() {
        this.mv = 0;
        swipRefreshView.setPull(this.mv);
        swipRefreshView.requestLayout();
        swipRefreshView.setState(SRV_STATE_PULL);
        clearAdapter();
    }

    public void pullloadelay() {
        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                pullLoad();
            }
        }, 200);
    }

    public void endPullLoad() {
        swipRefreshView.setloadend(this.mv);
    }


    private void doPull(MotionEvent event) {
        try {
            boolean canplt;
            float nm;
            if (orientation == LinearLayoutManager.VERTICAL) {
                canplt = this.canScrollVertically(-1);
                nm = event.getY();
                mt = event.getX();
            } else {
                canplt = this.canScrollHorizontally(-1);
                nm = event.getX();
                mt = event.getY();
            }
            if (!canplt || mv > 0) {
                if (lm != -999) {
//                    Log.d("onevent","L:"+event.getY()+" N:"+nm+" L:"+lm);
                    float sm = nm - lm;
                    if (!canplt || sm < 0) {
                        mv += sm;
                    }
                    if (swipRefreshView.getState() == swipRefreshView.SRV_STATE_REFI && mv < swipRefreshView.getMinload()) {
                        mv = swipRefreshView.getMinload();
                    }
                    if (mv < 0) {
                        mv = 0;
                    }
                    if (swipRefreshView.getState() == swipRefreshView.SRV_STATE_REFI && mv < swipRefreshView.getMinload()) {
                        mv = swipRefreshView.getMinload();
                    }
                    if (swipRefreshView != null) {
                        if (mv > 0) {
                            if (overScrollModel == -999) {
                                overScrollModel = this.getOverScrollMode();
                            }
                            this.setOverScrollMode(OVER_SCROLL_NEVER);
                            swipRefreshView.setPull(mv);
                            if (onSwipLoadListener != null) {
                                onSwipLoadListener.onSwipStateChange(swipRefreshView.getState(), mv, mt);
                            }
                            swipRefreshView.requestLayout();
                            if (swipRefreshView.getState() == SRV_STATE_PULL || swipRefreshView.getState() == swipRefreshView.SRV_STATE_RELR) {
                                this.scrollToPosition(0);
                            }
                        } else {
                            if (overScrollModel != -999) {
                                this.setOverScrollMode(overScrollModel);
                                overScrollModel = -999;
                            }
                            if (swipRefreshView.getState() == SRV_STATE_PULL || swipRefreshView.getState() == swipRefreshView.SRV_STATE_RELR) {
                                swipRefreshView.setPull(0);
                                if (onSwipLoadListener != null) {
                                    onSwipLoadListener.onSwipStateChange(swipRefreshView.getState(), mv, mt);
                                }
                                swipRefreshView.requestLayout();
                            }
                        }
                        if (mv > swipRefreshView.getMinload()) {
                            this.scrollToPosition(0);
                        }
                    }
                }
                lm = nm;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCanPull(boolean bol) {
        this.canPull = bol;
    }

    public void setOnPullListener(OnPullListener onPullListener) {
        this.onPullListener = onPullListener;
    }


    public static interface OnAdaLoad {
        void onAdaLoad(Adapter page);

    }

    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);
    }

    public void setOnAdaLoad(OnAdaLoad onAdaLoad) {
        this.onAdaLoad = onAdaLoad;
    }

    public static interface OnDataLoaded {
        void onDataLoaded(Object son, int page);

        void onReload(int page);

        void onDataLoaded(Adapter page);

        void onPageLoad(int page);
    }

    public MRecyclerView getRecyclerView() {
        return this;
    }
}