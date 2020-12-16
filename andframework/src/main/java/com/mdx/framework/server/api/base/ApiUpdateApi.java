/*
 * 文件名: ApiUpdate.java 版 权： Copyright XCDS Tech. Co. Ltd. All Rights Reserved. 描
 * 述: [该类的简要描述] 创建人: ryan 创建时间:2014-1-21
 * 
 * 修改人： 修改时间: 修改内容：[修改内容]
 */
package com.mdx.framework.server.api.base;

import android.content.Context;

import com.mdx.framework.server.api.ApiManager;
import com.mdx.framework.server.api.ApiUpdate;
import com.mdx.framework.server.api.UpdateOne;
import com.mdx.framework.utility.Device;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 *
 * @author ryan
 * @version [2014-1-21 上午9:00:14]
 */
public class ApiUpdateApi extends ApiUpdate {
    private static final long serialVersionUID = 1L;

    public UpdateOne get(Context context, Object parent, String method, String pkgname, int version, String platform, String appid, String source, String error) {
        return  this.get("APP_UPDATE_SELF",context,parent,method,pkgname,version,platform,appid,source,error);
    }


    public UpdateOne get(String id,Context context, Object parent, String method, String pkgname, int version, String platform, String appid, String source, String error) {
        this.setMethod(method);
        this.setContext(context);
        this.setParent(parent);
        UpdateOne update = new UpdateOne(id, new String[][]{
                {"pkgname", pkgname},
                {"version", version + ""},
                {"appkey", appid + ""},
                {"source", source},
                {"deviceid", Device.getId()},
                {"platform", platform},
                {"errorMsg", error}});
        return initUpdateOne(update);
    }

    public void load(String id, Context context, Object parent, String method, String pkgname, int version, String platform, String appid, String source, String error) {
        UpdateOne update = get(id,context, parent, method, pkgname, version, platform, appid, source, error);
        ApiManager.Load(getContext(), getParent(), new UpdateOne[]{update},this.getPostdelay());
    }

    public void load( Context context, Object parent, String method, String pkgname, int version, String platform, String appid, String source, String error) {
        UpdateOne update = get(context, parent, method, pkgname, version, platform, appid, source, error);
        ApiManager.Load(getContext(), getParent(), new UpdateOne[]{update},this.getPostdelay());
    }

    public ApiUpdateApi set(String pkgname, int version, String platform, String appid, String source, String error) {
        get( null, null, null, pkgname, version, platform, appid, source, error);
        return this;
    }
}