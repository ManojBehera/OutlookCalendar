package com.ianwong.outlookcalendar.weather;


import com.ianwong.outlookcalendar.weather.yahooweather.WeatherResponse;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by ianwong on 2016/10/5.
 * Weather Info is a singe instance object.
 * can return weather info by WeatherResponse
 * WeatherResponse.java(include yahoo weather fold)
 * is auto generated from json
 * by http://www.jsonschema2pojo.org/
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

    /**
     * get shenzhen WeathInfo.
     * becauseof  a Observable object ,caller can observer it
     * @return a Observable object that wrap WeatherResponse
     * WeatherResponse is auto generated from json by http://www.jsonschema2pojo.org/
     * */
    public Observable<WeatherResponse> getWeatherInfo(){
        String weatherQueryString = "select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22%E6%B7%B1%E5%9C%B3%22)";
        return mYahooWeatherApi.getWeatherInfo(weatherQueryString, "json");
    }

    public static String getCity(WeatherResponse weatherResponse){
        return weatherResponse.getQuery().getResults().getChannel().getLocation().getCity();

    }

    public static String getTemperature(WeatherResponse weatherResponse){
        return weatherResponse.getQuery().getResults()
                .getChannel().getItem().getCondition().getTemp();

    }

    public static String getLowTemperature(WeatherResponse weatherResponse){
        return weatherResponse.getQuery().getResults()
                .getChannel().getItem().getForecast().get(0).getLow();

    }

    public static String getHighTemperature(WeatherResponse weatherResponse){
        return weatherResponse.getQuery().getResults()
                .getChannel().getItem().getForecast().get(0).getHigh();

    }


    public static String getCode(WeatherResponse weatherResponse){
       return weatherResponse.getQuery().getResults().getChannel().getItem().getCondition().getCode();

    }



}
