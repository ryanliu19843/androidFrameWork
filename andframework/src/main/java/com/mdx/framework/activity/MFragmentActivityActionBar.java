package com.mdx.framework.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import androidx.fragment.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;

import com.mdx.framework.Frame;
import com.mdx.framework.broadcast.BIntent;
import com.mdx.framework.broadcast.BroadCast;
import com.mdx.framework.prompt.Prompt;
import com.mdx.framework.prompt.PromptDeal;
import com.mdx.framework.server.ServerParamsInit;
import com.mdx.framework.server.api.ApiManager;
import com.mdx.framework.utility.Helper;
import com.mdx.framework.utility.handle.MHandler;
import com.mdx.framework.utility.permissions.PermissionsHelper;

import java.util.List;

public abstract class MFragmentActivityActionBar extends FragmentActivity implements Prompt, IActivity {

    protected MHandler handler;

    private PromptDeal ldDialog;

    public Boolean LoadingShow = false;

    private boolean resumed = false;

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

    public Activity getActivity() {
        return this;
    }

    public Context getContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new MHandler();
        ldDialog = PromptDeal.create(this);
        String className = this.getClass().getSimpleName();
        handler.setId(className);
        handler.setMsglisnener(new MHandler.HandleMsgLisnener() {
            public void onMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        if (getParent() == null) {
                            finish();
                        }
                        break;
                    case 201:
                        disposeMsg(msg.arg1, msg.obj);
                        break;

                }
            }
        });
        Frame.HANDLES.add(handler);
        initcreate(savedInstanceState);
        Frame.setNowShowActivity(this);
        create(savedInstanceState);
        new ServerParamsInit().DataLoad(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsHelper.onRequestPermissions(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        Frame.removNowShowActivity(this);
        super.onDestroy();
        Helper.closeSoftKey(getActivity(), getActivity().getWindow().getDecorView());
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

    ;

    protected abstract void create(Bundle savedInstanceState);

    protected void initcreate(Bundle savedInstanceState) {
    }

    @Override
    protected void onPause() {
        super.onPause();
        resumed = false;
        pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumed = true;
        BroadCast.PostBroad(new BIntent("com.framework.ImageRecycle", this, null, 0, null));
        resume();
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return super.onKeyLongPress(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        return super.onKeyMultiple(keyCode, repeatCount, event);
    }

    @SuppressLint("NewApi")
    @Override
    public boolean onKeyShortcut(int keyCode, KeyEvent event) {
        return super.onKeyShortcut(keyCode, event);
    }


    public boolean getResumed() {
        return resumed;
    }

    public View findViewById(int id) {
        return super.findViewById(id);
    }
}
