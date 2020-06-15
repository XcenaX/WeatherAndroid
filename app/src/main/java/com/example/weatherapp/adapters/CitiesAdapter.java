package com.example.weatherapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.R;
import com.example.weatherapp.entities.City;

import java.util.ArrayList;
import java.util.List;

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.MyViewHolder> implements Filterable {
    private List<City> cities = null;
    private List<City> filteredCities = null;

    //take uiElement for set data
    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView countryTxtView;
        TextView nameTxtView;

        MyViewHolder(@NonNull View view) {
            super(view);
            countryTxtView = view.findViewById(R.id.weather_list_country);
            nameTxtView = view.findViewById(R.id.weather_list_name);
        }
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
        this.filteredCities = cities;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //get row layout
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weather_list_layout, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    //set item data to ui elements of itemView
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        City city = filteredCities.get(position);
        holder.countryTxtView.setText(city.getName());
        holder.nameTxtView.setText(city.getCountry());
    }

    @Override
    public int getItemCount() {
        return filteredCities.size();
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    filteredCities = cities;
                } else {
                    List<City> filteredList = new ArrayList<>();
                    for (City row : cities) {
                        // here we are looking for name
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())
                                || row.getCountry().toLowerCase().contains(charString.toLowerCase()) ) {
                            filteredList.add(row);
                        }
                    }

                    filteredCities = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredCities;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredCities = (List<City>) results.values;
                notifyDataSetChanged();
            }
        };
    }

}
