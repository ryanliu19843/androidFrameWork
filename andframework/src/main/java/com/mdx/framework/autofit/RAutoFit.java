package com.mdx.framework.autofit;

import android.content.Context;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.mdx.framework.activity.MPopDefault;
import com.mdx.framework.autofit.commons.PopShowParme;
import com.mdx.framework.prompt.MDialog;
import com.mdx.framework.server.api.ApiUpdate;
import com.mdx.framework.server.api.Son;
import com.mdx.framework.utility.validation.ValidationBase;
import com.mdx.framework.widget.pagerecycleview.ada.CardAdapter;
import com.mdx.framework.widget.pagerecycleview.autofit.AutoDataFormat;
import com.mdx.framework.widget.util.DataFormat;

import java.util.HashMap;
import java.util.List;

public class RAutoFit extends AutoFit {
    public AutoFit autoFit;
    public Son son;
    public int error = 0;
    public String message;

    public RAutoFit(AutoFit autoFit, int error, String message) {
        this.autoFit = autoFit;
        this.error = error;
        this.message = message;
    }

    @Override
    public Context getContext() {
        return autoFit.getContext();
    }

    @Override
    public AutoFit execsync() {
        if (checkerror()) {
            return this;
        }
        return autoFit.execsync();
    }

    @Override
    public AutoFit exec() {
        if (checkerror()) {
            return this;
        }

        return autoFit.exec();
    }

    @Override
    public Object V(int resid, ValidationBase validation, String name, Object... params) {
        return autoFit.V(resid, validation, name, params);
    }

    @Override
    public Object V(int resid) {
        if (checkerror()) {
            return null;
        }
        return autoFit.V(resid);
    }

    @Override
    public <T> T V(String key) {
        if (checkerror()) {
            return null;
        }
        return autoFit.V(key);
    }

    @Override
    public <T> T V(String key, Object def) {
        if (checkerror()) {
            return null;
        }
        return super.V(key, def);
    }

    @Override
    public AutoFit sync() {
        if (checkerror()) {
            return this;
        }
        return autoFit.sync();
    }

    @Override
    public AutoFit closesync() {
        if (checkerror()) {
            return this;
        }
        return autoFit.closesync();
    }

    @Override
    public boolean isEmpty(Object obj) {
        return autoFit.isEmpty(obj);
    }

    @Override
    public AutoFit close() {
        if (checkerror()) {
            return this;
        }
        return autoFit.close();
    }

    @Override
    public AutoFit hideSoftKeyboard(int resid) {
        if (checkerror()) {
            return this;
        }
        return autoFit.hideSoftKeyboard(resid);
    }

    @Override
    public AutoFit hideSoftKeyboard(View view) {
        if (checkerror()) {
            return this;
        }
        return autoFit.hideSoftKeyboard(view);
    }

    @Override
    public AutoFit close(int resid) {
        if (checkerror()) {
            return this;
        }
        return autoFit.close(resid);
    }

    @Override
    public AutoFit delsync() {
        if (checkerror()) {
            return this;
        }
        return autoFit.delsync();
    }

    @Override
    public AutoFit del() {
        if (checkerror()) {
            return this;
        }
        return autoFit.del();
    }

    @Override
    public AutoFit setValue(int resid, Object value, Object hint, boolean checked) {
        if (checkerror()) {
            return this;
        }
        return autoFit.setValue(resid, value, hint, checked);
    }

    @Override
    public AutoFit setValueSync(int resid, Object value, Object hint, boolean checked) {
        if (checkerror()) {
            return this;
        }
        return autoFit.setValueSync(resid, value, hint, checked);
    }

    @Override
    public AutoFit setValue(int resid, Object value) {
        if (checkerror()) {
            return this;
        }
        return autoFit.setValue(resid, value);
    }

    @Override
    public AutoFit setValueSync(int resid, Object value) {
        if (checkerror()) {
            return this;
        }
        return autoFit.setValueSync(resid, value);
    }

    @Override
    public AutoFit hide(int resid) {
        if (checkerror()) {
            return this;
        }
        return autoFit.hide(resid);
    }

    @Override
    public AutoFit show(int resid) {
        if (checkerror()) {
            return this;
        }
        return autoFit.show(resid);
    }

