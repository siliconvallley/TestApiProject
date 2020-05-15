package com.dh.testproject.activity.viewpager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.dh.testproject.R;

public class CardViewActivity extends BaseCardViewActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_card_view);

        viewPager2.setAdapter(new CardViewAdapter());
    }
}
