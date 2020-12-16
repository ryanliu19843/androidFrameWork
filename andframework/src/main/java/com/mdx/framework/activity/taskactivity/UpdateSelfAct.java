package com.mdx.framework.activity.taskactivity;

import android.os.Bundle;

import androidx.core.content.FileProvider;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mdx.framework.R;
import com.mdx.framework.activity.MActivity;

/**
 * Created by ryan on 2017/4/28.
 */

public class UpdateSelfAct extends MActivity {

    public TextView message;
    public TextView smallLabel;
    public LinearLayout messagecontext;
    public LinearLayout message_scrollView;
    public Button button_submit;
    public Button button_caner;
    public RelativeLayout contentDialog;
    public RelativeLayout dialog_rootView;

    @Override
    protected void create(Bundle savedInstanceState) {
        this.setContentView(R.layout.default_update_self);
    }

    private void findVMethod() {
        message = (TextView) findViewById(R.id.message);
        smallLabel = (TextView) findViewById(R.id.smallLabel);
        messagecontext = (LinearLayout) findViewById(R.id.messagecontext);
        message_scrollView = (LinearLayout) findViewById(R.id.message_scrollView);
        button_submit = (Button) findViewById(R.id.button_submit);
        button_caner = (Button) findViewById(R.id.button_caner);
        contentDialog = (RelativeLayout) findViewById(R.id.contentDialog);
        dialog_rootView = (RelativeLayout) findViewById(R.id.dialog_rootView);
    }
}