package com.yzl.clock.clockview;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;

/**
 * Created by Kingc on 2018/7/11.
 */

public class MathUtil {
    public static final float sizeRatio = 0.7f;

    public static int dpToPx(Context context, int dp){
        return (int)(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics()) + 0.5f);
    }

    public static int spToPx(Context context, int sp){
        return (int)(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics()) + 0.5f);
    }

    public static int getScreenWidth(Context context){
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getToolbarHeight(Context context){
        float height = getScreenWidth(context)*sizeRatio*0.25f;
        return (int)height;
    }

    //获取最大值和最小值之间的一个值
    public static float getFloatBetween(float value,float min,float max){
        value = Math.max(value,min);
        value = Math.min(value,max);
        return value;
    }

    //获取最大值和最小值之间的一个值
    public static int getIntBetween(int value,int min,int max){
        value = Math.max(value,min);
        value = Math.min(value,max);
        return value;
    }

    //获取控件在运动过程中的渐变色
    public static int getRunningColor(int start,int end,float fraction){
        int runningAlpha = getFractionValue(Color.alpha(start),Color.alpha(end),fraction);
        int runningRed = getFractionValue(Color.red(start),Color.red(end),fraction);
        int runningGreen = getFractionValue(Color.green(start),Color.green(end),fraction);
        int runningBlue = getFractionValue(Color.blue(start),Color.blue(end),fraction);
        int color = Color.argb(runningAlpha,runningRed,runningGreen,runningBlue);
        return color;
    }

    private static int getFractionValue(int start, int end, float fraction){
        int value = Math.round(end*fraction + start * (1-fraction));
        value = Math.min(value,255);
        return value;
    }

}
