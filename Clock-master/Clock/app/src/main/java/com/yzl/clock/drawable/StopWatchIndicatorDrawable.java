package com.yzl.clock.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.text.DecimalFormat;
import java.util.Calendar;

/**
 * Created by Administrator on 2018/6/12.
 */

public class StopWatchIndicatorDrawable extends StopWatchDrawable {

    public StopWatchIndicatorDrawable(float w, float h) {
        super(w,h);
    }

    private Long mStartMillis = 0L;

    private Long mCurrentMills = 0L;

    private Long mEndMills = 0L;

    DecimalFormat decimalFormat = new DecimalFormat("#0.000");

    private boolean isRunning = false;

    private String mDuration = "0.000";


    @Override
    public void draw(@NonNull Canvas canvas) {
        drawIndicator(canvas);
        if(isRunning) invalidateSelf();
    }

    private void drawIndicator(Canvas canvas){
        float [] array = calculateAngleAndDuration();
        float millisAngle = array[0];
        mDuration  = decimalFormat.format(array[1]);
        float millisStartX = cX - backIndicatorLength*(float) (Math.cos(millisAngle * Math.PI / 180));
        float millisStartY = cY - backIndicatorLength*(float) (Math.sin(millisAngle * Math.PI / 180));
        float millisEndX = cX + secondIndicatorLength*(float) (Math.cos(millisAngle * Math.PI / 180));
        float millisEndY = cY + secondIndicatorLength*(float) (Math.sin(millisAngle * Math.PI / 180));
        paint.setShader(null);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(secondIndicatorWidth);
        canvas.drawLine(millisStartX,millisStartY,millisEndX,millisEndY,paint);

        paint.setColor(mIndicatorShadowColor);
        paint.setStrokeWidth(secondIndicatorWidth/2);
        canvas.drawLine(millisStartX,millisStartY+mIndicatorShadowOffset,millisEndX,millisEndY+mIndicatorShadowOffset,paint);
        drawTimeText(canvas,mDuration);
    }

    private void drawTimeText(Canvas canvas,String text){
        paint.setShader(null);
        paint.setColor(Color.WHITE);
        paint.setTextSize(timeTextSize);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(text,textX,textY,paint);
    }

    private float[] calculateAngleAndDuration(){
        mCurrentMills = isRunning?SystemClock.uptimeMillis():mEndMills;
        mEndMills = mCurrentMills;
        float millis = mCurrentMills - mStartMillis;
        float seconds = millis/1000;
        float [] array = new float[]{millis/1000*360 - 90,seconds};
        return array;
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public int getIntrinsicHeight() {
        return (int) h;
    }

    @Override
    public int getIntrinsicWidth() {
        return (int) w;
    }

    public boolean getRunningState(){
        return isRunning;
    }

    public void setRunningState(boolean running){
        this.isRunning = running;
        if (running){
            mStartMillis = SystemClock.uptimeMillis();
            invalidateSelf();
        }
    }

    public void reset(){
        this.isRunning = false;
        mStartMillis = mCurrentMills = mEndMills = 0L;
        invalidateSelf();
    }

    public String getDuration(){
        return mDuration;
    }

}
