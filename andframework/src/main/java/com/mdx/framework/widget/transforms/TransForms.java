/*
 * 文件名: TransFormsFactory.java
 * 版    权：  Copyright XCDS Tech. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: ryan
 * 创建时间:2014年6月25日
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.mdx.framework.widget.transforms;

import androidx.viewpager.widget.ViewPager.PageTransformer;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 * @author ryan
 * @version [2014年6月25日 上午9:20:22] 
 */
public class TransForms {
    public static final int TRANSFORM_DEFAULT=0;
    public static final int TRANSFORM_ACCORDION=1;
    public static final int TRANSFORM_BACKGROUNDTOFOREGROUND=2;
    public static final int TRANSFORM_CUBEIN=3;
    public static final int TRANSFORM_CUBEOUT=4;
    public static final int TRANSFORM_DEPTH=5;
    public static final int TRANSFORM_FOREGROUNDTOBACKGROUND=6;
    public static final int TRANSFORM_FLIPHORIZONTAL=7;
    public static final int TRANSFORM_FLIPVERTICAL=8;
    public static final int TRANSFORM_ROTATEUP=9;
    public static final int TRANSFORM_ROTATEDOWN=10;
    public static final int TRANSFORM_STACK=11;
    public static final int TRANSFORM_TABLET=12;
    public static final int TRANSFORM_ZOOMIN=13;
    public static final int TRANSFORM_ZOOMOUT=14;
    public static final int TRANSFORM_ZOOMOUTSLIDE=15;
    
    
    public static PageTransformer getTransForms(int transformat){
        switch (transformat) {
            case TRANSFORM_ACCORDION:
                return new AccordionTransformer();
            case TRANSFORM_BACKGROUNDTOFOREGROUND:
                return new BackgroundToForegroundTransformer();
            case TRANSFORM_CUBEIN:
                return new CubeInTransformer();
            case TRANSFORM_CUBEOUT:
                return new CubeOutTransformer();
            case TRANSFORM_DEPTH:
                return new DepthPageTransformer();
            case TRANSFORM_FOREGROUNDTOBACKGROUND:
                return new ForegroundToBackgroundTransformer();
            case TRANSFORM_FLIPHORIZONTAL:
                return new FlipHorizontalTransformer();
            case TRANSFORM_FLIPVERTICAL:
                return new FlipVerticalTransformer();
            case TRANSFORM_ROTATEUP:
                return new RotateUpTransformer();
            case TRANSFORM_ROTATEDOWN:
                return new RotateDownTransformer();
            case TRANSFORM_STACK:
                return new StackTransformer();
            case TRANSFORM_TABLET:
                return new TabletTransformer();
            case TRANSFORM_ZOOMIN:
                return new ZoomInTransformer();
            case TRANSFORM_ZOOMOUT:
                return new ZoomOutTranformer();
            case TRANSFORM_ZOOMOUTSLIDE:
                return new ZoomOutSlideTransformer();
            default:
                return new DefaultTransformer();
        }
    }
}
