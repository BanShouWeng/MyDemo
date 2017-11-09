package com.example.leiming.mydemo.activity.nfc;

import android.os.Bundle;
import android.view.View;

import com.example.leiming.mydemo.R;
import com.example.leiming.mydemo.base.BaseActivity;

public class NFCActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_nfc;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void formatViews() {
        setOnClickListener(R.id.jumpNFC_r, R.id.jumpNFC_w);
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
            case R.id.jumpNFC_r:
                jumpTo(NFCReadActivity.class);
                break;

            case R.id.jumpNFC_w:
                jumpTo(NFCWriteActivity.class);
                break;
        }
    }
}
