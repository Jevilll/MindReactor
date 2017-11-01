package com.example.jevil.mindreactor.Events.TabEvents;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.jevil.mindreactor.BDHelpers.DBHelper;
import com.example.jevil.mindreactor.BDHelpers.DBHelperC;
import com.example.jevil.mindreactor.Other.HelpClass;
import com.example.jevil.mindreactor.R;

import java.util.Calendar;
import java.util.Date;

import static com.example.jevil.mindreactor.R.id.timePicker;


public class AddCompleteEvent extends AppCompatActivity implements View.OnClickListener {
    private Intent intent;
    private DBHelperC dbHelperC;
    private SQLiteDatabase dbC;
    Context context;
    Cursor c;
    int minutesInt;
    String name, type, benefit, id, previousDate;
    TextView tvMark, tvBenefit, tvBenefitHint;
    int mark;
    HelpClass helpClass;
    private TimePicker mTimePicker;
    int itemId;
    Button btnStartStopwatch;

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
//                if (intent.getStringExtra("type").equals("addFromNotification")) {
//                    Intent intent = new Intent(context, MainActivity.class);
//                    startActivity(intent);
//                } else
                    finish();
            }
        });

        SQLiteOpenHelper dbHelp;
        String tableName;
        intent = getIntent();
        if (intent.getStringExtra("type").equals("edit")) { // редактирование
            dbHelp = new DBHelperC(context);
            tableName = DBHelperC.TABLE_C_TASKS;
        } else {
            dbHelp = new DBHelper(context);
            tableName = DBHelper.TABLE_TASKS;
        }
        SQLiteDatabase dbDatabase = dbHelp.getWritableDatabase();
        Cursor cursor = dbDatabase.query(tableName, null, "_id" + " = " + intent.getIntExtra("_id", -1), null, null, null, null);
        cursor.moveToFirst();
        name = cursor.getString(cursor.getColumnIndex("name"));
        this.setTitle(name);

        mark = cursor.getInt(cursor.getColumnIndex("mark"));
        String textMark1 = "Баллов: " + mark;
        tvMark.setText(textMark1);
        type = cursor.getString(cursor.getColumnIndex("type"));
        benefit = cursor.getString(cursor.getColumnIndex("benefit"));
        if (benefit.equals("useful")) {
            tvBenefit.setText("Накопление");
            tvBenefitHint.setText("добавление баллов");
        }else {
            tvBenefit.setText("Трата");
            tvBenefitHint.setText("вычитание баллов");
        }

        if (intent.getStringExtra("type").equals("edit")) { // редактирование
            setTimePicker(Integer.valueOf(cursor.getString(cursor.getColumnIndex("minutes"))));
            id = cursor.getString(cursor.getColumnIndex("_id"));
            previousDate = cursor.getString(cursor.getColumnIndex("time"));
        }
        cursor.close();
        dbHelp.close();
        dbDatabase.close();

