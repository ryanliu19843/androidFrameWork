/*
 * 文件名: ApiManager.java 版 权： Copyright XCDS Tech. Co. Ltd. All Rights Reserved.
 * 描 述: [该类的简要描述] 创建人: ryan 创建时间:2014-1-3
 *
 * 修改人： 修改时间: 修改内容：[修改内容]
 */
package com.mdx.framework.server.api;

import android.content.Context;
import android.text.TextUtils;

import com.mdx.framework.Frame;
import com.mdx.framework.broadcast.BIntent;
import com.mdx.framework.broadcast.BroadCast;
import com.mdx.framework.commons.threadpool.PRunable;
import com.mdx.framework.commons.threadpool.ThreadPool;
import com.mdx.framework.config.ErrorConfig;
import com.mdx.framework.log.MLog;
import com.mdx.framework.prompt.Prompt;
import com.mdx.framework.prompt.PromptManager;
import com.mdx.framework.utility.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 *
 * @author ryan
 * @version [2014-1-3 上午9:34:27]
 */
public class ApiManager {
    public static HashMap<Object, List<ApisRun>> LoadingApis = new HashMap<Object, List<ApisRun>>();

    public static HashMap<Object, List<ApisRun>> LoadingContextApis = new HashMap<Object, List<ApisRun>>();

    public static ThreadPool ApiThreadPool = new ThreadPool();

    public static void Load(Context context, Object parent, UpdateOne[] upons, long delay) {
        List<UpdateOne> updates = new ArrayList<UpdateOne>();
        for (UpdateOne uo : upons) {
            updates.add(uo);
        }
        Load(context, parent, updates, 0, delay);
    }

    public static void Load(Context context, Object parent, UpdateOne[] upons) {
        List<UpdateOne> updates = new ArrayList<UpdateOne>();
        for (UpdateOne uo : upons) {
            updates.add(uo);
        }
        Load(context, parent, updates, 0, 0);
    }

    public static void Load(Context context, Object parent, List<UpdateOne> updates, int type, long delay) {
        ApisRun apisRun = new ApisRun(context, parent, updates, type, delay);
        synchronized (LoadingApis) {
            add(parent, apisRun, LoadingApis);
            add(context, apisRun, LoadingContextApis);
        }
        ApiThreadPool.execute(apisRun);
    }


    public static Son LoadSync(Context context, Object parent, UpdateOne[] upons) {
        List<UpdateOne> updates = new ArrayList<UpdateOne>();
        for (UpdateOne uo : upons) {
            updates.add(uo);
        }
        return LoadSync(context, parent, updates, 0, 0);
    }

    public static Son LoadSync(Context context, Object parent, List<UpdateOne> updates, int type, long delay) {
        ApisRun apisRun = new ApisRun(context, parent, updates, type, delay);
        synchronized (LoadingApis) {
            add(parent, apisRun, LoadingApis);
            add(context, apisRun, LoadingContextApis);
        }
        apisRun.run();
        return new Son(apisRun.error, apisRun.msg);
    }

    private static void add(Object key, ApisRun apisRun, HashMap<Object, List<ApisRun>> map) {
        List<ApisRun> apilist = null;
        if (map.containsKey(key)) {
            apilist = map.get(key);
        } else {
            apilist = new ArrayList<ApisRun>();
            map.put(key, apilist);
        }
        apilist.add(apisRun);
    }

    public static void Cancel(Object parent) {
        synchronized (LoadingApis) {
            cancel(parent, LoadingApis);
            if (parent instanceof Context) {
                cancel(parent, LoadingContextApis);
            }
        }
    }

    private static void cancel(Object key, HashMap<Object, List<ApisRun>> map) {
        if (key != null) {
            if (map.containsKey(key)) {
                for (ApisRun ar : map.get(key)) {
                    ar.intermit();
                }
            }
        }
    }

    private static class ApisRun extends PRunable {
        public List<UpdateOne> mUpdates;

        public Object mParent;

        public Context mContext;

        public int type = 0;

        public long delay = 0;

        public int error = 0;
        public String msg = "";

        public ApisRun(Context context, Object parent, List<UpdateOne> updates, int type, long delay) {
            mUpdates = new ArrayList<UpdateOne>();
            for (UpdateOne uo : updates) {
                mUpdates.add(uo);
                if (uo.updateones != null && uo.updateones.size() > 0) {
                    mUpdates.addAll(uo.updateones);
                    uo.updateones.clear();
                    type = 1;
                }
            }
            this.type = type;
            this.mContext = context;
            this.mParent = parent;
            this.delay = delay;
        }

