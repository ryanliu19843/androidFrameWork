/*
 * 文件名: MListView.java 版 权： Copyright XCDS Tech. Co. Ltd. All Rights Reserved. 描
 * 述: [该类的简要描述] 创建人: ryan 创建时间:2014-4-9
 * 
 * 修改人： 修改时间: 修改内容：[修改内容]
 */
package com.mdx.framework.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.mdx.framework.prompt.Prompt;
import com.mdx.framework.utility.Device;
import com.mdx.framework.widget.util.PullView;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 *
 * @author ryan
 * @version [2014-4-9 下午3:50:29]
 */
public class MPullListView extends MListView implements Prompt {
    private LinearLayout mTopViews;

    private View mPullView;

    private Scroller mScroller;

    private int initHeight = 0, pullviewHeight = 0;

    private boolean outBound = false, isShow = false, canPull = true, pullloading = false, mIsBeingDragged = false;

    private int distance;

    private int mTouchSlop, mDis;

    private float mLastMotionY = 0;

    private int firstOut;

    private OnPullListener mOnPullListener;

    public MPullListView(Context context) {
        super(context);
        inits(context);
    }

    public MPullListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inits(context);
    }

    public MPullListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inits(context);
    }

    protected void endLoad() {
        super.endLoad();
        pullloading = false;
        int ny = mTopViews.getHeight();
        mScroller.startScroll(0, ny, 0, -ny + initHeight, 500);
        // MLog.D("sct:"+(-ny + initHeight)+" ny:"+ny);
    }

    protected void startLoad() {
        super.startLoad();
        pullloading = true;
        if (this.mOnPullListener != null) {
            this.mOnPullListener.onPullload();
        }
        if (mTopViews != null) {
            mScroller.startScroll(0, 0, 0, pullviewHeight + initHeight, 500);
            // MLog.D("sct:"+(pullviewHeight + initHeight));
            this.invalidate();
        }
    }

    public void pullLoad() {
        startLoad();
        sendPullview();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                reloadWithOutCatch();
            }
        }, 200);
    }

    protected void inits(Context context) {
        if (mTopViews == null) {
            final ViewConfiguration configuration = ViewConfiguration.get(getContext());
            mTouchSlop = configuration.getScaledTouchSlop();
            mScroller = new Scroller(getContext());
            mTopViews = new LinearLayout(context);
            mTopViews.setOrientation(LinearLayout.VERTICAL);
            mTopViews.setGravity(Gravity.BOTTOM);
            mTopViews.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, initHeight));
            this.addHeaderView(mTopViews);
        }
        setPullView(new MpullView(getContext()));
    }

    public void setPullView(View view) {
        if (this.mPullView != null) {
            mTopViews.removeView(this.mPullView);
            this.mPullView = null;
        }
        this.mPullView = view;
        this.measureChild(Device.getMeticsW(), Device.getMeticsH());
        pullviewHeight = view.getMeasuredHeight();
        mTopViews.setPadding(0, -pullviewHeight - initHeight, 0, 0);
        mTopViews.addView(view);
    }

    private void setReload() {
        firstOut = 0;
        mDis = -Integer.MAX_VALUE;
        if (mPullView == null) {
            return;
        }
        mIsBeingDragged = false;
        int top = mTopViews.getTop();
        int ny = mTopViews.getHeight() + top;
        if (mTopViews.getParent() != null && getTh() > initHeight) {
            setSelection(0);
            setTopViewsHeight(ny);
        }
        if (ny > this.pullviewHeight + initHeight || pullloading) {
            mScroller.startScroll(0, ny, 0, -ny + pullviewHeight + initHeight, 300);
            // MLog.D("sct:"+( -ny + pullviewHeight + initHeight)+" ny:"+ny);
            if (!pullloading) {
                pullloading = true;
                if (this.mOnPullListener != null) {
                    this.mOnPullListener.onPullload();
                }
                reloadWithOutCatch();
            }
            sendPullview();
        } else {
            if (-ny + initHeight < 0) {
                mScroller.startScroll(0, ny, 0, -ny + initHeight, 300);
                // MLog.D("sct:"+(-ny + initHeight)+" ny:"+ny);
            } else if (getFirstVisiblePosition() == 0) {
                setSelectionFromTop(0, ny - initHeight);
                setTopViewsHeight(initHeight);
            }
            sendPullview();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!canPull) {
            return super.onTouchEvent(event);
        }
        int act = event.getAction();
        if (act == MotionEvent.ACTION_UP || act == MotionEvent.ACTION_CANCEL || act == MotionEvent.ACTION_DOWN) {
            mIsBeingDragged = false;
            mDis = -Integer.MAX_VALUE;
        }

        int firstPos = getFirstVisiblePosition();
        if (firstPos == 0 && mDis == -Integer.MAX_VALUE) {
            View firstView = getChildAt(firstPos);
            if (firstView != null) {
                mDis = firstView.getTop() + initHeight;
            }
        } else if (firstPos != 0) {
            mDis = -Integer.MAX_VALUE;
        }

        if (mTopViews.getParent() != null && mTopViews.getBottom() >= 0 && (!isLoading || pullloading) && mApiUpdate != null && mPullView != null) {
            if ((act == MotionEvent.ACTION_UP || act == MotionEvent.ACTION_CANCEL) && getTh() > initHeight) {
                // Util.setScrollAbleParent(this, true);
                outBound = false;
                setReload();
                firstOut = 0;
            } else {
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
            }
            if (act == MotionEvent.ACTION_MOVE) {
                if (mTopViews.getTop() >= 0) {
                    onScroll(event);
                } else if (pullloading) {
                    firstOut = (int) event.getRawY();
                    // Log.d("event", "setfo:"+firstOut);
                }
            } else {
                firstOut = (int) event.getRawY();
            }
            sendPullview();
        } else {
            firstOut = (int) event.getRawY();
            // Log.d("event", "setfo:"+firstOut);
            setTopViewsHeight(pullloading ? pullviewHeight + initHeight : initHeight);
        }
        return super.onTouchEvent(event);
    }

    public boolean onScroll(MotionEvent e) {
        // Util.setScrollAbleParent(this, false);
        int firstPos = getFirstVisiblePosition();
        View firstView = getChildAt(firstPos);
        if (firstOut == 0) {
            firstOut = (int) e.getRawY();
        }
        if ((outBound || firstView == null || (firstPos == 0 && firstView.getTop() == getPaddingTop()))) {
            distance = ((int) e.getRawY() - firstOut);
            // Log.d("scroll",
            // "distance:"+distance+" fo:"+firstOut+" fs:"+firstView.getTop());
            if (distance < 0) {
                distance = 0;
            }
            // if (distance > pullviewHeight) {
            // distance = pullviewHeight + (distance - pullviewHeight) / 3;
            // }
            if (pullloading) {
                distance = pullviewHeight + distance;
            }

            // Log.d("scrollto", "distance:" + distance + " fo:" + firstOut +
            // " md:" + mDis);
            if (mDis > 0 && mDis < initHeight) {
                if (distance > initHeight) {
                    setTopViewsHeight(distance);
                } else {
                    firstOut = (int) (e.getRawY() - initHeight);
                }
            } else {
                setTopViewsHeight(distance + initHeight);
            }
            invalidate();
            return true;
        }
        return true;
    }

    private void sendPullview() {
        if (mPullView instanceof PullView) {
            int scrolly = pullloading ? mPullView.getHeight() : mTopViews.getLayoutParams().height + mTopViews.getTop();
            if (this.mOnPullListener != null) {
                this.mOnPullListener.onPullLoad(scrolly);
            }
            // MLog.D("pull","th:"+mTopViews.getHeight()+" tt:"+mTopViews.getLayoutParams().height +" ss:"+scrolly);
            ((PullView) mPullView).setScroll(scrolly, mPullView.getHeight() + initHeight, pullloading ? 1 : 0);
        }
    }

    private int getTh() {
        return mTopViews.getLayoutParams().height;
    }

    private void setTopViewsHeight(int dis) {
        mTopViews.setLayoutParams(new LayoutParams(mTopViews.getLayoutParams().width, dis));
        mTopViews.setPadding(0, -pullviewHeight + dis, 0, 0);
        if (this.mOnPullListener != null) {
            this.mOnPullListener.onPullLoad(dis);
        }
    }

    @Override
    public void pshow(boolean isshow) {
        isShow = true;
    }

    @Override
    public void pdismiss(boolean isshow) {
        isShow = false;
        endLoad();
        invalidate();
    }

    @Override
    public boolean isShowing() {
        return isShow;
    }

    @Override
    public boolean canShow() {
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        boolean invalidata = false;
        if (mScroller.computeScrollOffset()) {
            setTopViewsHeight(mScroller.getCurrY());
            // MLog.D("log","");
            invalidata = true;
            sendPullview();
        }
        if (invalidata) {
            postInvalidate();
        }
    }

    public static int rs(int size, int measureSpec, int childMeasuredState) {
        int result = size;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = size;
                break;
            case MeasureSpec.AT_MOST:
                if (specSize < size) {
                    result = specSize | MEASURED_STATE_TOO_SMALL;
                } else {
                    result = size;
                }
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result | (childMeasuredState & MEASURED_STATE_MASK);
    }

    protected void measureChild(int widthMeasureSpec, int heightMeasureSpec) {

        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;

        // Account for padding too
        maxWidth += getPaddingLeft() + getPaddingRight();
        maxHeight += getPaddingTop() + getPaddingBottom();

        // Check against our minimum height and width
        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());

        // Check against our foreground's minimum height and width
        setMeasuredDimension(rs(maxWidth, widthMeasureSpec, childState), rs(maxHeight, heightMeasureSpec, childState << MEASURED_HEIGHT_STATE_SHIFT));

        if (mPullView != null) {
            ViewGroup.LayoutParams vlp = mPullView.getLayoutParams();
            if (vlp == null) {
                vlp = new MarginLayoutParams(MarginLayoutParams.MATCH_PARENT, MarginLayoutParams.WRAP_CONTENT);
            } else if (!(vlp instanceof MarginLayoutParams)) {
                vlp = new MarginLayoutParams(vlp.width, vlp.height);
            }

            final MarginLayoutParams lp = (MarginLayoutParams) vlp;
            int childWidthMeasureSpec;
            int childHeightMeasureSpec;

            if (lp.width == LayoutParams.MATCH_PARENT) {
                childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - lp.leftMargin - lp.rightMargin, MeasureSpec.EXACTLY);
            } else {
                childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin, lp.width);
            }

            if (lp.height == LayoutParams.MATCH_PARENT) {
                childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight() - getPaddingTop() - getPaddingBottom() - lp.topMargin - lp.bottomMargin, MeasureSpec.EXACTLY);
            } else {
                childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, getPaddingTop() + getPaddingBottom() + lp.topMargin + lp.bottomMargin, lp.height);
            }

            mPullView.measure(childWidthMeasureSpec, childHeightMeasureSpec);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!canPull) {
            return super.onInterceptTouchEvent(ev);
        }
        final int action = ev.getAction();
        if ((action == MotionEvent.ACTION_MOVE) && (mIsBeingDragged)) {
            return true;
        }
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_MOVE: {

                final float y = ev.getY(0);
                final int xDiff = (int) Math.abs(y - mLastMotionY);

                if (xDiff > mTouchSlop) {
                    firstOut = (int) ev.getRawY();
                    mIsBeingDragged = true;
                }
                break;
            }

            case MotionEvent.ACTION_DOWN: {
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                final float y = ev.getY();
                mLastMotionY = y;
                break;
            }

            case MotionEvent.ACTION_CANCEL:
                setReload();
                break;
            case MotionEvent.ACTION_UP:
                setReload();
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
        }
        if (mIsBeingDragged) {
            mIsBeingDragged = false;
            return true;
        } else {
            return super.onInterceptTouchEvent(ev);
        }
    }

    public boolean isCanPull() {
        return canPull;
    }

    public void setCanPull(boolean canPull) {
        this.canPull = canPull;
    }

    public boolean isPullloading() {
        return pullloading;
    }

    public interface OnPullListener {
        public void onPullload();

        public void onPullLoad(int scolly);
    }

    public void setOnPullListener(OnPullListener mOnPullListener) {
        this.mOnPullListener = mOnPullListener;
    }

    public int getInitHeight() {
        return initHeight;
    }

    public void setInitHeight(int initHeight) {
        this.initHeight = initHeight;
    }
}
