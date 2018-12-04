package com.yzl.clock.clockview;

import android.content.Context;
import android.view.View;

import com.yzl.clock.R;

/**
 * Created by Kingc on 2018/7/26.
 */

public class ViewUtil {
    public static View createDivider(Context context){
        View view = View.inflate(context, R.layout.divider,null);
        view.setMinimumHeight(MathUtil.dpToPx(context,1));
        return view;
    }
}
