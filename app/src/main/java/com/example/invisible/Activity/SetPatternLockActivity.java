package com.example.invisible.Activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.example.invisible.Confi.BaseActivity;
import com.example.invisible.R;

import java.util.List;

public class SetPatternLockActivity extends BaseActivity {

    private TextView mAttention;

    private Toolbar mToolbar;

    private PatternLockView mPatternLockView;

    private PatternLockViewListener mListener = new PatternLockViewListener() {
        @Override
        public void onStarted() {
            Log.d(getClass().getName(), "Pattern drawing started");
        }

        @Override
        public void onProgress(List<PatternLockView.Dot> progressPattern) {
            Log.d(getClass().getName(), "Pattern progress: " +
                    PatternLockUtils.patternToString(mPatternLockView, progressPattern));
        }

        @Override
        public void onComplete(List<PatternLockView.Dot> pattern) {
            Log.d(getClass().getName(), "Pattern complete: " +
                    PatternLockUtils.patternToString(mPatternLockView, pattern));
        }

        @Override
        public void onCleared() {
            Log.d(getClass().getName(), "Pattern has been cleared");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pattern_lock);
        mPatternLockView = findViewById(R.id.pattern_lock_view);
        mPatternLockView.addPatternLockListener(mListener);
        mToolbar = findViewById(R.id.toolbar);
        setUpToolbar(mToolbar, "手势", true);
        mAttention = findViewById(R.id.attention);
    }
}
