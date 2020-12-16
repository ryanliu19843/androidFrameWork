package com.mdx.framework.utility.validation;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.mdx.framework.Frame;
import com.mdx.framework.R;
import com.mdx.framework.utility.Verify;

/**
 * Created by ryan on 2017/11/2.
 */

public class IsNum implements ValidationBase {
    @Override
    public Verify validation(Object obj, Object... params) {
        if (TextUtils.isEmpty((String) obj)) {
            obj = "0";
        }
        boolean bol = Verify.isNumeric((String) obj);
        String errmsg = Frame.CONTEXT.getString(R.string.verivy_number);
        int min = -1, max = -1;
        if (params != null) {
            if (params.length >= 1) {
                min = (int) params[0];
            }
            if (params.length >= 2) {
                max = (int) params[1];
            }
            long str = Long.parseLong(obj.toString());
            if (min >= 0 && str < min) {
                bol = false;
                errmsg = String.format(Frame.CONTEXT.getString(R.string.verivy_number_max), min, max);
            }
            if (max >= 0 && str > max) {
                bol = false;
                errmsg = String.format(Frame.CONTEXT.getString(R.string.verivy_number_max), min, max);
            }
        }
        return new Verify(bol, "", bol ? "" : errmsg);
    }
}
