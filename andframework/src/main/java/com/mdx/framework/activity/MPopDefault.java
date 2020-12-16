package com.mdx.framework.activity;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Message;

import androidx.annotation.RequiresApi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.mdx.framework.Frame;
import com.mdx.framework.autofit.layout.FitLayout;
import com.mdx.framework.broadcast.BroadCast;
import com.mdx.framework.server.api.ApiManager;
import com.mdx.framework.utility.Device;
import com.mdx.framework.utility.Gravity;
import com.mdx.framework.utility.Helper;
import com.mdx.framework.utility.handle.MHandler;
import com.mdx.framework.widget.pagerecycleview.viewhold.MViewHold;

/**
 * Created by ryan on 2017/10/28.
 */

public class MPopDefault implements IActivity {
    public PopupWindow mPopupWindow;
    public Context context;
    public int resourceid;
    public View contextView;
    private LayoutInflater inflater;
    protected MHandler handler;


    public MPopDefault(Context context, int resid) {
        this.context = context;
        this.resourceid = resid;
        inflater = LayoutInflater.from(context);
        handler = new MHandler();
        String className = this.getClass().getSimpleName();
        handler.setId(className);
        handler.setMsglisnener(new MHandler.HandleMsgLisnener() {
            public void onMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        mPopupWindow.dismiss();
                        break;
                    case 201:
                        disposeMsg(msg.arg1, msg.obj);
                        break;

                }
            }
        });
        setContentView(resid);
    }


    public void setId(String id) {
        handler.setId(id);
    }

    @Override
    public boolean getResumed() {
        return false;
    }

    public View findViewById(int res) {
        return contextView.findViewById(res);
    }


    public void create(Context context) {
        setContentView(resourceid);
    }


    public void setTouchable(boolean bol) {
        this.mPopupWindow.setTouchable(bol);
    }


    public void setOutsideTouchable(boolean bol) {
        this.mPopupWindow.setOutsideTouchable(bol);
    }

    public void setContentView(int contextview) {
        LayoutInflater flater = LayoutInflater.from(context);
        this.contextView = inflater.inflate(contextview, null);
        mPopupWindow = new PopupWindow(contextView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                BroadCast.remove(this);
                Frame.HANDLES.remove(handler);
                ApiManager.Cancel(this);
            }
        });
    }


    public void disposeMsg(int type, Object obj) {
    }

    @Override
    public View getContextView() {
        return contextView;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public MViewHold delete() {
        return null;
    }

    @Override
    public MHandler getHandler() {
        return handler;
    }

    public void dismiss() {
        mPopupWindow.dismiss();
    }

    public void finish() {
        this.dismiss();
    }

    public void showAsDropDown(View anchor, int xoff, int yoff) {
        Frame.HANDLES.add(handler);
        mPopupWindow.showAsDropDown(anchor, xoff, yoff);
    }

    public void showAtLocation(View parent, int gravity, int x, int y) {
        Frame.HANDLES.add(handler);
        mPopupWindow.showAtLocation(parent, gravity, x, y);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        Frame.HANDLES.add(handler);
        mPopupWindow.showAsDropDown(anchor, xoff, yoff, gravity);
    }

    public void showAsDropDown(View anchor) {
        Frame.HANDLES.add(handler);
        mPopupWindow.showAsDropDown(anchor);
    }

    public void show(final View anchorView) {
        int left = -1, top = -1;
        if (anchorView instanceof FitLayout) {
            Point point = ((FitLayout) anchorView).getTouchPoint();
            if (point != null) {
                left = point.x;
                top = point.y;
            }
        }
        show(anchorView, left, top);
    }

    public void show(final View anchorView, int left, int top) {
        final int windowPos[] = new int[2];
        final int anchorLoc[] = new int[2];
        // 获取锚点View在屏幕上的左上角坐标位置
        anchorView.getLocationOnScreen(anchorLoc);

        int anchorHeight = anchorView.getHeight();
        int anchorWidth = anchorView.getWidth();
        if (left < 0) {
            left = anchorLoc[0];
            top = anchorLoc[1];
        } else {
            anchorWidth = 0;
            anchorHeight = 0;
        }
        // 获取屏幕的高宽
        final float screenHeight = Device.getMeticsH();
        final float screenWidth = Device.getMeticsW();
        this.contextView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        // 计算contentView的高宽
        final int windowHeight = this.contextView.getMeasuredHeight();
        final int windowWidth = this.contextView.getMeasuredWidth();
        // 判断需要向上弹出还是向下弹出显示
        final boolean isNeedShowUp = (screenHeight - top - anchorHeight < windowHeight);
        final boolean isright = (screenWidth - left < windowWidth);
        if (isNeedShowUp) {
            windowPos[1] = top - windowHeight;
        } else {
            windowPos[1] = top + anchorHeight;
        }
        if (isright) {
            windowPos[0] = left + anchorWidth - windowWidth;
        } else {
            windowPos[0] = left;
        }
        mPopupWindow.showAtLocation(anchorView, Gravity.TOP | Gravity.START, windowPos[0], windowPos[1]);
    }

    public boolean isshowing() {
        return mPopupWindow.isShowing();
    }

    public MHandler getOne(String id) {
        if (Frame.HANDLES.get(id).size() > 0) {
            return Frame.HANDLES.get(id).get(0);
        }
        return null;
    }
}
