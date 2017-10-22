package com.example.jevil.mindreactor.BDHelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/** Класс служит для обращения к БД со списком выполненных событий
 */

public class DBHelperC extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "cTasksDB";
    public static final String TABLE_C_TASKS = "completeTasks";
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_MARK = "mark";
    public static final String KEY_TYPE = "type";
    public static final String KEY_TIME = "time";
    public static final String KEY_MINUTES = "minutes";
    public static final String KEY_MARKS = "marks";
    public static final String KEY_BENEFIT = "benefit";

    public DBHelperC(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { //вызывается при создании базы данных
        db.execSQL("create table " + TABLE_C_TASKS + "("
                + KEY_ID + " integer primary key,"
                + KEY_NAME + " text,"
                + KEY_TYPE + " text,"
                + KEY_MINUTES + " integer,"
                + KEY_TIME + " integer,"
                + KEY_MARKS + " real,"
                + KEY_MARK + " integer,"
                + KEY_BENEFIT + " text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { //вызывается при изменении версии базы данных
        db.execSQL("drop table if exists " + TABLE_C_TASKS); //удаляем текущую таблицу
        onCreate(db); //создаем новую заново
    }
}