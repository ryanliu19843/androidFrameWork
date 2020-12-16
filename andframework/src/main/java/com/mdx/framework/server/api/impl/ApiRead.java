/*
 * 文件名: ApiRead.java
 * 版    权：  Copyright XCDS Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: ryan
 * 创建时间:2014-4-3
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.mdx.framework.server.api.impl;

import com.mdx.framework.server.api.Son;
import com.mdx.framework.server.api.UpdateOne;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 * @author ryan
 * @version [2014-4-3 下午1:21:49] 
 */
public interface ApiRead {
    /**
     * 初始化接口参数
     * @param params 参数1
     * @param data 参数2
     * @throws
     */
    public Object initObj(UpdateOne updateone);
    
    /**
     * 创建映射类
     * @param classname 映射类的名称
     * @throws
     */
    
    public void createRequest(UpdateOne updateone, String classname);
    
    
    /**
     * 获取接口返回值
     * @Description: TODO
     * @return
     * @throws
     */
    public Son readSon(UpdateOne updateone) ;
    
    /**
     * 保存模拟数据
     * @Description: TODO
     * @return
     * @throws
     */
    public void saveBuild(UpdateOne updateone, Object obj);
    
    
    public Son readBuild(UpdateOne updateone);
}
