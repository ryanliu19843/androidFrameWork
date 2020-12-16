package com.mdx.framework.activity.taskactivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mdx.framework.Frame;
import com.mdx.framework.R;
import com.mdx.framework.activity.MActivity;

/**
 * Created by ryan on 2017/5/23.
 */

public class ActRestart extends MActivity {
    public TextView title;
    public TextView message;
    public LinearLayout messagecontext;
    public ScrollView message_scrollView;
    public Button button_accept;
    public RelativeLayout contentDialog;
    public RelativeLayout dialog_rootView;

    @Override
    protected void create(Bundle savedInstanceState) {
        this.setContentView(R.layout.act_restart_self);

        findVMethod();

        button_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Frame.finish();
            }
        });
    }

    private void findVMethod() {
        title = findViewById(R.id.title);
        message = findViewById(R.id.message);
        messagecontext = findViewById(R.id.messagecontext);
        message_scrollView = findViewById(R.id.message_scrollView);
        button_accept = findViewById(R.id.button_accept);
        contentDialog = findViewById(R.id.contentDialog);
        dialog_rootView = findViewById(R.id.dialog_rootView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Frame.finish();
        System.exit(1);
    }
}
