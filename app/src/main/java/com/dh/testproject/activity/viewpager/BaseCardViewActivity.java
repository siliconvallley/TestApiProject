package com.dh.testproject.activity.viewpager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.dh.testproject.R;

public abstract class BaseCardViewActivity extends AppCompatActivity {

    private static final String TAG = "BaseCardViewActivity";
    private Spinner orientationSpinner;
    private CheckBox disableUserInputCheckbox;
    private Button jumpButton;
    private Spinner cardSpinner;
    private CheckBox smoothScrollCheckbox;
    private CheckBox rotateCheckbox;
    private CheckBox translateCheckbox;
    private CheckBox scaleCheckbox;
    protected ViewPager2 viewPager2;
    private boolean translateX;
    private boolean translateY;

    public boolean isTranslateX() {
        return viewPager2.getOrientation() == ViewPager2.ORIENTATION_VERTICAL
                && translateCheckbox.isChecked();
    }

    public boolean isTranslateY() {
        return viewPager2.getOrientation() == ViewPager2.ORIENTATION_HORIZONTAL
                && translateCheckbox.isChecked();
    }

    private ViewPager2.PageTransformer pageTransformer = new ViewPager2.PageTransformer() {
        @Override
        public void transformPage(@NonNull View page, float position) {
            float absP = Math.abs(position);
            Log.e(TAG, "position: " + position + "+++>absP: " + absP);
            page.setRotation(rotateCheckbox.isChecked() ? position * 360 : 0f);
            page.setTranslationX(isTranslateX() ? absP * 500f : 0f);
            page.setTranslationY(isTranslateY() ? absP * 400f : 0f);
            if (scaleCheckbox.isChecked()) {
                float scale = absP > 1 ? 0f : 1 - absP;
                page.setScaleX(scale);
                page.setScaleY(scale);
            } else {
                page.setScaleX(1f);
                page.setScaleY(1f);
            }
        }
    };

    protected int getLayoutId(){
        return R.layout.activity_card_view;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        init();
    }

    private void init() {
        viewPager2 = findViewById(R.id.vp);
        orientationSpinner = findViewById(R.id.orientation_spinner);
        disableUserInputCheckbox = findViewById(R.id.disable_user_input_checkbox);
        jumpButton = findViewById(R.id.jump_button);
        cardSpinner = findViewById(R.id.card_spinner);
        smoothScrollCheckbox = findViewById(R.id.smooth_scroll_checkbox);
        rotateCheckbox = findViewById(R.id.rotate_checkbox);
        translateCheckbox = findViewById(R.id.translate_checkbox);
        scaleCheckbox = findViewById(R.id.scale_checkbox);

        new UserInputController(viewPager2, disableUserInputCheckbox).setUp();
        new OrientationController(viewPager2, orientationSpinner).setUp();
        cardSpinner.setAdapter(createCardAdapter());

        viewPager2.setPageTransformer(pageTransformer);

        jumpButton.setOnClickListener(onClickListener);
        rotateCheckbox.setOnClickListener(onClickListener);
        translateCheckbox.setOnClickListener(onClickListener);
        scaleCheckbox.setOnClickListener(onClickListener);
    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.jump_button:
                    viewPager2.setCurrentItem(cardSpinner.getSelectedItemPosition(), smoothScrollCheckbox.isChecked());
                    break;
                case R.id.rotate_checkbox:
                case R.id.translate_checkbox:
                case R.id.scale_checkbox:
                    viewPager2.requestTransform();
                    break;
            }
        }
    };

    private SpinnerAdapter createCardAdapter() {
        ArrayAdapter<Card> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Card.Companion.getDECK());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }
}
