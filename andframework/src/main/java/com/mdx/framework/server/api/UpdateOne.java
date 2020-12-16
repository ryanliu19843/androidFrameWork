/*
 * 文件名: UpdateOne.java 版 权： Copyright XCDS Tech. Co. Ltd. All Rights Reserved. 描
 * 述: [该类的简要描述] 创建人: ryan 创建时间:2013-12-24
 * 
 * 修改人： 修改时间: 修改内容：[修改内容]
 */
package com.mdx.framework.server.api;

import android.text.TextUtils;

import com.mdx.framework.Frame;
import com.mdx.framework.broadcast.BReceiver;
import com.mdx.framework.broadcast.BroadCast;
import com.mdx.framework.commons.CanIntermit;
import com.mdx.framework.commons.ParamsManager;
import com.mdx.framework.config.ApiConfig;
import com.mdx.framework.log.MLog;
import com.mdx.framework.prompt.Prompt;
import com.mdx.framework.server.api.impl.ApiRead;
import com.mdx.framework.utility.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 从服务器取一个接口<BR>
 *
 * @author ryan
 * @version [2013-12-24 上午11:04:48]
 */
public class UpdateOne {
    private String mMd5str = "", mUrl = "";

    public String mUri, mMethodUrl;
    private String mId;

    private CanIntermit mServerIntermit;
    public String autoapiparamsStr = "";

    private Object mPostdata;

    public Object params, data;

    private boolean haspage = false, hasPageParams = false;

    public String[][] pageParams;

    private String mClassName = null;

    private Object mResultBuild = null;

    private int mType = 0;

    public String mDatatype = "", mEncode = "", mUpencode = "";

    private Long mCacheTime = 86400000L;

    private boolean isType = false, isCacheTime = false, isErrorType = false, isResultBuild = false, isSaveError = false, isDatatype = false, isEncode = false, isUpEncode = false;

    public boolean inited = false, initObj = false;

    private int mErrorType = 0;

    private HashMap<String, String> mMap = new HashMap<String, String>();

    private Prompt mPrompt;

    private boolean mSaveAble = true;

    private BReceiver mReceiver;

    private int mReceiverType = 0;

    private List<Integer> mSaveError;

    private long mPage = 1;

    private String mPageName = "page";

    private long mPageSize = 10;

    private String mPageSizeName = "limit";

    private ApiRead mapiRead;

    private Boolean mShowLoading = false;

    public Boolean isSetShowLoading = false;

    public Object mbuild;

    private String sonId = UUID.randomUUID().toString();

    public Map<String, Object> sonParams = new HashMap<String, Object>();

    public ArrayList<UpdateOne> updateones = new ArrayList<UpdateOne>();

    public OnApiLoadListener monApiLoadListener;

    public Map<String,Object> autofitparams=new HashMap<>();

    public UpdateOne() {

    }

    public UpdateOne clone() {
        UpdateOne retn = new UpdateOne();
        retn.mMd5str = this.mMd5str;
        retn.mUrl = this.mUrl;
        retn.mUri = this.mUri;
        retn.mMethodUrl = this.mMethodUrl;
        retn.mId = this.mId;
        retn.mServerIntermit = this.mServerIntermit;
        retn.autoapiparamsStr = this.autoapiparamsStr;
        retn.mPostdata = this.mPostdata;
        retn.params = this.params;
        retn.data = this.data;
        retn.haspage = this.haspage;
        retn.hasPageParams = this.hasPageParams;
        retn.pageParams = this.pageParams;
        retn.mClassName = this.mClassName;
        retn.mResultBuild = this.mResultBuild;
        retn.mType = this.mType;
        retn.mDatatype = this.mDatatype;
        retn.mEncode = this.mEncode;
        retn.mUpencode = this.mUpencode;
        retn.mCacheTime = this.mCacheTime;
        retn.isType = this.isType;
        retn.isCacheTime = this.isCacheTime;
        retn.isErrorType = this.isErrorType;
        retn.isResultBuild = this.isResultBuild;
        retn.isSaveError = this.isSaveError;
        retn.isDatatype = this.isDatatype;
        retn.isEncode = this.isEncode;
        retn.isUpEncode = this.isUpEncode;
        retn.mbuild = this.mbuild;
        retn.sonId = this.sonId;
        retn.sonParams = this.sonParams;
        retn.updateones = this.updateones;
        retn.inited = this.inited;
        retn.initObj = this.initObj;
        retn.mErrorType = this.mErrorType;
        retn.mMap = this.mMap;
        retn.mPrompt = this.mPrompt;
        retn.mSaveAble = this.mSaveAble;
        retn.mReceiver = this.mReceiver;
        retn.mReceiverType = this.mReceiverType;
        retn.mSaveError = this.mSaveError;
        retn.mPage = this.mPage;
        retn.mPageName = this.mPageName;
        retn.mPageSize = this.mPageSize;
        retn.mPageSizeName = this.mPageSizeName;
        retn.mapiRead = this.mapiRead;
        retn.mShowLoading = this.mShowLoading;
        retn.isSetShowLoading = this.isSetShowLoading;
        retn.autofitparams=this.autofitparams;
        return retn;
    }

