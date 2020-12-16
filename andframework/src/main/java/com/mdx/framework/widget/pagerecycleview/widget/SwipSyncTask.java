package com.mdx.framework.widget.pagerecycleview.widget;


import com.mdx.framework.widget.pagerecycleview.ada.CardAdapter;

/**
 * Created by ryan on 2017/5/4.
 */

public abstract class SwipSyncTask {

    public OnSyncPageSwipListener onPageSwipListener;
    public boolean haspage;
    public String error;

    public void setOnSyncPageSwipListener(OnSyncPageSwipListener onPageSwipListener) {
        this.onPageSwipListener = onPageSwipListener;
    }


    protected abstract CardAdapter doInBackground(Object... params);

    protected void onPostExecute(CardAdapter result) {
        if (onPageSwipListener != null) {
            this.onPageSwipListener.setAdapter(result, haspage, error);
        }
    }


    protected void onPreExecute() {
        error = null;
        haspage = false;
    }


    public void intermit() {
    }
}
