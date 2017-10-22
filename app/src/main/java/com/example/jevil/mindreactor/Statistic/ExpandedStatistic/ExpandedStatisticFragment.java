package com.example.jevil.mindreactor.Statistic.ExpandedStatistic;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jevil.mindreactor.BDHelpers.DBHelperC;
import com.example.jevil.mindreactor.Other.HelpClass;
import com.example.jevil.mindreactor.R;

import java.util.ArrayList;
import java.util.Calendar;

public class ExpandedStatisticFragment extends Fragment {
    private DBHelperC dbHelperC;
    private SQLiteDatabase dbC;
    private int periodOfStatistic;
    String selection;
    String[] selectionArgs;
    String time, timeEnd;
    boolean isYesterday = false;
    HelpClass helpClass;
    ArrayList<StatisticListItem> items;
    final String LOG_TAG = "myLogs";
    StatisticAdapter statisticAdapter;
    RecyclerView rv;

    public ExpandedStatisticFragment(int typeOfStatistic) { //0 - сегодня, 1 - неделя, 2 - месяц, 3 - год, 4 - все время
        this.periodOfStatistic = typeOfStatistic;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_expanded_statistic, container, false);

        helpClass = new HelpClass();

        rv = (RecyclerView) v.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        rv.setItemAnimator(new DefaultItemAnimator());

        Calendar calendar = Calendar.getInstance();
        switch (periodOfStatistic) {
            case 0: // все время
                calendar.set(2017, 0, 1);
                time = String.valueOf(calendar.getTimeInMillis());
                break;
            case 1: // сегодня
                time = String.valueOf(helpClass.getStartOfTheDay(calendar).getTimeInMillis());
                Log.d("mLog", "Time " + calendar.getTime());
                break;
            case 2: // вчера
                time = String.valueOf(helpClass.getYesterday(helpClass.getStartOfTheDay(calendar)).getTimeInMillis());
                calendar.add(calendar.DAY_OF_WEEK, 1);
                timeEnd = String.valueOf(helpClass.getStartOfTheDay(calendar).getTimeInMillis());
                isYesterday = true;
                break;
            case 3: // неделя
                time = String.valueOf(helpClass.getWeek(helpClass.getStartOfTheDay(calendar)).getTimeInMillis());
                break;
            case 4: // месяц
                time = String.valueOf(helpClass.getMonth(helpClass.getStartOfTheDay(calendar)).getTimeInMillis());
                break;
        }
        setStatistic();
        refreshList();
        return v;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        items = new ArrayList<>();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void setStatistic() {

        if (!isYesterday) {
            selection = "time > ?";
            selectionArgs = new String[]{time};
        } else {
            selection = "time > ? AND time < ?";
            selectionArgs = new String[]{time, timeEnd};
            isYesterday = false;
        }

        dbHelperC = new DBHelperC(getActivity());
        dbC = dbHelperC.getWritableDatabase(); //создаем объект для убравления БД
        String[] columns = null;
        String groupBy;
        groupBy = "name";
        int usefulHours = 0, harmfulHours = 0, lifeTaxesHours = 0, usefulOnce = 0, harmfulOnce = 0;
        columns = new String[]{"name, mark, type, benefit, type", "sum(minutes) as minutes, sum(marks) as marks"};
        Cursor c = dbC.query(dbHelperC.TABLE_C_TASKS, columns, selection, selectionArgs, groupBy, null, null); //чтение данных из БД

        if (c.moveToLast()) {
            //tvMessage.setVisibility(View.GONE);
            do {
                items.add(new StatisticListItem(c.getString(c.getColumnIndex("name")),
                        c.getInt(c.getColumnIndex("mark")),
                        c.getString(c.getColumnIndex("type")),
                        c.getInt(c.getColumnIndex("minutes")),
                        c.getFloat(c.getColumnIndex("marks")),
                        c.getString(c.getColumnIndex("benefit"))));
                //sdf.format(longDate);
                //Log.d("mLog", sdf.format(c.getLong(c.getColumnIndex("time"))));
                // Переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToPrevious());
        } else
            // tvMessage.setVisibility(View.VISIBLE);
            Log.d(LOG_TAG, "0 rows");
            c.close();

    }


    /**
     * Обновляем список
     */
    public void refreshList() {

//        // Создаем адаптер данных
        statisticAdapter = new StatisticAdapter(items, this.getContext());
        rv.setAdapter(statisticAdapter);
    }

}
