package com.yzl.clock.clockview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;

/**
 * Created by Kingc on 2018/7/25.
 */

public abstract class HeadClockView extends View {

    protected float mHeight;
    protected float mWidth;
    protected float mTextSize;
    protected float mTextLineWidth;
    protected float mCenterX;
    protected float mCenterY;
    protected String mText = "";
    protected int mBackgroundColor = 0xff009900;
    protected int mTextColor = 0xffffffff;
    protected Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    public HeadClockView(Context context) {
        super(context);
        init(context,null,0,0);
    }

    public HeadClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs,0,0);
    }

    public HeadClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr,0);

    }

    public HeadClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs,defStyleAttr,defStyleRes);
    }


    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes){
        mWidth = context.getResources().getDisplayMetrics().widthPixels;
        mHeight = mWidth/8;
        mTextSize = mHeight/2;
        mTextLineWidth = mTextSize/10;
        mCenterX = mWidth/2;
        mCenterY = mHeight/2;
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(mTextLineWidth);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setColor(mTextColor);
        mPaint.setTextSize(mTextSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(mText,mCenterX,mCenterY + mTextSize/3,mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mHeight>0&&mWidth>0){
            setMeasuredDimension((int)mWidth,(int)mHeight);
        }
    }

    protected abstract String calculateText();

}
