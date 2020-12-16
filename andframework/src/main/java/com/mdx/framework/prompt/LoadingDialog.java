package com.mdx.framework.prompt;

import com.mdx.framework.Frame;
import com.mdx.framework.R;

import android.app.ProgressDialog;
import android.content.Context;

public class LoadingDialog implements Prompt {
    private ProgressDialog mpdialog;

    public LoadingDialog(Context context) {
        mpdialog = new ProgressDialog(context);
        mpdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mpdialog.setMessage(Frame.CONTEXT.getString(R.string.loading_load));
        mpdialog.setIndeterminate(true);
        mpdialog.setCancelable(Boolean.valueOf(Frame.CONTEXT.getResources().getString(R.string.loading_cancleable)));
    }

    public void pshow(boolean isshow) {
        if (mpdialog == null) {
            return;
        }
        mpdialog.show();
    }

    public void pdismiss(boolean isshow) {
        if (mpdialog == null) {
            return;
        }
        mpdialog.dismiss();
    }

    @Override
    public boolean isShowing() {
        if (mpdialog == null) {
            return false;
        }
        return mpdialog.isShowing();
    }

    @Override
    public boolean canShow() {
        return true;
    }

}
