package com.mdx.framework.autofit;

import android.os.Bundle;
import android.text.TextUtils;

import com.mdx.framework.R;
import com.mdx.framework.activity.MFragment;
import com.mdx.framework.commons.ParamsManager;

import java.lang.reflect.Field;

/**
 * Created by ryan on 2017/10/18.
 */


public class AutoFitFragment extends MFragment {
    public int layoutid = 0;

    @Override
    protected void create(Bundle savedInstanceState) {
        if (layoutid == 0) {
            layoutid = getActivity().getIntent().getIntExtra("layout", 0);
            if (layoutid == 0) {
                String defxml = ParamsManager.get("DefaultXml");
                if (!TextUtils.isEmpty(defxml)) {
                    try {
                        int cldev = defxml.lastIndexOf(".");
                        String clsname = defxml.substring(0, cldev);
                        String bl = defxml.substring(cldev + 1);
                        Class clas = Class.forName(clsname);
                        Field field = clas.getField(bl);
                        layoutid = field.getInt(clas);
                    } catch (Exception e) {
                        layoutid = R.layout.frg_load;
                    }
                } else {
                    layoutid = R.layout.frg_load;
                }
            }
        }
        setContentView(layoutid);
    }
}

