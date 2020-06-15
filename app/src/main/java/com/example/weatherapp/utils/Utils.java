package com.example.weatherapp.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.weatherapp.entities.City;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class Utils {
    private static final String TAG = "Utils";

    public static List<City> getJsonFromAssets(Context context, String fileName) {

        try {
            InputStream inputStream = context.getAssets().open(fileName);
            //creating an InputStreamReader object
            InputStreamReader isReader = new InputStreamReader(inputStream);
            //Creating a BufferedReader object
            BufferedReader reader = new BufferedReader(isReader);
            StringBuffer sb = new StringBuffer();
            String str;
            while ((str = reader.readLine()) != null) {
                sb.append(str);
            }
            Log.d(TAG, "json read");
            Gson gson = new Gson();
            Type listUserType = new TypeToken<List<City>>() {
            }.getType();
            List<City> cities = gson.fromJson(sb.toString(), listUserType);
            Collections.sort(cities, new CitiesComparator());
            return cities;
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }

}
