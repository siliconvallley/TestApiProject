package com.dh.testproject.utils;

import android.content.Context;

import androidx.databinding.InverseMethod;

import com.dh.testproject.R;

public class NumberOfSetsConverters {

    @InverseMethod("stringToSetArray")
    public static String setArrayToString(Context context, int[] values) {
        return context.getString(R.string.sets_format, values[0] + 1, values[1]);
    }

    public static int[] stringToSetArray(Context context, String value) {
        if (value == null || value.length() == 0) {
            return new int[]{0, 0};
        }
        try {
            return new int[]{0, Integer.parseInt(value)};
        } catch (NumberFormatException e) {
            return new int[]{0, 0};
        }
    }
}
