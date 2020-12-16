/*
 * 文件名: Prompt.java 版 权： Copyright XCDS Tech. Co. Ltd. All Rights Reserved. 描 述:
 * [该类的简要描述] 创建人: ryan 创建时间:2014-1-2
 * 
 * 修改人： 修改时间: 修改内容：[修改内容]
 */
package com.mdx.framework.prompt;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 * 
 * @author ryan
 * @version [2014-1-2 上午10:41:21]
 */
public interface Prompt {
    /**
     * 显示信息对话框
     * @author ryan
     * @Title: show 
     * @Description: TODO
     * @throws
     */
    public void pshow(boolean bol);
    /**
     * 关闭信息对话框
     * @author ryan
     * @Title: dismiss 
     * @Description: TODO
     * @throws
     */
    public void pdismiss(boolean bol);
    /**
     * 是否在显示状态
     * @author ryan
     * @Title: isShowing 
     * @Description: TODO
     * @return
     * @throws
     */
    public boolean isShowing();
    /**
     * 是否允许显示
     * @author ryan
     * @Title: canShow 
     * @Description: TODO
     * @return
     * @throws
     */
    public boolean canShow();
}
