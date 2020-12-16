package com.mdx.framework.widget.smoothprogress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.animation.Interpolator;
import android.widget.ProgressBar;
/**
 * Created by castorflex on 11/10/13.
 */
public class SmoothProgressBar extends ProgressBar {
    
    public SmoothProgressBar(Context context) {
        this(context, null);
    }
    
    public SmoothProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public SmoothProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        SmoothProgressDrawable.Builder builder = new SmoothProgressDrawable.Builder(context);
        SmoothProgressDrawable d = builder.build();
        setIndeterminateDrawable(d);
    }
    
    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isIndeterminate() && getIndeterminateDrawable() instanceof SmoothProgressDrawable
                && !((SmoothProgressDrawable) getIndeterminateDrawable()).isRunning()) {
            getIndeterminateDrawable().draw(canvas);
        }
    }
    
    private SmoothProgressDrawable checkIndeterminateDrawable() {
        Drawable ret = getIndeterminateDrawable();
        if (ret == null || !(ret instanceof SmoothProgressDrawable))
            throw new RuntimeException("The drawable is not a SmoothProgressDrawable");
        return (SmoothProgressDrawable) ret;
    }
    
    @Override
    public void setInterpolator(Interpolator interpolator) {
        super.setInterpolator(interpolator);
        Drawable ret = getIndeterminateDrawable();
        if (ret != null && (ret instanceof SmoothProgressDrawable))
            ((SmoothProgressDrawable) ret).setInterpolator(interpolator);
    }
    
    public void setSmoothProgressDrawableInterpolator(Interpolator interpolator) {
        checkIndeterminateDrawable().setInterpolator(interpolator);
    }
    
    public void setSmoothProgressDrawableColors(int[] colors) {
        checkIndeterminateDrawable().setColors(colors);
    }
    
    public void setSmoothProgressDrawableColor(int color) {
        
        checkIndeterminateDrawable().setColor(color);
    }
    
    public void setSmoothProgressDrawableSpeed(float speed) {
        checkIndeterminateDrawable().setSpeed(speed);
    }
    
    public void setSmoothProgressDrawableProgressiveStartSpeed(float speed) {
        checkIndeterminateDrawable().setProgressiveStartSpeed(speed);
    }
    
    public void setSmoothProgressDrawableProgressiveStopSpeed(float speed) {
        checkIndeterminateDrawable().setProgressiveStopSpeed(speed);
    }
    
    public void setSmoothProgressDrawableSectionsCount(int sectionsCount) {
        checkIndeterminateDrawable().setSectionsCount(sectionsCount);
    }
    
    public void setSmoothProgressDrawableSeparatorLength(int separatorLength) {
        checkIndeterminateDrawable().setSeparatorLength(separatorLength);
    }
    
    public void setSmoothProgressDrawableStrokeWidth(float strokeWidth) {
        checkIndeterminateDrawable().setStrokeWidth(strokeWidth);
    }
    
    public void setSmoothProgressDrawableReversed(boolean reversed) {
        checkIndeterminateDrawable().setReversed(reversed);
    }
    
    public void setSmoothProgressDrawableMirrorMode(boolean mirrorMode) {
        checkIndeterminateDrawable().setMirrorMode(mirrorMode);
    }
    
    public void setProgressiveStartActivated(boolean progressiveStartActivated) {
        checkIndeterminateDrawable().setProgressiveStartActivated(progressiveStartActivated);
    }
    
    public void setSmoothProgressDrawableCallbacks(SmoothProgressDrawable.Callbacks listener) {
        checkIndeterminateDrawable().setCallbacks(listener);
    }
    
    public void setSmoothProgressDrawableBackgroundDrawable(Drawable drawable) {
        checkIndeterminateDrawable().setBackgroundDrawable(drawable);
    }
    
    public void progressiveStart() {
        checkIndeterminateDrawable().progressiveStart();
    }
    
    public void progressiveStop() {
        checkIndeterminateDrawable().progressiveStop();
    }
}
