package com.example.jevil.mindreactor.Events.TabEvents;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.jevil.mindreactor.BDHelpers.DBHelper;
import com.example.jevil.mindreactor.BDHelpers.DBHelperC;
import com.example.jevil.mindreactor.Events.TabEvents.EventsList.EventsItem;
import com.example.jevil.mindreactor.Events.TabStory.StoryListItem;
import com.example.jevil.mindreactor.Other.HelpClass;
import com.example.jevil.mindreactor.R;
import com.example.jevil.mindreactor.ServiceNotification;

import java.util.Date;

public class AddCompleteEvent extends AppCompatActivity implements View.OnClickListener {
    private Intent intent;
    private DBHelperC dbHelperC;
    private SQLiteDatabase dbC;
    Context context;
    Cursor c;
    int minutesInt;
    String name, type, benefit, id, mark, previousDate;
    TextView tvMark, tvType, tvBenefit, tvEverydayEvent;
    ImageButton ibPlay, ibStop;
    Chronometer chronometer;
    boolean isPlay = false;
    private long timeWhenStopped = 0;
    HelpClass helpClass;
    private TimePicker mTimePicker;
    int intEverydayEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_complete_task);
        initializeComponents();
        context = this;

        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mActionBarToolbar);
        mActionBarToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        intent = getIntent();
        if (intent.getStringExtra("type").equals("edit")) {
            StoryListItem item = StoryListItem.selectedItem;
            dbHelperC = new DBHelperC(context);
            dbC = dbHelperC.getWritableDatabase(); //создаем объект для убравления БД
            Cursor c = dbC.query(DBHelperC.TABLE_C_TASKS, null, "_id" + " = " + item.getBDitemID(), null, null, null, null);
            c.moveToFirst();
            tvMark.setText(String.valueOf(c.getInt(c.getColumnIndex("mark"))));
            tvType.setText(c.getString(c.getColumnIndex("type")));
            tvBenefit.setText(c.getString(c.getColumnIndex("benefit")));
            int minutesBuffer = Integer.valueOf(c.getString(c.getColumnIndex("minutes")));
            int hoursBuffer = 0;
            if (minutesBuffer >= 60) {
                hoursBuffer = minutesBuffer / 60;
                minutesBuffer = minutesBuffer % 60;
            }
            mTimePicker.setCurrentHour(hoursBuffer);
            mTimePicker.setCurrentMinute(minutesBuffer);
            //  etMinutes.setText(c.getString(c.getColumnIndex("minutes")));
            id = c.getString(c.getColumnIndex("_id"));
            name = c.getString(c.getColumnIndex("name"));
            type = c.getString(c.getColumnIndex("type"));
            benefit = c.getString(c.getColumnIndex("benefit"));
            previousDate = c.getString(c.getColumnIndex("time"));
            this.setTitle(c.getString(c.getColumnIndex("name")));
            c.close();
            dbHelperC.close();
            dbC.close();
        } else {
            EventsItem item = EventsItem.selectedItem;
            DBHelper dbHelper = new DBHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            c = db.query(DBHelper.TABLE_TASKS, null, "_id" + " = " + item.getBDitemID(), null, null, null, null);
            c.moveToFirst();
            name = c.getString(c.getColumnIndex("name"));
            type = c.getString(c.getColumnIndex("type"));
            benefit = c.getString(c.getColumnIndex("benefit"));
            this.setTitle(name);
            tvMark.setText(String.valueOf(c.getInt(c.getColumnIndex("mark"))));
            tvType.setText(type);
            tvBenefit.setText(benefit);
            mTimePicker.setCurrentHour(0);
            mTimePicker.setCurrentMinute(0);
            if (intEverydayEvent != 0) {
                tvEverydayEvent.setText("Каждодневное событие, минут в день: " + intEverydayEvent);
            } else {
                tvEverydayEvent.setText("");
            }
            c.close();
            dbHelper.close();
            db.close();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_add_complete_task:
                dbHelperC = new DBHelperC(context);
                dbC = dbHelperC.getWritableDatabase(); //создаем объект для убравления БД

                mark = tvMark.getText().toString();
                String hours = String.valueOf(mTimePicker.getCurrentHour());
                String minutes = String.valueOf(mTimePicker.getCurrentMinute());
                if (hours.equals("")) {
                    minutesInt = Integer.valueOf(minutes);
                } else {
                    if (minutes.equals("")) {
                        minutesInt = Integer.valueOf(hours) * 60;
                    } else {
                        minutesInt = Integer.valueOf(minutes) + Integer.valueOf(hours) * 60;
                    }
                }
                float markFloat = Integer.valueOf(mark);
                float marks = minutesInt * markFloat / 60;
                ContentValues contentValues = new ContentValues(); // добавляем строки в таблицу (ключ - значение)
                contentValues.put(DBHelperC.KEY_NAME, name); // название
                contentValues.put(DBHelperC.KEY_MARK, mark); // балл за час или разово
                contentValues.put(DBHelperC.KEY_TYPE, type); // тип, продолжительное или разовое
                contentValues.put(DBHelperC.KEY_MARKS, marks);
                contentValues.put(DBHelperC.KEY_MINUTES, minutesInt); // количество минут
                contentValues.put(DBHelperC.KEY_BENEFIT, benefit);  // польза или вред
                if (intent.getStringExtra("type").equals("edit")) {
                    contentValues.put(DBHelperC.KEY_TIME, previousDate);
                    dbC.update(DBHelperC.TABLE_C_TASKS, contentValues, DBHelperC.KEY_ID + "= ?", new String[]{String.valueOf(id)});
                } else {
                    long millis = new Date().getTime();
                    contentValues.put(DBHelperC.KEY_TIME, millis);
                    dbC.insert(DBHelperC.TABLE_C_TASKS, null, contentValues); //добавление данных в БД
                }

                dbHelperC.close();
                dbC.close();
                finish();
                break;
            case R.id.ibPlay:
                startService(new Intent(this, ServiceNotification.class));
//                ibStop.setVisibility(View.VISIBLE);
//                chronometer.setVisibility(View.VISIBLE);
//                if (isPlay) { //пауза
//                    timeWhenStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
//                    chronometer.stop();
//                    isPlay = false;
//                    ibPlay.setImageResource(R.mipmap.ic_play);
//                } else { //запуск
//                    chronometer.setBase(SystemClock.elapsedRealtime()+timeWhenStopped);
//                    chronometer.start();
//                    isPlay = true;
//                    ibPlay.setImageResource(R.mipmap.ic_pause);
//                }
//                break;
            case R.id.ibPause: //остановка
                ibStop.setVisibility(View.INVISIBLE);
                timeWhenStopped = chronometer.getBase() - SystemClock.elapsedRealtime();

                String[] arr = chronometer.getText().toString().split(":");
                int minutesBuffer = Integer.valueOf(arr[1]);
                int hoursBuffer = Integer.valueOf(arr[0]);

                if (minutesBuffer >= 60) {
                    hoursBuffer = minutesBuffer / 60;
                    minutesBuffer = minutesBuffer % 60;
                }
                mTimePicker.setCurrentHour(hoursBuffer);
                mTimePicker.setCurrentMinute(minutesBuffer);
                chronometer.stop();
                chronometer.setBase(SystemClock.elapsedRealtime());
                timeWhenStopped = 0;
                chronometer.setVisibility(View.INVISIBLE);
                ibPlay.setImageResource(R.mipmap.ic_play);
                isPlay = false;
                break;
        }
    }

    protected void initializeComponents() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        helpClass = new HelpClass();

        tvEverydayEvent = (TextView) findViewById(R.id.tvEverydayEvent);
        tvMark = (TextView) findViewById(R.id.tvMark);
        tvType = (TextView) findViewById(R.id.tvType);
        tvBenefit = (TextView) findViewById(R.id.tvBenefit);
        mTimePicker = (TimePicker) findViewById(R.id.timePicker);
        mTimePicker.setIs24HourView(true);
        ibPlay = (ImageButton) findViewById(R.id.ibPlay);
        ibPlay.setOnClickListener(this);
        ibStop = (ImageButton) findViewById(R.id.ibPause);
        ibStop.setOnClickListener(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_complete_task);
        fab.setOnClickListener(this);

        chronometer = (Chronometer) findViewById(R.id.chronometer2);
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener(){
            @Override
            public void onChronometerTick(Chronometer cArg) {
                long time = SystemClock.elapsedRealtime() - cArg.getBase();
                int h   = (int)(time /3600000);
                int m = (int)(time - h*3600000)/60000;
                int s= (int)(time - h*3600000- m*60000)/1000 ;
                String hh = h < 10 ? "0"+h: h+"";
                String mm = m < 10 ? "0"+m: m+"";
                String ss = s < 10 ? "0"+s: s+"";
                cArg.setText(hh+":"+mm+":"+ss);
            }
        });

    }
}