    @Override
    public AutoFit addValue(int resid, Object value) {
        if (checkerror()) {
            return this;
        }
        return autoFit.addValue(resid, value);
    }

    @Override
    public AutoFit addValueSync(int resid, Object value) {
        if (checkerror()) {
            return this;
        }
        return autoFit.addValueSync(resid, value);
    }

    @Override
    public AutoFit delValue(int resid, int size) {
        if (checkerror()) {
            return this;
        }
        return autoFit.delValue(resid, size);
    }

    @Override
    public AutoFit delValueSync(int resid, int size) {
        if (checkerror()) {
            return this;
        }
        return autoFit.delValueSync(resid, size);
    }

    @Override
    public AutoFit startFrg(Class fragment, Object activity, int flag, Object... params) {
        if (checkerror()) {
            return this;
        }
        return autoFit.startFrg(fragment, activity, flag, params);
    }

    @Override
    public AutoFit startFrg(Class fragment, Object activity, Object... params) {
        if (checkerror()) {
            return this;
        }
        return autoFit.startFrg(fragment, activity, params);
    }

    @Override
    public AutoFit startFrg(Fragment fragment, Object activity, int flag, Object... params) {
        if (checkerror()) {
            return this;
        }
        return autoFit.startFrg(fragment, activity, flag, params);
    }

    @Override
    public AutoFit startFrg(Fragment fragment, Object activity, Object... params) {
        if (checkerror()) {
            return this;
        }
        return autoFit.startFrg(fragment, activity, params);
    }

    @Override
    public AutoFit startFrg(int resid, Object activity, Object... params) {
        if (checkerror()) {
            return this;
        }
        return autoFit.startFrg(resid, activity, params);
    }

    @Override
    public AutoFit startFrg(int resid, Object activity, int flag, Object... params) {
        if (checkerror()) {
            return this;
        }
        return autoFit.startFrg(resid, activity, flag, params);
    }

    @Override
    public AutoFit startAct(Object activity, Object... params) {
        if (checkerror()) {
            return this;
        }
        return autoFit.startAct(activity, params);
    }

    @Override
    public AutoFit startAct(Object activity, int flag, Object... params) {
        if (checkerror()) {
            return this;
        }
        return autoFit.startAct(activity, flag, params);
    }

    @Override
    public AutoFit startActivity(int flag, Object fragment, Object activity, Object[] params) {
        if (checkerror()) {
            return this;
        }
        return autoFit.startActivity(flag, fragment, activity, params);
    }

    @Override
    public AutoFit startActivity(Object fragment, Object activity, Object[] params) {
        if (checkerror()) {
            return this;
        }
        return autoFit.startActivity(fragment, activity, params);
    }

    @Override
    public AutoFit startActivity(Object fragment, Object activity) {
        if (checkerror()) {
            return this;
        }
        return autoFit.startActivity(fragment, activity);
    }

    @Override
    public AutoFit bind(String name, Object object) {
        if (checkerror()) {
            return this;
        }
        return autoFit.bind(name, object);
    }

    @Override
    public AutoFit CKERR() {
        return super.CKERR();
    }

    @Override
    public AutoFit startActivity(int resId, Object activity, Object[] params) {
        if (checkerror()) {
            return this;
        }
        return autoFit.startActivity(resId, activity, params);
    }

    @Override
    public AutoFit startActivity(int flag, int resId, Object activity, Object[] params) {
        if (checkerror()) {
            return this;
        }
        return autoFit.startActivity(flag, resId, activity, params);
    }

    @Override
    public AutoFit startActivity(int resid, Object[] params) {
        if (checkerror()) {
            return this;
        }
        return autoFit.startActivity(resid, params);
    }

    @Override
    public View findView(int resid) {
        return autoFit.findView(resid);
    }

    @Override
    public AutoFit setParams(int resid, Object... params) {
        if (checkerror()) {
            return this;
        }
        return autoFit.setParams(resid, params);
    }

    @Override
    public AutoFit reload() {
        return super.reload();
    }

    @Override
    public AutoFit reload(int resid, int recycleviewid, Object... params) {
        if (checkerror()) {
            return this;
        }
        return autoFit.reload(resid, recycleviewid, params);
    }

