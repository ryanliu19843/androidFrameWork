package com.mdx.framework.prompt;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.mdx.framework.Frame;
import com.mdx.framework.activity.IActivity;
import com.mdx.framework.autofit.AutoFit;
import com.mdx.framework.autofit.AutoFitBase;
import com.mdx.framework.autofit.AutoFitUtil;
import com.mdx.framework.autofit.commons.FitPost;
import com.mdx.framework.broadcast.BroadCast;
import com.mdx.framework.log.MLog;
import com.mdx.framework.server.api.ApiManager;
import com.mdx.framework.utility.Helper;
import com.mdx.framework.utility.handle.MHandler;
import com.mdx.framework.widget.pagerecycleview.viewhold.MViewHold;

import java.util.List;

public abstract class MDialog extends Dialog implements Prompt, IActivity {

    protected MHandler handler;

    private PromptDeal ldDialog;
    private View contentview;
    public int resourceid = 0;
    public AutoFitBase autoFit;
    public AutoFit Fit;

    public Boolean LoadingShow = false;

    @Override
    public void pshow(boolean isshow) {
        ldDialog.pshow(isshow);
    }

    @Override
    public void pdismiss(boolean isshow) {
        ldDialog.pdismiss(isshow);
    }

    @Override
    public boolean isShowing() {
        return ldDialog.isShowing();
    }

    @Override
    public boolean canShow() {
        return LoadingShow;
    }

    public MDialog(Context context) {
        super(context);
        init();
    }

    public MDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    public MDialog(Context context, int theme) {
        super(context, theme);
        init();
    }

    public void init() {
        ldDialog = PromptDeal.create(getContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new MHandler();
        String className = this.getClass().getSimpleName();
        handler.setId(className);
        handler.setMsglisnener(new MHandler.HandleMsgLisnener() {
            public void onMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        dismiss();
                        break;
                    case 201:
                        disposeMsg(msg.arg1, msg.obj);
                        if (msg.obj instanceof FitPost) {
                            FitPost post = (FitPost) msg.obj;
                            if (post.resid == resourceid) {
                                autoFit.dispost(msg.arg1, post);
                            }
                        }
                        break;

                }
            }
        });
        initcreate(savedInstanceState);
        create(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(layoutResID, null);
        this.resourceid = layoutResID;
        setContentView(view);
        try {
            initDia();
        } catch (Exception e) {
        }
    }

    @Override
    public void setContentView(@NonNull View view) {
        contentview = view;
        super.setContentView(view);
    }

    @Override
    public void setContentView(@NonNull View view, @Nullable ViewGroup.LayoutParams params) {
        contentview = view;
        super.setContentView(view, params);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        Helper.closeSoftKey(getContext(), this.getWindow().getDecorView());
        BroadCast.remove(this);
        Frame.HANDLES.remove(handler);
        ApiManager.Cancel(this);
        destroy();
    }

    public void sendMessage(Message msg) {
        handler.sendMessage(msg);
    }

    public void close(String id) {
        Frame.HANDLES.close(id);
    }

    public List<MHandler> get(String id) {
        return Frame.HANDLES.get(id);
    }

    public MHandler getOne(String id) {
        if (Frame.HANDLES.get(id).size() > 0) {
            return Frame.HANDLES.get(id).get(0);
        }
        return null;
    }

    public void setId(String id) {
        handler.setId(id);
    }

    public String getId() {
        return handler.getId();
    }

    public Object runLoad(int type, Object obj) {
        return null;
    }

    public void disposeMsg(int type, Object obj) {
    }

    public void dataLoad(int[] types) {
    }

    protected void destroy() {
    }

    protected void resume() {
    }

    ;

    protected void pause() {

    }

    protected abstract void create(Bundle savedInstanceState);

    protected void initcreate(Bundle savedInstanceState) {

    }

    protected void initDia() {
        if (autoFit != null) {
            return;
        }
        ViewDataBinding viewDataBinding = DataBindingUtil.bind(this.getContextView());
        autoFit = new AutoFitBase(this);
//        this.setId(AutoFitUtil.FITNAME);
        autoFit.bindingobj = viewDataBinding;
        if (getContext() instanceof Activity) {
            Bundle bundle = ((Activity) getContext()).getIntent().getExtras();
            if (bundle != null) {
                for (String k : bundle.keySet()) {
                    if (k.startsWith("#")) {
                        try {
                            AutoFitUtil.setBind(autoFit.bindingobj, k.substring(1), bundle.get(k));
                        } catch (Exception e) {
                            Helper.toaste(e, getContext());
                        }
                    }
                    autoFit.params.put(k, bundle.get(k));
                }
            }
        }
        try {
            Fit = new AutoFit(autoFit);
            AutoFitUtil.setBind(autoFit.bindingobj, "F", Fit);
            AutoFitUtil.setBind(autoFit.bindingobj, "P", autoFit.params);
            autoFit.bindingobj.executePendingBindings();
        } catch (Exception e) {
            MLog.D(e);
        }
    }

    public void finish() {
        this.dismiss();
    }

    @Override
    public boolean getResumed() {
        return false;
    }

    @Override
    public View getContextView() {
        return this.contentview;
    }

    @Override
    public MHandler getHandler() {
        return handler;
    }

    public void show() {
        super.show();
        Frame.HANDLES.add(handler);
    }

    @Override
    public MViewHold delete() {
        return null;
    }


    @Override
    public <T extends View> T findViewById(int id) {
        return super.findViewById(id);
    }

//    public boolean isResumed() {
//        if(this.getOwnerActivity() instanceof MFragmentActivity){
//            return ((MFragmentActivity) this.getOwnerActivity()).isResumed();
//        }
//        return true;
//    }
}
