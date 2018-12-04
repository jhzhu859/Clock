package com.yzl.clock.fragment;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yzl.clock.R;
import com.yzl.clock.activity.ActivityAddAlarmClock;
import com.yzl.clock.clockview.AlarmClockView;
import com.yzl.clock.clockview.MathUtil;
import com.yzl.clock.clockview.ViewUtil;
import com.yzl.clock.database.ClockOpenHelper;
import com.yzl.clock.manager.AppBarStateManager;
import com.yzl.clock.model.AlarmClock;
import com.yzl.clock.widget.Button;
import com.yzl.clock.widget.Switch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/19.
 */

public class FragmentAlarmClock extends Fragment {

    private View mView;

    private ClockOpenHelper mClockOpenHelper;
    private LinearLayout mLinearLayout;
    private AppBarLayout appBarLayout;
    private AlarmClockView alarmClockView;
    private Toolbar mToolBar;
    FloatingActionButton mFabButton;
    private boolean isCreated = false;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            updateLog((AlarmClock) msg.obj);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mClockOpenHelper = new ClockOpenHelper(getContext());
        isCreated = true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_alarm_clock, null);
        mFabButton = mView.findViewById(R.id.fab_add);
        mLinearLayout = mView.findViewById(R.id.ll_content);
        alarmClockView = mView.findViewById(R.id.alarmClockView);
        appBarLayout =  mView.findViewById(R.id.appBarLayout);
        mToolBar = mView.findViewById(R.id.toolBar);
        int toolBarHeight = MathUtil.getToolbarHeight(getContext());
        mToolBar.setMinimumHeight(toolBarHeight);
        initScrollListener();
        mFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ActivityAddAlarmClock.class));
            }
        });


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
                alarmClockView.setScaleX(clockRatio + (1-clockRatio)*0.7f);
                alarmClockView.setScaleY(clockRatio + (1-clockRatio)*0.7f);
            }
        });

    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        queryLog();
    }

    private void queryLog() {
        List<AlarmClock> alarmClockList = new ArrayList<>();
        SQLiteDatabase db = mClockOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(ClockOpenHelper.AlarmClockTable.TABLE, null, null, null, null, null, null);// 注意空格！
        int openIndex = cursor.getColumnIndex(ClockOpenHelper.AlarmClockTable.OPEN);
        int daysIndex = cursor.getColumnIndex(ClockOpenHelper.AlarmClockTable.DAYS);
        int idIndex = cursor.getColumnIndex(ClockOpenHelper.AlarmClockTable.ID);
        int timeIndex = cursor.getColumnIndex(ClockOpenHelper.AlarmClockTable.TIME);
        while (cursor.moveToNext()) {
            String time = cursor.getString(timeIndex);
            String days = cursor.getString(daysIndex);
            int open = cursor.getInt(openIndex);
            int id = cursor.getInt(idIndex);
            alarmClockList.add(new AlarmClock(id,time,days,open == 1));
        }
        cursor.close();
        mLinearLayout.removeAllViews();
        for (int i = 0;i<alarmClockList.size();i++){
            mLinearLayout.addView(createView(alarmClockList.get(i)));
            mLinearLayout.addView(ViewUtil.createDivider(getContext()));
        }
    }

    private void updateLog(AlarmClock alarmClock){
        SQLiteDatabase db = mClockOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ClockOpenHelper.AlarmClockTable.OPEN,alarmClock.isOpen());
        int count = db.update(ClockOpenHelper.AlarmClockTable.TABLE, values, ClockOpenHelper.AlarmClockTable.ID + " = ?", new String[]{alarmClock.getId()+""});
        db.close();
    }

    private View createView(final AlarmClock alarmClock){
        View convertView = View.inflate(getContext(),R.layout.item_alarm_clock,null);
        TextView tvTime = convertView.findViewById(R.id.tv_time);
        TextView tvAmpm = convertView.findViewById(R.id.tv_ampm);
        TextView tvDays = convertView.findViewById(R.id.tv_days);
        TextView tvToAlarm = convertView.findViewById(R.id.tv_to_alarm);
        Switch swIsOpen = convertView.findViewById(R.id.sw_isOpen);

        String time = alarmClock.getTime();
        String ampm = Integer.parseInt(time.split(":")[0])>12?"下午":"上午";
        String days = alarmClock.getDays();
        boolean open = alarmClock.isOpen();

        tvTime.setText(time);
        tvAmpm.setText(ampm);
        tvDays.setText(days);
        swIsOpen.setChecked(open);

        swIsOpen.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckChanged(Switch s, boolean checked) {
                alarmClock.setOpen(checked);
                Message.obtain(mHandler,1,alarmClock).sendToTarget();
            }
        });

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDeleteDialog(alarmClock);
                return true;
            }
        });
        return convertView;
    };

    private void showDeleteDialog(final AlarmClock alarmClock){
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("删除该闹钟？")//设置对话框的标题
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteClock(alarmClock);
                        dialog.dismiss();
                    }
                }).create();

        dialog.show();
    }

    private void deleteClock(AlarmClock alarmClock){
        SQLiteDatabase db = mClockOpenHelper.getWritableDatabase();
        //要删除的数据
        int index = db.delete(ClockOpenHelper.AlarmClockTable.TABLE,"id = ?",new String[]{alarmClock.getId()+""});
        db.close();
        if (index>0){
            queryLog();
        }
    }

}
