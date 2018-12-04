package com.yzl.clock.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yzl.clock.R;
import com.yzl.clock.clockview.CountDownView;
import com.yzl.clock.clockview.MathUtil;
import com.yzl.clock.drawable.CountDownIndicatorDrawable;
import com.yzl.clock.manager.PickerLayoutManager;
import com.yzl.clock.widget.Button;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/19.
 */

public class FragmentCountDown extends Fragment {

    private View mView;

    private RecyclerView mHourRecyclerView;
    private RecyclerView mMinuteRecyclerView;
    private CardView mCardView;

    private PickerLayoutManager mHourPickerLayoutManager;
    private PickerLayoutManager mMinutePickerLayoutManager;
    private Button mButton;

    private CountDownView countDownView;
    private LinearLayout mLinearLayout;
    private AppBarLayout appBarLayout;
    private Toolbar mToolBar;

    private static List<String> mHours = new ArrayList<>();
    private static List<String> mMinutes = new ArrayList<>();

    private String mHourText = "00";
    private String mMinuteText = "00";
    private String durationText = "00:00:00";
    private String splitText = ":";

    private CountDownIndicatorDrawable mDrawable;
    private boolean isCreated = false;


    static {
        for (int i = 0; i <= 23; i++) {
            if (i <= 9) {
                mHours.add("0" + i);
            } else {
                mHours.add(i + "");
            }
        }

        for (int i = 0; i < 60; i++) {
            if (i <= 9) {
                mMinutes.add("0" + i);
            } else {
                mMinutes.add(i + "");
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isCreated = true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_count_down, null);
        countDownView = mView.findViewById(R.id.countDownView);
        mDrawable = countDownView.getIndicatorDrawable();
        mButton = mView.findViewById(R.id.btn_countDown);
        mCardView = mView.findViewById(R.id.cv_setTime);
        mHourRecyclerView = mView.findViewById(R.id.recycleHour);
        mMinuteRecyclerView = mView.findViewById(R.id.recycleMinute);
        appBarLayout =  mView.findViewById(R.id.appBarLayout);
        mToolBar = mView.findViewById(R.id.toolBar);
        int toolBarHeight = MathUtil.getToolbarHeight(getContext());
        mToolBar.setMinimumHeight(toolBarHeight);


        mHourPickerLayoutManager = new PickerLayoutManager(getContext(), mHourRecyclerView, PickerLayoutManager.VERTICAL, false, 5, 0.4f, true);
        mHourRecyclerView.setLayoutManager(mHourPickerLayoutManager);
        mHourRecyclerView.setAdapter(new MyAdapter(mHours));
        mMinutePickerLayoutManager = new PickerLayoutManager(getContext(), mMinuteRecyclerView, PickerLayoutManager.VERTICAL, false, 5, 0.4f, true);
        mMinuteRecyclerView.setLayoutManager(mMinutePickerLayoutManager);
        mMinuteRecyclerView.setAdapter(new MyAdapter(mMinutes));
        initListener();
        return mView;
    }







    private void initListener() {
        mHourPickerLayoutManager.OnSelectedViewListener(new PickerLayoutManager.OnSelectedViewListener() {
            @Override
            public void onSelectedView(View view, int position) {
                TextView textView = (TextView) view;
                if (textView != null) mHourText = textView.getText().toString().trim();
                mHourText = mHourText.length() == 1 ? 0 + mHourText : mHourText;
                durationText = mHourText + splitText + mMinuteText + splitText + "00";
                mDrawable.setDurationText(durationText);
            }
        });

        mMinutePickerLayoutManager.OnSelectedViewListener(new PickerLayoutManager.OnSelectedViewListener() {
            @Override
            public void onSelectedView(View view, int position) {
                TextView textView = (TextView) view;
                if (textView != null) mMinuteText = textView.getText().toString().trim();
                mMinuteText = mMinuteText.length() == 1 ? 0 + mMinuteText : mMinuteText;
                durationText = mHourText + splitText + mMinuteText + splitText + "00";
                mDrawable.setDurationText(durationText);
            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawable.setRunningState(!mDrawable.getRunningState());
                boolean running = mDrawable.getRunningState();
                String text = running ? "停止" : "开始";
                mCardView.setVisibility(running? View.GONE:View.VISIBLE);
                mButton.setText(text);
            }
        });
        String text = mDrawable.getRunningState() ? "停止" : "开始";
        mButton.setText(text);

        mDrawable.setStopListener(new CountDownIndicatorDrawable.OnStopListener() {
            @Override
            public void onStop() {
                boolean running = mDrawable.getRunningState();
                mButton.setText(running ? "停止" : "开始");
                mCardView.setVisibility(running? View.GONE:View.VISIBLE);
            }
        });


        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float range = appBarLayout.getTotalScrollRange();
                float offset = Math.abs(verticalOffset);
                float clockRatio = (range - offset)/range;
                clockRatio = clockRatio == 0f?1:clockRatio;
                countDownView.setScaleX(clockRatio + (1-clockRatio)*0.7f);
                countDownView.setScaleY(clockRatio + (1-clockRatio)*0.7f);
            }
        });

    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private List<String> mList;

        public MyAdapter(List<String> list) {
            this.mList = list;
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(FragmentCountDown.this.getContext()).inflate(R.layout.item_hour_minute, parent, false);
            return new MyAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
            holder.tvText.setText(mList.get(position));
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvText;

            public ViewHolder(View itemView) {
                super(itemView);
                tvText = itemView.findViewById(R.id.tv_hour_minute);
            }
        }
    }


    public static void setEnabled(View view, boolean enabled) {
        if(null == view) {
            return;
        }
        if(view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            LinkedList<ViewGroup> queue = new LinkedList<ViewGroup>();
            queue.add(viewGroup);
            // 遍历viewGroup
            while(!queue.isEmpty()) {
                ViewGroup current = queue.removeFirst();
                current.setEnabled(enabled);
                for(int i = 0; i < current.getChildCount(); i ++) {
                    if(current.getChildAt(i) instanceof ViewGroup) {
                        queue.addLast((ViewGroup) current.getChildAt(i));
                    }else {
                        current.getChildAt(i).setEnabled(enabled);
                    }
                }
            }
        }else {
            view.setEnabled(enabled);
        }
    }
}
