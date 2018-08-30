package com.bsw.mydemo.widget;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;
import android.widget.Toast;

import com.bsw.mydemo.R;
import com.bsw.mydemo.Utils.Const;
import com.bsw.mydemo.Utils.Logger;

/**
 * @author 半寿翁
 * @date 2018/6/22.
 */

public class BswVideoPlayer extends TextureView {
    /**
     * 视频路径
     */
    private String videoPath;
    private Context context;
    private BswVideoPlayerListener bswVideoPlayerListener;
    /**
     * 获取视频播放进度
     */
    private Handler handler = new Handler();

    /**
     * 视频播放Surface
     */
    private Surface surface;

    /**
     * 视频播放
     */
    private MediaPlayer mediaPlayer;
    private boolean isPausing;

    public BswVideoPlayer(Context context) {
        super(context);
        this.context = context;
        setSurfaceTextureListener(surfaceTextureListener);
    }

    public BswVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setSurfaceTextureListener(surfaceTextureListener);
    }

    public BswVideoPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setSurfaceTextureListener(surfaceTextureListener);
    }

    public void setBswVideoPlayerListener(BswVideoPlayerListener bswVideoPlayerListener) {
        this.bswVideoPlayerListener = bswVideoPlayerListener;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            bswVideoPlayerListener.onCompletion();
            Logger.i("MediaPlayer", "onCompletion");
        }
    };

    private final Runnable mTicker = new Runnable() {
        public void run() {
            long now = SystemClock.uptimeMillis();
            long next = now + (1000 - now % 1000);
            Logger.i("MediaPlayer", next + "");
            if (Const.notEmpty(mediaPlayer) && mediaPlayer.isPlaying()) {
                handler.postAtTime(mTicker, next);//延迟一秒再次执行runnable,就跟计时器一样效果
            }
        }
    };

    public void play() {
        if (isPausing) {
            mediaPlayer.prepareAsync();
        } else {
            new PlayerVideoThread().start();//开启一个线程去播放视频
        }
    }

    public void pause() {
        isPausing = true;
        mediaPlayer.pause();
    }

    public void restart() {
        mediaPlayer.start();
    }

    public void stop() {
        mediaPlayer.stop();
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

                if (TextUtils.isEmpty(videoPath)) {
                    Toast.makeText(context, R.string.file_error, Toast.LENGTH_LONG).show();
                    return;
                }

                mediaPlayer.setDataSource(videoPath);//设置播放资源(可以是应用的资源文件／url／sdcard路径)
                mediaPlayer.setSurface(surface);//设置渲染画板
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);//设置播放类型
                mediaPlayer.setOnCompletionListener(onCompletionListener);//播放完成监听
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                }
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {//预加载监听
                    @Override
                    public void onPrepared(MediaPlayer mp) {//预加载完成
                        mediaPlayer.start();//开始播放
                        handler.post(mTicker);//更新进度
                    }
                });
                mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        Logger.d(getName(), "OnError - Error code: " + what + " Extra code: " + extra);
                        switch (what) {
                            case - 1004:
                                Logger.d(getName(), "MEDIA_ERROR_IO");
                                break;
                            case - 1007:
                                Logger.d(getName(), "MEDIA_ERROR_MALFORMED");
                                break;
                            case 200:
                                Logger.d(getName(), "MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK");
                                break;
                            case 100:
                                Logger.d(getName(), "MEDIA_ERROR_SERVER_DIED");
                                break;
                            case - 110:
                                Logger.d(getName(), "MEDIA_ERROR_TIMED_OUT");
                                break;
                            case 1:
                                Logger.d(getName(), "MEDIA_ERROR_UNKNOWN");
                                break;
                            case - 1010:
                                Logger.d(getName(), "MEDIA_ERROR_UNSUPPORTED");
                                break;
                        }
                        switch (extra) {
                            case 800:
                                Logger.d(getName(), "MEDIA_INFO_BAD_INTERLEAVING");
                                break;
                            case 702:
                                Logger.d(getName(), "MEDIA_INFO_BUFFERING_END");
                                break;
                            case 701:
                                Logger.d(getName(), "MEDIA_INFO_METADATA_UPDATE");
                                break;
                            case 802:
                                Logger.d(getName(), "MEDIA_INFO_METADATA_UPDATE");
                                break;
                            case 801:
                                Logger.d(getName(), "MEDIA_INFO_NOT_SEEKABLE");
                                break;
                            case 1:
                                Logger.d(getName(), "MEDIA_INFO_UNKNOWN");
                                break;
                            case 3:
                                Logger.d(getName(), "MEDIA_INFO_VIDEO_RENDERING_START");
                                break;
                            case 700:
                                Logger.d(getName(), "MEDIA_INFO_VIDEO_TRACK_LAGGING");
                                break;
                        }
                        return false;
                    }
                });

                mediaPlayer.prepareAsync();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private SurfaceTextureListener surfaceTextureListener = new SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
            surface = new Surface(surfaceTexture);
            if (TextUtils.isEmpty(videoPath)) {
                return;
            }
            new PlayerVideoThread().start();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            surface = null;
            if (Const.notEmpty(mediaPlayer)) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };

    public interface BswVideoPlayerListener {
        void onCompletion();
    }
}
