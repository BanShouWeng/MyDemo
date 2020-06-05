package com.bsw.mydemo.activity.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.bsw.mydemo.BuildConfig;
import com.bsw.mydemo.R;
import com.bsw.mydemo.activity.view.ScanCodeActivity;
import com.bsw.mydemo.base.BaseActivity;
import com.bsw.mydemo.service.RetrofitFileDownloadService;
import com.bsw.mydemo.utils.Const;
import com.bsw.mydemo.utils.FileUtils;
import com.bsw.mydemo.utils.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;
import javax.net.ssl.X509TrustManager;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class FileDownloadActivity extends BaseActivity {
    private static final String TAG = "FileDownloadActivity";

    private final int REQUEST_CODE = 102;
    private ProgressBar uploadProgress;
    //下载地址
    private String inputText = "hzyzx.apk";
//    private String inputText = "FosLink_Safe.apk";
//    private String inputText = "FosLink_heima.apk";
    private EditText etUploadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.main_activity_btn_file_download);
        setBaseRightText(R.string.confirm);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_file_download;
    }

    @Override
    protected void findViews() {
        etUploadUrl = getView(R.id.et_download_url);
        uploadProgress = getView(R.id.main_progress);
    }

    @Override
    protected void formatViews() {
        setClickView(R.id.to_scan_qrCode, R.id.upgrade);
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
            case RIGHT_TEXT_ID:
                Logger.i(getName(), "inputText = " + inputText);
                break;

            case R.id.to_scan_qrCode:
                Bundle bundle = new Bundle();
                bundle.putString("tag", "getCode");
                jumpTo(ScanCodeActivity.class, REQUEST_CODE, bundle);

            case R.id.upgrade:
                Const.threadPoolExecute(new Runnable() {
                    @Override
                    public void run() {
                        getDownloadService();
                    }
                });
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            inputText = data.getStringExtra("result");
            etUploadUrl.setText(inputText);
        }
    }

    private void getDownloadService() {
        // 监听请求条件
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(60, TimeUnit.SECONDS);
        builder.readTimeout(60, TimeUnit.SECONDS);
        try {
            TrustManager[] trustManager = new TrustManager[]{
                    getSSLFactory()
            };
            // https信任
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustManager, new SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            //noinspection deprecation
            builder.sslSocketFactory(sslSocketFactory, getSSLFactory());
            builder.hostnameVerifier(hostname);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        Retrofit.Builder builder1 = new Retrofit.Builder()
//                .baseUrl("http://192.168.32.110/app/")
                .baseUrl("https://zxyun119.com/app/")
//                .baseUrl("http://k1.foslink.cn/app/")
//                .baseUrl("http://192.168.32.194/app/")
                // 配置监听请求
                .client(builder.build())
                // 请求接受工具（当前为RxJava2）
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        RetrofitFileDownloadService fileDownService = builder1.build().create(RetrofitFileDownloadService.class);
        Logger.e(TAG, "getDownloadService" + inputText);
        fileDownService.downloadFileUrl(inputText)
                .subscribe(new Observer<ResponseBody>() {

                    private File apk;
                    private long fileSize;

                    @Override
                    public void onSubscribe(Disposable d) {
                        Logger.e(TAG, "onSubscribe" + d.toString());
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        Logger.e(TAG, "onNext" + responseBody.toString());
                        writeResponseBodyToDisk(responseBody);
                    }

                    private void writeResponseBodyToDisk(ResponseBody body) {
                        // 改成自己需要的存储位置
                        apk = new File(FileUtils.getNewFileDir(FileDownloadActivity.this) + File.separator + "test.apk");
                        Logger.e(TAG, "writeResponseBodyToDisk() file=" + apk.getPath());
                        if (apk.exists()) {
                            apk.delete();
                        }
                        InputStream inputStream;
                        OutputStream outputStream = null;
                        byte[] fileReader = new byte[4096];

                        fileSize = body.contentLength();
                        long fileSizeDownloaded = 0;

                        inputStream = body.byteStream();
                        try {
                            outputStream = new FileOutputStream(apk);
                            while (true) {
                                int read = inputStream.read(fileReader);

                                if (read == -1) {
                                    break;
                                }

                                outputStream.write(fileReader, 0, read);

                                fileSizeDownloaded += read;
                                Logger.e(TAG, "download process=" + (int) (100 * fileSizeDownloaded / fileSize));
                                uploadProgress.setProgress((int) (100 * fileSizeDownloaded / fileSize));
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                if (inputStream != null) {
                                    inputStream.close();
                                }
                                if (outputStream != null) {
                                    outputStream.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(TAG, "onError" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Logger.e(TAG, "onComplete");
                        if (null != apk) {
                            if (apk.length() == fileSize && fileSize != 0) {
                                installApk(apk);
                            }
                        }
                    }
                });
    }

    private void installApk(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        } else {
            Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileProvider", file);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 证书验证
     */
    private HostnameVerifier hostname = new HostnameVerifier() {
        @SuppressLint("BadHostnameVerifier")
        @Override
        public boolean verify(String hostname, SSLSession session) {
            // 全部信任
            return true;
        }
    };

    private X509TrustManager getSSLFactory() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return x509TrustManager;
        } else {
            return x509ExtendedTrustManager;
        }
    }

    /**
     * 证书信任管理
     */
    private X509TrustManager x509TrustManager = new X509TrustManager() {

        @SuppressLint("TrustAllX509TrustManager")
        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {

        }

        @SuppressLint("TrustAllX509TrustManager")
        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {

        }

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[0];
        }
    };

    /**
     * 证书信任管理
     */
    @TargetApi(Build.VERSION_CODES.N)
    private X509ExtendedTrustManager x509ExtendedTrustManager = new X509ExtendedTrustManager() {

        @SuppressLint("TrustAllX509TrustManager")
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @SuppressLint("TrustAllX509TrustManager")
        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }

        @SuppressLint("TrustAllX509TrustManager")
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType, Socket socket) throws CertificateException {

        }

        @SuppressLint("TrustAllX509TrustManager")
        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType, Socket socket) throws CertificateException {

        }

        @SuppressLint("TrustAllX509TrustManager")
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType, SSLEngine engine) throws CertificateException {

        }

        @SuppressLint("TrustAllX509TrustManager")
        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType, SSLEngine engine) throws CertificateException {

        }
    };
}
