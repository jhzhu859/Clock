package com.yzl.clock.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.yzl.clock.R;

/**
 * Created by Kingc on 2018/6/30.
 */

public class Button extends View {

    private float mWidth;
    private float mHeight;
    private float mBorderWidth = 1;
    private float mRadius;
    private float mCenterX;
    private float mCenterY;
    private float mTextSize;
    private float mTextLineWidth;

    private RectF mRectF;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    private int mBorderColor = Color.parseColor("#1296db");
    private int mTextColor = 0x88000000;

    private String mText = "开始";


    public Button(Context context) {
        super(context);
        init(context,null,0,0);
    }

    public Button(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs,0,0);
    }

    public Button(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr,0);

    }
    @TargetApi(21)
    public Button(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs,defStyleAttr,defStyleRes);
    }


    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes){
        setClickable(true);
        int width = context.getResources().getDisplayMetrics().widthPixels;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Button, defStyleAttr, defStyleRes);
        mText = a.getString(R.styleable.Button_btn_text);

        mWidth = width*0.8f;
        mHeight = mWidth*0.15f;
        mRadius = mHeight/2 - mBorderWidth/2;
        mCenterX = mWidth/2;
        mTextSize = mHeight/2;
        mCenterY = mHeight - (mHeight - mTextSize)/2 - mTextSize/6;
        mTextLineWidth = mTextSize/10;
        mPaint.setTextSize(mTextSize);

        float left = mBorderWidth/2;
        float right = mWidth - left;
        float top = mBorderWidth/2;
        float bottom = mHeight - top;
        mRectF = new RectF(left,top,right,bottom);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mBorderColor);
        mPaint.setStrokeWidth(mBorderWidth);
        canvas.drawRoundRect(mRectF,mRadius,mRadius,mPaint);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(mTextLineWidth);
        mPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(mText,mCenterX,mCenterY,mPaint);
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(mWidth>0&&mHeight>0){
            setMeasuredDimension(Math.round(mWidth),Math.round(mHeight));
        }
    }

    public void setText(String text){
        this.mText = text;
        invalidate();
    }
}
