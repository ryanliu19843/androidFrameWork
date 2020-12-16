package com.mdx.framework.server.image.impl;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import pl.droidsonroids.gif.AnimationListener;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifError;
import pl.droidsonroids.gif.GifInfoHandle;
import pl.droidsonroids.gif.transforms.Transform;


public class MGifDrawable extends GifDrawable {
    public GifDrawable gifDrawable;
    public ColorFilter colorFilter;
    protected final Paint mPaint = new Paint(Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG);


    public MGifDrawable(GifDrawable gifDrawable) {
        super();
        this.gifDrawable = gifDrawable;
    }

    public void setBlur(int blur) {
        this.gifDrawable.setBlur(blur);
    }

    public void recycle() {
        gifDrawable.recycle();
    }

    public boolean isRecycled() {
        return gifDrawable.isRecycled();
    }

    @Override
    public int getIntrinsicHeight() {
        return gifDrawable.getIntrinsicHeight();
    }

    @Override
    public int getIntrinsicWidth() {
        return gifDrawable.getIntrinsicWidth();
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public ColorFilter getColorFilter() {
        return mPaint.getColorFilter();
    }

    public void start() {
        gifDrawable.start();
    }

    public void reset(boolean autostart) {
        gifDrawable.reset(autostart);
    }

    public void reset() {
        gifDrawable.reset();
    }

    public void stop() {
        gifDrawable.stop();
    }

    public boolean isRunning() {
        return gifDrawable.isRunning();
    }

    @Nullable
    public String getComment() {
        return gifDrawable.getComment();
    }

    public int getLoopCount() {
        return gifDrawable.getLoopCount();
    }

    public void setLoopCount(int loopCount) {
        gifDrawable.setLoopCount(loopCount);
    }

    @Override
    public String toString() {
        return gifDrawable.toString();
    }

    public int getNumberOfFrames() {
        return gifDrawable.getNumberOfFrames();
    }

    public GifError getError() {
        return gifDrawable.getError();
    }

    public void setSpeed(float factor) {
        gifDrawable.setSpeed(factor);
    }

    public void pause() {
        gifDrawable.pause();
    }

    public int getDuration() {
        return gifDrawable.getDuration();
    }

    public int getCurrentPosition() {
        return gifDrawable.getCurrentPosition();
    }

    public void seekTo(int position) {
        gifDrawable.seekTo(position);
    }

    public void seekToFrame(int frameIndex) {
        gifDrawable.seekToFrame(frameIndex);
    }

    public Bitmap seekToFrameAndGet(int frameIndex) {
        return gifDrawable.seekToFrameAndGet(frameIndex);
    }

    public Bitmap seekToPositionAndGet(int position) {
        return gifDrawable.seekToPositionAndGet(position);
    }

    public boolean isPlaying() {
        return gifDrawable.isPlaying();
    }

    public int getBufferPercentage() {
        return gifDrawable.getBufferPercentage();
    }

    public boolean canPause() {
        return gifDrawable.canPause();
    }

    public boolean canSeekBackward() {
        return gifDrawable.canSeekBackward();
    }

    public boolean canSeekForward() {
        return gifDrawable.canSeekForward();
    }

    public int getAudioSessionId() {
        return gifDrawable.getAudioSessionId();
    }

    public int getFrameByteCount() {
        return gifDrawable.getFrameByteCount();
    }

    public long getAllocationByteCount() {
        return gifDrawable.getAllocationByteCount();
    }

    public long getMetadataAllocationByteCount() {
        return gifDrawable.getMetadataAllocationByteCount();
    }

    public long getInputSourceByteCount() {
        return gifDrawable.getInputSourceByteCount();
    }

    public void getPixels(@NonNull int[] pixels) {
        gifDrawable.getPixels(pixels);
    }

    public int getPixel(int x, int y) {
        return gifDrawable.getPixel(x, y);
    }

    @Override
    public void onBoundsChange(Rect bounds) {
        gifDrawable.onBoundsChange(bounds);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        gifDrawable.draw(canvas, getBounds(), mPaint);
    }

    public int getOpacity() {
        if (mPaint.getAlpha() < 255) {
            return PixelFormat.TRANSPARENT;
        }
        return PixelFormat.OPAQUE;
    }

    @Override
    public int getAlpha() {
        return mPaint.getAlpha();
    }

    @Override
    public void setFilterBitmap(boolean filter) {
        mPaint.setFilterBitmap(filter);
        invalidateSelf();
    }

    @Override
    public void setDither(boolean dither) {
        mPaint.setDither(dither);
        invalidateSelf();
    }

    public void addAnimationListener(@NonNull AnimationListener listener) {
        gifDrawable.addAnimationListener(listener);
    }

    public boolean removeAnimationListener(AnimationListener listener) {
        return gifDrawable.removeAnimationListener(listener);
    }

    public Bitmap getCurrentFrame() {
        return gifDrawable.getCurrentFrame();
    }

    @Override
    public void setTintList(ColorStateList tint) {
        gifDrawable.setTintList(tint);
    }

    @Override
    public void setTintMode(@NonNull PorterDuff.Mode tintMode) {
        gifDrawable.setTintMode(tintMode);
    }

    @Override
    public boolean onStateChange(int[] stateSet) {
        return gifDrawable.onStateChange(stateSet);
    }

    @Override
    public boolean isStateful() {
        return gifDrawable.isStateful();
    }

    @Override
    public boolean setVisible(boolean visible, boolean restart) {
        return gifDrawable.setVisible(visible, restart);
    }

    public int getCurrentFrameIndex() {
        return gifDrawable.getCurrentFrameIndex();
    }

    public int getCurrentLoop() {
        return gifDrawable.getCurrentLoop();
    }

    public boolean isAnimationCompleted() {
        return gifDrawable.isAnimationCompleted();
    }

    public int getFrameDuration(int index) {
        return gifDrawable.getFrameDuration(index);
    }

    public void setCornerRadius(float cornerRadius) {
        gifDrawable.setCornerRadius(cornerRadius);
    }

    public float getCornerRadius() {
        return gifDrawable.getCornerRadius();
    }

    public void setTransform(@Nullable Transform transform) {
        gifDrawable.setTransform(transform);
    }

    @Nullable
    public Transform getTransform() {
        return gifDrawable.getTransform();
    }

    public GifInfoHandle getNativeInfoHandle() {
        return gifDrawable.getNativeInfoHandle();
    }

    @Override
    public void setBounds(@NonNull Rect bounds) {
        super.setBounds(bounds);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }
}
