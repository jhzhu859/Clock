package com.yzl.clock;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.yzl.clock.fragment.FragmentAlarmClock;
import com.yzl.clock.fragment.FragmentCountDown;
import com.yzl.clock.fragment.FragmentStopWatch;
import com.yzl.clock.fragment.FragmentTimeClock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kingc on 2018/6/30.
 */

public class TimeActivity extends AppCompatActivity {


    private String [] sTitles = {"闹钟","时钟","秒表","计时"};
    private TimePagerAdapter mTimePagerAdapter;

    private ViewPager mViewPager;
    private TabLayout mTabLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        mViewPager = (ViewPager) findViewById(R.id.container);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTimePagerAdapter = new TimePagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mTimePagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }


    public class TimePagerAdapter extends FragmentPagerAdapter {

        public List<Fragment> fragmentList = new ArrayList<Fragment>();

        public TimePagerAdapter(FragmentManager fm) {
            super(fm);
            fragmentList.add(new FragmentAlarmClock());
            fragmentList.add(new FragmentTimeClock());
            fragmentList.add(new FragmentStopWatch());
            fragmentList.add(new FragmentCountDown());
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return sTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return sTitles[position];
        }
    }


}
