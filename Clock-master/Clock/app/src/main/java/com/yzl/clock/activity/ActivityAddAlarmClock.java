package com.yzl.clock.activity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yzl.clock.R;
import com.yzl.clock.database.ClockOpenHelper;
import com.yzl.clock.manager.PickerLayoutManager;
import com.yzl.clock.widget.Button;
import com.yzl.clock.widget.CheckBox;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Kingc on 2018/7/21.
 */

public class ActivityAddAlarmClock extends AppCompatActivity {

    private RecyclerView mHourRecyclerView;
    private RecyclerView mMinuteRecyclerView;

    private PickerLayoutManager mHourPickerLayoutManager;
    private PickerLayoutManager mMinutePickerLayoutManager;

    private TextView mHourTextView;
    private TextView mMinuteTextView;

    private CheckBox mWeekDayCheckBox;
    private CheckBox mAllDayCheckBox;

    private Button mConfirmButton;

    private ClockOpenHelper mClockOpenHelper;


    private static List<String> mHours = new ArrayList<>();
    private static List<String> mMinutes = new ArrayList<>();
    static {
        for (int i = 0; i <= 23 ; i++) {
            if (i <= 9){
                mHours.add("0"+i);
            }else {
                mHours.add(i + "");
            }
        }

        for (int i = 0; i < 60; i++) {
            if (i <= 9){
                mMinutes.add("0"+i);
            }else {
                mMinutes.add(i + "");
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm_clock);
        mHourTextView = (TextView) findViewById(R.id.tv_hour);
        mMinuteTextView = (TextView) findViewById(R.id.tv_minute);
        mHourRecyclerView = (RecyclerView) findViewById(R.id.recycleHour);
        mMinuteRecyclerView = (RecyclerView)findViewById(R.id.recycleMinute);
        mWeekDayCheckBox = (CheckBox) findViewById(R.id.cb_week_day);
        mAllDayCheckBox = (CheckBox) findViewById(R.id.cb_all_day);
        mConfirmButton = (Button) findViewById(R.id.btn_confirm);
        mHourPickerLayoutManager = new PickerLayoutManager(this,mHourRecyclerView, PickerLayoutManager.VERTICAL, false,5,0.4f,true);
        mHourRecyclerView.setLayoutManager(mHourPickerLayoutManager);
        mHourRecyclerView.setAdapter(new MyAdapter(mHours));
        mMinutePickerLayoutManager = new PickerLayoutManager(this,mMinuteRecyclerView, PickerLayoutManager.VERTICAL, false,5,0.4f,true);
        mMinuteRecyclerView.setLayoutManager(mMinutePickerLayoutManager);
        mMinuteRecyclerView.setAdapter(new MyAdapter(mMinutes));
        initListener();
        mClockOpenHelper = new ClockOpenHelper(this);

    }


    private void initListener(){
        mHourPickerLayoutManager.OnSelectedViewListener(new PickerLayoutManager.OnSelectedViewListener() {
            @Override
            public void onSelectedView(View view, int position) {
                TextView textView = (TextView) view;
                if (textView != null)mHourTextView.setText(textView.getText());
            }
        });

        mMinutePickerLayoutManager.OnSelectedViewListener(new PickerLayoutManager.OnSelectedViewListener() {
            @Override
            public void onSelectedView(View view, int position) {
                TextView textView = (TextView) view;
                if (textView != null)mMinuteTextView.setText(textView.getText());
            }
        });

        mWeekDayCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public boolean OnCheckedChanged(CheckBox checkBox, boolean checked) {
                if (checked){
                    mAllDayCheckBox.setChecked(false);
                }
                return false;
            }
        });

        mAllDayCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public boolean OnCheckedChanged(CheckBox checkBox, boolean checked) {
                if (checked){
                    mWeekDayCheckBox.setChecked(false);
                }
                return false;
            }
        });

        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert();
            }
        });

    }





    private void insert(){

        if (mAllDayCheckBox.isChecked()&&mWeekDayCheckBox.isChecked()){
            Toast.makeText(this,"工作日或者所有天只能选择一个",Toast.LENGTH_SHORT).show();
            return;
        }

        if (!mAllDayCheckBox.isChecked()&&!mWeekDayCheckBox.isChecked()){
            Toast.makeText(this,"工作日或者所有天必须选择一个",Toast.LENGTH_SHORT).show();
            return;
        }

        String days = mWeekDayCheckBox.isChecked()?"工作日":"所有天";
        String hour = mHourTextView.getText().toString().trim();
        String minute = mMinuteTextView.getText().toString().trim();
        String time = hour + ":"+ minute;

        ContentValues contentValues = new ContentValues();
        contentValues.put(ClockOpenHelper.AlarmClockTable.DAYS,days);
        contentValues.put(ClockOpenHelper.AlarmClockTable.TIME,time);
        contentValues.put(ClockOpenHelper.AlarmClockTable.OPEN,true);
        SQLiteDatabase db = mClockOpenHelper.getWritableDatabase();
        Long index = db.insert(ClockOpenHelper.AlarmClockTable.TABLE, null, contentValues);

        if (index>0){Log.e("AlarmClock","insert success");}

        onBackPressed();
    }


    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
        private List<String> mList ;
        public MyAdapter(List<String> list) {
            this.mList = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ActivityAddAlarmClock.this).inflate(R.layout.item_hour_minute,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.tvText.setText(mList.get(position));
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            TextView tvText;
            public ViewHolder(View itemView) {
                super(itemView);
                tvText = itemView.findViewById(R.id.tv_hour_minute);
            }
        }
    }


}
