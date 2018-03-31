package com.bsw.mydemo.zxing.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.bsw.mydemo.R;
import com.bsw.mydemo.Utils.Const;
import com.bsw.mydemo.Utils.Logger;
import com.bsw.mydemo.Utils.PermissionUtils;
import com.bsw.mydemo.Utils.TimerUtils;
import com.bsw.mydemo.Utils.UriUtils;
import com.bsw.mydemo.base.BaseActivity;
import com.bsw.mydemo.zxing.MessageIDs;
import com.bsw.mydemo.zxing.camera.CameraManager;
import com.bsw.mydemo.zxing.decoding.AlbumDecoding;
import com.bsw.mydemo.zxing.decoding.CaptureActivityHandler;
import com.bsw.mydemo.zxing.decoding.InactivityTimer;
import com.bsw.mydemo.zxing.view.ViewfinderView;
import com.google.android.exoplayer.util.UriUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.datamatrix.DataMatrixReader;
import com.google.zxing.qrcode.QRCodeReader;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

/**
 * Initial the camera
 * 扫描二维码界面
 */
public class CaptureActivity extends BaseActivity implements Callback, TimerUtils.OnBaseTimerCallBack {
    public static final String QR_RESULT = "RESULT";
    private final String SCAN_TAG = "scan_tag";
    private final String NET_CONNECT_TAG = "net_connect_tag";

    private final int REQUEST_CODE_SCAN_GALLERY = 0x78;

    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private SurfaceView surfaceView;
    private Button flashOn;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    // private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private CameraManager cameraManager;

    private TimerUtils timerUtils;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setTitle(R.string.main_activity_btn_qrcode);
        setBaseRightText(R.string.album);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);

        timerUtils = new TimerUtils(120000, 120000, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        cameraManager = new CameraManager(getApplication());

        viewfinderView.setCameraManager(cameraManager);

        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
//        initBeepSound();
        vibrate = true;

        timerUtils.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        if (cameraManager != null) {
            cameraManager.closeDriver();
        }
        if (timerUtils != null) {
            timerUtils.stop();
        }
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_capture;
    }

    @Override
    protected void findViews() {
        surfaceView = getView(R.id.preview_view);
        viewfinderView = getView(R.id.viewfinder_view);
        flashOn = getView(R.id.flash_on);
    }

    @Override
    protected void formatViews() {
        setOnClickListener(R.id.flash_on);
    }

    @Override
    protected void formatData() {

    }

    @Override
    protected void getBundle(Bundle bundle) {

    }

    private void initCamera(final SurfaceHolder surfaceHolder) {
        PermissionUtils.setRequestPermissions(activity, new PermissionUtils.PermissionGrant() {
            @Override
            public Integer[] onPermissionGranted() {
                return new Integer[] {PermissionUtils.CODE_CAMERA};
            }

            @Override
            public void onRequestResult(List<String> deniedPermission) {
                if (Const.judgeListNull(deniedPermission) == 0) {
                    try {
                        cameraManager.openDriver(surfaceHolder);
                    } catch (IOException ioe) {
                        Logger.i(getName(), ioe);
                    }
                } else {
                    toast(R.string.permission_camera_hint);
                    finish();
                }
            }
        });
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (! hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    /**
     * 编码处置
     *
     * @param obj     结果
     * @param barcode 编码信息
     */
    public void handleDecode(Result obj, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        handlerScanResult(obj);
//		showResult(obj, barcode);
    }

    @NonNull
    private void handlerScanResult(Result rawResult) {
        String resultString = rawResult.getText();
        Logger.i("resultString", String.format("resultString = %s", resultString));
        toast(resultString);
        restartPreviewAfterDelay(0);
    }

    /**
     * 扫描重启
     *
     * @param delayMS 重启的延迟
     */
    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(MessageIDs.restart_preview, delayMS);
        }
    }

    /**
     * 播放扫描音频
     */
//    private void initBeepSound() {
//        if (playBeep && mediaPlayer == null) {
//            // The volume on STREAM_SYSTEM is not adjustable, and users found it
//            // too loud,
//            // so we now play on the music stream.
//            setVolumeControlStream(AudioManager.STREAM_MUSIC);
//            mediaPlayer = new MediaPlayer();
//            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            mediaPlayer.setOnCompletionListener(beepListener);
//
//            try {
//                AssetFileDescriptor fileDescriptor = getAssets().openFd("qrbeep.ogg");
//                this.mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(),
//                        fileDescriptor.getLength());
//                this.mediaPlayer.setVolume(0.1F, 0.1F);
//                this.mediaPlayer.prepare();
//            } catch (IOException e) {
//                this.mediaPlayer = null;
//            }
//        }
//    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(RESULT_CANCELED);
            finish();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_FOCUS || keyCode == KeyEvent.KEYCODE_CAMERA) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.flash_on:
                if (flashOn.getText().equals(getResources().getString(R.string.flash_on))) {
                    cameraManager.openFlashLight();
                    flashOn.setText(R.string.flash_off);
                } else {
                    cameraManager.offFlashLight();
                    flashOn.setText(R.string.flash_on);
                }

            case RIGHT_TEXT_ID:
                //打开手机中的相册
                Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                innerIntent.setType("image/*");
                startActivityForResult(innerIntent, REQUEST_CODE_SCAN_GALLERY);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SCAN_GALLERY:
                    Result result = AlbumDecoding.handleAlbumPic(activity, data);
                    if (null == result) {
                        toast(R.string.recognize_failed);
                    } else {
                        toast(result.getText());
                    }
                    break;
            }
        }
    }

    @Override
    public void onTick(long millisUntilFinished) {
        Logger.i(getName(), millisUntilFinished + "");
    }

    @Override
    public void onFinish() {
        finish();
    }
}