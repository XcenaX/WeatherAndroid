package com.example.weatherapp.entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Weather {
    @SerializedName("weather")
    ArrayList<WeatherInfo> weather;
    @SerializedName("main")
    Main main;
    @SerializedName("wind")
    Wind wind;
    @SerializedName("name")
    private String name;

    public Weather() {
        this.weather = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<WeatherInfo> getWeather() {
        return weather;
    }

    public void setWeather(ArrayList<WeatherInfo> weather) {
        this.weather = weather;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }
}
