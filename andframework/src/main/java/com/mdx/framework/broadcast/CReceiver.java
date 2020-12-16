package com.mdx.framework.broadcast;

import android.content.Context;

public class CReceiver extends BReceiver {
	private OnReceiverListener mOnReceiverListener;
	
	public CReceiver(String action,Object id,Object obj,Context context,OnReceiverListener onReceiverListener){
		super(action,id,obj,context);
		this.mOnReceiverListener=onReceiverListener;
	}
	
	public void onReceive(Context context, BIntent intent){
		mOnReceiverListener.onReceiver(context, intent);
	}
	
}
