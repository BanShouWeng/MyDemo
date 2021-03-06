package com.bsw.mydemo.activity.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.bsw.mydemo.R;
import com.bsw.mydemo.utils.Const;
import com.bsw.mydemo.zxing.activity.CaptureActivity;
import com.google.zxing.Result;

public class ScanCodeActivity extends CaptureActivity {

    private final int OPEN_ALBUM = 0x78;
    private Button flashOn;

    private boolean forResult = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.main_activity_btn_qrcode);
        setBaseRightText(R.string.album);
        setBaseBack();
    }

    @Override
    protected int setAutoCloseTime() {
        return 120000;
    }

    @Override
    protected void getResultString(String resultString) {
        if (forResult) {
            Intent intent = new Intent();
            intent.putExtra("result", resultString);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            toast(resultString);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_scan_code;
    }

    @Override
    protected void findViews() {
        // 布局中必须有这两个控件ViewfinderView，SurfaceView
        surfaceView = getView(R.id.preview_view);
        viewfinderView = getView(R.id.viewfinder_view);

        flashOn = getView(R.id.flash_on);
    }

    @Override
    protected void formatViews() {
        setClickView(R.id.flash_on);
    }

    @Override
    protected void formatData() {

    }

    @Override
    protected void getBundle(Bundle bundle) {
        if (Const.notEmpty(bundle)) {
            String tag = bundle.getString("tag");
            if (! TextUtils.isEmpty(tag) && tag.equals("getCode")) {
                forResult = true;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case OPEN_ALBUM:
                    Result result = getResult(data);
                    if (null == result) {
                        toast(R.string.recognize_failed);
                    } else {
                        if (forResult) {
                            Intent intent = new Intent();
                            intent.putExtra("result", result.getText());
                            setResult(RESULT_OK, intent);
                            finish();
                        } else {
                            toast(result.getText());
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 控制闪光灯开关
            case R.id.flash_on:
                if (flashOn.getText().equals(getResources().getString(R.string.flash_on))) {
                    cameraManager.openFlashLight();
                    flashOn.setText(R.string.flash_off);
                } else {
                    cameraManager.offFlashLight();
                    flashOn.setText(R.string.flash_on);
                }
                break;

            // 打开相册
            case RIGHT_TEXT_ID:
                openAlbum(OPEN_ALBUM);
                break;

            case BACK_ID:
                if (forResult) {
                    setResult(RESULT_CANCELED);
                }
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (forResult) {
                setResult(RESULT_CANCELED);
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
