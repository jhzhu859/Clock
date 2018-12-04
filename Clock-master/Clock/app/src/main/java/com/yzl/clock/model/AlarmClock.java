package com.yzl.clock.model;

/**
 * Created by Kingc on 2018/7/23.
 */

public class AlarmClock {

    private int id;
    private String time;
    private String days;
    private boolean open;

    public AlarmClock(int id, String time, String days, boolean open) {
        this.id = id;
        this.time = time;
        this.days = days;
        this.open = open;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
