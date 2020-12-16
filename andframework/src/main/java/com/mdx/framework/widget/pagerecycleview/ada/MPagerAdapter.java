package com.mdx.framework.widget.pagerecycleview.ada;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.mdx.framework.adapter.MAdapter;

import java.util.Stack;

/**
 * A PagerAdapter that wraps around another PagerAdapter to handle paging wrap-around.
 * Thanks to: https://github.com/antonyt/InfiniteViewPager
 */
public class MPagerAdapter extends PagerAdapter {

    private SparseArray<Stack<View>> mViews = new SparseArray<>();

    private SparseArray<Stack<RecyclerView.ViewHolder>> mViewHolders = new SparseArray<>();
    private Context context;
    private ViewPager mViewPager;
    private MAdapter adapter;
    private CardAdapter cardAdapter;

    public MPagerAdapter(MAdapter adapter) {
        this.adapter = adapter;
    }

    public MPagerAdapter(CardAdapter adapter) {
        this.cardAdapter = adapter;
    }

    @Override
    public int getCount() {
        return getRealCount();
    }

    public int getRealCount() {
        if (adapter != null) {
            return adapter.getItemCount();
        } else {
            return cardAdapter.getItemCount();
        }
    }


    public Object get(int i){
        if (adapter != null) {
            return adapter.get(i);
        } else {
            return cardAdapter.get(i);
        }
    }

    public int getItemPosition(Object object) {
        for (int i = 0; i < getRealCount(); i++) {
            Object obj = this.get(i);
            if (obj == object) {
                return i;
            } else {
                if (obj instanceof Card && ((Card) obj).item == object) {
                    return i;
                }
            }
        }
        return -1;
    }


    @Override
    public Object instantiateItem(ViewGroup rootview, int nowposition) {
        int position = nowposition % getRealCount();
        mViewPager = (ViewPager) rootview;
        if (adapter != null) {
            View contentView = null;
            int viewtype = adapter.getItemViewType(position);
            Stack<View> stk = mViews.get(viewtype);
            if (stk != null && stk.size() > 0) {
                contentView = stk.pop();
            }
            contentView = adapter.getview(position, contentView, mViewPager);
            mViewPager.addView(contentView);
            return contentView;
        } else {
            RecyclerView.ViewHolder contentView = null;
            int viewtype = cardAdapter.getItemViewType(position);
            Stack<RecyclerView.ViewHolder> stk = mViewHolders.get(viewtype);
            if (stk != null && stk.size() > 0) {
                contentView = stk.pop();
            }
            if (contentView == null) {
                contentView=cardAdapter.onCreateViewHolder(mViewPager,viewtype);
            }
            cardAdapter.onBindViewHolder(contentView, position);
            contentView.itemView.setTag(contentView);
            mViewPager.addView(contentView.itemView);
            return contentView.itemView;
        }
    }

    @Override
    public void destroyItem(ViewGroup arg0, int nowposition, Object arg2) {
        mViewPager = (ViewPager) arg0;
        mViewPager.removeView((View) arg2);
        int position = nowposition % getRealCount();
        if(adapter!=null) {
            int viewtype = adapter.getItemViewType(position);
            Stack<View> stk = mViews.get(viewtype);
            if (stk != null) {
                stk.push((View) arg2);
            } else {
                stk = new Stack<>();
                stk.push((View) arg2);
                mViews.put(viewtype, stk);
            }
        }else{
            int viewtype = cardAdapter.getItemViewType(position);
            Stack<RecyclerView.ViewHolder> stk = mViewHolders.get(viewtype);
            RecyclerView.ViewHolder vh=(RecyclerView.ViewHolder) ((View) arg2).getTag();
            if (stk != null) {
                stk.push(vh);
            } else {
                stk = new Stack<>();
                stk.push(vh);
                mViewHolders.put(viewtype, stk);
            }
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}