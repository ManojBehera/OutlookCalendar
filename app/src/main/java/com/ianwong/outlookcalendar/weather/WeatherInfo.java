package com.ianwong.outlookcalendar.weather;

import android.util.Log;

import com.ianwong.outlookcalendar.weather.yahooweather.WeatherResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ianwong on 2016/10/5.
 */

public class WeatherInfo {
    private  YahooWeatherApi mYahooWeatherApi;
    private  static WeatherInfo gWeatherInfo;
    private WeatherInfo(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://query.yahooapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mYahooWeatherApi = retrofit.create(YahooWeatherApi.class);
    }

    public static WeatherInfo getInstance(){
        if(gWeatherInfo == null){
            gWeatherInfo = new WeatherInfo();
        }

        return gWeatherInfo;
    }

    public  void getWeatherInfo(){
        String weatherQueryString = "select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22%E6%B7%B1%E5%9C%B3%22)";
        Call<WeatherResponse> call = mYahooWeatherApi.getWeathInfo(weatherQueryString, "json");
        call.enqueue(new Callback<WeatherResponse>(){
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response){
                    Log.d("weather" , response.body().toString());
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t){
                Log.d("weather" , "failed...");
            }

        });

    }

}
