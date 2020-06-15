package com.example.weatherapp.utils;

import com.example.weatherapp.entities.City;

import java.util.Comparator;

public class CitiesComparator implements Comparator<City> {
    @Override
    public int compare(City o1, City o2) {
        return o1.getName().compareTo(o2.getName());
    }
}
