package com.ianwong.outlookcalendar;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ianwong.outlookcalendar.calendar.CalendarAdapter;
import com.ianwong.outlookcalendar.calendar.CalendarItemDecoration;
import com.ianwong.outlookcalendar.calendar.CalendarSet;
import com.ianwong.outlookcalendar.schedule.ScheduleItemDecoration;
import com.ianwong.outlookcalendar.schedule.ScheduleViewAdapter;
import com.ianwong.outlookcalendar.weather.WeatherInfo;
import com.ianwong.outlookcalendar.weather.yahooweather.WeatherResponse;
import com.jakewharton.threetenabp.AndroidThreeTen;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {
    private RecyclerView mCalendarView;
    private CalendarAdapter mCalendarAdapter;
    private boolean mScheduleMoveByCalendar = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(@NonNull View view) {
                WeatherInfo.getInstance().getWeatherInfo()
                        .subscribeOn(Schedulers.io())
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
                                String weatherInfo = "city:" + weatherResponse.getQuery().getResults().getChannel().getLocation().getCity()
                                        + " temp:" + weatherResponse.getQuery().getResults().getChannel().getItem().getCondition().getTemp()
                                        + " code:" + weatherResponse.getQuery().getResults().getChannel().getItem().getCondition().getCode();

                                Toast toast = Toast.makeText(MainActivity.this, weatherInfo, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.BOTTOM, 0, 40);
                                toast.show();
                            }
                        });
            }
        });

        //initialize android three ten back port;
        AndroidThreeTen.init(this);
        //initialize Views
        initDataAndView();

    }

    public void initDataAndView() {
        //initialize calendar View
        mCalendarView = (RecyclerView) findViewById(R.id.calendar);
        final int screenWidth = getResources().getDisplayMetrics().widthPixels;
        ViewGroup.LayoutParams lp = mCalendarView.getLayoutParams();
        lp.height = screenWidth / 7 * 2 + (int) (3 * getResources().getDisplayMetrics().density);
        mCalendarView.setLayoutParams(lp);
        final CalendarSet calendarSet = new CalendarSet(3, 12);
        mCalendarAdapter = new CalendarAdapter(calendarSet);
        mCalendarView.setAdapter(mCalendarAdapter);
        final GridLayoutManager calendarManager = new GridLayoutManager(this, 7);
        mCalendarView.setLayoutManager(calendarManager);
        CalendarItemDecoration calendarItemDecoration = new CalendarItemDecoration(this);
        mCalendarView.addOnScrollListener(calendarItemDecoration.getScrollListener());
        mCalendarView.addItemDecoration(calendarItemDecoration);
        mCalendarView.setItemAnimator(null);
        calendarManager.scrollToPosition(calendarSet.getTodayDateIndex());

        mCalendarView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_DRAGGING){
                    //expand calendar:set calendarView's height as a month items height if needed
                    ViewGroup.LayoutParams lp = mCalendarView.getLayoutParams();
                    if(lp.height < screenWidth / 7 * 5) {
                        lp.height = screenWidth / 7 * 5 + (int) (3 * getResources().getDisplayMetrics().density);
                        mCalendarView.setLayoutParams(lp);
                    }
                }
            }
        });

        //initialize schedule View
        RecyclerView scheduleView = (RecyclerView) findViewById(R.id.schedule);
        final LinearLayoutManager scheduleLayoutManager = new LinearLayoutManager(this);
        scheduleView.setLayoutManager(scheduleLayoutManager);
        ScheduleViewAdapter scheduleViewAdapter = new ScheduleViewAdapter(calendarSet);
        scheduleView.setAdapter(scheduleViewAdapter);
        scheduleView.addItemDecoration(new ScheduleItemDecoration(this));
        scheduleLayoutManager.scrollToPosition(calendarSet.getTodayDateIndex() - 1);

        //set item click listener and scroll the schedule list
        mCalendarAdapter.setOnItemClickListener(new CalendarAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                //make the position item visible at the second place in schedule list.
                scheduleLayoutManager.scrollToPositionWithOffset(position - 1, 0);
                mScheduleMoveByCalendar = true;
                return;
            }
        });

        scheduleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //if scheduleView is scrolled by calendar ,not response this listener.
                if (mScheduleMoveByCalendar) {
                    mScheduleMoveByCalendar = false;
                    return;
                }

                //collapse calendar:set calendarView's height as two date item height if needed
                ViewGroup.LayoutParams lp = mCalendarView.getLayoutParams();
                if(lp.height > screenWidth / 7 * 2) {
                    lp.height = screenWidth / 7 * 2 + (int) (3 * getResources().getDisplayMetrics().density);
                    mCalendarView.setLayoutParams(lp);
                }

                //adjust calendarView's selected date on schedule view 's first visible item
                //position changed.
                int firstVisiblePosition = scheduleLayoutManager.findFirstVisibleItemPosition();
                //1)make the position visile
                mCalendarView.scrollToPosition(firstVisiblePosition);
                //2) set selected state if needed.
                if (firstVisiblePosition != mCalendarAdapter.getSelectedDateIndex()
                        ) {
                    mCalendarAdapter.setSelectedDateIndex(firstVisiblePosition);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
