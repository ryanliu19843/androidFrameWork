package com.mdx.framework.widget.pagerecycleview.ada;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mdx.framework.Frame;
import com.mdx.framework.widget.pagerecycleview.animator.AnimatorUtil;
import com.mdx.framework.widget.pagerecycleview.animator.ViewAnimator;
import com.mdx.framework.widget.pagerecycleview.autofit.AutoDataFormat;
import com.mdx.framework.widget.pagerecycleview.autofit.AutoMViewHold;
import com.mdx.framework.widget.pagerecycleview.viewhold.MViewHold;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.View.SCALE_X;
import static android.view.View.SCALE_Y;
import static android.view.View.TRANSLATION_Y;

@SuppressLint("UseSparseArrays")
public abstract class MAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected Context context;

    public HAdapter hAdapter;

    protected LayoutInflater layoutInflater;
    protected ViewAnimator mViewAnimator;

    public ArrayList<Card> overCard = new ArrayList<>();
    protected ArrayList<Card> list = new ArrayList<Card>();
    protected OnNotifyChangedListener onNotifyChangedListener;
    protected int resid = 0;

    public interface OnNotifyChangedListener {
        void onNotifyChanged(MAdapter adapter);
    }

    public RecyclerView recyclerView;

    public HashMap<Object, Object> params = new HashMap<Object, Object>();

    public MAdapter(Context context, Card[] list) {
        this.context = context;
        layoutInflater = LayoutInflater.from(this.context);
        for (Card item : list) {
            item.isanimation = false;
            item.setAdapter(this);
            item.overViewHold = null;
            this.list.add(item);
        }
        resetOverCard();
    }

    public MAdapter(Context context, List<Card> list) {
        this.context = context;
        layoutInflater = LayoutInflater.from(this.context);
        this.list.addAll(list);
        for (Card item : list) {
            item.isanimation = false;
            item.setAdapter(this);
            item.overViewHold = null;
        }
        resetOverCard();
    }

    public MAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(this.context);
    }


    public int getItemPosition(Object object) {
        for (int i = 0; i < getItemCount(); i++) {
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

    /**
     * 获取item类型
     *
     * @param position
     * @return
     */

    @Override
    public int getItemViewType(int position) {
        Card card = ((Card) get(position));
        resid = card.getResId();
//        Log.d("adaptertest","getItemViewType"+resid);
        return card.getResId() == 0 ? card.getCardType() : card.getResId();
    }

    public GridLayoutManager.SpanSizeLookup SpanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
        @Override
        public int getSpanSize(int position) {
            return MAdapter.this.getSpanSize(position);
        }
    };

    public int getSpanSize(int position) {
        return getList().get(position).getSpan();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        Log.d("test","onCreateViewHolder");
//        Log.d("adaptertest","oncreateviewholder"+resid);
        if (viewType == -88) {
            return new MViewHold(new View(parent.getContext()));
        }
        if (resid != 0) {
            return AutoMViewHold.getView(parent.getContext(), parent, resid);
        }
        return CardIDS.CreateViewHolde(viewType, parent.getContext(), parent);
    }

    private void animateView(final View view, final int position, final RecyclerView.ViewHolder holder, final Card card, Animator[] animators) {
        assert mViewAnimator != null;
        assert recyclerView != null;
        Animator alphaAnimator = ObjectAnimator.ofFloat(view, "alpha", 0, 1);
        Animator[] concatAnimators = AnimatorUtil.concatAnimators(animators, alphaAnimator);
        mViewAnimator.setAnimationDurationMillis(Frame.AnimationDurationMillis);
        mViewAnimator.setAnimationDelayMillis(Frame.AnimationDelayMillis);
        mViewAnimator.animateViewIfNecessary(position, view, holder, card, concatAnimators);
    }


    public Animator[] getAnimators(@NonNull View view, int first, int last, int posion) {
        if (first > posion) {
            return new Animator[]{
                    ObjectAnimator.ofFloat(view, TRANSLATION_Y, -50, 0),
                    ObjectAnimator.ofFloat(view, SCALE_Y, 0.88f, 1f),
                    ObjectAnimator.ofFloat(view, SCALE_X, 0.88f, 1f)
            };
        }
        return new Animator[]{
                ObjectAnimator.ofFloat(view, TRANSLATION_Y, 50, 0),
                ObjectAnimator.ofFloat(view, SCALE_Y, 0.88f, 1f),
                ObjectAnimator.ofFloat(view, SCALE_X, 0.88f, 1f)
        };
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Card card = (Card) get(position);
        card.viewHold = holder;
        card.dispbind(holder, position);
        if (card.getShowType() == 1) {
            return;
        }
        if (!card.useanimation) {
            return;
        }
        if (mViewAnimator == null) {
            return;
        }

        mViewAnimator.cancelExistingAnimation(holder.itemView);

        int first = 0;
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            first = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        }

        if (holder instanceof MViewHold) {
            MViewHold mvh = ((MViewHold) holder);
            if (!card.isanimation) {
                animateView(holder.itemView, position, holder, card, mvh.getAnimators(first, 0, position));
                if (!card.reanimation) {
                    card.isanimation = true;
                }
            }
        } else {
            if (!card.isanimation) {
                animateView(holder.itemView, position, holder, card, getAnimators(holder.itemView, first, 0, position));
                if (!card.reanimation) {
                    card.isanimation = true;
                }
            } else {
                holder.itemView.setRotationX(0);
                holder.itemView.setTranslationY(0);
                holder.itemView.setScaleX(1);
                holder.itemView.setScaleY(1);
                holder.itemView.setAlpha(1);
            }
        }
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (holder instanceof MViewHold) {
            if (((MViewHold) holder).card != null) {
                ((MViewHold) holder).card.viewHold = null;
            }
        }
    }

    public void AddAll(List list) {
        List<Card> added = new ArrayList<>();
        for (Object obj : list) {
            if (obj instanceof Card) {
                Card item = (Card) obj;
                item.isanimation = false;
                item.setAdapter(this);
                item.overViewHold = null;
                added.add(item);
            }
        }
        this.list.addAll(added);
        resetOverCard();
        if (hAdapter != null) {
            hAdapter.mnotifyDataSetChanged();
//            hAdapter.notifyItemRangeInserted(hAdapter.getiPosion(getItemCount() - 1), list.getItemCount());
        } else {
            mnotifyDataSetChanged();
//            this.notifyItemRangeInserted(getItemCount() - 1, list.getItemCount());
        }
    }

    public void AddAllOnBegin(List list) {
        for (Object obj : list) {
            if (obj instanceof Card) {
                Card item = (Card) obj;
                item.isanimation = false;
                item.setAdapter(this);
                item.overViewHold = null;
                this.list.add(0, item);
            }
        }
        resetOverCard();
        if (hAdapter != null) {
            hAdapter.mnotifyDataSetChanged();
//            hAdapter.notifyItemRangeInserted(hAdapter.getiPosion(getItemCount() - 1), list.getItemCount());
        } else {
            mnotifyDataSetChanged();
//            this.notifyItemRangeInserted(getItemCount() - 1, list.getItemCount());
        }
    }

    public void AddAll(MAdapter list) {
        for (int i = 0; i < list.getItemCount(); i++) {
            Card item = (Card) list.get(i);
            item.isanimation = false;
            item.setAdapter(this);
            item.overViewHold = null;
            this.list.add(item);
        }
        resetOverCard();
        if (list.params != null) {
            this.params.putAll(list.params);
        }
        if (hAdapter != null) {
            hAdapter.mnotifyDataSetChanged();
//            hAdapter.notifyItemRangeInserted(hAdapter.getiPosion(getItemCount() - 1), list.getItemCount());
        } else {
            mnotifyDataSetChanged();
//            this.notifyItemRangeInserted(getItemCount() - 1, list.getItemCount());
        }
    }

    public void AddAllOnBegin(MAdapter list) {
        for (int i = list.getItemCount() - 1; i >= 0; i--) {
            Card item = (Card) list.get(i);
            item.isanimation = false;
            item.setAdapter(this);
            item.overViewHold = null;
            this.list.add(0, item);
        }
        if (list.params != null) {
            this.params.putAll(list.params);
        }
        resetOverCard();
        if (hAdapter != null) {
            hAdapter.mnotifyDataSetChanged();
//            hAdapter.notifyItemRangeInserted(hAdapter.getiPosion(0), list.getItemCount());
        } else {
            mnotifyDataSetChanged();
//            this.notifyItemRangeInserted(0, list.getItemCount());
        }
    }


    public boolean addByCheck(Object item) {
        Card card=this.findItem(item);
        if(card==null){
            add(item);
            return true;
        }
        return false;
    }

    public void add(Object item) {
        if (item instanceof Card) {
            ((Card) item).isanimation = false;
            ((Card) item).setAdapter(this);
            ((Card) item).overViewHold = null;
            this.getList().add((Card) item);
        }
        resetOverCard();
        if (hAdapter != null) {
            hAdapter.mnotifyDataSetChanged();
//            hAdapter.notifyItemInserted(hAdapter.getiPosion(this.getItemCount() - 1));
        } else {
            mnotifyDataSetChanged();
            ;
//            this.notifyItemInserted(this.getItemCount() - 1);
        }
    }

    public void add(int ind, Object item) {
        if (item instanceof Card) {
            ((Card) item).isanimation = false;
            ((Card) item).setAdapter(this);
            ((Card) item).overViewHold = null;
            this.getList().add(ind, (Card) item);
        }
        resetOverCard();
        if (hAdapter != null) {
//            if (ind == 0) {
            hAdapter.mnotifyDataSetChanged();
//            } else {
//                hAdapter.notifyItemInserted(hAdapter.getiPosion(ind));
//            }
        } else {
//            if (ind == 0) {
            mnotifyDataSetChanged();
//            } else {
//                this.notifyItemInserted(ind);
//            }
        }
    }

    public void mnotifyDataSetChanged() {
        if (hAdapter != null) {
            hAdapter.mnotifyDataSetChanged();
        } else {
            notifyDataSetChanged();
        }
        if (onNotifyChangedListener != null) {
            onNotifyChangedListener.onNotifyChanged(this);
        }
    }

    public Card findItem(Object item) {
        for (int i = 0; i < getList().size(); i++) {
            if (this.getList().get(i) == item || this.getList().get(i).item == item) {
                return this.getList().get(i);
            }
        }
        return null;
    }

    public void removes(List items) {
        for (Object item : items) {
            for (int i = 0; i < getList().size(); i++) {
                if (this.getList().get(i) == item || this.getList().get(i).item == item) {
                    remove(i);
                    break;
                }
            }
        }
        resetOverCard();
        if (hAdapter != null) {
            hAdapter.mnotifyDataSetChanged();
//            hAdapter.notifyItemRemoved(hAdapter.getiPosion(posion));
        } else {
//            this.notifyItemRemoved(posion);
            mnotifyDataSetChanged();
        }
    }

    public void remove(Object item) {
        for (int i = 0; i < getList().size(); i++) {
            if (this.getList().get(i) == item || this.getList().get(i).item == item) {
                remove(i);
                break;
            }
        }
        resetOverCard();
        if (hAdapter != null) {
            hAdapter.mnotifyDataSetChanged();
//            hAdapter.notifyItemRemoved(hAdapter.getiPosion(posion));
        } else {
//            this.notifyItemRemoved(posion);
            mnotifyDataSetChanged();
        }
    }

    public void remove(int posion) {
        this.getList().remove(posion);
        resetOverCard();
        if (hAdapter != null) {
            hAdapter.mnotifyDataSetChanged();
//            hAdapter.notifyItemRemoved(hAdapter.getiPosion(posion));
        } else {
//            this.notifyItemRemoved(posion);
            mnotifyDataSetChanged();
        }
    }

    public void clear() {
        int size = this.getList().size();
        this.overCard.clear();
        this.getList().clear();
        if (this.mViewAnimator != null) {
            this.mViewAnimator.reset();
        }
        this.params.clear();
        if (hAdapter != null) {
            hAdapter.mnotifyDataSetChanged();
//            hAdapter.notifyItemRangeRemoved(hAdapter.getiPosion(0), size);
        } else {
            mnotifyDataSetChanged();
            this.notifyItemRangeRemoved(0, size);
        }
    }

    public void resetOverCard() {
        this.overCard.clear();
        for (int i = 0; i < list.size(); i++) {
            Card item = (Card) list.get(i);
            item.itemposion = i;
            if (item.getShowType() != 0) {
                overCard.add(item);
            }
        }
    }

    public void setOnNotifyChangedListener(OnNotifyChangedListener onNotifyChangedListener) {
        this.onNotifyChangedListener = onNotifyChangedListener;
    }

    public long getItemId(int position) {
        return 99900000 + position;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public LayoutInflater getLayoutInflater() {
        return layoutInflater;
    }

    public List<Card> getList() {
        return list;
    }

    public <T> T get(int ind) {
        return (T)list.get(ind);
    }

    public <T> T getItem(int ind) {
        return (T) (list.get(ind).item);
    }

    public int getPosion(Object item) {
        for (int i = 0; i < getList().size(); i++) {
            if (this.getList().get(i) == item || this.getList().get(i).item == item) {
                return i;
            }
        }
        return -1;
    }

    public int getRelPosion(Object item) {
        if (hAdapter != null) {
            return hAdapter.getPosion(item);
        } else {
            return getPosion(item);
        }
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        if (recyclerView != null) {
            mViewAnimator = new ViewAnimator(recyclerView);
        }
    }

    public Object getParams(String key) {
        return params.get(key);
    }

}
