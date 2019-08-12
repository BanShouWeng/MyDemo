package com.bsw.mydemo.activity.view;

import android.os.Bundle;
import android.view.View;

import com.bsw.mydemo.R;
import com.bsw.mydemo.base.BaseActivity;
import com.bsw.mydemo.widget.SeismicWaveView;

public class WaveViewActivity extends BaseActivity {

    private SeismicWaveView waveView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wave_view;
    }

    @Override
    protected void findViews() {
        waveView = findViewById(R.id.wave_view);
    }

    @Override
    protected void formatViews() {
        waveView.start();
    }

    @Override
    protected void formatData() {

    }

    @Override
    protected void getBundle(Bundle bundle) {

    }

    @Override
    public void onClick(View v) {

    }
}
