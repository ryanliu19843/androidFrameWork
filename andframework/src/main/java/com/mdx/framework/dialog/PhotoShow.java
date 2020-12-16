/*
 * 文件名: PhotoShow.java 版 权： Copyright XCDS Tech. Co. Ltd. All Rights Reserved. 描
 * 述: [该类的简要描述] 创建人: ryan 创建时间:2014年5月14日
 *
 * 修改人： 修改时间: 修改内容：[修改内容]
 */
package com.mdx.framework.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mdx.framework.Frame;
import com.mdx.framework.R;
import com.mdx.framework.activity.BaseActivity;
import com.mdx.framework.adapter.MPagerAdapter;
import com.mdx.framework.cache.ImageStoreCacheManage;
import com.mdx.framework.commons.verify.Md5;
import com.mdx.framework.prompt.MDialog;
import com.mdx.framework.server.image.ImageReadFactory;
import com.mdx.framework.server.image.impl.MBitmap;
import com.mdx.framework.utility.BitmapRead;
import com.mdx.framework.utility.Device;
import com.mdx.framework.utility.Helper;
import com.mdx.framework.utility.Util;
import com.mdx.framework.widget.MImageView;
import com.mdx.framework.widget.MImageView.OnImageLoaded;
import com.mdx.framework.widget.systembartint.SystemBarTintManager;
import com.mdx.framework.widget.transforms.TransForms;
import com.mdx.framework.widget.viewpagerindicator.IconPagerAdapter;
import com.mdx.framework.widget.viewpagerindicator.UnderlinePageIndicator;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import pl.droidsonroids.gif.GifDrawable;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 *
 * @author ryan
 * @version [2014年5月14日 下午5:39:52]
 */
