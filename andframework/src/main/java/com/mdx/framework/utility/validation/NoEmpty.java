package com.mdx.framework.utility.validation;

import android.text.TextUtils;

import com.mdx.framework.Frame;
import com.mdx.framework.R;
import com.mdx.framework.utility.Verify;

/**
 * Created by ryan on 2017/11/2.
 */

public class NoEmpty implements ValidationBase {
    @Override
    public Verify validation(Object obj, Object... params) {
        boolean bol=true;
        if(obj!=null && TextUtils.isEmpty(obj.toString())){
            bol= true;
        }else{
            bol= false;
        }

        return new Verify(bol, "", bol?"": Frame.CONTEXT.getString(R.string.verivy_null));
    }
}
