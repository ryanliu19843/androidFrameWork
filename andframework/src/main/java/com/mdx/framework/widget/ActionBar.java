/*
 * 文件名: ActionBar.java 版 权： Copyright XCDS Tech. Co. Ltd. All Rights Reserved. 描
 * 述: [该类的简要描述] 创建人: Administrator 创建时间:2015-5-22
 *
 * 修改人： 修改时间: 修改内容：[修改内容]
 */
package com.mdx.framework.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.mdx.framework.R;
import com.mdx.framework.activity.BaseActivity;
import com.mdx.framework.utility.Device;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.view.ViewPropertyAnimator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 *
 * @author Administrator
 * @version [2015-5-22 下午4:52:16]
 */
public class ActionBar extends RelativeLayout {

    private MImageView mBackBtn;

    private TextView mTitle;

    private LinearLayout mButtons;

    private boolean isInit = false;

    private boolean runh = false, runs = false;

    private CharSequence mTitleName;

    private OnClickListener mBackClick, mTitleClick;

    private ArrayList<Map<String, Object>> viewlist = new ArrayList<Map<String, Object>>();

    public ActionBar(Context context) {
        super(context);
    }

    public ActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ActionBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("NewApi")
    public ActionBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setTitle(CharSequence title) {
        mTitleName = title;
        if (mTitle != null) {
            mTitle.setText(title);
        }
    }

    public void init(final Activity context) {
        if (!isInit) {
            isInit = true;
            for (Map<String, Object> map : viewlist) {
                TextView view = (TextView) map.get("v");
                mButtons.addView(view);
            }
        }
        if (mBackBtn != null && mBackClick == null) {
            mBackBtn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    context.finish();
                }
            });
        }
        if (mTitleClick != null && mTitle != null) {
            mTitle.setOnClickListener(mTitleClick);
        }
        if (mTitleName != null && mTitle != null) {
            mTitle.setText(mTitleName);
        }
        if (mBackClick != null && mBackBtn != null) {
            mBackBtn.setOnClickListener(mBackClick);
        }

    }

    public void setTitleClick(OnClickListener click) {
        mTitleClick = click;
        if (mTitle != null) {
            mTitle.setOnClickListener(click);
        }
    }

    public void setBackClick(OnClickListener click) {
        mBackClick = click;
        if (mBackBtn != null) {
            mBackBtn.setOnClickListener(click);
        }
    }

    public void addButton(View view) {
        if (isInit) {
            mButtons.addView(view);
        } else {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("v", view);
            viewlist.add(map);
        }
    }

    @SuppressLint("InflateParams")
    public View addButton(int res) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(res, null);
        if (isInit) {
            mButtons.addView(v);
        } else {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("v", v);
            viewlist.add(map);
        }
        return v;
    }

    public void show() {
        if (!runs) {
            runs = true;
            runh = false;
            ViewPropertyAnimator.animate(this)
                    .y((Device.statusBarEnable ? Device.statush : 0))
                    .setDuration(500)
                    .setListener(new AnimatorListenerAdapter() {

                        @Override
                        public void onAnimationEnd(Animator arg0) {
                            runs = false;
                        }
                    });
        }
    }

    public void hide() {
        if (!runh) {
            runh = true;
            runs = false;
            ViewPropertyAnimator.animate(this)
                    .y(-(Device.statusBarEnable ? Device.statush : 0) - Device.actionbarh)
                    .setDuration(500)
                    .setListener(new AnimatorListenerAdapter() {

                        @Override
                        public void onAnimationEnd(Animator arg0) {
                            runh = false;
                        }

                    });
        }
    }

    /**
     * @return the mBackBtn
     */
    public MImageView getBackView() {
        return mBackBtn;
    }

    /**
     *
     */
    public void setBackView(MImageView button) {
        this.mBackBtn = button;
        if (mBackClick != null && mBackBtn != null) {
            mBackBtn.setOnClickListener(mBackClick);
        }
    }

    /**
     * @return the mTitle
     */
    public TextView getTitleView() {
        return mTitle;
    }

    /**
     *
     */
    public void setTitleView(TextView title) {
        this.mTitle = title;
        if (mTitleClick != null && mTitle != null) {
            mTitle.setOnClickListener(mTitleClick);
        }
        if (mTitleName != null && mTitle != null) {
            mTitle.setText(mTitleName);
        }
    }

    /**
     * @return the mButtons
     */
    public LinearLayout getButtonsView() {
        return mButtons;
    }

    /**
     *
     */
    public void setButtonsView(LinearLayout buttons) {
        this.mButtons = buttons;
    }

}
