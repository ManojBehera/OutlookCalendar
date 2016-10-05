package com.ianwong.outlookcalendar.calendar;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;

import com.ianwong.outlookcalendar.R;

import org.threeten.bp.LocalDate;

/**
 * Created by ianwong on 2016/10/3.
 */

public class CalendarAdapter extends RecyclerView.Adapter<CalendarItemViewHolder> {
    private CalendarSet mCalendarSet;
    private View mCurSelctedView = null;
    private int mCurSelectedDateIndex = -1;
    private  OnItemClickListener mOnItemClickListener = null;
    public interface OnItemClickListener {
       void onItemClickListener(View v,int position);
    }

    public CalendarAdapter(@NonNull CalendarSet calendarSet){
        mCalendarSet = calendarSet;
    }

    @Override
    public CalendarItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.calendar_item, parent, false);

        mCurSelectedDateIndex = mCalendarSet.getTodayDateIndex();
        CalendarItemView tv = (CalendarItemView) itemView;
        tv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CalendarItemView tv = (CalendarItemView) v;
                setSelectedDateIndex(tv.mDateIndex);
                if(mOnItemClickListener != null){
                    mOnItemClickListener.onItemClickListener(tv, tv.mDateIndex);
                }
            }
        });
        return new CalendarItemViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final CalendarItemViewHolder viewHolder,  final int i) {
        CalendarItemView tv = (CalendarItemView) viewHolder.itemView;
        tv.mDateIndex = i;
        LocalDate date = mCalendarSet.getDateByIndex(i);
        int day = date.getDayOfMonth();
        //set date text for this item
        if(day == 1 && i !=  mCurSelectedDateIndex){
            //1)show month and year
            StringBuffer str = new  StringBuffer("" + date.getMonth().getValue() + "月\n" + day);
            if(date.getYear() != LocalDate.now().getYear()){
                str.append("\n" + date.getYear());
            }
            tv.setText(str.toString());
        }
        else{
            //2)just show day
            tv.setText("" + day);
        }

        //set activate state if this item is today
        //this state has a different background .
        if(mCalendarSet.isToday(date)){
            tv.setActivated(true);
        }
        else{
            tv.setActivated(false);
        }

        //set selected state
        if(i ==  mCurSelectedDateIndex){
            if(mCurSelctedView != tv) {
                if(mCurSelctedView != null) {
                    mCurSelctedView.setSelected(false);
                }
                tv.setSelected(true);
                mCurSelctedView = tv;
            }
        }
        else {
            tv.setSelected(false);
        }
    }

    /**
     * set onItemClickListener for Calendar
     * notify the  observer when item clicked.
     * */
    public void setOnItemClickListener(OnItemClickListener onItemClickListner){
        mOnItemClickListener = onItemClickListner;
    }

    /**
     * set Selected Date in calendar
     * @param dateIndex position in mCalendarset.
     * */
    public void setSelectedDateIndex(int dateIndex){
        if(mCurSelectedDateIndex == dateIndex)
            return;
        int preSelectedIndex = mCurSelectedDateIndex;
        mCurSelectedDateIndex = dateIndex;
        //previous selected date item' text may be need changed
        //so we need notifyItemChanged for previous selected item.
        //then notify current selected item
        notifyItemChanged(preSelectedIndex);
        notifyItemChanged(mCurSelectedDateIndex);
    }

    /**
     * get current selected Date in calendar
     *
     * */
    public int getSelectedDateIndex(){
        return mCurSelectedDateIndex;
    }

    @Override
    public int getItemCount() {
        return (int)mCalendarSet.getAllDays();
    }

    /**
     * return calendar set used by others.
     * */
    public CalendarSet getCalendarSet(){
        return  mCalendarSet;
    }

}
