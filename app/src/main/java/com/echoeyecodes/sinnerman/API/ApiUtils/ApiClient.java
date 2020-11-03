package com.echoeyecodes.sinnerman.API.ApiUtils;

import android.content.Context;
import com.echoeyecodes.sinnerman.BuildConfig;
import com.echoeyecodes.sinnerman.Utils.AuthenticationManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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

            String token = new AuthenticationManager().checkToken(context);
            Request request = originalRequest.newBuilder().header("x-api-key", BuildConfig.SINNERMAN_SERVER_API_KEY).header("Content-Type", "application/json").header("token", token).build();

            Response response = chain.proceed(request);
            if(response.code() == 401){
                AuthenticationManager authenticationManager = new AuthenticationManager();
                authenticationManager.startAuthActivity(context.getApplicationContext());
                response.close();
                return response;
            }
            return response;
        });

        retrofitBuilder = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson)).client(httpclient.build());

        retrofit = retrofitBuilder.build();
    }

    public <T> T getClient(Class<T> t){
        return retrofit.create(t);
    }

}
