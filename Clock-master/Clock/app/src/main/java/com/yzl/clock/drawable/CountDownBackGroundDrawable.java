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
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by Administrator on 2018/6/12.
 */

public class CountDownBackGroundDrawable extends CountDownDrawable {

    public CountDownBackGroundDrawable(float w, float h) {
        super(w, h);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        drawCenter(canvas);
        drawOuter(canvas);
        drawInner(canvas);
        drawDial(canvas);
        drawMark(canvas);
        drawCenterBlackPoint(canvas);
        drawCenterRedPoint(canvas);
        drawArc(canvas);
    }


    private void drawArc(Canvas canvas){

        Path leftArcPath = new Path();
        Path rightArcPath = new Path();
        Path centerArcPath = new Path();

        paint.setShader(null);
        paint.setColor(outerLineColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(outerLineWidth);
        leftArcPath.arcTo(outerRectF,-180,70);
        canvas.drawPath(leftArcPath,paint);
        rightArcPath.arcTo(outerRectF,-70,70);
        canvas.drawPath(rightArcPath,paint);
        centerArcPath.arcTo(outerRectF,-110,40);
        paint.setTextSize(circleWidth);
        paint.setStrokeWidth(circleWidth/2);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawTextOnPath("时  间  进  度",centerArcPath,0,circleWidth/3,paint);

        float endX = cX  + outerRadius + circleWidth;
        float x1 = endX - shadowWidth;
        float y1 = cY;
        float x2 = endX + shadowWidth;
        float y2 = cY;
        float x3 = endX;
        float y3 = cY + shadowWidth*1.5f;

        Path trianglePath = new Path();
        trianglePath.moveTo(x1,y1);
        trianglePath.lineTo(x2,y2);
        trianglePath.lineTo(x3,y3);

        paint.setStyle(Paint.Style.FILL);
        canvas.drawPath(trianglePath,paint);
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
        paint.setStyle(Paint.Style.FILL);

        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.CENTER);

        for (int i = -90; i < 270; i += 90) {
            float startX;
            float startY;
            float endX;
            float endY;
            float paintWidth;

            paint.setColor(Color.BLACK);
            startX = cX + (dialRadius - largeMarkLength) * (float) (Math.cos(i * Math.PI / 180));
            startY = cY + (dialRadius - largeMarkLength) * (float) (Math.sin(i * Math.PI / 180));
            endX = cX + (dialRadius) * (float) (Math.cos(i * Math.PI / 180));
            endY = cY + (dialRadius) * (float) (Math.sin(i * Math.PI / 180));
            paintWidth = largeMarkLength / 5;

            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(textSize);

            if (i == -90) {
                paint.setStrokeWidth(textSize / 10);
                canvas.drawText("1", cX, cY - dialRadius + largeMarkLength * 2 + textSize / 3, paint);
            } else if (i == 0) {
                paint.setStrokeWidth(textSize / 10);
                canvas.drawText("1/4", cX + dialRadius - largeMarkLength * 2, cY + textSize / 3, paint);
            } else if (i == 90) {
                paint.setStrokeWidth(textSize / 10);
                canvas.drawText("2/4", cX, cY + dialRadius - largeMarkLength * 2 + textSize / 3, paint);
            } else if (i == 180) {
                paint.setStrokeWidth(textSize / 10);
                canvas.drawText("3/4", cX - dialRadius + largeMarkLength * 2, cY + textSize / 3, paint);
            }

            paint.setStrokeWidth(paintWidth);
            canvas.drawLine(startX, startY, endX, endY, paint);
        }
    }

    private void drawCenterBlackPoint(Canvas canvas) {
        paint.setShader(null);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(cX, cY, dialRadius / 10, paint);
    }

    private void drawCenterRedPoint(Canvas canvas) {
        paint.setShader(null);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(cX, cY, dialRadius / 20, paint);
    }


    private void drawIndicator(Canvas canvas) {
        float millisAngle = calculateIndicatorAngle();
        float millisStartX = cX - backIndicatorLength * (float) (Math.cos(millisAngle * Math.PI / 180));
        float millisStartY = cY - backIndicatorLength * (float) (Math.sin(millisAngle * Math.PI / 180));
        float millisEndX = cX + secondIndicatorLength * (float) (Math.cos(millisAngle * Math.PI / 180));
        float millisEndY = cY + secondIndicatorLength * (float) (Math.sin(millisAngle * Math.PI / 180));

        paint.setShader(null);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(secondIndicatorWidth);
        canvas.drawLine(millisStartX, millisStartY, millisEndX, millisEndY, paint);
    }


    private float calculateIndicatorAngle() {
        Calendar calendar = Calendar.getInstance();
        int millis = calendar.get(Calendar.MILLISECOND);
        Log.e("millis", millis + "");
        return (float) millis / 1000 * 360;
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
}
