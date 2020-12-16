package com.mdx.framework.autofit.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.os.Build;

import androidx.annotation.RequiresApi;

import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.mdx.framework.R;

/**
 * Created by ryan on 2017/10/19.
 */

public class FitScrollView extends ScrollView implements FitLayout {
    public Object loadApi, saveApi, updateApi;


    public FitScrollView(Context context) {
        super(context);
    }

    public FitScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttr(context, attrs, 0);
    }

    public FitScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttr(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FitScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        getAttr(context, attrs, defStyleAttr);
    }

    public void getAttr(Context context, AttributeSet attrs, int defStyle) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.FitLayout, defStyle, 0);
        loadApi = a.getString(R.styleable.FitLayout_loadApi);
        saveApi = a.getString(R.styleable.FitLayout_saveApi);
        updateApi = a.getString(R.styleable.FitLayout_updateApi);
        a.recycle();
    }

    @Override
    public void setLoadApi(Object loadApi) {
        this.loadApi = loadApi;
    }

    public void setOnLongClick(OnClickListener longClick) {
        this.setOnLongClickListener((v) -> {
            longClick.onClick(v);
            return true;
        });
    }

    @Override
    public void setSaveApi(Object saveApi) {
        this.saveApi = saveApi;
    }

    @Override
    public void setUpdateApi(Object updateApi) {
        this.updateApi = updateApi;
    }

    @Override
    public Object getLoadApi() {
        return loadApi;
    }

    @Override
    public Object getSaveApi() {
        return saveApi;
    }

    @Override
    public Object getUpdateApi() {
        return updateApi;
    }


    private float x = -1, y = -1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean retn = super.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
            x = -1;
            y = -1;
        } else {
            x = event.getRawX();
            y = event.getRawY();
        }
        return retn;
    }

    @Override
    public Point getTouchPoint() {
        if (x >= 0) {
            return new Point((int) x, (int) y);
        }
        return null;
    }
}
