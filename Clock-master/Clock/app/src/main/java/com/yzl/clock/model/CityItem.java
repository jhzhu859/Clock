package com.yzl.clock.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kingc on 2018/7/12.
 */

public class CityItem implements Parcelable{

    private String timeName;
    private String timeId;

    public CityItem(String timeName, String timeId) {
        this.timeName = timeName;
        this.timeId = timeId;
    }

    protected CityItem(Parcel in) {
        timeName = in.readString();
        timeId = in.readString();
    }

    public static final Creator<CityItem> CREATOR = new Creator<CityItem>() {
        @Override
        public CityItem createFromParcel(Parcel in) {
            return new CityItem(in);
        }

        @Override
        public CityItem[] newArray(int size) {
            return new CityItem[size];
        }
    };

    public String getTimeName() {
        return timeName;
    }

    public void setTimeName(String timeName) {
        this.timeName = timeName;
    }

    public String getTimeId() {
        return timeId;
    }

    public void setTimeId(String timeId) {
        this.timeId = timeId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(timeName);
        dest.writeString(timeId);
    }
}
