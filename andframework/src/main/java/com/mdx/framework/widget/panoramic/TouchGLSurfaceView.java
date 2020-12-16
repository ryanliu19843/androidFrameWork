/*
 * 文件名: TouchGLSurfaceView.java 版 权： Copyright XCDS Tech. Co. Ltd. All Rights
 * Reserved. 描 述: [该类的简要描述] 创建人: ryan 创建时间:2014年7月22日
 * 
 * 修改人： 修改时间: 修改内容：[修改内容]
 */
package com.mdx.framework.widget.panoramic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.mdx.framework.R;
import com.mdx.framework.server.image.impl.DefaultImageBase.OnLoadedListener;
import com.mdx.framework.server.image.impl.MBitmap;
import com.mdx.framework.utility.Helper;

public class TouchGLSurfaceView extends GLSurfaceView {
    private PanoramicRenderer mRender;
    public OnLoadingImage onLoadingImage;
    public Object obj;
    
    public TouchGLSurfaceView(Context context) {
        super(context);      
        init(context);
    }
    
    public TouchGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    
    private void init(Context context){
        mRender = new PanoramicRenderer(BitmapFactory.decodeResource(getResources(), R.drawable.panoramic_witeback), getContext());
        setRenderer(mRender);
        requestFocus();
        setFocusableInTouchMode(true);
    }
    
    public void setRenderer(PanoramicRenderer renderer) {
        super.setRenderer(renderer);
        mRender = renderer;
    }
    
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                return mRender.onTouchMove(x, y);
            case MotionEvent.ACTION_DOWN:
                return mRender.onTouchDown(x, y);
            case MotionEvent.ACTION_UP:
                return mRender.onTouchUp(x, y);
        }
        return true;
    }
    
    public void loadimage(Object obj) {
        this.obj=obj;
        queueEvent(new Runnable() {
            public void run() {
                mRender.setTexture(BitmapFactory.decodeResource(getResources(), R.drawable.panoramic_witeback));
            }
        });
        if(onLoadingImage!=null){
            onLoadingImage.OnLoadStart();
        }
        Helper.loadImage(obj, new OnLoadedListener() {
            @Override
            public void onLoaded(final MBitmap drawable, String loadingid) {
                
                queueEvent(new Runnable() {
                    public void run() {
                        if (drawable.getBitmap() instanceof Bitmap) {
                            mRender.setTexture(drawable.getBitmap());
                        }
                    }
                });
                if(onLoadingImage!=null){
                    onLoadingImage.OnLoadEnd();
                }
            }
        });
    }
    
    public interface OnLoadingImage{
        public void OnLoadStart();
        public void OnLoadEnd();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (obj != null) {
            loadimage(obj);
        }
    }
    
    
}
