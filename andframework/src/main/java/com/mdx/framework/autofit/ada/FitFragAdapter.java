package com.mdx.framework.autofit.ada;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mdx.framework.R;
import com.mdx.framework.autofit.AutoFitBase;
import com.mdx.framework.autofit.commons.MIT;
import com.mdx.framework.widget.MImageView;
import com.mdx.framework.widget.magicindicator.MagicIndicator;
import com.mdx.framework.widget.magicindicator.buildins.UIUtil;
import com.mdx.framework.widget.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.mdx.framework.widget.magicindicator.buildins.commonnavigator.abs.IMeasurablePagerTitleView;
import com.mdx.framework.widget.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.mdx.framework.widget.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.mdx.framework.widget.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import com.mdx.framework.widget.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView;
import com.mdx.framework.widget.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView;
import com.mdx.framework.widget.pagerecycleview.autofit.AutoCard;
import com.mdx.framework.widget.pagerecycleview.autofit.AutoMViewHold;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by ryan on 2018/1/5.
 */

public class FitFragAdapter extends FragmentPagerAdapter {
    public List<MIT> mFragments = new ArrayList<>();
    public ViewPager mViewPager;
    public Context context;
    public MagicIndicator magicIndicator;
    public AutoFitBase autoFitBase;
    public int linetype;
    public int linewidth;
    public int linecolor;

    public FitFragAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int posion) {
        return mFragments.get(posion).fragment;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }


    public CommonNavigatorAdapter commonNavigatorAdapter = new CommonNavigatorAdapter() {
        private SparseArray<Stack<RecyclerView.ViewHolder>> mViewHolders = new SparseArray<>();

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public IPagerTitleView getTitleView(Context context, final int position) {
            IMeasurablePagerTitleView retn = null;
            final MIT mi = mFragments.get(position);
            if (mi.type == 1) {
                if (mi.resid == 0) {
                    mi.resid = R.layout.pager_navigator_imagebar;
                }
                final AutoMViewHold viewHolder = AutoMViewHold.getView(context, magicIndicator, mi.resid);
                viewHolder.set(position, new AutoCard(mi.resid, mi, autoFitBase));
                if (mi.resid == R.layout.pager_navigator_imagebar) {
                    MImageView imgv = viewHolder.findViewById(R.id.title_img);
                    imgv.setImageDrawable(mi.drawable);
                }
                CommonPagerTitleView commonPagerTitleView = new CommonPagerTitleView(context);
                commonPagerTitleView.setContentView(viewHolder.itemView);
                commonPagerTitleView.setOnPagerTitleChangeListener(new CommonPagerTitleView.OnPagerTitleChangeListener() {

                    @Override
                    public void onSelected(int index, int totalCount) {
                        mi.index = index;
                        mi.totalCount = totalCount;
                        viewHolder.getContextView().setSelected(true);
                        viewHolder.getContextView().setBackgroundColor(mi.selcolor);
                        viewHolder.rebindItem();
                    }

                    @Override
                    public void onDeselected(int index, int totalCount) {
                        mi.index = index;
                        mi.totalCount = totalCount;
                        viewHolder.getContextView().setSelected(false);
                        viewHolder.getContextView().setBackgroundColor(mi.nomorecolor);
                        viewHolder.rebindItem();
                    }

                    @Override
                    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
                        mi.index = index;
                        mi.totalCount = totalCount;
                        mi.leftToRight = leftToRight;
                        mi.leavePercent = leavePercent;
                        viewHolder.rebindItem();
                    }

                    @Override
                    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
                        mi.index = index;
                        mi.totalCount = totalCount;
                        mi.enterPercent = enterPercent;
                        mi.leftToRight = leftToRight;
                        viewHolder.rebindItem();
                    }
                });
                retn = commonPagerTitleView;
            } else if (mi.type == 0) {
                ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context);
                clipPagerTitleView.setText(mi.name);
                clipPagerTitleView.setTextColor(mi.nomorecolor);
                clipPagerTitleView.setClipColor(mi.selcolor);
                retn = clipPagerTitleView;
            }
            if (retn instanceof View) {
                ((View) retn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(position);
                    }
                });
            }
            return retn;
        }

        @Override
        public IPagerIndicator getIndicator(Context context) {
            if (linetype == 0) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                float navigatorHeight = linewidth;
                float borderWidth = UIUtil.dip2px(context, 1);
                float lineHeight = navigatorHeight - 2 * borderWidth;
                indicator.setLineHeight(lineHeight);
                indicator.setRoundRadius(lineHeight / 2);
                indicator.setYOffset(borderWidth);
                indicator.setColors(linecolor);
                return indicator;
            }
            return null;
        }
    };
}
