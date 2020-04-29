package com.dh.testproject.databinding;

import java.util.TimerTask;

public class DefaultTimer implements Timer {
    private static final long TIMER_PERIOD_MS = 100L;

    private long startTime = System.currentTimeMillis();
    private long pauseTime = 0L;
    private java.util.Timer timer;

    @Override
    public void reset() {
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public void start(TimerTask task) {
        timer = new java.util.Timer();
        timer.schedule(task, 0, TIMER_PERIOD_MS);
    }

    @Override
    public long getElapsedTime() {
        return System.currentTimeMillis() - startTime;
    }

    @Override
    public void updatePausedTime() {
        startTime += System.currentTimeMillis() - pauseTime;
    }

    @Override
    public long getPausedTime() {
        return pauseTime - startTime;
    }

    @Override
    public void resetStartTime() {
        startTime = System.currentTimeMillis();
    }

    @Override
    public void resetPauseTime() {
        pauseTime = System.currentTimeMillis();
    }
}
