package com.mdx.framework.widget.pagerecycleview.viewhold;

import android.graphics.Rect;

/**
 * Created by ryan on 2016/6/30.
 */
public class ViewHodeParam {

    public int frameType = 0;

    public int showType = 0;

    public Rect rect;

    public int width=-99,height=-99;

    public ViewHodeParam(int frameType, Rect rect) {
        this.frameType = frameType;
        this.rect = rect;
    }


    public ViewHodeParam() {
    }
}
