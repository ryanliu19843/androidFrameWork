package com.mdx.framework.activity;

import android.os.Bundle;

import com.mdx.framework.widget.swipback.app.SwipeBackActivityActionBar;

public abstract class MActivityActionbar extends SwipeBackActivityActionBar {

	@Override
	protected void initcreate(Bundle savedInstanceState) {
	}


	public abstract void setNavTintColor(int color);

	public abstract void setStaTintColor(int color);

	public void setTintColor(int color){
		this.setNavTintColor(color);
		this.setStaTintColor(color);
	}
}
