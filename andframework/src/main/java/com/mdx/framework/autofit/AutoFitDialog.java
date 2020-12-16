package com.mdx.framework.autofit;

import android.content.Context;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.mdx.framework.autofit.commons.AutoParams;
import com.mdx.framework.autofit.commons.FitPost;
import com.mdx.framework.log.MLog;
import com.mdx.framework.prompt.MDialog;
import com.mdx.framework.utility.Helper;
import com.mdx.framework.widget.pagerecycleview.autofit.AutoMViewHold;

import java.util.Map;

/**
 * Created by ryan on 2017/10/30.
 */

public class AutoFitDialog extends MDialog {
    public AutoFitBase autoFit;
    public LayoutInflater inflater;
    public View contentView;
    public AutoFit Fit;
    public AutoFitBase parentfit;
    public int resourceid;
    public AutoParams framagemap = new AutoParams();

    public AutoFitDialog(Context context, int resid, Map<String, Object> framagemap, AutoFitBase parentfit) {
        super(context);
        this.parentfit = parentfit;
        this.resourceid = resid;
        if (framagemap != null) {
            for (String key : framagemap.keySet()) {
                this.framagemap.put(key, framagemap.get(key));
            }
        }
        inflater = LayoutInflater.from(context);
    }

    public AutoFitDialog(Context context, int theme, int resid, Map<String, Object> framagemap, AutoFitBase parentfit) {
        super(context, theme);
        this.resourceid = resid;
        if (framagemap != null) {
            for (String key : framagemap.keySet()) {
                this.framagemap.put(key, framagemap.get(key));
            }
        }
        inflater = LayoutInflater.from(context);
    }

    @Override
    protected void create(Bundle savedInstanceState) {
        contentView = inflater.inflate(resourceid, null);
        setContentView(contentView);
        initDia();
    }


    protected void initDia() {
        autoFit = new AutoFitBase(this);
        autoFit.parentfit = parentfit;
        this.setId(AutoFitUtil.FITNAME);
        autoFit.bindingobj = DataBindingUtil.bind(this.getContextView());
        autoFit.params = framagemap;
        try {
            Fit = new AutoFit(autoFit);
            if (parentfit.card != null) {
                AutoMViewHold.bindCard(autoFit.bindingobj, parentfit.card);
            }
            for (String k : framagemap.keySet()) {
                if (k.startsWith("#")) {
                    try {
                        AutoFitUtil.setBind(autoFit.bindingobj, k.substring(1), framagemap.get(k));
                    } catch (Exception e) {
                        Helper.toaste(e, getContext());
                    }
                }
            }
            AutoFitUtil.setBind(autoFit.bindingobj, "F", Fit);
            AutoFitUtil.setBind(autoFit.bindingobj, "P", autoFit.params);
            if (autoFit.getTopFit(autoFit).iActivity != null) {
                AutoFitUtil.setBind(autoFit.bindingobj, "Content", autoFit.getTopFit(autoFit).iActivity);
            }
            autoFit.bindingobj.executePendingBindings();
        } catch (Exception e) {
            MLog.D(e);
        }
    }

    @Override
    public void disposeMsg(int type, Object obj) {
        if (obj instanceof FitPost) {
            FitPost post = (FitPost) obj;
            if (post.resid == resourceid) {
                autoFit.dispost(type, post);
            }
        }
    }

    @Override
    public View getContextView() {
        return contentView;
    }
}
