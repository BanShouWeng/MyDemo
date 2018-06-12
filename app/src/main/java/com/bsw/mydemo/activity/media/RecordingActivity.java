package com.bsw.mydemo.activity.media;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bsw.mydemo.R;
import com.bsw.mydemo.Utils.EnvironmentShare;
import com.bsw.mydemo.Utils.PermissionUtils;
import com.bsw.mydemo.Utils.TimerUtils;
import com.bsw.mydemo.base.BaseActivity;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class RecordingActivity extends BaseActivity {

    // 多媒体播放器
    private MediaPlayer mediaPlayer;
    // 多媒体录制器
    private MediaRecorder mediaRecorder = new MediaRecorder();
    // 音频文件
    private File audioFile;

    // 传给Socket服务器端的上传和下载标志
    private final int UP_LOAD = 1;
    private final int DOWN_LOAD = 2;
    private Button btnStart;
    private Button btnStop;
    private Button btnPlay;
    private Button btnUpLoad;
    private Button btnDownLoad;

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
//                case R.id.btnStart:
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
//                    break;
//                  //停止录音
//                case R.id.btnStop:
//                    if (audioFile != null && audioFile.exists()) {
//                        // mediaRecorder.stop();
//                        mediaRecorder.reset();
//                    }
//                    msg = "已经停止录音.";
//                    break;
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
                // 上传录音文件
                case R.id.btnUpLoad:
                    // 开始上传录音文件
                    if (audioFile != null) {
                        msg = "正在上传录音文件...";
                        audioUpLoad();
                    }
                    break;
                // 下载录音文件
                case R.id.btnDownLoad:
                    // 开始下载录音文件
                    msg = "正在下载录音文件...";
                    downLoadDFile();
                    break;
            }
            // 更新标题栏 并用 Toast弹出信息提示用户
            if (! msg.equals("")) {
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
//          mediaRecorder.reset();

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
        btnUpLoad = findViewById(R.id.btnUpLoad);
        btnDownLoad = findViewById(R.id.btnDownLoad);
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
                            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
                            // 设置音频编码方式（默认的编码方式）
                            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                            // 创建一个临时的音频输出文件.record_是文件的前缀名 .amr是后缀名
                            audioFile = File.createTempFile("myDemoTest", ".MP3", EnvironmentShare.getAudioRecordDir());
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
                        if (audioFile != null && audioFile.exists()) {
                            // mediaRecorder.stop();
                            mediaRecorder.reset();
                        }
                        if (bSRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                            toast("已保存");
                            setTitle("已保存");
                        } else {
                            if (audioFile.exists()) {
                                audioFile.delete();
                            }
                            toast("已放弃");
                            setTitle("已放弃");
                        }
                        break;
                }
                timerUtils.stop();
                return false;
            }


        });
        setOnClickListener(R.id.btnPlay, R.id.btnUpLoad, R.id.btnDownLoad);
    }

    @Override
    protected void formatData() {

    }

    @Override
    protected void getBundle(Bundle bundle) {

    }

    /**
     * 上传 录音文件
     */
    private void audioUpLoad() {
//        new Thread() {
//            public void run() {
//                // DataInputStream reader = null;
//                // DataOutputStream out = null;
//                // Socket socket = null;
//                // byte[] buf = null;
//                // try {
//                // // 连接Socket
//                // socket = new Socket("192.168.42.219", 9999);
//                // // 1. 读取文件输入流
//                // reader = new DataInputStream(new BufferedInputStream(new
//                // FileInputStream(audioFile)));
//                // // 2. 将文件内容写到Socket的输出流中
//                // out = new DataOutputStream(socket.getOutputStream());
//                // out.writeInt(UP_LOAD);
//                // out.writeUTF(audioFile.getName()); // 附带文件名
//                //
//                // int bufferSize = 2048; // 2K
//                // buf = new byte[bufferSize];
//                // int read = 0;
//                // // 将文件输入流 循环 读入 Socket的输出流中
//                // while ((read = reader.read(buf)) != -1) {
//                // out.write(buf, 0, read);
//                // }
//                // handler.sendEmptyMessage(UPLOAD_SUCCESS);
//                // } catch (Exception e) {
//                // handler.sendEmptyMessage(UPLOAD_FAIL);
//                // } finally {
//                // try {
//                // // 善后处理
//                // buf = null;
//                // out.close();
//                // reader.close();
//                // socket.close();
//                // } catch (Exception e) {
//                //
//                // }
//                // }
//
//                // post请求上传
//                HttpClient httpclient = new DefaultHttpClient();
//                HttpPost post = new HttpPost("http://localhost:8080/action.jsp");
//                FileBody fileBody = new FileBody(audioFile);
//                try {
//                    StringBody stringBody = new StringBody("文件的描述");
//                    MultipartEntity entity = new MultipartEntity();
//                    entity.addPart("file", fileBody);
//                    entity.addPart("desc", stringBody);
//                    post.setEntity(entity);
//                    HttpResponse response = httpclient.execute(post);
//                    if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
//
//                        HttpEntity entitys = response.getEntity();
//                        if (entity != null) {
//                            System.out.println(entity.getContentLength());
//                            System.out.println(EntityUtils.toString(entitys));
//                        }
//                    }
//                } catch (UnsupportedEncodingException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                } catch (ClientProtocolException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                } catch (ParseException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//                httpclient.getConnectionManager().shutdown();
//
//            };
//        }.start();

    }

    /**
     * 下载录音文件
     */
    private void downLoadDFile() {
//        new Thread() {
//            public void run() {
//                DataOutputStream writer = null;
//                DataOutputStream socketOut = null;
//
//                DataInputStream inPutStream = null;
//                Socket socket = null;
//                byte[] buf = null;
//                try {
//                    // 连接Socket
//                    socket = new Socket("192.168.42.219", 9999);
//                    // 向服务端发送请求及数据
//                    socketOut = new DataOutputStream(socket.getOutputStream());
//                    socketOut.writeInt(DOWN_LOAD);
//                    socketOut.writeUTF(audioFile.getName());
//
//                    // 1. 读取Socket的输入流

//                    inPutStream = new DataInputStream(socket.getInputStream());
//                    File downLoadFile = new File(
//                            EnvironmentShare.getDownAudioRecordDir().getAbsolutePath() + "/" + audioFile.getName());
//                    downLoadFile.createNewFile();
//                    // File downLoadFile = File.createTempFile( fileName,
//                    // ".amr", EnvironmentShare.getDownAudioRecordDir());
//                    // 2. 获得文件的输出流
//                    writer = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(downLoadFile)));
//
//                    int bufferSize = 2048; // 2K
//                    buf = new byte[bufferSize];
//                    int read = 0;
//                    // 将文件输入流 循环 读入 Socket的输出流中
//                    while ((read = inPutStream.read(buf)) != -1) {
//                        writer.write(buf, 0, read);
//                    }
//                    handler.sendEmptyMessage(DOWNLOAD_SUCCESS);
//                } catch (Exception e) {
//                    handler.sendEmptyMessage(DOWNLOAD_FAIL);
//                } finally {
//                    try {
//                        // 善后处理
//                        buf = null;
//                        inPutStream.close();
//                        writer.close();
//                        socket.close();
//                    } catch (Exception e) {
//
//                    }
//                }
//            };
//        }.start();

    }

    // Socket上传下载 结果标志
    private final int UPLOAD_SUCCESS = 1;
    private final int UPLOAD_FAIL = 2;
    private final int DOWNLOAD_SUCCESS = 3;
    private final int DOWNLOAD_FAIL = 4;

    // Socket 上传下载 结果 Handler处理类
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            String showMessage = "";
            switch (msg.what) {
                case UPLOAD_SUCCESS:
                    showMessage = "录音文件上传成功！";
                    break;
                case UPLOAD_FAIL:
                    showMessage = "录音文件上传失败！";
                    break;
                case DOWNLOAD_SUCCESS:
                    showMessage = "录音文件下载成功！";
                    break;
                case DOWNLOAD_FAIL:
                    showMessage = "录音文件下载失败！";
                    break;

                default:
                    break;
            }
            // 显示提示信息并 设置标题
            EnvironmentShare.showToastAndTitle(RecordingActivity.this, showMessage, true);
        }

        ;
    };

    private TimerUtils.OnBaseTimerCallBack onBaseTimerCallBack = new TimerUtils.OnBaseTimerCallBack() {
        @Override
        public void onTick(long millisUntilFinished) {
            btnStart.setText(String.format(Locale.CHINA, "%d", time--));
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
