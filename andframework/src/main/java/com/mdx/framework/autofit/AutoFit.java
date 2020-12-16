package com.mdx.framework.autofit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.mdx.framework.R;
import com.mdx.framework.activity.MPopDefault;
import com.mdx.framework.activity.NoTitleAct;
import com.mdx.framework.autofit.commons.PopShowParme;
import com.mdx.framework.commons.ParamsManager;
import com.mdx.framework.commons.threadpool.PRunable;
import com.mdx.framework.commons.threadpool.ThreadPool;
import com.mdx.framework.dialog.PhotoShow;
import com.mdx.framework.prompt.MDialog;
import com.mdx.framework.server.api.ApiUpdate;
import com.mdx.framework.server.api.Son;
import com.mdx.framework.utility.Helper;
import com.mdx.framework.utility.Verify;
import com.mdx.framework.utility.validation.IDcard;
import com.mdx.framework.utility.validation.IsNum;
import com.mdx.framework.utility.validation.Mail;
import com.mdx.framework.utility.validation.NoEmpty;
import com.mdx.framework.utility.validation.NoNull;
import com.mdx.framework.utility.validation.Phone;
import com.mdx.framework.utility.validation.ValidationBase;
import com.mdx.framework.widget.getphoto.PopUpdataPhoto;
import com.mdx.framework.widget.pagerecycleview.ada.CardAdapter;
import com.mdx.framework.widget.pagerecycleview.autofit.AutoDataFormat;
import com.mdx.framework.widget.util.DataFormat;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static com.mdx.framework.autofit.AutoFitUtil.getActclass;

/**
 * Created by ryan on 2017/10/28.
 */

public class AutoFit {
    public static ValidationBase NUM = new IsNum();
    public static ValidationBase MAIL = new Mail();
    public static ValidationBase EMPTY = new NoEmpty();
    public static ValidationBase IDCARD = new IDcard();
    public static ValidationBase PHONE = new Phone();
    public static ValidationBase NULL = new NoNull();
    public AutoFit nowFit;

    public AutoFitBase autoFit;
    public ThreadPool threadPool = new ThreadPool(1);
    public Son lastson;
    public Boolean hasError = false;

    public AutoFit() {

    }

    public AutoFit(AutoFitBase autoFit) {
        this.autoFit = autoFit;
        this.autoFit.fit = this;
    }

    public Context getContext() {
        return autoFit.iActivity.getContext();
    }

    public AutoFit(AutoFitBase autoFit, AutoFit nowFit) {
        this.autoFit = autoFit;
        this.nowFit = nowFit;
    }

    //***********************重新绑定数据*******************************
    public AutoFit execsync() {
        if (autoFit.bindingobj != null) {
            autoFit.runnable.run();
        }
        return this;
    }

