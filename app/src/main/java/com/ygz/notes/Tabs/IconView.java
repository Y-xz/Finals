package com.ygz.notes.Tabs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;


/**
 * Created by Administrator on 2018-01-14.
 */

public class IconView extends android.support.v7.widget.AppCompatImageView{
/*开始**********************构造方法***********************************/
    public IconView(Context context){
        super(context);
    }
    public IconView(Context context, AttributeSet attributeSet){
        /*attribute set 属性集*/
        super(context,attributeSet);
    }
    public IconView(Context context,AttributeSet attributeSet,int defStyleAttr){
        /*def Style Attr 定义样式属性*/
        super(context,attributeSet,defStyleAttr);
    }
/*结尾**********************构造方法***********************************/
    private Paint mPaint;
    private Bitmap mSelectedIcon;
    private Bitmap mNormalIcon;
    private Rect mSelectedRect;
    private Rect mNormalRect;
    private int mSelectedAlpha = 0;

    public final void init(int normal,int selected,int width,int height){
        this.mNormalIcon = createBitmap(normal);
        this.mSelectedIcon = createBitmap(selected);
        this.mNormalRect = new Rect(0,0,width,height);
        this.mSelectedRect = new Rect(0,0,width,height);
        this.mPaint = new Paint(1);
    }
    private Bitmap createBitmap(int resId){
        /*位图工厂.资源解码*/
        return BitmapFactory.decodeResource(getResources(),resId);
    }
    /*滑动过程中重新绘制方法*/
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        if (this.mPaint == null){
            return;
        }
        this.mPaint.setAlpha(255 - this.mSelectedAlpha);
        /*画布.画图();*/
        canvas.drawBitmap(this.mNormalIcon,null,this.mNormalRect,this.mPaint);
        this.mPaint.setAlpha(this.mSelectedAlpha);
        canvas.drawBitmap(this.mSelectedIcon,null,this.mSelectedRect,this.mPaint);
    }

    public final void changeSelectedAlpha(int alpha){
        this.mSelectedAlpha = alpha;
        invalidate();/*销毁*/
    }
/*偏移量的变化*/
    public final void offsetChanged(float offset){
        changeSelectedAlpha((int)(255 * (1 - offset)));
    }
}
