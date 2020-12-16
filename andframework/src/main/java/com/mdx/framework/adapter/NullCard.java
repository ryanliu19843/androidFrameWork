/*
 * 文件名: NullCard.java
 * 版    权：  Copyright XCDS Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: ryan
 * 创建时间:2015-8-25
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.mdx.framework.adapter;

import android.content.Context;
import android.view.View;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 * @author ryan
 * @version [2015-8-25 下午5:03:16] 
 */
public class NullCard extends Card<String>{

    
    public NullCard() {
        this.type = 1008;
    }
    
    /**
     * [一句话功能简述]<BR>
     * [功能详细描述]
     * @param context
     * @param contentView
     * @return
     * @see Card#getView(Context, View)
     */
    
    @Override
    public View getView(Context context, View contentView) {
        if (contentView == null) {
            contentView = new View(context);
        }
        return contentView;
    }
    
}
