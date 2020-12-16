package com.example.autotest.item;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * Created by ryan on 2017/5/10.
 */

public class CanvasView extends View {
    public Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Xfermode mXfermode;
    private PorterDuff.Mode mPorterDuffMode = PorterDuff.Mode.MULTIPLY;

    public CanvasView(Context context) {
        super(context);
        init();
    }

    public CanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CanvasView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CanvasView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    public void init() {
        mXfermode = new PorterDuffXfermode(mPorterDuffMode);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setAntiAlias(true);
        paint.setColor(0xffff0000);

        getArc(canvas, 100, 150, 20f, 10f, 270, paint);

        drawArc(canvas, 250, 150, 20f, 10f, 270, paint);
    }


    public void getArc(Canvas canvas, float o_x, float o_y, float r, float startangel, float angel, Paint paint) {
        canvas.save();
        RectF rect = new RectF(o_x - r, o_y - r, o_x + r, o_y + r);
        Path path = new Path();
        path.moveTo(o_x, o_y);
        float endangel = startangel + angel;
        path.lineTo((float) (o_x + r * Math.cos(startangel * Math.PI / 180)), (float) (o_y + r * Math.sin(startangel * Math.PI / 180)));
        path.lineTo((float) (o_x + r * Math.cos(endangel * Math.PI / 180)), (float) (o_y + r * Math.sin(endangel * Math.PI / 180)));
        path.addArc(rect, startangel, angel);
        canvas.clipPath(path);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvas.drawCircle(o_x, o_y, r, paint);
        canvas.restore();
    }

    public void drawArc(Canvas canvas, float o_x, float o_y, float r, float startangel, float angel, Paint paint) {
        RectF oval = new RectF();                     //RectF对象
        oval.left = o_x - r;                              //左边
        oval.top = o_y - r;                                   //上边
        oval.right = o_x + r;                             //右边
        oval.bottom = o_y + r;                                //下边
        canvas.drawArc(oval, startangel, angel, false, paint);    //绘制圆弧
    }
}
