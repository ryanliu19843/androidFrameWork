package com.mdx.framework.widget.pagerecycleview.autofit;

import android.content.Context;
import android.text.TextUtils;

import com.mdx.framework.autofit.AutoFitBase;
import com.mdx.framework.commons.ParamsManager;
import com.mdx.framework.server.api.Son;
import com.mdx.framework.widget.pagerecycleview.ada.Card;
import com.mdx.framework.widget.pagerecycleview.ada.CardAdapter;
import com.mdx.framework.widget.pagerecycleview.ada.MAdapter;
import com.mdx.framework.widget.util.DataFormat;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ryan on 2017/10/17.
 */

public class AutoCardAdapter extends CardAdapter {
    public int resid;
    public AutoFitBase autoFit;
    public HashMap<Class, AutoDataFormat.CardParam> resids = new HashMap<>();


    public AutoCardAdapter(Context context){
        super(context);
    }

    public AutoCardAdapter(Context context, int resid, AutoFitBase autoFit, HashMap<Class, AutoDataFormat.CardParam> resids) {
        super(context);
        set(resid, autoFit, resids);
    }

    public AutoCardAdapter(Context context, Card[] list, int resid, AutoFitBase autoFit, HashMap<Class, AutoDataFormat.CardParam> resids) {
        super(context, list);
        set(resid, autoFit, resids);
    }

    public AutoCardAdapter(Context context, List<Card> list, int resid, AutoFitBase autoFit, HashMap<Class, AutoDataFormat.CardParam> resids) {
        super(context, list);
        set(resid, autoFit, resids);
    }

    public void set(int resid, AutoFitBase autoFit, HashMap<Class, AutoDataFormat.CardParam> resids) {
        this.resid = resid;
        this.autoFit = autoFit;
        this.resids = resids;
    }


    @Override
    public void AddAll(List list) {
        List<Card> added = new ArrayList<>();
        for (Object obj : list) {
            if (obj instanceof Card) {
                added.add((Card) obj);
            } else {
                AutoCard autoCard = AutoDataFormat.getCard(obj, resids, resid, autoFit);
                added.add(autoCard);
            }
        }
        super.AddAll(added);
    }

    @Override
    public void AddAllOnBegin(List list) {
        List<Card> added = new ArrayList<>();
        for (Object obj : list) {
            if (obj instanceof Card) {
                added.add((Card) obj);
            } else {
                AutoCard autoCard = AutoDataFormat.getCard(obj, resids, resid, autoFit);
                added.add(autoCard);
            }
        }
        super.AddAllOnBegin(added);
    }

    @Override
    public void add(Object item) {
        Card card;
        if (item instanceof Card) {
            card = (Card) item;
        } else {
            card = AutoDataFormat.getCard(item, resids, resid, autoFit);
        }
        super.add(card);
    }

    @Override
    public void add(int ind, Object item) {
        Card card;
        if (item instanceof Card) {
            card = (Card) item;
        } else {
            card = AutoDataFormat.getCard(item, resids, resid, autoFit);
        }
        super.add(ind, card);
    }
}
