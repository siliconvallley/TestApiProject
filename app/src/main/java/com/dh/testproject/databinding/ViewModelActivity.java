package com.dh.testproject.databinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import com.dh.testproject.R;

public class ViewModelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_model);


        ViewModelProvider viewModelProvider = new ViewModelProvider(this.getViewModelStore(), this.getDefaultViewModelProviderFactory());
        MyViewModel viewModel = viewModelProvider.get(MyViewModel.class);
        ActivityViewModelBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_view_model);

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
    }
}
