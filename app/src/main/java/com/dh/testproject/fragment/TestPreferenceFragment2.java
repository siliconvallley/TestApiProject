package com.dh.testproject.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreferenceCompat;

import com.dh.testproject.R;

public class TestPreferenceFragment2 extends PreferenceFragmentCompat {
    private AppCompatActivity activity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (AppCompatActivity) context;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        //Context context = getPreferenceManager().getContext();

        PreferenceScreen preferenceScreen = getPreferenceManager().createPreferenceScreen(activity);

        Preference testPreference = new Preference(activity);
        testPreference.setKey("testPreference");
        testPreference.setTitle("测试Preference");
        testPreference.setSummary("测试Preference的副标题");

        SwitchPreferenceCompat switchPreferenceCompat = new SwitchPreferenceCompat(activity);
        switchPreferenceCompat.setKey("switchPreferenceCompat");
        switchPreferenceCompat.setTitle("带开关的Preference");
        switchPreferenceCompat.setSummary("正在测试带开关的Preference");


        PreferenceCategory category1 = new PreferenceCategory(activity);
        category1.setIcon(R.mipmap.head_icon);
        category1.setTitle("Category One");
        // 如下的顺序不能反，在xml中，控件是一层一层添加的
        preferenceScreen.addPreference(category1);
        category1.addPreference(testPreference);

        preferenceScreen.addPreference(switchPreferenceCompat);


        setPreferenceScreen(preferenceScreen);

    }
}
