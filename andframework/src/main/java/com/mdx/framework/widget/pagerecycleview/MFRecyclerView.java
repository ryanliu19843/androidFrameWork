package com.mdx.framework.widget.pagerecycleview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.mdx.framework.R;
import com.mdx.framework.autofit.AutoFitUtil;
import com.mdx.framework.widget.pagerecycleview.ada.Card;
import com.mdx.framework.widget.pagerecycleview.ada.CardAdapter;
import com.mdx.framework.widget.pagerecycleview.ada.MAdapter;
import com.mdx.framework.widget.pagerecycleview.viewhold.MViewHold;
import com.mdx.framework.widget.pagerecycleview.widget.OnSwipLoadListener;
import com.mdx.framework.widget.pagerecycleview.widget.SwipRefreshLoadingView;
import com.mdx.framework.widget.pagerecycleview.widget.SwipRefreshState;
import com.mdx.framework.widget.pagerecycleview.widget.SwipRefreshStateView;
import com.mdx.framework.widget.pagerecycleview.widget.SwipRefreshView;

import java.util.ArrayList;
import java.util.List;

import static com.mdx.framework.widget.pagerecycleview.animator.ViewAnimator.DEFAULT_ANIMATION_DURATION_MILLIS;

/**
 * Created by ryan on 2016/7/2.
 */
public class MFRecyclerView extends FrameLayout implements IRecyclerView {
    private MRecyclerView mRecyclerView;
    private boolean mIsBeingDragged;
    private int mTouchSlop, mDis;
    private float mLastMotionY = 0;
    private List<Card> list = new ArrayList<>();
    private float offsetT = 0;
    private Card lcard = null;
    boolean isnedrset = false;
    private SwipRefreshStateView swipRefreshStateView;
    private int showtype = 0;
    private int state, error;
    private String msg;
    public String defaultapi, defaultdataformat, defaultswiplistener;
    public int itemres;
    public List<NTouch> lmhs;
    private boolean needdel = false;

