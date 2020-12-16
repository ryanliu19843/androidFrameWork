package com.mdx.framework.widget.selectphotos;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.annotation.RequiresPermission;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mdx.framework.Frame;
import com.mdx.framework.R;
import com.mdx.framework.activity.CameraActivity;
import com.mdx.framework.activity.MFragment;
import com.mdx.framework.utility.Device;
import com.mdx.framework.utility.Helper;
import com.mdx.framework.utility.Util;
import com.mdx.framework.utility.permissions.PermissionRequest;
import com.mdx.framework.widget.getphoto.ActCameraStream;
import com.mdx.framework.widget.selectphotos.utils.PhotosUtil;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by ryan on 2017/12/27.
 */

public class SelectPhoto extends MFragment implements CompoundButton.OnCheckedChangeListener {

    public com.mdx.framework.widget.pagerecycleview.MRecyclerView recycleview;
    public RelativeLayout control;
    public PhotosUtil photosUtil;
    public TextView tv_title;
    public TextView submit;
    public int maxSelectPhoto = 1, mfinishtype;
    private String filePath;

    @Override
    protected void create(Bundle savedInstanceState) {
        setContentView(R.layout.default_selphoto_frg_select_photo);
        initView();
        this.setId("SelectPhoto");

        String temptime = (new StringBuilder()).append(UUID.randomUUID().toString().replace("-", "")).toString();
        filePath = (new StringBuilder(String.valueOf(Util.getDPath(getContext(), "/temp/csimages/").getPath()))).append(temptime)
                .append("_cstempsave.temp")
                .toString();
    }


    private void initView() {
        findVMethod();
        mfinishtype = getActivity().getIntent().getIntExtra("finishtype", 0);
        maxSelectPhoto = getActivity().getIntent().getIntExtra("Max", maxSelectPhoto);
        PhotosUtil.read(this, new PhotosUtil.OnPhotosGet() {
            @Override
            public void onPhotosGet(PhotosUtil photos) {
                photosUtil = photos;
                photosUtil.onCheckedChangeListener = SelectPhoto.this;
                photosUtil.maxselected = maxSelectPhoto;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        autoFit.put("photos", photosUtil);
                        autoFit.put("folder", photosUtil.getImageAll());
                        autoFit.put("directs", photosUtil.getmDirPaths());
                        autoFit.runnable.run();
                        //fillList(photosUtil.getImageAll());
                    }
                });
            }
        });
    }

    private void findVMethod() {
        recycleview = findViewById(R.id.recycleview);
        control = findViewById(R.id.control);
        tv_title = findViewById(R.id.tv_title);
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Submit();
            }
        });
        loaddata();
    }

    private void loaddata() {
        recycleview.setLayoutManager(new GridLayoutManager(getContext(), 4));
        recycleview.setCanPull(false);
    }

    @Override
    public void disposeMsg(int type, Object obj) {
        super.disposeMsg(type, obj);
        if (type == 56) {
            recycleview.getAdapter().notifyDataSetChanged();
        } else if (type == 65) {
            opentogetPhoto();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        String str = getContent().getResources().getString(R.string.submit);
        submit.setText(str + "(" + photosUtil.selecteds.size() + "/" + photosUtil.maxselected + ")");
    }


    public void Submit() {
        result();
    }

    public void opentogetPhoto() {
        if ("google,".indexOf(Device.getBrand() + ",") >= 0) {
            Intent capture = new Intent("android.media.action.IMAGE_CAPTURE");
            capture.putExtra("output", Uri.fromFile(new File(filePath)));
            capture.putExtra("android.intent.extra.screenOrientation", true);
            startActivityForResult(capture, 2);
        } else {
            Intent capture = new Intent(getActivity(), CameraActivity.class);
            capture.putExtra("output", Uri.fromFile(new File(filePath)));
            capture.putExtra("android.intent.extra.screenOrientation", true);
            startActivityForResult(capture, 2);
        }
    }


    private void result() {
        Intent intent = new Intent();
        ArrayList<String> list = new ArrayList<String>();
        for (PhotosUtil.ImageItem img : photosUtil.selecteds) {
            list.add(img.path);
        }
        intent.putExtra("data", list);
        intent.putExtra("type", "ms");

        if (mfinishtype == 3) {
            Intent sendi = new Intent();
            sendi.setAction(ActCameraStream.RECEIVER_PHOTO);
            sendi.putExtra("list", list);
            sendi.putExtra("type", 2);
            getActivity().sendBroadcast(sendi);
        }

        if (mfinishtype == 1) {
            // getActivity().setResult(Activity.RESULT_OK, intent);
            intent.setClass(getActivity(), ActCameraStream.class);
            for (String key : getActivity().getIntent().getExtras().keySet()) {
                Object obj = getActivity().getIntent().getExtras().get(key);
                if ((obj instanceof Boolean)) {
                    intent.putExtra(key, (Boolean) obj);
                } else if ((obj instanceof Integer)) {
                    intent.putExtra(key, (Integer) obj);
                } else if ((obj instanceof Float)) {
                    intent.putExtra(key, (Float) obj);
                } else if ((obj instanceof Double)) {
                    intent.putExtra(key, (Double) obj);
                } else if ((obj instanceof Long)) {
                    intent.putExtra(key, (Long) obj);
                } else if ((obj instanceof String)) {
                    intent.putExtra(key, (String) obj);
                } else if ((obj instanceof Serializable)) {
                    intent.putExtra(key, (Serializable) obj);
                } else if ((obj instanceof Byte[])) {
                    intent.putExtra(key, (Byte[]) obj);
                } else if ((obj instanceof String[])) {
                    intent.putExtra(key, (String[]) obj);
                } else if ((obj instanceof Parcelable)) {
                    intent.putExtra(key, (Parcelable) obj);
                }
            }
            startActivity(intent);
        } else {
            getActivity().setResult(Activity.RESULT_OK, intent);
        }
        finish();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1) {
            String str = "";
            if (data == null || data.getData() == null) {
                str = filePath;
            } else {
                str = data.getData().toString();
                str = str.substring("file://".length());
            }
            Frame.IMAGECACHE.remove((new StringBuilder("file:")).append(str).toString());
            photosUtil.camImage.path = str;
            photosUtil.camImage.ischecked = true;
            if (!photosUtil.selecteds.contains(photosUtil.camImage)) {
                photosUtil.selecteds.add(photosUtil.camImage);
                onCheckedChanged(null, true);
            }
            recycleview.getAdapter().notifyDataSetChanged();
        }
    }
}
