package com.bsw.mydemo.activity.media;

import android.os.Bundle;
import android.view.View;

import com.bsw.mydemo.R;
import com.bsw.mydemo.Utils.Const;
import com.bsw.mydemo.Utils.PermissionUtils;
import com.bsw.mydemo.base.BaseActivity;

import java.util.List;

public class MediaActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.main_activity_btn_media);

        PermissionUtils.setRequestPermissions(activity, new PermissionUtils.PermissionGrant() {
            @Override
            public Integer[] onPermissionGranted() {
                return new Integer[] {PermissionUtils.CODE_CAMERA};
            }

            @Override
            public void onRequestResult(List<String> deniedPermission) {
                if (Const.judgeListNull(deniedPermission) != 0) {
                    toast(R.string.permission_recode_audio_hint);
                    finish();
                }
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_media;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void formatViews() {
        setOnClickListener(R.id.jumpShakeAndFlash, R.id.jumpRecording, R.id.jumpVideoRecord, R.id.jumpPicCrop);
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
            case R.id.jumpShakeAndFlash:
                jumpTo(ShakeAndFlashActivity.class);
                break;

            case R.id.jumpRecording:
                jumpTo(RecordingActivity.class);
                break;

            case R.id.jumpVideoRecord:
                jumpTo(CameraActivity.class);
                break;

            case R.id.jumpPicCrop:
                jumpTo(PicCropActivity.class);
                break;
        }
    }
}
