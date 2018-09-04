package com.kloosin.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.kloosin.utility.u.CommonHelper;
import com.kloosin.utility.u.SPHelper;
import com.kloosin.utility.u.TagNameHelper;
import com.kloosin.utility.u.URLHelper;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class KLRestService {

    private static final String LOG_TAG = KLRestService.class.getSimpleName();

    private static KLRestService mInstance;
    private static Retrofit _retrofit = null;
    private static Retrofit _retrofitWithAccessToken = null;
    public static KLWebServiceAPI _service = null;

    public static KLRestService getInstance() {
        if ( null == mInstance ) mInstance = new KLRestService();
        return (mInstance);
    }

    public KLWebServiceAPI getService(final Context context) {
        if (SPHelper.getInstance(context).readBoolean(TagNameHelper.getInstance().NEED_TO_REBOOT_RETROFIT_SERVICE_KEY))  {
            _retrofit = null;
            _service = null;
            SPHelper.getInstance(context).writeBoolean(TagNameHelper.getInstance().NEED_TO_REBOOT_RETROFIT_SERVICE_KEY, false);
        }
        if (null == _retrofit) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            // seems after login each call should contain auth token
            if ( null != CommonHelper.getInstance().getCurrentUser(context)) {
                httpClient.addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request newRequest = chain.request().newBuilder()
                                .addHeader("authorization", String.format("Bearer %s", CommonHelper.getInstance().getCurrentUser(context).getAccessToken().trim()))
                                .build();
                        return chain.proceed(newRequest);
                    }
                });
            }
            httpClient.addInterceptor(loggingInterceptor);
            httpClient.readTimeout(30, TimeUnit.SECONDS);
            httpClient.connectTimeout(30, TimeUnit.SECONDS);
            _retrofit = new Retrofit.Builder()
                    .baseUrl(URLHelper.getInstance().HOST_NAME)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        if (_retrofit != null && null == _service) {
            _service = _retrofit.create(KLWebServiceAPI.class);
        }
        return _service;
    }

    public boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } catch (Exception e) {
            return false;
        }
    }
}
