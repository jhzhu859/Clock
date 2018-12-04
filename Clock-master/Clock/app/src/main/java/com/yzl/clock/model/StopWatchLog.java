package com.yzl.clock.model;

/**
 * Created by Kingc on 2018/7/19.
 */

public class StopWatchLog {

    private int id;
    private String name;
    private String duration;
    private String time;

    public StopWatchLog(int id, String name, String duration, String time) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
