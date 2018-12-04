package com.yzl.clock.widget;

/**
 * Created by Kingc on 2018/6/30.
 */

public interface WatchOperator {
    void start();
    void pause();
    void stop();
    String getTime();
}
