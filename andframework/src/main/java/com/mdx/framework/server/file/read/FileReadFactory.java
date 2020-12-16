/*
 * 文件名: ImageReadFactory.java
 * 版    权：  Copyright XCDS Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: ryan
 * 创建时间:2013-12-3
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.mdx.framework.server.file.read;

import com.mdx.framework.server.file.impl.FileRead;
import com.mdx.framework.utility.Util;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 * @author ryan
 * @version [2013-12-3 下午4:03:28] 
 */
public class FileReadFactory {
    public static FileRead createFileRead(String url,Object obj, int loadtype, boolean useCache){
        if(Util.isFullUrl(url)){
            FileRead ir= new UrlFileLoad();
            ir.createRead(url,obj, loadtype, useCache);
            return ir;
        }else if(Util.isFileUrl(url)){
            FileFileLoad ir= new FileFileLoad();
            ir.createRead(url,obj, loadtype, useCache);
            return ir;
        }
        return null;
    }
    
    
}
