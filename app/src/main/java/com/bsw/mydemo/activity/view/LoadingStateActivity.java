package com.bsw.mydemo.activity.view;

import android.os.Bundle;
import android.view.View;

import com.bsw.mydemo.R;
import com.bsw.mydemo.base.BaseActivity;
import com.bsw.mydemo.widget.LoadingState.LoadingStatusView;

public class LoadingStateActivity extends BaseActivity {
    private LoadingStatusView loadingStatusView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_loading_state;
    }

    @Override
    protected void findViews() {
        setClickView(R.id.loading_success, R.id.loading_failure, R.id.loading_reset);

        loadingStatusView = findViewById(R.id.loading_state);
        loadingStatusView.loadLoading();
    }

    @Override
    protected void formatViews() {
    }

    @Override
    protected void formatData() {

    }

    @Override
    protected void getBundle(Bundle bundle) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loading_success:
                loadingStatusView.loadSuccess();
                break;

            case R.id.loading_failure:
                loadingStatusView.loadFailure();
                break;

            case R.id.loading_reset:
                loadingStatusView.loadLoading();
                break;

        }
    }
}
