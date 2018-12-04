package com.yzl.clock.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.yzl.clock.clockview.MathUtil;

/**
 * Created by Administrator on 2018/6/12.
 */

public class AlarmClockDrawable extends Drawable {

    protected float h;
    protected float w;

    protected float cX;
    protected float cY;

    protected float circleWidth;
    protected float shadowWidth;

    protected float margin;
    protected float centerRadius;
    protected float outerRadius;
    protected float innerRadius;
    protected float dialRadius;
    protected float largeIndicatorLength;
    protected float smallIndicatorLength;
    protected float hourIndicatorLength;
    protected float minuteIndicatorLength;
    protected float secondIndicatorLength;
    protected float mYOffset;
    protected float textX;
    protected float textY;
    protected float timeTextSize;
    protected float mIndicatorShadowOffset;


    protected float hourIndicatorWidth;
    protected float minuteIndicatorWidth;
    protected float secondIndicatorWidth;

    protected float backIndicatorLength;

    protected float indicatorTextSize;

    protected int color1 = Color.argb(255, 0xff, 0xff, 0xff);
    protected int color2 = Color.argb(255, 0xff, 0xff, 0xff);
    protected int color3 = Color.argb(255, 0xb4, 0xb4, 0xb4);
    protected int edgeColor = Color.argb(0xff, 0xb4, 0xb4, 0xb4);
    protected int centerColor = Color.argb(0xff, 0xff, 0xff, 0xff);
    protected int markColor = Color.argb(255, 0xb2, 0x22, 0x22);
    protected int mIndicatorShadowColor = 0x66808080;

    protected int topColor = Color.rgb(0x30,0x3f,0x9f);
    protected int bottomColor = Color.rgb(0x3f,0x51,0xb5);
    protected LinearGradient linearGradient;

    protected RectF bgRectF;
    protected RectF outerRectF;

    protected Paint paint;

    protected float[] circleStops = new float[2];
    protected int[] outerColors = new int[]{color2, color3, color2, color1, color2};
    protected int[] innerColors = new int[]{color2, color1, color2, color3, color2};

    protected float[] outerPositions = new float[]{0f, 0.25f, 0.5f, 0.75f, 1f};
    protected float[] innerPositions = new float[]{0f, 0.25f, 0.5f, 0.75f, 1f};

    public AlarmClockDrawable(float w, float h) {
        this.h = h;
        this.w = w;

        float scale = Math.min(h,w);

        paint = new Paint();
        paint.setAntiAlias(true);

        cX = w / 2;
        cY = h / 2;

        margin = scale / 6;
        mYOffset = scale / 12;
        circleWidth = scale / 25;
        shadowWidth = scale / 160;
        cY = cY - mYOffset;
        timeTextSize = margin/2;
        textX = cX;
        textY = h - margin/4*3+timeTextSize/3;

        outerRadius  =  scale / 2 - margin - shadowWidth / 2;
        centerRadius =  scale / 2 - margin - shadowWidth - circleWidth / 2;
        innerRadius  =  scale / 2 - margin - shadowWidth - circleWidth - shadowWidth / 2;
        dialRadius   =  scale / 2 - margin - shadowWidth - circleWidth - shadowWidth;

        largeIndicatorLength = dialRadius / 8;
        smallIndicatorLength = dialRadius / 16;
        indicatorTextSize = dialRadius/6;

        secondIndicatorLength = dialRadius*0.9f;
        minuteIndicatorLength = dialRadius*0.7f;
        hourIndicatorLength = dialRadius*0.5f;
        mIndicatorShadowOffset = dialRadius*0.05f;
        backIndicatorLength = dialRadius*0.2f;

        hourIndicatorWidth = dialRadius/8/5*2;
        minuteIndicatorWidth = dialRadius/8/5;
        secondIndicatorWidth = dialRadius/8/5/2;

        circleStops[0] = (centerRadius - circleWidth) / (centerRadius + circleWidth / 2);
        circleStops[1] = 1;

        bgRectF = new RectF(0, 0, w, h);

        outerRectF = new RectF(
                margin + shadowWidth / 2 + w*(1- MathUtil.sizeRatio)/2,
                margin + shadowWidth / 2 - mYOffset,
                w - margin - shadowWidth / 2 - w*(1- MathUtil.sizeRatio)/2,
                h - margin - shadowWidth / 2 - mYOffset );

    }

    @Override
    public void draw(@NonNull Canvas canvas) {
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
