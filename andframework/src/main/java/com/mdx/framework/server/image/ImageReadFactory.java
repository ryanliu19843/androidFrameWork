/*
 * 文件名: ImageReadFactory.java 版 权： Copyright XCDS Tech. Co. Ltd. All Rights
 * Reserved. 描 述: [该类的简要描述] 创建人: ryan 创建时间:2013-12-3
 *
 * 修改人： 修改时间: 修改内容：[修改内容]
 */
package com.mdx.framework.server.image;

import com.mdx.framework.log.MLog;
import com.mdx.framework.server.image.impl.ImageRead;
import com.mdx.framework.utility.Util;

import java.util.HashMap;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 *
 * @author ryan
 * @version [2013-12-3 下午4:03:28]
 */
public class ImageReadFactory {
    public static HashMap<String, Class<?>> IMAGEREADMAP = new HashMap<String, Class<?>>();

    public static ImageRead createImageRead(String url, Object obj, int imageType, boolean useCache) {
        if (Util.isFullUrl(url)) {
            ImageRead ir = new UrlImageLoad();
            ir.createRead(url, obj, imageType, useCache);
            return ir;
        } else if (Util.isFileUrl(url)) {
            ImageRead ir = new FileImageLoad();
            ir.createRead(url, obj, imageType, useCache);
            return ir;
        } else if (Util.isJarUrl(url)) {
            ImageRead ir = new JarImageLoad();
            ir.createRead(url, obj, imageType, useCache);
            return ir;
        } else if (Util.isAssets(url)) {
            ImageRead ir = new AssetsImageLoad();
            ir.createRead(url, obj, imageType, useCache);
            return ir;
        } else if (Util.isMedia(url) || Util.isMinMedia(url)) {
            ImageRead ir = new MediaImageLoad();
            ir.createRead(url, obj, imageType, useCache);
            return ir;
        } else if (Util.isContent(url)) {
            ImageRead ir = new ContentImageLoad();
            ir.createRead(url, obj, imageType, useCache);
            return ir;
        } else {
            for (String key : IMAGEREADMAP.keySet()) {
                if (url.startsWith(key)) {
                    try {
                        Object imageload = IMAGEREADMAP.get(key).newInstance();
                        if (imageload instanceof ImageRead) {
                            ((ImageRead) imageload).createRead(url, obj, imageType, useCache);
                            return (ImageRead) imageload;
                        }
                    } catch (Exception e) {
                        MLog.D(MLog.SYS_RUN, e);
                    }
                }
            }

        }
        return null;
    }
}
