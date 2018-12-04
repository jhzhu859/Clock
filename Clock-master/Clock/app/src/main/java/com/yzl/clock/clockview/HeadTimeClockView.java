package com.yzl.clock.clockview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import java.util.Calendar;

/**
 * Created by Kingc on 2018/7/25.
 */

public class HeadTimeClockView extends HeadClockView {
    public HeadTimeClockView(Context context) {
        super(context);
    }

    public HeadTimeClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HeadTimeClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public HeadTimeClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected String calculateText() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        String hourText = hour<10?"0"+hour:""+hour;
        String minuteText = minute<10?"0"+minute:""+minute;
        String secondText = second<10?"0"+second:""+second;

        String timeText = hourText + ":" + minuteText + ":"+ secondText;
        return timeText;
    }
}
