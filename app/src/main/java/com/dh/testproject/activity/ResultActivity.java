package com.dh.testproject.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.dh.testproject.R;

public class ResultActivity extends AppCompatActivity {
    private static final String TAG = "ResultActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);


        initParams();

    }

    private void initParams() {
        Intent intent = getIntent();
        if (intent == null) return;
        String testValue = intent.getStringExtra("testKey");
        Log.e(TAG, "通过xml文件传递的值： " + testValue);
    }
}
