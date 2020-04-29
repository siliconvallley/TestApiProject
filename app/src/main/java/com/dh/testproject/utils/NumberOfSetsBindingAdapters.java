package com.dh.testproject.utils;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

public class NumberOfSetsBindingAdapters {

    @BindingAdapter("app:numberOfSets")
    public static void setNumberOfSets(EditText editText, String value) {
        editText.setText(value);
    }

    @InverseBindingAdapter(attribute = "app:numberOfSets")
    public static String getNumberOfSets(EditText editText) {
        return editText.getText().toString();
    }

    @BindingAdapter("app:numberOfSetsAttrChanged")
    public static void setListener(EditText editText, final InverseBindingListener listener) {
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                TextView textView = (TextView) v;
                if (hasFocus) {
                    // Delete contents of the EditText if the focus entered.
                    textView.setText("");
                } else {
                    // If the focus left, update the listener
                    listener.onChange();
                }
            }
        });
    }
}
