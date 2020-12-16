package com.mdx.framework.adapter;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public abstract class LoopPagerAdapter<T> extends PagerAdapter{
	private Stack<View> mViews=new Stack<>();
	private List<T> mDataList=new ArrayList<>();
	private Context context;
	private ViewPager mViewPager;
	private int offset=0;
	
	public LoopPagerAdapter(Context context,List<T> list){
		this.context=context;
		mDataList=list;
	}


    public LoopPagerAdapter(Context context,T[] list){
        this.context=context;
        for(T t:list){
            mDataList.add(t);
        }
    }

	public Context getContext(){ 
		return context;
	}
	
	public int getSize(){
		return mDataList.size();
	}
	
	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}
	
	
	public abstract View getView(int position, View convertView) ;
	
	
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
	public void destroyItem(ViewGroup viewgroup, int position, Object object) {
		((ViewPager) viewgroup).removeView((View) object);
		mViewPager=(ViewPager) viewgroup;
		mViews.push((View) object);
	}
	
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0==arg1;
	}
	
	public T get(int position){
		return mDataList.get(getPosition(position));
	}
	
	public T getItem(int position){
		return mDataList.get(position);
	}
	
	public int getPosition(int position){
	    if(getSize()>0){
	        return (position+offset)%getSize();
	    }
	    return 0;
	}

	public int getSposition(int posion){
		return posion;
	}
	
	public void addLeft(T item){
		offset=1;
		this.notifyDataSetChanged();
		offset=0;
		mDataList.add(0,item);
		mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1);
		this.notifyDataSetChanged();
	}
	
	public void addRight(T item){
		mDataList.add(item);
		this.notifyDataSetChanged();
	}
}
