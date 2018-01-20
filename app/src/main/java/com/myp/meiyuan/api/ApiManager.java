package com.myp.meiyuan.api;

import android.util.Log;

import com.myp.meiyuan.util.GZipUtils;
import com.myp.meiyuan.util.LogUtils;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * 作者 by wuliang 时间 16/11/24.
 * <p>
 * 所有的请求控制
 */

public class ApiManager {

    private static final Charset UTF8 = Charset.forName("UTF-8");

    private static final String TAG = "ApiManager";
    private Retrofit mRetrofit;
    private static final int DEFAULT_TIMEOUT = 5;
    OkHttpClient.Builder builder;

    OtherHttpService service;

    /**
     * 初始化请求体
     */
    private ApiManager() {
        //手动创建一个OkHttpClient并设置超时时间
        builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.i(TAG, "log: " + message);
            }
        });
        loggingInterceptor.setLevel(level);
        builder.addInterceptor(loggingInterceptor);
        builder.addNetworkInterceptor(mNetInterceptor);  //添加网络拦截器
    }

    private static class SingletonHolder {
        private static final ApiManager INSTANCE = new ApiManager();
    }

    //获取单例
    public static ApiManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 获取请求代理
     *
     * @param service
     * @param url
     * @param <T>
     * @return
     */
    public <T> T configRetrofit(Class<T> service, String url) {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return mRetrofit.create(service);
    }


    /**
     * 自定义网络拦截器，如果头部信息中有通知数据已压缩，则解压过滤
     */
    Interceptor mNetInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Log.e("TAG", "拦截 应用 缓存");
            Request request = chain.request();
            Response response = chain.proceed(request);
            Headers header = response.headers();
            if ("gzip".equals(header.get("Content-Encoding"))) {   //解压数据
                if (HttpHeaders.hasBody(response)) {
                    ResponseBody responseBody = response.body();
                    long contentLength = responseBody.contentLength();
                    BufferedSource source = responseBody.source();
                    source.request(Long.MAX_VALUE); // Buffer the entire body.
                    Buffer buffer = source.buffer();
                    Charset charset = UTF8;
                    MediaType contentType = responseBody.contentType();
                    if (contentType != null) {
                        try {
                            charset = contentType.charset(UTF8);
                        } catch (UnsupportedCharsetException e) {
                            //Couldn't decode the response body; charset is likely malformed.
                            return response;
                        }
                    }
                    if (!isPlaintext(buffer)) {
                        LogUtils.I("<-- END HTTP (binary " + buffer.size() + "-byte body omitted)");
                        return response;
                    }
                    if (contentLength != 0) {
                        String result = buffer.clone().readString(charset);
                        //获取到response的body的string字符串
                        //do something .... <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                        String message = GZipUtils.unCompress(result);   //解压数据
                        LogUtils.E(message);
                        response.newBuilder().message(message);
                    }
                    LogUtils.I("<-- END HTTP (" + buffer.size() + "-byte body)");
                }
            }
            return response;
        }
    };


    private boolean isPlaintext(Buffer buffer) throws EOFException {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }


    /**
     * 获取rxjava实例对象，进行网络请求
     */

//    public Observable<List<HYClassify>> getHYClassify(){
//        if (mHuYaService==null) mHuYaService=configRetrofit(HuYaService.class,HuYaService.URL);
//        return mHuYaService.getClassify()
//                .compose(RxResultHelper.<List<HYClassify>>huyaResult());
//    }


    /**
     * 获取微信数据
     *
     * @param appid
     * @param sercret
     * @param code
     * @param grant_type
     * @return
     */
    public Observable getWxMesage(String appid, String sercret, String code, String grant_type) {
        if (service == null)
            service = configRetrofit(OtherHttpService.class, OtherHttpService.WXurL);
        return service.getWxMessage(appid, sercret, code, grant_type);
    }

    /**
     * 获取微信用户数据
     */
    public Observable getUserMessage(String access_token, String openid) {
        if (service == null)
            service = configRetrofit(OtherHttpService.class, OtherHttpService.WXurL);
        return service.getUserMessage(access_token, openid);
    }


}
