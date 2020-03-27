package com.monk.reader.retrofit2;


import com.monk.reader.retrofit2.bean.Category;
import com.monk.reader.retrofit2.bean.Result;

import java.util.HashMap;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CategoryApi {
    @GET("category/{id}")
    Observable<Result<Category>> getSubCategoryById(@Path("id") long id);

}
