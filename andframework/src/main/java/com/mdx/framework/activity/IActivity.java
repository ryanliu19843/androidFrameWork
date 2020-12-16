package com.mdx.framework.activity;

import android.content.Context;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import android.view.View;

import com.mdx.framework.utility.handle.MHandler;
import com.mdx.framework.widget.pagerecycleview.viewhold.MViewHold;

/**
 * Created by ryan on 2017/5/20.
 */

public interface IActivity {
    public boolean getResumed();

    @Nullable
    public <T extends View> T findViewById(@IdRes int id);

    void finish();

    void disposeMsg(int type, Object obj);

    View getContextView();

    Context getContext();

    MViewHold delete();

    MHandler getHandler();
}
