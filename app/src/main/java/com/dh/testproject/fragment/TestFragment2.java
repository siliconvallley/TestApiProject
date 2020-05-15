package com.dh.testproject.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;

import com.dh.testproject.R;

public class TestFragment2 extends Fragment {
    private static final String TAG = "TestFragment2";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test2, container, false);
        view.findViewById(R.id.btValue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("valueKey", "我是来自于Fragment2的值，你赶紧接收");
                getParentFragmentManager().setFragmentResult("resultKey", bundle);
            }
        });
        init();
        return view;
    }

    private void init() {
        TestFragment3 testFragment3 = new TestFragment3();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.flContent, testFragment3, "testFragment3");
        transaction.commit();

        getChildFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                Log.e(TAG, "requestKey: " + requestKey);
                Log.e(TAG, "value: " + result.getString("valueKey", "defaultValue"));
            }
        });
    }
}
