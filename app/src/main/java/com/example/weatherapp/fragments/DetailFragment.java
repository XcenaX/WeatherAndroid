package com.example.weatherapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.weatherapp.R;
import com.example.weatherapp.entities.Coordinate;
import com.example.weatherapp.entities.Weather;
import com.example.weatherapp.services.OpenWeatherApiService;
import com.example.weatherapp.services.WeatherClient;
import com.example.weatherapp.utils.GlideUtils;
import com.example.weatherapp.utils.LocationSrv;
import com.example.weatherapp.utils.OpenWeatherApi;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailFragment extends Fragment {
    private static final String TAG = "DetailFragment";
    private Coordinate mCoord;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mCoord = (Coordinate) bundle.getSerializable("LOC_COORD");
        }else{
            LocationSrv lsrv = new LocationSrv(this.getActivity());
            lsrv.getLastLocation();
            mCoord = lsrv.getmCoordinate();
        }
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.weather_detail_layout, container, false);

        final String url = String.format("weather?lat=%s&lon=%s&units=metric&appid=%s",
                mCoord.getLatitude(), mCoord.getLongitude(), OpenWeatherApi.KEY);

        final OpenWeatherApiService service = OpenWeatherApiService.getInstance();
        WeatherClient client = service.getRetrofitService();
        Call<Weather> call = client.getWeather(url);

        call.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                Weather weather = response.body();
                if (weather != null) {
                    TextView cityTxt = view.findViewById(R.id.weather_city_name);
                    cityTxt.setText(weather.getName());
                    TextView temp = view.findViewById(R.id.weather_temp);
                    temp.setText(String.valueOf(weather.getMain().getTemp()));
                    TextView hum = view.findViewById(R.id.weather_humidity);
                    hum.setText(String.valueOf(weather.getMain().getHumidity()));
                    TextView desc = view.findViewById(R.id.weather_main_desc);
                    desc.setText(weather.getWeather().get(0).getDescription());
                    ImageView image = view.findViewById(R.id.weather_icon);
                    GlideUtils.uploadIcon(getActivity(), image, weather.getWeather().get(0).getIcon());
                }
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                Log.e(TAG, Objects.requireNonNull(t.getMessage()));
                Toast.makeText(getActivity(), "can not get weather", Toast.LENGTH_SHORT).show();
            }
        });

        TextView lanTextVIew = view.findViewById(R.id.latitude_text);
        lanTextVIew.setText(String.valueOf(mCoord.getLatitude()));
        TextView lonTextView = view.findViewById(R.id.longitude_text);
        lonTextView.setText(String.valueOf(mCoord.getLongitude()));


        return view;
    }
}
