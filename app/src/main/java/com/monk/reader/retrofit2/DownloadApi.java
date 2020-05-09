package com.monk.reader.retrofit2;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;


public interface DownloadApi {

    @Streaming
    @Headers({"Accept-Encoding: identity"})
    @GET
    Call<ResponseBody> download(@Url String url, @Query("range") Long range);
}
