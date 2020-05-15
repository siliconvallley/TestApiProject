package com.dh.testproject.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import com.dh.testproject.R;

public class TestFragment1 extends Fragment implements View.OnClickListener {
    private static final String TAG = "TestFragment1";
    private Button btCallBack;
    private OnCallBackListener listener;
    private Button btToStack;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (OnCallBackListener) context;
        } catch (ClassCastException e) {
            Log.e(TAG, "You must implement OnCallBackListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test1, container, false);
        btCallBack = view.findViewById(R.id.btCallBack);
        btToStack = view.findViewById(R.id.btToStack);


        btCallBack.setOnClickListener(this);
        btToStack.setOnClickListener(this);
        init();
        return view;
    }

    private void init() {
        getParentFragmentManager().setFragmentResultListener("resultKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                Log.e(TAG, "requestKey: " + requestKey);
                Log.e(TAG, "value: " + result.getString("valueKey", "defaultValue"));
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btCallBack:
                if (listener != null) {
                    listener.onCallBack("我已经从Fragment传递值给Activity，请接收");
                }
                break;
            case R.id.btToStack:

                break;
        }
    }

    public interface OnCallBackListener {
        void onCallBack(String testStr);
    }
}