    //重新绑定数据
    public AutoFit exec() {
        threadPool.execute(new PRunable() {
            @Override
            public void run() {
                autoFit.iActivity.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        execsync();
                    }
                });
            }
        });
        return this;
    }

    //*************************获取值并验证*****************************

    /**
     * 判断值是否合法
     *
     * @param resid      控件id
     * @param validation 判断对象
     * @param name       错误名称
     * @param params     参数
     * @return
     */
    public Object V(int resid, ValidationBase validation, String name, Object... params) {
        Object obj = autoFit.GV(resid);
        Verify verify = validation.validation(obj, params);
        if (verify.error) {
            return autoFit.GV(resid);
        } else {
            hasError = true;
            lastson = new Son(1, "");
            this.toast(TextUtils.isEmpty(name) ? verify.errormsg : name);
        }
        return null;
    }


    /**
     * 获取控件的值
     *
     * @param resid
     * @return
     */
    public Object V(int resid) {
        return autoFit.GV(resid);
    }


    /**
     * 获取参数值
     *
     * @param key
     * @return
     */
    public <T> T V(String key) {
        return autoFit.getParams(key);
    }

    public <T> T V(String key, Object def) {
        return autoFit.getParams(key, def);
    }


    //*************************获取值并验证*****************************

    /**
     * 创建新线程
     *
     * @return
     */
    public AutoFit sync() {
        return new AutoFit(autoFit);
    }


    //*************************关闭窗口*****************************
    public AutoFit closesync() {
        autoFit.close();
        return this;
    }


    public boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        return TextUtils.isEmpty((String) obj);
    }

    public AutoFit close() {
        threadPool.execute(new PRunable() {
            @Override
            public void run() {
                autoFit.iActivity.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        closesync();
                    }
                });
            }
        });
        return this;
    }


    public AutoFit hideSoftKeyboard(final int resid) {
        autoFit.hideSoftKeyboard(findView(resid));
        return this;
    }

    public AutoFit hideSoftKeyboard(View view) {
        autoFit.hideSoftKeyboard(view);
        return this;
    }

    /**
     * 关闭fragment
     *
     * @param resid
     */
    public AutoFit close(final int resid) {
        threadPool.execute(new PRunable() {
            @Override
            public void run() {
                autoFit.iActivity.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        autoFit.send(null, resid, 0, 0);
                    }
                });
            }
        });
        return this;
    }


    //************************从adapter中移除**********************************************

    public AutoFit delsync() {
        autoFit.delete();
        return this;
    }

    public AutoFit del() {
        threadPool.execute(new PRunable() {
            @Override
            public void run() {
                autoFit.iActivity.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        delsync();
                    }
                });
            }
        });
        return this;
    }


    //************************************设置值************************************************

    /**
     * 设置控件的属性
     *
     * @param resid
     * @param value
     * @param hint
     * @param checked
     * @return
     */
    public AutoFit setValue(final int resid, final Object value, final Object hint, final boolean checked) {
        setValueSync(resid, value, hint, checked);
        return this;
    }


    public AutoFit setValueSync(final int resid, final Object value, final Object hint, final boolean checked) {
        autoFit.setValue(resid, value, hint, checked);
        return this;
    }


    public AutoFit setValue(final int resid, final Object value) {
        setValueSync(resid, value);
        return this;
    }


    public AutoFit setValueSync(final int resid, final Object value) {
        autoFit.setValue(resid, value, null, null);
        return this;
    }

    public AutoFit hide(final int resid) {
        threadPool.execute(new PRunable() {
            @Override
            public void run() {
                autoFit.iActivity.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        autoFit.hide(resid);
                    }
                });
            }
        });
        return this;
    }

    public AutoFit show(final int resid) {
        threadPool.execute(new PRunable() {
            @Override
            public void run() {
                autoFit.iActivity.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        autoFit.show(resid);
                    }
                });
            }
        });
        return this;
    }

    public AutoFit addValue(final int resid, final Object value) {
        threadPool.execute(new PRunable() {
            @Override
            public void run() {
                autoFit.iActivity.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        addValueSync(resid, value);
                    }
                });
            }
        });
        return this;
    }

    public AutoFit addValueSync(int resid, Object value) {
        autoFit.addText(resid, value);
        return this;
    }

    public AutoFit delValue(final int resid, final int size) {
        threadPool.execute(new PRunable() {
            @Override
            public void run() {
                autoFit.iActivity.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        delValueSync(resid, size);
                    }
                });
            }
        });
        return this;
    }

    public AutoFit delValueSync(int resid, int size) {
        autoFit.delValue(resid,size);
        return this;
    }

    //********************************************************打开新页面--------------------------------
    public AutoFit startFrg(Class fragment, Object activity, int flag, Object... params) {
        startActivity(flag, fragment, activity, params);
        return this;
    }

    public AutoFit startFrg(Class fragment, Object activity, Object... params) {
        startActivity(0, fragment, activity, params);
        return this;
    }

    public AutoFit startFrg(Fragment fragment, Object activity, int flag, Object... params) {
        startActivity(flag, fragment, activity, params);
        return this;
    }

    public AutoFit startFrg(Fragment fragment, Object activity, Object... params) {
        startActivity(0, fragment, activity, params);
        return this;
    }

    public AutoFit startFrg(int resid, Object activity, Object... params) {
        startActivity(resid, activity, params);
        return this;
    }

    public AutoFit startFrg(int resid, Object activity, int flag, Object... params) {
        startActivity(flag, resid, activity, params);
        return this;
    }

    public AutoFit startAct(Object activity, Object... params) {
        startActivity("", activity, params);
        return this;
    }

    public AutoFit startAct(Object activity, int flag, Object... params) {
        startActivity(flag, "", activity, params);
        return this;
    }

    public AutoFit startActivity(final int flag, final Object fragment, final Object activity, final Object[] params) {
        final Class frg;
        final Class act;
        if (fragment instanceof Class) {
            frg = (Class) fragment;
        } else if (fragment != null) {
            frg = fragment.getClass();
        } else {
            frg = Object.class;
        }
        if (activity instanceof Class) {
            act = (Class) activity;
        } else if (activity instanceof String) {
            act = getActclass((String) activity);
        } else {
            act = activity.getClass();
        }
        threadPool.execute(new PRunable() {
            @Override
            public void run() {
                autoFit.iActivity.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        Helper.startActivity(autoFit.iActivity.getContext(), flag, frg, act, params);
                    }
                });
            }
        });
        return this;
    }


    public AutoFit startActivity(final Object fragment, final Object activity, final Object[] params) {
        startActivity(0, fragment, activity, params);
        return this;
    }


    public AutoFit startActivity(final Object fragment, final Object activity) {
        startActivity(0, fragment, activity, null);
        return this;
    }


    public AutoFit startActivity(final int resId, final Object activity, final Object[] params) {
        startActivity(0, resId, activity, params);
        return this;
    }


    public AutoFit startActivity(final int flag, final int resId, final Object activity, final Object[] params) {
        final Class act;
        if (activity instanceof Class) {
            act = (Class) activity;
        } else if (activity instanceof String) {
            act = getActclass((String) activity);
        } else {
            act = activity.getClass();
        }
        threadPool.execute(new PRunable() {
            @Override
            public void run() {
                autoFit.iActivity.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        AutoFitUtil.startActivity(autoFit.iActivity.getContext(), flag, AutoFitFragment.class, act, resId, params);
                    }
                });
            }
        });
        return this;
    }

    public AutoFit startActivity(final int resid, final Object[] params) {
        threadPool.execute(new PRunable() {
            @Override
            public void run() {
                autoFit.iActivity.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        AutoFitUtil.startActivity(autoFit.iActivity.getContext(), 0, AutoFitFragment.class, NoTitleAct.class, resid, params);
                    }
                });
            }
        });
        return this;
    }


    //**********************************************获取控件*********************************
    public View findView(int resid) {
        return autoFit.iActivity.findViewById(resid);
    }


    //***************************************设置参数*******************************************

    /**
     * 设置参数
     *
     * @param resid
     * @param params
     */
    public AutoFit setParams(final int resid, final Object... params) {
        threadPool.execute(new PRunable() {
            @Override
            public void run() {
                autoFit.iActivity.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        autoFit.send(null, resid, 0, 1, params);
                    }
                });
            }
        });
        return this;
    }

    public AutoFit reload() {
        this.execsync();
        return this;
    }

    public AutoFit reload(final int resid, final int recycleviewid, final Object... params) {
        threadPool.execute(new PRunable() {
            @Override
            public void run() {
                autoFit.iActivity.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        autoFit.send(null, resid, recycleviewid, 20, params);
                    }
                });
            }
        });
        return this;
    }

    public AutoFit pulload(final int resid, final int recycleviewid, final Object... params) {

        threadPool.execute(new PRunable() {
            @Override
            public void run() {
                autoFit.iActivity.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        autoFit.send(null, resid, recycleviewid, 21, params);
                    }
                });
            }
        });
        return this;
    }


    /**/
    public AutoFit loadApi(final int resid, final Object... params) {
        threadPool.execute(new PRunable() {
            @Override
            public void run() {
                autoFit.iActivity.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        autoFit.send(null, resid, 0, 30, params);
                    }
                });
            }
        });

        return this;
    }

    /**
     * 关闭fragment
     *
     * @param resid
     */
    public AutoFit close(final String handleid, final int resid) {

        threadPool.execute(new PRunable() {
            @Override
            public void run() {
                autoFit.iActivity.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        autoFit.send(handleid, resid, 0, 0);
                    }
                });
            }
        });

        return this;
    }

    /**
     * 设置参数
     *
     * @param resid
     * @param params
     */
    public AutoFit setParams(final String handleid, final int resid, final Object... params) {
        threadPool.execute(new PRunable() {
            @Override
            public void run() {
                autoFit.iActivity.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        autoFit.send(handleid, resid, 0, 1, params);
                    }
                });
            }
        });

        return this;
    }

    public AutoFit reload(final String handleid, final int resid, final int recycleviewid, final Object... params) {

        threadPool.execute(new PRunable() {
            @Override
            public void run() {
                autoFit.iActivity.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        autoFit.send(handleid, resid, 0, 20, params);
                    }
                });
            }
        });
        return this;
    }

    public AutoFit pulload(final String handleid, final int resid, final int recycleviewid, final Object... params) {

        threadPool.execute(new PRunable() {
            @Override
            public void run() {
                autoFit.iActivity.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        autoFit.send(handleid, resid, 0, 21, params);
                    }
                });
            }
        });
        return this;
    }


    /**/
    public AutoFit loadApi(final String handleid, final int resid, final Object... params) {

        threadPool.execute(new PRunable() {
            @Override
            public void run() {
                autoFit.iActivity.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        autoFit.send(handleid, resid, 0, 30, params);
                    }
                });
            }
        });
        return this;
    }

    public AutoFit send(final String postid, final int type, final Object... params) {

        threadPool.execute(new PRunable() {
            @Override
            public void run() {
                autoFit.iActivity.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        autoFit.sent(postid, type, params);
                    }
                });
            }
        });
        return this;
    }


    /**
     * 合并两个list 不重复对象
     *
     * @param list1
     * @param list2
     * @return
     */
    public List merge(List list1, List list2) {
        List list = new ArrayList();
        for (Object o : list1) {
            if (!list.contains(o)) {
                list.add(o);
            }
        }
        for (Object o : list1) {
            if (!list.contains(o)) {
                list.add(o);
            }
        }
        return list;
    }

    /**
     * 合并两个list
     *
     * @param list1
     * @param list2
     * @return
     */
    public List megeall(List list1, List list2) {
        List list = new ArrayList();
        for (Object o : list1) {
            list.add(o);
        }
        for (Object o : list1) {
            list.add(o);
        }
        return list;
    }


    /**
     * 把参数设置为数组
     *
     * @param params
     * @return
     */
    public Object[] CV(Object... params) {
        return params;
    }

    public AutoFit run(final Object object, final String method, final Object... params) {
        return RUN(object, method, params);
    }

    public AutoFit RUN(final Object object, final String method, final Object... params) {
        threadPool.execute(new PRunable() {
            @Override
            public void run() {
                Handler mainThread = new Handler(Looper.getMainLooper());
                mainThread.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Class[] clases = new Class[params.length];
                            for (int i = 0; i < params.length; i++) {
                                Object obj = params[i];
                                clases[i] = obj.getClass();
                            }
                            Method me = object.getClass().getMethod(method, clases);
                            me.invoke(object, params);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        return this;
    }

    public AutoFit pop(int viewid, int resid) {
        PopShowParme psp = new PopShowParme();
        psp.type = PopShowParme.DROPDOWN_AUTO;
        psp.view = findView(viewid);
        pop(psp, resid);
        return this;
    }

    public AutoFit pop(View v, int resid) {
        PopShowParme psp = new PopShowParme();
        psp.type = PopShowParme.DROPDOWN_AUTO;
        psp.view = v;
        pop(psp, resid);
        return this;
    }

    public AutoFit pop(View v, int xoff, int yoff, int resid) {
        PopShowParme psp = new PopShowParme();
        psp.type = PopShowParme.DROPDOWN_ASVIEWOF;
        psp.view = v;
        psp.xoff = xoff;
        psp.yoff = yoff;
        pop(psp, resid);
        return this;
    }

    public AutoFit pop(View v, int xoff, int yoff, int gravty, int resid) {
        PopShowParme psp = new PopShowParme();
        psp.type = PopShowParme.DROPDOWN_ASVIEWOFF;
        psp.view = v;
        psp.xoff = xoff;
        psp.yoff = yoff;
        psp.gravty = gravty;
        pop(psp, resid);
        return this;
    }


    public AutoFit pop(MPopDefault pop, View v, int resid) {
        PopShowParme psp = new PopShowParme();
        psp.type = PopShowParme.DROPDOWN_AUTO;
        psp.view = v;
        pop(pop, psp, resid);
        return this;
    }

    public AutoFit pop(MPopDefault pop, View v, int xoff, int yoff, int resid) {
        PopShowParme psp = new PopShowParme();
        psp.type = PopShowParme.DROPDOWN_ASVIEWOF;
        psp.view = v;
        psp.xoff = xoff;
        psp.yoff = yoff;
        pop(pop, psp, resid);
        return this;
    }

    public AutoFit pop(MPopDefault pop, View v, int xoff, int yoff, int gravty, int resid) {
        PopShowParme psp = new PopShowParme();
        psp.type = PopShowParme.DROPDOWN_ASVIEWOFF;
        psp.view = v;
        psp.xoff = xoff;
        psp.yoff = yoff;
        psp.gravty = gravty;
        pop(pop, psp, resid);
        return this;
    }


    public AutoFit listSet(int recyclerviewid, ApiUpdate api, int resid) {
        return listSet(recyclerviewid, api, resid, true, true);
    }

    public AutoFit listSet(int recyclerviewid, ApiUpdate api, int resid, boolean isstart) {
        return listSet(recyclerviewid, api, resid, true, isstart);
    }

    public AutoFit listSet(int recyclerviewid, ApiUpdate api, int resid, boolean needpull, boolean isstart) {
        autoFit.listSet(recyclerviewid, api, resid, null, needpull, isstart);
        return this;
    }

    public AutoFit listSet(int recyclerviewid, ApiUpdate api, int resid, HashMap<Class, AutoDataFormat.CardParam> resids, boolean needpull, boolean isstart) {
        autoFit.listSet(recyclerviewid, api, resid, resids, needpull, isstart);
        return this;
    }


    public AutoFit listSet(int recyclerviewid, ApiUpdate api, DataFormat dataFormat) {
        return listSet(recyclerviewid, api, dataFormat, true, true);
    }

    public AutoFit listSet(int recyclerviewid, ApiUpdate api, DataFormat dataFormat, boolean isstart) {
        return listSet(recyclerviewid, api, dataFormat, true, isstart);
    }


    public AutoFit listSet(int recyclerviewid, ApiUpdate api, DataFormat dataFormat, boolean needpull, boolean isstart) {
        autoFit.listSet(recyclerviewid, api, dataFormat, needpull, isstart);
        return this;
    }

    public AutoFit save(ApiUpdate[] apis, String[] names) {
        return bind(apis, names, false, true, true);
    }

    public AutoFit save(ApiUpdate api, String name) {
        return bind(api, name, false, true, true);
    }

    public AutoFit save(ApiUpdate[] apis, String[] names, boolean isstart) {
        return bind(apis, names, false, isstart, true);
    }

    public AutoFit save(ApiUpdate api, String name, boolean isstart) {
        return bind(api, name, false, isstart, true);
    }


    public AutoFit save(ApiUpdate[] apis, String[] names, boolean bool, boolean isstart) {
        return bind(apis, names, bool, isstart, true);
    }

    public AutoFit save(ApiUpdate api, String name, boolean bool, boolean isstart) {
        return bind(api, name, bool, isstart, true);
    }

    public AutoFit bind(ApiUpdate[] apis, String[] names) {
        return bind(apis, names, false, true, false);
    }

    public AutoFit bind(ApiUpdate api, String name) {
        return bind(api, name, false, true, false);
    }

    public AutoFit bind(ApiUpdate[] apis, String[] names, boolean isstart) {
        return bind(apis, names, false, isstart, false);
    }

    public AutoFit bind(ApiUpdate api, String name, boolean isstart) {
        return bind(api, name, false, isstart, false);
    }


    public AutoFit bind(ApiUpdate[] apis, String[] names, boolean bool, boolean isstart) {
        return bind(apis, names, bool, isstart, false);
    }

    public AutoFit bind(ApiUpdate api, String name, boolean bool, boolean isstart) {
        return bind(api, name, bool, isstart, false);
    }

    public AutoFit bind(String name, Object object) {
        autoFit.bind(name, object);
        return this;
    }


    private AutoFit bind(final ApiUpdate api, final String name, final boolean bool, final boolean isstart, final boolean issave) {
        threadPool.execute(new PRunable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
                if (lastson == null || lastson.getError() == 0) {
                    lastson = autoFit.apiAndBind(api, name, bool, isstart, issave);
                }
            }
        });
        return this;
    }


    /**
     * 判断是否有错误，如果有错误则停止执行
     *
     * @return
     */
    public AutoFit CKERR() {
        boolean iserror = false;
        if (hasError) {
            Log.d("test", "" + 350 + threadPool.getWatrun().size());
            iserror = true;
            threadPool.setDoable(true);
        }
        if (lastson != null && lastson.getError() != 0) {
            iserror = true;
        }
        threadPool.execute(new PRunable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
                if (lastson != null && lastson.getError() != 0) {
                    Log.d("test", "" + 350 + threadPool.getWatrun().size());
                    hasError = true;
                }
                if (hasError) {
                    lastson = null;
                    hasError = false;
                    threadPool.clear();
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
                Log.d("test", "" + 352 + threadPool.getWatrun().size());
            }
        });
        if (iserror) {
            return new RAutoFit(this, 9, lastson != null ? lastson.getMsg() : "");   //熔断
        } else {
            return this;
        }
    }

    public AutoFit CKERR(boolean hasError) {
        boolean iserror = false;
        if (hasError) {
            Log.d("test", "" + 350 + threadPool.getWatrun().size());
            iserror = true;
            threadPool.setDoable(true);
        }
        if (lastson != null && lastson.getError() != 0) {
            iserror = true;
        }
        if (iserror) {
            return new RAutoFit(this, 9, lastson != null ? lastson.getMsg() : "");   //熔断
        } else {
            return this;
        }
    }

    public AutoFit check(Object obj, boolean bool) {
        if (!hasError) {
            hasError = !bool;
        }
        threadPool.setDoable(!hasError);
        return this;
    }

    /**
     * 调用接口并且实现数据绑定
     *
     * @param apis  接口实现
     * @param names 绑定数据名称
     * @param bool  相同请求是否请求接口 true 请求， fals不请求
     * @return
     */

    private AutoFit bind(final ApiUpdate[] apis, final String[] names, final boolean bool, final boolean isstart, final boolean issave) {
        threadPool.execute(new PRunable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
                if (lastson == null || lastson.getError() == 0) {
                    lastson = autoFit.apiAndBind(apis, names, bool, isstart, issave);
                }
            }
        });
        return this;
    }


    public AutoFit pop(final MPopDefault autoFitPop, final PopShowParme sp, final int resid) {
        threadPool.execute(new PRunable() {
            @Override
            public void run() {
                autoFit.iActivity.getHandler().post(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void run() {
                        autoFit.openpop(autoFitPop, sp, resid);
                    }
                });
            }
        });
        return this;
    }

    public AutoFit pop(final PopShowParme sp, final int resid) {
        threadPool.execute(new PRunable() {
            @Override
            public void run() {
                autoFit.iActivity.getHandler().post(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void run() {
                        autoFit.openpop(sp, resid);
                    }
                });
            }
        });
        return this;
    }

    public AutoFit dialog(final int resid) {
        threadPool.execute(new PRunable() {
            @Override
            public void run() {
                autoFit.iActivity.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        autoFit.openDialog(resid);
                    }
                });
            }
        });
        return this;
    }

    public AutoFit dialog(final MDialog dialog) {
        threadPool.execute(new PRunable() {
            @Override
            public void run() {
                autoFit.iActivity.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        autoFit.openDialog(dialog);
                    }
                });
            }
        });
        return this;
    }

    public AutoFit dialog(String title, final String bindname, final String bindvalue) {
        threadPool.execute(new PRunable() {
            @Override
            public void run() {
                autoFit.iActivity.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        new AlertDialog.Builder(getContext())
                                .setTitle("提示")
                                .setMessage(title)
                                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        put(bindname, bindvalue);
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).create().show();
                    }
                });
            }
        });
        return this;
    }

    /**
     * 设置变量值
     *
     * @param key
     * @param value
     * @return
     */
    public AutoFit put(final String key, final Object value) {
        putsync(key, value);
        return this;
    }

    /**
     * 设置变量值
     *
     * @param key
     * @param value
     * @return
     */
    public AutoFit putsync(String key, Object value) {
        this.autoFit.setV(key, value);
        return this;
    }


    /**
     * 弹错错误信息
     *
     * @param text
     * @return
     */
    public AutoFit toastsync(final String text) {
        autoFit.toast(text);
        return this;
    }

    public AutoFit toast(final String text) {
        threadPool.execute(new PRunable() {
            @Override
            public void run() {
                autoFit.iActivity.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        autoFit.toast(text);
                    }
                });
            }
        });
        return this;
    }


    /**
     * 显示控件
     *
     * @param resid
     * @param vis
     * @return
     */
    public AutoFit visable(int resid, int vis) {
        setvisable(resid, vis);
        return this;
    }

    public AutoFit visablesync(int resid, int vis) {
        autoFit.setvisable(resid, vis);
        return this;
    }


    private AutoFit setvisable(final int resid, final int vis) {
        threadPool.execute(new PRunable() {
            @Override
            public void run() {
                autoFit.iActivity.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        autoFit.setvisable(resid, vis);
                    }
                });
            }
        });
        return this;
    }

    public AutoFit setVisable(int resid, boolean isshow) {
        return setvisable(resid, isshow ? View.VISIBLE : View.GONE);
    }


    public AutoFit setAdasync(int resid, int itemresid, Object object) {
        autoFit.setAdapter(resid, itemresid, object);
        return this;
    }

    public AutoFit setAdasync(int resid, CardAdapter adapter) {
        autoFit.setAdapter(resid, adapter);
        return this;
    }


    public AutoFit setAda(final int resid, final int itemresid, final Object object) {
        threadPool.execute(new PRunable() {
            @Override
            public void run() {
                autoFit.iActivity.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        autoFit.setAdapter(resid, itemresid, object);
                    }
                });
            }
        });
        return this;
    }

    public AutoFit setAda(final int resid, final CardAdapter adapter) {
        threadPool.execute(new PRunable() {
            @Override
            public void run() {
                autoFit.iActivity.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        autoFit.setAdapter(resid, adapter);
                    }
                });
            }
        });
        return this;
    }

    public AutoFit setAda(final View view, final int itemresid, final Object object) {
        threadPool.execute(new PRunable() {
            @Override
            public void run() {
                autoFit.iActivity.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        autoFit.setAdapter(view, itemresid, object);
                    }
                });
            }
        });
        return this;
    }

    public AutoFit setAda(final View view, final CardAdapter adapter) {
        threadPool.execute(new PRunable() {
            @Override
            public void run() {
                autoFit.iActivity.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        autoFit.setAdapter(view, adapter);
                    }
                });
            }
        });
        return this;
    }

    public AutoFit setCurr(final int resid, final Object object) {
        threadPool.execute(new PRunable() {
            @Override
            public void run() {
                autoFit.iActivity.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        autoFit.setCurr(resid, object);
                    }
                });
            }
        });
        return this;
    }


    public AutoFit setCurrSync(final int resid, final Object object) {
        autoFit.setCurr(resid, object);
        return this;
    }

    public AutoFit parent() {
        return autoFit.getParent(this);
    }

    public AutoFit child() {
        if (nowFit != null) {
            return nowFit;
        }
        return this;
    }

    public boolean equal(Object obj1, Object obj2) {
        if (obj1 == obj2) {
            return true;
        }
        if (obj1 != null) {
            return obj1.equals(obj2);
        } else {
            return false;
        }
    }


    /**
     * getphoto
     */

    public AutoFit getPhoto(final String bindname, final Integer aspectX, final Integer aspectY, final Integer outputX, final Integer outputY) {
        autoFit.getPhoto(bindname, aspectX, aspectY, outputX, outputY);
        return this;
    }

    public AutoFit getPhotos(final String bindname, final Integer size) {
        autoFit.getPhotos(bindname, size);
        return this;
    }

    public AutoFit getPhoto(final String bindname) {
        autoFit.getPhoto(bindname);
        return this;
    }

    public AutoFit showPhoto(List<String> photopaths) {
        autoFit.showPhoto(null, photopaths);
        return this;
    }

    public AutoFit showPhoto(String path, String... photopaths) {
        autoFit.showPhoto(path, photopaths);
        return this;
    }

    public AutoFit showPhoto(String path, List<String> photopaths) {
        autoFit.showPhoto(path, photopaths);
        return this;
    }

}
