package com.mdx.framework.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mdx.framework.R;
import com.mdx.framework.widget.pagerecycleview.viewhold.MViewHold;

public class TitleTransparentAct extends BaseActivity {

    @Override
    public void create(Bundle savedInstanceState) {
        setContentView(R.layout.default_act_transparent_title);
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
