package com.dh.testproject.databinding;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class IntervalTimerViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(TimerObservableViewModel.class)) {
            return (T) new TimerObservableViewModel(new DefaultTimer());
        }

        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
