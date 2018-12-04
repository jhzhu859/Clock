package com.yzl.clock.adapter;

import android.content.Context;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yzl.clock.R;
import com.yzl.clock.model.StopWatchLog;

import java.util.List;
import android.os.Handler;

/**
 * Created by Kingc on 2018/7/20.
 */

public class StopWatchAdapter extends BaseAdapter {

    private Context context;
    private List<StopWatchLog> list;
    private Handler mHandler;

    public StopWatchAdapter(Context context, List<StopWatchLog> list,Handler mHandler) {
        this.context = context;
        this.list = list;
        this.mHandler = mHandler;
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
        return list.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final StopWatchLog stopWatchLog = list.get(position);

        if (convertView == null){
            convertView = View.inflate(context, R.layout.item_stop_watch_log,null);
        }

        TextView name = convertView.findViewById(R.id.tv_name);
        TextView duration = convertView.findViewById(R.id.tv_duration);
        TextView time = convertView.findViewById(R.id.tv_time);
        ImageView cancel = convertView.findViewById(R.id.iv_cancel);

        name.setText(stopWatchLog.getName());
        duration.setText(stopWatchLog.getDuration());
        time.setText(stopWatchLog.getTime());

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message.obtain(mHandler,1,position,stopWatchLog.getId()).sendToTarget();
            }
        });
        return convertView;
    }

    public List<StopWatchLog> getList(){
        return list;
    }

}
