package com.ianwong.outlookcalendar.schedule;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ianwong.outlookcalendar.calendar.CalendarSet;
import com.ianwong.outlookcalendar.R;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.TextStyle;

import java.util.Locale;

/**
 * Created by ianwong on 2016/10/4.
 */

public class ScheduleViewAdapter extends RecyclerView.Adapter {
    private CalendarSet mCalendarSet;
    public ScheduleViewAdapter(@NonNull CalendarSet calendarSet) {
        super();
        mCalendarSet = calendarSet;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
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
        return new ScheduleItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ScheduleItemViewHolder viewHolder = (ScheduleItemViewHolder)holder;
        LocalDate date = mCalendarSet.getDateByIndex(position);
        StringBuffer strDate = new StringBuffer();
        if(CalendarSet.isToday(date)){
            strDate.append("今天.");
        }
        else if(CalendarSet.isYesterday(date)){
            strDate.append("昨天.");
        }
        else if(CalendarSet.isTomorrow(date)){
            strDate.append("明天.");
        }

        DayOfWeek dayOfWeek = date.getDayOfWeek();
        strDate.append(dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA) + ',');

        String monthDay = date.getDayOfMonth() < 10 ? "0" + date.getDayOfMonth() : "" + date.getDayOfMonth();
        strDate.append(date.getMonthValue() + "月 " + monthDay);

        if(date.getYear() != LocalDate.now().getYear()){
            strDate.append(", " + date.getYear());
        }
//        viewHolder.mDate.setText(strDate);
    }

    @Override
    public int getItemCount() {
        return (int)mCalendarSet.getAllDays();
    }

    public CalendarSet getCalendarSet(){
        return  mCalendarSet;
    }
}
