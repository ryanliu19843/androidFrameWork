package com.mdx.framework.widget.selectphotos.item;

import com.mdx.framework.widget.selectphotos.utils.PhotosUtil;

import java.util.ArrayList;
import java.util.List;

public class Forld {
        public PhotosUtil photosUtil;

        public List<PhotosUtil.ImageItem> images = new ArrayList<PhotosUtil.ImageItem>();
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

        public Forld(PhotosUtil photosUtil){
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
