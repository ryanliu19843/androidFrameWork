package com.mdx.framework.widget.pagerecycleview.autofit;

import android.content.Context;
import android.text.TextUtils;

import com.mdx.framework.autofit.AutoFit;
import com.mdx.framework.autofit.AutoFitBase;
import com.mdx.framework.autofit.commons.AutoFitItem;
import com.mdx.framework.commons.ParamsManager;
import com.mdx.framework.server.api.Son;
import com.mdx.framework.widget.pagerecycleview.ada.Card;
import com.mdx.framework.widget.pagerecycleview.ada.CardAdapter;
import com.mdx.framework.widget.util.DataFormat;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ryan on 2017/10/17.
 */

public class AutoDataFormat extends DataFormat {
    public int resid;
    private int size = 1;
    public AutoFitBase autoFit;
    public HashMap<Class, CardParam> resids = new HashMap<>();

    public AutoDataFormat(int resid, AutoFitBase autoFit) {
        this.resid = resid;
        this.autoFit = autoFit;
    }

    public AutoDataFormat(int resid, AutoFitBase autoFit, HashMap<Class, CardParam> resids) {
        this.resid = resid;
        this.autoFit = autoFit;
        if (resids != null) {
            this.resids = resids;
        }
    }

    @Override
    public boolean hasNext() {
        String pagesizenum = ParamsManager.get("pagesizenum");
        int mPageSize = 10;
        if (!TextUtils.isEmpty(pagesizenum)) {
            mPageSize = ParamsManager.getIntValue("pagesizenum");
        }
        return size >= mPageSize;
    }

    public CardAdapter getCardAdapter(Context context, Son son, int page) {
        Object sonBuild = son.getBuild();
        return getAda(context, sonBuild);
    }

    public CardAdapter getAda(Context context, Object sonBuild) {
        List listvalue = new ArrayList();
        if (sonBuild instanceof List) {
            listvalue = (List) sonBuild;
        } else if (sonBuild instanceof Object[]) {
            Object[] objs = (Object[]) sonBuild;
            for (Object f : objs) {
                listvalue.add(f);
            }
        } else {
            Field[] fields = sonBuild.getClass().getDeclaredFields();
            for (Field f : fields) {
                if (!java.lang.reflect.Modifier.isFinal(f.getModifiers()) && f.getType().isAssignableFrom(List.class)) {
                    try {
                        Object obj = f.get(sonBuild);
                        if (obj instanceof List) {
                            listvalue = (List) obj;
                        }
                        break;
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        size = listvalue.size();
        List<Card> list = new ArrayList<>();
        for (Object o : listvalue) {
            list.add(getCard(o, resids, resid, autoFit));
        }
        return new AutoCardAdapter(context, list, resid, autoFit, resids);
    }


    public static AutoCard getCard(Object o, HashMap<Class, CardParam> resids, int resid, AutoFitBase autoFit) {
        CardParam cardparam = null;
        if (resids != null && resids.size() > 0) {
            cardparam = resids.get(o.getClass());
        }
        if (cardparam == null) {
            cardparam = new CardParam(resid, 0, true);
        }

        try {
            cardparam.resid = getValueByinvoke(o, "getResId", cardparam.resid);
            cardparam.useanimation = getValueByinvoke(o, "useAnimation", cardparam.useanimation);
            cardparam.showtype = getValueByinvoke(o, "showType", cardparam.showtype);
        } catch (Exception e) {

        }
        AutoCard card = new AutoCard(cardparam.resid, o, autoFit);
        card.useanimation = cardparam.useanimation;
        card.setShowType(cardparam.showtype);
        return card;
    }

    public static <T> T getValueByinvoke(Object o, String method, T defaultvalue, Class<?>... parameterTypes) {
        try {
            Method mothod = o.getClass().getMethod(method);
            if (method != null) {
                T retn = (T) mothod.invoke(o);
                if (retn != null) {
                    return retn;
                }
            }
        } catch (Exception e) {
        }
        return defaultvalue;
    }

    @Override
    public String[][] getPageNext() {
        return null;
    }

    @Override
    public void reload() {

    }

    public static class CardParam {
        public int resid = 0;
        public int showtype = 0;
        public boolean useanimation = true;

        public CardParam(int resid, int showtype, boolean useanimation) {
            this.resid = resid;
            this.showtype = showtype;
            this.useanimation = useanimation;
        }

        public CardParam() {
        }
    }
}
