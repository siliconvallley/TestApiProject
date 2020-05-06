package com.dh.testproject.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.dh.testproject.R;
import com.dh.testproject.databinding.ActivityMain4Binding;
import com.dh.testproject.databinding.ObservableActivity;
import com.dh.testproject.databinding.TwoWayActivity;
import com.dh.testproject.databinding.ViewModelActivity;

public class Main4Activity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        ActivityMain4Binding binding = DataBindingUtil.setContentView(this, R.layout.activity_main4);

        binding.btObservable.setOnClickListener(this);
        binding.btViewModel.setOnClickListener(this);
        binding.btWay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btObservable:
                startActivity(new Intent(this, ObservableActivity.class));
                break;
            case R.id.btViewModel:
                startActivity(new Intent(this, ViewModelActivity.class));
                break;
            case R.id.btWay:
                startActivity(new Intent(this, TwoWayActivity.class));
                break;
        }
    }
}
