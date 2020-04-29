package com.dh.testproject.databinding;

import androidx.databinding.Observable;
import androidx.databinding.PropertyChangeRegistry;
import androidx.lifecycle.ViewModel;

public class ObservableViewModel extends ViewModel implements Observable {

    private PropertyChangeRegistry calls = new PropertyChangeRegistry();

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        calls.add(callback);
    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        calls.remove(callback);
    }

    public void notifyChange() {
        calls.notifyCallbacks(this, 0, null);
    }

    public void notifyPropertyChanged(int arg) {
        calls.notifyCallbacks(this, arg, null);
    }
}
