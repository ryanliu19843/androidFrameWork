package com.mdx.framework.widget.transforms;

import com.nineoldandroids.view.ViewHelper;

import android.view.View;

public class RotateUpTransformer extends ABaseTransformer {

	private static final float ROT_MOD = -15f;

	@Override
	protected void onTransform(View view, float position) {
		final float width = view.getWidth();
		final float rotation = ROT_MOD * position;

		ViewHelper.setPivotX(view,width * 0.5f);
		ViewHelper.setPivotY(view,0f);
		ViewHelper.setTranslationX(view,0f);
		ViewHelper.setRotation(view,rotation);
	}
	
	@Override
	protected boolean isPagingEnabled() {
		return true;
	}

}
