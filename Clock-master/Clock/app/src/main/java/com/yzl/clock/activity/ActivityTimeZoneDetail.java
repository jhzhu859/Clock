package com.yzl.clock.activity;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yzl.clock.R;
import com.yzl.clock.clockview.TimeZoneView;
import com.yzl.clock.model.CityItem;

/**
 * Created by Kingc on 2018/7/13.
 */

public class ActivityTimeZoneDetail extends AppCompatActivity {

    private TimeZoneView timeZoneView;
    private ImageView imageView;
    private String zongId = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_zone_detail);
        timeZoneView = (TimeZoneView) findViewById(R.id.cityTime);
        imageView = (ImageView) findViewById(R.id.iv_city);
        Bundle bundle = getIntent().getBundleExtra("cityBundle");
        if (bundle!=null){
            CityItem cityItem = bundle.getParcelable("cityItem");
            zongId = cityItem.getTimeId();
            if (cityItem!=null){
                timeZoneView.setCity(cityItem);
            }
        }

        switch (zongId){
            case "Asia/Chongqing": Glide.with(this).load(R.drawable.bg_chongqing).into(imageView);break;
            case "America/Chicago":Glide.with(this).load(R.drawable.bg_chicago).into(imageView);break;
            case "Europe/Monaco":Glide.with(this).load(R.drawable.bg_monaco1).into(imageView);break;
            case "Europe/London":Glide.with(this).load(R.drawable.bg_lundon).into(imageView);break;
            case "Europe/Copenhagen":Glide.with(this).load(R.drawable.bg_copenhagen1).into(imageView);break;
            case "America/New_York":Glide.with(this).load(R.drawable.bg_newyork).into(imageView);break;
            case "Europe/Moscow":Glide.with(this).load(R.drawable.bg_moscow).into(imageView);break;
        }
    }

}
