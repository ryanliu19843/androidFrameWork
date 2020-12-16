package com.mdx.framework.widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.annotation.IntRange;

import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.mdx.framework.Frame;
import com.mdx.framework.R;
import com.mdx.framework.activity.IActivity;
import com.mdx.framework.broadcast.BIntent;
import com.mdx.framework.broadcast.CReceiver;
import com.mdx.framework.broadcast.OnReceiverListener;
import com.mdx.framework.config.ImageConfig;
import com.mdx.framework.server.image.ImageLoad;
import com.mdx.framework.server.image.impl.ImageBase;
import com.mdx.framework.server.image.impl.MBitmap;
import com.mdx.framework.server.image.impl.MGifDrawable;
import com.mdx.framework.widget.photoview.PhotoViewAttacher;

import java.util.HashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import pl.droidsonroids.gif.GifDrawable;

@SuppressLint("AppCompatCustomView")
@SuppressWarnings("deprecation")
public class MImageView extends ImageView implements ImageBase, OnReceiverListener {
    public final ScheduledThreadPoolExecutor mExecutor = new ScheduledThreadPoolExecutor(1);
    public static final int STATE_SELPRESSED = 3;
    public static final int STATE_PRESSED = 1;
    public static final int STATE_SELECTED = 2;
    public static final int STATE_NOMORE = 0;
    protected Object obj = null;
    protected ImageLoad imageload = Frame.IMAGELOAD;
    protected boolean change = false, defaultDrawableLoaded = false, userAnim = true, animed = false;
    protected boolean loaded = false;
    public boolean isPalette = false;
    protected boolean loading = false, reload = false, mUseCache = true, isFirstLoading = true, canScale = false, isFirstDraw = true, autosize = true;
    protected int mImageLoadType = ImageConfig.getImageLoadType();
    protected Drawable defaultDrawable, ndrawable;
    protected String mLoadingId = "";
    protected PhotoViewAttacher mPhotoViewAttacher;
    protected OnImageLoaded onImageLoaded;
    protected OnClickListener onClickListener;
    protected OnTouchListener onTouchListener;
    protected int blur = 0, round = 0, imgWidth = -1, imgHeight = -1;
    protected boolean isCircle = false;
    private CReceiver recyclecr;
    private int mTocolor = 0xff000000, mSelcolor = 0xff000000, mSelClickcolor = 0x00222222, mClickColor = 0x00222222, mclickflg = -1;
    private HashMap<Integer, ColorFilter> colorFilters = new HashMap<>();
    private int lastwidth, lastheight;
    private PorterDuff.Mode mChcolorMode, mSelChcolorMode;
    public MBitmap mBitmap;
    public long lastsettime = 0;
    private Path mPath = new Path();
    private int drawable_state = STATE_NOMORE;

    public MImageView(Context context) {
        super(context);
        defaultDrawable = context.getResources().getDrawable(R.drawable.default_image);
        setClickColor(mClickColor, mclickflg);
        initImage();
    }

    public void setChecked(boolean checked) {
        this.setSelected(checked);
    }

    public boolean isChecked(){
        return this.isSelected();
    }

