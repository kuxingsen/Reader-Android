package com.monk.reader.retrofit2;

import com.monk.reader.retrofit2.bean.Range;
import com.monk.reader.retrofit2.bean.Result;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface RangeApi {
    @GET("range/list")
    Observable<Result<Range>> getRangeList();
}
