/*
 * 文件名: DataChoose.java 版 权： Copyright Huawei Tech. Co. Ltd. All Rights
 * Reserved. 描 述: [该类的简要描述] 创建人: Administrator 创建时间:2014-12-22
 * 
 * 修改人： 修改时间: 修改内容：[修改内容]
 */
package com.mdx.framework.utility.commons;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 * 
 * @author Administrator
 * @version [2014-12-22 下午3:54:17]
 */
public interface DataChoose {
    public int getFirst();
    
    public int getsecond(int first, Object fitem);
    
    public int gettheed(int first, Object fitem, int second, Object sitem);
}