public class PhotoShow extends MDialog implements View.OnTouchListener, View.OnClickListener,
        ViewPager.OnPageChangeListener {
    private ViewPager mPager;

    private List<TitlePhoto> dataList;

    private int ind = 0;

    private boolean isshowMark = false;

    private int transforms = 0;

    private RelativeLayout mcontrollayout;

    private View mSend, mDownload, mpopCont, mbottomLayout, mtoplayout, mback, mmore;

    private TextView mtitle, mmark;

    public PhotoShow(Context context, List<String> list) {
        super(context, R.style.full_dialog);
        ArrayList<TitlePhoto> string = new ArrayList<TitlePhoto>();
        for (String p : list) {
            TitlePhoto tp = new TitlePhoto();
            tp.url = p;
            string.add(tp);
        }
        dataList = string;
        ind = 0;
    }

    public PhotoShow(Context context, List<String> list, String sind) {
        super(context, R.style.full_dialog);
        ArrayList<TitlePhoto> string = new ArrayList<TitlePhoto>();
        for (String p : list) {
            TitlePhoto tp = new TitlePhoto();
            tp.url = p;
            string.add(tp);
        }
        dataList = string;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(sind)) {
                ind = i;
                break;
            }
        }
    }

    public PhotoShow(Context context, String photos, String sind) {
        super(context, R.style.full_dialog);
        String[] photol = photos.split(",");
        ArrayList<TitlePhoto> string = new ArrayList<TitlePhoto>();
        for (String p : photol) {
            TitlePhoto tp = new TitlePhoto();
            tp.url = p;
            string.add(tp);
        }
        dataList = string;
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).equals(sind)) {
                ind = i;
                break;
            }
        }
    }

    public PhotoShow(Context context, String photos) {
        super(context, R.style.full_dialog);
        String[] photol = photos.split(",");
        ArrayList<TitlePhoto> string = new ArrayList<TitlePhoto>();
        for (String p : photol) {
            TitlePhoto tp = new TitlePhoto();
            tp.url = p;
            string.add(tp);
        }
        dataList = string;
    }

    public PhotoShow(Context context, boolean cancelable, OnCancelListener cancelListener, ArrayList<String> list) {
        super(context, cancelable, cancelListener);
        ArrayList<TitlePhoto> string = new ArrayList<TitlePhoto>();
        for (String p : list) {
            TitlePhoto tp = new TitlePhoto();
            tp.url = p;
            string.add(tp);
        }
        dataList = string;
    }

    public PhotoShow(Context context, int theme, ArrayList<String> list) {
        super(context, theme);
        ArrayList<TitlePhoto> string = new ArrayList<TitlePhoto>();
        for (String p : list) {
            TitlePhoto tp = new TitlePhoto();
            tp.url = p;
            string.add(tp);
        }
        dataList = string;
    }

    @Override
    protected void create(Bundle savedInstanceState) {
        setContentView(R.layout.photoshow_dia);
        mcontrollayout = (RelativeLayout) findViewById(R.id.controllayout);
        mSend = findViewById(R.id.send);
        mDownload = findViewById(R.id.download);
        mmark = (TextView) findViewById(R.id.mark);
        mtitle = (TextView) findViewById(R.id.title);
        mpopCont = findViewById(R.id.popCont);
        mbottomLayout = findViewById(R.id.bottomLayout);
        mtoplayout = findViewById(R.id.toplayout);
        mback = findViewById(R.id.back);
        mmore = findViewById(R.id.more);

        mback.setOnClickListener(this);
        mpopCont.setOnTouchListener(this);
        mmore.setOnClickListener(this);
        mSend.setOnClickListener(this);
        mDownload.setOnClickListener(this);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new PhotoAdapter(getContext(), dataList));

        mPager.addOnPageChangeListener(this);

        Activity activity = null;
        if (getContext() instanceof ContextThemeWrapper) {
            ContextThemeWrapper ctw = (ContextThemeWrapper) getContext();
            activity = (Activity) ctw.getBaseContext();
        }

        if (activity instanceof BaseActivity) {
            mcontrollayout.setPadding(0, ((BaseActivity) activity).getStatusH(), 0, 0);
            mmark.setPadding(0, 0, 0, ((BaseActivity) activity).getNavbarH());
            LayoutParams lp = mbottomLayout.getLayoutParams();
            lp.height = lp.height + ((BaseActivity) activity).getNavbarH();
            mbottomLayout.setLayoutParams(lp);
        } else if (activity instanceof Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // 判断
                // 系统版本
                SystemBarTintManager tintManager = new SystemBarTintManager(activity);

                tintManager.getConfig().getPixelInsetBottom();
                tintManager.getConfig().getPixelInsetTop(false);

                int navigationbarh = tintManager.getConfig().getNavigationBarHeight();
                int statush = tintManager.getConfig().getStatusBarHeight();
                mcontrollayout.setPadding(0, statush, 0, 0);
                mmark.setPadding(0, 0, 0, navigationbarh);
                LayoutParams lp = mbottomLayout.getLayoutParams();
                lp.height = lp.height + navigationbarh;
                mbottomLayout.setLayoutParams(lp);
            }
        }

        setTransforms(transforms);
        UnderlinePageIndicator indicator = (UnderlinePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        mPager.setCurrentItem(ind, false);
        if (dataList.size() > 0) {
            TitlePhoto item = dataList.get(0);
            if (item.title == null) {
                mbottomLayout.setVisibility(View.GONE);
                isshowMark = false;
            } else {
                mbottomLayout.setVisibility(View.VISIBLE);
                isshowMark = true;
                mtitle.setText(item.title == null ? "" : item.title);
                mmark.setText(item.mark == null ? "" : item.title);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back) {
            dismiss();
        } else if (v.getId() == R.id.more) {
            mpopCont.setVisibility(View.VISIBLE);
            ViewPropertyAnimator.animate(mpopCont).alpha(1).setDuration(200).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                }
            });
        } else if (v.getId() == R.id.send) {
            TitlePhoto item = dataList.get(mPager.getCurrentItem());
            pshow(false);
            mAsyncTask mat = new mAsyncTask();
            mat.execute(item.url);
            ViewPropertyAnimator.animate(mpopCont).alpha(0).setDuration(200).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mpopCont.setVisibility(View.GONE);
                }
            });
        } else if (v.getId() == R.id.download) {
            TitlePhoto item = dataList.get(mPager.getCurrentItem());
            pshow(false);
            mAsyncTask mat = new mAsyncTask();

            mat.execute(item.url, Util.getPath(Frame.CONTEXT, "imagesave", Md5.mD5(item.url) + ".jpg").toString());
            ViewPropertyAnimator.animate(mpopCont).alpha(0).setDuration(200).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mpopCont.setVisibility(View.GONE);
                }
            });
        } else {
            if (mtoplayout.getVisibility() == View.VISIBLE) {
                ViewPropertyAnimator.animate(mtoplayout)
                        .alpha(0)
                        .setDuration(200)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                mtoplayout.setVisibility(View.GONE);
                            }
                        });
                if (isshowMark) {
                    ViewPropertyAnimator.animate(mbottomLayout)
                            .alpha(0)
                            .setDuration(200)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    mbottomLayout.setVisibility(View.GONE);
                                }
                            });
                }
            } else {
                mtoplayout.setVisibility(View.VISIBLE);
                ViewPropertyAnimator.animate(mtoplayout).alpha(1).setDuration(200).setListener(null);
                if (isshowMark) {
                    mbottomLayout.setVisibility(View.VISIBLE);
                    ViewPropertyAnimator.animate(mbottomLayout).alpha(1).setDuration(200).setListener(null);
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.popCont) {
            ViewPropertyAnimator.animate(mpopCont).alpha(0).setDuration(200).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mpopCont.setVisibility(View.GONE);
                }
            });
        }
        return true;
    }

    public static class TitlePhoto {
        public String url;

        public String title;

        public String mark;
    }

    public class PhotoAdapter extends MPagerAdapter<TitlePhoto> implements IconPagerAdapter {
        public PhotoAdapter(Context context, List<TitlePhoto> list) {
            super(context, list);
        }

        @Override
        public int getIconResId(int index) {
            return 0;
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(int position, View convertView) {
            TitlePhoto tp = get(position);
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                LayoutInflater f = LayoutInflater.from(getContext());
                convertView = f.inflate(R.layout.photoshow_item, null);
                holder.img = (MImageView) convertView.findViewById(R.id.photoshow_imageview);
                holder.process = (ProgressBar) convertView.findViewById(R.id.photoshow_processbar);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.process.setVisibility(View.VISIBLE);
            final ViewHolder hold = holder;
            holder.img.setOnImageLoaded(new OnImageLoaded() {

                @Override
                public void onImageLoaded(Object obj, Drawable drawable, MBitmap mBitmap, int size, int length) {
                    hold.process.setVisibility(View.GONE);
                }
            });
            holder.img.setScaleAble(true);
            holder.img.setObj(tp.url);
            holder.img.setOnClickListener(PhotoShow.this);
            return convertView;
        }

        public final class ViewHolder {
            public MImageView img;

            public ProgressBar process;
        }
    }

    public int getTransforms() {
        return transforms;
    }

    public void setTransforms(int transforms) {
        this.transforms = transforms;
        if (mPager != null) {
            mPager.setPageTransformer(true, TransForms.getTransForms(transforms));
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    private class mAsyncTask extends AsyncTask<String/* 参数1 */, String/* 参数2 */, String/* 参数3 */> {
        public boolean isSend = true;

        @Override
        protected String doInBackground(String... params/* 参数1 */) {
            String url = Util.getUrl(params[0]);
            if (params.length > 1) {
                isSend = false;
            }

            if (Util.isFileUrl(url)) {
                return url;
            }

            if (params.length == 1) {
                if (!Util.isMedia(url) && !Util.isJarUrl(url) && !Util.isAssets(url)) {
                    File f = ImageStoreCacheManage.getBig(url, 0, 0);
                    if (f != null) {
                        return f.toString();
                    }
                }
            }

            File file = Util.getPath(Frame.CONTEXT, "temp", Md5.mD5(url) + ".jpgtmp");
            if (params.length > 1) {
                file = new File(params[1]);
            }

            if (file.exists()) {
                return file.toString();
            }
            Object bit;
            try {
                bit = ImageReadFactory.createImageRead(url, url, 0, true).loadImageFromUrl(1080, 0);
                if (bit instanceof Bitmap) {
                    BitmapRead.saveImage((Bitmap) bit, new FileOutputStream(file));
                } else if (bit instanceof GifDrawable) {
                    BitmapRead.saveImage((Bitmap) ((GifDrawable) bit).seekToFrameAndGet(0), new FileOutputStream(file));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return file.toString();
        }

        @Override
        protected void onPostExecute(String result/* 参数3 */) {
            super.onPostExecute(result);
            pdismiss(false);
            if (!result.toLowerCase(Locale.ENGLISH).startsWith("file:")) {
                result = "file:" + result;
            }
            if (!isSend) {
                Helper.toast(getContext().getResources().getText(R.string.save_success), getContext());
                getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(result)));
                return;
            }

            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(result));
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "share.jpg");
            shareIntent.putExtra(Intent.EXTRA_TITLE, "share.jpg");
            shareIntent.setType("image/*");
            try {
                getContext().startActivity(Intent.createChooser(shareIntent, getContext().getResources().getText(R.string.send)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onPageSelected(int arg0) {
        TitlePhoto item = dataList.get(arg0);
        if (item.title == null) {
            mbottomLayout.setVisibility(View.GONE);
            isshowMark = false;
        } else {
            mbottomLayout.setVisibility(View.VISIBLE);
            isshowMark = true;
            mtitle.setText(item.title == null ? "" : item.title);
            mmark.setText(item.mark == null ? "" : item.title);
        }
    }
}