    public UpdateOne(String id) {
        this.mId = id;
    }

    public UpdateOne(String id, int type) {
        this.mId = id;
        this.mType = type;
        isType = true;
    }

    public UpdateOne(String id, int type, int errorType) {
        this.mId = id;
        this.mType = type;
        this.mErrorType = errorType;
        isType = true;
        isErrorType = true;
    }

    public UpdateOne(String id, Object params, Object data, int type, int errorType, Object build) {
        this.mId = id;
        this.mType = type;
        this.mErrorType = errorType;
        this.mResultBuild = build;
        this.params = params;
        this.data = data;
        isType = true;
        isErrorType = true;
        isResultBuild = true;
    }

    public UpdateOne(String id, Object params, Object data) {
        this.mId = id;
        this.params = params;
        this.data = data;
    }

    public UpdateOne(String id, Object params, Object data, Object build) {
        this.mId = id;
        this.mResultBuild = build;
        isResultBuild = true;
        this.params = params;
        this.data = data;
    }

    public UpdateOne(String id, Object params) {
        this.mId = id;
        this.params = params;
    }


    public interface OnApiLoadListener {
        void onStart(UpdateOne one);

        void onEnd(UpdateOne one, Son son);

        void onError(UpdateOne one, Son son);
    }

    public void autoFit(HashMap<String,Object> autparams){
        autofitparams=autparams;
    }

    public Object fitValue(String obj){
        if(autofitparams==null){
            autofitparams=new HashMap<>();
        }
        Object value=ParamsManager.getValue(obj,autofitparams);
        return value==null?obj:value;
    }

