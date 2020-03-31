package com.monk.reader.retrofit2;

import com.monk.reader.retrofit2.bean.Range;
import com.monk.reader.retrofit2.bean.Result;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RangeApi {
    @GET("range/category/{id}")
    Observable<Result<Range>> getRangeList(@Path("id") Long categoryId);
}
