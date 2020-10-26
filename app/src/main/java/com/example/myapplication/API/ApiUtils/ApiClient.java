package com.example.myapplication.API.ApiUtils;

import android.content.Context;
import android.util.Log;
import com.example.myapplication.Utils.AuthenticationManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient{
    private Retrofit.Builder retrofitBuilder;
    private static final String BASE_URL = "https://sinnerman-real.uc.r.appspot.com/";
    private Retrofit retrofit;
    private static ApiClient apiClient;
    private Context context;


    private ApiClient(){
        init();
    }

    private ApiClient(Context context){
        this.context = context.getApplicationContext();
        init();
    }

    public static ApiClient getInstance(Context ctx){
        if(apiClient == null){
            apiClient = new ApiClient(ctx);
        }
        return apiClient;
    }

    public void init(){
        Gson gson = new GsonBuilder().setLenient().create();

        OkHttpClient.Builder httpclient = new OkHttpClient.Builder();
        httpclient.addInterceptor(chain -> {
            Request originalRequest = chain.request();
            Request.Builder builder = originalRequest.newBuilder().header("x-api-key", "123456789").header("Content-Type", "application/json");

            String token = new AuthenticationManager().checkToken(context);
            builder.header("token", token);

            return chain.proceed(builder.build());
        });

        retrofitBuilder = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson)).client(httpclient.build());

        retrofit = retrofitBuilder.build();
    }

    public <T> T getClient(Class<T> t){
        return retrofit.create(t);
    }

}
