package com.bsw.mydemo.activity.media;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bsw.mydemo.R;
import com.bsw.mydemo.base.BaseActivity;
import com.bsw.mydemo.utils.EnvironmentShare;
import com.bsw.mydemo.utils.Logger;
import com.bsw.mydemo.utils.PermissionUtils;
import com.bsw.mydemo.utils.TimerUtils;
import com.bsw.mydemo.utils.net.ApiRequest;
import com.bsw.mydemo.utils.net.NetActionEnum;
import com.bsw.mydemo.utils.net.NetUtils;
import com.bsw.mydemo.utils.net.result.ResultBean;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import static com.bsw.mydemo.utils.net.NetActionEnum.POST__UPLOAD_AAC;

public class RecordingActivity extends BaseActivity {

    // 多媒体播放器
    private MediaPlayer mediaPlayer;
    // 多媒体录制器
    private MediaRecorder mediaRecorder = new MediaRecorder();
    // 音频文件
    private File audioFile;

    // 传给Socket服务器端的上传和下载标志
    private Button btnStart;
    private Button btnStop;
    private Button btnPlay;

    private TimerUtils timerUtils;

    private int time = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.main_activity_btn_recording);
        timerUtils = new TimerUtils(60 * 1000, 1000, onBaseTimerCallBack);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onClick(View view) {
        try {
            String msg = "";
            switch (view.getId()) {
                // 开始录音
                case R.id.btnStart:
//                    if (! EnvironmentShare.haveSdCard()) {
//                        Toast.makeText(this, "SD不存在，不正常录音！！", Toast.LENGTH_LONG).show();
//                    } else {
//                        // 设置音频来源(一般为麦克风)
//                        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//                        // 设置音频输出格式（默认的输出格式）
//                        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
//                        // 设置音频编码方式（默认的编码方式）
//                        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
//                        // 创建一个临时的音频输出文件.record_是文件的前缀名 .amr是后缀名
//                        audioFile = File.createTempFile("record_", ".amr", EnvironmentShare.getAudioRecordDir());
//                        // audioFile =new
//                        // File(Environment.getExternalStorageDirectory().getCanonicalPath()+"/sound.amr");
//                        // 设置录制器的文件保留路径
//                        mediaRecorder.setOutputFile(audioFile.getAbsolutePath());
//
//                        // 准备并且开始启动录制器
//                        mediaRecorder.prepare();
//                        mediaRecorder.start();
//                        msg = "正在录音...";
//                    }
                    break;
                //停止录音
                case R.id.btnStop:
                    if (audioFile != null && audioFile.exists()) {
                        // mediaRecorder.stop();
                        mediaRecorder.reset();
                    }
                    msg = "已经停止录音.";
                    break;
                // 录音文件的播放
                case R.id.btnPlay:

                    if (mediaRecorder != null) {
                        // mediaRecorder.stop();
                        mediaRecorder.reset();
                    }

                    if (audioFile != null && audioFile.exists()) {
                        Log.i("com.kingtone.www.record", ">>>>>>>>>" + audioFile);
                        mediaPlayer = new MediaPlayer();
                        // 为播放器设置数据文件
                        mediaPlayer.setDataSource(audioFile.getAbsolutePath());
                        // 准备并且启动播放器
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                setTitle("录音播放完毕.");

                            }
                        });
                        msg = "正在播放录音...";
                    }
                    break;

                default:
                    break;
            }
            // 更新标题栏 并用 Toast弹出信息提示用户
            if (!"".equals(msg)) {
                setTitle(msg);

                Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            setTitle(e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();

        }
        if (mediaRecorder != null) {
            mediaRecorder.reset();

            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recording;
    }

    @Override
    protected void findViews() {
        // 获得三个按钮的UI控件
        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);
        btnPlay = findViewById(R.id.btnPlay);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void formatViews() {
        btnStart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Rect bSRect = new Rect();
                btnStart.getGlobalVisibleRect(bSRect);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        try {
                            // 设置音频来源(一般为麦克风)
                            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                            // 设置音频输出格式（默认的输出格式）
                            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
//                            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
                            // 设置音频编码方式（默认的编码方式）
                            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
//                            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                            // 创建一个临时的音频输出文件.record_是文件的前缀名 .amr是后缀名
                            audioFile = File.createTempFile("myDemoTest", ".aac", EnvironmentShare.getAudioRecordDir());
                            // audioFile =new
                            // File(Environment.getExternalStorageDirectory().getCanonicalPath()+"/sound.amr");
                            // 设置录制器的文件保留路径
                            mediaRecorder.setOutputFile(audioFile.getAbsolutePath());
                            // 准备并且开始启动录制器
                            mediaRecorder.prepare();
                            mediaRecorder.start();
                            timerUtils.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        timerUtils.stop();
                        if (audioFile != null && audioFile.exists()) {
                            // mediaRecorder.stop();
                            mediaRecorder.reset();
                        }
                        if (bSRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                            toast("已保存");
                            setTitle("已保存");
                            NetUtils.getNewInstance(activity)
                                    .request(new NetUtils.NetRequestCallBack() {
                                                 @Override
                                                 public void success(NetActionEnum action, ResultBean resultBean, Object tag) {
                                                     Logger.i(resultBean.toString());
                                                 }

                                                 @Override
                                                 public void failure(NetActionEnum action, ResultBean resultBean, Object tag) {

                                                 }

                                                 @Override
                                                 public void error(NetActionEnum action, Throwable e, Object tag) {

                                                 }
                                             }, true, new ApiRequest<>(POST__UPLOAD_AAC, ApiRequest.SPECIAL_FILE_UPLOAD)
                                                    .setRequestType(ApiRequest.REQUEST_TYPE_POST)
                                                    .setRequestBody(audioFile)
                                    );
                        } else {
                            if (audioFile.exists()) {
                                audioFile.delete();
                            }
                            toast("已放弃");
                            setTitle("已放弃");
                        }
                        break;

                    default:
                        break;
                }
                return false;
            }


        });
        setClickView(R.id.btnPlay);
    }

    @Override
    protected void formatData() {

    }

    @Override
    protected void getBundle(Bundle bundle) {

    }

    private TimerUtils.OnBaseTimerCallBack onBaseTimerCallBack = new TimerUtils.OnBaseTimerCallBack() {
        @Override
        public void onTick(long millisUntilFinished) {
            btnStart.setText(String.format(Locale.CHINA, "%d", time--));
            Logger.i("TimerUtils", time + "");
        }

        @Override
        public void onFinish() {
            if (mediaRecorder != null && audioFile != null && audioFile.exists()) {
                // mediaRecorder.stop();
                mediaRecorder.reset();
            }
            btnStart.setText("btnStart");
            toast("已保存");
            setTitle("已保存");
            time = 60;
        }
    };
}
