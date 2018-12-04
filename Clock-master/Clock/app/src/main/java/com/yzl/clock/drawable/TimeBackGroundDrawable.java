package com.yzl.clock.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Administrator on 2018/6/12.
 */

public class TimeBackGroundDrawable extends TimeClockDrawable {

    public TimeBackGroundDrawable(float w, float h) {
        super(w,h);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        //drawBg(canvas);
        drawCenter(canvas);
        drawOuter(canvas);
        drawInner(canvas);
        drawDial(canvas);
        drawMark(canvas);
    }


    private void drawBg(Canvas canvas){
        paint.setShader(null);
        linearGradient = new LinearGradient(0,0,0,h,topColor,bottomColor, Shader.TileMode.MIRROR);
        paint.setColor(Color.parseColor("#0088ff"));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(bgRectF,paint);
    }

    private void drawInner(Canvas canvas) {
        paint.setShader(null);
        SweepGradient sweepGradient = new SweepGradient(cX, cY, innerColors, innerPositions);
        paint.setStrokeWidth(shadowWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setShader(sweepGradient);
        canvas.drawCircle(cX, cY, innerRadius, paint);
    }

    private void drawCenter(Canvas canvas) {
        paint.setShader(null);
        RadialGradient radialGradient = new RadialGradient(cX, cY, centerRadius, new int[]{edgeColor, centerColor}, circleStops, Shader.TileMode.MIRROR);
        paint.setShader(radialGradient);
        paint.setStrokeWidth(circleWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(cX, cY, centerRadius, paint);
    }

    private void drawOuter(Canvas canvas) {
        paint.setShader(null);
        SweepGradient sweepGradient = new SweepGradient(cX, cY, outerColors, outerPositions);
        paint.setStrokeWidth(shadowWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setShader(sweepGradient);
        canvas.drawCircle(cX, cY, outerRadius, paint);
    }

    private void drawDial(Canvas canvas) {
        paint.setShader(null);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(cX, cY, dialRadius, paint);
    }

    private void drawMark(Canvas canvas) {

        paint.setShader(null);
        paint.setColor(markColor);
        paint.setStyle(Paint.Style.STROKE);

        for (int i = -90; i < 270; i += 6) {
            float startX;
            float startY;
            float endX;
            float endY;
            float paintWidth;
            if (i % 30 == 0) {
                startX = cX + (dialRadius - largeIndicatorLength) * (float) (Math.cos(i * Math.PI / 180));
                startY = cY + (dialRadius - largeIndicatorLength) * (float) (Math.sin(i * Math.PI / 180));
                endX = cX + (dialRadius) * (float) (Math.cos(i * Math.PI / 180));
                endY = cY + (dialRadius) * (float) (Math.sin(i * Math.PI / 180));
                paintWidth = largeIndicatorLength/5;

                paint.setTextSize(indicatorTextSize);
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setStyle(Paint.Style.FILL);

                if(i == -90){
                    paint.setStrokeWidth(indicatorTextSize/10);
                    canvas.drawText("12",cX,cY-dialRadius+largeIndicatorLength*2+indicatorTextSize/3,paint);
                }else if(i == 0){
                    paint.setStrokeWidth(indicatorTextSize/10);
                    canvas.drawText("3",cX + dialRadius-largeIndicatorLength*2,cY+indicatorTextSize/3,paint);
                }else if(i == 90){
                    paint.setStrokeWidth(indicatorTextSize/10);
                    paint.setStyle(Paint.Style.FILL);
                    canvas.drawText("6",cX ,cY + dialRadius-largeIndicatorLength*2+indicatorTextSize/3,paint);
                }else if(i == 180){
                    paint.setStrokeWidth(indicatorTextSize/10);
                    canvas.drawText("9",cX-dialRadius+largeIndicatorLength*2 ,cY+indicatorTextSize/3,paint);
                }

            }else {
                paintWidth = smallIndicatorLength/5;
                startX = cX + (dialRadius - smallIndicatorLength) * (float) (Math.cos(i * Math.PI / 180));
                startY = cY + (dialRadius - smallIndicatorLength) * (float) (Math.sin(i * Math.PI / 180));
                endX = cX + (dialRadius) * (float) (Math.cos(i * Math.PI / 180));
                endY = cY + (dialRadius) * (float) (Math.sin(i * Math.PI / 180));
            }
            paint.setStrokeWidth(paintWidth);
            canvas.drawLine(startX,startY,endX,endY,paint);
        }
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {

    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
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
}
