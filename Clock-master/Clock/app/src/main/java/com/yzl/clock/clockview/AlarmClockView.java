package com.yzl.clock.clockview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.yzl.clock.drawable.AlarmBackGroundDrawable;
import com.yzl.clock.drawable.AlarmIndicatorDrawable;
import com.yzl.clock.drawable.TimeBackGroundDrawable;
import com.yzl.clock.drawable.TimeIndicatorDrawable;

/**
 * Created by Administrator on 2018/6/12.
 */

public class AlarmClockView extends AppCompatImageView {

    private AlarmIndicatorDrawable mIndicatorDrawable;
    private AlarmBackGroundDrawable mBackGroundDrawable;
    float mWidth;
    float mHeight;

    public AlarmClockView(Context context) {
        super(context);
        init(context);
    }

    public AlarmClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AlarmClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        mWidth = context.getResources().getDisplayMetrics().widthPixels;
        mHeight = mWidth*MathUtil.sizeRatio;
        setWillNotDraw(false);
        mIndicatorDrawable = new AlarmIndicatorDrawable(mWidth,mHeight);
        mBackGroundDrawable = new AlarmBackGroundDrawable(mWidth,mHeight);
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





}
