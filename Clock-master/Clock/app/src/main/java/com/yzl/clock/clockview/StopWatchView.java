package com.yzl.clock.clockview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.yzl.clock.drawable.StopWatchBackGroundDrawable;
import com.yzl.clock.drawable.StopWatchIndicatorDrawable;

/**
 * Created by Administrator on 2018/6/19.
 */

public class StopWatchView extends AppCompatImageView {

    private StopWatchBackGroundDrawable mBackground;
    private StopWatchIndicatorDrawable mIndicator;

    float mWidth;
    float mHeight;

    public StopWatchView(Context context) {
        super(context);
        init(context);
    }

    public StopWatchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StopWatchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        mWidth = context.getResources().getDisplayMetrics().widthPixels;
        mHeight = mWidth*MathUtil.sizeRatio;
        setWillNotDraw(false);
        mBackground = new StopWatchBackGroundDrawable(mWidth,mHeight);
        mIndicator = new StopWatchIndicatorDrawable(mWidth,mHeight);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setBackgroundDrawable(mBackground);
        setImageDrawable(mIndicator);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(mWidth>0&&mHeight>0){
            setMeasuredDimension((int) mWidth, (int) (mHeight));
        }
    }

    public StopWatchIndicatorDrawable getIndicatorDrawable(){
        return mIndicator;
    }

}
