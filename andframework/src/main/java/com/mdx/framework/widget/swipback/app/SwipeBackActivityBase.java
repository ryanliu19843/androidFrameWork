package com.mdx.framework.widget.swipback.app;

import com.mdx.framework.widget.swipback.SwipeBackLayout;

/**
 * @author Yrom
 */
public interface SwipeBackActivityBase {
	/**
	 * @return the SwipeBackLayout associated with this activity.
	 */
	public abstract SwipeBackLayout getSwipeBackLayout();

	public abstract void setSwipeBackEnable(boolean enable);

	/**
	 * Scroll out contentView and finish the activity
	 */
	public abstract void scrollToFinishActivity();

}
