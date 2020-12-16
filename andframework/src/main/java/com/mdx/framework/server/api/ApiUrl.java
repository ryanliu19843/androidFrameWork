/*
 * 文件名: ApiUrl.java 版 权： Copyright XCDS Tech. Co. Ltd. All Rights Reserved. 描 述:
 * [该类的简要描述] 创建人: ryan 创建时间:2013-12-24
 *
 * 修改人： 修改时间: 修改内容：[修改内容]
 */
package com.mdx.framework.server.api;

import com.mdx.framework.config.BaseConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * 接口调用地址的类<BR>
 *
 * @author ryan
 * @version [2013-12-24 上午11:32:51]
 */
public class ApiUrl {
    /**
     * 接口完整地址
     */
    public String configName;

    public String url;

    public String configururl="";

    public String methodUrl;

    /**
     * 接口类型
     */
    public int type;

    /**
     * 接口映射类
     */
    public String className;

    /**
     * 接口调用的错误类型
     */
    public int errorType;

    /**
     * 接口缓存的有效时间
     */
    public Long cacheTime;

    public List<Integer> saveError;

    /**
     * 接口返回格式类型
     */
    public String dataType;

    /**
     * 内容下载编码
     */
    public String encode;

    /**
     * 参数上传编码
     */
    public String upEncode;

    public void setSaveError(String str) {
        saveError = new ArrayList<Integer>();
        String[] sts = str.split(",");
        for (String s : sts) {
            if (s != null && s.length() > 0) {
                try {
                    saveError.add(Integer.valueOf(s));
                } catch (Exception e) {
                }
            }
        }
    }

    public String getUri() {
        return BaseConfig.getUri(configName);
    }

    public String toString() {
        return url;
    }
}
