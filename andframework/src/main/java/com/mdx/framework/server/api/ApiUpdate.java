/*
 * t * 文件名: ApiUpdate.java 版 权： Copyright XCDS Tech. Co. Ltd. All Rights
 * Reserved. 描 述: [该类的简要描述] 创建人: ryan 创建时间:2014-1-21
 *
 * 修改人： 修改时间: 修改内容：[修改内容]
 */
package com.mdx.framework.server.api;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.mdx.framework.broadcast.BIntent;
import com.mdx.framework.broadcast.BReceiver;
import com.mdx.framework.config.ErrorConfig;
import com.mdx.framework.log.MLog;
import com.mdx.framework.prompt.ErrorPrompt;
import com.mdx.framework.prompt.Prompt;
import com.mdx.framework.utility.Util;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 *
 * @author ryan
 * @version [2014-1-21 上午9:00:14]
 */
public class ApiUpdate implements Serializable {
    private static final long serialVersionUID = 1L;

    private Context mContext;

    private Object mParent;

    private String mMethod;

    private int mReceiveType;

    private long mCatcheTime = -1;

    private boolean mSaveAble = true, havaPage = false, userSetHavepage = false;

    private int mErrorType = -1;

    private String mPageName = "page", mPageSizeName = "limit";

    private long mPage = 1, mPageSize = 10;

    private String mSonId;

    private Prompt mPrompt;

    private UpdateOne mUpdateOne, iUpdataOne;

    private OnApiInitListener mOnApiInitListeners;

    private long postdelay = 0;

    private UpdateOne.OnApiLoadListener monApiLoadListener;

    public ApiUpdate[] apiUpdates;

    private Boolean mShowLoading = false, isSetShowLoading = false;

    private String[][] PageParams;
    private Map<String, Object> sonparams = new HashMap<>();

    private int type = Integer.MAX_VALUE;

    private Map<String, Object> autofitParams;
    public OnApiLoadListener onApiLoadListener;


    protected UpdateOne initUpdateOne(UpdateOne update) {
        this.mUpdateOne = update.clone();
        update.setSaveAble(isSaveAble());
        update.setCacheTime(getCatcheTime());
        update.setErrorType(getErrorType());
        if (type != Integer.MAX_VALUE) {
            update.setType(type);
        }
        update.setPageName(getPageName());
        if (havaPage) {
            update.setPage(mPage);
        }
        update.setPageSize(mPageSize);
        update.setHaspage(havaPage);
        update.setPageSizeName(mPageSizeName);
        update.setPageParams(PageParams);
        update.setPrompt(mPrompt);
        update.setSonParams(sonparams);
        update.autofitparams = autofitParams;
        if (this.monApiLoadListener != null) {
            update.monApiLoadListener = this.monApiLoadListener;
        }
        update.setSonId(mSonId);
        if (isSetShowLoading) {
            update.setShowLoading(mShowLoading);
        }
        update.updateones.clear();
        if (mOnApiInitListeners != null) {
            ApiUpdate[] list = mOnApiInitListeners.onApiInitListener(mPage, update);
            if (list != null) {
                for (ApiUpdate au : list) {
                    update.updateones.add(au.getUpdateOne());
                }
            }
        }
        update.setReceiver(new Receiver(getContext()), getReceiveType());
        iUpdataOne = update;
        return update;
    }

    public String getMd5str() {
        getUpdateOne().initRead();
        return iUpdataOne.getMd5str();
    }

    public UpdateOne getUpdateOne() {
        return initUpdateOne(mUpdateOne);
    }

    public void loadUpdateOne() {
        ApiManager.Load(getContext(), getParent(), new UpdateOne[]{getUpdateOne()}, getPostdelay());
    }

    public Context getContext() {
        return mContext;
    }

    public ApiUpdate setContext(Context context) {
        this.mContext = context;
        return this;

    }

    public Object getParent() {
        return mParent;
    }

