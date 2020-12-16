package com.mdx.framework.widget.transforms;

import com.nineoldandroids.view.ViewHelper;

import android.view.View;

public class FlipHorizontalTransformer extends ABaseTransformer {

	@Override
	protected void onTransform(View view, float position) {
		final float rotation = 180f * position;

		ViewHelper.setAlpha(view,rotation > 90f || rotation < -90f ? 0 : 1);
		ViewHelper.setPivotX(view,view.getWidth() * 0.5f);
		ViewHelper.setPivotY(view,view.getHeight() * 0.5f);
		ViewHelper.setRotationY(view,rotation);
	}

}