    /**
     * 获取接口返回值
     *
     * @return
     * @throws
     * @author ryan
     * @Title: getSon
     * @Description: TODO
     */
    public Son getSon() {
        if (monApiLoadListener != null) {
            Util.HANDLER.post(new Runnable() {
                @Override
                public void run() {
                    monApiLoadListener.onStart(UpdateOne.this);
                }
            });
        }
        try {
            if (updateones != null) {
                for (UpdateOne uo : updateones) {
                    uo.initRead();
                }
            }
            initRead();
            if (getReceiver() != null) {
                getReceiver().set(BroadCast.BROADLIST_APIMANAGER, getMd5str(), null).regedit();
            }
            Son son = mapiRead.readSon(this);
            son.sonId = sonId;
            son.sonParam = sonParams;
            if (updateones != null) {
                for (int i = 0; i < son.sons.size(); i++) {
                    Son s = son.sons.get(i);
                    UpdateOne uo = updateones.get(i);
                    s.sonId = uo.getSonId();
                    s.sonParam = uo.sonParams;
                }
            }
            final Son sson = son;
            if (monApiLoadListener != null) {
                Util.HANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        if (sson.getError() != 0) {
                            monApiLoadListener.onError(UpdateOne.this, sson);
                        }
                        monApiLoadListener.onEnd(UpdateOne.this, sson);
                    }
                });
            }
            return son;
        } catch (Exception e) {
            MLog.D(MLog.SYS_RUN, e);
            Son son = new Son(97, e.getMessage(), this);
            son.sonId = sonId;
            son.sonParam = sonParams;
            final Son sson = son;
            if (monApiLoadListener != null) {
                Util.HANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        monApiLoadListener.onError(UpdateOne.this, sson);
                        monApiLoadListener.onEnd(UpdateOne.this, sson);
                    }
                });
            }
            return son;
        }
    }

    public void initRead() {
        if (!inited) {
            init();
            inited = true;
        }
        setPostdata(mapiRead.initObj(this));
        if (!initObj) {
            initObj = true;
        }
    }

    public void saveBuild(Object obj) {
        initRead();
        mapiRead.saveBuild(this, obj);
    }

    public Son readBuild() {
        initRead();
        return mapiRead.readBuild(this);
    }

    public boolean canSave(int error) {
        return checkSaveError(error);
    }

    private void init() {
        synchronized (UpdateOne.this) {
            synchronized (Frame.INITLOCK) {
            }
            String sizename = ParamsManager.get("pagesize");
            if (!TextUtils.isEmpty(sizename)) {
                mPageSizeName = ParamsManager.getString(sizename);
            }
            String pagesizenum = ParamsManager.get("pagesizenum");
            if (!TextUtils.isEmpty(pagesizenum)) {
                mPageSize = ParamsManager.getIntValue("pagesizenum");
            }
            String pagename = ParamsManager.get("page");
            if (!TextUtils.isEmpty(pagename)) {
                mPageName = ParamsManager.getString(pagename);
            }
            ApiUrl apiurl = ApiConfig.getApiUrl(getId());
            mUrl = apiurl.url;
            mMethodUrl = apiurl.methodUrl;
            mUri = apiurl.getUri();
            if (!isCacheTime) {
                mCacheTime = apiurl.cacheTime;
            }
            if (!isResultBuild) {
                mClassName = apiurl.className;
            }
            if (!isErrorType) {
                mErrorType = apiurl.errorType;
            }
            if (!isType) {
                mType = apiurl.type;
            }
            if (!isEncode) {
                mEncode = apiurl.encode;
            }
            if (!isUpEncode) {
                mUpencode = apiurl.upEncode;
            }
            if (!isDatatype) {
                mDatatype = apiurl.dataType;
            }
            if (!isSaveError) {
                mSaveError = apiurl.saveError;
            }
            mapiRead = ApiReadFactory.getApiRead(mDatatype);
            if (mClassName == null && mResultBuild != null) {
                this.mbuild = mResultBuild;
            } else if (mClassName != null && mResultBuild == null) {
                mapiRead.createRequest(this, mClassName);
            }
        }
    }

    public Object getPostdata() {
        return mPostdata;
    }

    public void setPostdata(Object postdata) {
        this.mPostdata = postdata;
    }

    public UpdateOne setPostdata(Object params, Object data) {
        this.params = params;
        this.data = data;
        return this;
    }

    public int getType() {
        return mType;
    }

    public UpdateOne setType(int Type) {
        this.mType = Type;
        isType = true;
        return this;
    }

    public Long getCacheTime() {
        return mCacheTime;
    }

    public UpdateOne setCacheTime(Long CacheTime) {
        if (CacheTime == -1) {
            return this;
        }
        this.mCacheTime = CacheTime;
        isCacheTime = true;
        return this;
    }

    public int getErrorType() {
        return mErrorType;
    }

    public UpdateOne setErrorType(int ErrorType) {
        if (ErrorType == -1) {
            return this;
        }
        this.mErrorType = ErrorType;
        isErrorType = true;
        return this;
    }

    public HashMap<String, String> getMap() {
        return mMap;
    }

    public String getMd5str() {

        if (updateones != null) {
            for (UpdateOne uo : updateones) {
                uo.getMd5str();
            }
        }
        return mMd5str;
    }

    public void setMd5str(String md5) {
        this.mMd5str = md5;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getId() {
        return mId;
    }

    public UpdateOne setId(String id) {
        mId = id;
        return this;
    }

    public boolean userCache() {
        return (getType() % 10000) / 1000 == 0;
    }

    public Prompt getPrompt() {
        return mPrompt;
    }

    public UpdateOne setPrompt(Prompt mPrompt) {
        this.mPrompt = mPrompt;
        return this;
    }

    public boolean isSaveAble() {
        return mSaveAble;
    }

    public void setSaveAble(boolean mSaveAble) {
        this.mSaveAble = mSaveAble;
    }

    public BReceiver getReceiver() {
        return mReceiver;
    }

    public UpdateOne setReceiver(BReceiver mReceiver, int type) {
        this.mReceiver = mReceiver;
        this.mReceiverType = type;
        return this;
    }

    public UpdateOne setReceiver(BReceiver mReceiver) {
        this.mReceiver = mReceiver;
        return this;
    }

    public List<Integer> getSaveError() {
        return mSaveError;
    }

    public void setSaveError(List<Integer> mSaveError) {
        isSaveError = true;
        this.mSaveError = mSaveError;
    }

    public boolean checkSaveError(int i) {
        if (i == 0) {
            return true;
        }
        if (mSaveError == null) {
            return false;
        }
        for (Integer e : mSaveError) {
            if (e == i) {
                return true;
            }
        }
        return false;
    }

    public long getPage() {
        return mPage;
    }

    public UpdateOne setPage(long Page) {
        if (this.mPage != Page) {
            this.initObj = false;
        }
        this.mPage = Page;
        return this;
    }

    public String getPageName() {
        return mPageName;
    }

    public void setPageName(String PageName) {
        if (this.mPageName != PageName) {
            this.initObj = false;
        }
        this.mPageName = PageName;
    }

    public int getReceiverType() {
        return mReceiverType;
    }

    public UpdateOne setReceiverType(int ReceiverType) {
        this.mReceiverType = ReceiverType;
        return this;
    }

    public boolean haspage() {
        return haspage;
    }

    public void setHaspage(boolean haspage) {
        if (this.haspage != haspage) {
            this.initObj = false;
        }
        this.haspage = haspage;
    }

    public void intermit() {
        if (mServerIntermit != null) {
            mServerIntermit.intermit();
        }
    }

    public void setServerIntermit(CanIntermit serverIntermit) {
        this.mServerIntermit = serverIntermit;
    }

    public long getPageSize() {
        return mPageSize;
    }

    public void setPageSize(long mPageSize) {
        if (this.mPageSize != mPageSize) {
            this.initObj = false;
        }
        this.mPageSize = mPageSize;
    }

    public String getPageSizeName() {
        return mPageSizeName;
    }

    public void setPageSizeName(String mPageSizeName) {
        if (this.mPageSizeName != mPageSizeName) {
            this.initObj = false;
        }
        this.mPageSizeName = mPageSizeName;
    }

    public boolean isHasPageParams() {
        return hasPageParams;
    }

    public String[][] getPageParams() {
        return pageParams;
    }

    public void setPageParams(String[][] pageParams) {
        if (this.pageParams != pageParams) {
            this.initObj = false;
        }
        if (pageParams != null && pageParams.length > 0) {
            this.hasPageParams = true;
            this.pageParams = pageParams;
        }
    }

    public String getSonId() {
        return sonId;
    }

    public UpdateOne setSonId(String sonId) {
        this.sonId = sonId;
        return this;
    }

    public UpdateOne put(String key, Object value) {
        this.sonParams.put(key, value);
        this.initObj = false;
        return this;
    }

    public UpdateOne setSonParams(Map<String,Object> sonParams) {
        for(String k:sonParams.keySet()){
            this.sonParams.put(k,sonParams.get(k));
        }
        this.initObj = false;
        return this;
    }

    public Object get(String key) {
        return this.sonParams.get(key);
    }

    public Boolean getShowLoading() {
        return mShowLoading;
    }

    public void setShowLoading(Boolean mShowLoading) {
        isSetShowLoading = true;
        this.mShowLoading = mShowLoading;
    }
}
