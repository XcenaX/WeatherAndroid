package com.example.weatherapp.services;

import com.example.weatherapp.entities.Weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface WeatherClient {
    @GET
    Call<Weather> getWeather(@Url String url);
}
