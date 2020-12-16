/*
 * 文件名: MTextView.java 版 权： Copyright Huawei Tech. Co. Ltd. All Rights Reserved.
 * 描 述: [该类的简要描述] 创建人: ryan 创建时间:2014年5月23日
 *
 * 修改人： 修改时间: 修改内容：[修改内容]
 */
package com.mdx.framework.widget.marqueeview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 *
 * @author ryan
 * @version [2014年5月23日 下午1:45:46]
 */
@SuppressLint("AppCompatCustomView")
public class MarqueeTextView extends TextView {
    /**
     * [构造简要说明]
     *
     * @param context
     */
    public MarqueeTextView(Context context) {
        super(context);
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
