package com.yzl.clock.clockview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yzl.clock.model.CityItem;

import java.sql.Time;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Kingc on 2018/7/11.
 */

public class TimeZoneView extends View {

    private float mWidth;
    private float mHeight;
    private float mScale;
    private float mCircleRadius;
    private float mPointRadius;
    private float mCircleWidth;
    private float mHourIndicatorLength;
    private float mMinuteIndicatorLength;
    private float mHourIndicatorWidth;
    private float mMinuteIndicatorWidth;
    private float mLargeTextSize;
    private float mSmallTextSize;
    private float mLargeTextX;
    private float mLargeTextY;
    private float mSmallTextX;
    private float mSmallTextY;
    private int mColor = 0xffb22222;

    private float mCenterX;
    private float mCenterY;

    private String mCityId = "America/New_York";
    private String mCityName = "美国/纽约";

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            invalidate();
        }
    };

    public TimeZoneView(Context context) {
        super(context);

    }

    public TimeZoneView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeZoneView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getWidth();
        mHeight = getHeight();
        mScale = Math.min(mWidth,mHeight);
        mCenterY = mScale/2;
        mCenterX = mScale;
        mCircleWidth = mScale*0.05f;
        mCircleRadius = mScale/2 - mCircleWidth/2;
        mPointRadius = mScale*0.1f;
        mHourIndicatorWidth = mCircleWidth;
        mMinuteIndicatorWidth = mCircleWidth*0.7f;
        mHourIndicatorLength = (mScale/2 - mCircleWidth)*0.7f;
        mMinuteIndicatorLength = (mScale/2 - mCircleWidth)*0.8f;
        mLargeTextSize = mScale/3;
        mSmallTextSize = mScale/5;
        mLargeTextX = mScale + mScale;
        mLargeTextY = mScale / 3 + mScale/12;
        mSmallTextX = mScale + mScale;
        mSmallTextY = mScale/2 + mScale/6 + mScale/6;;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        TimeInfo timeInfo = calculate();
        drawImage(canvas,timeInfo);
        drawText(canvas,timeInfo);
        mHandler.sendEmptyMessageDelayed(1,2*1000);
    }


    private void drawImage(Canvas canvas,TimeInfo timeInfo){
        mPaint.setColor(mColor);
        float hourAngle = timeInfo.getHourAngle();
        float minuteAngle = timeInfo.getMinuteAngle();
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mCenterX,mCenterY,mPointRadius,mPaint);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mCircleWidth);
        canvas.drawCircle(mCenterX,mCenterY,mCircleRadius,mPaint);

        float hourStartX = mCenterX;
        float hourStartY = mCenterY;
        float hourEndX = mCenterX + mHourIndicatorLength*(float) (Math.cos(hourAngle * Math.PI / 180));
        float hourEndY = mCenterY + mHourIndicatorLength*(float) (Math.sin(hourAngle * Math.PI / 180));

        float minuteStartX = mCenterX;
        float minuteStartY = mCenterY;
        float minuteEndX = mCenterX + mMinuteIndicatorLength*(float) (Math.cos(minuteAngle * Math.PI / 180));
        float minuteEndY = mCenterY + mMinuteIndicatorLength*(float) (Math.sin(minuteAngle * Math.PI / 180));

        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(mHourIndicatorWidth);
        canvas.drawLine(hourStartX,hourStartY,hourEndX,hourEndY,mPaint);
        mPaint.setStrokeWidth(mMinuteIndicatorWidth);
        canvas.drawLine(minuteStartX,minuteStartY,minuteEndX,minuteEndY,mPaint);
    }

    private void drawText(Canvas canvas,TimeInfo timeInfo){

        String pm = "上午";

        mPaint.setColor(Color.BLACK);
        String hourAndMinute = "";
        String hour = timeInfo.getHour()+"";
        hour = hour.length() == 1?"0"+hour:hour;
        String minute = timeInfo.getMinute()+"";
        minute = minute.length() == 1?"0"+minute:minute;
        hourAndMinute = hour + ":" + minute;

        hourAndMinute = timeInfo.getHour()<12?"上午    "+hourAndMinute:"下午    "+hourAndMinute;
        String ymd = timeInfo.getYear()+"年"+timeInfo.getMonth()+"月"+timeInfo.getDay()+"日";

        String otherInfo = ymd + "    " +mCityName;
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(mLargeTextSize);
        mPaint.setStrokeWidth(mLargeTextSize/10);
        canvas.drawText(hourAndMinute,mLargeTextX,mLargeTextY,mPaint);
        mPaint.setTextSize(mSmallTextSize);
        mPaint.setStrokeWidth(mSmallTextSize/10);
        canvas.drawText(otherInfo,mSmallTextX,mSmallTextY,mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mScale>0){
            setMeasuredDimension(Math.round(mWidth),Math.round(mHeight));
        }
    }

    private TimeInfo calculate(){

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone(mCityId));
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        float hourAngle1 = (float)(hour%12)*30;
        float hourAngle2 = (float)(minute%60)/60*30;
        float hourAngle = hourAngle1 + hourAngle2  - 90;
        float minuteAngle = (float)minute/60*360 - 90;

        TimeInfo timeInfo = new TimeInfo(year,month,day,week,hour,minute,hourAngle,minuteAngle);
        return timeInfo;
    }

    public void setCity(CityItem cityItem){
        mCityId = cityItem.getTimeId();
        mCityName = cityItem.getTimeName();
        invalidate();
    }


    private class TimeInfo{
        private int year;
        private int month;
        private int day;
        private int week;
        private int hour;
        private int minute;

        private float hourAngle;
        private float minuteAngle;

        public TimeInfo(int year, int month, int day, int week, int hour, int minute,float hourAngle,float minuteAngle) {
            this.year = year;
            this.month = month;
            this.day = day;
            this.week = week;
            this.hour = hour;
            this.minute = minute;
            this.hourAngle = hourAngle;
            this.minuteAngle = minuteAngle;
        }

        public int getYear() {
            return year;
        }

        public int getMonth() {
            return month;
        }

        public int getDay() {
            return day;
        }

        public int getWeek() {
            return week;
        }

        public int getHour() {
            return hour;
        }

        public int getMinute() {
            return minute;
        }

        public float getHourAngle() {
            return hourAngle;
        }

        public float getMinuteAngle() {
            return minuteAngle;
        }
    }

}
