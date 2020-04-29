package com.dh.testproject.databinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableInt;

import android.os.Bundle;
import android.view.View;

import com.dh.testproject.R;

public class ObservableActivity extends AppCompatActivity {

    private UserDataBinding userDataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_observable);

        ActivityObservableBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_observable);
        userDataBinding = new UserDataBinding("张三", "李四", new ObservableInt(0));
        binding.setUser(userDataBinding);
    }

    public void btAdd(View view) {
        userDataBinding.getLikes().set(userDataBinding.getLikes().get() + 1);
    }

    public void btSub(View view) {
        if (userDataBinding.getLikes().get() < 1) return;
        userDataBinding.getLikes().set(userDataBinding.getLikes().get() - 1);
    }
}
