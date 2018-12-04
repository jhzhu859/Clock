package com.yzl.clock.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Checkable;

import com.yzl.clock.R;
import com.yzl.clock.clockview.MathUtil;


/**
 * Created by Kingc on 2018/7/5.
 */

public class CheckBox extends View implements Checkable {

    private float mWidth;
    private float mHeight;
    private float mCornerRatio = 0.15f;
    private float mCornerRadius;
    private float mBorderRatio = 0.05f;
    private float mBorderWidth;
    private float mMarkRatio = 0.1f;
    private float mMarkLineWidth;
    private float mMarkWidth;
    private float mCurveRatio = 0.382f;
    private float mPaddingRatio = 0.05f;
    private float mPaddingWidth;
    private float mImageCenterX;
    private float mImageCenterY;
    private float mTextSize;
    private float mTextLineWidth;
    private float mFraction = 0f;
    private float mTextX;
    private float mTextY;

    private int mCheckedColor = 0xff009900;
    private int mUnCheckedColor = 0xffb4b4b4;
    private int mTextColor = 0xff333333;

    private boolean mChecked = false;
    private boolean mRunning = false;

    private String mText = "测试";
    private RectF mEmptyRectF;
    private RectF mFillRectF;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    public CheckBox(Context context) {
        super(context);
        init(context,null,0,0);
    }

    public CheckBox(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs,0,0);
    }

    public CheckBox(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr,0);
    }

    @TargetApi(21)
    public CheckBox(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs,defStyleAttr,defStyleRes);
    }

    private void init(Context context,AttributeSet attributeSet,int defStyleAttr,int defStyleRes){
        TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.CheckBox, defStyleAttr, defStyleRes);
        mCheckedColor = a.getColor(R.styleable.CheckBox_cb_selectColor,mCheckedColor);
        mUnCheckedColor = a.getColor(R.styleable.CheckBox_cb_unSelectColor,mUnCheckedColor);
        mTextColor = a.getColor(R.styleable.CheckBox_cb_text_color,mTextColor);
        mCornerRatio = a.getFloat(R.styleable.CheckBox_cb_corner_ratio,mCornerRatio);
        mCornerRatio = Math.min(mCornerRatio,0.5f);
        mText = a.getString(R.styleable.CheckBox_cb_text);
        if (TextUtils.isEmpty(mText)){
            mText = "";
        }
        mChecked = a.getBoolean(R.styleable.CheckBox_cb_checked,false);
        mFraction = mChecked?1:0;
        a.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getWidth();
        mHeight = getHeight();
        float mScale = Math.min(mWidth,mHeight);
        mHeight = mScale;
        mTextSize = mScale*0.9f;
        mPaint.setTextSize(mTextSize);
        int length = computeMaxStringWidth(0,new String[]{mText},mPaint);
        mWidth =Math.max(mWidth,mScale + mScale*mBorderRatio + length) ;

        mTextLineWidth = mTextSize/10;
        mImageCenterX = mScale/2;
        mImageCenterY = mScale/2;
        mTextX = mScale + mBorderWidth;
        mTextY = mScale - (mScale - mTextSize)/2 - mTextSize/6;
        mCornerRadius = mCornerRatio*mScale;
        mBorderWidth = mBorderRatio*mScale;
        mMarkLineWidth = mMarkRatio*mScale;
        mPaddingWidth = mPaddingRatio*mScale;
        mMarkWidth = mScale - 8*mBorderWidth - 2*mPaddingWidth;
        mEmptyRectF = new RectF(mPaddingWidth+mBorderWidth/2,mPaddingWidth+mBorderWidth/2,mScale-mPaddingWidth-mBorderWidth/2,mScale-mPaddingWidth-mBorderWidth/2);
        mFillRectF = new RectF(mPaddingWidth,mPaddingWidth,mScale-mPaddingWidth,mScale-mPaddingWidth);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mHeight>0&&mWidth>0){
            setMeasuredDimension((int)mWidth,(int)mHeight);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawChecked(canvas);
        drawText(canvas);
    }

    private void drawChecked(Canvas canvas){
        if (mFraction == 0f){
            mPaint.setStrokeWidth(mBorderWidth);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(mUnCheckedColor);
            canvas.drawRoundRect(mEmptyRectF,mCornerRadius,mCornerRadius,mPaint);
            return;
        }
        mPaint.setStyle(Paint.Style.FILL);
        int color = MathUtil.getRunningColor(mUnCheckedColor,mCheckedColor,mFraction);
        mPaint.setColor(color);
        canvas.drawRoundRect(mFillRectF,mCornerRadius,mCornerRadius,mPaint);
        Path path = new Path();
        float x1,y1,x2,y2,x3,y3;
        x1 = 4*mBorderWidth+mPaddingWidth;
        y1 = mImageCenterY - (mCurveRatio - 0.25f)*mMarkWidth;
        path.moveTo(x1,y1);
        if (mFraction<=mCurveRatio){
            float length = mMarkWidth*mFraction;
            x2 = x1 + length;
            y2 = y1 + length;
            path.lineTo(x2,y2);
        }else {
            float beforeCurveLength = mMarkWidth*mCurveRatio;
            x2 = x1 + beforeCurveLength;
            y2 = y1 + beforeCurveLength;
            float afterCurveLength = (mFraction - mCurveRatio)*mMarkWidth;
            x3 = x2 + afterCurveLength;
            y3 = y2 - afterCurveLength;
            path.lineTo(x2,y2);
            path.lineTo(x3,y3);
        }
        mPaint.setStrokeWidth(mMarkLineWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path,mPaint);
    }


    private void drawText(Canvas canvas){
        mPaint.setTextSize(mTextSize);
        mPaint.setStrokeWidth(mTextLineWidth);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.setColor(mTextColor);
        canvas.drawText(mText,mTextX,mTextY,mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mRunning){
            return true;
        }
        int action = event.getAction();
        if (action ==MotionEvent.ACTION_DOWN){
            return true;
        }
        if(action == MotionEvent.ACTION_CANCEL||action == MotionEvent.ACTION_UP){
            toggle();
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void setChecked(boolean checked) {
        if (mChecked!=checked){
            mChecked = checked;
            if(mOnCheckedChangeListener!=null){
                mOnCheckedChangeListener.OnCheckedChanged(this,checked);
            }
        }
        mChecked = checked;
        float desPosition = checked?1f:0f;
        if(desPosition!=mFraction){
            getAnimator(mFraction,desPosition).start();
        }
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    private ValueAnimator getAnimator(float start, float end) {

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(start, end);
        valueAnimator.setDuration(200);
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mRunning = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mRunning = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mRunning = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mFraction = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        return valueAnimator;
    }

    public interface OnCheckedChangeListener{
        boolean OnCheckedChanged(CheckBox checkBox, boolean checked);
    }

    private OnCheckedChangeListener mOnCheckedChangeListener;

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener){
        mOnCheckedChangeListener = listener;
    }


    private int computeMaxStringWidth(int currentMax, String[] strings, Paint p) {
        float maxWidthF = 0.0f;
        int len = strings.length;
        for (int i = 0; i < len; i++) {
            float width = p.measureText(strings[i]);
            maxWidthF = Math.max(width, maxWidthF);
        }
        int maxWidth = (int) (maxWidthF + 0.5);
        if (maxWidth < currentMax) {
            maxWidth = currentMax;
        }
        return maxWidth;
    }
}
