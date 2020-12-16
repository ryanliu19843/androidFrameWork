/*
 * 文件名: SwipMoreView.java 版 权： Copyright XCDS Tech. Co. Ltd. All Rights
 * Reserved. 描 述: [该类的简要描述] 创建人: ryan 创建时间:2014年5月29日
 * 
 * 修改人： 修改时间: 修改内容：[修改内容]
 */
package com.mdx.framework.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 * 
 * @author ryan
 * @version [2014年5月29日 下午2:41:11]
 */
public abstract class SwipMoreView extends FrameLayout {
    private float mDownX, nowX;
    
    private boolean swipable = true, isParentIntouch = true, hasmDownX = false, hasmoved = false;
    
    private int mTouchSlop;
    
    public SwipMoreView(Context context) {
        super(context);
        init(context);
    }
    
    public boolean isSwipable() {
        return swipable;
    }

    public void setSwipable(boolean swipable) {
        this.swipable = swipable;
    }

    public SwipMoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    
    public SwipMoreView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }
    
    private void init(Context context) {
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
        postDelayed(new Runnable() {
            
            @Override
            public void run() {
                moreView().setVisibility(View.INVISIBLE);
            }
        }, 10);
    }
    
    public abstract boolean swipLeftAble();
    
    public abstract boolean swipRightAble();
    
    public abstract View swipView();
    
    public abstract View moreView();
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!this.swipable) {
            return super.onInterceptTouchEvent(ev);
        }
        final int action = ev.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_MOVE: {
                if (!hasmDownX) {
                    mDownX = ev.getRawX();
                    nowX = ViewHelper.getX(swipView());
                    hasmDownX = true;
                }
                
                if (Math.abs(ev.getRawX() - mDownX) > mTouchSlop) {
                    return true;
                }
                break;
            }
            
            case MotionEvent.ACTION_DOWN: {
                mDownX = ev.getRawX();
                hasmoved = false;
                nowX = ViewHelper.getX(swipView());
                hasmDownX = true;
                break;
            }
            
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mDownX = 0;
                hasmoved = false;
                hasmDownX = false;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!this.swipable) {
            return false;
        }
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                mDownX = ev.getRawX();
                hasmoved = false;
                nowX = ViewHelper.getX(swipView());
                this.hasmDownX = true;
                break;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                if (!isParentIntouch) {
                    requestParentDisallowInterceptTouchEvent(false);
                }
                float nx = ViewHelper.getX(swipView());
                float to = 0;
                View v = moreView();
                int margs[]=getSiwpViewMargin();
                if (v != null) {
                    float mv = v.getWidth();
                    if (mv / 2 < nx) {
                        to = mv;
                    } else if (-mv / 2 > nx) {
                        to = -mv+margs[1]+margs[0];
                    } else {
                        to = 0+margs[0];
                    }
                }
                final  boolean ishide = to==0+margs[0];
                ViewPropertyAnimator.animate(swipView()).x(to).alpha(1).setDuration(250).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if(ishide){
                            moreView().setVisibility(View.INVISIBLE);
                        }
                    }
                });
                mDownX = 0;
                hasmoved = false;
                nowX = ViewHelper.getX(swipView());
                hasmDownX = false;
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (Math.abs(ev.getRawX() - mDownX) > mTouchSlop && isParentIntouch) {
                    requestParentDisallowInterceptTouchEvent(true);
                    if (!hasmoved) {
                        mDownX = ev.getRawX();
                        hasmoved = true;
                    }
                }
                if (hasmoved) {
                    float deltaX = nowX + ev.getRawX() - mDownX;
                    View v = moreView();
                    int margs[]=getSiwpViewMargin();
                    if (v != null) {
                        float mv = v.getWidth();
                        if (!swipLeftAble()) {
                            if (deltaX < 0) {
                                deltaX = 0;
                            }
                        }
                        if (!swipRightAble()) {
                            if (deltaX > 0) {
                                deltaX = 0;
                            }
                        }
                        if (deltaX > 0) {
                            ViewHelper.setX(v, 0);
                            if (deltaX > mv) {
                                deltaX = mv;
                            }
                        }
                        if (deltaX < 0) {
                            ViewHelper.setX(v, getWidth() - mv);
                            if (deltaX < -mv+margs[1]+margs[0]) {
                                deltaX = -mv+margs[1]+margs[0];
                            }
                        }
                    }
                    if (deltaX != 0) {
                        moreView().setVisibility(View.VISIBLE);
                    }else{
                        deltaX=margs[0];
                    }
                    ViewHelper.setX(swipView(), deltaX);
                }
                break;
            }
        }
        super.onTouchEvent(ev);
        return true;
    }
    
    public int[] getSiwpViewMargin(){
        int margs[]=new int[]{0,0};
        ViewGroup.LayoutParams layout=swipView().getLayoutParams();
        if(layout instanceof MarginLayoutParams){
            margs[0]=((MarginLayoutParams) layout).leftMargin;
            margs[1]=((MarginLayoutParams) layout).rightMargin;
        }
        return margs;
    }
    
    public void moveBack() {
        int margs[]=getSiwpViewMargin();
        ViewPropertyAnimator.animate(swipView()).x(0+margs[0]).alpha(1).setDuration(250).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                moreView().setVisibility(View.INVISIBLE);
            }
        });
   
    }
    
    public void reset() {
        ViewHelper.setX(swipView(), 0);
        moreView().setVisibility(View.INVISIBLE);
    }
    
    private void requestParentDisallowInterceptTouchEvent(boolean disallowIntercept) {
        isParentIntouch = !disallowIntercept;
        final ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(disallowIntercept);
        }
    }
}
