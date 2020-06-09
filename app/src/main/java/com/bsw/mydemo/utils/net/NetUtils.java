package com.bsw.mydemo.utils.net;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.StringDef;
import android.text.TextUtils;


import com.bsw.mydemo.BuildConfig;
import com.bsw.mydemo.R;
import com.bsw.mydemo.utils.Const;
import com.bsw.mydemo.utils.Logger;
import com.bsw.mydemo.utils.MediaFileJudgeUtils;
import com.bsw.mydemo.utils.StringFormatUtils;
import com.bsw.mydemo.utils.net.result.ResultBean;
import com.bsw.mydemo.widget.BswProgressDialog;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络请求工具基类
 *
 * @author 雷鸣
 * @date 2020-04-14
 */
public class NetUtils {
    /**
     * 开发环境Host
     */
    @SuppressWarnings("FieldCanBeLocal,unused")
    public static final String DEV_HOST = "http://192.168.32.133/";
    /**
     * 测试环境Host
     */
    @SuppressWarnings("FieldCanBeLocal,unused")
    public static final String TEST_HOST = "http://192.168.32.194/";
    /**
     * 生产环境的Host
     */
    @SuppressWarnings("FieldCanBeLocal,unused")
    public static final String RELEASE_HOST = "http://k1.foslink.cn/";

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({DEV_HOST, TEST_HOST, RELEASE_HOST})
    public @interface HostType {
    }

    /**
     * 用于缓存的单例工具类
     */
    private static NetUtils netUtils;

    /**
     * 上下文的弱引用，避免内存泄漏
     */
    private WeakReference<Context> weakContext;
    /**
     * 加载提示框
     */
    private BswProgressDialog bswProgressDialog;

    /**
     * header cookie
     */
    private InDiskCookieStore cookieStore;

    /***
     * OkHttp构造器
     */
    private OkHttpClient.Builder builder;
    /**
     * Retrofit构造器
     */
    private Retrofit.Builder builder1;
    /**
     * json解析工具
     */
    private final Gson gson;

    /*---------------------------------------------内部逻辑-----------------------------------------------------------*/

    /**
     * 单例方法
     *
     * @param mContext 上下文
     * @return 网络请求工具类
     */
    public static NetUtils getNewInstance(Context mContext) {

        // 添加上下文弱引用是否为空的判断，若为空，则重新创建单例NetUtils
        if (null == netUtils || null == netUtils.weakContext.get()) {
            synchronized (NetUtils.class) {
                if (null == netUtils || null == netUtils.weakContext.get()) {
                    if (null == mContext) {
                        /*
                         * 由于需要在ApiRequest重新请求，因此将mContext的判断放到单例获取判断里
                         * 当存在单例网络请求工具，且弱引用的上下文存在的时候，可刷新请求
                         */
                        return null;
                    } else {
                        netUtils = new NetUtils(mContext);
                    }
                }
            }
        }
        return netUtils;
    }

    private NetUtils(Context mContext) {
        weakContext = new WeakReference<>(mContext);
        cookieStore = InDiskCookieStore.newInstance(mContext);
        bswProgressDialog = new BswProgressDialog(mContext, R.style.progress_dialog_loading, R.string.all_loaded);
        gson = new Gson();
    }