    @Override
    public AutoFit pulload(int resid, int recycleviewid, Object... params) {
        if (checkerror()) {
            return this;
        }
        return autoFit.pulload(resid, recycleviewid, params);
    }

    @Override
    public AutoFit loadApi(int resid, Object... params) {
        if (checkerror()) {
            return this;
        }
        return autoFit.loadApi(resid, params);
    }

    @Override
    public AutoFit close(String handleid, int resid) {
        if (checkerror()) {
            return this;
        }
        return autoFit.close(handleid, resid);
    }

    @Override
    public AutoFit setParams(String handleid, int resid, Object... params) {
        if (checkerror()) {
            return this;
        }
        return autoFit.setParams(handleid, resid, params);
    }

    @Override
    public AutoFit reload(String handleid, int resid, int recycleviewid, Object... params) {
        if (checkerror()) {
            return this;
        }
        return autoFit.reload(handleid, resid, recycleviewid, params);
    }

    @Override
    public AutoFit pulload(String handleid, int resid, int recycleviewid, Object... params) {
        if (checkerror()) {
            return this;
        }
        return autoFit.pulload(handleid, resid, recycleviewid, params);
    }

    @Override
    public AutoFit loadApi(String handleid, int resid, Object... params) {
        if (checkerror()) {
            return this;
        }
        return autoFit.loadApi(handleid, resid, params);
    }

    @Override
    public AutoFit send(String postid, int type, Object... params) {
        if (checkerror()) {
            return this;
        }
        return autoFit.send(postid, type, params);
    }

    @Override
    public List merge(List list1, List list2) {
        return autoFit.merge(list1, list2);
    }

    @Override
    public List megeall(List list1, List list2) {
        return autoFit.megeall(list1, list2);
    }

    @Override
    public Object[] CV(Object... params) {
        return autoFit.CV(params);
    }

    @Override
    public AutoFit RUN(Object object, String method, Object... params) {
        if (checkerror()) {
            return this;
        }
        return autoFit.RUN(object, method, params);
    }

    @Override
    public AutoFit pop(int viewid, int resid) {
        return super.pop(viewid, resid);
    }

    @Override
    public AutoFit pop(View v, int resid) {
        if (checkerror()) {
            return this;
        }
        return autoFit.pop(v, resid);
    }

    @Override
    public AutoFit pop(View v, int xoff, int yoff, int resid) {
        if (checkerror()) {
            return this;
        }
        return autoFit.pop(v, xoff, yoff, resid);
    }

    @Override
    public AutoFit pop(View v, int xoff, int yoff, int gravty, int resid) {
        if (checkerror()) {
            return this;
        }
        return autoFit.pop(v, xoff, yoff, gravty, resid);
    }

    @Override
    public AutoFit pop(MPopDefault pop, View v, int resid) {
        if (checkerror()) {
            return this;
        }
        return autoFit.pop(pop, v, resid);
    }

    @Override
    public AutoFit pop(MPopDefault pop, View v, int xoff, int yoff, int resid) {
        if (checkerror()) {
            return this;
        }
        return autoFit.pop(pop, v, xoff, yoff, resid);
    }

    @Override
    public AutoFit pop(MPopDefault pop, View v, int xoff, int yoff, int gravty, int resid) {
        if (checkerror()) {
            return this;
        }
        return autoFit.pop(pop, v, xoff, yoff, gravty, resid);
    }

    @Override
    public AutoFit listSet(int recyclerviewid, ApiUpdate api, int resid) {
        if (checkerror()) {
            return this;
        }
        return autoFit.listSet(recyclerviewid, api, resid);
    }

    @Override
    public AutoFit listSet(int recyclerviewid, ApiUpdate api, int resid, boolean isstart) {
        if (checkerror()) {
            return this;
        }
        return autoFit.listSet(recyclerviewid, api, resid, isstart);
    }

    @Override
    public AutoFit listSet(int recyclerviewid, ApiUpdate api, int resid, boolean needpull, boolean isstart) {
        if (checkerror()) {
            return this;
        }
        return autoFit.listSet(recyclerviewid, api, resid, needpull, isstart);
    }

