package com.bsw.mydemo.zxing.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import com.bsw.mydemo.R;
import com.bsw.mydemo.Utils.Const;
import com.bsw.mydemo.Utils.Logger;
import com.bsw.mydemo.Utils.PermissionUtils;
import com.bsw.mydemo.Utils.TimerUtils;
import com.bsw.mydemo.base.BaseActivity;
import com.bsw.mydemo.zxing.MessageIDs;
import com.bsw.mydemo.zxing.camera.CameraManager;
import com.bsw.mydemo.zxing.decoding.AlbumDecoding;
import com.bsw.mydemo.zxing.decoding.CaptureActivityHandler;
import com.bsw.mydemo.zxing.decoding.InactivityTimer;
import com.bsw.mydemo.zxing.view.ViewfinderView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

/**
 * Initial the camera
 * 扫描二维码界面
 */
public abstract class CaptureActivity extends BaseActivity implements Callback, TimerUtils.OnBaseTimerCallBack {

    /**
     * 不自动关闭扫码页面（如不自动关闭，扫码页面长时间前台运行可能会导致内存溢出崩溃）
     */
    protected final int DO_NOT_CLOSE = 0;

    private CaptureActivityHandler handler;
    protected ViewfinderView viewfinderView;
    protected SurfaceView surfaceView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    // private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    protected CameraManager cameraManager;

    private TimerUtils timerUtils;
    private int autoCloseTime = 0;

    /**
     * Called when the activity is first created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//		requestWindowFeature(Window.FEATURE_NO_TITLE);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);

        autoCloseTime = setAutoCloseTime();
        if (autoCloseTime != DO_NOT_CLOSE) {
            timerUtils = new TimerUtils(autoCloseTime, autoCloseTime, this);
        }
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
        if (autoCloseTime != DO_NOT_CLOSE) {
            timerUtils.start();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 打开相册
     *
     * @param requestCode 请求码
     */
    protected void openAlbum(int requestCode) {
        //打开手机中的相册
        Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        innerIntent.setType("image/*");
        startActivityForResult(innerIntent, requestCode);
    }

    /**
     * 长时间前台运行关闭时间
     *
     * @return 时间（毫秒级）
     */
    protected abstract int setAutoCloseTime();

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

    /**
     * 获取扫描结果
     *
     * @param data 相册中获取的图片信息
     * @return 扫描结果
     */
    public Result getResult(Intent data) {
        return AlbumDecoding.handleAlbumPic(activity, data);
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