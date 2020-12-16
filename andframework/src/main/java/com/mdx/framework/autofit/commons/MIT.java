package com.mdx.framework.autofit.commons;


import android.graphics.drawable.Drawable;

import androidx.fragment.app.Fragment;

import com.mdx.framework.Frame;
import com.mdx.framework.R;
import com.mdx.framework.autofit.AutoFit;
import com.mdx.framework.autofit.AutoFitFragment;

/**
 * Created by ryan on 2018/1/5.
 */

public class MIT {
    public Fragment fragment;

    public String name;

    public String imageurl;
    public int imgid;

    public Drawable drawable;

    public int selcolor = 0;

    public int nomorecolor = 0;

    public int type;

    public int resid;

    public int index, totalCount;
    public float leavePercent;
    public boolean leftToRight;
    public float enterPercent;


    public static MIT get(Fragment fragment, String name, String imageurl, Drawable drawable, int selcolor, int nomorecolor, int resid, int type) {
        MIT item = new MIT();
        item.fragment = fragment;
        item.name = name;
        item.imageurl = imageurl;
        item.drawable = drawable;
        item.selcolor = selcolor;
        item.nomorecolor = nomorecolor;
        item.type = type;
        item.resid = resid;
        item.type = 1;
        return item;
    }

    public static MIT get(Fragment fragment, String name, Drawable drawable, int selcolor, int nomorecolor) {
        MIT item = new MIT();
        item.fragment = fragment;
        item.name = name;
        item.selcolor = selcolor;
        item.drawable = drawable;
        item.nomorecolor = nomorecolor;
        item.type = 1;
        return item;
    }

    public static MIT get(Fragment fragment, String name, Drawable drawable) {
        MIT item = new MIT();
        item.fragment = fragment;
        item.name = name;
        item.drawable = drawable;
        item.type = 1;
        return item;
    }

    public static MIT get(Fragment fragment, String name) {
        MIT item = new MIT();
        item.fragment = fragment;
        item.name = name;
        item.type = 0;
        return item;
    }

    public static MIT get(int fragmentid, String name) {
        MIT item = new MIT();
        AutoFitFragment frg = new AutoFitFragment();
        frg.layoutid = fragmentid;
        item.fragment = frg;
        item.name = name;
        item.type = 0;
        return item;
    }


    public static MIT get(int fragmentid, String name, Drawable drawable, int selcolor, int nomorecolor) {
        MIT item = new MIT();
        AutoFitFragment frg = new AutoFitFragment();
        frg.layoutid = fragmentid;
        item.fragment = frg;
        item.drawable = drawable;
        item.name = name;
        item.type = 0;
        item.selcolor = selcolor;
        item.nomorecolor = nomorecolor;
        item.type = 1;
        return item;
    }

    public static MIT get(int fragmentid, String name, Drawable drawable) {
        MIT item = new MIT();
        AutoFitFragment frg = new AutoFitFragment();
        frg.layoutid = fragmentid;
        item.fragment = frg;
        item.drawable = drawable;
        item.name = name;
        item.type = 0;
        item.type = 1;
        return item;
    }

    public static MIT get(int fragmentid, String name, int selcolor, int nomorecolor) {
        MIT item = new MIT();
        AutoFitFragment frg = new AutoFitFragment();
        frg.layoutid = fragmentid;
        item.fragment = frg;
        item.name = name;
        item.type = 0;
        item.selcolor = selcolor;
        item.nomorecolor = nomorecolor;
        return item;
    }


    public static MIT get(Fragment fragment, String name, int selcolor, int nomorecolor) {
        MIT item = new MIT();
        item.fragment = fragment;
        item.name = name;
        item.selcolor = selcolor;
        item.nomorecolor = nomorecolor;
        item.type = 0;
        return item;
    }

    public static MIT get(Fragment fragment) {
        return get(fragment, "");
    }

    public static MIT get(int fragmentid) {
        return get(fragmentid, "");
    }


    public MIT type(int type) {
        this.type = type;
        return this;
    }

    public MIT selcolor(int selcolor) {
        this.selcolor = selcolor;
        return this;
    }


    public MIT nomcolor(int nomorecolor) {
        this.nomorecolor = nomorecolor;
        return this;
    }

    public MIT resid(int resid) {
        this.resid = resid;
        return this;
    }

    public MIT drawable(Drawable drawable) {
        this.drawable = drawable;
        return this;
    }

    public MIT imgid(int imgid) {
        this.imgid = imgid;
        return this;
    }

    public MIT name(String name) {
        this.name = name;
        return this;
    }

    public MIT imageurl(String imageurl) {
        this.imageurl = imageurl;
        return this;
    }

    public static SetFrag getSF(AutoFit act, int resid, int linetype, int linewidth, int lincolor, MIT... ims) {
        SetFrag setFrag = new SetFrag();
        setFrag.act = act;
        setFrag.mits = ims;
        setFrag.resid = resid;
        return setFrag;
    }

    public static SetFrag getSF(AutoFit act, int resid, int lincolor, MIT... ims) {
        SetFrag setFrag = new SetFrag();
        setFrag.act = act;
        setFrag.mits = ims;
        setFrag.resid = resid;
        return setFrag;
    }

    public static SetFrag getSF(AutoFit act, int resid, MIT... ims) {
        SetFrag setFrag = new SetFrag();
        setFrag.act = act;
        setFrag.mits = ims;
        setFrag.resid = resid;
        return setFrag;
    }
}
