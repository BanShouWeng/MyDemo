package com.bsw.mydemo.activity;

import android.app.Service;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.bsw.mydemo.R;
import com.bsw.mydemo.Utils.Const;
import com.bsw.mydemo.Utils.Logger;
import com.bsw.mydemo.Utils.PermissionUtils;
import com.bsw.mydemo.base.BaseActivity;

import java.util.List;
/**
 * @author 半寿翁
 */
public class ShakeAndFlashActivity extends BaseActivity {

    /**
     * 震动
     */
    private Vibrator vibrator;

    private int maxMusic;
    private int currentMusic;
    private int maxSystem;
    private int currentSystem;
    /**
     * 音频播放器
     */
    private SoundPool soundPool;
    private int streamId;
    private MediaPlayer mediaPlayer;
    /**
     * 闪光灯
     */
    private boolean isFlash;
    private Camera camera;
    private Camera.Parameters parameter;
    private boolean isPause;
    private AudioManager mAudioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("震动/闪光灯/音频");

        mAudioManager = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
        mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);

        maxMusic = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        currentMusic = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxSystem = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
        currentSystem = mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
    }

    /**
     * 设置最大音量
     */
    private void setMaxVoice() {
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxMusic, 0);
        mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, maxSystem, 0);
    }

    /**
     * 还原之前音量
     */
    private void setCurrentVoice() {
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentMusic, 0);
        mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, currentSystem, 0);
    }

    /**
     * 播放音频
     * 在Manifest中配置权限<uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS" />
     */
    private void alert() {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
        }
        mediaPlayer = MediaPlayer.create(context, R.raw.fire_alert);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setLooping(true);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer.start();
                Log.i(ShakeAndFlashActivity.class.getSimpleName(), "开始");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            mAudioManager.setSpeakerphoneOn(true);
                            Log.i(ShakeAndFlashActivity.class.getSimpleName(), "公放");
                        }
                    }
                }).start();
            }
        });
    }

    /**
     * 播放音乐
     */
    public void play() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shake_and_flash;
    }

    @Override
    protected void findViews() {
        setOnClickListener(R.id.startMusic, R.id.stopMusic, R.id.startFlash, R.id.stopFlash, R.id.startShake, R.id.stopShake);
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

    /**
     * 开启闪烁
     */
    private void flash() {
        isFlash = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isFlash) {
                    openFlashLight();
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    offFlashLight();
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        Logger.i("InterruptedException", e);
                    }
                }
            }
        }).start();
    }

    /**
     * 打开闪光灯
     */
    public void openFlashLight() {
        try {
            if (camera == null) {
                camera = Camera.open();
                int textureId = 0;
                camera.setPreviewTexture(new SurfaceTexture(textureId));
                camera.startPreview();
            }
            parameter = camera.getParameters();
            parameter.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(parameter);
        } catch (Exception e) {
            Logger.i("Exception", e);
        }
    }

    /**
     * 关闭闪光灯
     */
    public void offFlashLight() {
        if (camera != null) {
            parameter = camera.getParameters();
            parameter.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(parameter);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    public void pause() {
        if (isPlaying()) {
            isPause = true;
            mediaPlayer.pause();
        }
    }

    public void resume() {
        if (isPause) {
            isPause = false;
            mediaPlayer.start();
        }
    }

    /**
     * 停止播放
     */
    public void stop() {
        if (isPlaying()) {
            try {
                mediaPlayer.stop();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 是否正在播放
     *
     * @return 正在播放返回true, 否则返回false
     */
    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startMusic:
                setMaxVoice();
                alert();
                break;

            case R.id.stopMusic:
                mediaPlayer.stop();
                setCurrentVoice();

                mAudioManager.setSpeakerphoneOn(false);
                break;

            case R.id.startFlash:
                PermissionUtils.setRequestPermissions(activity, new PermissionUtils.PermissionGrant() {
                    @Override
                    public Integer[] onPermissionGranted() {
                        return new Integer[]{PermissionUtils.CODE_CAMERA};
                    }

                    @Override
                    public void onRequestResult(List<String> deniedPermission) {
                        if (Const.judgeListNull(deniedPermission) == 0) {
                            flash();
                        }
                    }
                });
                break;

            case R.id.stopFlash:
                isFlash = false;
                break;

            case R.id.startShake:
                //设置震动周期，数组表示时间：等待+执行，单位是毫秒，下面操作代表:等待100，执行100，等待100，执行1000，
                //后面的数字如果为-1代表不重复，之执行一次，其他代表会重复，0代表从数组的第0个位置开始
                if (vibrator == null) {
                    vibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
                }
                vibrator.vibrate(new long[]{300, 1000}, 0);
                break;

            case R.id.stopShake:
                //取消震动
                if (vibrator != null) {
                    vibrator.cancel();
                }
                break;
        }
    }
}
