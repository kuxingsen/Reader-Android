package com.monk.reader.retrofit2;

import com.monk.reader.dao.bean.BookCatalogue;
import com.monk.reader.dao.bean.Cache;
import com.monk.reader.retrofit2.bean.DataCache;
import com.monk.reader.retrofit2.bean.Result;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface BookCacheApi {

    @GET("cache/book/{id}")
    Observable<Result<DataCache>> getCacheByBookId(@Path("id") long id);

    @GET("cache/book/{bookId}/index/{index}")
    Observable<Result<DataCache>> getBookCache(@Path("bookId") long bookId, @Path("index") int index);

}
