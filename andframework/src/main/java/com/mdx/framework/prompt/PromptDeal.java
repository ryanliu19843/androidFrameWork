/*
 * 文件名: PromptDeal.java 版 权： Copyright XCDS Tech. Co. Ltd. All Rights Reserved.
 * 描 述: [该类的简要描述] 创建人: ryan 创建时间:2014-1-2
 * 
 * 修改人： 修改时间: 修改内容：[修改内容]
 */
package com.mdx.framework.prompt;

import java.lang.reflect.Constructor;

import android.content.Context;
import android.text.TextUtils;

import com.mdx.framework.commons.ParamsManager;
import com.mdx.framework.log.MLog;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 * 
 * @author ryan
 * @version [2014-1-2 下午12:39:28]
 */
public class PromptDeal implements Prompt {
    public Prompt prompt;
    
    public int size;
    
    public PromptDeal(Prompt prompt) {
        this.prompt = prompt;
        this.size = 0;
    }
    
    @Override
    public void pshow(boolean isshow) {
        size++;
        if ((canShow() || isshow) && !isShowing()) {
            try {
                prompt.pshow(isshow);
            }
            catch (Exception e) {
                MLog.D(MLog.SYS_RUN, e);
            }
            
        }
    }
    
    @Override
    public void pdismiss(boolean isshow) {
        if (size > 0) {
            size--;
        }
        if (size <= 0) {
            try {
                prompt.pdismiss(isshow);
            }
            catch (Exception e) {
                MLog.D(MLog.SYS_RUN, e);
            }
        }
    }
    
    @Override
    public boolean isShowing() {
        return prompt.isShowing();
    }
    
    public static PromptDeal create(Context context) {
        String loadingclassname = ParamsManager.getString("loading");
        Prompt prompt = null;
        if (!TextUtils.isEmpty(loadingclassname)) {
            try {
                Class<?> clas = Class.forName(loadingclassname);
                Constructor<?> clst = clas.getConstructor(Context.class);
                Object obj = clst.newInstance(context);
                if (obj instanceof Prompt) {
                    prompt = (Prompt) obj;
                } else {
                    prompt = new LoadingDialog(context);
                }
            }
            catch (Exception e) {
                prompt = new LoadingDialog(context);
            }
        } else {
            prompt = new LoadingDialog(context);
        }
        return new PromptDeal(prompt);
    }
    
    @Override
    public boolean canShow() {
        return prompt.canShow();
    }
}
