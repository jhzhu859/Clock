package com.yzl.clock.fragment;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yzl.clock.R;
import com.yzl.clock.clockview.MathUtil;
import com.yzl.clock.clockview.StopWatchView;
import com.yzl.clock.clockview.ViewUtil;
import com.yzl.clock.database.ClockOpenHelper;
import com.yzl.clock.drawable.StopWatchIndicatorDrawable;
import com.yzl.clock.manager.AppBarStateManager;
import com.yzl.clock.model.StopWatchLog;
import com.yzl.clock.widget.CircleButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/6/19.
 */

public class FragmentStopWatch extends Fragment {


    private View mView;
    private StopWatchView mStopWatchView;
    private CircleButton mButton;
    private StopWatchIndicatorDrawable mDrawable;
    private ClockOpenHelper mClockOpenHelper;
    private StopWatchView stopWatchView;
    private LinearLayout mLinearLayout;
    private AppBarLayout appBarLayout;
    private Toolbar mToolBar;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");
    private boolean isCreated = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mClockOpenHelper = new ClockOpenHelper(getContext());
        isCreated = true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_stop_watch, null);
        mLinearLayout = mView.findViewById(R.id.ll_content);
        stopWatchView = mView.findViewById(R.id.stopWatchView);
        appBarLayout = mView.findViewById(R.id.appBarLayout);
        mToolBar = mView.findViewById(R.id.toolBar);
        int toolBarHeight = MathUtil.getToolbarHeight(getContext());
        mToolBar.setMinimumHeight(toolBarHeight);
        initScrollListener();
        return mView;
    }




    private void initScrollListener() {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float range = appBarLayout.getTotalScrollRange();
                float offset = Math.abs(verticalOffset);
                float clockRatio = (range - offset) / range;
                clockRatio = clockRatio == 0f ? 1 : clockRatio;
                stopWatchView.setScaleX(clockRatio + (1 - clockRatio) * 0.7f);
                stopWatchView.setScaleY(clockRatio + (1 - clockRatio) * 0.7f);
            }
        });
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mStopWatchView = mView.findViewById(R.id.stopWatchView);
        mButton = mView.findViewById(R.id.operator);
        mDrawable = mStopWatchView.getIndicatorDrawable();
        if (mDrawable != null) {
            boolean isRunning = mDrawable.getRunningState();
            String text = isRunning ? "停止" : "开始";
            mButton.setText(text);
            mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean state = mDrawable.getRunningState();
                    mDrawable.setRunningState(!state);
                    String text = mDrawable.getRunningState() ? "停止" : "开始";
                    mButton.setText(text);
                    if (text.equals("开始")) {
                        String duration = mDrawable.getDuration();
                        Log.e("所需时间", duration);
                        showDialog(duration);
                    }
                }
            });
        }
        queryLog();
    }

    private void showDialog(final String duration) {
        View view = View.inflate(getContext(), R.layout.dialog_stop_watch_insert, null);
        final EditText editText = (EditText) view.findViewById(R.id.et_name);
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("输入标记")
                .setMessage("耗时：" + duration + "秒")
                .setView(view)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = editText.getText().toString();
                        insetLog(name, duration);
                        dialog.dismiss();
                    }
                }).create();

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mDrawable.reset();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mDrawable.reset();
            }
        });
        dialog.show();
    }


    private void insetLog(String name, String duration) {
        SQLiteDatabase db = mClockOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ClockOpenHelper.StopWatchTable.NAME, name);
        values.put(ClockOpenHelper.StopWatchTable.DURATION, duration);
        values.put(ClockOpenHelper.StopWatchTable.TIME, simpleDateFormat.format(new Date()));
        Long index = db.insert(ClockOpenHelper.StopWatchTable.TABLE, null, values);
        if (index > 0) {
            queryLog();
        }
    }


    private void queryLog() {
        List<StopWatchLog> logList = new ArrayList<>();
        SQLiteDatabase db = mClockOpenHelper.getReadableDatabase();
        Cursor cursor = db.query("stopWatch", null, null, null, null, null, null);// 注意空格！
        int nameIndex = cursor.getColumnIndex(ClockOpenHelper.StopWatchTable.NAME);
        int durationIndex = cursor.getColumnIndex(ClockOpenHelper.StopWatchTable.DURATION);
        int idIndex = cursor.getColumnIndex(ClockOpenHelper.StopWatchTable.ID);
        int timeIndex = cursor.getColumnIndex(ClockOpenHelper.StopWatchTable.TIME);
        while (cursor.moveToNext()) {
            String name = cursor.getString(nameIndex);
            String time = cursor.getString(timeIndex);
            String duration = cursor.getString(durationIndex);
            int id = cursor.getInt(idIndex);
            logList.add(new StopWatchLog(id, name, duration, time));
        }
        cursor.close();
        mLinearLayout.removeAllViews();
        for (int i = 0; i < logList.size(); i++) {
            mLinearLayout.addView(createView(logList.get(i)));
            mLinearLayout.addView(ViewUtil.createDivider(getContext()));
        }

    }


    private View createView(final StopWatchLog stopWatchLog) {

        View convertView = View.inflate(getContext(), R.layout.item_stop_watch_log, null);
        TextView name = convertView.findViewById(R.id.tv_name);
        TextView duration = convertView.findViewById(R.id.tv_duration);
        TextView time = convertView.findViewById(R.id.tv_time);
        ImageView cancel = convertView.findViewById(R.id.iv_cancel);

        name.setText(stopWatchLog.getName());
        duration.setText(stopWatchLog.getDuration());
        time.setText(stopWatchLog.getTime());

        cancel.setVisibility(View.INVISIBLE);

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDeleteDialog(stopWatchLog);
                return true;
            }
        });
        return convertView;
    }

    private void deleteLog(StopWatchLog stopWatchLog) {
        SQLiteDatabase db = mClockOpenHelper.getWritableDatabase();
        //要删除的数据
        int index = db.delete(ClockOpenHelper.StopWatchTable.TABLE, "id = ?", new String[]{stopWatchLog.getId() + ""});
        db.close();
        if (index > 0) {
            queryLog();
        }
    }


    private void showDeleteDialog(final StopWatchLog stopWatchLog) {
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("删除该记录？")//设置对话框的标题
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteLog(stopWatchLog);
                        dialog.dismiss();
                    }
                }).create();

        dialog.show();
    }

}
