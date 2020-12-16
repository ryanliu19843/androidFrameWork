package com.mdx.framework.widget.selectphotos.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import com.mdx.framework.R;
import com.mdx.framework.autofit.AutoFit;
import com.mdx.framework.widget.selectphotos.PhotoShow;
import com.mdx.framework.widget.selectphotos.SelectPhoto;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ryan on 2017/12/27.
 */

public class PhotosUtil {
    private ArrayList<Folder> mDirPaths = new ArrayList<>();
    private Folder imageAll=new Folder(this);
    public SelectPhoto selectPhoto;
    public ArrayList<ImageItem> selecteds=new ArrayList<>();
    public int maxselected=1;
    public ImageItem checkedImage;
    public ImageItem camImage;
    public CompoundButton.OnCheckedChangeListener onCheckedChangeListener;


    public static void read(final SelectPhoto selectPhoto,final OnPhotosGet onPhotosGet){
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                PhotosUtil pu=new PhotosUtil();
                pu.selectPhoto=selectPhoto;
                pu.getThumbnail(selectPhoto.getContext());
                onPhotosGet.onPhotosGet(pu);
            }
        });
        thread.start();
    }

    public interface OnPhotosGet{
        void onPhotosGet(PhotosUtil photosUtil);
    }

    private void getThumbnail(Context context) {
        /**
         * 临时的辅助类，用于防止同一个文件夹的多次扫描
         */
        HashMap<String, Integer> tmpDir = new HashMap<String, Integer>();

        Cursor mCursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.ImageColumns.DATA, MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME}, "", null,
                MediaStore.MediaColumns.DATE_ADDED + " DESC");
        ImageItem camitem=new ImageItem(this,true);
        camImage=camitem;
        imageAll.images.add(camitem);
        if (mCursor.moveToFirst()) {
            do {
                // 获取图片的路径
                String path = mCursor.getString(0);
                ImageItem item=new ImageItem(this,path);

                imageAll.images.add(item);
                // 获取该图片的父路径名
                File parentFile = new File(path).getParentFile();
                if (parentFile == null) {
                    continue;
                }
                Folder imageFloder = null;
                String dirPath = parentFile.getAbsolutePath();
                if (!tmpDir.containsKey(dirPath)) {
                    // 初始化imageFloder
                    imageFloder = new Folder(this);
                    imageFloder.setDir(dirPath);
                    imageFloder.setFirstImagePath(path);
                    imageFloder.images.add(camitem);
                    mDirPaths.add(imageFloder);
                    Log.d("zyh", dirPath + "," + path);
                    tmpDir.put(dirPath, mDirPaths.indexOf(imageFloder));
                } else {
                    imageFloder = mDirPaths.get(tmpDir.get(dirPath));
                }
                imageFloder.images.add(item);
            } while (mCursor.moveToNext());
        }
        mCursor.close();
        imageAll.name=context.getString(R.string.photoall);
        imageAll.dir=null;
        if(imageAll.images.size()>1) {
            imageAll.firstImagePath = imageAll.images.get(1).path;
        }
        mDirPaths.add(0,imageAll);
        for (int i = 0; i < mDirPaths.size(); i++) {
            Folder f = mDirPaths.get(i);
            Log.d("zyh", i + "-----" + f.getName() + "---" + f.images.size());
        }
        tmpDir = null;
    }


    public static class Folder {
        public PhotosUtil photosUtil;

        public List<ImageItem> images = new ArrayList<ImageItem>();
        /**
         * 图片的文件夹路径
         */
        private String dir;
        /**
         * 第一张图片的路径
         */
        private String firstImagePath;
        /**
         * 文件夹的名称
         */
        private String name;

        public Folder(PhotosUtil photosUtil){
            this.photosUtil=photosUtil;
        }

        public String getDir() {
            return dir;
        }

        public void setDir(String dir) {
            this.dir = dir;
            int lastIndexOf = this.dir.lastIndexOf("/");
            this.name = this.dir.substring(lastIndexOf);
        }

        public String getFirstImagePath() {
            return firstImagePath;
        }

        public void setFirstImagePath(String firstImagePath) {
            this.firstImagePath = firstImagePath;
        }

        public String getName() {
            return name;
        }

    }

    public static class ImageItem {
        public PhotosUtil photosUtil;
        public String path;
        public boolean ischecked=false;
        public boolean iscam=false;

        public ImageItem(PhotosUtil photosUtil,boolean iscam) {
            this.photosUtil=photosUtil;
            this.iscam=iscam;
        }

        public void setCheck(View v, boolean bol){
            photosUtil.selectAdd(v,bol,this);
        }

        public ImageItem(PhotosUtil photosUtil,String p) {
            this.photosUtil=photosUtil;
            this.path = p;
        }

        public AutoFit showPhoto(AutoFit fit){
            photosUtil.showPhoto(this);
            return fit;
        }

        public void showPhoto(){
            photosUtil.showPhoto(this);
        }
    }

    public void selectAdd(View v,boolean bol,ImageItem item){
        boolean isnchange=false;
        if(bol){
            if(!selecteds.contains(item)) {
                if(selecteds.size()>=maxselected){
                    if(v instanceof CompoundButton){
                        ((CompoundButton) v).setChecked(false);
                    }
                }else {
                    item.ischecked=true;
                    selecteds.add(item);
                    isnchange=true;
                }
            }
        }else{
            item.ischecked=false;
            selecteds.remove(item);
            isnchange=true;
        }
        if(isnchange && onCheckedChangeListener!=null){
            onCheckedChangeListener.onCheckedChanged(null,bol);
        }
    }

    public void showPhoto(ImageItem imageItem){
        PhotoShow photoShow=new PhotoShow(selectPhoto.getContext(),R.layout.default_selphoto_dia_show_photo,selectPhoto.autoFit.params,selectPhoto.autoFit,imageItem);
        photoShow.selectPhoto=selectPhoto;
        photoShow.show();
    }


    public ArrayList<Folder> getmDirPaths() {
        return mDirPaths;
    }

    public Folder getImageAll() {
        return imageAll;
    }
}
