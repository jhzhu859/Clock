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

import java.util.Calendar;

/**
 * Created by Administrator on 2018/6/12.
 */

public class StopWatchBackGroundDrawable extends StopWatchDrawable {

    public StopWatchBackGroundDrawable(float w, float h) {
        super(w,h);
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
        drawHead(canvas);
    }


    private void drawHead(Canvas canvas){
        Path path = new Path();
        float halfAngle = swipeAngle/2;
        float angle1 = -90 - halfAngle;
        float angle2 = -90 + halfAngle;
        float x1 = cX + outerRadius*(float) Math.cos(angle1/180*Math.PI);;
        float y1 = cY + outerRadius*(float) Math.sin(angle1/180*Math.PI);
        float x4 = cX + (outerRadius + barLength)*(float) Math.cos(angle1/180*Math.PI);
        float y4 = cY + (outerRadius + barLength)*(float) Math.sin(angle1/180*Math.PI);
        float x3 = cX + (outerRadius + barLength)*(float) Math.cos(angle2/180*Math.PI);
        float y3 = cY + (outerRadius + barLength)*(float) Math.sin(angle2/180*Math.PI);
        path.moveTo(x1,y1);
        path.arcTo(outerRectF,angle1,swipeAngle);
        path.lineTo(x3,y3);
        path.lineTo(x4,y4);
        RectF rectF = new RectF(x4 - barLength/2,y4-barLength,x3+barLength/2,y4);
        LinearGradient linearGradient1 = new LinearGradient(x4,y4,x3,y3,linearColors,linearPositions, Shader.TileMode.MIRROR);
        LinearGradient linearGradient2 = new LinearGradient(x4-barLength/2,y4,x3+barLength/2,y3,linearColors,linearPositions, Shader.TileMode.MIRROR);
        paint.setShader(null);
        paint.setShader(linearGradient1);
        canvas.drawPath(path,paint);
        paint.setShader(null);
        paint.setShader(linearGradient2);
        canvas.drawRect(rectF,paint);

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

        for (int i = -90; i < 270; i += 1) {
            float startX;
            float startY;
            float endX;
            float endY;
            float paintWidth;
            if (i % 30 == 0) {
                paint.setColor(Color.BLACK);
                startX = cX + (dialRadius - largeMarkLength) * (float) (Math.cos(i * Math.PI / 180));
                startY = cY + (dialRadius - largeMarkLength) * (float) (Math.sin(i * Math.PI / 180));
                endX = cX + (dialRadius) * (float) (Math.cos(i * Math.PI / 180));
                endY = cY + (dialRadius) * (float) (Math.sin(i * Math.PI / 180));
                paintWidth = largeMarkLength/5;

                if(i == -90){
                    paint.setStrokeWidth(textSize/10);
                    paint.setColor(markColor);
                    canvas.drawText("60",cX,cY-dialRadius+largeMarkLength*2+textSize/3,paint);
                }else if(i == 0){
                    paint.setStrokeWidth(textSize/10);
                    paint.setColor(markColor);
                    canvas.drawText("15",cX + dialRadius-largeMarkLength*2,cY+textSize/3,paint);
                }else if(i == 90){
                    paint.setStrokeWidth(textSize/10);
                    paint.setColor(markColor);
                    canvas.drawText("30",cX ,cY + dialRadius-largeMarkLength*2+textSize/3,paint);
                }else if(i == 180){
                    paint.setStrokeWidth(textSize/10);
                    paint.setColor(markColor);
                    canvas.drawText("45",cX-dialRadius+largeMarkLength*2 ,cY+textSize/3,paint);
                }

            }else if(i%5 == 0){
                paint.setColor(markColor);
                paintWidth = middleMarkLength/5;
                startX = cX + (dialRadius - middleMarkLength) * (float) (Math.cos(i * Math.PI / 180));
                startY = cY + (dialRadius - middleMarkLength) * (float) (Math.sin(i * Math.PI / 180));
                endX = cX + (dialRadius) * (float) (Math.cos(i * Math.PI / 180));
                endY = cY + (dialRadius) * (float) (Math.sin(i * Math.PI / 180));
            }else {
                paint.setColor(Color.BLACK);
                paintWidth = smallMarkLength/5;
                startX = cX + (dialRadius - smallMarkLength) * (float) (Math.cos(i * Math.PI / 180));
                startY = cY + (dialRadius - smallMarkLength) * (float) (Math.sin(i * Math.PI / 180));
                endX = cX + (dialRadius) * (float) (Math.cos(i * Math.PI / 180));
                endY = cY + (dialRadius) * (float) (Math.sin(i * Math.PI / 180));
            }
            paint.setStrokeWidth(paintWidth);
            canvas.drawLine(startX,startY,endX,endY,paint);
        }
    }

    private void drawCenterBlackPoint(Canvas canvas){
        paint.setShader(null);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(cX,cY,dialRadius/10,paint);
    }

    private void drawCenterRedPoint(Canvas canvas){
        paint.setShader(null);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(cX,cY,dialRadius/20,paint);
    }


    private void drawIndicator(Canvas canvas){
        float millisAngle = calculateIndicatorAngle();
        float millisStartX = cX - backIndicatorLength*(float) (Math.cos(millisAngle * Math.PI / 180));
        float millisStartY = cY - backIndicatorLength*(float) (Math.sin(millisAngle * Math.PI / 180));
        float millisEndX = cX + secondIndicatorLength*(float) (Math.cos(millisAngle * Math.PI / 180));
        float millisEndY = cY + secondIndicatorLength*(float) (Math.sin(millisAngle * Math.PI / 180));

        paint.setShader(null);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(secondIndicatorWidth);
        canvas.drawLine(millisStartX,millisStartY,millisEndX,millisEndY,paint);
    }


    private float calculateIndicatorAngle(){
        Calendar calendar = Calendar.getInstance();
        int millis = calendar.get(Calendar.MILLISECOND);
        Log.e("millis",millis+"");
        return (float) millis/1000*360;
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
