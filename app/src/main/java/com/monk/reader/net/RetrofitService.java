package com.monk.reader.net;


import com.monk.reader.dao.model.BookCatalog;
import com.monk.reader.dao.model.ChannelBook;
import com.monk.reader.dao.model.CullingBook;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by heyao on 2017/7/18.
 */

public interface RetrofitService {

    @Streaming
    @GET
    Call<ResponseBody> downloadAPK(@Url String url);

    @GET("listchlArt/{category}-{channel}-{page}")
    Call<List<ChannelBook>> getChannelBook(@Path("category") int categoryCode, @Path("channel") String channel, @Path("page") int page);

    @GET("")
    Call<List<CullingBook>> getCullingBook(String channel, int page);

    @GET("listart/{bookId}-{page}")
    Call<BookCatalog> getChapterList(@Path("bookId") String bookId, @Path("page") int page);

    @Streaming
    @GET
    Call<ResponseBody> download(@Url String url, @Header("Range") String range);
}
