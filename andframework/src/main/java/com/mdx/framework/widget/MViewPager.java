/*
 * 文件名: MViewPager.java 版 权： Copyright XCDS Tech. Co. Ltd. All Rights Reserved.
 * 描 述: [该类的简要描述] 创建人: ryan 创建时间:2014年5月7日
 * 
 * 修改人： 修改时间: 修改内容：[修改内容]
 */
package com.mdx.framework.widget;

import com.mdx.framework.widget.util.MScrollAble;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 * 
 * @author ryan
 * @version [2014年5月7日 下午7:21:28]
 */
public class MViewPager extends ViewPager implements MScrollAble {
    private boolean isScrollAble = true, InterceptAble = false;
    
    public MViewPager(Context context) {
        super(context);
    }
    
    public MViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isScrollAble) {
        	try {
        		return super.onInterceptTouchEvent(ev);
			} catch (Throwable e) {
				return false;
			}
        } else {
            return InterceptAble;
        }
    }
    
    @Override
    public void setScrollAble(boolean bol) {
        isScrollAble = bol;
    }
    
    @Override
    public boolean isScrollAble() {
        return isScrollAble;
    }
    
    @Override
    public void setIntercept(boolean bol) {
        InterceptAble = bol;
    }

}
