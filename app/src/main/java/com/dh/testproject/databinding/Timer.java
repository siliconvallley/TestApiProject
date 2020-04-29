package com.dh.testproject.databinding;

import java.util.TimerTask;

public interface Timer {
    void reset();

    void start(TimerTask task);

    long getElapsedTime();

    void updatePausedTime();

    long getPausedTime();

    void resetStartTime();

    void resetPauseTime();
}
