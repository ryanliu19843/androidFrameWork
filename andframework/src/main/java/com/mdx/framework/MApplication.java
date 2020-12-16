package com.mdx.framework;

import android.app.Application;
import android.os.StrictMode;

public class MApplication extends Application {
    
    @Override
    public void onCreate() {
        super.onCreate();
        Frame.init(getApplicationContext());
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }
    
}
