package com.ianwong.outlookcalendar.schedule;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ianwong.outlookcalendar.calendar.CalendarSet;
import com.ianwong.outlookcalendar.R;
import com.ianwong.outlookcalendar.weather.WeatherInfo;
import com.ianwong.outlookcalendar.weather.yahooweather.WeatherResponse;
import com.koushikdutta.ion.Ion;

import org.threeten.bp.LocalDate;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ianwong on 2016/10/4.
 * schedule View adapter
 */

public class ScheduleViewAdapter extends RecyclerView.Adapter {
    private CalendarSet mCalendarSet;
    public ScheduleViewAdapter(@NonNull CalendarSet calendarSet) {
        super();
        mCalendarSet = calendarSet;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        if(viewType == 1) {
            //viewType == 1 today and tomorrow to show weather info.
            LinearLayout itemContainer = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.schedule_item_container, parent, false);

            for(int i = 0 ; i < 3 ; i++){
                View weatherView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.schedule_item_weather, parent, false);
                itemContainer.addView(weatherView);
                TextView tv = (TextView)weatherView.findViewById(R.id.scheduleTime);
                if(i == 1){
                    tv.setText("Afternoon");
                }
                else if(i == 2){
                    tv.setText("Evening");
                }
            }

            itemView = itemContainer;
        }
        else{
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.schedule_item, parent, false);
            itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Toast toast = Toast.makeText(v.getContext(), "没有事件", Toast.LENGTH_SHORT);
                                                toast.setGravity(Gravity.BOTTOM, 0, 40);
                                                toast.show();
                                            }
                                        }
            );
        }

        return new ScheduleItemViewHolder(itemView, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ScheduleItemViewHolder viewHolder = (ScheduleItemViewHolder)holder;
        if(viewHolder.mViewType != 1){
            return;
        }

        //set weather info for today and tomorrow.
        WeatherInfo.getInstance().getWeatherInfo()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WeatherResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(WeatherResponse weatherResponse) {
                        LocalDate date = mCalendarSet.getDateByIndex(position);
                        if(CalendarSet.isToday(date)){
                            try {
                                //today weather info
                                String imgUrl = WeatherInfo.getWeatherStateImage(weatherResponse);
                                for(int i = 0 ; i < 3 ; i++) {
                                    Ion.with(viewHolder.mWeatherState[i]).load(imgUrl);
                                }
                                viewHolder.mTemperature[0].setText(WeatherInfo.getTemperature(weatherResponse, 0, 0));
                                viewHolder.mTemperature[1].setText(WeatherInfo.getTemperature(weatherResponse, 0, 2));
                                viewHolder.mTemperature[2].setText(WeatherInfo.getTemperature(weatherResponse, 0, 1));
                            }
                            catch (IndexOutOfBoundsException e){
                                e.printStackTrace();
                            }

                        }
                        else{
                            try {
                                //tomorrow weather info
                                String imgUrl = WeatherInfo.getWeatherStateImage(weatherResponse);
                                for(int i = 0 ; i < 3 ; i++) {
                                    Ion.with(viewHolder.mWeatherState[i]).load(imgUrl);
                                }
                                viewHolder.mTemperature[0].setText(WeatherInfo.getTemperature(weatherResponse, 1, 0));
                                viewHolder.mTemperature[1].setText(WeatherInfo.getTemperature(weatherResponse, 1, 2));
                                viewHolder.mTemperature[2].setText(WeatherInfo.getTemperature(weatherResponse, 1, 1));

                            }
                            catch (IndexOutOfBoundsException e){
                                e.printStackTrace();
                            }

                        }
                    }
                });
    }

    @Override
    public int getItemViewType (int position){
        LocalDate date = mCalendarSet.getDateByIndex(position);
        if(CalendarSet.isToday(date) || CalendarSet.isTomorrow(date)){
            return 1;
        }
        return  0;
    }

    @Override
    public int getItemCount() {
        return (int)mCalendarSet.getAllDays();
    }

    public CalendarSet getCalendarSet(){
        return  mCalendarSet;
    }
}
