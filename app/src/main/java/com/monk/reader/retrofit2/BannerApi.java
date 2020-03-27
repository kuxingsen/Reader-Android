package com.monk.reader.retrofit2;


import com.monk.reader.retrofit2.bean.Banner;
import com.monk.reader.retrofit2.bean.Category;
import com.monk.reader.retrofit2.bean.Result;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface BannerApi {
    @GET("banner/category/{id}")
    Observable<Result<Banner>> getBannerByCategoryId(@Path("id") long id);

}