    /**
     * 初始化数据
     *
     * @param action    当前请求的尾址
     * @param inputType 网络请求类型
     */
    private synchronized Retrofit initBaseData(final String action, int inputType) {
        // https信任管理
        TrustManager[] trustManager = new TrustManager[]{
                getSSLFactory()
        };

        if (builder == null) {
            builder = new OkHttpClient.Builder();
        }
        if (inputType != ApiRequest.API_TYPE_FILE_OPERATION) {
            // 请求超时
            builder.connectTimeout(20, TimeUnit.SECONDS);
            builder.readTimeout(20, TimeUnit.SECONDS);
        } else {
            builder.connectTimeout(60, TimeUnit.SECONDS);
            builder.readTimeout(60, TimeUnit.SECONDS);
        }
        // 请求参数获取
        builder.addInterceptor(interceptor);
        CookieManager cookieManager = new CookieManager(cookieStore, CookiePolicy.ACCEPT_ORIGINAL_SERVER);
        builder.cookieJar(new JavaNetCookieJar(cookieManager));
        try {
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

        // 构建Builder，请求结果RxJava接收，使用GSON转化为Bean，
        if (builder1 == null) {
            builder1 = new Retrofit.Builder()
                    .client(builder.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        }

        if (0 == inputType) {
            inputType = ApiRequest.API_TYPE_NORMAL;
        }

        String baseUrl = getUrl(weakContext.get(), inputType, action);
        Logger.i("NetUtils#initBaseData  baseUrl = " + baseUrl + " action = " + action);
        builder1.baseUrl(baseUrl);
        return builder1.build();
    }

    private X509TrustManager getSSLFactory() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return x509TrustManager;
        } else {
            return x509ExtendedTrustManager;
        }
    }

