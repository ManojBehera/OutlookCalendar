package com.ianwong.outlookcalendar.schedule;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;

import com.ianwong.outlookcalendar.calendar.CalendarSet;
import com.ianwong.outlookcalendar.R;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.TextStyle;

import java.util.Locale;

/**
 * Created by ianwong on 2016/10/4.
 */

public class ScheduleItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDateBkg;
    private int mHeight;
    private int mTextOffset;
    private Paint mTextPaint;
    private int mTxtColor;
    private int mTxtColorSelected;
    public ScheduleItemDecoration(Context ctx) {
        super();
        mDateBkg = ctx.getResources().getDrawable(R.drawable.schedule_item_date_bkg);
        mHeight = ctx.getResources().getDimensionPixelSize(R.dimen.schedule_item_decoration_height);
        mTextOffset = ctx.getResources().getDimensionPixelSize(R.dimen.schedule_item_text_offset);
        mTxtColorSelected = ctx.getResources().getColor(R.color.scheduleTextColorSelected);
        mTxtColor = ctx.getResources().getColor(R.color.scheduleTextColor);
        int txtFontSize = ctx.getResources().getDimensionPixelSize(R.dimen.sp_12);
        mTextPaint = new Paint();
        mTextPaint.setColor(mTxtColor);
        mTextPaint.setTextSize(txtFontSize);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setSubpixelText(true);
    }


    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++){
            //draw Top background
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = 0;
            final int right = parent.getWidth();
            int top = child.getTop() - params.topMargin - mHeight;
            if(top < 0 && child.getBottom() > 0){
                if(child.getBottom() < mHeight){
                   top = child.getBottom() - mHeight;
                }
                else{
                    top = 0;
                }
            }
            //Log.d("scroll","top:" + top);

            final int bottom = top + mHeight;
            mDateBkg.setBounds(left, top, right, bottom);
            mDateBkg.draw(c);

            //draw date info
            ViewHolder vh = (ViewHolder)child.getTag();
            final int position = vh.getAdapterPosition();
            final CalendarSet calendarSet = ((ScheduleViewAdapter)parent.getAdapter()).getCalendarSet();
            LocalDate date = calendarSet.getDateByIndex(position);
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

            Rect bounds = new Rect();
            mTextPaint.getTextBounds(strDate.toString(), 0, strDate.length(), bounds);
            if(calendarSet.isToday(date)){
                mTextPaint.setColor(mTxtColorSelected);
            }
            else {
                mTextPaint.setColor(mTxtColor);
            }
            c.drawText(strDate.toString(), mTextOffset, top + bounds.height() + (mHeight - bounds.height())/2 , mTextPaint);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(0, mHeight, 0, 0);
    }
}
