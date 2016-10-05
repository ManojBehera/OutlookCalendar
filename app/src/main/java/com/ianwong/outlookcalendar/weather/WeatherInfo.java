package com.ianwong.outlookcalendar.weather;


import com.ianwong.outlookcalendar.weather.yahooweather.WeatherResponse;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by ianwong on 2016/10/5.
 */

public class WeatherInfo {
    private  YahooWeatherApi mYahooWeatherApi;
    private  static WeatherInfo gWeatherInfo;
    private WeatherInfo(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://query.yahooapis.com/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
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

    public Observable<WeatherResponse> getWeatherInfo(){
        String weatherQueryString = "select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22%E6%B7%B1%E5%9C%B3%22)";
        return mYahooWeatherApi.getWeatherInfo(weatherQueryString, "json");
    }
}
