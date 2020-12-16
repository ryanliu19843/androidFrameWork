/*
 * 文件名: TouchGLSurfaceView.java 版 权： Copyright XCDS Tech. Co. Ltd. All Rights
 * Reserved. 描 述: [该类的简要描述] 创建人: ryan 创建时间:2014年7月22日
 * 
 * 修改人： 修改时间: 修改内容：[修改内容]
 */
package com.mdx.framework.widget.panoramic;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.mdx.framework.widget.panoramic.TouchGLSurfaceView.OnLoadingImage;

public class PanoramicView extends FrameLayout {
    public TouchGLSurfaceView touchGLSurfaceView;
    public OnLoadingImage onLoadingImage;
    
    public PanoramicView(Context context) {
        super(context);      
        init(context);
    }
    
    public PanoramicView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    
    private void init(Context context){
        TouchGLSurfaceView tsv=new TouchGLSurfaceView(context);
        this.addView(tsv);
    }
    
    public void setRenderer(PanoramicRenderer renderer) {
        touchGLSurfaceView.setRenderer(renderer);
    }
    
    public void loadimage(Object obj) {
        touchGLSurfaceView.loadimage(obj);
    }
    
    protected void onResume() {
        touchGLSurfaceView.onResume();
    }
    
    protected void onPause() {
        touchGLSurfaceView.onPause();
    }

}
