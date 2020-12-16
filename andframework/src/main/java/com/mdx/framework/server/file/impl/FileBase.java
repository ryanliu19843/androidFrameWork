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
package com.mdx.framework.server.file.impl;

import java.io.File;
/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 * @author ryan
 * @version [2013-12-3 下午3:38:11] 
 */
public interface FileBase {
    /**
     * 获取加载的内容的索引
     * @author ryan
     * @Title: getObj 
     * @Description: TODO
     * @return
     * @throws
     */
    public Object getObj();
    /**
     * 是否使用文件缓存
     * @author ryan
     * @Title: isCache 
     * @Description: TODO
     * @return
     * @throws
     */
    public boolean isCache();
    /**
     * 是否重新加载
     * @author ryan
     * @Title: isCache 
     * @Description: TODO
     * @return
     * @throws
     */
    public boolean isReload();

    /**
     * 获取图片加载方式
     * @author ryan
     * @Title: getLoadType URLIMAGELOAD_TYPE_POST 采用post URLIMAGELOAD_TYPE_GET 采用get URLIMAGELOAD_TYPE_NOPARAMS  不设置参数
     * @Description: TODO
     * @return
     * @throws
     */
    public int getLoadType();
    /**
     * 是否已经改变
     * @author ryan
     * @Title: isChange 
     * @Description: TODO
     * @return
     * @throws
     */
    public boolean isChange();
    /**
     * 加载显示图片
     * @author ryan
     * @Title: loaded 
     * @Description: TODO
     * @param drawable
     * @param loadingid 加载的类id  在重复加载时分割加载线程和显示类,防止出现错位现象
     * @throws
     */
    public void loaded(File file, String loadingid);
    
    /**
     * 设置加载的类id  在重复加载时分割加载线程和显示类,防止出现错位现象
     * @author ryan
     * @Title: setLoadid 
     * @Description: TODO
     * @param id
     * @throws
     */
    public void setLoadid(String id);
}
