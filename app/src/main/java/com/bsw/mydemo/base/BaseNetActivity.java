package com.bsw.mydemo.base;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;

import com.bsw.mydemo.R;
import com.bsw.mydemo.utils.Const;
import com.bsw.mydemo.utils.Logger;
import com.bsw.mydemo.utils.NetUtils;
import com.bsw.mydemo.widget.CustomProgressDialog;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

/**
 * @author 半寿翁
 */
public abstract class BaseNetActivity extends BaseActivity {

    /**
     * 加载提示框
     */
    private CustomProgressDialog customProgressDialog;

    private NetUtils netUtils;

    public Map<String, Object> params;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customProgressDialog = new CustomProgressDialog(activity, R.style.progress_dialog_loading, "玩命加载中。。。");
        netUtils = new NetUtils();
        netUtils.initHeader();
    }

    public void refreshHeader() {
        netUtils.initHeader();
    }

    /**
     * Get请求
     *
     * @param action     请求接口的尾址
     * @param clazz      要转换的Bean类型（需继承BaseBean）
     * @param showDialog 显示加载进度条
     */
    public <T extends BaseBean> void get(final String action, Class<T> clazz, boolean showDialog) {
        if (!isNetworkAvailable()) {
            toast("网络异常，请检查网络是否连接");
            error(action, new Exception("网络异常，请检查网络是否连接"));
            return;
        }
        if (showDialog) {
            showLoadDialog();
        }
        if (params == null) {
            params = new HashMap<>();
        }
        netUtils.get(action, params, new MyObserver<>(action, clazz));
        params = null;
    }

    /**
     * Get请求
     *
     * @param action     请求接口的尾址
     * @param showDialog 显示加载进度条
     */
    public <T extends BaseBean> void getImage(final String action, boolean showDialog) {
        if (!isNetworkAvailable()) {
            toast("网络异常，请检查网络是否连接");
            error(action, new Exception("网络异常，请检查网络是否连接"));
            return;
        }
        if (showDialog) {
            showLoadDialog();
        }
        if (params == null) {
            params = new HashMap<>();
        }
        netUtils.get(action, params, new MyObserver<>(action, 1));
        params = null;
    }

    /**
     * Get请求
     *
     * @param action     请求接口的尾址
     * @param showDialog 显示加载进度条
     */
    public <T extends BaseBean> void fileDownload(final String action, boolean showDialog) {
        if (!isNetworkAvailable()) {
            toast("网络异常，请检查网络是否连接");
            error(action, new Exception("网络异常，请检查网络是否连接"));
            return;
        }
        if (showDialog) {
            showLoadDialog();
        }
        if (params == null) {
            params = new HashMap<>();
        }
        netUtils.get(action, params, new MyObserver<>(action, 1));
        params = null;
    }

    /**
     * Post请求
     *
     * @param action     请求接口的尾址
     * @param showDialog 显示加载进度条
     */
    public <T extends BaseBean> void getFile(final String action, boolean showDialog) {
        if (!isNetworkAvailable()) {
            toast("网络异常，请检查网络是否连接");
            error(action, new Exception("网络异常，请检查网络是否连接"));
            return;
        }
        if (showDialog) {
            showLoadDialog();
        }
        if (params == null) {
            params = new HashMap<>();
        }
        netUtils.get(action, params, new MyObserver<>(action, 2));
        params = null;
    }

    /**
     * Post请求
     *
     * @param action     请求接口的尾址
     * @param clazz      要转换的Bean类型（需继承BaseBean）
     * @param showDialog 显示加载进度条
     */
    public <T extends BaseBean> void post(final String action, String json, final Class<T> clazz, boolean showDialog) {
        if (!isNetworkAvailable()) {
            toast("网络异常，请检查网络是否连接");
            error(action, new Exception("网络异常，请检查网络是否连接"));
            return;
        }
        if (showDialog) {
            showLoadDialog();
        }
        netUtils.post(action, json, new MyObserver<>(action, clazz));
    }

    /**
     * 访问成功回调抽象方法
     *
     * @param action   网络访问尾址
     * @param baseBean 返回的数据Bean
     */
    public abstract void success(String action, BaseBean baseBean);

    /**
     * 访问成功回调方法
     *
     * @param action 网络访问尾址
     * @param bitmap 获取的Bitmap
     */
    public void success(String action, Bitmap bitmap) {
    }

    public abstract void error(String action, Throwable e);

    /**
     * 显示加载提示框
     */
    private void showLoadDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                customProgressDialog.show();
            }
        });
    }

    /**
     * 隐藏加载提示框
     */
    private void hideLoadDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (customProgressDialog != null && customProgressDialog.isShowing()) {
                    customProgressDialog.dismiss();
                }
            }
        });
    }

    private class MyObserver<T extends BaseBean> implements Observer<ResponseBody> {

        private Class<T> clazz;
        private String action;
        /**
         * 返回结果状态：0、正常Bean；1、Bitmap
         */
        private int resultStatus = 0;
        private String errorbody;

        MyObserver(String action, Class<T> clazz) {
            this.clazz = clazz;
            this.action = action;
        }

        MyObserver(String action, int resultStatus) {
            this.action = action;
            this.resultStatus = resultStatus;
        }

        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }

        @Override
        public void onNext(@NonNull ResponseBody responseBody) {
            hideLoadDialog();
            try {
                switch (resultStatus) {
                    case 0:
                        String responseString = responseBody.string();
                        Logger.i("responseString", action + "********** responseString get  " + responseString);
                        success(action, new Gson().fromJson(responseString, clazz));
                        break;

                    case 1:
                        success(action, BitmapFactory.decodeStream(responseBody.byteStream()));
                        Logger.i("responseString", action + "********** 图片获取成功 ");
                        break;

                    case 2:
                        InputStream is = null;
                        byte[] buf = new byte[2048];
                        int len = 0;
                        FileOutputStream fos = null;
                        // 储存下载文件的目录
                        String savePath = isExistDir("hzyzx.apk");
                        try {
                            is = responseBody.byteStream();
                            long total = responseBody.contentLength();
                            File file = new File(savePath, getNameFromUrl(action));
                            fos = new FileOutputStream(file);
                            long sum = 0;
                            while ((len = is.read(buf)) != -1) {
                                fos.write(buf, 0, len);
                                sum += len;
                                int progress = (int) (sum * 1.0f / total * 100);
                                // 下载中
                                Logger.i(getName(), "下载进度： progress = " + progress);
                            }
                            fos.flush();
                            Logger.i(getName(), "下载进度完成");
                        } catch (Exception e) {
                        }
                        break;

                    default:
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(@NonNull Throwable e) {
            try {
                if (e instanceof HttpException) {
                    errorbody = ((HttpException) e).response().errorBody().string();
                    Logger.i("responseString", String.format("%s********** responseString get error %s content %s", action, e.toString(), TextUtils.isEmpty(errorbody) ? "" : errorbody));
                } else {
                    Logger.i("responseString", String.format("%s********** responseString get error %s", action, e.toString()));
                }
            } catch (IOException | NullPointerException e1) {
                e1.printStackTrace();
            }
            error(action, e);
        }

        @Override
        public void onComplete() {
            params = null;
        }
    }

    /**
     * @param url
     * @return 从下载连接中解析出文件名
     */
    @NonNull
    private String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    /**
     * @param saveDir
     * @return
     * @throws IOException 判断下载目录是否存在
     */
    private String isExistDir(String saveDir) throws IOException {
        // 下载位置
        File downloadFile = new File(Environment.getExternalStorageDirectory(), saveDir);
        if (!downloadFile.mkdirs()) {
            downloadFile.createNewFile();
        }
        String savePath = downloadFile.getAbsolutePath();
        return savePath;
    }
}