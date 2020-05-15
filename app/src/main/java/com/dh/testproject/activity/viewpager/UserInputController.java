package com.dh.testproject.activity.viewpager;

import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.viewpager2.widget.ViewPager2;

public class UserInputController {
    private ViewPager2 vp;
    private CheckBox checkBox;

    public UserInputController(ViewPager2 vp, CheckBox checkBox) {
        this.vp = vp;
        this.checkBox = checkBox;
    }

    public void setUp() {
        checkBox.setChecked(!vp.isUserInputEnabled());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                vp.setUserInputEnabled(!isChecked);
            }
        });
    }
}
