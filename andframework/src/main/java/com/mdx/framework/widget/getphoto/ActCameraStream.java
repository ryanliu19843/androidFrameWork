package com.mdx.framework.widget.getphoto;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.mdx.framework.Frame;
import com.mdx.framework.R;
import com.mdx.framework.activity.NoTitleAct;
import com.mdx.framework.activity.PTitleAct;
import com.mdx.framework.frg.multiplephoto.MultiplePhotoSelect;
import com.mdx.framework.log.MLog;
import com.mdx.framework.utility.BitmapRead;
import com.mdx.framework.utility.Device;
import com.mdx.framework.utility.Helper;
import com.mdx.framework.utility.Util;
import com.mdx.framework.widget.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class ActCameraStream extends NoTitleAct {
    public final static String RECEIVER_PHOTO = "com.mdx.receivePhoto";
    
    public final static int DISPOSE_GET_PIC_PASH = 0x30;
    
    public final static int REQUEST_CAMERA = 0x10;
    
    public final static int RESULT_CAMERA = 0x20;
    
    public final static String RESULT_PHONE = "result_phone";
    
    // Static final constants
    private static final int DEFAULT_ASPECT_RATIO_VALUES = 10;
    
    private static final String ASPECT_RATIO_X = "ASPECT_RATIO_X";
    
    private static final int ROTATE_NINETY_DEGREES = 90;
    
    private static final String ASPECT_RATIO_Y = "ASPECT_RATIO_Y";
    
    // Instance variables
    private int mAspectRatioX = DEFAULT_ASPECT_RATIO_VALUES;
    
    private int mAspectRatioY = DEFAULT_ASPECT_RATIO_VALUES;
    
    private int aspectX = 1, aspectY = 1, outputX = 350, outputY = 350;
    
    private Button submit, cancle, rotate;
    
    private String picpathsave;
    
    private String picpathcrop;
    
    private CropImageView cropImageView;

    private View loadingview;
    
    private Bitmap mBitmaprust;
    
    private boolean isCallBack = false;
    
    // Saves the state upon rotating the screen/restarting the activity
    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(ASPECT_RATIO_X, mAspectRatioX);
        bundle.putInt(ASPECT_RATIO_Y, mAspectRatioY);
    }
    
    // Restores the state upon rotating the screen/restarting the activity
    @Override
    protected void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        mAspectRatioX = bundle.getInt(ASPECT_RATIO_X);
        mAspectRatioY = bundle.getInt(ASPECT_RATIO_Y);
    }
    
    // private void ShowPick(String type) {
    // if (type.equals("1")) {
    // Intent pick = new Intent(Intent.ACTION_GET_CONTENT);
    // pick.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
    // "image/*");
    // startActivityForResult(pick, 1);
    // }
    // if (type.equals("2")) {
    // if ("google,".indexOf(Device.getBrand() + ",") >= 0) {
    // Intent capture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    // capture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new
    // File(picpathsave)));
    // capture.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, true);
    // startActivityForResult(capture, 2);
    // } else {
    // Helper.startActivity(getActivity(), CameraActivity.class,
    // NoTitleAct.class,"output",Uri.fromFile(new
    // File(picpathsave)),"android.intent.extra.screenOrientation",true);

    // Intent capture = new Intent(this, CameraActivity.class);
    // capture.putExtra("output", Uri.fromFile(new File(picpathsave)));
    // capture.putExtra("android.intent.extra.screenOrientation", true);
    // startActivityForResult(capture, 2);
    // }
    // }
    // }

    public void create(Bundle savedInstanceState) {
        
        setContentView(R.layout.getphoto_activity_photo);
        submit = (Button) findViewById(R.id.submit);
        cancle = (Button) findViewById(R.id.cancle);
        rotate = (Button) findViewById(R.id.rotate);
        loadingview = findViewById(R.id.process);
        cropImageView = (CropImageView) findViewById(R.id.CropImageView);
        aspectX = getIntent().getIntExtra("aspectX", -1);
        aspectY = getIntent().getIntExtra("aspectY", -1);
        outputX = getIntent().getIntExtra("outputX", 0);
        outputY = getIntent().getIntExtra("outputY", 0);
        
        if (getIntent() != null && "ms".equals(getIntent().getStringExtra("type"))) {
            ArrayList<String> list = getIntent().getStringArrayListExtra("data");
            if (list.size() > 0) {
                String str = list.get(0);
                new mAsyncTask().execute(new String[] { str });
            } else {
                finish();
            }
        } else {
            Helper.startActivityForResult(getActivity(), 2, MultiplePhotoSelect.class, PTitleAct.class);
        }

        String temptime = "" + UUID.randomUUID().toString().replace("-", "");
        
        picpathsave = Util.getDPath(this, "/temp/csimages/").getPath() + temptime + "_cstempsave.jpgtemp";
        picpathcrop = Util.getDPath(this, "/temp/csimages/").getPath() + temptime + "_cstempcrop.jpgtemp";
        
        cropImageView.setFixedAspectRatio(false);
        
        if (Frame.CONTEXT == null) {
            Frame.CONTEXT = this;
        }
        
        rotate.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                cropImageView.rotateImage(ROTATE_NINETY_DEGREES);
            }
        });
        
        submit.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                new SaveAsyncTask().execute("");
            }
        });
        
        cancle.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        
        // if (userType == 1) {
        // ShowPick("1");
        // } else if (userType == 2) {
        // ShowPick("2");
        // }
    }
    
    public void init() {
        
    }
    
    public void savePhoto(Bitmap bitmap, String path) {
        FileOutputStream out = null;
        try {
            float bw = bitmap.getWidth(), bh = bitmap.getHeight();
            float w = outputX, h = outputY;
            if (w != 0 || h != 0) {
                if (w / bw < h / bh) {
                    bitmap = big(bitmap, w / bw);
                } else {
                    bitmap = big(bitmap, h / bh);
                }
            }
            out = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        }
        catch (Exception e) {
            MLog.D(MLog.SYS_RUN, e);
        }
        try {
            out.flush();
            out.close();
        }
        catch (Exception e) {
            MLog.D(MLog.SYS_RUN, e);
        }
    }
    
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            
            Intent intent = new Intent();
            intent.setAction(RECEIVER_PHOTO);
            intent.putExtra("type", 0);
            sendBroadcast(intent);
            
            finish();
            return;
        }
        if (data != null && data.getIntExtra("selfcam", 0) == 1) {
            String str = data.getData().toString();
            str = str.substring("file://".length());
            new mAsyncTask().execute(new String[] { str });
            return;
        }
        if (data != null && data.getStringExtra("type").equals("ms")) {
            ArrayList<String> list = data.getStringArrayListExtra("data");
            if (list.size() > 0) {
                String str = list.get(0);
                new mAsyncTask().execute(new String[] { str });
            } else {
                finish();
            }
            return;
        }
        switch (requestCode) {
            case 1:
                new mAsyncTask().execute(getFilePath(this, data.getData()));
                break;
            case 2:
                if (data != null) {
                    Bitmap bmap = data.getParcelableExtra("data");
                    if (bmap != null) {
                        setBitmap(bmap);
                    }
                } else {
                    new mAsyncTask().execute(picpathsave);
                }
                break;
        }
    }
    
    private class mAsyncTask extends AsyncTask<String/* 参数1 */, String/* 参数2 */, Bitmap/* 参数3 */> {
        @Override
        protected Bitmap doInBackground(String... params/* 参数1 */) {
            try {
                Thread.sleep(100);
            }
            catch (InterruptedException e) {
            }
            int w = Device.getMeticsW(), h = Device.getMeticsH();
            if (Device.getMeticsW() >= 1080) {
                w = (int) (Device.getMeticsW() * 1.5);
                h = (int) (Device.getMeticsH() * 1.5);
            } else if (Device.getMeticsW() >= 640) {
                w = (int) (Device.getMeticsW() * 2);
                h = (int) (Device.getMeticsH() * 2);
            } else {
                w = (int) (Device.getMeticsW() * 3);
                h = (int) (Device.getMeticsH() * 3);
            }
            return BitmapRead.decodeSampledBitmapFromFile(params[0], w, h);
        }
        
        @Override
        protected void onPostExecute(Bitmap result/* 参数3 */) {
            super.onPostExecute(result);
            setBitmap(result);
            loadingview.setVisibility(View.GONE);
        }
        
        protected void onPreExecute() {
            MLog.D(MLog.SYS_RUN, "start");
        }
        
        //
        protected void onProgressUpdate(String... values/* 参数2 */) {
            // textView.append(values[0]);
            MLog.D(MLog.SYS_RUN, "onProgressUpdate");
        }
        
        // onCancelled方法用于在取消执行中的任务时更改UI
        @Override
        protected void onCancelled() {
            MLog.D(MLog.SYS_RUN, "onCancelled");
        }
    }
    
    public void setBitmap(Bitmap result) {
        if (result == null) {
            finish();
        }
        float bw = result.getWidth();
        float bh = result.getHeight();
        float w = Device.getMeticsW();
        float h = Device.getMeticsH();
        if (w / bw < h / bh) {
            result = big(result, w / bw);
        } else {
            result = big(result, h / bh);
        }
        mBitmaprust = result;
        cropImageView.setImageBitmap(result);
        if (aspectX != -1 && aspectY != -1)
            cropImageView.postDelayed(new Runnable() {
                
                public void run() {
                    cropImageView.setFixedAspectRatio(true);
                    cropImageView.setAspectRatio(aspectX, aspectY);
                }
            }, 20L);
    }
    
    @SuppressWarnings("WrongThread")
    private class SaveAsyncTask extends AsyncTask<String/* 参数1 */, String/* 参数2 */, String/* 参数3 */> {
        @Override
        protected String doInBackground(String... params/* 参数1 */) {
            try {
                Bitmap bitmap = cropImageView.getCroppedImage();
                savePhoto(bitmap, picpathcrop); // save the croped image
                delphoto(picpathsave); // delete the source image
                Intent intent = new Intent();
                intent.setAction(RECEIVER_PHOTO);
                intent.putExtra("what", picpathcrop);
                intent.putExtra("width", bitmap.getWidth());
                intent.putExtra("height", bitmap.getHeight());
                intent.putExtra("type", 1);
                sendBroadcast(intent);
                isCallBack = true;
                
                Intent data = new Intent();
                ArrayList<String> list = new ArrayList<String>();
                list.add(picpathcrop);
                data.putExtra(RESULT_PHONE, picpathcrop);
                setResult(RESULT_CAMERA, data);
                intent.putExtra("data", list);
                getActivity().setResult(Activity.RESULT_OK, intent);
                bitmap.recycle();
                return picpathsave;
            }
            catch (Exception e) {}
            return null;
        }
        
        @Override
        protected void onPostExecute(String result/* 参数3 */) {
            super.onPostExecute(result);
            finish();
        }
        
        protected void onPreExecute() {
        }
        
        //
        protected void onProgressUpdate(String... values/* 参数2 */) {
        }
        
        // onCancelled方法用于在取消执行中的任务时更改UI
        @Override
        protected void onCancelled() {
        }
    }
    
    @SuppressLint(value = { "NewApi" })
    private String getFilePath(Context context, Uri contentUri) {
        String filePath = contentUri.toString();
        if (android.os.Build.VERSION.SDK_INT < 19)
            return selectImage(context, contentUri);
        if (DocumentsContract.isDocumentUri(context, contentUri)) {
            String wholeID = DocumentsContract.getDocumentId(contentUri);
            String id = wholeID.split(":")[1];
            String[] column = { MediaStore.Images.Media.DATA };
            String sel = MediaStore.Images.Media._ID + "=?";
            Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    column,
                    sel,
                    new String[] { id },
                    null);
            int columnIndex = cursor.getColumnIndex(column[0]);
            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
                return filePath;
            }
            cursor.close();
        } else {
            return selectImage(context, contentUri);
        }
        return null;
    }
    
    public static String selectImage(Context context, Uri selectedImage) {
        if (selectedImage != null) {
            String uriStr = selectedImage.toString();
            String path = uriStr.substring(10, uriStr.length());
            if (path.startsWith("com.sec.android.gallery3d")) {
                return null;
            }
        }
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return picturePath;
    }
    
    protected void onDestroy() {
        if (!isCallBack) {
            Intent intent = new Intent();
            intent.setAction(RECEIVER_PHOTO);
            intent.putExtra("type", 0);
            sendBroadcast(intent);
        }
        if (mBitmaprust != null && !mBitmaprust.isRecycled())
            mBitmaprust.recycle();
        super.onDestroy();
        if (Frame.HANDLES.size() == 0) {
            Frame.IMAGECACHE.clean();
            System.exit(1);
        }
    }
    
    public void delphoto(String path) {
        File f = new File(path);
        f.delete();
    }
    
    private static Bitmap big(Bitmap bitmap, float scale) {
        Matrix matrix = new Matrix();
        matrix.postScale(scale * 1.5f, scale * 1.5f);
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (resizeBmp != bitmap) {
            bitmap.recycle();
        }
        return resizeBmp;
    }
}
