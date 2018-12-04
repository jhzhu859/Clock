package com.yzl.clock.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Kingc on 2018/7/19.
 */

public class ClockOpenHelper extends SQLiteOpenHelper {

    private String TAG = "ClockOpenHelper";


    public static final String DATABASE_NAME = "clock.db";//数据库名字
    public static final int DATABASE_VERSION = 1;//数据库版本号
    public static final String CREATE_TABLE_STOP_WATCH
            = "create table IF NOT EXISTS stopWatch ("
            + "id integer primary key autoincrement,"
            + "name text, "
            + "duration text, "
            + "time text)";//数据库里的表

    public static final String CREATE_TABLE_ALARM_CLOCK
            = "create table IF NOT EXISTS alarmClock ("
            + "id integer primary key autoincrement,"
            + "days text, "
            + "open integer, "
            + "time text)";//数据库里的表


    public ClockOpenHelper(Context context) {
        this(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private ClockOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);//调用到SQLiteOpenHelper中
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_STOP_WATCH);
        db.execSQL(CREATE_TABLE_ALARM_CLOCK);
        Log.e(TAG,"create table success");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static final class StopWatchTable{
        public static final String TABLE = "stopWatch";
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String DURATION = "duration";
        public static final String TIME = "time";
    }

    public static final class AlarmClockTable{
        public static final String TABLE = "alarmClock";
        public static final String ID = "id";
        public static final String OPEN = "open";
        public static final String DAYS = "days";
        public static final String TIME = "time";
    }
}