package com.bsw.mydemo.service;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * 大文件下载Service
 *
 * @author leiming
 * @date 2020-6-3
 */
public interface RetrofitFileDownloadService {
    /**
     * 下载文件用
     * “@Streaming” 是大文件的接收流
     *
     * @param action 文件路径
     * @return 回调
     */
    @Streaming
    @GET("{action}")
    Observable<ResponseBody> downloadFileUrl(@Path("action") String action);
}
