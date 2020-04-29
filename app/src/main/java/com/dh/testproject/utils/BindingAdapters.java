package com.dh.testproject.utils;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.databinding.BindingAdapter;

import com.dh.testproject.R;
import com.dh.testproject.databinding.Popularity;

public class BindingAdapters {

    @BindingAdapter("app:popularityIcon")
    public static void popularityIcon(ImageView imageView, Popularity popularity) {
        int color = getAssociatedColor(imageView.getContext(), popularity);
        ImageViewCompat.setImageTintList(imageView, ColorStateList.valueOf(color));
        imageView.setImageDrawable(getDrawablePopularity(imageView.getContext(), popularity));
    }

    @BindingAdapter(value = {"app:progressScaled", "app:max"}, requireAll = false)
    public static void setProgress(ProgressBar progressBar, int likes, int max) {
        progressBar.setProgress(Math.min(likes, max));
    }

    @BindingAdapter("app:progressTint")
    public static void progressTint(ProgressBar progressBar, Popularity popularity) {
        int color = getAssociatedColor(progressBar.getContext(), popularity);
        progressBar.setProgressTintList(ColorStateList.valueOf(color));
    }

    @BindingAdapter("app:hideIfZero")
    public static void hideIfZero(View view, int likes) {
        view.setVisibility((likes == 0) ? View.GONE : View.VISIBLE);
    }

    private static Drawable getDrawablePopularity(Context context, Popularity popularity) {
        if (popularity == Popularity.NORMAL) {
            return ContextCompat.getDrawable(context, R.drawable.ic_person_black_96dp);
        } else {
            return ContextCompat.getDrawable(context, R.drawable.ic_whatshot_black_96dp);
        }
    }

    private static int getAssociatedColor(Context context, Popularity popularity) {
        int color = 0;
        switch (popularity) {
            case STAR:
                color = ContextCompat.getColor(context, R.color.star);
                break;
            case POPULAR:
                color = ContextCompat.getColor(context, R.color.popular);
                break;
            case NORMAL:
                color = context.getTheme().obtainStyledAttributes(new int[]{android.R.attr.colorForeground}).getColor(0, 0x000000);
                break;
        }
        return color;
    }


    @BindingAdapter("app:clearTextOnFocus")
    public static void clearTextOnFocus(EditText editText, boolean enabled) {
        if (enabled) {
            clearOnFocusAndDispatch(editText, null);
        } else {
            editText.setOnFocusChangeListener(null);
        }
    }

    @BindingAdapter("app:clearOnFocusAndDispatch")
    public static void clearOnFocusAndDispatch(final EditText editText, final View.OnFocusChangeListener listener) {
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                TextView textView = (TextView) v;
                if (hasFocus) {
                    // Delete contents of the EditText if the focus entered.
                    editText.setTag(R.id.previous_value, textView.getText());
                    textView.setText("");
                } else {
                    if (textView.getText().length() == 0) {
                        CharSequence tag = (CharSequence) textView.getTag(R.id.previous_value);
                        textView.setText(tag != null ? tag : "");
                    }
                    // If the focus left, update the listener
                    listener.onFocusChange(v, false);
                }
            }
        });
    }


    @BindingAdapter("app:loseFocusWhen")
    public static void loseFocusWhen(EditText editText, boolean condition) {
        if (condition) editText.clearFocus();
    }

    @BindingAdapter("app:hideKeyboardOnInputDone")
    public static void hideKeyboardOnInputDone(final EditText editText, boolean enabled) {

        if (!enabled) {
            return;
        }

        TextView.OnEditorActionListener listener = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    editText.clearFocus();
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    }
                }
                return false;
            }
        };
        editText.setOnEditorActionListener(listener);
    }


    @BindingAdapter({"app:animateBackground", "app:animateBackgroundStage"})
    public static void animateBackground(View view, boolean timerRunning, boolean activeStage) {
        // If the timer is not running, don't animate and set the default color.
        if (!timerRunning) {
            view.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.disabledInputColor));
            // This tag prevents a glitch going from reset to started.
            view.setTag(R.id.hasBeenAnimated, false);
        }
        // activeStage controls whether this particular timer (work or rest) is active.
        if (activeStage) {
            // Start animation
            animateBgColor(view, true);
            // This tag prevents a glitch going from paused to started.
            view.setTag(R.id.hasBeenAnimated, true);
        } else {
            // Prevent "end" animation if animation never started
            boolean hasBeenAnimated = (boolean) view.getTag(R.id.hasBeenAnimated);
            if (hasBeenAnimated) {
                animateBgColor(view, false);
                view.setTag(R.id.hasBeenAnimated, false);
            }
        }
    }

    private static final long BG_COLOR_ANIMATION_DURATION = 500L;
    private static final long VERTICAL_BIAS_ANIMATION_DURATION = 900L;

    private static void animateBgColor(View view, boolean tint) {
        int colorRes = ContextCompat.getColor(view.getContext(), R.color.colorPrimaryLight);

        int color2Res = ContextCompat.getColor(view.getContext(), R.color.disabledInputColor);

        ObjectAnimator animator;
        if (tint) {
            animator = ObjectAnimator.ofObject(view,
                    "backgroundColor",
                    new ArgbEvaluator(),
                    color2Res,
                    colorRes);
        } else {
            animator = ObjectAnimator.ofObject(view,
                    "backgroundColor",
                    new ArgbEvaluator(),
                    colorRes,
                    color2Res);
        }

        animator.setDuration(BG_COLOR_ANIMATION_DURATION);
        animator.start();
    }
}
