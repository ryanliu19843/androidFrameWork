/*
 * 文件名: MDigitalClock.java 版 权： Copyright XCDS Tech. Co. Ltd. All Rights
 * Reserved. 描 述: [该类的简要描述] 创建人: ryan 创建时间:2014-1-14
 * 
 * 修改人： 修改时间: 修改内容：[修改内容]
 */
package com.mdx.framework.widget;

import java.util.Calendar;
import java.util.TimeZone;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * @author ryan
 * @version [2014-1-14 上午10:51:00]
 */
public class TimeClock extends LinearLayout {
    private boolean mAttached;
    
    private Calendar mTime;
    
    private OnTimeChanged mOnTimeChanged = null;
    
    private String mTimeZone;
    
    private long mStart, mNow, mEnd;
    
    private final ContentObserver mFormatChangeObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            onTimeChanged();
        }
        
        public void onChange(boolean selfChange, Uri uri) {
            onTimeChanged();
        }
    };
    
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mTimeZone == null && Intent.ACTION_TIMEZONE_CHANGED.equals(intent.getAction())) {
                final String timeZone = intent.getStringExtra("time-zone");
                createTime(timeZone);
            }
            onTimeChanged();
        }
    };
    
    private final Runnable mTicker = new Runnable() {
        public void run() {
            onTimeChanged();
            long now = SystemClock.uptimeMillis();
            long next = now + (1000 - now % 1000);
            if(mAttached){
                getHandler().postAtTime(mTicker, next);
            }
        }
    };
    
    public TimeClock(Context context) {
        super(context);
        init();
    }
    
    public TimeClock(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    private void init() {
        createTime(mTimeZone);
    }
    
    private void createTime(String timeZone) {
        if (timeZone != null) {
            mTime = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
        } else {
            mTime = Calendar.getInstance();
        }
    }
    
    public String getTimeZone() {
        return mTimeZone;
    }
    
    public void setTimeZone(String timeZone) {
        mTimeZone = timeZone;
        
        createTime(timeZone);
        onTimeChanged();
    }
    
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        
        startRun();
    }
    
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        unRun();
    }
    
    private void startRun() {
        if(getHandler()==null){
            return;
        }
        if (!mAttached) {
            mAttached = true;
            
            registerReceiver();
            registerObserver();
            
            createTime(mTimeZone);
            mTicker.run();
            onTimeChanged();
        }
    }
    
    private void unRun() {
        if (mAttached) {
            mAttached = false;
            unregisterReceiver();
            unregisterObserver();
            
            getHandler().removeCallbacks(mTicker);
        }
    }
    
    private void registerReceiver() {
        final IntentFilter filter = new IntentFilter();
        
        filter.addAction(Intent.ACTION_TIME_TICK);
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        
        getContext().registerReceiver(mIntentReceiver, filter, null, getHandler());
    }
    
    private void registerObserver() {
        final ContentResolver resolver = getContext().getContentResolver();
        resolver.registerContentObserver(Settings.System.CONTENT_URI, true, mFormatChangeObserver);
    }
    
    private void unregisterReceiver() {
        getContext().unregisterReceiver(mIntentReceiver);
    }
    
    private void unregisterObserver() {
        final ContentResolver resolver = getContext().getContentResolver();
        resolver.unregisterContentObserver(mFormatChangeObserver);
    }
    
    private void onTimeChanged() {
        mTime.setTimeInMillis(System.currentTimeMillis());
        if (this.mOnTimeChanged != null) {
            long last = mEnd - mStart - mTime.getTimeInMillis() + mNow;
            if (last <= 0) {
                unRun();
                if(this.mOnTimeChanged!=null){
                    this.mOnTimeChanged.onTimeChanged(mTime, 0);
                    this.mOnTimeChanged.onTimeEnd(mTime);
                }
            }else{
                if(this.mOnTimeChanged!=null){
                    this.mOnTimeChanged.onTimeChanged(mTime, last);
                }
            }
        }
    }
    
    public void set(long start, long now, long end) {
        this.mStart = start;
        this.mNow = now;
        this.mEnd = end;
        startRun();
        onTimeChanged();
    }
    
    public interface OnTimeChanged {
        public void onTimeChanged(Calendar time, long endtime);
        public void onTimeEnd(Calendar time);
    }
    
    public OnTimeChanged getOnTimeChanged() {
        return mOnTimeChanged;
    }
    
    public void setOnTimeChanged(OnTimeChanged mOnTimeChanged) {
        this.mOnTimeChanged = mOnTimeChanged;
    }
    
    public void add(OnTimeChanged timechange) {
        if (timechange != null && timechange instanceof View) {
            this.addView((View) timechange);
            this.mOnTimeChanged = timechange;
        }
    }
}
