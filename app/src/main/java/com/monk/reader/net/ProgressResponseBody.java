package com.monk.reader.net;


import com.monk.reader.listener.ProgressResponseListener;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by heyao on 2017/7/28.
 */

public class ProgressResponseBody extends ResponseBody {
    private ResponseBody responseBody;
    private ProgressResponseListener listener;
    private BufferedSource bufferedSource;
    private long contentLength;

    public ProgressResponseBody(ResponseBody responseBody, ProgressResponseListener listener) {
        this.responseBody = responseBody;
        contentLength = responseBody.contentLength();
        this.listener = listener;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                listener.onResponseProgress(totalBytesRead, contentLength, bytesRead == -1);
                return bytesRead;
            }
        };
    }
}
