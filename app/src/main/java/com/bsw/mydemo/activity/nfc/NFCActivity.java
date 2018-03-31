package com.bsw.mydemo.activity.nfc;

import android.os.Bundle;
import android.view.View;

import com.bsw.mydemo.R;
import com.bsw.mydemo.base.BaseActivity;
/**
 * @author 半寿翁
 */
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
        setOnClickListener(R.id.jumpNFC_r, R.id.jumpNFC_w, R.id.jumpNFC_sp, R.id.jumpNFC_rp, R.id.jumpNFC_write_with_password);
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

            case R.id.jumpNFC_sp:
                jumpTo(SetNFCPasswordActivity.class);
                break;

            case R.id.jumpNFC_rp:

                break;

            case R.id.jumpNFC_write_with_password:
                jumpTo(NFCWriteWithPasswordActivity.class);
                break;
        }
    }
}
