package com.bsw.mydemo.service;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * @author 半寿翁
 */
public interface RetrofitPostService {

    @FormUrlEncoded
    @POST("{action}")
    Observable<ResponseBody> postResult(@Path("action") String action, @FieldMap Map<String, String> params);
}
