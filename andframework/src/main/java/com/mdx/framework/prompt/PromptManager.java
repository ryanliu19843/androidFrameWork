/*
 * 文件名: DialogManager.java 版 权： Copyright XCDS Tech. Co. Ltd. All Rights
 * Reserved. 描 述: [该类的简要描述] 创建人: ryan 创建时间:2014-1-2
 * 
 * 修改人： 修改时间: 修改内容：[修改内容]
 */
package com.mdx.framework.prompt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 *
 * @author ryan
 * @version [2014-1-2 上午10:39:00]
 */
public class PromptManager {

    private static HashMap<Context, List<PromptDeal>> mPrompts = new HashMap<Context, List<PromptDeal>>();

    public synchronized static void show(Context context, Prompt prompt, boolean isshow) {
        synchronized (mPrompts) {
            PromptDeal pd = null;
            if (prompt == null && context instanceof Prompt) {
                prompt = (Prompt) context;
            }
            if (mPrompts.containsKey(context)) {
                List<PromptDeal> prots = mPrompts.get(context);
                pd = checkPrompt(prots, prompt);
                if (pd == null) {
                    if (prompt instanceof PromptDeal) {
                        pd = (PromptDeal) prompt;
                    } else {
                        pd = new PromptDeal(prompt);
                    }
                    prots.add(pd);
                }
            } else {
                List<PromptDeal> prots = new ArrayList<PromptDeal>();
                if (prompt instanceof PromptDeal) {
                    pd = (PromptDeal) prompt;
                } else {
                    pd = new PromptDeal(prompt);
                }
                prots.add(pd);
                mPrompts.put(context, prots);
            }
            pd.pshow(isshow);
        }
    }

    private static PromptDeal checkPrompt(List<PromptDeal> prots, Prompt prompt) {
        if (prompt instanceof PromptDeal) {
            PromptDeal pt = (PromptDeal) prompt;
            for (PromptDeal pot : prots) {
                if (pot == pt || pot.prompt == prompt) {
                    return pot;
                }
            }
        } else {
            for (PromptDeal pot : prots) {
                if (pot.prompt == prompt) {
                    return pot;
                }
            }
        }
        return null;
    }

    public synchronized static void clear(Context context) {
        synchronized (mPrompts) {
            if (mPrompts.containsKey(context)) {
                List<PromptDeal> pots = mPrompts.remove(context);
                for (PromptDeal pot : pots) {
                    if (pot.prompt != null && pot.prompt.isShowing()) {
                        pot.prompt.pdismiss(false);
                    }
                }
            }
        }
    }

    public synchronized static void dismiss(Context context, Prompt prompt, boolean isshow) {
        synchronized (mPrompts) {
            PromptDeal pd = null;
            if (prompt == null && context instanceof Prompt) {
                prompt = (Prompt) context;
            }
            List<PromptDeal> prots = null;
            if (mPrompts.containsKey(context)) {
                prots = mPrompts.get(context);
                pd = checkPrompt(prots, prompt);
            }
            if (pd == null) {
                return;
            }
            pd.pdismiss(isshow);
            if (pd.size <= 0) {
                prots.remove(pd);
            }
            if (prots.size() == 0) {
                mPrompts.remove(context);
            }
        }
    }
}
