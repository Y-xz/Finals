package com.ygz.notes.Tabs;


import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**容器
 * Created by Yxz on 2018-01-15.
 */

public class ContainerView extends LinearLayout{

/*开始**********************构造方法***********************************/
    public ContainerView(Context context){
        super(context,null);
    }
    public ContainerView(Context context, AttributeSet attrs){
        super(context,attrs);
    }
/*结束**********************构造方法***********************************/
    private ViewPager mViewPager;
    private ViewPager.OnPageChangeListener mViewPagerPageChangeListener;

    private int mTextNormalColor;
    private int mTextSelectedColor;

    private int mLastPosition;/*上个位置*/
    private int mSelectedPosition;
    private float mSelectionOffset;/*选择偏移位置*/

    private String[] mTitles;
    private int[][] mIconRes;
    private View[] mTabView;
    /*布局 and 控件*/
    private int mLayoutId;
    /*private int mTextViewId;*/
    private int mIconViewId;
    private int mIconWidth;
    private int mIconHeight;
    /*显示过度颜色*/
    private boolean mShowTransitionColor = true;
    /*初始化容器*/
    public void initContainer(String[] titles,int[][] iconRes,/*int[] colors,*/boolean showTransitionColor){
        this.mTitles = titles;
        this.mIconRes = iconRes;
        /*this.mTextNormalColor = getResources().getColor(colors[0]);*/
        /*this.mTextSelectedColor = getResources().getColor(colors[1]);*/
        this.mShowTransitionColor = showTransitionColor;
    }
/**/
    public void setContainerLayout(int layout,int iconId,int width,int height){
        mLayoutId = layout;
        mIconViewId = iconId;
        mIconWidth = width;
        mIconHeight = height;
    }
/*    public void setSingleTextLayout(int layout,int textId,int width,int height){
        setContainerLayout(layout,0,textId,width,height);
    }
    public void setSingleIconLayout(int layou,int iconId,int width,int height){
        setContainerLayout(layou,iconId,0,width,height);
    }*/
/**/
    public void setViewPager(ViewPager viewPager){
        removeAllViews();
        mViewPager = viewPager;
        if (viewPager != null && viewPager.getAdapter() != null){
            viewPager.setOnPageChangeListener(new InternalViewPagerListener());
            addTabViewToContainer();
        }
    }
/*添加Tab View 到容器*/
    private void addTabViewToContainer(){
        final PagerAdapter adapter = mViewPager.getAdapter();
        mTabView = new View[adapter.getCount()];

        for (int index = 0,len = adapter.getCount();index < len;index++){
            /*inflate 膨胀*/
            final View tabView = LayoutInflater.from(getContext()).inflate(mLayoutId,this,false);
            mTabView[index] = tabView;

            IconView iconView = null;
            if (mIconViewId > 0){
                iconView = (IconView)tabView.findViewById(mIconViewId);
                iconView.init(mIconRes[index][0],mIconRes[index][1],mIconWidth,mIconHeight);
            }
      /*      TextView textView = null;
            if (mTextViewId > 0){
                textView = (TextView)tabView.findViewById(mTextViewId);
                textView.setText(mTitles[index]);
            }*/
            /*布局参数*/
            LayoutParams lp = (LayoutParams)tabView.getLayoutParams();
            lp.width = 0;
            lp.weight = 1;
            /*点击事件*/
            addTabOnClickListener(tabView,index);
            /*判断当前项目*/
            if (index == mViewPager.getCurrentItem()){
                if (iconView != null){
                    iconView.offsetChanged(0);
                }
                tabView.setSelected(true);
               /* if (textView != null){
                    textView.setTextColor(mTextSelectedColor);
                }*/
            }
            addView(tabView);
        }
    }
/*监听滑动事件*/
    private class InternalViewPagerListener implements ViewPager.OnPageChangeListener{
        private int mScrollState;

