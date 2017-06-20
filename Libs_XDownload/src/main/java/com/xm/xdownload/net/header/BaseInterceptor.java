package com.xm.xdownload.net.header;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author: 小民
 * @date: 2017-06-20
 * @time: 16:14
 * @说明: 新增头部多请求 -> 当然 文件下载是不会有的
 */
public class BaseInterceptor implements Interceptor {
    private NetRequestParamsListener mNetRequestParamsListener;

    public BaseInterceptor(NetRequestParamsListener netRequestParamsListener) {
        mNetRequestParamsListener = netRequestParamsListener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        HashMap<String, String> headers = mNetRequestParamsListener.getHeaderParams();
        //如果有就加，没有就不加
        if (headers != null && headers.size() > 0){
            Set<String> keys = headers.keySet();
            for (String headerKey : keys) {
                builder.addHeader(headerKey,   headers.get(headerKey)).build();
            }
        }
        return chain.proceed(builder.build());

    }
}