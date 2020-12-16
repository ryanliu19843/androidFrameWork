/*
 * 文件名: ToolsInit.java 版 权： Copyright XCDS Tech. Co. Ltd. All Rights Reserved. 描
 * 述: [该类的简要描述] 创建人: ryan 创建时间:2013-12-24
 * 
 * 修改人： 修改时间: 修改内容：[修改内容]
 */
package com.mdx.framework.config;

/**
 * 工具参数<BR>
 * [功能详细描述]
 * 
 * @author ryan
 * @version [2013-12-24 下午12:19:52]
 */
public class ToolsConfig extends BaseConfig{
    public static final String TAG_IMAGE_MEMORY_CACHE = "ImageMemoryCache";
    
    public static final String TAG_IMAGE_STORE_CACHE = "ImageStoreCache";
    public static final String TAG_File_STORE_CACHE = "FileStoreCache";
    
    
    public static final String TAG_IMAGE_NET_DOWN = "ImageNetDown";
    
    /**
     * log打印筛选
     */
    private static String mStatisticsTags = ","+TAG_IMAGE_NET_DOWN+",";
    
    /**
     * 是否统计流量
     */
    private static boolean mStatistics = true;
    
    /**
     * 是否显示工具框
     */
    private static boolean mLogToolsShow = true;
    
    /**
     * 流量显示方式
     */
    private static int mStatisticsShowType = 1;
    
    /**
     * 是否开启流量统计
     * 
     * @return true 开启 false 关闭
     */
    public synchronized static boolean isStatistics() {
        return mStatistics;
    }
    
    /**
     * 是否开启流量统计
     * 
     * @param netStatistics
     */
    public synchronized static void setStatistics(boolean Statistics) {
        mStatistics = Statistics;
    }
    
    /**
     * 统计显示方式 0 累计显示 1 显示后重置
     * 
     * @return
     */
    public synchronized static int getStatisticsShowType() {
        return mStatisticsShowType;
    }
    
    /**
     * 统计显示方式 0 累计显示 1 显示后重置
     * 
     * @return
     */
    public synchronized static void setStatisticsShowType(int StatisticsShowType) {
        mStatisticsShowType = StatisticsShowType;
    }
    
    /**
     * 统计标签
     * 
     * @return
     */
    public synchronized static String getStatisticsTags() {
        return "," + mStatisticsTags + ",";
    }
    
    /**
     * 统计标签
     * 
     * @param mStatisticsTags
     */
    public synchronized static void setStatisticsTags(String StatisticsTags) {
        mStatisticsTags = StatisticsTags;
    }
    
    /**
     * 是否显示log工具
     * 
     * @return
     */
    public static boolean isLogToolsShow() {
        return mLogToolsShow;
    }
    
    /**
     * 设置是否显示log工具
     * 
     * @param mLogToolsShow
     */
    public static void setLogToolsShow(boolean LogToolsShow) {
        mLogToolsShow = LogToolsShow;
    }
    
    /**
     * 检查tag是否需要统计
     * 
     * @param tag
     * @return
     */
    public synchronized static boolean checkStatistics(String tag) {
        if (isStatistics()) {
            return getStatisticsTags().indexOf("," + tag + ",") >= 0 || getStatisticsTags().indexOf(",all,") >= 0;
        } else {
            return false;
        }
    }
}
