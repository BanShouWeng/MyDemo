package com.bsw.mydemo.widget.ImgAndVideo;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ImageUploadToQiniuActivity extends AppCompatActivity {

//    private String path;
//    private int type;
//    private int size;
//    private UploadManager uploadManager;
//    private Map<String, String> params = new HashMap<String, String>();
//    private String uid, safeToken;
//    private boolean canUpload = false;
//    private Map<String, String> map = new HashMap<>();
//    private MyHandler myHandler;
//
//    @Bind(R.id.upload_image_out_of_size)
//    RelativeLayout uploadImageOutOfSize;
//    @Bind(R.id.upload_image)
//    RelativeLayout uploadImage;
//    @Bind(R.id.upload_image_progress_bar)
//    LoadingProgressBar uploadImageProgressBar;
//    @Bind(R.id.upload_image_progress_tv)
//    TextView uploadImageProgressTv;
//    @Bind(R.id.file_too_large)
//    TextView fileTooLarge;
//    @Bind(R.id.file_cannot_upload)
//    TextView fileCannotUpload;
//
//    @OnClick({R.id.upload_image_out_of_size_close, R.id.upload_image_cancel_upload})
//    public void jump(View v) {
//        switch (v.getId()) {
//            case R.id.upload_image_out_of_size_close:
//                finish();
//                break;
//            case R.id.upload_image_cancel_upload:
//                stopUpload();
//                break;
//        }
//    }
//
//    //终止上传
//    private void stopUpload() {
//        final android.app.AlertDialog myDialog = new android.app.AlertDialog.Builder(ImageUploadToQiniuActivity.this).create();
//        myDialog.show();
//        myDialog.getWindow().setContentView(R.layout.stop_upload);
//        myDialog.getWindow().findViewById(R.id.tv_ok)
//                .setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        finish();
//                        myDialog.dismiss();
//                    }
//                });
//        myDialog.getWindow().findViewById(R.id.tv_cancel)
//                .setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        myDialog.dismiss();
//                    }
//                });
//    }
//
//    //文件类型选择错误
//    private void wrongFile() {
//        final android.app.AlertDialog myDialog = new android.app.AlertDialog.Builder(ImageUploadToQiniuActivity.this).create();
//        myDialog.show();
//        myDialog.getWindow().setContentView(R.layout.wrong_file);
//        myDialog.getWindow().findViewById(R.id.tv_ok)
//                .setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        finish();
//                        myDialog.dismiss();
//                    }
//                });
//        myDialog.getWindow().findViewById(R.id.tv_cancel)
//                .setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        chooseFile();
//                        myDialog.dismiss();
//                    }
//                });
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_image_upload_to_qiniu);
//        ButterKnife.bind(this);
//        myHandler = new MyHandler();
//        init();
//        chooseFile();
//    }
//
//    private void chooseFile() {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("image/*;video/*");
//        startActivityForResult(intent, 1);
//    }
//
//    private void init() {
//        String dirPath = "/storage/emulated/0/Download";
//        Recorder recorder = null;
//        try {
//            File f = File.createTempFile("qiniu_xxxx", ".tmp");
//            Log.d("qiniu", f.getAbsolutePath().toString());
//            dirPath = f.getParent();
//            recorder = new FileRecorder(f.getAbsolutePath());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        final String dirPath1 = dirPath;
//        //默认使用 key 的url_safe_base64编码字符串作为断点记录文件的文件名。
//        //避免记录文件冲突（特别是key指定为null时），也可自定义文件名(下方为默认实现)：
//        KeyGenerator keyGen = new KeyGenerator() {
//            public String gen(String key, File file) {
//                // 不必使用url_safe_base64转换，uploadManager内部会处理
//                // 该返回值可替换为基于key、文件内容、上下文的其它信息生成的文件名
//                return key + "_._" + new StringBuffer(file.getAbsolutePath()).reverse();
//            }
//        };
//
//        Configuration config = new Configuration.Builder()
//                .chunkSize(256 * 1024)  //分片上传时，每片的大小。 默认256K
//                .putThreshhold(512 * 1024)  // 启用分片上传阀值。默认512K
//                .connectTimeout(10) // 链接超时。默认10秒
//                .responseTimeout(60) // 服务器响应超时。默认60秒
//                .recorder(recorder)  // recorder分片上传时，已上传片记录器。默认null
//                .recorder(recorder, keyGen)  // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
//                .zone(Zone.zone0) // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
//                .build();
//        uploadManager = new UploadManager(config);
////        uploadManager = new UploadManager();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == 1) {
//            if (resultCode == Activity.RESULT_OK) {//是否选择，没选择就不会继续
//                Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
//                String[] proj = {MediaStore.Images.Media.DATA};
//                Cursor actualimagecursor = managedQuery(uri, proj, null, null, null);
//                int actualImageColumnIndex = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                actualimagecursor.moveToFirst();
//                String img_path = actualimagecursor.getString(actualImageColumnIndex);
//                File file = new File(img_path);
//                path = file.toString();
//                type = FileUtils.getFileType(path);
//                if (type == 0) {
//                    wrongFile();
//                    return;
//                }
//                judgeFileSize();
//            }
//        }
//    }
//
//    private void judgeFileSize() {
//        try {
//            String length = FileUtils.formentFileSize(FileUtils.getFileSizes(path));
//            if (length.contains("G")) {
//                showWarningDialog(false);
//                return;
//            } else if (length.contains("M")) {
//                if (Double.parseDouble(length.substring(0, length.indexOf("M"))) > 10) {
//                    showWarningDialog(false);
//                    return;
//                }
//            }
//            size = FileUtils.formentFileSize_Kb(FileUtils.getFileSizes(path));
//            getQiniuToken();
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(ImageUploadToQiniuActivity.this, "获取图片失败", Toast.LENGTH_LONG).show();
//        }
//    }
//
//    private void getQiniuToken() {
//        if (params == null) {
//            params = new HashMap<String, String>();
//        }
//        uid = App.getInstence().getUid();
//        safeToken = App.getInstence().getToken();
//        params.put("action", "getQiNiuToken");
//        params.put("uid", uid == null ? "-1000" : uid);
//        params.put("clientType", 2 + "");
//        params.put("platform", "android");
//        params.put("safeToken", safeToken == null ? "anonymous" : safeToken);
//        VolleyUtils.getInstance().get(ImageUploadToQiniuActivity.this, params, BuildConfig.SERVER_PATH + "getQiNiuToken"
//                , successListner);
//    }
//
//    private void showWarningDialog(Boolean uploadFail) {
//        if (uploadFail) {
//            fileTooLarge.setText("很遗憾，您的文件上传失败");
//            fileCannotUpload.setText("请检查网络环境");
//        }
//        uploadImage.setVisibility(View.GONE);
//        uploadImageOutOfSize.setVisibility(View.VISIBLE);
//    }
//
//    Response.Listener successListner = new Response.Listener<String>() {
//        public void onResponse(String responseString) {
//            QiNiuTokenResponse qiNiuTokenResponse = new Gson().fromJson(responseString, QiNiuTokenResponse.class);
//            int code = qiNiuTokenResponse.getCode();
//            if (code == 2000) {
//                final String keyNum = "android_qiniu_test" + "_" + uid + new Date().getTime();
//                String uploadToken = qiNiuTokenResponse.getUploadToken();
//                Log.i("progress", "uploadToken = " + uploadToken);
//                File file = new File(path);
//                uploadManager.put(file, keyNum, uploadToken, new UpCompletionHandler() {
//                    @Override
//                    public void complete(String key, ResponseInfo responseInfo, JSONObject jsonObject) {
//                        Log.i("progress", "jsonObject = " + jsonObject);
//                        if (jsonObject != null) {
//                            Message message = myHandler.obtainMessage();
//                            message.arg1 = 1;
//                            message.obj = 100.00;
//                            message.sendToTarget();
//                            if (key.equals(keyNum)) {
//                                Intent intent = new Intent();
//                                intent.putExtra("imageKey", key);
//                                intent.putExtra("size", size);
//                                intent.putExtra("type", type);
//                                Log.i("str_refresh", "imageKey = " + key + " size  = " + size + " type = " + type);
//                                setResult(RESULT_OK, intent);
//                                finish();
//                                return;
//                            }
//                        }
//                        showWarningDialog(true);
//                    }
//                }, new UploadOptions(null, null, false, new UpProgressHandler() {
//                    @Override
//                    public void progress(String s, double v) {
//                        Log.i("progress", "progress = " + v);
//                        Message message = myHandler.obtainMessage();
//                        message.arg1 = 1;
//                        message.obj = v;
//                        message.sendToTarget();
//                    }
//                }, new UpCancellationSignal() {
//                    @Override
//                    public boolean isCancelled() {
//                        return canUpload;
//                    }
//                }));
//            } else {
//                Toast.makeText(ImageUploadToQiniuActivity.this, "网络不稳定，上传失败", Toast.LENGTH_SHORT).show();
//            }
//        }
//    };
//
//    class MyHandler extends Handler {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.arg1) {
//                case 1:
//                    uploadImageProgressBar.setProgress(Float.parseFloat(String.format("%.2f", msg.obj)));//保留两位小数
//                    uploadImageProgressTv.setText(Float.parseFloat(String.format("%.2f", msg.obj)) + "%");
//                    break;
//            }
//        }
//    }
}
