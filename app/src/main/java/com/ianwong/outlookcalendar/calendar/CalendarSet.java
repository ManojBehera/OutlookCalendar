package com.ianwong.outlookcalendar.calendar;

import android.support.annotation.NonNull;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.temporal.ChronoUnit;

/**
 * Created by ianwong on 2016/10/2.
 */

public class CalendarSet {
    private LocalDate mFirstShowDate;
    private LocalDate mLastShowDate;
    private long mAllDays;

    /**
    * set the show days based on today ,
    * include previous months (@monthsToSubtract),current month and future months.
    *@param monthsToSubtract, months that be show before current month
    *@param monthsToAdd, months that be show after current month
    */
    public CalendarSet(int monthsToSubtract, int monthsToAdd){

        LocalDate today = LocalDate.now();

        LocalDate startYearDate = today.minusMonths(monthsToSubtract);
        int subDays = startYearDate.getDayOfWeek().getValue() % 7;
        mFirstShowDate = startYearDate.minusDays(subDays);

        LocalDate endYearDate = today.plusMonths(monthsToAdd);
        int addDays = 6 - (endYearDate.getDayOfWeek().getValue()) % 7;
        mLastShowDate = endYearDate.plusDays(addDays);
        mAllDays = mFirstShowDate.until(mLastShowDate, ChronoUnit.DAYS) + 1;
    }

    /**
     * get days in this Calendar ranges
     * */
    public long getAllDays(){
        return mAllDays;
    }

    /**
     * get LocalDate by index
     * @param index offset from the first show date
     * */
    @NonNull
    public LocalDate getDateByIndex(long index) throws ArrayIndexOutOfBoundsException{
        if(index < 0 || index >= mAllDays)
            throw new ArrayIndexOutOfBoundsException();

        return  mFirstShowDate.plusDays(index);
    }

    /**
     * get date index by local date
     * return @param date's the offset from first show date.
     * @param date used to calculate the offset.
     * */
    public int getDateIndexByLocalDate(LocalDate date){
        int offset  = (int)mFirstShowDate.until(date, ChronoUnit.DAYS);
        return offset;
    }

    /**
     * begin with first show date, get today's offset in all days.
     * */
    public int getTodayDateIndex(){
        LocalDate today = LocalDate.now();
       return getDateIndexByLocalDate(today);
    }

    /**
     * get month's show lines in current calendar.
     * @param date calculate date's month lines
     * */
    public int getMonthLines(LocalDate date){
        if(date == null ){
            return  0;
        }

        date = date.withDayOfMonth(1);
        if(date.getDayOfWeek() == DayOfWeek.SUNDAY && date.lengthOfMonth() == 28) {
            return 4;
        }

        return 5;
    }

    /**
     * return the date is today or not
     * @param date if the date is today return true
     * else return false
     * */
    static public boolean isToday(LocalDate date){
        LocalDate today = LocalDate.now();
        if(today.isEqual(date)) {
            return true;
        }
        return false;
    }

    /**
     * return the date is yesterday or not
     * @param date if the date is today return true
     * else return false
     * */
    static public boolean isYesterday(LocalDate date){
        LocalDate yesterday = LocalDate.now().minusDays(1);
        if(yesterday.isEqual(date)){
            return true;
        }
        return  false;
    }


    /**
     * return the date is yesterday or not
     * @param date if the date is today return true
     * else return false
     * */
    static public boolean isTomorrow(LocalDate date){
        LocalDate yesterday = LocalDate.now().plusDays(1);
        if(yesterday.isEqual(date)){
            return true;
        }
        return  false;
    }

}
