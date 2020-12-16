package com.mdx.framework.widget;



import com.mdx.framework.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class FootLoadingView extends LinearLayout {	
	public FootLoadingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public FootLoadingView(Context context) {
		super(context);
		init(context);
	}
	
	private void init(Context context){
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.page_list_footloadingview, this);
	}
}
