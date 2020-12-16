/*
 * 文件名: Card.java
 * 版    权：  Copyright XCDS Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: ryan
 * 创建时间:2014年6月27日
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
 * @version [2014年6月27日 下午2:00:07] 
 */
public abstract class Card<T> {
    protected int type=0;
    protected CardAdapter mCardAdapter;
    protected T mData;
    
    public abstract View getView(Context context,View contentView);
    
    public int getCardType(){
        return type;
    }

    public CardAdapter getAdapter() {
        return mCardAdapter;
    }

    public void setAdapter(CardAdapter adapter) {
        this.mCardAdapter = adapter;
    }
    
    public void setData(T obj){
        this.mData=obj;
    }
    
    public T getData(){
        return mData;
    }
}
