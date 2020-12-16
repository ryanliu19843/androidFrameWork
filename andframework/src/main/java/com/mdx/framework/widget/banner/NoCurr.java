/*
 * 文件名: CirleCurr.java 版 权： Copyright XCDS Tech. Co. Ltd. All Rights Reserved. 描
 * 述: [该类的简要描述] 创建人: ryan 创建时间:2014年5月16日
 * 
 * 修改人： 修改时间: 修改内容：[修改内容]
 */
package com.mdx.framework.widget.banner;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.mdx.framework.R;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 * 
 * @author ryan
 * @version [2014年5月16日 下午12:51:05]
 */
public class NoCurr extends CirleCurr {
    public NoCurr(Context context) {
        super(context);
        init();
    }
    
    public NoCurr(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    public NoCurr(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    
    protected void init() {
        LayoutInflater f = LayoutInflater.from(getContext());
        f.inflate(R.layout.baner_viewpager_nocurr, this);
        mViewpager = (ViewPager) findViewById(R.id.framework_banner_viewpager);
        sendScrollMessage(interval);
    }
}
