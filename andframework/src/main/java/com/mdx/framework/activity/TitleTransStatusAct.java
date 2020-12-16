package com.mdx.framework.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mdx.framework.R;

public class TitleTransStatusAct extends BaseActivity {

    @Override
    public void create(Bundle savedInstanceState) {
        setContentView(R.layout.default_act_transstatus_title);
        this.isSkipStatus = true;
    }

    @SuppressLint("NewApi")
    protected void actionBarInit() { // 初始化 actionbar
        {
            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) topWidget.getLayoutParams();
            rlp.width = LayoutParams.MATCH_PARENT;
            rlp.height = (statusBarEnable ? statush : 0);
            topWidget.setLayoutParams(rlp);
        }
        {
            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) bottomWidget.getLayoutParams();
            rlp.width = LayoutParams.MATCH_PARENT;
            rlp.height = navigationbarEnable ? navigationbarh : 0;
            bottomWidget.setLayoutParams(rlp);
        }
        {
            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) defautltContainer.getLayoutParams();
            rlp.bottomMargin = navigationbarEnable ? navigationbarh : 0;
            rlp.topMargin = (statusBarEnable ? statush : 0);
            defautltContainer.setLayoutParams(rlp);
        }
    }


    protected void setDefaultActionBar() {
        if (getActionbar().getChildCount() == 0) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View v = inflater.inflate(R.layout.default_actionbar_trans, getActionbar());
            getActionbar().setBackView(v.findViewById(R.id.action_back));
            getActionbar().setTitleView((TextView) v.findViewById(R.id.action_title));
            getActionbar().setButtonsView((LinearLayout) v.findViewById(R.id.action_buttons));
            getActionbar().init(getActivity());
        }
    }
}
