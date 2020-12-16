package com.mdx.framework.utility.validation;

import com.mdx.framework.Frame;
import com.mdx.framework.R;
import com.mdx.framework.utility.Verify;

/**
 * Created by ryan on 2017/11/2.
 */

public class Phone implements ValidationBase {
    @Override
    public Verify validation(Object obj, Object... params) {
        boolean bol= Verify.isNumeric((String) obj);
        return new Verify(bol, "", bol?"": Frame.CONTEXT.getString(R.string.verivy_phone));
    }
}
