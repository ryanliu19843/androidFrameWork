package com.mdx.framework.autofit;

import androidx.databinding.ViewDataBinding;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.mdx.framework.Frame;
import com.mdx.framework.activity.IActivity;
import com.mdx.framework.activity.MPopDefault;
import com.mdx.framework.autofit.commons.AutoParams;
import com.mdx.framework.broadcast.BIntent;
import com.mdx.framework.broadcast.OnReceiverListener;
import com.mdx.framework.dialog.PhotoShow;
import com.mdx.framework.widget.getphoto.PopUpdataPhoto;
import com.mdx.framework.widget.pagerecycleview.ada.Card;
import com.mdx.framework.autofit.commons.FitPost;
import com.mdx.framework.autofit.commons.PopShowParme;
import com.mdx.framework.commons.ParamsManager;
import com.mdx.framework.log.MLog;
import com.mdx.framework.prompt.MDialog;
import com.mdx.framework.server.api.ApiUpdate;
import com.mdx.framework.server.api.Son;
import com.mdx.framework.utility.Helper;
import com.mdx.framework.widget.banner.CirleCurr;
import com.mdx.framework.widget.pagerecycleview.IRecyclerView;
import com.mdx.framework.widget.pagerecycleview.MFRecyclerView;
import com.mdx.framework.widget.pagerecycleview.ada.CardAdapter;
import com.mdx.framework.widget.pagerecycleview.ada.MAdapter;
import com.mdx.framework.widget.pagerecycleview.ada.MPagerAdapter;
import com.mdx.framework.widget.pagerecycleview.autofit.AutoDataFormat;
import com.mdx.framework.widget.pagerecycleview.widget.OnPageSwipListener;
import com.mdx.framework.widget.util.DataFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mdx.framework.autofit.AutoFitUtil.FITNAME;

/**
 * Created by ryan on 2017/11/1.
 */

public class AutoFitBase implements ParamsManager.PutHas {
    public ViewDataBinding bindingobj;
    public AutoParams params = new AutoParams();
    public HashMap<String, Long> sonObjs = new HashMap<>();
    public ApiUpdate listapiup;
    public int itemresid;
    public AutoFitBase parentfit;
    public boolean isfirstPull = true;
    public DataFormat dataFormat;
    public boolean isfirstload = false, isfirstsave = false;
    public IActivity iActivity;
    public Card card;
    public AutoFit fit;
    public ApiUpdate.OnApiLoadListener apiLoadListener = new ApiUpdate.OnApiLoadListener() {
        @Override
        public void onLoaded(Son son) {
            apiload(son);
        }
    };
    public ApiUpdate.OnApiLoadListener saveLoadListener = new ApiUpdate.OnApiLoadListener() {
        @Override
        public void onLoaded(Son son) {
            savload(son);
        }
    };

    /**
     * 调用接口并且实现数据绑定
     *
     * @param api  接口实现
     * @param name 绑定数据名称
     * @param bool 相同请求是否请求接口 true 请求， fals不请求
     * @return
     */
    public Son apiAndBind(ApiUpdate api, String name, boolean bool, boolean isstart, boolean issave) {
        if (!isstart) {
            return null;
        }
        api.putSonParams("setparam", name);
        Log.d("test", "apiAndBind" + 250);
        if (!api.getIsSetShowLoading()) {
            api.setShowLoading(true);
        }
        api.setAutofit(this.params);
        if (!this.sonObjs.containsKey(api.getMd5str()) || System.currentTimeMillis() - this.sonObjs.get(api.getMd5str()) > 3000 || bool) {
            if (issave) {
                return api.LoadSync(this.iActivity.getContext(), saveLoadListener);
            } else {
                return api.LoadSync(this.iActivity.getContext(), apiLoadListener);
            }
        }
        return null;
    }


    /**
     * 调用接口并且实现数据绑定
     *
     * @param apis  接口实现
     * @param names 绑定数据名称
     * @param bool  相同请求是否请求接口 true 请求， fals不请求
     * @return
     */

