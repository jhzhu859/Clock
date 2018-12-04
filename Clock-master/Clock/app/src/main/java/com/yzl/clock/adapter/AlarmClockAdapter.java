package com.yzl.clock.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yzl.clock.R;
import com.yzl.clock.model.AlarmClock;
import com.yzl.clock.widget.Switch;

import java.util.List;

/**
 * Created by Kingc on 2018/7/23.
 */

public class AlarmClockAdapter extends BaseAdapter {

    private List<AlarmClock> list;
    private Context context;
    private Handler handler;

    public AlarmClockAdapter(List<AlarmClock> list, Context context, Handler handler) {
        this.list = list;
        this.context = context;
        this.handler = handler;
    }

    @Override
    public int getCount() {
        return list == null?0:list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final AlarmClock alarmClock = list.get(position);

        if(convertView == null){
            convertView = View.inflate(context, R.layout.item_alarm_clock,null);
        }

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
                Message.obtain(handler,1,alarmClock).sendToTarget();
            }
        });

        return convertView;
    }
}
