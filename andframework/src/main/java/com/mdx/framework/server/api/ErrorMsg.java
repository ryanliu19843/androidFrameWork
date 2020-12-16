/*
 * 文件名: ErrorMsg.java 版 权： Copyright XCDS Tech. Co. Ltd. All Rights Reserved. 描
 * 述: [该类的简要描述] 创建人: ryan 创建时间:2014-1-7
 *
 * 修改人： 修改时间: 修改内容：[修改内容]
 */
package com.mdx.framework.server.api;

import java.lang.reflect.Constructor;

import android.content.Context;

import com.mdx.framework.config.ErrorConfig;
import com.mdx.framework.prompt.ErrorPrompt;
import com.mdx.framework.utility.Verify;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 *
 * @author ryan
 * @version [2014-1-7 上午9:53:09]
 */
public class ErrorMsg {
    /**
     * 错误产生类
     */
    public Object object;

    /**
     * 错误id,即错误编号
     */
    public int id = 0;

    /**
     * 错误类型
     */
    public int type = 0;

    /**
     * 错误内容
     */
    public String name;

    /**
     * 错误提示类
     */
    public String className;

    /**
     * 错误翻译内容
     */
    public String value;

    /**
     * 错误翻译标题
     */
    public String title;

    /**
     * 接口id
     */
    public String method;

    public ErrorMsg() {

    }

    public ErrorMsg clone() {
        ErrorMsg retn = new ErrorMsg();
        retn.object = this.object;
        retn.id = id;
        retn.type = type;
        retn.name = name;
        retn.className = className;
        retn.value = value;
        retn.title = title;
        retn.method = method;
        return retn;
    }

    public ErrorMsg(Son son) {
        this.id = son.getError();
        this.name = son.getMsg();
        this.method = son.mErrMethod;
        this.object = son;
    }

    public String toString() {
        return name + value;
    }

    public Son getSon() {
        if (object == null) {
            return null;
        }
        if (object instanceof Son) {
            return (Son) object;
        }
        return null;
    }

    public ErrorPrompt getMsgPrompt(Context context) {
        if (!Verify.isNull(className)) {
            try {
                Class<?> clazz = Class.forName(className);
                Constructor<?> clst = clazz.getConstructor(Context.class);
                Object obj = clst.newInstance(context);
                if (obj instanceof ErrorPrompt) {
                    return (ErrorPrompt) obj;
                } else {
                    throw new Exception("Error Class type! not MsgDialog!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ErrorConfig.getMsgPrompt(context);
            }
        } else {
            return ErrorConfig.getMsgPrompt(context);
        }
    }
}