    public Son apiAndBind(ApiUpdate[] apis, String[] names, boolean bool, boolean isstart, boolean issave) {
        if (!isstart) {
            return null;
        }
        ApiUpdate apiu = null;
        List<ApiUpdate> apius = new ArrayList<>();

        for (int i = 0; i < apis.length; i++) {
            ApiUpdate api = apis[i];
            if (!api.getIsSetShowLoading()) {
                api.setShowLoading(true);
            }
            api.putSonParams("setparam", names[i]);
            api.setAutofit(this.params);
            if (!this.sonObjs.containsKey(api.getMd5str()) || bool) {
                if (apiu == null) {
                    apiu = api;
                } else {
                    apius.add(api);
                }
            }
        }
        apiu.addApiUpdates(apis);
        if (issave) {
            return apiu.LoadSync(this.iActivity.getContext(), saveLoadListener);
        } else {
            return apiu.LoadSync(this.iActivity.getContext(), apiLoadListener);
        }
    }


    /**
     * 设置列表数据
     *
     * @param api   列表的api
     * @param resid 列表的显示的layoutid
     * @return
     */
    public void listSet(int recyclerViewid, ApiUpdate api, int resid, HashMap<Class, AutoDataFormat.CardParam> resids, boolean needpull, boolean isstart) {
        if (!isstart) {
            return;
        }
        IRecyclerView recyclerView = (IRecyclerView) iActivity.findViewById(recyclerViewid);
        if (this.listapiup != null && this.listapiup.getMd5str().equals(api.getMd5str()) && resid == this.itemresid) {
            return;
        }
        this.itemresid = resid;
        this.listapiup = api;
        if (recyclerView != null) {
            recyclerView.setOnSwipLoadListener(new OnPageSwipListener(this.iActivity.getContext(), api, new AutoDataFormat(resid, this, resids)));
            if (needpull) {
                if (this.isfirstPull) {
                    recyclerView.pullloadelay();
                    this.isfirstPull = false;
                } else {
                    recyclerView.pullLoad();
                }
            }
        }
    }

