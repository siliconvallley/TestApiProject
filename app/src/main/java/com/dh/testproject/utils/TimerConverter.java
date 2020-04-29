package com.dh.testproject.utils;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class TimerConverter {
    public static int cleanSecondsString(String value) {
        String filteredValue = value.replace(Pattern.compile("[^\\d:.]").pattern(), "");
        if (filteredValue.length() == 0) return 0;
        String[] split = filteredValue.split(":");
        List<Integer> list = new ArrayList<>();
        for (String s : split) {
            list.add((int) Math.rint(Double.parseDouble(s)));
        }

        int result;
        if (list.size() > 2) {
            return 0;
        } else if (list.size() > 1) {
            result = list.get(0) * 60;
            result += list.get(1);
            return result * 10;
        } else {
            return list.get(0) * 10;
        }
    }


    @SuppressLint("DefaultLocale")
    public static String fromTenthsToSeconds(int tenths) {
        if (tenths < 600) {
            return String.format("%.1f", tenths / 10.0);
        } else {
            int minutes = (tenths / 10) / 60;
            int seconds = (tenths / 10) % 60;
            return String.format("%d:%02d", minutes, seconds);
        }
    }
}
