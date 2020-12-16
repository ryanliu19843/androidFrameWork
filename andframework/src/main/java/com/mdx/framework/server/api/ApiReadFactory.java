/*
 * 文件名: ApiReadFormat.java 版 权： Copyright XCDS Tech. Co. Ltd. All Rights
 * Reserved. 描 述: [该类的简要描述] 创建人: ryan 创建时间:2014-4-3
 * 
 * 修改人： 修改时间: 修改内容：[修改内容]
 */
package com.mdx.framework.server.api;

import java.util.Locale;

import android.text.TextUtils;

import com.mdx.framework.commons.ParamsManager;
import com.mdx.framework.server.api.impl.ApiRead;
import com.mdx.framework.server.api.json.Updateone2json;
import com.mdx.framework.server.api.protobuf.UpdateOne2Protobuf;
import com.mdx.framework.server.api.webservice.Updateone2WebService;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 * 
 * @author ryan
 * @version [2014-4-3 下午1:26:25]
 */
public class ApiReadFactory {
    public static ApiRead getApiRead(String datatype) {
        if (!TextUtils.isEmpty(datatype)) {
            if ("JSON".equals(datatype.toUpperCase(Locale.ENGLISH))) {
                return new Updateone2json();
            }
            if ("WEBSERVICEJSON".equals(datatype.toUpperCase(Locale.ENGLISH))) {
                return new Updateone2WebService();
            }
            String pkg = ParamsManager.get("DATATYPE" + datatype.toUpperCase(Locale.ENGLISH));
            if (pkg != null) {
                Class<?> clas;
                try {
                    clas = Class.forName(pkg);
                    Object obj = clas.newInstance();
                    if (obj instanceof ApiRead) {
                        return (ApiRead) obj;
                    }
                }
                catch (Exception e) {}
            }
        }
        return new UpdateOne2Protobuf();
    }
}
