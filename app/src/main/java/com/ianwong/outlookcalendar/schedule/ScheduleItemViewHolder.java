package com.ianwong.outlookcalendar.schedule;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ianwong.outlookcalendar.R;

/**
 * Created by ianwong on 2016/10/4.
 */

public class ScheduleItemViewHolder extends RecyclerView.ViewHolder {
    public TextView mScheduleContent;
    public ScheduleItemViewHolder(View itemView) {
        super(itemView);
        mScheduleContent = (TextView)itemView.findViewById(R.id.scheduleContent);
        itemView.setTag(this);
    }
}
