package com.dh.testproject.databinding;

import androidx.arch.core.util.Function;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class MyViewModel extends ViewModel {
    private MutableLiveData<String> tempName = new MutableLiveData<>("张三");
    private MutableLiveData<String> tempLastName = new MutableLiveData<>("王五");
    private MutableLiveData<Integer> tempLikes = new MutableLiveData<>(0);

    public LiveData<String> name = tempName;
    public LiveData<String> lastName = tempLastName;
    public LiveData<Integer> likes = tempLikes;

    public LiveData<Popularity> popularity = Transformations.map(tempLikes, new Function<Integer, Popularity>() {
        @Override
        public Popularity apply(Integer input) {
            if (input > 9) {
                return Popularity.STAR;
            } else if (input > 4) {
                return Popularity.POPULAR;
            } else {
                return Popularity.NORMAL;
            }
        }
    });

    public void onLike() {
        tempLikes.setValue((tempLikes.getValue() != null ? tempLikes.getValue() : 0) + 1);
    }

    public void onSubLike() {
        if (tempLikes.getValue() == null) return;
        if (tempLikes.getValue() >= 1) {
            tempLikes.setValue(tempLikes.getValue() - 1);
        }
    }
}
