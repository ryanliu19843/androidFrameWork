package com.mdx.framework.autofit.commons;

public class AutoFitObject implements AutoFitItem {
    public Object object;
    public Integer resid = null, showType = null;
    public Boolean useranimation = null;

    public AutoFitObject(Object object) {
        this.object = object;
    }

    public AutoFitObject(int resid, Object object) {
        this.resid = resid;
        this.object = object;
    }

    public AutoFitObject(int resid, Object object, int showType) {
        this.resid = resid;
        this.object = object;
        this.showType = showType;
    }

    public AutoFitObject(int resid, Object object, int showType, boolean useranimation) {
        this.resid = resid;
        this.object = object;
        this.showType = showType;
        this.useranimation = useranimation;
    }

    @Override
    public Object getObj() {
        return object;
    }

    @Override
    public Integer getResId() {
        return resid;
    }

    @Override
    public Boolean useAnimation() {
        return useranimation;
    }

    @Override
    public Integer showType() {
        return showType;
    }


    public void setObject(Object object) {
        this.object = object;
    }

    public void setResid(Integer resid) {
        this.resid = resid;
    }

    public void setShowType(Integer showType) {
        this.showType = showType;
    }

    public void setUseranimation(Boolean useranimation) {
        this.useranimation = useranimation;
    }
}
