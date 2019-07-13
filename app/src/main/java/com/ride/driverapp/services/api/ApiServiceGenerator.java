package com.ride.driverapp.services.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ride.driverapp.R;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class ApiServiceGenerator {

    private static final String BASE_URL = "https://europe-west1-beta-94f76.cloudfunctions.net/api/";



    private static Gson gson = new GsonBuilder()
            .setLenient()
            .create();


    //for GSON customization see https://futurestud.io/tutorials/retrofit-2-adding-customizing-the-gson-converter
    private static Retrofit.Builder builder
            = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson));

    private static Retrofit retrofit = builder.build();

    private static HttpLoggingInterceptor logging =
            new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY);


    private static OkHttpClient.Builder httpClient
            = new OkHttpClient.Builder();
            //.connectTimeout(30, TimeUnit.SECONDS)
            //.writeTimeout(30, TimeUnit.SECONDS)
            //.readTimeout(30, TimeUnit.SECONDS)
            //.build();


    public static <S> S createService(Class<S> serviceClass, Context ctx) {

        //TODO: no need for context https://stackoverflow.com/questions/13558550/can-i-get-data-from-shared-preferences-inside-a-service

        SharedPreferences sharedPreferences = ctx.getSharedPreferences( ctx.getString(R.string.pref_file) , MODE_PRIVATE);

        String versionCode = String.valueOf(  sharedPreferences.getInt("PackageCode", 0)  );
        String versionName = sharedPreferences.getString("PackageName", "");
        String applicationId = sharedPreferences.getString("ApplicationId", "");
        String authToken = sharedPreferences.getString("AuthToken", "");
        Log.w("ok", versionCode + " " + versionName + " " + applicationId + " " + authToken);

                httpClient.retryOnConnectionFailure(false); // otherwise on slow connections it creates multiple similar
                httpClient.cache(null);
                httpClient.interceptors().clear();
                httpClient.addInterceptor( chain -> {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .addHeader("authorization" , authToken)
                            .addHeader("version_code"  , versionCode)
                            .addHeader("version_name"  , versionName)
                            .addHeader("application_id", applicationId)
                            .build();
                    return chain.proceed(request);
                });

                    builder.client(httpClient.build());
                    retrofit = builder.build();


        return retrofit.create(serviceClass);
    }




}