    public MFRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public MFRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        getAttr(context, attrs, 0);
    }

    public MFRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        getAttr(context, attrs, defStyleAttr);
    }


    /**
     * lp  0 GridLayoutManager 1 LinearLayoutManager 2 StaggeredGridLayoutManager
     * morientation  RecyclerView.VERTICAL   RecyclerView.HORIZONTAL
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public void getAttr(Context context, AttributeSet attrs, int defStyle) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MFRecyclerView, defStyle, 0);
        defaultapi = a.getString(R.styleable.MFRecyclerView_api);
        defaultdataformat = a.getString(R.styleable.MFRecyclerView_dataformat);
        defaultswiplistener = a.getString(R.styleable.MFRecyclerView_swipListener);
        itemres = a.getResourceId(R.styleable.MFRecyclerView_itemres, 0);
        int column = a.getInt(R.styleable.MFRecyclerView_column, 1);
        int orientation = a.getInt(R.styleable.MFRecyclerView_morientation, RecyclerView.VERTICAL);
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


    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public void init(Context context) {
        this.setLayoutTransition(null);
        mRecyclerView = new MRecyclerView(context);
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        this.addView(mRecyclerView, lp);
        mRecyclerView.defaultapi = defaultapi;
        mRecyclerView.defaultdataformat = defaultdataformat;
        mRecyclerView.defaultswiplistener = defaultswiplistener;

        this.swipRefreshStateView = new SwipRefreshStateView(context);
        this.addView(swipRefreshStateView, lp);
        swipRefreshStateView.setVisibility(View.INVISIBLE);
    }

    public int getShowtype() {
        return showtype;
    }

    public void setShowtype(int showtype) {
        this.showtype = showtype;
        if (showtype == 1 || showtype == 2) {
            if (state == 3) {
                mRecyclerView.setCanPull(true);
                swipRefreshStateView.setVisibility(View.INVISIBLE);
            }
        } else {
            setLoadingState(state, error, msg);
        }
    }

    public void setLoadingFace(SwipRefreshStateView.LoadingFace loadingFace) {
        swipRefreshStateView.setLoadingFace(loadingFace);
    }

    public void setLoadingState(int state, int error, String msg) {
        this.state = state;
        this.error = error;
        this.msg = msg;
        if (showtype == 0) {
            if (state == 0) {
                mRecyclerView.setCanPull(true);
                swipRefreshStateView.setVisibility(View.INVISIBLE);
            } else if (state == 1) {
                mRecyclerView.setCanPull(false);
                swipRefreshStateView.setOnClickListener(null);
                swipRefreshStateView.setVisibility(View.VISIBLE);
            } else if (state == 3) {
                mRecyclerView.setCanPull(false);
                swipRefreshStateView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mRecyclerView.reload();
                    }
                });
                mRecyclerView.hidePage();
                swipRefreshStateView.setVisibility(View.VISIBLE);
            }
            this.swipRefreshStateView.setState(state, error, msg);
        } else if (showtype == 1) {
            if (state == 0) {
                mRecyclerView.setCanPull(true);
                swipRefreshStateView.setVisibility(View.INVISIBLE);
            } else if (state == 1) {
                mRecyclerView.setCanPull(false);
                swipRefreshStateView.setOnClickListener(null);
                swipRefreshStateView.setVisibility(View.VISIBLE);
            } else if (state == 3) {
                mRecyclerView.setCanPull(true);
                swipRefreshStateView.setVisibility(View.INVISIBLE);
                mRecyclerView.hidePage();
            }
            this.swipRefreshStateView.setState(state, error, msg);
        } else if (showtype == 2) {
            mRecyclerView.setCanPull(true);
            swipRefreshStateView.setVisibility(View.INVISIBLE);
            if (state == 3) {
                mRecyclerView.setCanPull(true);
                mRecyclerView.hidePage();
            }
        }
        if (error != 0) {
            mRecyclerView.swipRefreshLoadingView.setState(2, msg);
        }
    }


    public void setTouch(List<NTouch> nts) {
        if (lmhs != null && lmhs.size() == nts.size()) {
            boolean issame = true;
            for (int i = 0; i < nts.size(); i++) {
                if (!lmhs.get(i).equal(nts.get(i))) {
                    issame = false;
                    break;
                }
            }
            if (issame) {
                return;
            }
        }
        lmhs = nts;
        boolean isinnts = false;
        float t = Float.MAX_VALUE;
        for (NTouch nt : nts) {
            if (nt.mh.itemView.getParent() == null) {
                this.addViewInLayout(nt.mh.itemView, -1, new LayoutParams(nt.width, nt.height));
            }
            nt.mh.itemView.setVisibility(View.VISIBLE);
            nt.mh.itemView.setLayoutParams(new LayoutParams(nt.width, nt.height));
            nt.mh.itemView.setY(nt.top);
            nt.mh.itemView.setX(nt.left);
            nt.mh.setXY(nt.left, nt.top);
            nt.mh.y = nt.top;
            nt.mh.x = nt.left;
            if (!nt.mh.isAnima && nt.mh.card.useanimation) {
                itemAnimshow(nt.mh.getOverAnimators(0, 0, 0), nt.mh.itemView);
                nt.mh.isAnima = true;
            }
            if (lcard != null && nt.mh == lcard.overViewHold) {
                isinnts = true;
            } else if (t > nt.top) {
                t = nt.top;
            }
        }

        if (lcard != null) {
            if (lcard != null && lcard.overViewHold != null) {
                View v = lcard.overViewHold.itemView;
                if (v.getParent() == null) {
                    this.addViewInLayout(v, -1, new LayoutParams(v.getWidth(), v.getHeight()));
                }
                int mb = lcard.getViewHodeParam() != null ? (lcard.getViewHodeParam().rect != null ? lcard.getViewHodeParam().rect.bottom : 0) : 0;
                float b = v.getHeight() + (mb < 0 ? 0 : mb) + offsetT;
                float y = offsetT;
                if (b > t) {
                    float top = y - (b - t) - 1;
                    v.setY(top);
                    lcard.overViewHold.setXY(lcard.overViewHold.itemView.getX(), top);
                    isnedrset = false;
                    v.setVisibility(View.VISIBLE);
                } else {
                    v.setY(0);
                }
            }
        }
    }

    public void itemAnimshow(final Animator[] animators, final View view) {
        Animator alphaAnimator = ObjectAnimator.ofFloat(view, "alpha", 0, 1);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(animators);
        set.setDuration(DEFAULT_ANIMATION_DURATION_MILLIS);
        set.start();
    }

    long lasttouchtime = 0;

    public void setDisTouch(RecyclerView parent, RecyclerView.State state) {
        if (System.currentTimeMillis() - lasttouchtime > 300) {
            needdel = true;
        }
        lasttouchtime = System.currentTimeMillis();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.ViewHolder viewh = parent.getChildViewHolder(child);
            if (viewh instanceof MViewHold) {
                ((MViewHold) viewh).setXY(viewh.itemView.getLeft(), viewh.itemView.getTop());
            }
        }

        int lastshow = Integer.MAX_VALUE;
        int poind = 0;
        lcard = null;
        isnedrset = true;
        boolean isshowhead = false;
        if (parent.getLayoutManager() instanceof LinearLayoutManager) {
            int first = ((LinearLayoutManager) parent.getLayoutManager()).findFirstVisibleItemPosition();
            int last = ((LinearLayoutManager) parent.getLayoutManager()).findLastVisibleItemPosition();
            if (parent.getAdapter() instanceof MAdapter) {
                for (Card card : ((MAdapter) parent.getAdapter()).overCard) {
                    int po = card.itemposion;
                    if (po < first && lastshow > first - po) {
                        lastshow = first - po;
                        lcard = card;
                        poind = lastshow;
                    } else {
                        break;
                    }
                }
            }
        }
        boolean isneedskip = true;
        if (offsetT != 0) {
            float lastheight = 0;
            if (lcard != null) {
                lastheight = 0;
            }
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                int ind = parent.getChildAdapterPosition(child);
                if (ind >= 0) {
                    RecyclerView.Adapter ada = parent.getAdapter();
                    if (ada instanceof MAdapter) {
                        Object obj = ((MAdapter) ada).get(ind);
                        if (obj instanceof Card) {
                            Card c = (Card) obj;
                            if (c.getViewHodeParam() != null && c.getViewHodeParam().showType == 1) {
                                if (lcard == null || lcard.overViewHold.itemView != child) {
                                    if (c.overViewHold.itemView.getY() < lastheight) {
                                        isneedskip = false;
                                        break;
                                    }
                                    lastheight = c.overViewHold.itemView.getY() + c.overViewHold.itemView.getHeight();
                                }
                            }
                        }
                    }
                }
            }
        }


        int ox = 0, oy = 0;

        if (mRecyclerView.getLayoutParams() instanceof MarginLayoutParams) {
            MarginLayoutParams layoutParams = (MarginLayoutParams) mRecyclerView.getLayoutParams();
            ox = layoutParams.leftMargin;
            oy = layoutParams.topMargin;
        }

        List<NTouch> mhs = new ArrayList<>();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            int ind = parent.getChildAdapterPosition(child);
            if (ind >= 0) {
                RecyclerView.Adapter ada = parent.getAdapter();
                if (ada instanceof MAdapter) {
                    Object obj = ((MAdapter) ada).get(ind);
                    if (obj instanceof Card) {
                        Card c = (Card) obj;
                        if (c.getViewHodeParam() != null && c.getViewHodeParam().showType == 1) {
                            MViewHold mh = c.overViewHold;
                            float y = child.getY() + oy;
                            if (child.getY() + oy < offsetT) {
                                y = offsetT;
                                lcard = c;
                            }
                            isshowhead = true;
                            if ((child.getY() + oy + child.getHeight()) < offsetT && !isneedskip) {
                                y = child.getY() + oy;
                            }
                            mhs.add(new NTouch(mh, child.getX() + ox, y, child.getWidth(), child.getHeight(), ind));
                        }
                    }
                }
            }
        }
        try {
            setTouch(mhs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < this.getChildCount(); i++) {
            View v = this.getChildAt(i);
            View lcview = (lcard != null && lcard.overViewHold != null) ? lcard.overViewHold.itemView : null;
            boolean isin = false;
            for (NTouch mhv : mhs) {
                if (v == mhv.mh.itemView) {
                    isin = true;
                    break;
                }
            }
            if (!(v instanceof MRecyclerView) && !(v instanceof SwipRefreshState) && v != lcview && !isin) {
                v.setVisibility(View.GONE);
            }
        }
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (needdel) {
            for (int i = 0; i < this.getChildCount(); i++) {
                View v = this.getChildAt(i);
                if (!(v instanceof MRecyclerView) && !(v instanceof SwipRefreshState)) {
                    if (v.getVisibility() == View.GONE) {
                        v.clearAnimation();
                        this.removeViewInLayout(v);
                    }
                }
            }
            needdel = false;
        }
        super.dispatchDraw(canvas);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mIsBeingDragged) {
            if (ev.getAction() == MotionEvent.ACTION_UP) {
                mIsBeingDragged = false;
            }
            return mRecyclerView.onTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        if ((action == MotionEvent.ACTION_MOVE) && (mIsBeingDragged)) {
            return true;
        }
        if (!mIsBeingDragged) {
            switch (action & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_MOVE: {

                    final float y = ev.getY(0);
                    final int xDiff = (int) Math.abs(y - mLastMotionY);

                    if (xDiff > mTouchSlop) {
                        mRecyclerView.dispatchTouchEvent(MotionEvent.obtain(System.currentTimeMillis(), System.currentTimeMillis(), MotionEvent.ACTION_CANCEL, ev.getX(), ev.getY(), ev.getMetaState()));
                        mIsBeingDragged = true;
                        mRecyclerView.dispatchTouchEvent(MotionEvent.obtain(System.currentTimeMillis(), System.currentTimeMillis(), MotionEvent.ACTION_DOWN, ev.getX(), ev.getY(), ev.getMetaState()));
                    }
                    break;
                }

                case MotionEvent.ACTION_DOWN: {
                    final float y = ev.getY();
                    mLastMotionY = y;
                    break;
                }

                case MotionEvent.ACTION_CANCEL:
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    break;
            }
        }
        if (mIsBeingDragged) {
            return true;
        } else {
            return super.onInterceptTouchEvent(ev);
        }
    }

    public void reload() {
        mRecyclerView.reload();
    }

    public void pullLoad() {
        setLoadingState(0, 0, "");
        mRecyclerView.pullLoad();
    }

    public void resetLoad() {
        mRecyclerView.resetLoad();
    }

    public void pullloadelay() {
        setLoadingState(0, 0, "");
        mRecyclerView.pullloadelay();
    }

    public void update() {
        mRecyclerView.update();
    }

    public void setOnSwipLoadListener(OnSwipLoadListener onSwipLoadListener) {
        mRecyclerView.setOnSwipLoadListener(onSwipLoadListener);
    }

    public void setOnDataLoaded(MRecyclerView.OnDataLoaded onDataLoaded) {
        mRecyclerView.setOnDataLoaded(onDataLoaded);
    }

    public void setSwipRefreshView(SwipRefreshView swipRefreshView, int type) {
        mRecyclerView.setSwipRefreshView(swipRefreshView, type);
    }

    public void setSwipRefreshLoadingView(SwipRefreshLoadingView swipRefreshLoadingView, int type) {
        mRecyclerView.setSwipRefreshLoadingView(swipRefreshLoadingView, type);
    }

    public void hideFoot() {
        mRecyclerView.endPage();
    }

    public void showFoot() {
        mRecyclerView.showPage();
    }

    public <T extends RecyclerView.Adapter> T getMAdapter() {
        return (T) mRecyclerView.getMAdapter();
    }

    public RecyclerView.Adapter getAdapter() {
        return mRecyclerView.getAdapter();
    }

    public View getHeadView(int ind) {
        return mRecyclerView.getHeadView(ind);
    }

    public View getFootView(int ind) {
        return mRecyclerView.getFootView(ind);
    }

    public void addHeadView(MViewHold viewHold, int ind) {
        mRecyclerView.addHeadView(viewHold, ind);
    }

    public void addFootView(MViewHold viewHold, int ind) {
        mRecyclerView.addFootView(viewHold, ind);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }

    public void addAdapter(CardAdapter cardAdapter) {
        mRecyclerView.addAdapter(cardAdapter);
    }

    public void clearAdapter() {
        mRecyclerView.clearAdapter();
    }

    public void setLayoutManager(RecyclerView.LayoutManager layout) {
        mRecyclerView.setLayoutManager(layout);
    }


    public void setOnPullListener(MRecyclerView.OnPullListener onPullListener) {
        mRecyclerView.setOnPullListener(onPullListener);
    }

    public MRecyclerView getRecyclerView() {
        return mRecyclerView;
    }


    public void setSwipPadding(int swipPadding) {
        mRecyclerView.setSwipPadding(swipPadding);
        this.offsetT = swipPadding;
    }

    public void setLoadingPadding(int loadingPadding) {
        mRecyclerView.setLoadingPadding(loadingPadding);
    }

    public void setOnAdaLoad(MRecyclerView.OnAdaLoad onAdaLoad) {
        mRecyclerView.setOnAdaLoad(onAdaLoad);
    }


    public void setLoadingType(int type) {
        mRecyclerView.setLoadingType(type);
    }

    public void setDecoration(int left, int top, int right, int bottom) {
        mRecyclerView.setDecoration(left, top, right, bottom);
    }


    public static class NTouch {
        public MViewHold mh;
        public float left;
        public float top;
        public int width;
        public int height;
        public int pos;

        public NTouch(MViewHold mh, float l, float t, int w, int h, int posion) {
            this.mh = mh;
            this.left = l;
            this.top = t;
            this.width = w;
            this.height = h;
            this.pos = posion;
        }

        public boolean equal(NTouch nt) {
            if (nt.mh == mh && left == nt.left && top == nt.top && nt.width == nt.width && nt.height == nt.height && nt.pos == nt.pos) {
                return true;
            }
            return false;
        }

    }

    public void scrollToPositionWithOffset(int pos, int offset) {
        RecyclerView.LayoutManager mLayoutManager = mRecyclerView.getLayoutManager();
        if (mLayoutManager instanceof LinearLayoutManager) {
            ((LinearLayoutManager) mLayoutManager).scrollToPositionWithOffset(pos, 0);
        } else if (mLayoutManager instanceof GridLayoutManager) {
            mLayoutManager.scrollToPosition(pos);
        } else {
            mLayoutManager.scrollToPosition(pos);
        }
    }


    public void scrollToPosition(int pos) {
        getRecyclerView().scrollToPosition(pos);
    }


    public void smoothScrollToPosition(int pos) {
        getRecyclerView().smoothScrollToPosition(pos);
    }


}