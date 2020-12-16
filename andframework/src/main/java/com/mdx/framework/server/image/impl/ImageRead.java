/*
 * 文件名: ImageRead.java 版 权： Copyright XCDS Tech. Co. Ltd. All Rights Reserved. 描
 * 述: [该类的简要描述] 创建人: ryan 创建时间:2013-12-3
 * 
 * 修改人： 修改时间: 修改内容：[修改内容]
 */
package com.mdx.framework.server.image.impl;

import com.mdx.framework.commons.CanIntermit;
import com.mdx.framework.commons.MException;

/**
 * 图片读取接口<BR>
 * 
 * @author ryan
 * @version [2013-12-3 下午2:53:09]
 */
public interface ImageRead extends CanIntermit {
    public Object loadImageFromUrl(int width, int height) throws MException;
    
    public void createRead(String url, Object object, int imageType, boolean reload);
    
    public abstract boolean needCache();
}
