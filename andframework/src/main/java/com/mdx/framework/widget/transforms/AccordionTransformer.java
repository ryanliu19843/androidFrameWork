package com.mdx.framework.widget.transforms;

import com.nineoldandroids.view.ViewHelper;

import android.view.View;

public class AccordionTransformer extends ABaseTransformer {

    /**
     * [一句话功能简述]<BR>
     * [功能详细描述]
     * @param view
     * @param position
     * @see ABaseTransformer#onTransform(View, float)
     */
    @Override
    protected void onTransform(View view, float position) {
        ViewHelper.setPivotX(view,position < 0 ? 0 : view.getWidth());
        ViewHelper.setScaleX(view,position < 0 ? 1f + position : 1f - position);
    }
}
