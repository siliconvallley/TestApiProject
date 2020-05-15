package com.dh.testproject.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.dh.testproject.R;
import com.dh.testproject.fragment.TestFragment1;
import com.dh.testproject.fragment.TestFragment2;

public class Main9Activity extends AppCompatActivity implements TestFragment1.OnCallBackListener,
        View.OnClickListener {
    private static final String TAG = "Main9Activity";
    private TestFragment1 testFragment1;
    private TestFragment2 testFragment2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main9);
        findViewById(R.id.btToStack).setOnClickListener(this);

        init();
    }

    private void init() {
        testFragment1 = new TestFragment1();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //transaction.replace(R.id.rlContent,testFragment1);
        if (!testFragment1.isAdded()) {
            transaction.add(R.id.rlContent, testFragment1, "testFragment1");
        } else {
            if (testFragment1.isHidden()) {
                transaction.show(testFragment1);
            }
        }
        transaction.addToBackStack("testFragment1");
        transaction.commit();
    }

    @Override
    public void onCallBack(String testStr) {
        Log.e(TAG, "来自fragment的值: " + testStr);
    }

    @Override
    public void onClick(View v) {
        testFragment2 = new TestFragment2();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!testFragment1.isHidden()) {
            transaction.hide(testFragment1);
        }
        if (!testFragment2.isAdded()) {
            transaction.add(R.id.rlContent, testFragment2);
        }
        transaction.addToBackStack("testFragment2");
        transaction.commit();
    }
}