    public ApiUpdate setParent(Object parent) {
        this.mParent = parent;
        return this;
    }

    public long getPage() {
        return mPage;
    }

    public ApiUpdate setPage(long page) {
        this.mPage = page;
        return this;
    }

    public String getMethod() {
        return mMethod;
    }

    public ApiUpdate setMethod(String method) {
        this.mMethod = method;
        return this;
    }

    public int getReceiveType() {
        return mReceiveType;
    }

    public ApiUpdate setReceiveType(int receiveType) {
        this.mReceiveType = receiveType;
        return this;
    }

    public long getCatcheTime() {
        return mCatcheTime;
    }

    public ApiUpdate setCatcheTime(long catcheTime) {
        this.mCatcheTime = catcheTime;
        return this;
    }

    public boolean isSaveAble() {
        return mSaveAble;
    }

    public ApiUpdate setSaveAble(boolean saveAble) {
        this.mSaveAble = saveAble;
        return this;
    }

    public int getErrorType() {
        return mErrorType;
    }

    public ApiUpdate setErrorType(int errorType) {
        this.mErrorType = errorType;
        return this;
    }

    public String getPageName() {
        return mPageName;
    }

    public boolean isHavaPage() {
        return havaPage;
    }

    public ApiUpdate setHavaPage(boolean havaPage) {
        if (!userSetHavepage) {
            this.havaPage = havaPage;
        }
        return this;
    }

    public ApiUpdate setHasPage(boolean havepage) {
        this.havaPage = havepage;
        userSetHavepage = true;
        return this;
    }

    public ApiUpdate setPageName(String pageName) {
        this.mPageName = pageName;
        return this;
    }

    public String getPageSizeName() {
        return mPageSizeName;
    }

    public ApiUpdate setPageSizeName(String mPageSizeName) {
        this.mPageSizeName = mPageSizeName;
        return this;
    }

    public long getPageSize() {
        return mPageSize;
    }

    public ApiUpdate setPageSize(long mPageSize) {
        this.mPageSize = mPageSize;
        return this;
    }

    public Prompt getPrompt() {
        return mPrompt;
    }

    public ApiUpdate setPrompt(Prompt prompt) {
        this.mPrompt = prompt;
        return this;
    }

    public class Receiver extends BReceiver {

        public Receiver(Context context) {
            super(context);
        }

        @Override
        public void onReceive(Context context, BIntent intent) {
            if (intent.data instanceof Son) {
                Son s = (Son) intent.data;
                if (onApiLoadListener != null) {
                    onApiLoadListener.onLoaded(s);
                }
                if (TextUtils.isEmpty(mMethod)) {
                    return;
                }
                Method method = null;
                int type = 0;
                try {
                    method = mParent.getClass().getMethod(mMethod, Son.class);
                    type = 0;
                } catch (Exception error) {
                    try {
                        if (s.getBuild() != null) {
                            method = mParent.getClass().getMethod(mMethod, s.getBuild().getClass(), Son.class);
                            type = 1;
                        }
                    } catch (Exception e) {
                        Son son = new Son(9096, e.toString(), s);
                        showError(son);
                        return;
                    }
                }
                try {
                    if (method != null) {
                        switch (type) {
                            case 0:
                                method.invoke(mParent, s);
                                break;
                            default:
                                method.invoke(mParent, s.getBuild(), s);
                                break;
                        }
                    }
                } catch (Exception e) {
                    MLog.D(MLog.SYS_RUN, e);
                    Son son = new Son(9097, e.toString(), s);
                    showError(son);
                }
            }
        }

        public void showError(Son s) {
            if (s.mErrorType % 10 == 0) {
                Context act;
                if (mContext instanceof Activity) {
                    act = (Activity) mContext;
                    while (((Activity) act).getParent() != null) {
                        act = ((Activity) act).getParent();
                    }
                } else {
                    act = mContext;
                }
                ErrorMsg error = ErrorConfig.getErrorMsg(s);
                if (error.type % 100 / 10 == 0) {
                    final ErrorPrompt ed = error.getMsgPrompt(mContext);
                    ed.setMsg(error);
                    synchronized (Util.HANDLER) {

                    }
                    ed.show();
                }
            }
        }
    }

