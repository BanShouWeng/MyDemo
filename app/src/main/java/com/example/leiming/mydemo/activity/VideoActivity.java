package com.example.leiming.mydemo.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;

import com.example.leiming.mydemo.R;

public class VideoActivity extends AppCompatActivity implements View.OnClickListener, TextureView.SurfaceTextureListener, MediaPlayer.OnCompletionListener {

    @SuppressLint("AuthLeak")
    private String path1 = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
    private String path2 = "rtsp://...";

    private MediaPlayer mediaPlayer;
    private Surface surface;
    private TextureView textureView;
    private ImageView imageView;

    private Handler handler = new Handler();

    private final Runnable mTicker = new Runnable() {
        public void run() {
            long now = SystemClock.uptimeMillis();
            long next = now + (1000 - now % 1000);

            handler.postAtTime(mTicker, next);//延迟一秒再次执行runnable,就跟计时器一样效果
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        imageView = (ImageView) findViewById(R.id.image);
        textureView = (TextureView) findViewById(R.id.video_view);
        imageView.setOnClickListener(this);
        textureView.setSurfaceTextureListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image:
                Bitmap bitmap = textureView.getBitmap();
                imageView.setImageBitmap(bitmap);
                break;
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        surface = new Surface(surfaceTexture);
        new PlayerVideoThread().start();//开启一个线程去播放视频
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        surface = null;
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    private class PlayerVideoThread extends Thread {
        @Override
        public void run() {
            try {
                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying())
                        mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
                mediaPlayer = new MediaPlayer();

                mediaPlayer.setDataSource(TextUtils.isEmpty(path1) ? path2 : path1);//设置播放资源(可以是应用的资源文件／url／sdcard路径)
                mediaPlayer.setSurface(surface);//设置渲染画板
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);//设置播放类型
                mediaPlayer.setOnCompletionListener(VideoActivity.this);//播放完成监听
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {//预加载监听
                    @Override
                    public void onPrepared(MediaPlayer mp) {//预加载完成
                        mediaPlayer.start();//开始播放
                        handler.post(mTicker);//更新进度
                    }
                });
                mediaPlayer.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
