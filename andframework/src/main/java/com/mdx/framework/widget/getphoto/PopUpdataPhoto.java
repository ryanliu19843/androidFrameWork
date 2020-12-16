package com.mdx.framework.widget.getphoto;

import java.util.ArrayList;
import java.util.HashMap;

import com.mdx.framework.activity.NoTitleAct;
import com.mdx.framework.activity.PNoTitleAct;
import com.mdx.framework.activity.PTitleAct;
import com.mdx.framework.frg.multiplephoto.MultiplePhotoSelect;
import com.mdx.framework.utility.Helper;
import com.mdx.framework.widget.selectphotos.SelectPhoto;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;

public class PopUpdataPhoto {// CanShow,

    private Context context;

    private OnReceiverPhoto onReceiverPhoto;

    private OnReceiverPhotos onReceiverPhotos;

    public int offerY = 0;

    private HashMap<String, Object> map = new HashMap<String, Object>();

    /**
     * function is deprecated pls use Helper.getPhoto;
     *
     *
     */
    @Deprecated
    public PopUpdataPhoto(Context context, View view) {
        this.context = context;
    }

    /**
     * function is deprecated pls use Helper.getPhoto;
     *
     *
     */
    @Deprecated
    public PopUpdataPhoto(Context context, OnReceiverPhoto onReceiverPhoto, OnReceiverPhotos onReceiverPhotos) {
        this.context = context;
        this.onReceiverPhoto = onReceiverPhoto;
        this.onReceiverPhotos = onReceiverPhotos;
    }

    public void putParams(String key, Integer value) {
        map.put(key, value);
    }

    /**
     * function is deprecated pls use Helper.getPhoto;
     *

     *
     */
    @Deprecated
    public void show() {
        // Intent pick = new Intent(context, ActCameraStream.class);
        // for (String key : map.keySet()) {
        // pick.putExtra(key, map.get(key));
        // }
        map.put("finishtype", 1);
        Helper.startActivity(context, SelectPhoto.class, PNoTitleAct.class, map);
        // context.startActivity(pick);
        startReceive();
    }

    /**
     * function is deprecated pls use Helper.getPhoto;
     *

     *
     */
    @Deprecated
    public void shown() {
        // Intent pick = new Intent(context, ActCameraStream.class);
        // for (String key : map.keySet()) {
        // pick.putExtra(key, map.get(key));
        // }
        map.put("finishtype", 3);
        Helper.startActivity(context, SelectPhoto.class, NoTitleAct.class, map);
        // context.startActivity(pick);
        startReceive();
    }

    private void startReceive() {
        if (onReceiverPhoto == null && onReceiverPhotos == null) {
            return;
        }
        try {
            context.unregisterReceiver(mBroadcastReceiver);
        }
        catch (Exception e) {

        }
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(ActCameraStream.RECEIVER_PHOTO);
        context.registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    private void endReceive() {
        try {
            context.unregisterReceiver(mBroadcastReceiver);
        }
        catch (Exception e) {

        }
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int i = intent.getExtras().getInt("type", 1);
            if (i == 2) {
                ArrayList<String> list = (ArrayList<String>) intent.getExtras().getSerializable("list");
                onReceiverPhotos.onReceiverPhoto(list);
            } else if (i != 0) {
                String str = intent.getExtras().getString("what");
                int w = intent.getExtras().getInt("width", 0);
                int h = intent.getExtras().getInt("height", 0);
                onReceiverPhoto.onReceiverPhoto(str, w, h);
            }
            endReceive();
        }
    };

    public void setOnReceiverPhoto(OnReceiverPhoto onreceiverPhoto) {
        onReceiverPhoto = onreceiverPhoto;
    }

    public interface OnReceiverPhoto {
        public void onReceiverPhoto(String photoPath, int width, int height);
    }

    public interface OnReceiverPhotos {
        public void onReceiverPhoto(ArrayList<String> photos);
    }
}