    public void intermit() {
        mUpdateOne.intermit();
    }

    public String[][] getPageParams() {
        return PageParams;
    }

    public void setPageParams(String[][] pageParams) {
        PageParams = pageParams;
    }

    public String getSonId() {
        return mSonId;
    }

    public void setSonId(String sonId) {
        this.mSonId = sonId;
    }

    /**
     * @return the mShowLoading
     */
    public Boolean getShowLoading() {
        return mShowLoading;
    }

    /**
     * @param mShowLoading the mShowLoading to set
     */
    public ApiUpdate setShowLoading(Boolean mShowLoading) {
        this.mShowLoading = mShowLoading;
        isSetShowLoading = true;
        return this;
    }

    public Boolean getIsSetShowLoading() {
        return isSetShowLoading;
    }

    public interface OnApiInitListener {
        ApiUpdate[] onApiInitListener(long page, UpdateOne updateone);
    }

    public ApiUpdate setOnApiInitListeners(OnApiInitListener onApiInitListeners) {
        this.mOnApiInitListeners = onApiInitListeners;
        return this;
    }

    public ApiUpdate addApiUpdates(final ApiUpdate[] apis) {
        this.apiUpdates = apis;
        this.setOnApiInitListeners(new OnApiInitListener() {
            @Override
            public ApiUpdate[] onApiInitListener(long page, UpdateOne updateone) {
                return apis;
            }
        });
        return this;
    }

    public ApiUpdate addApiUpdates(final List<ApiUpdate> apis) {
        ApiUpdate[] apiUpdates = new ApiUpdate[apis.size()];
        apis.toArray(apiUpdates);
        this.addApiUpdates(apiUpdates);
        return this;
    }


    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public UpdateOne.OnApiLoadListener getOnApiLoadListener() {
        return monApiLoadListener;
    }

    public ApiUpdate setOnApiLoadListener(UpdateOne.OnApiLoadListener onApiLoadListener) {
        this.monApiLoadListener = onApiLoadListener;
        return this;
    }

    public UpdateOne Load(Context context, Object parent, String method) {
        this.setMethod(method);
        this.setContext(context);
        this.setParent(parent);
        UpdateOne update = getUpdateOne();
        ApiManager.Load(this.getContext(), this.getParent(), new UpdateOne[]{update});
        return update;
    }

    public UpdateOne Load(Context context, OnApiLoadListener onApiLoadListener) {
        this.setContext(context);
        this.onApiLoadListener = onApiLoadListener;
        UpdateOne update = getUpdateOne();
        ApiManager.Load(this.getContext(), this.getParent(), new UpdateOne[]{update});
        return update;
    }

    public Son LoadSync(Context context, Object parent, String method) {
        this.setMethod(method);
        this.setContext(context);
        this.setParent(parent);
        UpdateOne update = getUpdateOne();
        return ApiManager.LoadSync(this.getContext(), this.getParent(), new UpdateOne[]{update});
    }

    public Son LoadSync(Context context, OnApiLoadListener onApiLoadListener) {
        this.setContext(context);
        this.onApiLoadListener = onApiLoadListener;
        UpdateOne update = getUpdateOne();
        return ApiManager.LoadSync(this.getContext(), this.getParent(), new UpdateOne[]{update});
    }


    public long getPostdelay() {
        return postdelay;
    }

    public void setPostdelay(long postdelay) {
        this.postdelay = postdelay;
    }

    public ApiUpdate putSonParams(String key, Object value) {
        this.sonparams.put(key, value);
        return this;
    }

    public void setAutofit(Map<String, Object> autofitParams) {
        this.autofitParams = autofitParams;
    }

    public interface OnApiLoadListener {
        void onLoaded(Son son);
    }
}
