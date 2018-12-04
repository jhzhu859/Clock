package com.yzl.clock.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Checkable;

import com.yzl.clock.R;
import com.yzl.clock.clockview.MathUtil;


/**
 * Created by Administrator on 2018/6/20.
 */

public class Switch extends View implements Checkable {
    private float width;
    private float height;
    private float padding;
    private float lineWidth;
    private float radius;
    private float mLeftCenter;
    private float mRightCenterX;
    private float mMoveLength;
    private float mCenterY;
    private float mCenterX;
    private float maxDistance ;
    private float clickDistance = 2f;
    private float fraction = 0f;
    private float mTouchX;

    private float mCornerRatio = 0.5f;
    private float mBorderCornerRadius;
    private float mCenterCornerRadius;

    private RectF mBorderRectF;

    private boolean moving = false;
    private boolean checked = false;

    private int mSelectColor = 0xff3f51b5;
    private int mUnSelectColor = 0xffb4b4b4;
    private final int STYLE_STROKE = 1;
    private final int STYLE_FILL = 2;
    private int currentStyle = STYLE_FILL;

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private OnCheckedChangeListener listener;

    private String mText;


    public void setOnCheckedChangeListener(OnCheckedChangeListener listener){
        this.listener = listener;
    }

    public Switch(Context context) {
        super(context);
        init(context,null,0,0);
    }

    public Switch(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs,0,0);
    }

    public Switch(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr,0);
    }

    @TargetApi(21)
    public Switch(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs,defStyleAttr,defStyleRes);
    }

    private void init(Context context,AttributeSet attributeSet,int defStyleAttr,int defStyleRes){
        TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.Switch, defStyleAttr, defStyleRes);
        mSelectColor = a.getColor(R.styleable.Switch_sw_selectColor,mSelectColor);
        mUnSelectColor = a.getColor(R.styleable.Switch_sw_unSelectColor,mUnSelectColor);
        mCornerRatio = a.getFloat(R.styleable.Switch_sw_corner_ratio,mCornerRatio);
        mCornerRatio = Math.min(mCornerRatio,0.5f);
        currentStyle = a.getInt(R.styleable.Switch_sw_style,STYLE_FILL);
        checked = a.getBoolean(R.styleable.Switch_sw_checked,false);
        fraction = checked?1:0;
        a.recycle();
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
        height = width / 2;
        lineWidth = width / 40;
        padding = width / 40;

        mBorderCornerRadius = (height - 2*padding)*mCornerRatio;
        radius = (height - padding * 2) / 8 * 3;
        mCenterCornerRadius = radius*2*mCornerRatio;

        mLeftCenter = padding + (height - padding * 2) / 2;
        mRightCenterX = width - mLeftCenter;
        mMoveLength = mRightCenterX - mLeftCenter;
        mCenterY = height / 2;
        mCenterX = mLeftCenter;
        clickDistance = Math.max(3,width/100);

        if (currentStyle == STYLE_FILL){
            mBorderRectF = new RectF(padding,padding,width-padding,height-padding);
        }else {
            mBorderRectF = new RectF(padding+lineWidth/2,padding+lineWidth/2,width-padding-lineWidth/2,height-padding-lineWidth/2);
        }
        paint.setStrokeWidth(lineWidth);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (width > 0 && height > 0) {
            setMeasuredDimension((int) (width), (int) height);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (moving) {
            return true;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchX = event.getX();
                maxDistance = 0;
                return true;
            case MotionEvent.ACTION_MOVE:
                float mX = event.getX();
                float distance = mX - mTouchX;
                maxDistance = Math.max(maxDistance, Math.abs(distance));
                if(maxDistance>clickDistance){
                    mX = checked?mRightCenterX + distance:mLeftCenter + distance;
                    mX = MathUtil.getFloatBetween(mX,mLeftCenter,mRightCenterX);
                    fraction = (mX - mLeftCenter) / mMoveLength;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (maxDistance < clickDistance) {
                    setChecked(!checked);
                }else {
                    setChecked(fraction >= 0.5f);
                }
        }
        return super.onTouchEvent(event);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (STYLE_FILL == currentStyle){
            paint.setColor(MathUtil.getRunningColor(mUnSelectColor,mSelectColor,fraction));
            mCenterX = mLeftCenter + fraction * mMoveLength;
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRoundRect(mBorderRectF,mBorderCornerRadius,mBorderCornerRadius,paint);
            paint.setColor(Color.WHITE);
            canvas.drawRoundRect(new RectF(mCenterX - radius,mCenterY-radius,mCenterX+radius,mCenterY+radius),mCenterCornerRadius,mCenterCornerRadius,paint);
        }else {
            paint.setColor(MathUtil.getRunningColor(mUnSelectColor,mSelectColor,fraction));
            mCenterX = mLeftCenter + fraction * mMoveLength;
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRoundRect(mBorderRectF,mBorderCornerRadius,mBorderCornerRadius,paint);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(mCenterX, mCenterY, radius, paint);
            canvas.drawRoundRect(new RectF(mCenterX - radius,mCenterY-radius,mCenterX+radius,mCenterY+radius),mCenterCornerRadius,mCenterCornerRadius,paint);
        }
    }

    @Override
    public void setChecked(boolean checked) {
        if(this.checked!=checked){
            if(listener!=null){
                listener.onCheckChanged(this,checked);
            }
        }
        this.checked = checked;
        getAnimator(fraction).start();
    }

    @Override
    public boolean isChecked() {
        return false;
    }

    @Override
    public void toggle() {
        setChecked(!checked);
    }


    private ValueAnimator getAnimator(float mFraction) {
        long duration = 0;
        float end;
        if (checked) {
            duration = (int)((1-mFraction) * 100);
            end = 1;
        } else {
            duration = (int)((mFraction) * 100);
            end = 0;
        }

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(fraction, end);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                moving = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                moving = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                moving = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                fraction = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        return valueAnimator;
    }

    public interface OnCheckedChangeListener{
        void onCheckChanged(Switch s, boolean checked);
    }
}
