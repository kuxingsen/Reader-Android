package com.monk.reader.retrofit2;

import com.monk.reader.dao.bean.BookCatalogue;
import com.monk.reader.retrofit2.bean.Result;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface BookCatalogueApi {

    @GET("catalogue/book/{id}")
    Observable<Result<BookCatalogue>> getCatalogueByBookId(@Path("id") long id);
}
