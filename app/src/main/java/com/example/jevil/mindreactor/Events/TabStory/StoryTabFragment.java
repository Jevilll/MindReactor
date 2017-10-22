package com.example.jevil.mindreactor.Events.TabStory;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.jevil.mindreactor.BDHelpers.DBHelperC;
import com.example.jevil.mindreactor.Other.HelpClass;
import com.example.jevil.mindreactor.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Jevil on 23.10.2016.
 */

public class StoryTabFragment extends Fragment{

    public Context context;
    ArrayList<StoryListItem> items;
    RecyclerView rv;
    String dateForDivider = "";
    StoryAdapter storyAdapter;
    SimpleDateFormat sdfDate;
    HelpClass helpClass;
    Calendar calendar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sdfDate = new SimpleDateFormat("d MMMM yyyy");
        helpClass = new HelpClass();
        calendar = Calendar.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_story, container, false);
        rv = (RecyclerView) v.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        rv.setItemAnimator(new DefaultItemAnimator());
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        initList();
        refreshList();
    }

    // генерируем данные для адаптера
    public void initList() {
        items = new ArrayList<>();
        DBHelperC dbHelperC = new DBHelperC(this.getContext());
        SQLiteDatabase db = dbHelperC.getWritableDatabase();
        Cursor c = db.query(DBHelperC.TABLE_C_TASKS, null, null, null, null, null, null);
        if (c.moveToLast()) {
            do {
                calendar.setTimeInMillis(c.getLong(c.getColumnIndex("time")));
                String firstRecord;
                if (sdfDate.format(helpClass.getStartOfTheDay(calendar).getTimeInMillis()).equals(dateForDivider)) {
                    firstRecord = "";
                } else {
                    firstRecord = sdfDate.format(helpClass.getStartOfTheDay(calendar).getTimeInMillis());
                    dateForDivider = sdfDate.format(helpClass.getStartOfTheDay(calendar).getTimeInMillis());
                }
                items.add(new StoryListItem(c.getString(c.getColumnIndex("name")),
                        c.getInt(c.getColumnIndex("mark")),
                        c.getInt(c.getColumnIndex("_id")),
                        c.getString(c.getColumnIndex("type")),
                        c.getInt(c.getColumnIndex("minutes")),
                        c.getLong(c.getColumnIndex("time")),
                        c.getFloat(c.getColumnIndex("marks")),
                        c.getString(c.getColumnIndex("benefit")),
                        firstRecord));
            } while (c.moveToPrevious());
        } else
            c.close();
    }
    /**
     * Обновляем список
     */
    public void refreshList() {
//        // Создаем адаптер данных
        storyAdapter = new StoryAdapter(items, this.getContext());
        rv.setAdapter(storyAdapter);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        storyAdapter.getItemSelected(item);
        return super.onContextItemSelected(item);
    }
}

