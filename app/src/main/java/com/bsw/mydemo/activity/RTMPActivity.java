package com.bsw.mydemo.activity;

import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;

import com.bsw.mydemo.R;
import com.bsw.mydemo.base.BaseBean;
import com.bsw.mydemo.base.BaseNetActivity;

import java.io.IOException;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
/**
 * @author 半寿翁
 */
public class RTMPActivity extends BaseNetActivity implements View.OnClickListener, TextureView.SurfaceTextureListener {

    private String path = "rtmp:....";
    private IMediaPlayer mediaPlayer;
    private Surface surface;

    private TextureView textureView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_rtmp;
    }

    @Override
    protected void findViews() {
        imageView = (ImageView) findViewById(R.id.rymp_image);
        textureView = (TextureView) findViewById(R.id.rymp_view);
    }

    @Override
    protected void formatViews() {
        setTitle("视频");

        imageView.setOnClickListener(this);
        textureView.setSurfaceTextureListener(this);
    }

    @Override
    protected void formatData() {

    }

    @Override
    protected void getBundle(Bundle bundle) {

    }

    @Override
    public void success(String action, BaseBean baseBean) {

    }

    @Override
    public void error(String action, Throwable e) {

    }

    public IMediaPlayer createMediaPlayer() {
        IjkMediaPlayer ijkMediaPlayer = null;
        if (!TextUtils.isEmpty(path)) {
            ijkMediaPlayer = new IjkMediaPlayer();
            IjkMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_DEBUG);
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 0);
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 0);
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", IjkMediaPlayer.SDL_FCC_RV32);
        }
        return ijkMediaPlayer;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rymp_image:
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
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        surface = null;
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    private class PlayerVideoThread extends Thread {
        @Override
        public void run() {
            try {
                mediaPlayer = createMediaPlayer();
                // 是否成功获取视频流的监听
//                mediaPlayer.setOnPreparedListener(mPreparedListener);
//                mediaPlayer.setOnVideoSizeChangedListener(mSizeChangedListener);
//                mediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);

                // 播放流程未调用
//                mediaPlayer.setOnSeekCompleteListener(mSeekCompleteListener);
//                mediaPlayer.setOnCompletionListener(mCompletionListener);
//                mediaPlayer.setOnErrorListener(mErrorListener);
//                mediaPlayer.setOnInfoListener(mInfoListener);
//                mediaPlayer.setOnTimedTextListener(mOnTimedTextListener);
                try {
                    mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(path), null);
                    mediaPlayer.setSurface(surface);
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setScreenOnWhilePlaying(true);
                    mediaPlayer.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
