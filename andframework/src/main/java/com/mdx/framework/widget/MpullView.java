/*
 * 文件名: MpullView.java 版 权： Copyright XCDS Tech. Co. Ltd. All Rights Reserved. 描
 * 述: [该类的简要描述] 创建人: ryan 创建时间:2014-4-10
 * 
 * 修改人： 修改时间: 修改内容：[修改内容]
 */
package com.mdx.framework.widget;

import com.mdx.framework.R;
import com.mdx.framework.widget.util.PullView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 * 
 * @author ryan
 * @version [2014-4-10 下午3:28:24]
 */
public class MpullView extends LinearLayout implements PullView {
	public View progress, array;

	public TextView state, time;

	public int lastType = 2;

	public boolean isUp = false;

	public boolean isloading = false;

	public MpullView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.pull_refresh_mheader, this);
		progress = findViewById(R.id.progress);
		array = findViewById(R.id.array);
		state = (TextView) findViewById(R.id.stateview);
		time = (TextView) findViewById(R.id.timeview);
		// progress.setVisibility(View.GONE);
	}

	/**
	 * 
	 * [一句话功能简述]<BR>
	 * [功能详细描述]
	 * 
	 * @param scroll
	 * @param height
	 * @param type
	 * @see PullView#setScroll(float, int, int)
	 */
	@Override
	public void setScroll(float scroll, int height, int type) {
		if (scroll <= height - getHeight() && type == 0) {
			progress.setVisibility(View.GONE);
			array.setVisibility(View.GONE);
			isloading = false;
			return;
		}
		if (isloading) {
			return;
		}
		if (scroll > height && type == 0) {
			state.setText(R.string.pull_state_a);
			progress.setVisibility(View.GONE);
			array.setVisibility(View.VISIBLE);
			if (!isUp) {
				RotateAnimation rotateR = new RotateAnimation(0, 180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
				rotateR.setInterpolator(new LinearInterpolator());
				rotateR.setDuration(250);
				rotateR.setFillAfter(true);
				array.startAnimation(rotateR);
				isUp = true;
			}
		} else if (type == 0) {
			if (isUp) {
				RotateAnimation rotateL = new RotateAnimation(180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
				rotateL.setInterpolator(new LinearInterpolator());
				rotateL.setDuration(250);
				rotateL.setFillAfter(true);
				array.startAnimation(rotateL);
				isUp = false;
			}
			state.setText(R.string.pull_state_b);
			progress.setVisibility(View.GONE);
			array.setVisibility(View.VISIBLE);
		} else {
			array.clearAnimation();
			state.setText(R.string.pull_state_c);
			isloading = true;
			isUp = false;
			progress.setVisibility(View.VISIBLE);
			array.setVisibility(View.GONE);
			return;
		}
	}

	@Override
	public void setTime(long time) {

	}

}
