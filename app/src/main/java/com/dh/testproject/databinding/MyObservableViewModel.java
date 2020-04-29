package com.dh.testproject.databinding;

import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.dh.testproject.BR;

public class MyObservableViewModel extends ObservableViewModel {
    private ObservableField<String> name = new ObservableField<>("张三");
    private ObservableField<String> lastName = new ObservableField<>("王五");
    private ObservableInt likes = new ObservableInt(0);


    public void onLike() {
        likes.set(likes.get() + 1);
        notifyPropertyChanged(BR.popularity);
    }

    @Bindable
    public Popularity getPopularity() {
        int i = likes.get();
        if (i > 9) {
            return Popularity.STAR;
        } else if (i > 4) {
            return Popularity.POPULAR;
        } else {
            return Popularity.NORMAL;
        }
    }
}
