package com.example.weatherapp.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OpenWeatherApiService {
    private  static OpenWeatherApiService apiService;
    private static String BASE_URL = "https://api.openweathermap.org/data/2.5/";

    private OpenWeatherApiService(){}

    public static OpenWeatherApiService getInstance(){
        if(apiService == null){
            apiService = new OpenWeatherApiService();
        }
        return apiService;
    }

    public WeatherClient getRetrofitService()  {

        Gson gson = new GsonBuilder().create();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder clientBuilder =
                new OkHttpClient.Builder()
                        .addInterceptor(interceptor)
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(10, TimeUnit.SECONDS);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(clientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(WeatherClient.class);
    }

}
