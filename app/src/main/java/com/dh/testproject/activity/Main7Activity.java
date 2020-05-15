package com.dh.testproject.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import android.os.Bundle;

import com.dh.testproject.R;
import com.dh.testproject.fragment.TestPreferenceFragment;
import com.dh.testproject.fragment.TestPreferenceFragment2;

public class Main7Activity extends AppCompatActivity implements PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main7);


        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.clContent, new TestPreferenceFragment())
                //.replace(R.id.clContent, new TestPreferenceFragment2())
                .commit();

    }

    @Override
    public boolean onPreferenceStartFragment(PreferenceFragmentCompat caller, Preference pref) {


        final Bundle args = pref.getExtras();
        final Fragment fragment = getSupportFragmentManager().getFragmentFactory().instantiate(
                getClassLoader(),
                pref.getFragment());
        fragment.setArguments(args);
        fragment.setTargetFragment(caller, 0);
        // Replace the existing Fragment with the new Fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.clContent, fragment)
                .addToBackStack(null)
                .commit();
        return true;

    }
}