    @Override
    public AutoFit listSet(int recyclerviewid, ApiUpdate api, int resid, HashMap<Class, AutoDataFormat.CardParam> resids, boolean needpull, boolean isstart) {
        if (checkerror()) {
            return this;
        }
        return autoFit.listSet(recyclerviewid, api, resid, resids, needpull, isstart);
    }

    @Override
    public AutoFit listSet(int recyclerviewid, ApiUpdate api, DataFormat dataFormat) {
        if (checkerror()) {
            return this;
        }
        return autoFit.listSet(recyclerviewid, api, dataFormat);
    }

    @Override
    public AutoFit listSet(int recyclerviewid, ApiUpdate api, DataFormat dataFormat, boolean isstart) {
        if (checkerror()) {
            return this;
        }
        return autoFit.listSet(recyclerviewid, api, dataFormat, isstart);
    }

    @Override
    public AutoFit listSet(int recyclerviewid, ApiUpdate api, DataFormat dataFormat, boolean needpull, boolean isstart) {
        if (checkerror()) {
            return this;
        }
        return autoFit.listSet(recyclerviewid, api, dataFormat, needpull, isstart);
    }

    @Override
    public AutoFit save(ApiUpdate[] apis, String[] names) {
        if (checkerror()) {
            return this;
        }
        return autoFit.save(apis, names);
    }

    @Override
    public AutoFit save(ApiUpdate api, String name) {
        if (checkerror()) {
            return this;
        }
        return autoFit.save(api, name);
    }

    @Override
    public AutoFit save(ApiUpdate[] apis, String[] names, boolean isstart) {
        if (checkerror()) {
            return this;
        }
        return autoFit.save(apis, names, isstart);
    }

    @Override
    public AutoFit save(ApiUpdate api, String name, boolean isstart) {
        if (checkerror()) {
            return this;
        }
        return autoFit.save(api, name, isstart);
    }

    @Override
    public AutoFit save(ApiUpdate[] apis, String[] names, boolean bool, boolean isstart) {
        if (checkerror()) {
            return this;
        }
        return autoFit.save(apis, names, bool, isstart);
    }

    @Override
    public AutoFit save(ApiUpdate api, String name, boolean bool, boolean isstart) {
        if (checkerror()) {
            return this;
        }
        return autoFit.save(api, name, bool, isstart);
    }

    @Override
    public AutoFit bind(ApiUpdate[] apis, String[] names) {
        if (checkerror()) {
            return this;
        }
        return autoFit.bind(apis, names);
    }

    @Override
    public AutoFit bind(ApiUpdate api, String name) {
        if (checkerror()) {
            return this;
        }
        return autoFit.bind(api, name);
    }

    @Override
    public AutoFit bind(ApiUpdate[] apis, String[] names, boolean isstart) {
        if (checkerror()) {
            return this;
        }
        return autoFit.bind(apis, names, isstart);
    }

    @Override
    public AutoFit bind(ApiUpdate api, String name, boolean isstart) {
        if (checkerror()) {
            return this;
        }
        return autoFit.bind(api, name, isstart);
    }

    @Override
    public AutoFit bind(ApiUpdate[] apis, String[] names, boolean bool, boolean isstart) {
        if (checkerror()) {
            return this;
        }
        return autoFit.bind(apis, names, bool, isstart);
    }

    @Override
    public AutoFit bind(ApiUpdate api, String name, boolean bool, boolean isstart) {
        if (checkerror()) {
            return this;
        }
        return autoFit.bind(api, name, bool, isstart);
    }


    @Override
    public AutoFit check(Object obj, boolean bool) {
        if (checkerror()) {
            return this;
        }
        return autoFit.check(obj, bool);
    }

    @Override
    public AutoFit pop(MPopDefault autoFitPop, PopShowParme sp, int resid) {
        if (checkerror()) {
            return this;
        }
        return autoFit.pop(autoFitPop, sp, resid);
    }

    @Override
    public AutoFit pop(PopShowParme sp, int resid) {
        if (checkerror()) {
            return this;
        }
        return autoFit.pop(sp, resid);
    }

    @Override
    public AutoFit dialog(int resid) {
        if (checkerror()) {
            return this;
        }
        return autoFit.dialog(resid);
    }

