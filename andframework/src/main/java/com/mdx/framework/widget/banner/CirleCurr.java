/*
 * 文件名: CirleCurr.java 版 权： Copyright XCDS Tech. Co. Ltd. All Rights Reserved. 描
 * 述: [该类的简要描述] 创建人: ryan 创建时间:2014年5月16日
 * 
 * 修改人： 修改时间: 修改内容：[修改内容]
 */
package com.mdx.framework.widget.banner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.mdx.framework.R;
import com.mdx.framework.adapter.MAdapter;
import com.mdx.framework.widget.pagerecycleview.ada.CardAdapter;
import com.mdx.framework.widget.viewpagerindicator.CirclePageIndicator;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 * 
 * @author ryan
 * @version [2014年5月16日 下午12:51:05]
 */
public class CirleCurr extends FrameLayout {
    protected CirclePageIndicator mIndicator;
    
    protected int transforms = 0;
    
    protected ViewPager mViewpager;
    
    protected boolean isAutoScroll = true;
    
    protected int interval = 5000, SCROLL_WHAT = 999, step = 1;
    
    protected MyHandler handler = new MyHandler();
    
    public CirleCurr(Context context) {
        super(context);
        init();
    }
    
    public CirleCurr(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    public CirleCurr(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    
    protected void init() {
        LayoutInflater f = LayoutInflater.from(getContext());
        f.inflate(R.layout.baner_viewpager_cirle, this);
        mViewpager = (ViewPager) findViewById(R.id.framework_banner_viewpager);
        setTransforms(transforms);
        mIndicator = (CirclePageIndicator) findViewById(R.id.framework_banner_indicator);
        sendScrollMessage(interval);
    }
    
    protected void sendScrollMessage(long delayTimeInMills) {
        if (isAutoScroll) {
            handler.removeMessages(SCROLL_WHAT);
            handler.sendEmptyMessageDelayed(SCROLL_WHAT, delayTimeInMills);
        }
    }
    
    @SuppressLint("HandlerLeak")
    protected class MyHandler extends Handler {
        
        @Override
        public void handleMessage(Message msg) {
            if (isAutoScroll) {
                scrollOnce();
                sendScrollMessage(interval);
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        sendScrollMessage(interval);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        handler.removeMessages(SCROLL_WHAT);
    }
    
    public void scrollOnce() {
        PagerAdapter mp = mViewpager.getAdapter();
        if (mp != null) {
            int curritem = mViewpager.getCurrentItem();
            if(mp instanceof LoopPageAda){
                int relcount = ((LoopPageAda) mp).getRealCount();
                if(curritem==relcount*3){
                    curritem=relcount*2;
                    mViewpager.setCurrentItem(relcount*2,false);
                }
                if(relcount==0){
                    return;
                }
                mViewpager.setCurrentItem(curritem+step);
            }else{
                int c = mp.getCount();
                if(c==0){
                    return;
                }
                if (c > 1 && curritem >= 0) {
                    if (curritem == c - 1) {
                        step = -1;
                    } else if (curritem == 0) {
                        step = 1;
                    }
                    mViewpager.setCurrentItem(step + curritem);
                }
            }
        }
    }

    public PagerAdapter getAdapter(){
        return mViewpager.getAdapter();
    }
    
    public void setAdapter(PagerAdapter pageadapter) {
        if (pageadapter != null) {
            mViewpager.setAdapter(pageadapter);
            if (mIndicator != null) {
                mIndicator.setViewPager(mViewpager);
//                mIndicator.setOnPageChangeListener(listener);
            }
        } else {
            if (mIndicator != null) {
                mIndicator.setViewPager(null);
            }
        }
    }

    public void setCurr(int ind){
        PagerAdapter adapter=mViewpager.getAdapter();
        if(adapter instanceof LoopPageAda){
            int relcount=((LoopPageAda) adapter).getRealCount();
            mViewpager.setCurrentItem(ind%relcount+relcount*2,false);
        }else{
            mViewpager.setCurrentItem(ind);
        }
    }
    
    public void setFillColor(int fillColor) {
        mIndicator.setFillColor(fillColor);
    }
    public void setPageColor(int fillColor) {
        mIndicator.setPageColor(fillColor);
    }
  
    public void setAdapter(MAdapter pageadapter) {
        if (pageadapter != null) {
            mViewpager.setAdapter(new LoopPageAda(pageadapter));
            mViewpager.setCurrentItem(pageadapter.getCount()*2,false);
//            mViewpager.addOnPageChangeListener(listener);
            if (mIndicator != null) {
                mIndicator.setViewPager(mViewpager);
            }
        } else {
            if (mIndicator != null) {
                mIndicator.setViewPager(null);
            }
        }
    }


    public void setAdapter(CardAdapter pageadapter) {
        if (pageadapter != null) {
            mViewpager.setAdapter(new LoopPageAda(pageadapter));
            mViewpager.setCurrentItem(pageadapter.getItemCount()*2,false);
//            mViewpager.addOnPageChangeListener(listener);
            if (mIndicator != null) {
                mIndicator.setViewPager(mViewpager);
            }
        } else {
            if (mIndicator != null) {
                mIndicator.setViewPager(null);
            }
        }
    }

    public boolean isAutoScroll() {
        return isAutoScroll;
    }
    
    public void setAutoScroll(boolean isAutoScroll) {
        this.isAutoScroll = isAutoScroll;
        if (isAutoScroll) {
            sendScrollMessage(interval);
        } else {
            handler.removeMessages(SCROLL_WHAT);
        }
    }
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        sendScrollMessage(interval);
        return super.dispatchTouchEvent(ev);
    }
    
    public int getTransforms() {
        return transforms;
    }
    
    public void setTransforms(int transforms) {
        this.transforms = transforms;
        if (mViewpager != null) {
//            mViewpager.setPageTransformer(true, TransForms.getTransForms(transforms));
        }
    }

//    public ViewPagerEx.OnPageChangeListener listener=new ViewPagerEx.OnPageChangeListener() {
//        @Override
//        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        }
//
//        @Override
//        public void onPageSelected(int position) {
//
//        }
//
//        @Override
//        public void onPageScrollStateChanged(int state) {
//
//        }
//    };
}
