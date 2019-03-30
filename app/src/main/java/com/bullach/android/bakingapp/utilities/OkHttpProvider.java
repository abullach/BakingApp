package com.bullach.android.bakingapp.utilities;

import android.util.Log;

import com.bullach.android.bakingapp.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Creates OkHttpClient.
 * <br>
 * Reference: @see "https://medium.com/@wingoku/synchronization-between-retrofit-espresso-achieved-9540fe95d357"
 * "https://github.com/chiuki/espresso-samples/tree/master/idling-resource-okhttp"
 */
public abstract class OkHttpProvider {

    private static final String TAG = OkHttpProvider.class.getSimpleName();
    private static OkHttpClient sInstance = null;

    public static OkHttpClient getOkHttpInstance() {
        if (sInstance == null) {
            sInstance = new OkHttpClient();

            OkHttpClient.Builder okHttpClientBuilder = sInstance.newBuilder();
            okHttpClientBuilder.connectTimeout(Constants.CONNECT_TIMEOUT_SHORT, TimeUnit.SECONDS);
            okHttpClientBuilder.readTimeout(Constants.READ_TIMEOUT_LONG, TimeUnit.SECONDS);

            if (BuildConfig.DEBUG) {
                Log.d(TAG, "Debug build.");
                // See http://square.github.io/okhttp/3.x/logging-interceptor/ to see the options.
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                okHttpClientBuilder.addInterceptor(interceptor);
            }
        }
        return sInstance;
    }
}
