package com.mdx.framework.utility.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mdx.framework.Frame;
import com.mdx.framework.broadcast.BIntent;
import com.mdx.framework.broadcast.BroadCast;
import com.mdx.framework.broadcast.OnReceiverListener;
import com.mdx.framework.log.MLog;

public class FloatView {
    public static boolean ISADDED = false; // 是否已增加悬浮窗
    
    private WindowManager wm;
    
    private WindowManager.LayoutParams buttonparams, logparams;
    
    private View button, logshow;
    
    private TextView netStatistics, logshowTv;
    
    public static String LOGTOOLS = "logtools";
    
    /**
     * 创建悬浮窗
     */
    @SuppressWarnings("deprecation")
    public void createFloatView(Context context) {
        logshow = createLogView(context);
        button = createButtonView(context);
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        buttonparams = createParams(60, 40, false);
        logparams = createParams(wm.getDefaultDisplay().getWidth(), wm.getDefaultDisplay().getHeight(), true);
        logshow.setVisibility(View.GONE);
        wm.addView(logshow, logparams);
        wm.addView(button, buttonparams);
        ISADDED = true;
    }
    
    public void removeToolView() {
        wm.removeViewImmediate(logshow);
        wm.removeViewImmediate(button);
    }
    
    public WindowManager.LayoutParams createParams(int w, int h, boolean notach) {
        WindowManager.LayoutParams layout = new WindowManager.LayoutParams();
        layout.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        layout.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明
        if (notach) {
            layout.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        } else {
            layout.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            ;
        }
        layout.width = (int) (w * Frame.DENSITY);
        layout.height = (int) (h * Frame.DENSITY);
        return layout;
    }
    
    public View createLogView(Context context) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(0x55000000);
        {
            netStatistics = new TextView(context);
            @SuppressWarnings("deprecation")
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT);
            netStatistics.setLayoutParams(lp);
            netStatistics.setTextColor(0xffff0000);
            netStatistics.setPadding(10, 0, 10, 0);
            layout.addView(netStatistics);
        }
        {
            logshowTv = new TextView(context);
            @SuppressWarnings("deprecation")
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
                    LayoutParams.FILL_PARENT);
            lp.weight = 1;
            logshowTv.setLayoutParams(lp);
            logshowTv.setPadding(10, 0, 10, 0);
            logshowTv.setTextColor(0xffffffff);
            layout.addView(logshowTv);
        }
        return layout;
    }
    
    @SuppressLint("ClickableViewAccessibility")
    public Button createButtonView(Context context) {
        final Button btn = new Button(context);
        btn.setText("LOG");
        btn.setPadding(0, 0, 0, 0);
        btn.setOnTouchListener(new OnTouchListener() {
            int lastX, lastY;
            
            int paramX, paramY;
            
            boolean click = true;
            
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        click = true;
                        paramX = buttonparams.x;
                        paramY = buttonparams.y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;
                        buttonparams.x = paramX + dx;
                        buttonparams.y = paramY + dy;
                        if (dx * dx + dy * dy > 30 * 30) {
                            click = false;
                            btn.onTouchEvent(createCancelEvent(event));
                        }
                        // 更新悬浮窗位置
                        wm.updateViewLayout(btn, buttonparams);
                        break;
                }
                if (click) {
                    btn.onTouchEvent(event);
                }
                return true;
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (logshow.getVisibility() == View.GONE) {
                    BroadCast.addReceiver(v.getContext(),
                            MLog.LOGTOOLS_LOGSHOW,
                            "",
                            LOGTOOLS,
                            new OnReceiverListener() {
                                @Override
                                public void onReceiver(Context context, BIntent intent) {
                                    String str = logshowTv.getText().toString();
                                    String[] strs = str.split("\n");
                                    StringBuffer sb = new StringBuffer();
                                    int ind = 0;
                                    if (strs.length > 5) {
                                        ind = strs.length - 5;
                                    }
                                    for (int i = ind; i < strs.length; i++) {
                                        sb.append(strs[i] + "\n");
                                    }
                                    sb.append(intent.data.toString() + "\n");
                                    logshowTv.setText(sb);
                                }
                            });
                    BroadCast.addReceiver(v.getContext(),
                            NetStatistics.STATISTICS_BROADCAST_ACTION,
                            "",
                            LOGTOOLS,
                            new OnReceiverListener() {
                                @Override
                                public void onReceiver(Context context, BIntent intent) {
                                    netStatistics.setText(intent.data.toString());
                                }
                            });
                    NetStatistics.log();
                    logshow.setVisibility(View.VISIBLE);
                } else {
                    netStatistics.setText("");
                    logshowTv.setText("");
                    BroadCast.removeByParent(LOGTOOLS);
                    logshow.setVisibility(View.GONE);
                }
            }
        });
        return btn;
    }
    
    /**
     * 创建一个取消按键
     * 
     * @param ev
     * @return
     */
    @SuppressLint("Recycle")
    public MotionEvent createCancelEvent(MotionEvent ev) {
        return MotionEvent.obtain(ev.getDownTime(),
                SystemClock.uptimeMillis(),
                MotionEvent.ACTION_CANCEL,
                ev.getX(),
                ev.getY(),
                0);
    }
    
}
