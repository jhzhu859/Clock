package com.yzl.clock.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.os.SystemClock;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.yzl.clock.widget.WatchOperator;

import java.util.Calendar;

/**
 * Created by Administrator on 2018/6/12.
 */

public class CountDownIndicatorDrawable extends CountDownDrawable {

    public CountDownIndicatorDrawable(float w, float h) {
        super(w, h);
    }

    private Long mDuration = 60 * 1000L;
    private Long mStartMillis = 0L;
    private Long mCurrentMillis = 0L;
    private String durationText = "00:01:00";
    private boolean running = false;
    String splitText = ":";
    private Long handledMills = 0L;

    @Override
    public void draw(@NonNull Canvas canvas) {
        drawIndicator(canvas);
        drawTimeText(canvas);
        if (running) invalidateSelf();
    }

    private void drawIndicator(Canvas canvas) {
        float millisAngle = calculateIndicatorAngle();
        float millisEndX = cX + hourIndicatorLength * (float) (Math.cos(millisAngle * Math.PI / 180));
        float millisEndY = cY + hourIndicatorLength * (float) (Math.sin(millisAngle * Math.PI / 180));
        paint.setShader(null);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(hourIndicatorWidth);
        paint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawLine(cX, cY, millisEndX, millisEndY, paint);
        paint.setColor(mIndicatorShadowColor);
        canvas.drawLine(cX, cY+mIndicatorShadowOffset, millisEndX, millisEndY+mIndicatorShadowOffset, paint);
        if (millisAngle >= 360f) {
            stop();
        }
    }

    private void drawTimeText(Canvas canvas) {
        paint.setShader(null);
        paint.setColor(Color.WHITE);
        paint.setTextSize(timeTextSize);
        paint.setTextAlign(Paint.Align.CENTER);
        String text = "剩余时间:" + durationText;
        canvas.drawText(text, textX, textY, paint);
    }

    private float calculateIndicatorAngle() {
        if (running) {
            mCurrentMillis = SystemClock.uptimeMillis();
            handledMills = mCurrentMillis - mStartMillis;
            handledMills = Math.min(mDuration,handledMills);
        }
        Long remainMillis = mDuration - handledMills;
        if (remainMillis == 0L) {
            durationText = "00:00:00";
            stop();
        } else {
            Long hourRemain = remainMillis / 3600 / 1000;
            Long minuteRemain = (remainMillis - hourRemain * 3600 * 1000) / 60 / 1000;
            Long secondRemain = (remainMillis - hourRemain * 3600 * 1000 - minuteRemain * 60 * 1000) / 1000 ;
            String hourText = hourRemain < 10 ? "0" + hourRemain : "" + hourRemain;
            String minuteText = minuteRemain < 10 ? "0" + minuteRemain : "" + minuteRemain;
            String secondText = secondRemain < 10 ? "0" + secondRemain : "" + secondRemain;
            durationText = hourText + splitText + minuteText + splitText + secondText;
        }
        return mDuration == 0L?-90:(float) handledMills / mDuration * 360 - 90f;
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


    public void start() {
        running = true;

    }

    public void stop() {
        running = false;
        if (listener != null) {
            listener.onStop();
        }
    }


    public boolean getRunningState() {
        return running;
    }

    public void setRunningState(boolean runningState){
        this.running = runningState;
        if (running){
            mStartMillis = SystemClock.uptimeMillis();
            invalidateSelf();
        }else {

        }
    }

    ;


    public interface OnStopListener {
        void onStop();
    }

    private OnStopListener listener;

    public void setStopListener(OnStopListener onStopListener) {
        listener = onStopListener;
    }

    public void setDurationText(String text) {
        this.durationText = text;
        String[] array = durationText.split(splitText);
        String hourText = array[0];
        String minuteText = array[1];
        String secondText = array[2];

        int hour = Integer.parseInt(hourText);
        int minute = Integer.parseInt(minuteText);
        int second = Integer.parseInt(secondText);
        mDuration = hour * 3600 * 1000L + minute * 60 * 1000L + second*1000L;
        handledMills = 0L;
        invalidateSelf();
    }

}
