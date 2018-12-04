package com.yzl.clock.clockview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.yzl.clock.drawable.CountDownBackGroundDrawable;
import com.yzl.clock.drawable.CountDownIndicatorDrawable;
import com.yzl.clock.drawable.TimeBackGroundDrawable;
import com.yzl.clock.drawable.TimeIndicatorDrawable;
import com.yzl.clock.widget.WatchOperator;

/**
 * Created by Administrator on 2018/6/12.
 */

public class CountDownView extends AppCompatImageView{

    private CountDownIndicatorDrawable mIndicatorDrawable;
    private CountDownBackGroundDrawable mBackGroundDrawable;
    float mWidth;
    float mHeight;

    public CountDownView(Context context) {
        super(context);
        init(context);
    }

    public CountDownView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CountDownView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        mWidth = context.getResources().getDisplayMetrics().widthPixels;
        mHeight = mWidth*MathUtil.sizeRatio;
        setWillNotDraw(false);
        mIndicatorDrawable = new CountDownIndicatorDrawable(mWidth,mHeight);
        mBackGroundDrawable = new CountDownBackGroundDrawable(mWidth,mHeight);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setBackgroundDrawable(mBackGroundDrawable);
        setImageDrawable(mIndicatorDrawable);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(mWidth>0&&mHeight>0){
            setMeasuredDimension((int) mWidth, (int) (mHeight));
        }
    }

    public CountDownIndicatorDrawable getIndicatorDrawable(){
        return mIndicatorDrawable;
    }

}
