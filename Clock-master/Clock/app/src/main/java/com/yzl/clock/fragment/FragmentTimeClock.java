package com.yzl.clock.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.yzl.clock.R;
import com.yzl.clock.activity.ActivityTimeZoneDetail;
import com.yzl.clock.clockview.MathUtil;
import com.yzl.clock.clockview.TimeClockView;
import com.yzl.clock.clockview.TimeZoneView;
import com.yzl.clock.clockview.ViewUtil;
import com.yzl.clock.manager.AppBarStateManager;
import com.yzl.clock.model.CityItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/19.
 */

public class FragmentTimeClock extends Fragment {

    private View mView;
    private List<CityItem> list = new ArrayList<>();
    private LinearLayout mLinearLayout;
    private AppBarLayout appBarLayout;
    private TimeClockView timeClockView;
    private Toolbar mToolBar;
    private FloatingActionButton mFabButton;
    private boolean isCreated = false;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list.add(new CityItem("中国/重庆","Asia/Chongqing"));
        list.add(new CityItem("美国/芝加哥","America/Chicago"));
        list.add(new CityItem("法国/摩纳哥","Europe/Monaco"));
        list.add(new CityItem("英国/伦敦","Europe/London"));
        list.add(new CityItem("丹麦/哥本哈根","Europe/Copenhagen"));
        list.add(new CityItem("美国/纽约","America/New_York"));
        list.add(new CityItem("俄罗斯/莫斯科","Europe/Moscow"));
        isCreated = true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_time_clock, null);
        mFabButton = mView.findViewById(R.id.fab_add);
        mLinearLayout = mView.findViewById(R.id.ll_content);
        timeClockView = mView.findViewById(R.id.timeClockView);
        appBarLayout =  mView.findViewById(R.id.appBarLayout);
        mToolBar = mView.findViewById(R.id.toolBar);
        int toolBarHeight = MathUtil.getToolbarHeight(getContext());
        mToolBar.setMinimumHeight(toolBarHeight);

        mFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"功能开发中~，敬请期待",Toast.LENGTH_SHORT).show();
            }
        });

        initScrollListener();
        mLinearLayout.removeAllViews();
        for (int i = 0;i<list.size();i++){
            mLinearLayout.addView(createView(list.get(i)));
            mLinearLayout.addView(ViewUtil.createDivider(getContext()));
        }
        return mView;
    }




    private void initScrollListener(){
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float range = appBarLayout.getTotalScrollRange();
                float offset = Math.abs(verticalOffset);
                float clockRatio = (range - offset)/range;
                clockRatio = clockRatio == 0f?1:clockRatio;
                timeClockView.setScaleX(clockRatio + (1-clockRatio)*0.7f);
                timeClockView.setScaleY(clockRatio + (1-clockRatio)*0.7f);
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private View createView(final CityItem cityItem){
        final View convertView = View.inflate(getContext(),R.layout.item_time_zone,null);
        TimeZoneView timeZoneView = convertView.findViewById(R.id.time_zone_view);
        timeZoneView.setCity(cityItem);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityTimeZoneDetail.class);
                Bundle options =  ActivityOptions.makeSceneTransitionAnimation(getActivity(), convertView, "zoneView").toBundle();
                Bundle bundle = new Bundle();
                bundle.putParcelable("cityItem",cityItem);
                intent.putExtra("cityBundle",bundle);
                startActivity(intent, options);
            }
        });
        return convertView;
    };

}
