/*
 * 文件名: MPagerAdapter.java
 * 版    权：  Copyright XCDS Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: ryan
 * 创建时间:2014年5月16日
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.mdx.framework.adapter;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.mdx.framework.widget.MImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 * @author ryan
 * @version [2014年5月16日 下午1:13:03] 
 */
public class MPagerAdapter<T> extends PagerAdapter{
    private Stack<View> mViews=new Stack<View>();
    private Context context;
    private ViewPager mViewPager;
    private List<T> mDataList=new ArrayList<T>();
    private MAdapter<T> mAdapter;

    
    
    public MPagerAdapter(Context context,List<T> list){
        this.context=context;
        mDataList=list;
    }
    
    public MPagerAdapter(Context context,MAdapter<T> adapter){
        this.context=context;
        mAdapter= adapter;
    }
    
    public Context getContext(){
        return context;
    }
    
    @Override
    public int getCount() {
        if(mAdapter!=null){
            return mAdapter.getCount();
        }
        return  mDataList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
    
    
    public View getView(int position, View convertView) {
        if(mAdapter!=null){
            return mAdapter.getView(position, convertView, null);
        }
        MImageView mv= new MImageView(getContext());
        return mv;
    }
    
    
    @Override
    public Object instantiateItem(ViewGroup rootview, int position) {
        mViewPager=(ViewPager) rootview;
        View contentView=null;
        if(mViews.size()>0){
            contentView=mViews.pop();
        }
        contentView=getView(position, contentView);
        mViewPager.addView(contentView);
        return contentView;
    }

    @Override
    public void destroyItem(ViewGroup arg0, int position, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);
        mViewPager=(ViewPager) arg0;
        mViews.push((View) arg2);
    }
    
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0==arg1;
    }
    
    public T get(int position){
        if(mAdapter!=null){
            return mAdapter.get(position);
        }
        return mDataList.get(position);
    }
}