        /*scrolled 滚动*/
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            onViewPagerPageChanged(position,positionOffset);
            if (mViewPagerPageChangeListener != null){
                mViewPagerPageChangeListener.onPageScrolled(position,positionOffset,positionOffsetPixels);
            }
        }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0;i<getChildCount();i++){
            if (mIconViewId > 0){
                ((IconView)mTabView[i].findViewById(mIconViewId)).offsetChanged(position == i ? 0 :1);
            }
           /* if (mTextViewId >0){
                ((TextView)mTabView[i].findViewById(mTextViewId)).setTextColor(position == i ? mTextSelectedColor : mTextNormalColor);
            }*/
        }
        if (mScrollState == ViewPager.SCROLL_STATE_IDLE){
            onViewPagerPageChanged(position,0f);
        }
        for (int i = 0,size = getChildCount();i < size;i++){
            getChildAt(i).setSelected(position == i);
        }
        if (mViewPagerPageChangeListener != null){
            mViewPagerPageChangeListener.onPageSelected(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        mScrollState = state;
        if (mViewPagerPageChangeListener != null){
            mViewPagerPageChangeListener.onPageScrollStateChanged(state);
        }
    }
}
/*更新滑动后的视图*/
    private void onViewPagerPageChanged(int position, float positionOffset){
        mSelectedPosition = position;
        mSelectionOffset = positionOffset;
        if (positionOffset == 0f && mLastPosition != mSelectedPosition ){
            mLastPosition = mSelectedPosition;
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final int childCount = getChildCount();
        if (childCount > 0){
            if (mSelectionOffset > 0f && mSelectedPosition < (getChildCount() - 1) && mShowTransitionColor){
                /*获取当前tab和下一个*/
                View selectedTab = getChildAt(mSelectedPosition);
                View nextTab = getChildAt(mSelectedPosition + 1);

                if (mIconViewId > 0){
                    View selectedIconView = selectedTab.findViewById(mIconViewId);
                    View nextIconView = nextTab.findViewById(mIconViewId);
                    /*instanceof 指出对象是否为特定类的一个实例*/
                    if (selectedIconView instanceof IconView && nextIconView instanceof IconView){
                        ((IconView)selectedIconView).offsetChanged(mSelectionOffset);
                        ((IconView)nextIconView).offsetChanged(1 - mSelectionOffset);
                    }
                }
                /*if (mTextViewId >0){
                    View selectedTextView = selectedTab.findViewById(mTextViewId);
                    View nextTextView = nextTab.findViewById(mTextViewId);

                    Integer selectedColor = (Integer)evaluate(mSelectionOffset,mTextSelectedColor,mTextNormalColor);
                    Integer nextColor = (Integer)evaluate(1 - mSelectionOffset,mTextSelectedColor,mTextNormalColor);

                    if (selectedTextView instanceof TextView && nextTextView instanceof TextView){
                        ((TextView)selectedTextView).setTextColor(selectedColor);
                        ((TextView)nextTextView).setTextColor(nextColor);
                    }
                }*/
            }
        }
    }

    private void addTabOnClickListener(View view, final int position){
        OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(position,false);
            }
        };
        view.setOnClickListener(listener);
    }
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener){
        mViewPagerPageChangeListener = listener;
    }

    public Object evaluate (float fraction,Object startValue,Object endValue){

        int startInt = (Integer)startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;
        int endInt = (Integer)endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;

        return(startA + (int)(fraction * (endA - startA))) << 24 | (startR + (int)(fraction * (endR - startR))) << 16 |
                (startG + (int)(fraction * (endG - startG))) << 8 | (startB + (int)(fraction * (endB - startB)));
    }

/*    public void setTextSelectedColor(int textSelectedColor){
        mTextSelectedColor = textSelectedColor;
    }
    public int getTextSelectedColor(){
        return mTextSelectedColor;
    }

    public void setTextNormalColor(int textNormalColor){
        mTextNormalColor = textNormalColor;
    }
    public int getTextNormalColor(){
        return mTextNormalColor;
    }

    public void setLastPosition(int lastPosition){
        mLastPosition = lastPosition;
    }
    public int getLastPosition(){
        return mLastPosition;
    }

    public void setSelectedPosition(int selectedPosition){
        mSelectedPosition = selectedPosition;
    }
    public int getSelectedPosition(){
        return mSelectedPosition;
    }

    public void setSelectionOffset(float selectionOffset){
        mSelectionOffset = selectionOffset;
    }
    public float getSelectionOffset(){
        return mSelectionOffset;
    }

    public void setTitles(String[] titles){
        mTitles = titles;
    }
    public String[] getTitles(){
        return mTitles;
    }

    public void setIconRes(int[][] iconRes){
        mIconRes = iconRes;
    }
    public int[][] getIconRes(){
        return mIconRes;
    }

    public void setTabView(View[] tabView){
        mTabView = tabView;
    }
    public View[] getTabView(){
        return mTabView;
    }

    public void setShowTransitionColor(boolean showTransitionColor){
        mShowTransitionColor = showTransitionColor;
    }
    public boolean isShowTransitionColor(){
        return mShowTransitionColor;
    }*/

}
