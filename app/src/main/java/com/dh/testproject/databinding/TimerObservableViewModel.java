package com.dh.testproject.databinding;

import androidx.databinding.Bindable;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.Transformations;

import com.dh.testproject.BR;

import java.util.TimerTask;

import static com.dh.testproject.utils.TimerConverter.cleanSecondsString;

public class TimerObservableViewModel extends ObservableViewModel {
    private static final int INITIAL_SECONDS_PER_WORK_SET = 5;
    private static final int INITIAL_SECONDS_PER_REST_SET = 2;
    private static final int INITIAL_NUMBER_OF_SETS = 5;


    public ObservableInt timePerWorkSet = new ObservableInt(INITIAL_SECONDS_PER_WORK_SET * 10); // tenths
    public ObservableInt timePerRestSet = new ObservableInt(INITIAL_SECONDS_PER_REST_SET * 10); // tenths
    public ObservableInt workTimeLeft = new ObservableInt(timePerWorkSet.get()); // tenths
    public ObservableInt restTimeLeft = new ObservableInt(timePerRestSet.get()); // tenths

    private boolean timerRunning;
    private int numberOfSetsTotal = INITIAL_NUMBER_OF_SETS;
    private int numberOfSetsElapsed = 0;
    private boolean inWorkingStage;

    private TimerStates state = TimerStates.STOPPED;
    private StartedStages stage = StartedStages.WORKING;
    private int[] numberOfSets = {};

    private Timer timer;

    public TimerObservableViewModel(Timer timer) {
        this.timer = timer;
    }

    @Bindable
    public boolean isTimerRunning() {
        return state == TimerStates.STARTED;
    }

    public void setTimerRunning(boolean timerRunning) {
        // this.timerRunning = timerRunning;

        if (timerRunning) {
            startButtonClicked();
        } else {
            pauseButtonClicked();
        }
    }

    public int getNumberOfSetsTotal() {
        return numberOfSetsTotal;
    }

    public void setNumberOfSetsTotal(int numberOfSetsTotal) {
        this.numberOfSetsTotal = numberOfSetsTotal;
    }

    public int getNumberOfSetsElapsed() {
        return numberOfSetsElapsed;
    }

    public void setNumberOfSetsElapsed(int numberOfSetsElapsed) {
        this.numberOfSetsElapsed = numberOfSetsElapsed;
    }

    @Bindable
    public boolean isInWorkingStage() {
        return stage == StartedStages.WORKING;
    }

    public void setInWorkingStage(boolean inWorkingStage) {
        this.inWorkingStage = inWorkingStage;
    }

    @Bindable
    public int[] getNumberOfSets() {
        return new int[]{numberOfSetsElapsed, numberOfSetsTotal};
    }

    public void setNumberOfSets(int[] numberOfSets) {
        //this.numberOfSets = numberOfSets;

        // Only the second Int is being set
        int newTotal = numberOfSets[1];
        if (newTotal == getNumberOfSets()[1]) return; // Break loop if there's no change
        // Only update if it doesn't affect the current exercise
        if (newTotal != 0 && newTotal > numberOfSetsElapsed) {
            this.numberOfSets = numberOfSets;
            numberOfSetsTotal = newTotal;
        }
        // Even if the input is empty, force a refresh of the view
        notifyPropertyChanged(BR.numberOfSets);
    }

    /**
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */

    public void stopButtonClicked() {
        resetTimers();
        numberOfSetsElapsed = 0;
        state = TimerStates.STOPPED;
        stage = StartedStages.WORKING; // Reset for the next set
        timer.reset();

        notifyPropertyChanged(BR.timerRunning);
        notifyPropertyChanged(BR.inWorkingStage);
        notifyPropertyChanged(BR.numberOfSets);
    }

    private void pauseButtonClicked() {
        if (state == TimerStates.STARTED) {
            startedToPaused();
        }
        notifyPropertyChanged(BR.timerRunning);
    }

