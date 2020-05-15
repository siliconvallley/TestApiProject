package com.dh.testproject.activity.viewpager;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.viewpager2.widget.ViewPager2;

public class OrientationController {
    private static final String HORIZONTAL = "horizontal";
    private static final String VERTICAL = "vertical";
    private ViewPager2 viewPager2;
    private Spinner spinner;

    public OrientationController(ViewPager2 viewPager2, Spinner spinner) {
        this.viewPager2 = viewPager2;
        this.spinner = spinner;
    }

    public void setUp() {
        int orientation = viewPager2.getOrientation();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                spinner.getContext(),
                android.R.layout.simple_spinner_item,
                new String[]{HORIZONTAL, VERTICAL});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        int initialPosition = adapter.getPosition(orientationToString(orientation));
        if (initialPosition >= 0) {
            spinner.setSelection(initialPosition);
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewPager2.setOrientation(stringToOrientation(parent.getSelectedItem().toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * 根据当前Item设置Viewpager2的方向
     * @param s
     * @return
     */
    @ViewPager2.Orientation
    private int stringToOrientation(String s) {
        if (s.equals(HORIZONTAL)) {
            return ViewPager2.ORIENTATION_HORIZONTAL;
        } else if (s.equals(VERTICAL)) {
            return ViewPager2.ORIENTATION_VERTICAL;
        } else {
            throw new IllegalArgumentException("Orientation $string doesn't exist");
        }
    }

    /**
     * 获取当前的Item
     * @param orientation
     * @return
     */
    private String orientationToString(int orientation) {
        if (orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
            return HORIZONTAL;
        } else if (orientation == ViewPager2.ORIENTATION_VERTICAL) {
            return VERTICAL;
        } else {
            throw new IllegalArgumentException("Orientation $orientation doesn't exist");
        }
    }
}
