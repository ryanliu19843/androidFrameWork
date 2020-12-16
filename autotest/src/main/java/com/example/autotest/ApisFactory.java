package com.example.autotest;

import com.mdx.framework.server.api.base.ApiUpdateApi;
import com.udows.common.proto.apis.ApiMFeedback;
import com.udows.common.proto.apis.ApiMSysParamByCode;
import com.udows.common.proto.apis.ApiMSysParams;
import com.udows.common.proto.apis.ApiMUploadFile;

/**
 * Created by ryan on 2017/10/19.
 */

public class ApisFactory {   //通过工具自动生成
    public static ApiUpdateApi getApiUpdateApi() {
        return new ApiUpdateApi();
    }

    public static ApiMUploadFile getApiMUploadFile() {
        return new ApiMUploadFile();
    }

    public static ApiMFeedback getApiMFeedback() {
        return new ApiMFeedback();
    }

    public static ApiMSysParams getApiMSysParams() {
        return new ApiMSysParams();
    }

    public static ApiMSysParamByCode getApiMSysParamByCode() {
        return new ApiMSysParamByCode();
    }
}
