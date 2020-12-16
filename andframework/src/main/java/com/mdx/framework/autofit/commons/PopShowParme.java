package com.mdx.framework.autofit.commons;

import android.view.View;

/**
 * Created by ryan on 2017/10/28.
 */

public class PopShowParme {
    public static final int DROPDOWN_ASVIEW = 0;
    public static final int DROPDOWN_ASVIEWOFF = 1;
    public static final int DROPDOWN_ASVIEWOF = 2;
    public static final int DROPDOWN_ASLOCAL = 3;
    public static final int DROPDOWN_AUTO = 9;

    public View view;
    public int xoff, yoff, gravty;
    public int x, y;
    public int type = 0;
}