    private void startButtonClicked() {
        switch (state) {
            case STARTED:
                // Do nothing
                break;
            case PAUSED:
                pausedToStarted();
                break;
            case STOPPED:
                stoppedToStarted();
                break;
        }

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (state == TimerStates.STARTED) {
                    updateCountdowns();
                }
            }
        };

        // Schedule timer every 100ms to update the counters.
        timer.start(task);

    }

    /* STOPPED -> STARTED */
    private void stoppedToStarted() {
        // Set the start time
        timer.resetStartTime();
        state = TimerStates.STARTED;
        stage = StartedStages.WORKING;

        notifyPropertyChanged(BR.inWorkingStage);
        notifyPropertyChanged(BR.timerRunning);
    }

    /* PAUSED -> STARTED */
    private void pausedToStarted() {
        timer.updatePausedTime();
        state = TimerStates.STARTED;

        notifyPropertyChanged(BR.timerRunning);
    }

    /* STARTED -> PAUSED */
    private void startedToPaused() {
        state = TimerStates.PAUSED;
        timer.resetPauseTime();
    }

    private void updateCountdowns() {
        if (state == TimerStates.STOPPED) {
            resetTimers();
            return;
        }

        long elapsed;

        if (state == TimerStates.PAUSED) {
            elapsed = timer.getPausedTime();
        } else {
            elapsed = timer.getElapsedTime();
        }

        if (stage == StartedStages.RESTING) {
            updateRestCountdowns(elapsed);
        } else {
            // work
            updateWorkCountdowns(elapsed);
        }
    }

    private void updateWorkCountdowns(long elapsed) {
        stage = StartedStages.WORKING;
        int newTimeLeft = timePerWorkSet.get() - (int) (elapsed / 100);
        if (newTimeLeft <= 0) {
            workoutFinished();
        }
        workTimeLeft.set(Math.max(newTimeLeft, 0));
    }

    private void updateRestCountdowns(long elapsed) {
        // Calculate the countdown time with the start time
        int newRestTimeLeft = timePerRestSet.get() - (int) (elapsed / 100);
        restTimeLeft.set(Math.max(newRestTimeLeft, 0));

        if (newRestTimeLeft <= 0) { // Rest time is up
            numberOfSetsElapsed += 1;
            resetTimers();
            if (numberOfSetsElapsed >= numberOfSetsTotal) { // End
                timerFinished();
            } else { // End of set
                setFinished();
            }
        }
    }

    /* WORKING -> RESTING */
    private void workoutFinished() {
        timer.resetStartTime();
        stage = StartedStages.RESTING;
        notifyPropertyChanged(BR.inWorkingStage);
    }

    /* RESTING -> WORKING */
    private void setFinished() {
        timer.resetStartTime();
        stage = StartedStages.WORKING;

        notifyPropertyChanged(BR.inWorkingStage);
        notifyPropertyChanged(BR.numberOfSets);
    }

    /* RESTING -> STOPPED */
    private void timerFinished() {
        state = TimerStates.STOPPED;
        stage = StartedStages.WORKING; // Reset for the next set
        timer.reset();
        notifyPropertyChanged(BR.timerRunning);
        numberOfSetsElapsed = 0;

        notifyPropertyChanged(BR.inWorkingStage);
        notifyPropertyChanged(BR.numberOfSets);
    }

    private void resetTimers() {
        // Reset counters
        workTimeLeft.set(timePerWorkSet.get());

        // Set the start time
        restTimeLeft.set(timePerRestSet.get());
    }


    public void timePerRestSetChanged(CharSequence value) {
        try {
            timePerRestSet.set(cleanSecondsString(value.toString()));
        } catch (NumberFormatException e) {
            return;
        }
        if (!isRestTimeAndRunning()) {
            restTimeLeft.set(timePerRestSet.get());
        }
    }

    public void timePerWorkSetChanged(CharSequence value) {
        try {
            timePerWorkSet.set(cleanSecondsString(value.toString()));
        } catch (NumberFormatException e) {
            return;
        }
        if (!timerRunning) {
            workTimeLeft.set(timePerWorkSet.get());
        }
    }

    private boolean isRestTimeAndRunning() {
        return (state == TimerStates.PAUSED || state == TimerStates.STARTED)
                && workTimeLeft.get() == 0;
    }


    public void restTimeIncrease() {
        timePerSetIncrease(timePerRestSet, 1, 0);
    }

    public void workTimeIncrease() {
        timePerSetIncrease(timePerWorkSet, 1, 0);
    }

    public void setsIncrease() {
        numberOfSetsTotal += 1;
        notifyPropertyChanged(BR.numberOfSets);
    }

    public void restTimeDecrease() {
        timePerSetIncrease(timePerRestSet, -1, 0);
    }

    public void workTimeDecrease() {
        timePerSetIncrease(timePerWorkSet, -1, 10);
    }

    public void setsDecrease() {
        if (numberOfSetsTotal > numberOfSetsElapsed + 1) {
            numberOfSetsTotal -= 1;
            notifyPropertyChanged(BR.numberOfSets);
        }
    }

    private void timePerSetIncrease(ObservableInt timePerSet, int sign, int min) {
        if (timePerSet.get() < 10 && sign < 0) {
            return;
        }
        roundTimeIncrease(timePerSet, sign, min);
        if (state == TimerStates.STOPPED) {
            // If stopped, update the timers right away
            workTimeLeft.set(timePerWorkSet.get());
            restTimeLeft.set(timePerRestSet.get());
        } else {
            // If running or paused, the timers need to be calculated
            updateCountdowns();
        }
    }

    private void roundTimeIncrease(ObservableInt timePerSet, int sign, int min) {
        int currentValue = timePerSet.get();
        int newValue = 0;
        if (currentValue < 100) {
            // <10 Seconds - increase 1
            newValue = timePerSet.get() + sign * 10;
        } else if (currentValue < 600) {
            // >10 seconds, 5-second increase
            newValue = (int) (Math.rint(currentValue / 50.0) * 50 + (50 * sign));
        } else {
            // >60 seconds, 10-second increase
            newValue = (int) (Math.rint(currentValue / 100.0) * 100 + (100 * sign));
        }
        timePerSet.set(Math.max(newValue, min));
    }

}
