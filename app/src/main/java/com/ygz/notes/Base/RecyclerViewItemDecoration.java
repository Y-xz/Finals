package com.ygz.notes.Base;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2018-03-17.
 */

public class RecyclerViewItemDecoration extends RecyclerView.ItemDecoration{

    /*画笔 paint*/
    private Paint mPaint;


    public RecyclerViewItemDecoration(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
    }

    /*设置分割线 位置*/
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        if (position != 0){
            outRect.top = 10;
        }
    }

    /*绘制分割线样式*/
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        /*矩形 Rect*/
        Rect mRect = new Rect();
        mRect.left = parent.getPaddingLeft();
        mRect.right = parent.getWidth() - parent.getPaddingRight();

        /*当前可见的Item列表个数*/
        int childCount = parent.getChildCount();

        for (int i = 1;i < childCount ;i++){
            mRect.bottom = parent.getChildAt(i).getTop();
            mRect.top = mRect.bottom - 10;
            c.drawRect(mRect,mPaint);
        }
    }
}
