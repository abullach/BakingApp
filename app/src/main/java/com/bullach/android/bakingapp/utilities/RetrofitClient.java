package com.bullach.android.bakingapp.utilities;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *  Create a singleton of Retrofit.
 */
public class RetrofitClient {

    /** Static variable for Retrofit */
    private static Retrofit sRetrofit = null;

    public static Retrofit getClient() {
        if (sRetrofit == null) {

            // Create the Retrofit instance using the builder
            sRetrofit = new Retrofit.Builder()
                    .client(OkHttpProvider.getOkHttpInstance())
                    // Set the base URL
                    .baseUrl(Constants.BASE_URL)
                    // Use GsonConverterFactory class to generate an implementation of the recipes interface, which uses Gson for its deserialization.
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return sRetrofit;
    }
}
