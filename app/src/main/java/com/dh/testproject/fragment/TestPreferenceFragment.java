package com.dh.testproject.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.preference.CheckBoxPreference;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceDataStore;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;

import com.dh.testproject.R;
import com.dh.testproject.activity.ResultActivity;

public class TestPreferenceFragment extends PreferenceFragmentCompat {
    private static final String TAG = "TestPreferenceFragment";
    private CheckBoxPreference checkBoxPreference;
    private EditTextPreference editTextPreference;
    private Preference testIntentPreference;
    private Preference testWebPreference;
    private SwitchPreferenceCompat switchPreferenceCompat;
    private Preference feedBackPreference;
    private Preference foldPreference;
    private PreferenceCategory foldContentCategory;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.fragment_test_preference, rootKey);

        switchPreferenceCompat = findPreference("notifications");
        checkBoxPreference = findPreference("checkBox");
        editTextPreference = findPreference("editText");
        testIntentPreference = findPreference("testIntent");
        testWebPreference = findPreference("testWebpage");
        feedBackPreference = findPreference("feedback");

        foldPreference = findPreference("fold");
        foldContentCategory = findPreference("foldContent");

        foldPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                foldPreference.setVisible(false);
                foldContentCategory.setVisible(true);
                return true;
            }
        });

        if (getActivity() != null) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
            boolean isNotification = sp.getBoolean("notifications", false);
            //Log.e(TAG, "开关状态： " + isNotification);
        }

        switchPreferenceCompat.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Log.e(TAG, "开关状态： " + (boolean) newValue);
                return true;
            }
        });

        checkBoxPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                Log.e(TAG, "checkbox的值: " + (boolean) newValue);
                editTextPreference.setVisible((boolean) newValue);
                return true;
            }
        });


        /*EditTextPreference.SimpleSummaryProvider summaryProvider = EditTextPreference.SimpleSummaryProvider.getInstance();
        editTextPreference.setSummaryProvider(summaryProvider);*/

        editTextPreference.setSummaryProvider(new Preference.SummaryProvider<EditTextPreference>() {

            @Override
            public CharSequence provideSummary(EditTextPreference preference) {
                String text = preference.getText();
                if (TextUtils.isEmpty(text)) {
                    return "Not value";
                }
                return "Length of saved value: " + text.length();
            }
        });

        editTextPreference.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
            @Override
            public void onBindEditText(@NonNull EditText editText) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
        });

        //testIntentPreference.setIntent(new Intent(getActivity(), ResultActivity.class));

        /*Intent webIntent = new Intent(Intent.ACTION_VIEW);
        webIntent.setData(Uri.parse("http://www.baidu.com"));
        testWebPreference.setIntent(webIntent);*/

        testWebPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Log.e(TAG, "我还是监听到了点击事件");
                return false;
            }
        });



        /*CustomDataStore dataStore = new CustomDataStore();
        // 为单个Preference定义数据存储方式
        switchPreferenceCompat.setPreferenceDataStore(dataStore);
        // 为全局所有的Preference定义数据存储方式
        getPreferenceManager().setPreferenceDataStore(dataStore);*/

        /*feedBackPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                feedBackPreference.setVisible(false);
                editTextPreference.setVisible(true);
                return true;
            }
        });*/
    }

    private SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals("notifications")) {
                Log.e(TAG, "打印出通知开关的值： " + sharedPreferences.getBoolean(key, false));
            } else if (key.equals("checkBox")) {
                Log.e(TAG, "打印出选择框的值： " + sharedPreferences.getBoolean(key, false));
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        if (onSharedPreferenceChangeListener != null) {
            getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (onSharedPreferenceChangeListener != null) {
            getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
        }
    }

    /**
     * 自定义Preference数据的存储方式
     */
    class CustomDataStore extends PreferenceDataStore {
        @Override
        public void putBoolean(String key, boolean value) {
            super.putBoolean(key, value);
        }

        @Override
        public boolean getBoolean(String key, boolean defValue) {
            return super.getBoolean(key, defValue);
        }
    }
}
