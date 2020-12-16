package com.mdx.framework.widget.pagerecycleview;

import com.mdx.framework.widget.pagerecycleview.widget.OnSwipLoadListener;

/**
 * Created by ryan on 2017/10/28.
 */

public interface IRecyclerView {

    void setOnSwipLoadListener(OnSwipLoadListener onSwipLoadListener);
    void pullloadelay();
    void pullLoad();
    void reload();
}
