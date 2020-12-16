/*
 * 文件名: SLinearLayout.java 版 权： Copyright XCDS Tech. Co. Ltd. All Rights
 * Reserved. 描 述: [该类的简要描述] 创建人: ryan 创建时间:2015-7-16
 * 
 * 修改人： 修改时间: 修改内容：[修改内容]
 */
package com.mdx.framework.widget.suit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.mdx.framework.activity.BaseActivity;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 * 
 * @author ryan
 * @version [2015-7-16 下午9:44:03]
 */
public class SLinearLayout extends LinearLayout {
    public SLinearLayout(Context context) {
        super(context);
    }
    
    public SLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    @SuppressLint("NewApi")
    public SLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    
    @SuppressLint("NewApi")
    public SLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    
    
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }
    
    
}
