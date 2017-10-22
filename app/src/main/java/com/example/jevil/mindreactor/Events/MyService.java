package com.example.jevil.mindreactor.Events;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;

import com.example.jevil.mindreactor.BDHelpers.DBHelperC;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {
    final String LOG_TAG = "myLogs";
    private Cursor c;
    Intent intent;
    Context context;
    private Timer mTimer;

    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        this.intent = intent;
        mTimer = new Timer();
        MyTimerTask mMyTimerTask = new MyTimerTask();
        mTimer.schedule(mMyTimerTask, 1000 * 60 * 60, 1000 * 60 * 60);
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    private class MyTimerTask extends TimerTask {
        private DBHelperC dbHelperC;
        private SQLiteDatabase dbC;
        @Override
        public void run() {
            dbHelperC = new DBHelperC(context);
            dbC = dbHelperC.getWritableDatabase();
            ContentValues contentValues = new ContentValues(); // добавляем строки в таблицу (ключ - значение)
            contentValues.put(DBHelperC.KEY_NAME, "Налог на жизнь"); // название
            contentValues.put(DBHelperC.KEY_MARK, 1); // балл за час или разово
            contentValues.put(DBHelperC.KEY_TYPE, "cont"); // тип, продолжительное или разовое
            contentValues.put(DBHelperC.KEY_MINUTES, 60); // количество минут
            contentValues.put(DBHelperC.KEY_BENEFIT, "lifeTaxes");  // польза или вред
            contentValues.put(DBHelperC.KEY_TIME, Calendar.getInstance().getTimeInMillis());
            contentValues.put(DBHelperC.KEY_MARKS, 1); // сумма баллов
            dbC.insert(DBHelperC.TABLE_C_TASKS, null, contentValues); //добавление данных в БД
        }
    }
}
