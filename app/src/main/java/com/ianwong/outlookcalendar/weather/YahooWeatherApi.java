package com.ianwong.outlookcalendar.weather;
import com.ianwong.outlookcalendar.weather.yahooweather.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by ianwong on 2016/10/5.
 */

public interface YahooWeatherApi {
    @GET("v1/public/yql")
    Observable<WeatherResponse> getWeatherInfo(@Query(value = "q", encoded = true) String weatherQueryString
            , @Query(value = "format", encoded = true) String format);
}
