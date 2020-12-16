package com.mdx.framework.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.IdRes;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.os.Message;

import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.mdx.framework.Frame;
import com.mdx.framework.autofit.AutoFit;
import com.mdx.framework.autofit.AutoFitBase;
import com.mdx.framework.autofit.AutoFitUtil;
import com.mdx.framework.autofit.commons.ActResult;
import com.mdx.framework.autofit.commons.FitPost;
import com.mdx.framework.broadcast.BroadCast;
import com.mdx.framework.commons.ParamsManager;
import com.mdx.framework.log.MLog;
import com.mdx.framework.prompt.Prompt;
import com.mdx.framework.prompt.PromptDeal;
import com.mdx.framework.server.api.ApiManager;
import com.mdx.framework.utility.Helper;
import com.mdx.framework.utility.handle.MHandler;
import com.mdx.framework.widget.ActionBar;
import com.mdx.framework.widget.pagerecycleview.viewhold.MViewHold;

import java.util.ArrayList;
import java.util.List;

public abstract class MFragment extends Fragment implements Prompt, IActivity {
    protected MHandler handler;

    public AutoFitBase autoFit;
    public AutoFit Fit;

    protected View contextView = null;
    public int resourceid = 0;

    private LayoutInflater inflater;

    private ViewGroup viewgroup;

    private PromptDeal ldDialog;

    public Boolean LoadingShow = false;

    private Boolean createed = false;

    private ArrayList<OnFragmentCreateListener> onFragmentCreateListenerss = new ArrayList<OnFragmentCreateListener>();

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

    public void setContentView(int contextview) {
        this.contextView = inflater.inflate(contextview, viewgroup, false);
        this.resourceid = contextview;
        try {
            initFrg();
        } catch (Exception e) {
        }
    }

    public void setContextView(View contextview) {
        this.contextView = contextview;
    }

    public View getContextView() {
        return this.contextView;
    }

    public Context getContext() {
        return getActivity();
    }

    public ActionBar getActionBar() {
        return ((BaseActivity) getActivity()).getActionbar();
    }

    public MHandler getHandler() {
        return handler;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ldDialog = PromptDeal.create(getActivity());
        setHasOptionsMenu(true);
        MLog.I(MLog.SYS_RUN, "class:" + this.getClass().toString());
        handler = new MHandler();
        String className = this.getClass().getSimpleName();
        handler.setId(className);
        handler.setMsglisnener(new MHandler.HandleMsgLisnener() {
            public void onMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        finish();
                        break;
                    case 99:
                        removeFragment(MFragment.this);
                        break;
                    case 201:
                        try {
                            disposeMsg(msg.arg1, msg.obj);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
        Frame.HANDLES.add(handler);
        initcreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        this.viewgroup = container;
        if (!createed) {
            createed = true;
            for (OnFragmentCreateListener onf : onFragmentCreateListenerss) {
                onf.onFragmentCreateListener(this);
            }
            create(savedInstanceState);

        }
        if (this.contextView.getParent() != null) {
            if (this.contextView.getParent() instanceof ViewGroup) {
                ((ViewGroup) this.contextView.getParent()).removeView(this.contextView);
            }
        }
        return this.contextView;
    }

    public final <T extends View> T findViewById(@IdRes int id) {
        if (contextView == null) {
            return null;
        }
        return contextView.findViewById(id);
    }

    protected void removeFragment(MFragment fragment) {
        FragmentActivity parent = getActivity();
        FragmentTransaction fragmentTransaction = parent.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();
    }

    public void finish() {
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Helper.closeSoftKey(getActivity(), getActivity().getWindow().getDecorView());
        BroadCast.remove(this);
        Frame.HANDLES.remove(handler);
        ApiManager.Cancel(this);
        destroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        ParamsManager.savebuild();
        resume();
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

    public String getHId() {
        return handler.getId();
    }

    public void dataLoadByDelay(final int[] types) {
        dataLoadByDelay(types, 500);
    }

    public void dataLoadByDelay(final int[] type, long time) {
        handler.postDelayed(new Runnable() {
            public void run() {
                dataLoad(type);
            }
        }, time);
    }

    public void clearView() {
        if (contextView != null) {
            ViewParent vg = contextView.getParent();
            if (vg instanceof ViewGroup) {
                ((ViewGroup) vg).removeAllViewsInLayout();
            }
        }
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

    ;

    protected abstract void create(Bundle savedInstanceState);

    protected void initcreate(Bundle savedInstanceState) {
    }

    public void setActionBar(ActionBar actionBar, Context context) {

    }

    public void addOnFragmentCreateListener(OnFragmentCreateListener onCreate) {
        onFragmentCreateListenerss.add(onCreate);
    }

    public void removeOnFragmentCreateListener(OnFragmentCreateListener onCreate) {
        onFragmentCreateListenerss.remove(onCreate);
    }

    public interface OnFragmentCreateListener {
        public void onFragmentCreateListener(MFragment mFragment);
    }

    public View getContent() {
        return this.viewgroup;
    }

    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return false;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return false;
    }

    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        return false;
    }

    @SuppressLint("NewApi")
    public boolean onKeyShortcut(int keyCode, KeyEvent event) {
        return false;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    public boolean getResumed() {
        return isResumed();
    }


    protected void initFrg() {
        if (autoFit != null) {
            return;
        }
        ViewDataBinding viewDataBinding = DataBindingUtil.bind(this.contextView);
        autoFit = new AutoFitBase(this);
//        this.setId(AutoFitUtil.FITNAME);
        autoFit.bindingobj = viewDataBinding;
        Bundle bundle = getActivity().getIntent().getExtras();
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
        try {
            Fit = new AutoFit(autoFit);
            AutoFitUtil.setBind(autoFit.bindingobj, "F", Fit);
            AutoFitUtil.setBind(autoFit.bindingobj, "P", autoFit.params);
            autoFit.bindingobj.executePendingBindings();
        } catch (Exception e) {
            MLog.D(e);
        }
    }

    @Override
    public MViewHold delete() {
        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            ActResult actResult = new ActResult();
            actResult.requestCode = requestCode;
            actResult.resultCode = resultCode;
            actResult.data = data;
            AutoFitUtil.setBind(autoFit.bindingobj, "actresult", actResult);
        } catch (Exception e) {
            MLog.D(e);
        }
    }
}