//        if (intent.getStringExtra("type").equals("edit")) { // редактирование
//            dbHelperC = new DBHelperC(context);
//            dbC = dbHelperC.getWritableDatabase(); //создаем объект для убравления БД
//            Cursor c = dbC.query(DBHelperC.TABLE_C_TASKS, null, "_id" + " = " + intent.getIntExtra("_id", -1), null, null, null, null);
//            c.moveToFirst();
//            mark = c.getInt(c.getColumnIndex("mark"));
//            String textMark = "Баллов: " + mark;
//            tvMark.setText(textMark);
//            setTimePicker(Integer.valueOf(c.getString(c.getColumnIndex("minutes"))));
//            id = c.getString(c.getColumnIndex("_id"));
//            name = c.getString(c.getColumnIndex("name"));
//            type = c.getString(c.getColumnIndex("type"));
//            benefit = c.getString(c.getColumnIndex("benefit"));
//            previousDate = c.getString(c.getColumnIndex("time"));
//            this.setTitle(c.getString(c.getColumnIndex("name")));
//            c.close();
//            dbHelperC.close();
//            dbC.close();
//        } else { // новая задача
////            EventsItem item = EventsItem.selectedItem;
////            if (intent.getStringExtra("type").equals("addFromNotification")) {
////                itemId =intent.getIntExtra("itemId", -1);
////            } else {
////                itemId =  item.getBDitemID();
////            }
//            DBHelper dbHelper = new DBHelper(this);
//            SQLiteDatabase db = dbHelper.getWritableDatabase();
//            c = db.query(DBHelper.TABLE_TASKS, null, "_id" + " = " + intent.getIntExtra("_id", -1), null, null, null, null);
//            c.moveToFirst();
//            name = c.getString(c.getColumnIndex("name"));
//            type = c.getString(c.getColumnIndex("type"));
//            benefit = c.getString(c.getColumnIndex("benefit"));
//            this.setTitle(name);
//            mark = c.getInt(c.getColumnIndex("mark"));
//            String textMark = "Баллов: " + mark;
//            tvMark.setText(textMark);
//            if (benefit.equals("useful")) {
//                tvBenefit.setText("Накопление");
//                tvBenefitHint.setText("добавление баллов");
//            }else {
//                tvBenefit.setText("Трата");
//                tvBenefitHint.setText("вычитание баллов");
//            }
//            c.close();
//            dbHelper.close();
//            db.close();
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("timeStart")) { // получаем минуты
                int diff = (int) ((Calendar.getInstance().getTimeInMillis() - extras.getLong("timeStart", -1)) / 1000 / 60);
                if (diff > 0) { // если больше 1 минуты, добавляем
                    setTimePicker(diff);
                } else { // иначе выводим предупреждение
                    setTimePicker(0);
                    Snackbar.make(mTimePicker, "Продолжительность меньше минуты", Snackbar.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void setTimePicker(int timeMinutes) {
        int minutesBuffer = timeMinutes;
        int hoursBuffer = 0;
        if (minutesBuffer >= 60) {
            hoursBuffer = minutesBuffer / 60;
            minutesBuffer = minutesBuffer % 60;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mTimePicker.setHour(hoursBuffer);
            mTimePicker.setMinute(minutesBuffer);
        } else {
            mTimePicker.setCurrentHour(hoursBuffer);
            mTimePicker.setCurrentMinute(minutesBuffer);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode > 0) {
           setTimePicker(requestCode);
        } else {
            setTimePicker(0);
            Snackbar.make(mTimePicker, "Продолжительность меньше минуты", Snackbar.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_add_complete_task:
                dbHelperC = new DBHelperC(context);
                dbC = dbHelperC.getWritableDatabase(); //создаем объект для убравления БД

//                mark = tvMark.getText().toString();
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
//                float markFloat = Integer.valueOf(mark);
                float marks = minutesInt * mark / 60;
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
            case R.id.btnStartStopwatch:

                Context context = getApplicationContext();

                Intent notificationIntent = new Intent(context, AddCompleteEvent.class).putExtra("timeStart", Calendar.getInstance().getTimeInMillis());
                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent contentIntent = PendingIntent.getActivity(context,
                        0, notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                Notification.Builder builder = new Notification.Builder(context);

                builder.setContentIntent(contentIntent)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setWhen(System.currentTimeMillis())
                        .setAutoCancel(true)
                        .setPriority(Notification.PRIORITY_MAX)
                        .setUsesChronometer(true)
                        .setContentTitle(name)
                        .setOngoing(true)
                        .setContentText("Нажмите для завершения"); // Текст уведомления

                Notification notification = builder.build();
                notification.defaults = Notification.DEFAULT_VIBRATE;

                NotificationManager notificationManager = (NotificationManager) context
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(101, notification);
















//                PendingIntent pi;
//                Intent intent;
//
//                // Создаем PendingIntent для Task1
//                pi = createPendingResult(TASK_CODE, new Intent(), 0);
//                // Создаем Intent для вызова сервиса, кладем туда параметр времени
//                // и созданный PendingIntent
//                intent = new Intent(this, ServiceNotification.class).putExtra(PARAM_PINTENT, pi).putExtra("itemId", itemId);
//                // стартуем сервис
//                startService(intent);
//                setTimePicker(0);



//                startService(new Intent(this, ServiceNotification.class).putExtra("itemId", itemId));



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
                break;
//            case R.id.ibPause: //остановка




//                ibStop.setVisibility(View.INVISIBLE);
//                timeWhenStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
//
//                String[] arr = chronometer.getText().toString().split(":");
//                int minutesBuffer = Integer.valueOf(arr[1]);
//                int hoursBuffer = Integer.valueOf(arr[0]);
//
//                if (minutesBuffer >= 60) {
//                    hoursBuffer = minutesBuffer / 60;
//                    minutesBuffer = minutesBuffer % 60;
//                }
//                mTimePicker.setCurrentHour(hoursBuffer);
//                mTimePicker.setCurrentMinute(minutesBuffer);
//                chronometer.stop();
//                chronometer.setBase(SystemClock.elapsedRealtime());
//                timeWhenStopped = 0;
//                chronometer.setVisibility(View.INVISIBLE);
//                ibPlay.setImageResource(R.mipmap.ic_play);
//                isPlay = false;
//                break;
        }
    }

    protected void initializeComponents() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        helpClass = new HelpClass();

        tvMark = (TextView) findViewById(R.id.tvMark);
//        tvType = (TextView) findViewById(R.id.tvType);
        tvBenefit = (TextView) findViewById(R.id.tvBenefit);
        tvBenefitHint = (TextView) findViewById(R.id.tvBenefitHint);
        mTimePicker = (TimePicker) findViewById(timePicker);
        mTimePicker.setIs24HourView(true);
        setTimePicker(0);
        btnStartStopwatch = (Button) findViewById(R.id.btnStartStopwatch);
        btnStartStopwatch.setOnClickListener(this);
//        ibStop = (ImageButton) findViewById(R.id.ibPause);
//        ibStop.setOnClickListener(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_complete_task);
        fab.setOnClickListener(this);
    }
}
