package com.mdx.framework.utility.validation;

import android.text.TextUtils;

import com.mdx.framework.Frame;
import com.mdx.framework.R;
import com.mdx.framework.utility.Verify;

/**
 * Created by ryan on 2017/11/2.
 */

public class NoNull implements ValidationBase {
    @Override
    public Verify validation(Object obj, Object... params) {
        boolean bol = true;
        String errmsg = "";
        if (obj == null || TextUtils.isEmpty(obj.toString())) {
            errmsg = Frame.CONTEXT.getString(R.string.verivy_null);
            bol = false;
        } else {
            int min = -1, max = -1;
            if (params != null) {
                if (params.length >= 1) {
                    min = (int) params[0];
                }
                if (params.length >= 2) {
                    max = (int) params[1];
                }
                String str = obj.toString();
                if (min >= 0 && str.length() < min) {
                    bol = false;
                    errmsg=String.format(Frame.CONTEXT.getString(R.string.verivy_lenerror),min,max);
                }
                if (max >= 0 && str.length() > max) {
                    bol = false;
                    errmsg=String.format(Frame.CONTEXT.getString(R.string.verivy_lenerror),min,max);
                }
            }
        }
        return new Verify(bol, "", bol ? "" : errmsg);
    }
}
