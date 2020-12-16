/*
 * 文件名: ImageBase.java
 * 版    权：  Copyright XCDS Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: ryan
 * 创建时间:2013-12-3
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.mdx.framework.server.image.impl;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 *
 * @author ryan
 * @version [2013-12-3 下午3:38:11]
 */
public interface ImageBase {
    /**
     * 获取加载的内容的索引
     *
     * @return
     * @throws
     * @author ryan
     * @Title: getObj
     * @Description: TODO
     */
    public Object getObj();

    /**
     * 获取宽度
     *
     * @return
     * @throws
     * @author ryan
     * @Title: getWidth
     * @Description: TODO
     */
    public int getImageWidth();

    /**
     * 获取高度
     *
     * @return
     * @throws
     * @author ryan
     * @Title: getHeight
     * @Description: TODO
     */
    public int getImageHeight();

    /**
     * 是否使用文件缓存
     *
     * @return
     * @throws
     * @author ryan
     * @Title: isCache
     * @Description: TODO
     */
    public boolean isCache();

    public Object getLoadContext();

    /**
     * 是否重新加载
     *
     * @return
     * @throws
     * @author ryan
     * @Title: isCache
     * @Description: TODO
     */
    public boolean isReload();

//    public final static int URLIMAGELOAD_TYPE_POST=0;
//    public final static int URLIMAGELOAD_TYPE_GET=1;
//    public final static int URLIMAGELOAD_TYPE_NOPARAMS=2;

    /**
     * 获取图片加载方式
     *
     * @return
     * @throws
     * @author ryan
     * @Title: getLoadType URLIMAGELOAD_TYPE_POST 采用post URLIMAGELOAD_TYPE_GET 采用get URLIMAGELOAD_TYPE_NOPARAMS  不设置参数
     * @Description: TODO
     */
    public int getLoadType();

    /**
     * 是否已经改变
     *
     * @return
     * @throws
     * @author ryan
     * @Title: isChange
     * @Description: TODO
     */
    public boolean isChange();

    /**
     * 加载显示图片
     *
     * @param drawable
     * @param loadingid 加载的类id  在重复加载时分割加载线程和显示类,防止出现错位现象
     * @throws
     * @author ryan
     * @Title: loaded
     * @Description: TODO
     */
    public void loaded(MBitmap drawable, String loadingid, int type);

    /**
     * 设置加载的类id  在重复加载时分割加载线程和显示类,防止出现错位现象
     *
     * @param id
     * @throws
     * @author ryan
     * @Title: setLoadid
     * @Description: TODO
     */
    public void setLoadid(String id);

    public boolean isPalette();

    public int getBlur();

    public int getRound();

    public boolean isCircle();

    public boolean isAutosize();
}