    @Override
    public AutoFit dialog(MDialog dialog) {
        if (checkerror()) {
            return this;
        }
        return autoFit.dialog(dialog);
    }

    @Override
    public AutoFit put(String key, Object value) {
        if (checkerror()) {
            return this;
        }
        return autoFit.put(key, value);
    }

    @Override
    public AutoFit putsync(String key, Object value) {
        if (checkerror()) {
            return this;
        }
        return autoFit.putsync(key, value);
    }

    @Override
    public AutoFit toastsync(String text) {
        return autoFit.toastsync(text);
    }

    @Override
    public AutoFit toast(String text) {
        if (checkerror()) {
            return this;
        }
        return autoFit.toast(text);
    }

    @Override
    public AutoFit visable(int resid, int vis) {
        if (checkerror()) {
            return this;
        }
        return autoFit.visable(resid, vis);
    }

    @Override
    public AutoFit visablesync(int resid, int vis) {
        if (checkerror()) {
            return this;
        }
        return autoFit.visablesync(resid, vis);
    }

    @Override
    public AutoFit setVisable(int resid, boolean isshow) {
        if (checkerror()) {
            return this;
        }
        return autoFit.setVisable(resid, isshow);
    }

    @Override
    public AutoFit setAdasync(int resid, int itemresid, Object object) {
        if (checkerror()) {
            return this;
        }
        return autoFit.setAdasync(resid, itemresid, object);
    }

    @Override
    public AutoFit setAdasync(int resid, CardAdapter adapter) {
        if (checkerror()) {
            return this;
        }
        return autoFit.setAdasync(resid, adapter);
    }

    @Override
    public AutoFit setAda(int resid, int itemresid, Object object) {
        if (checkerror()) {
            return this;
        }
        return autoFit.setAda(resid, itemresid, object);
    }

    @Override
    public AutoFit setAda(int resid, CardAdapter adapter) {
        if (checkerror()) {
            return this;
        }
        return autoFit.setAda(resid, adapter);
    }

    @Override
    public AutoFit setAda(View view, int itemresid, Object object) {
        if (checkerror()) {
            return this;
        }
        return autoFit.setAda(view, itemresid, object);
    }

    @Override
    public AutoFit setAda(View view, CardAdapter adapter) {
        if (checkerror()) {
            return this;
        }
        return autoFit.setAda(view, adapter);
    }

    @Override
    public AutoFit setCurr(int resid, Object object) {
        if (checkerror()) {
            return this;
        }
        return autoFit.setCurr(resid, object);
    }

    @Override
    public AutoFit setCurrSync(int resid, Object object) {
        if (checkerror()) {
            return this;
        }
        return autoFit.setCurrSync(resid, object);
    }

    @Override
    public AutoFit parent() {
        return autoFit.parent();
    }

    @Override
    public AutoFit child() {
        return autoFit.child();
    }

    @Override
    public boolean equal(Object obj1, Object obj2) {
        return autoFit.equal(obj1, obj2);
    }

    public boolean checkerror() {
        if (error == 0) {
            return false;
        } else {
            return true;
        }
    }


    @Override
    public AutoFit getPhoto(String bindname, Integer aspectX, Integer aspectY, Integer outputX, Integer outputY) {
        if (checkerror()) {
            return this;
        }
        return super.getPhoto(bindname, aspectX, aspectY, outputX, outputY);
    }

    @Override
    public AutoFit getPhotos(String bindname, Integer size) {
        if (checkerror()) {
            return this;
        }
        return super.getPhotos(bindname, size);
    }

    @Override
    public AutoFit getPhoto(String bindname) {
        if (checkerror()) {
            return this;
        }
        return super.getPhoto(bindname);
    }

    @Override
    public AutoFit showPhoto(String path, String... photopaths) {
        if (checkerror()) {
            return this;
        }
        return super.showPhoto(path, photopaths);
    }

    @Override
    public AutoFit showPhoto(String path, List<String> photopaths) {
        if (checkerror()) {
            return this;
        }
        return super.showPhoto(path, photopaths);
    }

    @Override
    public AutoFit showPhoto(List<String> photopaths) {
        if (checkerror()) {
            return this;
        }
        return super.showPhoto(photopaths);
    }
}
