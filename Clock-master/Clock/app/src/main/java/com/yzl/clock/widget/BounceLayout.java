package com.yzl.clock.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PointF;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import com.yzl.clock.clockview.MathUtil;

/**
 * Created by Kingc on 2018/7/30.
 */

public class BounceLayout extends FrameLayout {

    private View mChildView;
    private ViewDragHelper dragHelper;
    private int dragWidth;
    private int childWidth;
    private int childHeight;

    private int rawLeft;
    private int rawTop;

    private int touchSlop;

    public BounceLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public BounceLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BounceLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(21)
    public BounceLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    /** 将拦截事件交给ViewDragHelper处理 */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // 这里只是为了保证onTouchEvent可以执行
        return dragHelper.shouldInterceptTouchEvent(ev);
    }

    /** 将触摸事件交给ViewDragHelper处理 */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        dragHelper.processTouchEvent(event);
        return true;
    }

    private void init(){
        dragHelper = ViewDragHelper.create(this,callback);
        // 获取系统认为的滑动的临界值
        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mChildView = getChildAt(0);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }


    ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {

        /** 确定需要触摸的View */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            //Toast.makeText(getContext(), "事件捕获", Toast.LENGTH_SHORT).show();
            return child == mChildView;
        }

        /** View在水平方向的拖拽范围，不要返回0 */
        @Override
        public int getViewHorizontalDragRange(View child) {
            return dragWidth;
        }

        /**
         * 控制子View在水平方向移动
         * @param child 拖拽的View
         * @param left  手指滑动之后子ViewDragHelper认为的View的left
         * @param dx    手指在水平方向移动的距离
         * @return      子View最终的left
         */

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return left;
        }



        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return top;
        }

        /**
         * View位置改变时调用，一般用来做伴随移动和判断状态执行相应的操作
         * @param changedView
         * @param left View当前的left
         * @param top  View当前的top
         * @param dx   View的水平移动距离
         * @param dy   View的竖直移动距离
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            if (changedView == mChildView){
                int newLeft = mChildView.getLeft() ;
                int newTop = mChildView.getTop() ;
                mChildView.layout(newLeft,newTop,newLeft + childWidth,newTop + childHeight);
            }
        }

        /** 手指抬起的时候执行 */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            reset();
        }
    };


    /** 获取contentView和deleteView的测量大小 */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        dragWidth = mChildView.getMeasuredWidth();
        childWidth = mChildView.getMeasuredWidth();
        childHeight = mChildView.getMeasuredHeight();

        rawLeft = MathUtil.dpToPx(getContext(),30);
        rawTop = MathUtil.dpToPx(getContext(),10);;
    }

    @Override
    public void computeScroll() {
        if(dragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(BounceLayout.this);
        }
    }

    private void reset(){
        dragHelper.smoothSlideViewTo(mChildView,rawLeft,rawTop);
        ViewCompat.postInvalidateOnAnimation(BounceLayout.this);
    }

}
