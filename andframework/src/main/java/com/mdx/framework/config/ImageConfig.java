/*
 * 文件名: ImageInit.java 版 权： Copyright XCDS Tech. Co. Ltd. All Rights Reserved. 描
 * 述: [该类的简要描述] 创建人: ryan 创建时间:2013-12-24
 * 
 * 修改人： 修改时间: 修改内容：[修改内容]
 */
package com.mdx.framework.config;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 * 
 * @author ryan
 * @version [2013-12-24 上午11:46:23]
 */
public class ImageConfig extends BaseConfig{
    /**
     * 图片加载方式
     */
    private static int mImageLoadType = 0;
    
    /**
     * 图片参数
     */
    private static String mImageParam = "image=[view_width]x[view_height]";
    
    /**
     * 图片加载地址
     */
    private static String mImageUrl = "";
    
    private static HashMap<String, String> mImageUrls=new HashMap<String, String>();
    
    /**
     * 网络加载图片的最小网速
     */
    private static int mMinLoadImage = 10;
    
    /**
     * 获取图片读取地址
     * 
     * @return
     */
    public static String getImageUrl() {
        if (mImageUrl == null || mImageUrl.length() == 0) {
            return "";
        }
        String url = mImageUrl;
        if (url.startsWith("/")) {
            url = getUri() + url;
        }
        return url;
    }
    
    /**
     * 设置图片读取url
     * 
     * @param ImageUrl
     */
    public static void setImageUrl(String ImageUrl) {
        mImageUrl = ImageUrl;
    }
    
    /**
     * 获取图片读取方式
     * 
     * @return post 为post方法, get get方法
     */
    public static int getImageLoadType() {
        return mImageLoadType;
    }
    
    /**
     * 设置图片读取方式
     * 
     * @return post 为post方法, get get方法
     */
    public static void setImageLoadType(int ImageLoadType) {
        mImageLoadType = ImageLoadType;
    }
    
    /**
     * 设置image压缩的参数
     * 
     * @return
     */
    public static String getImageParam() {
        return mImageParam;
    }
    
    /**
     * 设置image压缩的参数
     * 
     * @return
     */
    public static void setImageParam(String ImageParam) {
        mImageParam = ImageParam;
    }
    
    /**
     * 获取图片加载的最小网络速率
     * 
     * @author ryan
     * @Title: getmMinLoadImage 0 全部加载 1 在低速时就加载 2. 在2g网络下加载 3 在3g网络下加载 4
     *         在4g网络下加载 10 在wifi下加载
     * @Description: TODO
     * @return
     * @throws
     */
    public static int getMinLoadImage() {
        return mMinLoadImage;
    }
    
    /**
     * 设置图片加载的最小网络速率
     * 
     * @author ryan
     * @Title: setmMinLoadImage
     * @Description: TODO
     * @param mMinLoadImage 0 全部加载 1 在低速时就加载 2. 在2g网络下加载 3 在3g网络下加载 4 在4g网络下加载
     *            10 在wifi下加载
     * @throws
     */
    public static void setMinLoadImage(int MinLoadImage) {
        mMinLoadImage = MinLoadImage;
    }

    public static HashMap<String, String> getImageUrls() {
        return mImageUrls;
    }

    public static void setImageUrls(HashMap<String, String> imageUrls) {
        ImageConfig.mImageUrls = imageUrls;
    }
    
    public static String getImageUrl(String name){
        String url=mImageUrl;
        if( ImageConfig.mImageUrls.containsKey(name)){
            url=ImageConfig.mImageUrls.get(name);
        }
        if (url == null || url.length() == 0) {
            return "";
        }
        if (url.startsWith("/")) {
            url = getUri(name) + url;
        }
        return url;
    }
    
    public static String readImageUrl(String str){
        if(str.trim().startsWith("[")){
            Pattern pattern = Pattern.compile("\\[([A-Za-z0-9=\\-_]*?)\\]");
            Matcher matcher = pattern.matcher(str);
            String name="";
            while (matcher.find()) {
                if (matcher.groupCount() > 0) {
                    name=matcher.group(1);
                    break;
                }
            }
            return getImageUrl(name)+str.replaceAll("\\[([A-Za-z0-9=\\-_]*?)\\]", "");
        }else{
            return getImageUrl()+str;
        }
    }
    
    public static void putUrl(String name,String value) {
        ImageConfig.mImageUrls.put(name, value);
    }
}
