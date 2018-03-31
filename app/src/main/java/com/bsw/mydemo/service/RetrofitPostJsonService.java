package com.bsw.mydemo.service;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
/**
 * @author 半寿翁
 */
public interface RetrofitPostJsonService {

    @POST("{action}")
    Observable<ResponseBody> postResult(@Path("action") String action, @Body RequestBody requestBody);
}
