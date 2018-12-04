package com.yzl.clock;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.yzl.clock.fragment.FragmentStopWatch;
import com.yzl.clock.fragment.FragmentTimeClock;


public class MainActivity extends AppCompatActivity {

    FrameLayout mContainer;
    BottomNavigationBar mTab;

    private FragmentTimeClock mFragment1;
    private FragmentStopWatch mFragment2;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragmentManager = getSupportFragmentManager();
        if (savedInstanceState != null) {
            mFragment1 = (FragmentTimeClock) mFragmentManager.findFragmentByTag("fragment1");
            mFragment2 = (FragmentStopWatch) mFragmentManager.findFragmentByTag("fragment4");
        }
        mContainer = (FrameLayout) findViewById(R.id.container);
        mTab = (BottomNavigationBar)findViewById(R.id.tab);
        initTab();
    }

    private void initTab(){
        mTab.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mTab.setMode(BottomNavigationBar.MODE_FIXED);
        mTab.addItem(new BottomNavigationItem(R.drawable.timeclock2, "时钟")
                        .setInactiveIconResource(R.drawable.timeclock1)
                        .setActiveColorResource(R.color.colorPrimary)
                        .setInActiveColorResource(R.color.colorBottomInActive))
                .addItem(new BottomNavigationItem(R.drawable.stopwatch2, "秒表")
                        .setInactiveIconResource(R.drawable.stopwatch1)
                        .setActiveColorResource(R.color.colorPrimary)
                        .setInActiveColorResource(R.color.colorBottomInActive))
                .setFirstSelectedPosition(0)
                .initialise();
        mTab.setTabSelectedListener(mOnTabSelectedListener);
        f1();
    }


    private BottomNavigationBar.OnTabSelectedListener mOnTabSelectedListener = new BottomNavigationBar.OnTabSelectedListener() {
        @Override
        public void onTabSelected(int position) {
            switch (position) {
                case 0:
                    f1();
                    break;
                case 1:
                    f2();
                    break;
            }
        }

        @Override
        public void onTabUnselected(int position) {

        }

        @Override
        public void onTabReselected(int position) {
            switch (position) {
                case 0:
                    f1();
                    break;
                case 1:
                    f2();
                    break;
            }
        }
    };


    private void f1(){

        if (mFragment2 != null) {
            mFragmentManager.beginTransaction().hide(mFragment2).commit();
        }

        if (mFragment1 == null) {
            mFragment1 = new FragmentTimeClock();
            mFragmentManager.beginTransaction().add(R.id.container, mFragment1, "fragment1").commit();
        } else {
            mFragmentManager.beginTransaction().show(mFragment1).commit();
        }

    }

    private void f2(){

        if (mFragment1 != null) {
            mFragmentManager.beginTransaction().hide(mFragment1).commit();
        }

        if (mFragment2 == null) {
            mFragment2 = new FragmentStopWatch();
            mFragmentManager.beginTransaction().add(R.id.container, mFragment2, "fragment2").commit();
        } else {
            mFragmentManager.beginTransaction().show(mFragment2).commit();
        }
    }

}
