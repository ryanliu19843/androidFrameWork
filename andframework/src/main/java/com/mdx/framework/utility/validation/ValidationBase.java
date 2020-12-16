package com.mdx.framework.utility.validation;

import com.mdx.framework.utility.Verify;

/**
 * Created by ryan on 2017/11/2.
 */

public interface ValidationBase {
    public Verify validation(Object obj, Object... params);
}
