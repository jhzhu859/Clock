package com.yzl.clock.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yzl.clock.R;
import com.yzl.clock.clockview.TimeZoneView;
import com.yzl.clock.model.CityItem;

import java.util.List;

/**
 * Created by Kingc on 2018/7/13.
 */

public class TimeZoneAdapter extends BaseAdapter {

    private List<CityItem> list;
    private Context mContext;

    public TimeZoneAdapter(List<CityItem> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
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
    @TargetApi(21)
    public View getView(int position, View convertView, ViewGroup parent) {
        CityItem item = list.get(position);
        View view = View.inflate(mContext,R.layout.item_time_zone,null);
        TimeZoneView timeZoneView = view.findViewById(R.id.time_zone_view);
        timeZoneView.setCity(item);
        return view;
    }

}
