package com.yzl.clock.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Administrator on 2018/6/12.
 */

public class AlarmBackGroundDrawable extends AlarmClockDrawable {

    public AlarmBackGroundDrawable(float w, float h) {
        super(w,h);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        //drawBg(canvas);
        drawHead(canvas);
        drawCenter(canvas);
        drawOuter(canvas);
        drawInner(canvas);
        drawDial(canvas);
        drawMark(canvas);
    }


    private void drawHead(Canvas canvas){
        float angleLeft1 = 210;
        float angleLeft2 = 240;
        float angleLeft = 225;
        float angleRight1 = -30;
        float angleRight2 = -60;
        float angleRight = -45;
        float lineWidth = hourIndicatorWidth;

        float leftStartX = cX + (outerRadius+lineWidth/2)*(float) Math.cos(angleLeft1* Math.PI / 180);
        float leftStartY = cY + (outerRadius+lineWidth/2)*(float) Math.sin(angleLeft1* Math.PI / 180);
        float leftCenterX = cX + (outerRadius+lineWidth/2+8*lineWidth)*(float) Math.cos(angleLeft* Math.PI / 180);
        float leftCenterY = cY + (outerRadius+lineWidth/2+8*lineWidth)*(float) Math.sin(angleLeft* Math.PI / 180);
        float leftEndX = cX + (outerRadius+lineWidth/2)*(float) Math.cos(angleLeft2* Math.PI / 180);
        float leftEndY = cY + (outerRadius+lineWidth/2)*(float) Math.sin(angleLeft2* Math.PI / 180);

        float rightStartX = cX + (outerRadius+lineWidth/2)*(float) Math.cos(angleRight1* Math.PI / 180);
        float rightStartY = cY + (outerRadius+lineWidth/2)*(float) Math.sin(angleRight1* Math.PI / 180);
        float rightCenterX = cX + (outerRadius+lineWidth/2+8*lineWidth)*(float) Math.cos(angleRight* Math.PI / 180);
        float rightCenterY = cY + (outerRadius+lineWidth/2+8*lineWidth)*(float) Math.sin(angleRight* Math.PI / 180);
        float rightEndX = cX + (outerRadius+lineWidth/2)*(float) Math.cos(angleRight2* Math.PI / 180);
        float rightEndY = cY + (outerRadius+lineWidth/2)*(float) Math.sin(angleRight2* Math.PI / 180);

        Path pathLeft = new Path();
        Path pathRight = new Path();
        pathLeft.moveTo(leftStartX,leftStartY);
        pathLeft.quadTo(leftCenterX,leftCenterY,leftEndX,leftEndY);
        pathLeft.arcTo(outerRectF,angleLeft2,-30);
        pathRight.moveTo(rightStartX,rightStartY);
        pathRight.quadTo(rightCenterX,rightCenterY,rightEndX,rightEndY);
        pathRight.arcTo(outerRectF,angleRight2,30);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(secondIndicatorWidth);
        paint.setColor(0xffd4d4d4);
        canvas.drawPath(pathLeft,paint);
        canvas.drawPath(pathRight,paint);
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
        for (int i = -90; i < 270; i += 30) {
            if (i % 30 == 0) {
                paint.setTextSize(indicatorTextSize);
                paint.setStrokeWidth(indicatorTextSize/10);
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setStyle(Paint.Style.FILL);
                if(i == -90){
                    canvas.drawText("12",cX,cY-dialRadius+largeIndicatorLength*2+indicatorTextSize/3,paint);
                }else if(i == 0){
                    canvas.drawText("3",cX + dialRadius-largeIndicatorLength*2,cY+indicatorTextSize/3,paint);
                }
            }
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
