package com.mdx.framework.frg.multiplephoto;

import android.content.Context;
import android.database.Cursor;
import androidx.cursoradapter.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.mdx.framework.utility.Device;
import com.mdx.framework.widget.MImageView;

import java.util.ArrayList;

public class ImageCursorAdapter extends CursorAdapter {
    
    public Context mContext;
    
    public ArrayList<Image> checkedList = new ArrayList<Image>();;
    
    public Image img;
    
    public OnClickListener onClickListener;
    
    public OnClickListener onClickBigger;
    
    public OnClickListener mcheckedListener;
    
    public viewHold mvh;
    
    public int maxSize;
    
    public static class Image {
        
        public String name;
        
        public String data;
        
        public String dir;
        
        public String id;
        
        public Image() {
        }
    }
    
    public static class viewHold {
        
        public Image image;
        
        public MImageView imageView;
        
        public CheckBox checkbox;
        
        public View view;
        
        public TextView tv;
        
        public viewHold(MImageView image, CheckBox checkbox, View view) {
            imageView = image;
            this.checkbox = checkbox;
            this.view = view;
        }
    }
    
    public ImageCursorAdapter(Context context, Cursor cursor, OnClickListener onClickListener,
            OnClickListener onClickBigger, OnClickListener checkedListener) {
        super(context, cursor, false);
        mContext = null;
        img = new Image();
        maxSize = 0x7fffffff;
        mContext = context;
        this.onClickListener = onClickListener;
        this.onClickBigger = onClickBigger;
        mcheckedListener = checkedListener;
    }
    
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater vi = LayoutInflater.from(context);;
        View view = vi.inflate(com.mdx.framework.R.layout.default_item_image_sel, parent, false);
        viewHold vh = new viewHold((MImageView) view.findViewById(com.mdx.framework.R.id.image),
                (CheckBox) view.findViewById(com.mdx.framework.R.id.checkbox),
                view.findViewById(com.mdx.framework.R.id.maxview));
        vh.tv = (TextView) view.findViewById(com.mdx.framework.R.id.tv);
        view.setTag(vh);
        return view;
    }
    
    @SuppressWarnings("deprecation")
    public void bindView(View view, Context context, Cursor cursor) {
        final viewHold vh = (viewHold) view.getTag();
        if (vh == mvh)
            mvh = null;
        if (cursor == null) {
            mvh = vh;
            vh.imageView.setObj(null);
            if (this.img.data != null) {
                vh.imageView.setObj((new StringBuilder("file:")).append(this.img.data).toString());
                vh.view.setEnabled(true);
            } else {
                if (isMax())
                    vh.view.setEnabled(false);
                else
                    vh.view.setEnabled(true);
            }
            this.img.id = null;
            vh.tv.setText("");
            vh.view.setBackgroundResource(com.mdx.framework.R.drawable.default_bt_paizhao);
            vh.checkbox.setVisibility(View.GONE);
            vh.imageView.setDefault(context.getResources()
                    .getDrawable(com.mdx.framework.R.drawable.default_bg_translate));
            if (onClickListener != null)
                vh.view.setOnClickListener(onClickListener);
            return;
        }
        final Image img = new Image();
        vh.checkbox.setVisibility(View.VISIBLE);
        vh.view.setBackgroundColor(0);
        vh.view.setOnClickListener(null);
        vh.imageView.setDefault(context.getResources().getDrawable(com.mdx.framework.R.drawable.default_df_img_load));
        img.data = cursor.getString(cursor.getColumnIndex("_data"));
        img.name = cursor.getString(cursor.getColumnIndex("_display_name"));
        img.id = cursor.getString(cursor.getColumnIndex("_id"));
        img.dir = cursor.getString(cursor.getColumnIndex("bucket_display_name"));
        vh.checkbox.setOnCheckedChangeListener(null);
        Image himg = null;
        for (Image i : checkedList) {
            if (img.id.equals(i.id)) {
                himg = i;
                break;
            }
        }
        
        if (isMax())
            vh.checkbox.setEnabled(false);
        else
            vh.checkbox.setEnabled(true);
        if (himg == null) {
            vh.checkbox.setChecked(false);
        } else {
            vh.checkbox.setChecked(true);
            vh.checkbox.setEnabled(true);
            vh.view.setBackgroundColor(0x88000000);
        }
        vh.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    vh.view.setBackgroundColor(0x88000000);
                else
                    vh.view.setBackgroundColor(0);
                setChecked(img, isChecked);
                mcheckedListener.onClick(buttonView);
                if (maxSize <= checkedList.size())
                    notifyDataSetChanged();
                else
                    notifyDataSetChanged();
            }
        });
        if (onClickBigger != null) {
            vh.view.setTag(img);
            vh.view.setOnClickListener(onClickBigger);
        }
        vh.tv.setText("");
        vh.imageView.setUserAnim(false);
        if (Device.getSdkVersion() < 16)
            vh.imageView.setObj((new StringBuilder("file:")).append(img.data).toString());
        else
            vh.imageView.setObj((new StringBuilder("MEDIA:")).append(img.id).toString());
    }
    
    private boolean isMax() {
        return maxSize - (img.data == null ? 0 : 1) <= checkedList.size();
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    private void setChecked(Image img, boolean check) {
        Image himg = null;
        for (Image i : checkedList) {
            if (img.id.equals(i.id)) {
                himg = i;
                break;
            }
        }
        
        if (check) {
            if (himg == null)
                checkedList.add(img);
        } else if (himg != null)
            checkedList.remove(himg);
    }
    
    public Object getItem(int position) {
        if (position == 0)
            return null;
        position--;
        if (mDataValid && mCursor != null) {
            mCursor.moveToPosition(position);
            return mCursor;
        } else {
            return null;
        }
    }
    
    public long getItemId(int position) {
        if (position == 0)
            return 0L;
        position--;
        if (mDataValid && mCursor != null) {
            if (mCursor.moveToPosition(position))
                return mCursor.getLong(mRowIDColumn);
            else
                return 0L;
        } else {
            return 0L;
        }
    }
    
    public int getCount() {
        if (mDataValid && mCursor != null)
            return mCursor.getCount() + 1;
        else
            return 0;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        if (!mDataValid)
            throw new IllegalStateException("this should only be called when the cursor is valid");
        if (position > 0 && !mCursor.moveToPosition(position - 1))
            throw new IllegalStateException((new StringBuilder("couldn't move cursor to position ")).append(position)
                    .toString());
        View v;
        if (convertView == null)
            v = newView(mContext, position != 0 ? mCursor : null, parent);
        else
            v = convertView;
        bindView(v, mContext, position != 0 ? mCursor : null);
        return v;
    }
    
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (mDataValid) {
            if (position > 0)
                mCursor.moveToPosition(position - 1);
            View v;
            if (convertView == null)
                v = newDropDownView(mContext, position != 0 ? mCursor : null, parent);
            else
                v = convertView;
            bindView(v, mContext, position != 0 ? mCursor : null);
            return v;
        } else {
            return null;
        }
    }
    
    
}
