/*
 * 文件名: DefaultClickListener.java
 * 版    权：  Copyright XCDS Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: Administrator
 * 创建时间:2014-12-19
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.mdx.framework.utility;

import android.view.View;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 * @author Administrator
 * @version [2014-12-19 上午11:30:15] 
 */
public class DelayClickListener implements View.OnClickListener{
    private View.OnClickListener click;
    private long lastClickTime=0;

    public DelayClickListener(View.OnClickListener click){
        this.click=click;
    }
    
    @Override
    public void onClick(View v) {
        if(System.currentTimeMillis()-lastClickTime>1000){
            lastClickTime=System.currentTimeMillis();
            click.onClick(v);
        }
    }
    
}
