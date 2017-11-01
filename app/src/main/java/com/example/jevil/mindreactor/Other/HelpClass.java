package com.example.jevil.mindreactor.Other;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.jevil.mindreactor.BDHelpers.DBHelperC;

import java.util.Calendar;

/**
 * Класс служит для хранения вспомогательных методов
 */
public class HelpClass {

    private int hoursBegin = 4, minutesBegin = 0, year, month, day, hour, mimute; // начало дня, взять из настроек
    private DBHelperC dbHelperC;
    private SQLiteDatabase dbC;

    /**
     * Получить форматированную строку для вывода баллов
     *
     * @return возвращает форматированную строку
     */
    public String getMarkFormatString(double mark) {


        double result = Math.round(mark * 10.0) / 10.0;
        int intPart = (int) result;
        double fracPart = result - intPart;
        if (fracPart == 0) {
            return String.valueOf(intPart);
        } else {
            return String.valueOf(result);
        }
    }

    /**
     * Получить начало дня
     *
     * @return возвращает начало в формате Calendar
     */
    public Calendar getStartOfTheDay(Calendar calendar) {

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        mimute = calendar.get(Calendar.MINUTE);

        calendar.set(year, month, day, hoursBegin, minutesBegin);
        if ((hour >= 24) && (mimute >= 1) || (hour <= 3) && (mimute <= 59)) {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }
        return calendar;
    }

    /**
     * Минус неделя
     *
     * @return возвращает начало дня
     */
    public Calendar getWeek(Calendar calendar) {
        calendar.add(Calendar.DAY_OF_WEEK, -7);
        return getStartOfTheDay(calendar);
    }

    /**
     * Минус месяц
     *
     * @return возвращает разность дат в миллисекундах
     */
    public Calendar getMonth(Calendar calendar) {
        calendar.add(Calendar.MONTH, -1);
        return getStartOfTheDay(calendar);
    }

    /**
     * Минус сутки
     *
     * @return возвращает разность дат в миллисекундах
     */
    public Calendar getYesterday(Calendar calendar) {
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return getStartOfTheDay(calendar);
    }

    /**
     * Получить разность дат
     *
     * @return возвращает разность дат в часах
     */
    public long getDiffDateHours(long millis1, long millis2) {
        return (int) ((millis1 - millis2) / (60 * 60 * 1000));
    }

    public long getDiffDateMinutes(long millis1, long millis2) {
        return (int) ((millis1 - millis2) / (60 * 1000));
    }

