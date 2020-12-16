/*
 * 文件名: DataFormat.java
 * 版    权：  Copyright XCDS Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: ryan
 * 创建时间:2014-4-11
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.mdx.framework.widget.util;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import com.mdx.framework.adapter.MAdapter;
import com.mdx.framework.server.api.Son;
import com.mdx.framework.widget.pagerecycleview.MRecyclerView;

import java.io.Serializable;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 *
 * @author ryan
 * @version [2014-4-11 上午11:07:01]
 */
public abstract class DataFormat implements Serializable {
    public MRecyclerView mrecyclerView;

    public MAdapter<?> getAdapter(Context context, Son son, int page) {
        return null;
    }

    public Object getCardAdapter(Context context, Son son, int page) {
        return null;
    }

    public void updateCardAdapter(Context context, Son son, RecyclerView.Adapter adapter) {
    }

    public abstract boolean hasNext();

    public abstract String[][] getPageNext();

    public abstract void reload();


}