    /**
     * 设置列表数据
     *
     * @param api        列表的api
     * @param dataFormat 自定义的数据解析类
     * @return
     */
    public void listSet(int recyclerViewid, ApiUpdate api, DataFormat dataFormat, boolean needpull, boolean isstart) {
        if (!isstart) {
            return;
        }
        if (this.listapiup != null && this.listapiup.getMd5str().equals(api.getMd5str()) && this.dataFormat != null && this.dataFormat.getClass().getName().equals(dataFormat.getClass().getName())) {
            return;
        }
        IRecyclerView recyclerView = (IRecyclerView) iActivity.findViewById(recyclerViewid);
        this.dataFormat = dataFormat;
        this.listapiup = api;
        if (recyclerView != null) {
            recyclerView.setOnSwipLoadListener(new OnPageSwipListener(this.iActivity.getContext(), api, dataFormat));
            if (needpull) {
                if (isfirstPull) {
                    recyclerView.pullloadelay();
                    this.isfirstPull = false;
                } else {
                    recyclerView.pullLoad();
                }
            }
        }
    }


    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (bindingobj != null) {
                try {
                    AutoFitUtil.setBind(bindingobj, "P", params);
                    AutoFitUtil.setBind(bindingobj, "F", fit);
                } catch (Exception e) {
                    MLog.D(e);
                }
                bindingobj.executePendingBindings();
                bindingobj.notifyChange();
            }
        }
    };


    public AutoFitBase(IActivity iActivity) {
        this.iActivity = iActivity;
    }


    /**
     * 通知调用
     *
     * @param type
     * @param fitPost
     */
    public void dispost(int type, FitPost fitPost) {
        for (String key : fitPost.params.keySet()) {
            put(key, fitPost.params.get(key));
        }
        try {
            AutoFitUtil.setBind(bindingobj, "P", params);
            AutoFitUtil.setBind(bindingobj, "F", fit);
        } catch (Exception e) {
            MLog.D(e);
        }
        bindingobj.executePendingBindings();
        bindingobj.notifyChange();

        IRecyclerView recyclerView = null;
        if (type == 20) {
            recyclerView = (IRecyclerView) iActivity.findViewById(fitPost.recycleviewid);
            if (recyclerView != null) {
                recyclerView.reload();
            }
        } else if (type == 21) {
            recyclerView = (IRecyclerView) iActivity.findViewById(fitPost.recycleviewid);
            if (recyclerView != null) {
                recyclerView.pullLoad();
            }
        } else if (type == 30) {
            fitPost.apiUpdate.Load(iActivity.getContext(), this, "apiload");
        } else if (type == 0) {
            iActivity.finish();
        }
    }

    public Object GV(int resid) {
        View v = iActivity.findViewById(resid);
        if (v instanceof CompoundButton) {
            return ((CompoundButton) v).isChecked();
        } else if (v instanceof RadioGroup) {
            return ((RadioGroup) v).getCheckedRadioButtonId();
        } else if (v instanceof Spinner) {
            Spinner sp = (Spinner) v;
            return sp.getSelectedItem();
        } else if (v instanceof TextView) {
            return ((TextView) v).getText().toString();
        }
        return null;
    }

    public void delValue(int resid, int size) {
        View v = iActivity.findViewById(resid);

        if (v instanceof EditText) {
            int ind = ((EditText) v).getSelectionStart();
            Editable editable = ((EditText) v).getText();
            editable.delete((ind - size) < 0 ? 0 : (ind - size), ind);
        } else {
            Object obj = GV(resid);
            int sel = -1;
            if (obj != null) {
                String val = obj.toString();

                int ind = 0;
                if (val.length() > size) {
                    ind = val.length() - size;
                } else {
                    ind = 0;
                }
                val = val.substring(0, ind);
                setValue(resid, val);
            }
        }
    }

    /**
     * 处理接口方法
     *
     * @param son
     */
    public void apiload(Son son) {
        boolean bol = false;
        try {
            bol = bindSon(son) || bol;
            for (Son s : son.sons) {
                bol = bindSon(s) || bol;
            }
            if (bol) {
                bindingobj.executePendingBindings();
            }
        } catch (Exception e) {
            Helper.toaste(e, iActivity.getContext());
        }
    }


    /**
     * 绑定数据到指定的类
     *
     * @param son 接口返回的数据
     * @return
     * @throws Exception
     */
    public boolean bindSon(Son son) throws Exception {
        if (!son.sonMd5.equals(sonObjs.get(son.Md5str))) {
            String key = son.getSonParam("setparam").toString();
            //ParamsManager.putValue(key, son.getBuild(), this);
            setV(key, son.getBuild());
            AutoFitUtil.setBind(bindingobj, key, son.getBuild());
            sonObjs.put(son.Md5str, System.currentTimeMillis());
            return true;
        }
        return false;
    }


    /**
     * 绑定数据到指定的类
     *
     * @param son 接口返回的数据
     * @return
     * @throws Exception
     */
    public boolean saveSon(Son son) throws Exception {
        if (!son.sonMd5.equals(sonObjs.get(son.Md5str))) {
            String key = (String) son.getSonParam("setparam");
            String endtype = (String) son.getSonParam("endtype");
//            ParamsManager.putValue(key, son.getBuild(), this);
            setV(key, son.getBuild());
            if (endtype != null && endtype.equals("99")) {
                iActivity.finish();
            } else {
                if (!TextUtils.isEmpty(key)) {
                    put(key, son.getBuild());
                }
            }
            sonObjs.put(son.Md5str, System.currentTimeMillis());
            return true;
        }
        return false;
    }

    /**
     * 保存接口返回
     *
     * @param son
     */
    public void savload(Son son) {
        try {
            saveSon(son);
            for (Son s : son.sons) {
                saveSon(son);
            }
        } catch (Exception e) {
            Helper.toaste(e, iActivity.getContext());
        }
    }

    public void close() {
        this.iActivity.finish();
    }


    /**
     * 设置params
     *
     * @param key
     * @param object
     */
    public void put(String key, Object object) {
        params.put(key, object);
        if (key.startsWith("#")) {
            try {
                AutoFitUtil.setBind(bindingobj, key.substring(1), object);
            } catch (Exception e) {
                Helper.toaste(e, iActivity.getContext());
            }
        }
    }


    public void setV(String key, Object object) {
        ParamsManager.putValue(key, object, this);
    }

    /**
     * 获取params
     *
     * @param key
     * @return
     */
    public <T> T getParams(String key) {
        return (T) ParamsManager.getValue(key, this.params);
    }

    public <T> T getParams(String key, Object def) {
        Object object = getParams(key);
        if (object == null) {
            setV(key, def);
            return (T) def;
        }
        return (T) object;
    }


    public void send(String handid, int resid, int recycleviewid, int type, Object... params) {
        FitPost fitPost = new FitPost();
        if (resid != 0) {
            fitPost.resid = resid;
        }
        fitPost.recycleviewid = recycleviewid;
        for (int ind = 0; ind < params.length; ind++) {
            String key = params[ind].toString();
            if (params.length > ind + 1) {
                Object obj = params[(ind + 1)];
                fitPost.put(key, obj);
            }
            ind++;
        }
        if (handid == null) {
            handid = FITNAME;
        }
        Frame.HANDLES.sentAll(handid, type, fitPost);
    }


    public void sent(String postid, int type, Object... params) {
        if (params == null) {
            Frame.HANDLES.sentAll(postid, type, null);
        } else if (params.length == 1) {
            Frame.HANDLES.sentAll(postid, type, params[0]);
        } else {
            FitPost fitPost = new FitPost();
            for (int ind = 0; ind < params.length; ind++) {
                String key = params[ind].toString();
                if (params.length > ind + 1) {
                    Object obj = params[(ind + 1)];
                    fitPost.put(key, PV(obj));
                }
                ind++;
            }
            Frame.HANDLES.sentAll(postid, type, fitPost);
        }
    }


    public Object PV(Object obj) {
        if (obj instanceof String) {
            String value = (String) obj;
            if (value.startsWith("#") || value.startsWith("_") || value.startsWith("@")) {
                return this.getParams(value);
            } else {
                if (value.startsWith(" ")) {
                    return value.substring(1);
                }
            }
        }
        return obj;
    }

    public void toast(String text) {
        Helper.toast(text, Frame.CONTEXT);
    }


    /*********************************dialog*******************************************/
    public void openDialog(int resid) {
        AutoFitDialog afd = new AutoFitDialog(iActivity.getContext(), resid, params, getTopFit(this));
        afd.show();
    }

    public AutoFitBase getTopFit(AutoFitBase autoFit) {
        if (autoFit.parentfit == null) {
            return autoFit;
        } else {
            return getTopFit(autoFit.parentfit);
        }
    }


    public void setValue(int resid, Object value) {
        View view = iActivity.findViewById(resid);
        if (value != null) {
            if (value instanceof Boolean) {
                setValue(resid, null, null, (Boolean) value);
            } else {
                setValue(resid, value, null, null);
            }
        }
    }

    public void addText(int resid, Object value) {
        View v = iActivity.findViewById(resid);
        if (v instanceof EditText) {
            int ind = ((EditText) v).getSelectionStart();
            Editable editable = ((EditText) v).getText();
            editable.insert(ind, value.toString());
        } else {
            String s;
            Object nv = GV(resid);
            nv = nv == null ? "" : nv;
            if (value != null) {
                s = nv.toString() + value.toString();
            } else {
                s = value.toString();
            }
            setValue(resid, s, null, null);
        }
    }

    public void setHint(int resid, Object hint) {
        View view = iActivity.findViewById(resid);
        if (hint != null) {
            setValue(resid, null, hint, null);
        }
    }

    public void setValue(int viewid, Object value, Object hint, Boolean check) {
        View view = iActivity.findViewById(viewid);
        if (view instanceof TextView) {
            if (value instanceof Boolean && view instanceof CompoundButton) {
                ((CompoundButton) view).setChecked((Boolean) value);
            } else {
                ((TextView) view).setText(getCs(value));
                if (hint != null) {
                    ((TextView) view).setHint(getCs(hint));
                }
                if (check != null && view instanceof CompoundButton) {
                    ((CompoundButton) view).setChecked(check);
                }
            }
        } else if (view instanceof Spinner) {

        }
    }

    public CharSequence getCs(Object value) {
        CharSequence text = null;
        if (value instanceof String) {
            text = value.toString();
        } else if (value instanceof CharSequence) {
            text = (CharSequence) value;
        }
        return text;
    }

    public void openDialog(MDialog dialog) {
        dialog.show();
    }

    /*********************************dialog*******************************************/


    /*********************************打开   popwindows*******************************************/
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void openpop(MPopDefault autoFitPop, PopShowParme sp, int resid) {
        if (sp.type == PopShowParme.DROPDOWN_ASLOCAL) {
            autoFitPop.showAtLocation(sp.view, sp.gravty, sp.x, sp.y);
        } else if (sp.type == PopShowParme.DROPDOWN_ASVIEW) {
            autoFitPop.showAsDropDown(sp.view);
        } else if (sp.type == PopShowParme.DROPDOWN_ASVIEWOF) {
            autoFitPop.showAsDropDown(sp.view, sp.xoff, sp.yoff);
        } else if (sp.type == PopShowParme.DROPDOWN_ASVIEWOFF) {
            autoFitPop.showAsDropDown(sp.view, sp.xoff, sp.yoff, sp.gravty);
        } else {
            autoFitPop.show(sp.view);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void openpop(PopShowParme sp, int resid) {
        AutoFitPop autoFitPop = new AutoFitPop(sp.view.getContext(), resid, params, this);
        openpop(autoFitPop, sp, resid);
    }

    /*********************************pop  windows*******************************************/


    public void setvisable(int resid, int vis) {
        View view = iActivity.findViewById(resid);
        if (view != null) {
            view.setVisibility(vis);
        }
    }

    public void hide(int resid) {
        setvisable(resid, View.GONE);
    }

    public void show(int resid) {
        setvisable(resid, View.VISIBLE);
    }


    public void setAdapter(int resid, int itemresid, Object object) {
        object = PV(object);
        if (object == null) {
            return;
        }
        View view = iActivity.findViewById(resid);
        setAdapter(view, itemresid, object);
    }


    public void setAdapter(View view, int itemresid, Object object) {
        setAdapter(view, itemresid, null, object);
    }

    public void setAdapter(View view, int itemresid, HashMap<Class, AutoDataFormat.CardParam> resids, Object object) {
        CardAdapter cardAdapter;
        if (resids != null) {
            cardAdapter = new AutoDataFormat(itemresid, this, resids).getAda(iActivity.getContext(), object);
        } else {
            cardAdapter = new AutoDataFormat(itemresid, this).getAda(iActivity.getContext(), object);
        }
        if (view instanceof CirleCurr) {
            ((CirleCurr) view).setAdapter(cardAdapter);
        } else if (view instanceof RecyclerView) {
            ((RecyclerView) view).setAdapter(cardAdapter);
        } else if (view instanceof Spinner) {
            ((Spinner) view).setAdapter(cardAdapter.getBaseAdapter());
        } else if (view instanceof MFRecyclerView) {
            ((MFRecyclerView) view).setAdapter(cardAdapter);
        } else if (view instanceof ViewPager) {
            ((ViewPager) view).setAdapter(new MPagerAdapter(cardAdapter));
        }
    }


    public CardAdapter getAdapter(int itemresid, Object object) {
        CardAdapter cardAdapter = new AutoDataFormat(itemresid, this).getAda(iActivity.getContext(), object);
        return cardAdapter;
    }

    public CardAdapter getAdapter(int itemresid, Object object, HashMap<Class, AutoDataFormat.CardParam> resids) {
        CardAdapter cardAdapter = new AutoDataFormat(itemresid, this, resids).getAda(iActivity.getContext(), object);
        return cardAdapter;
    }

    public void setAdapter(int resid, CardAdapter adapter) {
        if (adapter == null) {
            return;
        }
        View view = iActivity.findViewById(resid);
        setAdapter(view, adapter);
    }

    public void setAdapter(View view, CardAdapter adapter) {
        if (view instanceof CirleCurr) {
            ((CirleCurr) view).setAdapter(adapter);
        } else if (view instanceof RecyclerView) {
            ((RecyclerView) view).setAdapter(adapter);
        } else if (view instanceof Spinner) {
            ((Spinner) view).setAdapter(adapter.getBaseAdapter());
        } else if (view instanceof MFRecyclerView) {
            ((MFRecyclerView) view).setAdapter(adapter);
        } else if (view instanceof ViewPager) {
            ((ViewPager) view).setAdapter(new MPagerAdapter(adapter));
        }
    }

    public void setCurr(int resid, Object object) {
        object = PV(object);
        if (object == null) {
            return;
        }
        View view = iActivity.findViewById(resid);
        if (view instanceof CirleCurr) {
            PagerAdapter pagerAdapter = ((CirleCurr) view).getAdapter();
            int curr = pagerAdapter.getItemPosition(object);
            ((CirleCurr) view).setCurr(curr);
        } else if (view instanceof RecyclerView) {
            RecyclerView.Adapter adapter = ((RecyclerView) view).getAdapter();
            if (adapter instanceof MAdapter) {
                int curr = ((MAdapter) adapter).getItemPosition(object);
                ((RecyclerView) view).scrollToPosition(curr);
            }
        } else if (view instanceof Spinner) {
            SpinnerAdapter adapter = ((Spinner) view).getAdapter();
            int curr = 0;
            for (int i = 0; i < adapter.getCount(); i++) {
                if (adapter.getItem(i) == object) {
                    curr = i;
                    break;
                }
            }
            ((Spinner) view).setSelection(curr);
        } else if (view instanceof MFRecyclerView) {
            RecyclerView.Adapter adapter = ((MFRecyclerView) view).getMAdapter();
            if (adapter instanceof MAdapter) {
                int curr = ((MAdapter) adapter).getItemPosition(object);
                ((RecyclerView) view).scrollToPosition(curr);
            }
        } else if (view instanceof ViewPager) {
            PagerAdapter adapter = ((ViewPager) view).getAdapter();
            int curr = adapter.getItemPosition(object);
            ((ViewPager) view).setCurrentItem(curr);
        }
    }

    public AutoFit getParent(AutoFit child) {
        return new AutoFit(parentfit, child);
    }

    public void delete() {
        iActivity.delete();
    }

    public void hideSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) iActivity.getContext().getSystemService(iActivity.getContext().INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void bind(String name, Object object) {
        try {
            AutoFitUtil.setBind(bindingobj, name, object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getPhoto(final String bindname, final Integer aspectX, final Integer aspectY, final Integer outputX, final Integer outputY) {
        Helper.getPhoto(iActivity.getContext(), new PopUpdataPhoto.OnReceiverPhoto() {
            @Override
            public void onReceiverPhoto(String photoPath, int width, int height) {
                put(bindname, photoPath);
            }
        }, aspectX, aspectY, outputX, outputY);
    }

    public void getPhotos(final String bindname, final Integer size) {
        Helper.getPhotos(iActivity.getContext(), new PopUpdataPhoto.OnReceiverPhotos() {
            @Override
            public void onReceiverPhoto(ArrayList<String> photos) {
                put(bindname, photos);
            }
        }, size);
    }

    public void getPhoto(final String bindname) {
        getPhoto(bindname, -1, -1, 0, 0);
    }


    public void showPhoto(String path, String... photopaths) {
        List<String> list = new ArrayList<>();
        if (photopaths == null || photopaths.length == 0) {
            if (!TextUtils.isEmpty(path)) {
                list.add(path);
            }
        } else {
            for (String pp : photopaths) {
                list.add(pp);
            }
        }
        showPhoto(path, list);
    }

    public void showPhoto(String path, List<String> photopaths) {
        PhotoShow photoShow = new PhotoShow(iActivity.getContext(), photopaths, path);
        photoShow.show();
    }
}
