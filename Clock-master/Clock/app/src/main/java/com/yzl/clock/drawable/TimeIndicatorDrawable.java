package com.yzl.clock.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Calendar;

/**
 * Created by Administrator on 2018/6/12.
 */

public class TimeIndicatorDrawable extends TimeClockDrawable {





    public TimeIndicatorDrawable(float w, float h) {
        super(w,h);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        drawIndicator(canvas);
        drawCenterPoint(canvas);
        invalidateSelf();
    }


    private void drawIndicator(Canvas canvas){

        float [] result = calculateIndicatorAngle();

        float hourAngle = result[0];
        float minuteAngle = result[1];
        float secondAngle = result[2];

        int hour = (int)result[3];
        int minute = (int)result[4];
        int second = (int)result[5];

        String hourText = hour<10?"0"+hour:""+hour;
        String minuteText = minute<10?"0"+minute:""+minute;
        String secondText = second<10?"0"+second:""+second;

        String timeText = hourText + ":" + minuteText + ":"+ secondText;

        float hourStartX = cX - backIndicatorLength*(float) (Math.cos(hourAngle * Math.PI / 180));
        float hourStartY = cY - backIndicatorLength*(float) (Math.sin(hourAngle * Math.PI / 180));
        float hourEndX = cX + hourIndicatorLength*(float) (Math.cos(hourAngle * Math.PI / 180));
        float hourEndY = cY + hourIndicatorLength*(float) (Math.sin(hourAngle * Math.PI / 180));

        float minuteStartX = cX - backIndicatorLength*(float) (Math.cos(minuteAngle * Math.PI / 180));
        float minuteStartY = cY - backIndicatorLength*(float) (Math.sin(minuteAngle * Math.PI / 180));
        float minuteEndX = cX + minuteIndicatorLength*(float) (Math.cos(minuteAngle * Math.PI / 180));
        float minuteEndY = cY + minuteIndicatorLength*(float) (Math.sin(minuteAngle * Math.PI / 180));

        float secondStartX = cX - backIndicatorLength*(float) (Math.cos(secondAngle * Math.PI / 180));
        float secondStartY = cY - backIndicatorLength*(float) (Math.sin(secondAngle * Math.PI / 180));
        float secondEndX = cX + secondIndicatorLength*(float) (Math.cos(secondAngle * Math.PI / 180));
        float secondEndY = cY + secondIndicatorLength*(float) (Math.sin(secondAngle * Math.PI / 180));

        paint.setShader(null);

        paint.setStrokeCap(Paint.Cap.ROUND);

        //画指针
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(hourIndicatorWidth);
        canvas.drawLine(hourStartX,hourStartY,hourEndX,hourEndY,paint);
        paint.setStrokeWidth(minuteIndicatorWidth);
        canvas.drawLine(minuteStartX,minuteStartY,minuteEndX,minuteEndY,paint);
        paint.setStrokeWidth(secondIndicatorWidth);
        canvas.drawLine(secondStartX,secondStartY,secondEndX,secondEndY,paint);
        //画阴影
        paint.setColor(mIndicatorShadowColor);
        paint.setStrokeWidth(hourIndicatorWidth/2);
        canvas.drawLine(hourStartX,hourStartY+mIndicatorShadowOffset,hourEndX,hourEndY+mIndicatorShadowOffset,paint);
        paint.setStrokeWidth(minuteIndicatorWidth/2);
        canvas.drawLine(minuteStartX,minuteStartY+mIndicatorShadowOffset,minuteEndX,minuteEndY+mIndicatorShadowOffset,paint);
        paint.setStrokeWidth(secondIndicatorWidth/2);
        canvas.drawLine(secondStartX,secondStartY+mIndicatorShadowOffset,secondEndX,secondEndY+mIndicatorShadowOffset,paint);

        paint.setColor(Color.BLACK);
        drawTimeText(canvas,timeText);
    }

    private void drawCenterPoint(Canvas canvas){
        paint.setShader(null);
        paint.setColor(Color.parseColor("#ff0000"));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(cX,cY,dialRadius/20,paint);
    }

    private void drawTimeText(Canvas canvas,String text){
        paint.setShader(null);
        paint.setTextSize(timeTextSize);
        paint.setColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(text,textX,textY,paint);
    }


    private float[] calculateIndicatorAngle(){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        float hourAngle1 = (float)(hour%12)*30;
        float hourAngle2 = (float)(minute%60)/60*30;
        float hourAngle3 = (float)second/3600*30;
        float hourAngle = hourAngle1 + hourAngle2 + hourAngle3 - 90;
        float minuteAngle2 = (float)(minute%60)/60*360;
        float minuteAngle3 = (float)(second%60)/60*5;
        float minuteAngle =  minuteAngle2 + minuteAngle3- 90;
        float secondAngle3 = (float)(second%60)/60*360;
        float secondAngle =   secondAngle3 - 90;
        float[] result = new float[]{hourAngle,minuteAngle,secondAngle,hour,minute,second};
        return result;
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
