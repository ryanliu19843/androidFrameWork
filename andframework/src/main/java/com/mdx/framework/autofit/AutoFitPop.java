package com.mdx.framework.autofit;

import android.content.Context;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.mdx.framework.activity.MPopDefault;
import com.mdx.framework.autofit.commons.FitPost;
import com.mdx.framework.log.MLog;
import com.mdx.framework.utility.Helper;
import com.mdx.framework.widget.pagerecycleview.autofit.AutoMViewHold;

import java.util.Map;

/**
 * Created by ryan on 2017/10/28.
 */

public class AutoFitPop extends MPopDefault {
    public AutoFitBase autoFit;
    public AutoFit Fit;

    public AutoFitPop(Context context, int resid, Map<String, Object> framagemap, AutoFitBase parentfit) {
        super(context, resid);
        initPop(framagemap, parentfit);
    }


    protected void initPop(Map<String, Object> framagemap, AutoFitBase parentfit) {
        autoFit = new AutoFitBase(this);
        autoFit.parentfit = parentfit;
        this.setId(AutoFitUtil.FITNAME);

        autoFit.bindingobj = DataBindingUtil.bind(this.contextView);
        for (String key : framagemap.keySet()) {
            autoFit.params.put(key, framagemap.get(key));
        }
        try {
            Fit = new AutoFit(autoFit);
            if(parentfit.card!=null){
                AutoMViewHold.bindCard(autoFit.bindingobj,parentfit.card);
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
                AutoFitUtil.setBind(autoFit.bindingobj, "content", autoFit.getTopFit(autoFit).iActivity);
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
}