    public boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    /**
     * 获取host
     *
     * @return host
     */
    public String getHost() {
        String host;
        if (isDebug()) {
            host = DEV_HOST;
        } else {
            host = RELEASE_HOST;
        }
        return host;
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

    /**
     * OkHttp拦截器
     * <p>
     * 用于日志打印
     */
    private Interceptor interceptor = new Interceptor() {
        @Override
        public okhttp3.Response intercept(@NonNull Interceptor.Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();
            Logger.i(String.format("Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()));

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            Logger.i(String.format(Locale.getDefault(), "Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));

            try {
                Logger.i("request", "request header : ".concat(request.headers().toString())
                        .concat("\nrequest content : ".concat(request.toString()))
                        .concat("\nproceed header : ".concat(response.headers().toString())));
            } catch (Exception e) {
                e.printStackTrace();
            }

            return response;
        }
    };

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

    /*--------------------------------------------请求路径-----------------------------------------------*/

    /**
     * 获取请求Url
     *
     * @param context    上下文
     * @param actionType 请求类型
     * @param action     当前的请求，若是用于cookie持久化判断，则为空
     * @return 拼接后的url
     */
    private synchronized String getUrl(Context context, int actionType, String action) {
        return String.format("%s%s", getHost(), action);
    }

    /*----------------------------------------------请求------------------------------------------------------------*/

    /**
     * 网络请求
     *
     * @param netRequestCallBack 网络请求回调
     * @param showDialog         是否显示弹窗
     * @param apiRequests        请求类
     */
    public synchronized void request(NetRequestCallBack netRequestCallBack, final boolean showDialog, final ApiRequest... apiRequests) {
        if (null == weakContext.get()) {
            return;
        }
        if (Const.judgeListNull(apiRequests) == 0) {
            throw new IllegalArgumentException("apiRequests can't be null");
        }
        if (showDialog) {
            showLoadDialog();
        }
        tokenJudge(netRequestCallBack, showDialog, apiRequests);
    }

    /**
     * token存在性判断，若有token则直接请求，若没有token则先获取token
     *
     * @param netRequestCallBack 网络请求回调
     * @param showDialog         是否显示弹窗
     * @param apiRequests        请求类
     */
    private void tokenJudge(final NetRequestCallBack netRequestCallBack, final boolean showDialog, final ApiRequest... apiRequests) {
        Const.threadPoolExecute(new Runnable() {
            @Override
            public void run() {
                execute(netRequestCallBack, showDialog, apiRequests);
            }
        });
    }

    /**
     * 执行网络请求
     *
     * @param netRequestCallBack 网络请求回调
     * @param showDialog         是否显示弹窗
     * @param apiRequests        请求类
     */
    private synchronized void execute(final NetRequestCallBack netRequestCallBack, final boolean showDialog, final ApiRequest... apiRequests) {
        for (ApiRequest apiRequest : apiRequests) {
            apiRequest.setNetRequestCallBack(netRequestCallBack);
            Logger.i("RequestDebug: Net request execute :" + apiRequest.getAction());
            switch (apiRequest.getRequestType()) {
                case ApiRequest.REQUEST_TYPE_GET:
                    get(apiRequest, showDialog);
                    break;

                case ApiRequest.REQUEST_TYPE_POST:
                    if (apiRequest.getSpecialTreatment() == ApiRequest.SPECIAL_FILE_UPLOAD) {
                        postFile(apiRequest, showDialog);
                    } else {
                        post(apiRequest, showDialog);
                    }
                    break;

                default:
                    break;
            }
        }
    }


    /**
     * Get请求
     *
     * @param apiRequest {@link ApiRequest}
     * @param showDialog 是否显示加载弹窗
     */
    private synchronized <DataBean extends ResultBean> void get(ApiRequest<DataBean> apiRequest, boolean showDialog) {
        String action = apiRequest.getAction().getActionString();
        RetrofitService getService = initBaseData(action.substring(0, action.lastIndexOf("/") + 1), apiRequest.getApiType()).create(RetrofitService.class);
        Map<String, Object> params = apiRequest.getRequestParams();
        if (params == null) {
            params = new HashMap<>();
        }

        Logger.i("zzz", "request====" + new JSONObject(params));

        getService.getResult(action.substring(action.lastIndexOf("/") + 1), params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<>(apiRequest, showDialog));
    }

    /**
     * Post请求
     *
     * @param apiRequest 请求参数
     * @param showDialog 是否展示请求加载框
     */
    private synchronized <DataBean extends ResultBean> void post(ApiRequest<DataBean> apiRequest, boolean showDialog) {
        Map<String, Object> fieldMap = apiRequest.getRequestParams();
        boolean isFieldMapUseful = Const.judgeListNull(fieldMap) > 0;
        String requestString;
        NetActionEnum action = apiRequest.getAction();
        String actionString = action.getActionString();
        String useAction = actionString.substring(actionString.lastIndexOf("/") + 1);
        Object o = apiRequest.getRequestBody();
        if (Const.notEmpty(o)) {
            requestString = gson.toJson(o);
            if (isFieldMapUseful) {
                for (Map.Entry<String, Object> entry : fieldMap.entrySet()) {
                    useAction = useAction.concat(useAction.contains("?") ? "&" : "?").concat(entry.getKey()).concat("=").concat(entry.getValue().toString());
                }
            }
        } else {
            requestString = String.valueOf(new JSONObject(isFieldMapUseful ? fieldMap : new HashMap<String, Object>()));
        }

        Logger.i("request", "request====" + requestString);

        NetRequestCallBack netRequestCallBack = apiRequest.getNetRequestCallBack();
        if (TextUtils.isEmpty(requestString) && Const.notEmpty(netRequestCallBack)) {
            netRequestCallBack.error(action, new Exception(StringFormatUtils.getString(weakContext.get(), R.string.data_abnormal)), apiRequest.getTag());
        }

        RetrofitService jsonService = initBaseData(actionString.substring(0, actionString.lastIndexOf("/") + 1), apiRequest.getApiType()).create(RetrofitService.class);
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                        requestString);

        jsonService.postResult(useAction, requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<>(apiRequest, showDialog));
    }

    /**
     * 上传文件
     *
     * @param <DataBean> 上传数据类泛型
     * @param apiRequest 请求参数
     */
    private synchronized <DataBean extends ResultBean> void postFile(ApiRequest<DataBean> apiRequest, boolean showDialog) {
        try {
            Object files = apiRequest.getRequestBody();
            List<File> fileList = null;
            if (files instanceof List) {
                //noinspection unchecked
                fileList = (List<File>) files;
            } else if (files instanceof File) {
                fileList = new ArrayList<>();
                fileList.add((File) files);
            } else if (files instanceof String) {
                fileList = new ArrayList<>();
                fileList.add(new File((String) files));
            }
            if (Const.judgeListNull(fileList) == 0) {
                return;
            }
            //防止重置私有云后，不重新创建，导致异常
            String action = apiRequest.getAction().getActionString();
            RetrofitService fileService = initBaseData(action.substring(0, action.lastIndexOf("/") + 1), ApiRequest.API_TYPE_FILE_OPERATION).create(RetrofitService.class);

            // 若fileList为空，则Const.judgeListNull() == 0，因此能运行到这里肯定不是空
            assert fileList != null;
            MediaFileJudgeUtils.MediaFileType mediaFileType = MediaFileJudgeUtils.getFileType(fileList.get(0).getAbsolutePath());
            if (Const.isEmpty(mediaFileType)) {
                throw new IllegalArgumentException("File is wrong type");
            }
            List<MultipartBody.Part> partList = filesToMultipartBodyParts(fileList, mediaFileType.mimeType);

            fileService.fileResult(action.substring(action.lastIndexOf("/") + 1), partList)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new MyObserver<>(apiRequest, showDialog));
        } catch (ClassCastException e) {
            Logger.e(getClass().getSimpleName(), e);
        }
    }

    private List<MultipartBody.Part> filesToMultipartBodyParts(List<File> files, String fileType) {
        Logger.i(getClass().getSimpleName(), fileType);
        List<MultipartBody.Part> parts = new ArrayList<>(files.size());
        for (File file : files) {
            RequestBody requestBody = RequestBody.create(MediaType.parse(fileType), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            parts.add(part);
        }
        return parts;
    }

    public void removeCookies() {
        cookieStore.removeAll();
    }

    private class MyObserver<DataBean extends ResultBean> implements Observer<ResponseBody> {
        /**
         * 尾址
         */
        private NetActionEnum action;
        boolean showDialog;
        /**
         * 请求API
         */
        ApiRequest<DataBean> apiRequest;
        /**
         * 返回结果状态：0、正常Bean；1、Bitmap
         */
        private int resultStatus = 0;
        private Object tag;

        MyObserver(ApiRequest<DataBean> apiRequest, boolean showDialog) {
            this.apiRequest = apiRequest;
            this.action = apiRequest.getAction();
            this.tag = apiRequest.getTag();
            if (apiRequest.getSpecialTreatment() == ApiRequest.SPECIAL_GET_BITMAP) {
                this.resultStatus = 1;
            } else {
                this.resultStatus = 0;
            }
            this.showDialog = showDialog;
        }

        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }

        @SuppressWarnings("unchecked")
        @Override
        public void onNext(@NonNull ResponseBody responseBody) {
            if (showDialog) {
                hideLoadDialog();
            }
            NetRequestCallBack netRequestCallBack = apiRequest.getNetRequestCallBack();
            try {

                switch (resultStatus) {
                    case 0:
                        String responseString = responseBody.string();
                        Logger.i("responseString ：", action + "********** responseString get  " + responseString);
                        if (Const.notEmpty(netRequestCallBack)) {
                            Logger.i("responseString ：", "netRequestCallBack can be use : " + responseString);
                            ResultBean resultBean = gson.fromJson(responseString, null == apiRequest.getClz() ? ResultBean.class : apiRequest.getClz());
                            if (resultBean.isSuccessful()) {
                                netRequestCallBack.success(action, resultBean, tag);
                            } else {
                                netRequestCallBack.failure(action, resultBean, tag);
                            }
                        } else {
                            Logger.i("responseString ：", "netRequestCallBack is null!!!!!");
                        }
                        break;

                    case 1:
                        if (Const.notEmpty(netRequestCallBack)) {
                            netRequestCallBack.success(action, BitmapFactory.decodeStream(responseBody.byteStream()), tag);
                        }
                        Logger.i("responseString ：", action + "********** 图片获取成功 ");
                        break;

                    default:
                        break;
                }
            } catch (Exception e) {
                Logger.e(getClass().getSimpleName(), e);
                Logger.i("responseString ：", action + "********** 请求异常 ");
                netRequestCallBack.error(action, e, tag);
            }
        }

        @Override
        public void onError(@NonNull Throwable e) {
            try {
                if (showDialog) {
                    hideLoadDialog();
                }

                if (e instanceof HttpException) {
                    ResponseBody errorbody = ((HttpException) e).response().errorBody();
                    if (Const.notEmpty(errorbody)) {
                        Logger.i("responseString ：", String.format("%s********** responseString get error %s content %s", action, e.toString(), TextUtils.isEmpty(errorbody.string()) ? "" : errorbody));
                    }
                } else {
                    Logger.i("responseString ：", String.format("%s********** responseString get error %s", action, e.toString()));
                }
                NetRequestCallBack netRequestCallBack = apiRequest.getNetRequestCallBack();
                if (Const.notEmpty(netRequestCallBack)) {
                    netRequestCallBack.error(action, e, tag);
                }
                if (e.toString().contains("401")) {
                    removeCookies();
                } else {
                }
            } catch (IOException | NullPointerException e1) {
                e1.printStackTrace();
            }
        }

        @Override
        public void onComplete() {
            if (showDialog) {
                hideLoadDialog();
            }
        }
    }

    /**
     * 隐藏加载提示框
     */
    public synchronized void hideLoadDialog() {
        try {
            ((Activity) weakContext.get()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (bswProgressDialog != null && bswProgressDialog.isShowing()) {
                        bswProgressDialog.dismiss();
                    }
                }
            });
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示加载提示框
     */
    public synchronized void showLoadDialog() {
        try {
            Activity activity = (Activity) weakContext.get();
            if (null != activity) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (bswProgressDialog != null && !bswProgressDialog.isShowing()) {
                                bswProgressDialog.show();
                            }
                        } catch (Exception ex) {
                            StringWriter stackTrace = new StringWriter();
                            ex.printStackTrace(new PrintWriter(stackTrace));
                            Logger.i("Exception", stackTrace.toString());
                        }
                    }
                });
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public boolean hasToken(String baseUrl) {
        return cookieStore.hasCookies(baseUrl);
    }

    public boolean hasToken() {
        return cookieStore.hasCookies(cookieStore.getHost(getUrl(weakContext.get(), ApiRequest.API_TYPE_NORMAL, "")));
    }

    /**
     * 网络请求文本结果回调接口
     */
    @SuppressWarnings("AlibabaAbstractClassShouldStartWithAbstractNaming")
    public abstract static class NetRequestCallBack<DataBean extends ResultBean> {

        /**
         * 请求成功 code为000000
         *
         * @param action 尾址
         * @param bitmap 图片
         * @param tag    标识
         */
        public void success(NetActionEnum action, Bitmap bitmap, Object tag) {

        }

        /**
         * 请求成功 code为000000
         *
         * @param action 尾址
         * @param file   文件
         * @param tag    标识
         */
        public void success(NetActionEnum action, File file, Object tag) {

        }

        /**
         * 请求成功 code为000000
         *
         * @param action     尾址
         * @param resultBean 请求结果
         * @param tag        标识
         */
        public abstract void success(NetActionEnum action, DataBean resultBean, Object tag);

        /**
         * 请求失败 code不为000000
         *
         * @param action     尾址
         * @param resultBean 请求结果
         * @param tag        标识
         */
        public abstract void failure(NetActionEnum action, DataBean resultBean, Object tag);

        /**
         * 访问失败回调抽象方法
         *
         * @param action 网络访问尾址
         * @param e      所返回的异常
         * @param tag    当接口复用时，用于区分请求的表识
         */
        public abstract void error(NetActionEnum action, Throwable e, @Nullable Object tag);
    }
}