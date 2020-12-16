/*
 * 文件名: PTitleAct.java 版 权： Copyright XCDS Tech. Co. Ltd. All Rights Reserved. 描
 * 述: [该类的简要描述] 创建人: Administrator 创建时间:2015-6-4
 * 
 * 修改人： 修改时间: 修改内容：[修改内容]
 */
package com.mdx.framework.activity;

import com.mdx.framework.Frame;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 * 
 * @author Administrator
 * @version [2015-6-4 下午2:13:22]
 */
public class PTitleAct extends TitleAct {
    protected void destroy() {
        if (Frame.HANDLES.size() == 0) {
            System.exit(1);
            Frame.IMAGECACHE.clean();
        }
        
    }
    
    
}