    public MImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Mimage, defStyle, 0);
        mTocolor = a.getColor(R.styleable.Mimage_toColor, 0xff000000);
        mClickColor = a.getColor(R.styleable.Mimage_clickColor, 0x00222222);
        mclickflg = a.getColor(R.styleable.Mimage_clickFlag, -1);
        round = a.getInt(R.styleable.Mimage_round, 0);
        blur = a.getInt(R.styleable.Mimage_blur, 0);
        setChecked(a.getBoolean(R.styleable.Mimage_checked, false));
        isCircle = a.getBoolean(R.styleable.Mimage_iscircle, false);
        canScale = a.getBoolean(R.styleable.Mimage_canScale, false);
        int resid = a.getResourceId(R.styleable.Mimage_obj, 0);
        Object obj = resid;
        if (resid == 0) {
            obj = a.getString(R.styleable.Mimage_obj);
        }
        if (getDrawable() != null) {
            defaultDrawable = getDrawable();
        } else if (a.hasValue(R.styleable.Mimage_defaultdrawable)) {
            defaultDrawable = a.getDrawable(R.styleable.Mimage_defaultdrawable);
        } else {
            defaultDrawable = context.getResources().getDrawable(R.drawable.default_image);
        }
        if (defaultDrawable != null) {
            defaultDrawableLoaded = true;
        }
        a.recycle();
        if (mTocolor != 0xff000000) {
            setTocolor(mTocolor);
        }
        setClickColor(mClickColor, mclickflg);
        initImage();
        if (obj != null) {
            this.setObj(obj);
        }

    }

    public void initImage() {
        setDefault();
    }


    public void setObj(Object obj, int imgw, int imgh) {
        this.setImageSize(imgw, imgh);
        setObj(obj, true);
    }

    public void setObj(Object obj) {
        this.setImageSize(-1, -1);
        setObj(obj, true);
    }

    public void setObj(Object obj, boolean bol) {
        this.isPause = false;
        this.isStop = false;
        this.loopcount = 0;
        setLoading(false);
        this.ndrawable = null;
        this.mBitmap = null;
        animed = false;
        if (canScale) {
            if (mPhotoViewAttacher != null) {
                mPhotoViewAttacher.cleanup();
            }
        }
        isFirstLoading = true;
        if (obj != null && obj.toString().length() > 0) {
            setLoaded(false);
            reload = false;
            if (obj.equals(this.obj)) {
                if (this.getLayoutParams() == null || this.getLayoutParams().width == LayoutParams.WRAP_CONTENT || this.getLayoutParams().height == LayoutParams.WRAP_CONTENT) {
                    toimageload();
                }
                return;
            } else {
                this.obj = obj;
                change = true;
                imageload.stop(this);
            }
        } else {
            this.obj = null;
        }
        if (bol) {
            setDefault();
        }
        if (this.getLayoutParams() == null || this.getLayoutParams().width == LayoutParams.WRAP_CONTENT || this.getLayoutParams().height == LayoutParams.WRAP_CONTENT) {
            toimageload();
        }
        invalidate();
    }

    @Override
    protected void onAttachedToWindow() {
        if (recyclecr != null) {
            recyclecr.unRegedit();
        }
        recyclecr = new CReceiver("com.framework.ImageRecycle", getLoadContext(), null, getLoadContext(), this);
        recyclecr.regedit();
        recyclecr.id = obj;
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        if (recyclecr != null) {
            recyclecr.unRegedit();
            recyclecr = null;
        }
        if (canScale) {
            if (mPhotoViewAttacher != null) {
                mPhotoViewAttacher.cleanup();
            }
        }
        imageload.stop(this);
        Drawable drawable = ndrawable == null ? (defaultDrawable == null ? getDrawable() :
                defaultDrawable) : ndrawable;
        if (drawable != null && drawable.getCallback() == this) {
            drawable.setCallback(null);
        }
        super.onDetachedFromWindow();
    }

    private void setLoaded(boolean bol) {
        loaded = bol;
    }

    private void setLoading(boolean bol) {
        loading = bol;
    }

    /**
     * 重新加载
     *
     * @throws
     * @author ryan
     * @Title: reload
     * @Description: TODO
     */
    public void reload() {
        imageload.stop(this);
        if (this.obj != null && this.obj.toString().length() > 0) {
            setLoaded(false);
            setLoading(false);
            reload = true;
            this.invalidate();
        }
    }

    /**
     * 设置显示图片
     *
     * @param drawable
     * @throws
     * @author ryan
     * @Title: setImage
     * @Description: TODO
     */
    public void setImage(Drawable drawable) {
        if (drawable != null) {
            this.ndrawable = drawable;
            this.setImageDrawable(drawable);
            ndrawable.setCallback(this);
            initColorFilter();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private Drawable toimageload() {
        synchronized (this) {
            if (!defaultDrawableLoaded) {
                if (getDrawable() != null) {
                    defaultDrawable = getDrawable();
                }
                defaultDrawableLoaded = true;
            }
            if (obj != null) {
                MBitmap bitmap = imageload.get(this);
                this.mBitmap = bitmap;
                Drawable drawable = null;
                if (!loaded && !loading) {
                    if (bitmap == null || reload) {
                        setLoading(true);
                        loadimage(obj);
                    } else {
                        setLoaded(true);
                        drawable = bitmap.getDrawable();
                        if (ndrawable instanceof MGifDrawable) {
                            ((MGifDrawable) ndrawable).setBlur(blur);
                        }
                        if (this.onImageLoaded != null) {
                            onImageLoaded.onImageLoaded(this.getObj(), drawable, mBitmap, 100, 100);
                        }
                        setDrawable(drawable);

                        if (canScale) {
                            if (mPhotoViewAttacher != null) {
                                mPhotoViewAttacher.cleanup();
                            }
                            mPhotoViewAttacher = new PhotoViewAttacher(MImageView.this);
                        }
                    }
                }
                return drawable;
            }
            return null;
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (this.mBitmap != null) {
            synchronized (mBitmap) {
                if (mBitmap.isRecycled()) {
                    setLoaded(false);
                    reload = false;
                    ndrawable = null;
                    setDefault();
                }
                try {
                    if (isFirstDraw) {
                        isFirstDraw = false;
                        if (this.onImageLoaded != null) {
                            onImageLoaded.onImageLoaded(this.getObj(), getDrawable(), mBitmap, 100, 100);
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
        try {
            setmpath(canvas);
            super.onDraw(canvas);
        } catch (Exception e) {

        }
        if (this.mBitmap == null || this.mBitmap.isRecycled()) {   //如果图片被释放则调用接口
            toimageload();
        }
    }

    private void setmpath(Canvas canvas) {
        if (isCircle) {
            mPath.reset();
            int w = this.getWidth();
            int h = this.getHeight();
            int bj = w;
            if (h < w) {
                bj = h;
            }
            mPath.addCircle(this.getWidth() / 2, this.getHeight() / 2, bj / 2, Path.Direction.CW);
        } else if (round > 0) {
            mPath.reset();
            mPath.addRoundRect(new RectF(0, 0, getWidth(), getHeight()), round * 1f, round * 1f, Path.Direction.CW);
        }
        if (isCircle || round > 0) {
            canvas.clipPath(mPath);
        }
    }

    /**
     * 暂停播放动画
     */
    private boolean isPause = false;

    public void pause() {
        Drawable drawable = ndrawable == null ? (defaultDrawable == null ? getDrawable() : defaultDrawable) : ndrawable;
        if (drawable instanceof GifDrawable) {
            ((GifDrawable) drawable).pause();
        }
        isPause = true;
    }

    /**
     * 结束动画
     *
     * @param autorun 是否自动动画
     */
    private boolean isStop = false;

    public void stop(boolean autorun) {
        Drawable drawable = ndrawable == null ? (defaultDrawable == null ? getDrawable() : defaultDrawable) : ndrawable;
        if (drawable instanceof GifDrawable) {
            ((GifDrawable) drawable).reset(autorun);
        }
        isStop = true;
        isPause = !autorun;
    }

    /**
     * 开始播放动画
     */
    public void start() {
        Drawable drawable = ndrawable == null ? (defaultDrawable == null ? getDrawable() : defaultDrawable) : ndrawable;
        if (drawable instanceof GifDrawable) {
            GifDrawable gif = (GifDrawable) drawable;
            if (gif.getLoopCount() <= 0) {
                gif.start();
                return;
            }
            if (gif.getCurrentFrameIndex() == gif.getNumberOfFrames() - 1 && gif.getLoopCount() - 1 == gif.getCurrentLoop()) {
                gif.reset();
            } else {
                gif.start();
            }
        }
        isStop = false;
        isPause = false;
    }

    /**
     * 设置循环次数
     *
     * @param loopCount
     */
    private int loopcount = 0;

    public void setLoopCount(@IntRange(from = 0, to = Character.MAX_VALUE) final int loopCount) {
        Drawable drawable = ndrawable == null ? (defaultDrawable == null ? getDrawable() : defaultDrawable) : ndrawable;
        if (drawable instanceof GifDrawable) {
            ((GifDrawable) drawable).setLoopCount(loopCount);
        }
        this.loopcount = loopCount;
    }

    /**
     * 获取全部帧
     *
     * @return
     */
    public int getNumberOfFrames() {
        Drawable drawable = ndrawable == null ? (defaultDrawable == null ? getDrawable() : defaultDrawable) : ndrawable;
        if (drawable instanceof GifDrawable) {
            return ((GifDrawable) drawable).getNumberOfFrames();
        }
        return 1;
    }

    /**
     * 获取当前播放循环位置
     *
     * @return
     */
    public int getCurrentLoop() {
        Drawable drawable = ndrawable == null ? (defaultDrawable == null ? getDrawable() : defaultDrawable) : ndrawable;
        if (drawable instanceof GifDrawable) {
            return ((GifDrawable) drawable).getCurrentLoop();
        }
        return 1;
    }

    /**
     * 获取当前帧
     *
     * @return
     */
    public int getCurrentPosition() {
        Drawable drawable = ndrawable == null ? (defaultDrawable == null ? getDrawable() : defaultDrawable) : ndrawable;
        if (drawable instanceof GifDrawable) {
            return ((GifDrawable) drawable).getCurrentPosition();
        }
        return 1;
    }

    /**
     * 获取播放间隔
     *
     * @return
     */
    public int getDuration() {
        Drawable drawable = ndrawable == null ? (defaultDrawable == null ? getDrawable() : defaultDrawable) : ndrawable;
        if (drawable instanceof GifDrawable) {
            return ((GifDrawable) drawable).getDuration();
        }
        return 1;
    }

    /**
     * 获取某帧的间隔
     *
     * @param index
     * @return
     */
    public int getFrameDuration(@IntRange(from = 0) final int index) {
        Drawable drawable = ndrawable == null ? (defaultDrawable == null ? getDrawable() : defaultDrawable) : ndrawable;
        if (drawable instanceof GifDrawable) {
            return ((GifDrawable) drawable).getFrameDuration(index);
        }
        return 1;
    }

    /**
     * 跳转到动画某一帧
     *
     * @param position
     */
    public void seekto(@IntRange(from = 0, to = Integer.MAX_VALUE) final int position) {
        Drawable drawable = ndrawable == null ? (defaultDrawable == null ? getDrawable() : defaultDrawable) : ndrawable;
        if (drawable instanceof GifDrawable) {
            ((GifDrawable) drawable).seekTo(position);
        }
    }


    /**
     * 判断动画是否正在播放
     *
     * @return
     */
    public boolean isRunning() {
        Drawable drawable = ndrawable == null ? (defaultDrawable == null ? getDrawable() : defaultDrawable) : ndrawable;
        if (drawable instanceof GifDrawable) {
            return ((GifDrawable) drawable).isRunning();
        }
        return false;
    }

    /**
     * 判断动画是否正在播放
     *
     * @return
     */
    public void seekToFrame(@IntRange(from = 0, to = Integer.MAX_VALUE) final int frameIndex) {
        Drawable drawable = ndrawable == null ? (defaultDrawable == null ? getDrawable() : defaultDrawable) : ndrawable;
        if (drawable instanceof GifDrawable) {
            ((GifDrawable) drawable).seekToFrame(frameIndex);
        }
    }

    /**
     * 判断动画是否正在播放
     *
     * @return
     */
    public int getCurrentFrameIndex() {
        Drawable drawable = ndrawable == null ? (defaultDrawable == null ? getDrawable() : defaultDrawable) : ndrawable;
        if (drawable instanceof GifDrawable) {
            return ((GifDrawable) drawable).getCurrentFrameIndex();
        }
        return 0;
    }


    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
    }

    protected void loadimage(final Object obj) {
        imageload.loadDrawable(this);
    }

    public void setDrawable(Drawable drawable) {
        setDrawable(drawable, true);
    }

    private void setDrawable(Drawable drawable, boolean isset) {
        if (!defaultDrawableLoaded) {
            if (getDrawable() == null) {
                defaultDrawable = drawable;
            } else {
                defaultDrawable = getDrawable();
            }
            defaultDrawableLoaded = true;
        }
        this.ndrawable = drawable;
        this.setImageDrawable(drawable);
        initColorFilter();
        setGifDrawable();
    }

    private void setDefault() {
        if (!defaultDrawableLoaded) {
            if (getDrawable() != null) {
                defaultDrawable = getDrawable();
            }
            defaultDrawableLoaded = true;
        }
        setImageDrawable(defaultDrawable);
        initColorFilter();
        if (canScale) {
            if (mPhotoViewAttacher != null) {
                mPhotoViewAttacher.cleanup();
            }
            mPhotoViewAttacher = new PhotoViewAttacher(MImageView.this);
        }

    }

    /**
     * 设置默认图片
     *
     * @param drawable
     * @throws
     * @author ryan
     * @Title: setDefault
     * @Description: TODO
     */
    public void setDefault(Drawable drawable) {
        this.defaultDrawable = drawable;
        defaultDrawableLoaded = true;
        setImageDrawable(defaultDrawable);
        initColorFilter();
    }

    /**
     * 获取图片加载索引<BR>
     *
     * @return
     * @see ImageBase#getObj()
     */
    public Object getObj() {
        return obj;
    }

    /**
     * 设置图片加载器
     *
     * @param imageload
     * @throws
     * @author ryan
     * @Title: setImageload
     * @Description: TODO
     */
    public void setImageload(ImageLoad imageload) {
        this.imageload = imageload;
    }

    /**
     * 索引是否改变<BR>
     * 索引是否改变
     *
     * @return
     * @see ImageBase#isChange()
     */
    public boolean isChange() {
        return change;
    }

    private void setLoaded(boolean loaded, Drawable drawable, int type) {
        setLoaded(false);
        setLoading(false);
        if (reload) {
            this.reload = false;
        }
        if (drawable == null) {
            setDefault();
        } else {
            if (this.onImageLoaded != null) {
                onImageLoaded.onImageLoaded(this.getObj(), drawable, mBitmap, 100, 100);
            }
            setDrawable(drawable);
            if (userAnim && !animed) {
                animed = true;
                Animation animation = this.getAnimation();
                if (animation == null) {
                    animation = AnimationUtils.loadAnimation(this.getContext(), R.anim.mimgview_loaded_out);
                    this.setAnimation(animation);
                }
                this.startAnimation(this.getAnimation());
            }
        }
    }


    private void setGifDrawable() {
        if (isStop) {
            this.stop(!isPause);
        } else if (isPause) {
            this.pause();
        } else if (loopcount > 0) {
            this.setLoopCount(loopcount);
        }
    }

    /**
     * 是否启用文件缓存<BR>
     * 是否启用文件缓存
     *
     * @return
     * @see ImageBase#isCache()
     */
    @Override
    public boolean isCache() {
        return mUseCache;
    }

    public void setUseCache(boolean bol) {
        this.mUseCache = bol;
    }

    /**
     * 图片加载类型<BR>
     * 获取图片加载类型
     *
     * @see ImageBase#getLoadType()
     */
    @Override
    public int getLoadType() {
        return mImageLoadType;
    }

    /**
     * @param loadtype
     * @throws
     * @author ryan
     * @Title: setLoadType
     * @Description: TODO
     */
    public void setLoadType(int loadtype) {
        this.mImageLoadType = loadtype;
    }

    /**
     * [一句话功能简述]<BR>
     * [功能详细描述]
     *
     * @param event
     * @return
     * @see View#onTouchEvent(android.view.MotionEvent)
     */

    /**
     * 完成图片加载<BR>
     * 图片加载完成,处理显示方式
     *
     * @param drawable
     */
    @Override
    public void loaded(MBitmap drawable, final String loading, final int type) {
        if (drawable != null) {
//            if(drawable.isGif && drawable instanceof MGifDrawable){
//
//            }
            this.mBitmap = drawable;
            ndrawable = drawable.getDrawable();
            if (ndrawable instanceof MGifDrawable) {
                ((MGifDrawable) ndrawable).setBlur(blur);
            }
        } else {
            ndrawable = null;
        }
        post(new Runnable() {
            @Override
            public void run() {
                if (mLoadingId != null && mLoadingId.equals(loading)) {
                    setLoaded(true, ndrawable, type);
                    if (canScale) {
                        if (mPhotoViewAttacher != null) {
                            mPhotoViewAttacher.cleanup();
                        }
                        mPhotoViewAttacher = new PhotoViewAttacher(MImageView.this);
                    }
                }
            }
        });
    }

    /**
     * [一句话功能简述]<BR>
     * [功能详细描述]
     *
     * @return
     * @see ImageBase#isReload()
     */

    @Override
    public boolean isReload() {
        return reload;
    }

    /**
     * [一句话功能简述]<BR>
     * [功能详细描述]
     *
     * @param id
     * @see ImageBase#setLoadid(String)
     */

    @Override
    public void setLoadid(String id) {
        this.mLoadingId = id;
    }

    @Override
    public boolean isPalette() {
        return isPalette;
    }

    public boolean isScaleAble() {
        return canScale;
    }

    public void setScaleAble(boolean scaleAble) {
        this.canScale = scaleAble;
    }

    public void setUserAnim(boolean userAnim) {
        this.userAnim = userAnim;
    }

    public void setOnImageLoaded(OnImageLoaded onImageLoaded) {
        this.onImageLoaded = onImageLoaded;
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(l);
        this.onClickListener = l;
    }

    public OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public interface OnImageLoaded {
        public void onImageLoaded(Object obj, Drawable drawable, MBitmap mBitmap, int size, int length);
    }

    public int getBlur() {
        return blur;
    }

    public void setBlur(int blur) {
        this.blur = blur;
        this.invalidate();
    }

    public boolean isCircle() {
        return isCircle;
    }

    public void setCircle(boolean isCircle) {
        this.isCircle = isCircle;
        this.invalidate();
    }

    public boolean isAutosize() {
        return autosize;
    }

    public void setAutosize(boolean autosize) {
        this.autosize = autosize;
    }

    /**
     * [一句话功能简述]<BR>
     * [功能详细描述]
     *
     * @return
     * @see ImageBase#getLoadContext()
     */

    @Override
    public Context getLoadContext() {
        if (getContext() instanceof Activity) {
            return getContext();
        } else if (getContext() instanceof ContextThemeWrapper) {
            ContextThemeWrapper cont = (ContextThemeWrapper) getContext();
            return cont.getBaseContext();
        }
        return getContext();
    }

    /**
     * [一句话功能简述]<BR>
     * [功能详细描述]
     *
     * @param context
     * @param intent
     * @see com.mdx.framework.broadcast.OnReceiverListener#onReceiver(Context, com.mdx.framework.broadcast.BIntent)
     */
    @Override
    public void onReceiver(Context context, BIntent intent) {
        if (intent.type == 99) {
            Drawable drab = null;
            if (ndrawable instanceof MGifDrawable) {
                drab = ((MGifDrawable) ndrawable).gifDrawable;
            } else {
                drab = ndrawable;
            }
            if (drab == intent.data) {
                Context act = getLoadContext();
                if (act instanceof IActivity) {
                    if (((IActivity) act).getResumed()) {
                        this.invalidate();
                    }
                }
            }
        }
        if (getDrawable() instanceof BitmapDrawable && ((BitmapDrawable) getDrawable()).getBitmap().isRecycled()) {
            setLoaded(false);
            reload = false;
            setDefault();
        } else {
            if (this.getWidth() != lastwidth && this.getHeight() != lastheight) {
                this.lastheight = getHeight();
                this.lastwidth = getWidth();
                if (this.getLayoutParams() == null || this.getLayoutParams().width == LayoutParams.WRAP_CONTENT || this.getLayoutParams().height == LayoutParams.WRAP_CONTENT) {
                    setLoaded(false);
                    reload = false;
                    toimageload();
                    invalidate();
                }
            }
        }
    }

    public void setImgToColor(int state, int color) {
        if (state == STATE_NOMORE) {
            mTocolor = color;
            clearTocolor(STATE_NOMORE);
            ColorMatrix mColorMatrix = new ColorMatrix();
            mColorMatrix.set(new float[]{0, 0, 0, 0, Color.red(color), 0, 0, 0, 0, Color.green(color), 0, 0, 0, 0, Color.blue(color), 0, 0, 0, Color.alpha(color) / 255f, 0});
            ColorFilter colorFilter = new ColorMatrixColorFilter(mColorMatrix);
            colorFilters.put(STATE_NOMORE, colorFilter);
            setClickColor(mClickColor, mclickflg, false);
        } else if (state == STATE_SELECTED) {
            mSelcolor = color;
            clearTocolor(STATE_SELECTED);
            ColorMatrix mColorMatrix = new ColorMatrix();
            mColorMatrix.set(new float[]{0, 0, 0, 0, Color.red(color), 0, 0, 0, 0, Color.green(color), 0, 0, 0, 0, Color.blue(color), 0, 0, 0, Color.alpha(color) / 255f, 0});
            ColorFilter colorFilter = new ColorMatrixColorFilter(mColorMatrix);
            colorFilters.put(STATE_SELECTED, colorFilter);
            setClickColor(mClickColor, mclickflg, false);
        } else if (state == STATE_SELPRESSED) {
            mSelClickcolor = color;
            setClickColor(mClickColor, mclickflg, false);
        } else if (state == STATE_PRESSED) {
            mClickColor = color;
            setClickColor(mClickColor, mclickflg, false);
        }
        initColorFilter();
    }


    public void setImgToColor(int state, int color, PorterDuff.Mode mode) {
        if (state == STATE_NOMORE) {
            mTocolor = color;
            this.mChcolorMode = mode;
            clearTocolor(STATE_NOMORE);
            ColorFilter cf = new PorterDuffColorFilter(color, mode);
            colorFilters.put(STATE_NOMORE, cf);
            setClickColor(mClickColor, mclickflg, false);
            initColorFilter();
        } else if (state == STATE_SELECTED) {
            mSelcolor = color;
            this.mSelChcolorMode = mode;
            clearTocolor(STATE_SELECTED);
            ColorFilter cf = new PorterDuffColorFilter(color, mode);
            colorFilters.put(STATE_NOMORE, cf);
            setClickColor(mClickColor, mclickflg, false);
            initColorFilter();
        } else if (state == STATE_SELPRESSED) {
            mSelClickcolor = color;
            setClickColor(mClickColor, mclickflg, false);
        } else if (state == STATE_PRESSED) {
            mClickColor = color;
            setClickColor(mClickColor, mclickflg, false);
        }
        initColorFilter();
    }


    /**
     * @param tocolor the mTocolor to set
     */
    public void setTocolor(int tocolor) {
        this.mTocolor = tocolor;
        clearTocolor(STATE_NOMORE);
        ColorMatrix mColorMatrix = new ColorMatrix();
        mColorMatrix.set(new float[]{0, 0, 0, 0, Color.red(mTocolor), 0, 0, 0, 0, Color.green(mTocolor), 0, 0, 0, 0, Color.blue(mTocolor), 0, 0, 0, Color.alpha(mTocolor) / 255f, 0});
        ColorFilter colorFilter = new ColorMatrixColorFilter(mColorMatrix);
        colorFilters.put(STATE_NOMORE, colorFilter);
        setClickColor(mClickColor, mclickflg, false);
        initColorFilter();
    }

    public void clearTocolor(int mode) {
        this.clearColorFilter();
        this.colorFilters.remove(mode);
        setClickColor(mClickColor, mclickflg, false);
        initColorFilter();
    }


    public void setChcolor(int color, PorterDuff.Mode mode) {
        this.mTocolor = color;
        this.mChcolorMode = mode;
        clearTocolor(STATE_NOMORE);
        ColorFilter cf = new PorterDuffColorFilter(color, mode);
        colorFilters.put(STATE_NOMORE, cf);
        setClickColor(mClickColor, mclickflg, false);
        initColorFilter();
    }

    public void setChcolor(int color) {
        setChcolor(color, PorterDuff.Mode.SRC_ATOP);
    }

    public void setClickColor(int clickColor, int flag) {
        setClickColor(clickColor, flag, true);
    }

    public void setClickColor(ColorFilter colorFilter) {
        colorFilters.put(STATE_PRESSED, colorFilter);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        int[] state = getDrawableState();
        boolean hasstate = false;
        for (int i : state) {
            if (i == SELECTED_STATE_SET[0]) {
                drawable_state = STATE_SELECTED;
                hasstate = true;
            }
            if (i == PRESSED_STATE_SET[0]) {
                drawable_state = this.isSelected() ? STATE_SELPRESSED : STATE_PRESSED;
                hasstate = true;
            }
        }
        if (!hasstate) {
            drawable_state = STATE_NOMORE;
        }
        setProcess(drawable_state);
    }

    public void setImageSize(int imgw, int imgh) {
        this.imgWidth = imgw;
        this.imgHeight = imgh;
    }


    /**
     * [一句话功能简述]<BR>
     * [功能详细描述]
     *
     * @return
     * @see ImageBase#getImageWidth()
     */

    @Override
    public int getImageWidth() {
        if (this.imgWidth == -1) {
            return getWidth();
        }
        return imgWidth;
    }

    /**
     * [一句话功能简述]<BR>
     * [功能详细描述]
     *
     * @return
     * @see ImageBase#getImageHeight()
     */

    @Override
    public int getImageHeight() {
        if (this.imgHeight == -1) {
            return getHeight();
        }
        return imgHeight;
    }

    @Override
    public int getRound() {
        return round;
    }

    /**
     * @param round the round to set
     */
    public void setRound(int round) {
        this.round = round;
        this.invalidate();
    }

    private void initColorFilter() {
        ColorFilter clf = null;
        if (colorFilters != null) {
            clf = colorFilters.get(drawable_state);
        }
        if (clf != null) {
            this.setColorFilter(clf);
            if (ndrawable != null) {
                ndrawable.setColorFilter(clf);
            }
            if (defaultDrawable != null) {
                defaultDrawable.setColorFilter(clf);
            }
        } else {
            this.clearColorFilter();
            if (ndrawable != null) {
                ndrawable.clearColorFilter();
            }
            if (defaultDrawable != null) {
                defaultDrawable.clearColorFilter();
            }
        }

        invalidate();
    }

    public void setProcess(int state) {
        initColorFilter();
    }

    public void setClickColor(int clickColor, int flag, boolean isc) {
        this.mClickColor = clickColor;
        if (isc) {
            this.mclickflg = flag;
        }
        ColorFilter nomor = colorFilters.get(STATE_NOMORE);
        ColorFilter seled = colorFilters.get(STATE_SELECTED);
        if (nomor != null) {
            if (nomor instanceof ColorMatrixColorFilter) {
                ColorMatrix mColorMatrix = new ColorMatrix();
                float r = Color.red(mTocolor) * (1 + Color.red(mClickColor) / 255f * flag);
                float g = Color.green(mTocolor) * (1 + Color.green(mClickColor) / 255f * flag);
                float b = Color.blue(mTocolor) * (1 + Color.blue(mClickColor) / 255f * flag);
                float a = Color.alpha(mTocolor) / 255f;
                r = r < 0 ? 0 : (r > 255 ? 255 : r);
                g = g < 0 ? 0 : (g > 255 ? 255 : g);
                b = b < 0 ? 0 : (b > 255 ? 255 : b);
                mColorMatrix.set(new float[]{0, 0, 0, 0, r, 0, 0, 0, 0, g, 0, 0, 0, 0, b, 0, 0, 0, a, 0});
                ColorFilter presscolor = new ColorMatrixColorFilter(mColorMatrix);
                colorFilters.put(STATE_PRESSED, presscolor);
            } else if (nomor instanceof PorterDuffColorFilter) {

                float r = Color.red(mTocolor) * (1 + Color.red(mClickColor) / 255f * flag);
                float g = Color.green(mTocolor) * (1 + Color.green(mClickColor) / 255f * flag);
                float b = Color.blue(mTocolor) * (1 + Color.blue(mClickColor) / 255f * flag);
                float a = Color.alpha(mTocolor) / 255f;
                r = r < 0 ? 0 : (r > 255 ? 255 : r);
                g = g < 0 ? 0 : (g > 255 ? 255 : g);
                b = b < 0 ? 0 : (b > 255 ? 255 : b);
                int color = argb(a, r, g, b);
                ColorFilter cf = new PorterDuffColorFilter(color, this.mChcolorMode);
                colorFilters.put(STATE_PRESSED, cf);
            }
        } else {
            ColorMatrix mColorMatrix = new ColorMatrix();
            float r = 1 + Color.red(mClickColor) / 255f * flag;
            float g = 1 + Color.green(mClickColor) / 255f * flag;
            float b = 1 + Color.blue(mClickColor) / 255f * flag;
            float a = 1 + Color.alpha(mClickColor) / 255f * flag;
            r = r < 0 ? 0 : (r > 1 ? 1 : r);
            g = g < 0 ? 0 : (g > 1 ? 1 : g);
            b = b < 0 ? 0 : (b > 1 ? 1 : b);
            a = a < 0 ? 0 : (a > 1 ? 1 : a);
            mColorMatrix.set(new float[]{r, 0, 0, 0, 0, 0, g, 0, 0, 0, 0, 0, b, 0, 0, 0, 0, 0, a, 0});
            ColorFilter presscolor = new ColorMatrixColorFilter(mColorMatrix);
            colorFilters.put(STATE_PRESSED, presscolor);
        }

        if (seled != null) {
            if (seled instanceof ColorMatrixColorFilter) {
                ColorMatrix mColorMatrix = new ColorMatrix();
                float r = Color.red(mSelcolor) * (1 + Color.red(mSelClickcolor) / 255f * flag);
                float g = Color.green(mSelcolor) * (1 + Color.green(mSelClickcolor) / 255f * flag);
                float b = Color.blue(mSelcolor) * (1 + Color.blue(mSelClickcolor) / 255f * flag);
                float a = Color.alpha(mSelcolor) / 255f;
                r = r < 0 ? 0 : (r > 255 ? 255 : r);
                g = g < 0 ? 0 : (g > 255 ? 255 : g);
                b = b < 0 ? 0 : (b > 255 ? 255 : b);
                mColorMatrix.set(new float[]{0, 0, 0, 0, r, 0, 0, 0, 0, g, 0, 0, 0, 0, b, 0, 0, 0, a, 0});
                ColorFilter selpresscolor = new ColorMatrixColorFilter(mColorMatrix);
                colorFilters.put(STATE_SELPRESSED, selpresscolor);
            } else if (seled instanceof PorterDuffColorFilter) {
                float r = Color.red(mSelcolor) * (1 + Color.red(mSelClickcolor) / 255f * flag);
                float g = Color.green(mSelcolor) * (1 + Color.green(mSelClickcolor) / 255f * flag);
                float b = Color.blue(mSelcolor) * (1 + Color.blue(mSelClickcolor) / 255f * flag);
                float a = Color.alpha(mSelcolor) / 255f;
                r = r < 0 ? 0 : (r > 255 ? 255 : r);
                g = g < 0 ? 0 : (g > 255 ? 255 : g);
                b = b < 0 ? 0 : (b > 255 ? 255 : b);
                int color = argb(a, r, g, b);
                ColorFilter cf = new PorterDuffColorFilter(color, this.mSelChcolorMode);
                colorFilters.put(STATE_SELPRESSED, cf);
            }
        } else {
            ColorMatrix mColorMatrix = new ColorMatrix();
            float r = 1 + Color.red(mSelClickcolor) / 255f * flag;
            float g = 1 + Color.green(mSelClickcolor) / 255f * flag;
            float b = 1 + Color.blue(mSelClickcolor) / 255f * flag;
            float a = 1 + Color.alpha(mSelClickcolor) / 255f * flag;
            r = r < 0 ? 0 : (r > 1 ? 1 : r);
            g = g < 0 ? 0 : (g > 1 ? 1 : g);
            b = b < 0 ? 0 : (b > 1 ? 1 : b);
            a = a < 0 ? 0 : (a > 1 ? 1 : a);
            mColorMatrix.set(new float[]{r, 0, 0, 0, 0, 0, g, 0, 0, 0, 0, 0, b, 0, 0, 0, 0, 0, a, 0});
            ColorFilter selpresscolor = new ColorMatrixColorFilter(mColorMatrix);
            colorFilters.put(STATE_SELPRESSED, selpresscolor);
        }
    }

    public static int argb(float alpha, float red, float green, float blue) {
        return ((int) (alpha * 255.0f + 0.5f) << 24) |
                ((int) (red * 255.0f + 0.5f) << 16) |
                ((int) (green * 255.0f + 0.5f) << 8) |
                (int) (blue * 255.0f + 0.5f);
    }
}

