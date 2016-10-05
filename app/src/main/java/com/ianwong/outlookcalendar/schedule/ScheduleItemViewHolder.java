package com.ianwong.outlookcalendar.schedule;

import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ianwong.outlookcalendar.R;

/**
 * Created by ianwong on 2016/10/4.
 * item viewHolder for Schedule list.
 */

public class ScheduleItemViewHolder extends RecyclerView.ViewHolder {
    public int mViewType;
    public TextView mScheduleContent;
    public ImageView[] mWeatherState = new ImageView[3];
    public TextView[] mTemperature = new TextView[3];
    public ScheduleItemViewHolder(View itemView, int viewType) {
        super(itemView);
        mViewType = viewType;
        mScheduleContent = (TextView)itemView.findViewById(R.id.scheduleContent);
        if(itemView instanceof LinearLayout && viewType == 1){
            for(int i = 0 ; i < 3 ; i++){
                mWeatherState[i] = (ImageView)((LinearLayout) itemView).getChildAt(i)
                                    .findViewById(R.id.weatherState);
                mTemperature[i] = (TextView)((LinearLayout) itemView).getChildAt(i)
                                    .findViewById(R.id.temperature);
            }
        }

        itemView.setTag(this);
    }
}
