/*
 * 文件名: LogConfig.java
 * 版    权：  Copyright XCDS Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: ryan
 * 创建时间:2013-12-24
 *
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.mdx.framework.config;


/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 *
 * @author ryan
 * @version [2013-12-24 下午12:30:42]
 */
public class LogConfig extends BaseConfig {
    private static boolean mShowLog = true;
    private static int loglev = 5;   //0 不打印调试log   1    5 打印文件调试log

    public synchronized static boolean isShowLog() {
        return mShowLog;
    }

    public synchronized static void setShowLog(boolean mShowLog) {
        LogConfig.mShowLog = mShowLog;
    }

    public static int getLoglev() {
        return loglev;
    }

    public static void setLoglev(int loglev) {
        LogConfig.loglev = loglev;
    }

}
