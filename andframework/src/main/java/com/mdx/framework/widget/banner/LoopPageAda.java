package com.mdx.framework.widget.banner;

import com.mdx.framework.adapter.MAdapter;
import com.mdx.framework.widget.banner.views.InfinitePagerAdapter;
import com.mdx.framework.widget.pagerecycleview.ada.CardAdapter;

/**
 * Created by ryan on 2017/5/18.
 */

public class LoopPageAda extends InfinitePagerAdapter {

    public LoopPageAda(MAdapter adapter) {
        super(adapter);
    }

    public LoopPageAda(CardAdapter adapter) {
        super(adapter);
    }
}
