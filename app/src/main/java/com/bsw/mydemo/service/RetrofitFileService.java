package com.bsw.mydemo.service;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * @author leiming
 * @date 2017/10/11
 */
public interface RetrofitFileService {
    @Multipart
    @POST("{action}")//不传递参数的Service
    Observable<ResponseBody> fileResult(@Path("action") String action, @HeaderMap Map<String, String> headerParams, @Part List<MultipartBody.Part> parts);
}

