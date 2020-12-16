package com.mdx.framework.widget.pagerecycleview.autofit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mdx.framework.activity.IActivity;
import com.mdx.framework.autofit.AutoFit;
import com.mdx.framework.autofit.AutoFitBase;
import com.mdx.framework.autofit.AutoFitUtil;
import com.mdx.framework.autofit.commons.AutoFitItem;
import com.mdx.framework.log.MLog;
import com.mdx.framework.utility.handle.MHandler;
import com.mdx.framework.widget.pagerecycleview.ada.Card;
import com.mdx.framework.widget.pagerecycleview.viewhold.MViewHold;

import java.lang.reflect.Method;

/**
 * Created by ryan on 2017/10/17.
 */

public class AutoMViewHold extends MViewHold implements IActivity {
    protected Context context;
    private ViewDataBinding bindingobj;
    protected MHandler handler;
    public AutoFitBase autoFit;

    public AutoMViewHold(View itemView, Context context) {
        super(itemView);
        this.context = context;
        handler = new MHandler();
        autoFit = new AutoFitBase(this);
        bindingobj = DataBindingUtil.bind(this.itemView);

        try {
            autoFit.bindingobj = bindingobj;
        } catch (Exception e) {
            MLog.D(e);
        }
    }

    @SuppressLint("InflateParams")
    public static AutoMViewHold getView(Context context, ViewGroup parent, int resId) {
        LayoutInflater flater = LayoutInflater.from(context);
        View convertView;
        if (parent == null) {
            convertView = flater.inflate(resId, null);
        } else {
            convertView = flater.inflate(resId, parent, false);
        }
        return new AutoMViewHold(convertView, context);
    }

    public void set(int posion, Card card) {
        this.card = card;
        try {
            autoFit.params = card.params;
            if (card instanceof AutoCard) {
                autoFit.parentfit = ((AutoCard) card).autoFit;
                autoFit.card = card;
            }
            bindCard(bindingobj, card);
            if (autoFit.parentfit.iActivity != null) {
                AutoFitUtil.setBind(bindingobj, "content", autoFit.getTopFit(autoFit).iActivity);
            }
            AutoFitUtil.setBind(autoFit.bindingobj, "p", card.params);
            AutoFitUtil.setBind(autoFit.bindingobj, "f", new AutoFit(autoFit));
            bindingobj.executePendingBindings();
        } catch (Exception e) {
            MLog.D(e);
        }
    }

    public void rebindItem() {
        AutoFitUtil.setBind(bindingobj, "item", card.item);
        bindingobj.executePendingBindings();
    }

    public static void bindCard(ViewDataBinding dataBinding, Card card) {
        setBindCard(dataBinding, "card", card);
        boolean isbitem = AutoFitUtil.setBind(dataBinding, "item", card.item);
        if (card.item instanceof AutoFitItem) {
            Object obj = ((AutoFitItem) card.item).getObj();
            if (isbitem) {
                AutoFitUtil.setBind(dataBinding, "o", obj);
            } else {
                AutoFitUtil.setBind(dataBinding, "item", card.item);
                AutoFitUtil.setBind(dataBinding, "o", obj);
            }
        }
    }

    private static boolean setBindCard(ViewDataBinding dataBinding, String methodname, Object obj) {
        try {
            Method setmethod = dataBinding.getClass().getMethod(methodname, Card.class);
            if (setmethod != null) {
                setmethod.invoke(dataBinding, obj);
                return true;
            }
        } catch (Exception e) {

        }
        return false;
    }

    @Override
    public boolean getResumed() {
        return false;
    }

    @Override
    public void finish() {
        if (context instanceof Activity) {
            if (autoFit.parentfit != null) {
                autoFit.parentfit.close();
            } else {
                ((Activity) context).finish();
            }
        }
    }

    @Override
    public void disposeMsg(int type, Object obj) {

    }

    @Override
    public View getContextView() {
        return this.itemView;
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public MHandler getHandler() {
        return handler;
    }

    @Override
    public MViewHold delete() {
        this.card.getAdapter().remove(this.card);
        return this;
    }
}