        @Override
        public void run() {
            showDialog();
            Util.sleep(100 + delay);

            if (type == 1) {
                ArrayList<String> revs = new ArrayList<String>();
                try {
                    HashMap<String, UpdateOne> hashmap = new HashMap<String, UpdateOne>();
                    HashMap<String, Son> sonmap = new HashMap<String, Son>();
                    List<String> md5strs = new ArrayList<String>();

                    for (UpdateOne uo : mUpdates) {
                        if (stoped) {
                            break;
                        }
                        uo.initRead();
                        md5strs.add(uo.getMd5str());

                        if (TextUtils.isEmpty(uo.mDatatype)) {
                            if (hashmap.containsKey(uo.mUri)) {
                                UpdateOne up = hashmap.get(uo.mUri);
                                up.updateones.add(uo);
                            } else {
                                hashmap.put(uo.mUri, uo);
                            }
                        } else {
                            sonmap.put(uo.getMd5str(), uo.getSon());
                        }
                    }

                    for (String key : hashmap.keySet()) {
                        UpdateOne uo = hashmap.get(key);
                        Son son = uo.getSon();
                        revs.add(uo.getMd5str());
                        sonmap.put(uo.getMd5str(), son);

                        for (int i = 0; i < son.sons.size(); i++) {
                            sonmap.put(uo.updateones.get(i).getMd5str(), son.sons.get(i));
                        }
                    }

                    Son retn = sonmap.get(md5strs.get(0));
                    retn.sons.clear();
                    for (int i = 1; i < md5strs.size(); i++) {
                        Son sn = sonmap.get(md5strs.get(i));
                        if (sn == null) {
                            UpdateOne updateOne = null;
                            for (UpdateOne uo : mUpdates) {
                                if (uo.getMd5str() == md5strs.get(i)) {
                                    updateOne = uo;
                                    break;
                                }
                            }
                            sn = new Son(retn.getError(), retn.getMsg(), updateOne);
                            sonmap.put(md5strs.get(i), sn);
                        } else {
                            if (retn.getError() == 0) {
                                retn.mError = sn.getError();
                                retn.mErrMethod = sn.getMethod();
                            }
                            sn.sons.clear();
                        }
                        retn.sons.add(sn);
                    }
                    if (!this.stoped) {
                        Util.sleep(100);
                        postSon(retn, md5strs.get(0));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Util.ShowError(new Son(9099, e.getMessage(), "", 0, 0), mContext);
                } finally {
                    for (String receiver : revs) {
                        BroadCast.remove(BroadCast.BROADLIST_APIMANAGER, receiver);
                    }
                }
            } else {
                for (UpdateOne uo : mUpdates) {
                    if (stoped) {
                        break;
                    }
                    try {
                        if (type == 0 && (uo.getPrompt() != null || uo.getShowLoading())) {
                            showDialog(mContext, uo.getPrompt(), true, true);
                        }
                        Son son = uo.getSon();
                        if (!this.stoped) {
                            Util.sleep(100);
                            postSon(son, uo.getMd5str());
                        } else {
                            break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Util.ShowError(new Son(9099, e.getMessage(), uo), mContext);
                    } finally {
                        if (uo.getReceiver() != null && uo.getReceiverType() == 0) {
                            BroadCast.remove(BroadCast.BROADLIST_APIMANAGER, uo.getReceiver().id);
                        }
                        if (type == 0 && (uo.getPrompt() != null || uo.getShowLoading())) {
                            showDialog(mContext, uo.getPrompt(), false, true);
                        }
                    }
                }
            }
            closeDialog();
            Util.sleep(100);
            synchronized (LoadingApis) {
                remove(mParent, LoadingApis);
                remove(mContext, LoadingContextApis);
            }
        }

        public void postSon(Son son, String md5) {
            if (Frame.getOnApiLoadListener() != null) {
                Frame.getOnApiLoadListener().onApiLoad(mContext, mParent, son);
            }
            if (son.getError() != 0) {
                ErrorMsg err = null;
                if (son.mErrorType % 10 == 0) {
                    err = Util.ShowError(son, mContext);
                } else {
                    err = ErrorConfig.getErrorMsg(son);
                }
                if (son.type % 1000 / 100 == 1 || son.mErrorType % 1000 / 100 == 1 || (err != null && err.type % 1000 / 100 == 1)) {
                    postBroadcast(md5, son);
                }
                this.error = son.getError();
                this.msg = err.name;
            } else if (!this.stoped) {
                postBroadcast(md5, son);
            }
        }

        @Override
        public void onIntermit() {
            for (UpdateOne uo : mUpdates) {
                uo.intermit();
            }
        }

        private void postBroadcast(Object id, Object data) {
            try {
                BIntent bi = new BIntent(BroadCast.BROADLIST_APIMANAGER, id, null, 0, data);
                BroadCast.PostBroad(bi);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void remove(Object key, HashMap<Object, List<ApisRun>> map) {
            if (map.containsKey(key)) {
                List<ApisRun> list = map.get(key);
                list.remove(this);
                if (list.size() == 0) {
                    map.remove(key);
                }
            }
        }

        public void closeDialog() {
            if (mContext instanceof Prompt) {
                showDialog(mContext, (Prompt) mContext, false, false);
            }
            if (mContext != mParent) {
                if (mParent instanceof Prompt) {
                    showDialog(mContext, (Prompt) mParent, false, false);
                }
            }
        }

        public void showDialog() {
            if (mContext instanceof Prompt) {
                showDialog(mContext, (Prompt) mContext, true, false);
            }
            if (mContext != mParent) {
                if (mParent instanceof Prompt) {
                    showDialog(mContext, (Prompt) mParent, true, false);
                }
            }
        }

        public synchronized void showDialog(final Context context, final Prompt prompt, final boolean show, final boolean isshow) {
            Util.HANDLER.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (show) {
                            PromptManager.show(context, prompt, isshow);
                        } else {
                            PromptManager.dismiss(context, prompt, isshow);
                        }
                    } catch (Exception e) {
                        MLog.D(MLog.SYS_RUN, e);
                    }
                }
            });
        }
    }
}
