package com.monk.reader.retrofit2;

import com.monk.reader.retrofit2.bean.Book;
import com.monk.reader.retrofit2.bean.Result;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BookApi {
    @GET("book/{id}")
    Observable<Result<Book>> getBook(@Path("id") long id);

    @GET("book/list/new")
    Observable<Result<Book>> getNewBookList(@Query("count") int count);

    @GET("book/list/range/{id}")
    Observable<Result<Book>> getRangeBookList(@Path("id")long id,@Query("duration") String duration);

    @GET("book/list/search")
    Observable<Result<Book>> getSelectedBookList(@Query("page") int page,
                                                 @Query("limit") int limit,
                                                 @Query("search") String search,//书名搜索
                                                 @Query("categoryId") long categoryId,
                                                 @Query("length") int length,//字数，单位万字
                                                 @Query("order") String order//排序，star、up_date、download、access
    );

    @GET("book/list/range/{id}/week")
    Observable<Result<Book>> getRangeWeekBookList(@Path("id")long id);

    @GET("book/list/range/{id}/month")
    Observable<Result<Book>> getRangeMonthBookList(@Path("id")long id);

    @GET("book/list/range/{id}/total")
    Observable<Result<Book>> getRangeTotalBookList(@Path("id")long id);
}
