package com.dh.testproject.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.dh.testproject.R;
import com.dh.testproject.algorithm_utils.BreadthFirstSearch;

public class TestAlgorithmActivity extends AppCompatActivity implements View.OnClickListener,
        AudioManager.OnAudioFocusChangeListener {
    private static final String TAG = "TestAlgorithmActivity";

    private Button btBreadth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_algorithm);

        initViews();
        initListener();
    }

    private void initListener() {
        btBreadth.setOnClickListener(this);
    }

    private void initViews() {
        btBreadth = findViewById(R.id.btBreadth);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btBreadth:
                String firstSearch = BreadthFirstSearch.breadthFirstSearch("me");
                Log.e(TAG, "是否找到商人, 姓名: " + firstSearch);
                break;
        }
    }


    @Override
    public void onAudioFocusChange(int focusChange) {
        
    }
}
