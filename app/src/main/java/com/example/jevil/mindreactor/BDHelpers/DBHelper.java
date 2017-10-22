package com.example.jevil.mindreactor.BDHelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/** Класс служит для обращения к БД со списком событий
 */
public class DBHelper  extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "tasksDB";
    public static final String TABLE_TASKS = "tasks";
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_MARK = "mark";
    public static final String KEY_TYPE = "type";
    public static final String KEY_BENEFIT = "benefit";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { //вызывается при создании базы данных
        db.execSQL("create table " + TABLE_TASKS + "("
                + KEY_ID + " integer primary key,"
                + KEY_NAME + " text,"
                + KEY_TYPE + " text,"
                + KEY_BENEFIT + " text,"
                + KEY_MARK + " integer" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { //вызывается при изменении версии базы данных
        db.execSQL("drop table if exists " + TABLE_TASKS); //удаляем текущую таблицу
        onCreate(db); //создаем новую заново
    }
}
