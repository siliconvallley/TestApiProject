package com.dh.testproject.databinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.dh.testproject.BR;
import com.dh.testproject.R;

public class TwoWayActivity extends AppCompatActivity {
    private static final String SHARED_PREFS_KEY = "timer";
    private static final String TAG = TwoWayActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_two_way);

        //ViewModelProvider viewModelProvider = new ViewModelProvider(this);
        ViewModelProvider viewModelProvider = new ViewModelProvider(this.getViewModelStore(), new IntervalTimerViewModelFactory());
        final TimerObservableViewModel viewModel = viewModelProvider.get(TimerObservableViewModel.class);
        ActivityTwoWayBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_two_way);
        binding.setViewModel(viewModel);

        /* Save the user settings whenever they change */
        observeAndSaveTimePerSet(
                viewModel.timePerWorkSet, R.string.prefs_timePerWorkSet);
        observeAndSaveTimePerSet(
                viewModel.timePerRestSet, R.string.prefs_timePerRestSet);

        /* Number of sets needs a different  */
        observeAndSaveNumberOfSets(viewModel);

        if (savedInstanceState == null) {
            /* If this is the first run, restore shared settings */
            restorePreferences(viewModel);
            observeAndSaveNumberOfSets(viewModel);
        }

        /*((ToggleButton)findViewById(R.id.btPause)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                viewModel.setTimerRunning(isChecked);
            }
        });*/
    }

    private void restorePreferences(TimerObservableViewModel viewModel) {
        SharedPreferences sp = getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE);
        if (sp == null) return;
        String timePerWorkSetKey = getString(R.string.prefs_timePerWorkSet);
        boolean wasAnythingRestored = false;
        if (sp.contains(timePerWorkSetKey)) {
            viewModel.timePerWorkSet.set(sp.getInt(timePerWorkSetKey, 100));
            wasAnythingRestored = true;
        }
        String timePerRestSetKey = getString(R.string.prefs_timePerRestSet);
        if (sp.contains(timePerRestSetKey)) {
            viewModel.timePerRestSet.set(sp.getInt(timePerRestSetKey, 50));
            wasAnythingRestored = true;
        }
        String numberOfSetsKey = getString(R.string.prefs_numberOfSets);
        if (sp.contains(numberOfSetsKey)) {
            int[] numbers = new int[]{0, sp.getInt(numberOfSetsKey, 5)};
            Log.e(TAG, "numbers[]: " + numbers.length);
            viewModel.setNumberOfSets(numbers);
            wasAnythingRestored = true;
        }
        if (wasAnythingRestored) {
            Log.e(TAG, "saveTimePerWorkSet ==> Preferences restored");
        }
        viewModel.stopButtonClicked();
    }

    private void observeAndSaveNumberOfSets(final TimerObservableViewModel viewModel) {
        viewModel.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (propertyId == BR.numberOfSets) {
                    Log.e(TAG, "saveTimePerWorkSet ==> Saving number of sets preference");
                    SharedPreferences sp = getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE);
                    if (sp == null) return;
                    sp.edit().putInt(getString(R.string.prefs_numberOfSets), viewModel.getNumberOfSets()[1]).apply();
                }
            }
        });
    }

    private void observeAndSaveTimePerSet(ObservableInt timePerWorkSet, final int prefsKey) {
        timePerWorkSet.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                Log.e(TAG, "saveTimePerWorkSet ==> Saving time-per-set preference");
                SharedPreferences sp = getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE);
                if (sp == null) return;
                sp.edit().putInt(getString(prefsKey), ((ObservableInt) sender).get()).apply();
            }
        });
    }
}
