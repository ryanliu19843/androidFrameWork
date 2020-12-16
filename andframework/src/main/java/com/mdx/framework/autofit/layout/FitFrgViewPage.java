package com.mdx.framework.autofit.layout;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import android.util.AttributeSet;
import android.view.View;

import com.mdx.framework.autofit.AutoFit;
import com.mdx.framework.autofit.ada.FitFragAdapter;
import com.mdx.framework.autofit.commons.MIT;
import com.mdx.framework.autofit.commons.SetFrag;
import com.mdx.framework.widget.magicindicator.MagicIndicator;
import com.mdx.framework.widget.magicindicator.ViewPagerHelper;
import com.mdx.framework.widget.magicindicator.buildins.commonnavigator.CommonNavigator;
import com.mdx.framework.widget.viewpagerindicator.PageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryan on 2018/1/5.
 */

public class FitFrgViewPage extends ViewPager {

    public FitFragAdapter fitFragAdapter;

    public FitFrgViewPage(Context context) {
        super(context);
    }

    public FitFrgViewPage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void setFragments(SetFrag sf) {
        AutoFit act = sf.act;
        MIT[] ims = sf.mits;
        List<MIT> list = new ArrayList<>();
        for (MIT im : ims) {
            list.add(im);
        }
        createAdapter(act);
        fitFragAdapter.mFragments = list;
        fitFragAdapter.notifyDataSetChanged();
        this.setAdapter(fitFragAdapter);
        fitFragAdapter.commonNavigatorAdapter.notifyDataSetChanged();
        setMindicator(sf.act, sf.resid, sf.linetype, sf.linewidth, sf.lincolor);
    }

    public void setMindicator(AutoFit act, int resid, int linetype, int linewidth, int lincolor) {
        View indicator = act.findView(resid);
        if (indicator instanceof MagicIndicator) {
            MagicIndicator magicIndicator = (MagicIndicator) indicator;
            createAdapter(act);
            fitFragAdapter.linecolor = lincolor;
            fitFragAdapter.linetype = linetype;
            fitFragAdapter.linewidth = linewidth;
            fitFragAdapter.magicIndicator = magicIndicator;
            CommonNavigator commonNavigator = new CommonNavigator(getContext());
            commonNavigator.setAdjustMode(true);
            commonNavigator.setAdapter(fitFragAdapter.commonNavigatorAdapter);
            magicIndicator.setNavigator(commonNavigator);
            ViewPagerHelper.bind(magicIndicator, this);
        } else if (indicator instanceof PageIndicator) {
            ((PageIndicator) indicator).setViewPager(this);
        }

    }

    public void setOnLongClick(OnClickListener longClick) {
        this.setOnLongClickListener((v) -> {
            longClick.onClick(v);
            return true;
        });
    }

    public void createAdapter(AutoFit act) {
        if (fitFragAdapter != null) {
            return;
        }
        if (act.autoFit.iActivity instanceof FragmentActivity) {
            fitFragAdapter = new FitFragAdapter(((FragmentActivity) act.autoFit.iActivity).getSupportFragmentManager());
        } else if (act.autoFit.iActivity instanceof Fragment) {
            fitFragAdapter = new FitFragAdapter(((Fragment) act.autoFit.iActivity).getChildFragmentManager());
        }
        fitFragAdapter.mViewPager = this;
        fitFragAdapter.autoFitBase = act.autoFit;
        fitFragAdapter.context = act.autoFit.iActivity.getContext();
    }
}
