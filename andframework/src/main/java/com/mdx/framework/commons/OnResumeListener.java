package com.mdx.framework.commons;

import android.app.Activity;

public interface OnResumeListener {
    void onResume(Activity activity, String id);

    void onCreate(Activity activity, String id);
}
