package com.ianwong.outlookcalendar.weather;


import com.ianwong.outlookcalendar.weather.yahooweather.Forecast;
import com.ianwong.outlookcalendar.weather.yahooweather.WeatherResponse;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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
    private  static  WeatherResponse gWeatherResponse;
    private  static   Observable<WeatherResponse> gCachedWeatherRequest;

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
     * get China/shenzhen Weather Info.
     * becauseof  a Observable object ,caller can observer it
     * @return a Observable object that wrap WeatherResponse
     * WeatherResponse is auto generated from json by http://www.jsonschema2pojo.org/
     *
     * WARING: not thread safe .
     * */
    public Observable<WeatherResponse> getWeatherInfo(){
        String weatherQueryString = "select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22%E6%B7%B1%E5%9C%B3%22)";

        if(gWeatherResponse == null ) {
            if(gCachedWeatherRequest == null) {
                gCachedWeatherRequest = mYahooWeatherApi.getWeatherInfo(weatherQueryString, "json")
                        .subscribeOn(Schedulers.io())
                        .map(new Func1<WeatherResponse, WeatherResponse>() {
                            @Override
                            public WeatherResponse call(WeatherResponse weatherResponse) {
                                gWeatherResponse = weatherResponse;
                                return weatherResponse;
                            }
                        });
            }

            return gCachedWeatherRequest;
        }
        else{
            gCachedWeatherRequest = null;
            return  Observable.just(gWeatherResponse);
        }
    }

    /**
    * get city that the weatherResponse from.
    * */
    public static String getCity(WeatherResponse weatherResponse){
        return weatherResponse.getQuery().getResults().getChannel().getLocation().getCity();

    }

    /**
     * convert degree fah to degree cel
     * @param temp  degree fah (string)
     * */
    public static  String convertFahToCel(String temp){

      double cel = Double.parseDouble(temp);
        return String.valueOf((int)((cel - 32)/1.8)) + "℃";
    }

    /**
     * get temperature
     * @param weatherResponse  weather info from yahoo
     * @param whichDay which day that forecast by yahoo
     * range from today
     * @param type ==0 lowest temperature
     *             ==1 average temperature
     *             ==2 highest temperature
     *             default return average temperature.
     * */
    public static String getTemperature(WeatherResponse weatherResponse, int whichDay, int type)
            throws IndexOutOfBoundsException{

        List<Forecast> forecasts = weatherResponse.getQuery().getResults()
                .getChannel().getItem().getForecast();

        if(whichDay < 0 || whichDay > forecasts.size() ){
            throw new IndexOutOfBoundsException("exceed forecast ability");
        }

        String fah;
        switch (type){
            case 0: {
                fah = forecasts.get(whichDay).getLow();
            }
                break;
            case 2: {
                fah = forecasts.get(whichDay).getHigh();
            }
                break;
            default:{
                fah = weatherResponse.getQuery().getResults()
                        .getChannel().getItem().getCondition().getTemp();
            }
                break;
        }

        return convertFahToCel(fah);

    }

    /**
     * get weather state such as
     * "Partly Cloudy", "showers", "thunderstorms" etc.
     * */
    public static String getCloudState(WeatherResponse weatherResponse){
        return weatherResponse.getQuery().getResults()
                .getChannel().getItem().getCondition().getText();

    }

}
