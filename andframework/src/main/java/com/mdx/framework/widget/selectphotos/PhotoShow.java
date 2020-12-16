package com.mdx.framework.widget.selectphotos;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mdx.framework.R;
import com.mdx.framework.activity.BaseActivity;
import com.mdx.framework.autofit.AutoFitBase;
import com.mdx.framework.autofit.AutoFitDialog;
import com.mdx.framework.autofit.commons.FitPost;
import com.mdx.framework.utility.Device;
import com.mdx.framework.widget.pagerecycleview.ada.CardAdapter;
import com.mdx.framework.widget.pagerecycleview.autofit.AutoDataFormat;
import com.mdx.framework.widget.selectphotos.utils.PhotosUtil;
import com.mdx.framework.widget.systembartint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ryan on 2017/12/29.
 */

public class PhotoShow extends AutoFitDialog implements CompoundButton.OnCheckedChangeListener {
    public PhotosUtil photosUtil;
    public PhotosUtil.Folder nowfolder;
    public com.mdx.framework.widget.MViewPager pager;
    public com.mdx.framework.widget.viewpagerindicator.UnderlinePageIndicator indicator;
    public TextView submit;
    public CheckBox checkbox;
    public Context activity;
    public RelativeLayout actionbar;
    public LinearLayout navbar;
    public List<PhotosUtil.ImageItem> list = new ArrayList<>();
    public PhotosUtil.ImageItem imageItem;
    public int curritem = 0;
    public com.mdx.framework.autofit.layout.FitLinearLayout contextview;
    public RecyclerView recycleview;
    public CardAdapter cardAdapter;
    public ImageButton back;
    public SelectPhoto selectPhoto;

    public PhotoShow(Context context, int resid, Map<String, Object> framagemap, AutoFitBase parentfit, PhotosUtil.ImageItem imageItem) {
        super(context, R.style.full_dialog, resid, framagemap, parentfit);
        this.activity = context;
        this.imageItem = imageItem;
    }


    @Override
    protected void create(Bundle savedInstanceState) {
        contentView = inflater.inflate(resourceid, null);
        photosUtil = (PhotosUtil) framagemap.get("photos");
        nowfolder = (PhotosUtil.Folder) framagemap.get("folder");
        setContentView(contentView);
        this.setContentView(R.layout.default_selphoto_dia_show_photo);
        initDia();
        this.setId("showphoto");
        list.addAll(nowfolder.images);
        for (PhotosUtil.ImageItem item : photosUtil.selecteds) {
            if (!list.contains(item)) {
                list.add(item);
            }
        }
        if (list.get(0).iscam && list.get(0).path == null) {
            list.remove(0);
        }
        curritem = list.indexOf(imageItem);
        findVMethod();
        setSubmit(null, false);
    }


    public void loadData() {
        autoFit.setAdapter(R.id.pager, R.layout.default_selphoto_item_image_show, list);
        pager.setCurrentItem(curritem);
        checkbox.setChecked(imageItem.ischecked);
        photosUtil.checkedImage = imageItem;
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                PhotosUtil.ImageItem item = list.get(position);
                setcheckbox(item.ischecked);
                setSelectChecked();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        checkbox.setOnCheckedChangeListener(this);
        this.cardAdapter = new AutoDataFormat(R.layout.default_selphoto_item_selimage_show, autoFit).getAda(getContext(), photosUtil.selecteds);
        if (photosUtil.selecteds.size() == 0) {
            recycleview.setVisibility(View.GONE);
        } else {
            recycleview.setVisibility(View.VISIBLE);
        }
        recycleview.setAdapter(cardAdapter);
    }

    public void setcheckbox(boolean bol) {
        checkbox.setOnCheckedChangeListener(null);
        checkbox.setChecked(bol);
        checkbox.setOnCheckedChangeListener(this);
    }

    private void findVMethod() {
        pager = (com.mdx.framework.widget.MViewPager) findViewById(R.id.pager);
        indicator = (com.mdx.framework.widget.viewpagerindicator.UnderlinePageIndicator) findViewById(R.id.indicator);
        submit = (TextView) findViewById(R.id.submit);
        checkbox = (CheckBox) findViewById(R.id.checkbox);
        actionbar = (RelativeLayout) findViewById(R.id.actionbar);
        navbar = (LinearLayout) findViewById(R.id.navbar);
        contextview = (com.mdx.framework.autofit.layout.FitLinearLayout) findViewById(R.id.contextview);
        recycleview = (RecyclerView) findViewById(R.id.recycleview);
        back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                selectPhoto.Submit();

            }
        });

        recycleview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        if (activity instanceof BaseActivity) {
            int navigationbarh = ((BaseActivity) activity).getNavbarH();
            int statush = ((BaseActivity) activity).getStatusH();
            contextview.setPadding(0, statush, 0, navigationbarh);
        } else if (activity instanceof Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // 判断
                // 系统版本
                SystemBarTintManager tintManager = new SystemBarTintManager((Activity) activity);

                tintManager.getConfig().getPixelInsetBottom();
                tintManager.getConfig().getPixelInsetTop(false);

                int navigationbarh = tintManager.getConfig().getNavigationBarHeight();
                int statush = tintManager.getConfig().getStatusBarHeight();
                contextview.setPadding(0, statush, 0, navigationbarh);
            }
        }
        loadData();
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean bol) {
        PhotosUtil.ImageItem item = list.get(pager.getCurrentItem());
        item.setCheck(buttonView, bol);
        CardAdapter ada = new AutoDataFormat(R.layout.default_selphoto_item_selimage_show, autoFit).getAda(getContext(), photosUtil.selecteds);
        cardAdapter.getList().clear();
        cardAdapter.getList().addAll(ada.getList());
        cardAdapter.notifyDataSetChanged();
        if (photosUtil.selecteds.size() == 0) {
            recycleview.setVisibility(View.GONE);
        } else {
            recycleview.setVisibility(View.VISIBLE);
        }
        setSelectChecked();
        setSubmit(buttonView, bol);
    }

    public void setSelectChecked() {
        PhotosUtil.ImageItem selitem = list.get(pager.getCurrentItem());
        photosUtil.checkedImage = selitem;
        int pos = 0;
        boolean isfind = false;
        int ind = cardAdapter.getItemPosition(selitem);
        if (ind > 0) {
            pos = ind;
            isfind = true;
        }
        cardAdapter.notifyDataSetChanged();
        if (isfind) {
            recycleview.smoothScrollToPosition(pos);
        }
        autoFit.sent("SelectPhoto", 56);
    }


    public void setSubmit(CompoundButton buttonView, boolean isChecked) {
        String str = getContext().getResources().getString(R.string.submit);
        submit.setText(str + "(" + photosUtil.selecteds.size() + "/" + photosUtil.maxselected + ")");
    }


    @Override
    public void disposeMsg(int type, Object obj) {
        super.disposeMsg(type, obj);
        if (type == 33) {
            FitPost post = (FitPost) obj;
            Object object = post.get("item");
            pager.setCurrentItem(list.indexOf(object), false);
        }
    }

}
