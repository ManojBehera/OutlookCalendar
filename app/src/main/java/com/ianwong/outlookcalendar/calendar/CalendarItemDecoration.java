package com.ianwong.outlookcalendar.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ianwong.outlookcalendar.R;

import org.threeten.bp.LocalDate;

/**
 * Created by ianwong on 2016/10/3.
 * draw bottom line for CalendarItemView.
 */

public class CalendarItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDivider;
    private Paint mTextPaint;
    private Paint mMaskPaint;
    private int mScrollState;

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if(newState == RecyclerView.SCROLL_STATE_IDLE){
                //remove scroll pressed state after 500ms
                final int setNewState = newState;
                final RecyclerView rcView = recyclerView;
                recyclerView.postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        CalendarItemDecoration.this.mScrollState = setNewState;
                        rcView.invalidate();
                    }
                }, 500);
            }
            else{
                mScrollState = newState;
            }
        }
    };

    public CalendarItemDecoration(Context ctx) {
        super();
        mDivider = ctx.getResources().getDrawable(R.drawable.calendar_item_divider);
        int monthTipsColor = ctx.getResources().getColor(R.color.calendarMonthTips);
        int calendarMaskColor = ctx.getResources().getColor(R.color.calendarMask);
        int monthTipsFontSize = ctx.getResources().getDimensionPixelSize(R.dimen.sp_20);
        mTextPaint = new Paint();
        mTextPaint.setColor(monthTipsColor);
        mTextPaint.setTextSize(monthTipsFontSize);
        mMaskPaint = new Paint();
        mMaskPaint.setColor(calendarMaskColor);
    }

    public RecyclerView.OnScrollListener  getScrollListener(){
        return mOnScrollListener;
    }


    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state){
        drawHorizontalLine(c,parent);
        if(mScrollState != RecyclerView.SCROLL_STATE_IDLE) {
            drawPressState(c, parent);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition,
                               RecyclerView parent){

            outRect.set(0, 0, 0, 0);
    }

    //draw horizontal line for calendar
    protected void drawHorizontalLine(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++){
            //draw bottom line
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getLeft() - params.leftMargin;
            final int right = child.getRight() + params.rightMargin;
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    /**
     * draw calendar's press state.
     * 1)draw foreground Mask
     * 2)draw Month Tips
     * */
    protected void drawPressState(Canvas c, RecyclerView parent){

        //draw foreground Mask
        c.drawRect(0,0,parent.getWidth(),parent.getHeight(),mMaskPaint);
        //draw month tips
        int curMonth = 0;
        boolean hasDrawTips = false;
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++){
            //draw monthTips if needed.
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final  int dateIndex = ((CalendarItemView)child).mDateIndex;
            final CalendarSet calendarSet = ((CalendarAdapter)parent.getAdapter()).getCalendarSet();
            LocalDate date = calendarSet.getDateByIndex(dateIndex);
            if(date.getMonthValue() != curMonth){
                curMonth = date.getMonthValue();
                hasDrawTips = false;
            }

            if(!hasDrawTips){
                int monthStartLineNum = calendarSet.getDateIndexByLocalDate(date.withDayOfMonth(1))/7;
                int monthNowLineNum = calendarSet.getDateIndexByLocalDate(date) / 7;
                int lines = calendarSet.getMonthLines(date);
                int itemHeight = child.getHeight() + params.bottomMargin + params.topMargin;
                int startTop = (child.getTop()-params.topMargin) - (monthNowLineNum - monthStartLineNum) * itemHeight;

                StringBuffer monthTips = new StringBuffer(date.getMonth().getValue() + "月");
                if(LocalDate.now().getYear() != date.getYear()){
                    monthTips.append(" " + date.getYear());
                }

                Rect bounds = new Rect();
                mTextPaint.getTextBounds(monthTips.toString(), 0, monthTips.length(), bounds);
                int txtWidth = bounds.width();
                int txtHeight = bounds.height();
                bounds.left = (parent.getWidth() - txtWidth) / 2;
                bounds.top = startTop + (lines * itemHeight - txtHeight)/2;
                c.drawText(monthTips.toString(), bounds.left, bounds.top, mTextPaint);
                hasDrawTips = true;
            }
        }
    }
}
