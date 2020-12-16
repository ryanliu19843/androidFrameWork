package com.mdx.framework.utility.validation;

import com.mdx.framework.utility.Verify;

/**
 * Created by ryan on 2017/11/2.
 */

public class IDcard implements ValidationBase {
    @Override
    public Verify validation(Object obj, Object... params) {
        return Verify.validateIdCard((String) obj);
    }
}
