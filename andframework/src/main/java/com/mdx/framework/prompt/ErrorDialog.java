package com.mdx.framework.prompt;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.mdx.framework.server.api.ErrorMsg;


public class ErrorDialog implements ErrorPrompt {
    private ErrorMsg errorMsg;
    private Context context;
	public ErrorDialog(Context context) {
	    this.context=context;
	}

	public void setMsg(ErrorMsg em) {
	    this.errorMsg=em;
	}

	public void show() {
		if(!TextUtils.isEmpty(errorMsg.value)) {
			Toast.makeText(context, errorMsg.value, Toast.LENGTH_LONG).show();
		}
	}

	public void dismiss() {
//		dialog.dismiss();
	}
}
