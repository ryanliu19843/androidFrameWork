package com.mdx.framework.frg.multiplephoto;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore.Images.Media;
import androidx.loader.app.LoaderManager.LoaderCallbacks;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;

import com.mdx.framework.Frame;
import com.mdx.framework.R;
import com.mdx.framework.activity.CameraActivity;
import com.mdx.framework.activity.MFragment;
import com.mdx.framework.dialog.PhotoShow;
import com.mdx.framework.utility.Device;
import com.mdx.framework.utility.Util;
import com.mdx.framework.widget.ActionBar;
import com.mdx.framework.widget.getphoto.ActCameraStream;

public class MultiplePhotoSelect extends MFragment implements LoaderCallbacks<Cursor>, OnClickListener {
    
    private ImageCursorAdapter ica;
    
    private String filePath;
    
    private static final String STORE_IMAGES[] = { Media.DISPLAY_NAME, Media.DATE_ADDED, Media.DATA,
            Media.BUCKET_DISPLAY_NAME, Media.LATITUDE, Media.LONGITUDE, Media._ID };
    
    public GridView grid_view;
    
    public Button button;
    
    public int maxSelectPhoto, mfinishtype = 0;
    
    public OnClickListener camSel;
    private boolean isCallBack = false;
    
    
    public MultiplePhotoSelect() {
        ica = null;
        maxSelectPhoto = 1;
        camSel = new OnClickListener() {
            
            public void onClick(View v) {
                int cam = 0;
                if (maxSelectPhoto == 1 && ica.checkedList.size() == 1) {
                    result();
                }
                if (ica.img.data != null)
                    cam = 1;
                if (maxSelectPhoto <= 0)
                    button.setText((new StringBuilder(getActivity().getString(R.string.complete)+"(")).append(ica.checkedList.size() + cam)
                            .append(")")
                            .toString());
                else
                    button.setText((new StringBuilder(getActivity().getString(R.string.complete)+"(")).append(ica.checkedList.size() + cam)
                            .append("/")
                            .append(maxSelectPhoto)
                            .append(")")
                            .toString());
            }
        };
    }
    
    @Override
    public void setActionBar(ActionBar actionBar, Context context) {
        actionBar.setTitle(context.getString(R.string.choose_photo));
    }
    
    protected void create(Bundle savedInstanceState) {
        setContentView(R.layout.default_mutiple_photo_select);
        mfinishtype = getActivity().getIntent().getIntExtra("finishtype", 0);
        maxSelectPhoto = getActivity().getIntent().getIntExtra("Max", maxSelectPhoto);
        String temptime = (new StringBuilder()).append(UUID.randomUUID().toString().replace("-", "")).toString();
        filePath = (new StringBuilder(String.valueOf(Util.getDPath(getContext(), "/temp/csimages/").getPath()))).append(temptime)
                .append("_cstempsave.temp")
                .toString();
        initView();
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1) {
            String str = "";
            if (data == null || data.getData()==null) {
                str = filePath;
            } else {
                str = data.getData().toString();
                str = str.substring("file://".length());
            }
            ica.img.data = str;
            ica.img.id = str;
            Frame.IMAGECACHE.remove((new StringBuilder("file:")).append(str).toString());
            ica.notifyDataSetChanged();
            camSel.onClick(null);
            if (maxSelectPhoto == 1) {
                result();
            }
        }
    }
    
    private void initView() {
        grid_view = (GridView) findViewById(R.id.grid_view);
        button = (Button) findViewById(R.id.submit);
        ica = new ImageCursorAdapter(getContext(), null, this, new OnClickListener() {
            
            public void onClick(View v) {
                ImageCursorAdapter.Image image = (ImageCursorAdapter.Image) v.getTag();
                List<String> list = new ArrayList<String>();
                boolean bol = false;
                if (ica.img.data != null)
                    list.add((new StringBuilder("file:")).append(ica.img.data).toString());
                for (ImageCursorAdapter.Image img : ica.checkedList) {
                    list.add((new StringBuilder("file:")).append(img.data).toString());
                    if (img.id.equals(image.id))
                        bol = true;
                }
                
                if (!bol)
                    list.add((new StringBuilder("file:")).append(image.data).toString());
                PhotoShow ps = new PhotoShow(getContext(), list, (new StringBuilder("file:")).append(image.data)
                        .toString());
                ps.show();
            }
        }, camSel);
        if (maxSelectPhoto == 0)
            ica.maxSize = 0x7fffffff;
        else
            ica.maxSize = maxSelectPhoto;
        camSel.onClick(null);
        grid_view.setAdapter(ica);
        getLoaderManager().initLoader(0, null, this);
        button.setOnClickListener(new OnClickListener() {
            
            public void onClick(View v) {
                result();
            }
        });
        if (maxSelectPhoto == 1) {
            findViewById(R.id.control).setVisibility(View.GONE);
        }
    }
    
    private void result() {
        Intent intent = new Intent();
        ArrayList<String> list = new ArrayList<String>();
        if (ica.img.data != null)
            list.add(ica.img.data);
        for (ImageCursorAdapter.Image img : ica.checkedList) {
            list.add(img.data);
        }
        intent.putExtra("data", list);
        intent.putExtra("type", "ms");
        
        if (mfinishtype == 3) {
            Intent sendi = new Intent();
            sendi.setAction(ActCameraStream.RECEIVER_PHOTO);
            sendi.putExtra("list", list);
            sendi.putExtra("type", 2);
            getActivity().sendBroadcast(sendi);
            isCallBack = true;
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
            isCallBack = true;
        } else {
            getActivity().setResult(Activity.RESULT_OK, intent);
        }
        finish();
    }
    
    
    
    /**
     * [一句话功能简述]<BR>
     * [功能详细描述]
     * @see com.mdx.framework.activity.MFragment#destroy()
     */
    
    @Override
    protected void destroy() {
        if (!isCallBack) {
            Intent intent = new Intent();
            
            intent.setAction(ActCameraStream.RECEIVER_PHOTO);
            intent.putExtra("type", 0);
            getActivity().sendBroadcast(intent);
        }
    }

    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        CursorLoader cursorLoader = new CursorLoader(getContext(),
                Media.EXTERNAL_CONTENT_URI, STORE_IMAGES, null, null,
                Media.DATE_MODIFIED + " desc");
        return cursorLoader;
    }
    
    public void onLoaderReset(Loader<Cursor> arg0) {
        ica.swapCursor(null);
    }
    
    public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
        ica.swapCursor(cursor);
    }
    
    public void onClick(View v) {
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
    
}