    public long getDateWithoutSeconds(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        mimute = calendar.get(Calendar.MINUTE);
        calendar.set(year, month, day, hoursBegin, minutesBegin, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * Сделать запрос в БД и получить баланс баллов
     *
     * @return возвращает баланс
     */
    public String setMainMark(Context context) {
        dbHelperC = new DBHelperC(context);
        dbC = dbHelperC.getWritableDatabase(); //создаем объект для убравления БД
        String[] columns;
        String groupBy;
        groupBy = "benefit";
        double benUseful = 0, benHarmful = 0;
        columns = new String[]{"benefit", "sum(marks) as marks"};
        Cursor cursor = dbC.query(DBHelperC.TABLE_C_TASKS, columns, null, null, groupBy, null, null); //чтение данных из БД
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    if (cursor.getString(cursor.getColumnIndex("benefit")).equals("useful")) {
                        benUseful = benUseful + cursor.getFloat(cursor.getColumnIndex("marks"));
                        Log.d("mLog", "useful " + benUseful);
                    } else {
                        benHarmful = benHarmful + cursor.getFloat(cursor.getColumnIndex("marks"));
                        Log.d("mLog", "harmful " + benHarmful);
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
            double mainMark = benUseful - benHarmful;
            return "Баланс: " + getMarkFormatString(mainMark);
        } else return "error";
    }

    public void recalculateLifeTaxes(Context context) {
        //items.clear();
        // Заполняем список
        dbHelperC = new DBHelperC(context);
        dbC = dbHelperC.getWritableDatabase();
        String selection = "name = ?";
        String[] selectionArgs = new String[] { "Налог на жизнь" };
        Cursor c = dbC.query(DBHelperC.TABLE_C_TASKS, null, selection, selectionArgs, null, null, null);
        c.moveToLast();
        if (c.getCount() != 0) {
            if (getDiffDateMinutes(Calendar.getInstance().getTimeInMillis(), c.getLong(c.getColumnIndex("time"))) >= 240) { // заносим данные только когда больше 4 часов
                long lifeTaxesMinutes = getDiffDateMinutes(Calendar.getInstance().getTimeInMillis(), c.getLong(c.getColumnIndex("time")));
                float mark = 2;
                dbHelperC = new DBHelperC(context);
                dbC = dbHelperC.getWritableDatabase();
                ContentValues contentValues = new ContentValues(); // добавляем строки в таблицу (ключ - значение)
                contentValues.put(DBHelperC.KEY_NAME, "Налог на жизнь"); // название
                contentValues.put(DBHelperC.KEY_MARK, mark); // балл за час или разово
                contentValues.put(DBHelperC.KEY_TYPE, "cont"); // тип, продолжительное или разовое
                contentValues.put(DBHelperC.KEY_MINUTES, lifeTaxesMinutes); // количество минут
                contentValues.put(DBHelperC.KEY_BENEFIT, "lifeTaxes");  // польза или вред
                contentValues.put(DBHelperC.KEY_TIME, Calendar.getInstance().getTimeInMillis());

                float marks = lifeTaxesMinutes * mark / 240;
                contentValues.put(DBHelperC.KEY_MARKS, marks); // сумма баллов
                dbC.insert(DBHelperC.TABLE_C_TASKS, null, contentValues); //добавление данных в БД
            }
        } else { // при первом запуске
            dbHelperC = new DBHelperC(context);
            dbC = dbHelperC.getWritableDatabase();
            ContentValues contentValues = new ContentValues(); // добавляем строки в таблицу (ключ - значение)
            float mark = 2;
            contentValues.put(DBHelperC.KEY_NAME, "Налог на жизнь"); // название
            contentValues.put(DBHelperC.KEY_MARK, 2); // балл за час или разово
            contentValues.put(DBHelperC.KEY_TYPE, "cont"); // тип, продолжительное или разовое
            contentValues.put(DBHelperC.KEY_MINUTES, 240); // количество минут
            contentValues.put(DBHelperC.KEY_BENEFIT, "lifeTaxes");  // польза или вред
            contentValues.put(DBHelperC.KEY_TIME, Calendar.getInstance().getTimeInMillis());
            float marks = 60 * mark / 60;
            contentValues.put(DBHelperC.KEY_MARKS, marks); // сумма баллов
            dbC.insert(DBHelperC.TABLE_C_TASKS, null, contentValues); //добавление данных в БД
        }
        c.close();
    }

    public String getRightHourString(float num) {
        String name = "";
        if ((num >= 11 ) && (num <= 14)) {
            name = " часов";
        } else if (num % 10 == 1) {
            name = " час";
        } else if ((num % 10 >= 2) && (num % 10 <= 4)) {
            name = " часа";
        } else if ((num % 10 >= 5) || (num % 10 == 0)) {
            name = " часов";
        }
        return name;
    }

    public String getRightMinutesString(float num) {
        String name = "";
        if ((num >= 11 ) && (num <= 14)) {
            name = " минут";
        } else if (num % 10 == 1) {
            name = " минута";
        } else if ((num % 10 >= 2) && (num % 10 <= 4)) {
            name = " минуты";
        } else if ((num % 10 >= 5) || (num % 10 == 0)) {
            name = " минут";
        }
        return name;
    }

    public String getRightTime(int minutes) {
        String min = String.valueOf(minutes);
        if (min.equals("0")) {
            min = "Разовое действие";
        } else {
            int minBuffer = Integer.valueOf(min);
            if (minBuffer >= 60) {
                minBuffer = minBuffer / 60;
                int minutesWithoutHours = Integer.valueOf(min) % 60;
                if (minutesWithoutHours != 0) {
                    min = "" + minBuffer + getRightHourString(minBuffer) + " " + minutesWithoutHours + getRightMinutesString(minutesWithoutHours);
                } else {
                    min = "" + minBuffer + getRightHourString(minBuffer);
                }
            } else {
                min = "" + min + getRightMinutesString(Integer.valueOf(min));
            }
        }
        return min;
    }

    public String getRightMarkString(float num) {
        String name = "";
        if (num % 10 == 1) {
            name = " балл";
        } else if ((num % 10 >= 2) && (num % 10 <= 4)) {
            name = " балла";
        } else if ((num % 10 >= 5) || (num % 10 == 0)) {
            name = " баллов";
        }
        return name;
    }

    public String getRightOnce(int num) {
        String name;
        if ((num % 10 >= 2) && (num % 10 <= 4)) {
            name = " раза";
        } else {
            name = " раз";
        }
        return num + " " + name;
    }
}
