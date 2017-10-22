package com.example.jevil.mindreactor.Events.TabEvents;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.jevil.mindreactor.BDHelpers.DBHelper;
import com.example.jevil.mindreactor.Events.TabEvents.EventsList.EventsAdapter;
import com.example.jevil.mindreactor.Events.TabEvents.EventsList.EventsItem;
import com.example.jevil.mindreactor.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class EventsTabFragment extends Fragment {
    public Context context;
    ArrayList<EventsItem> items;
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private Cursor c;
    private boolean sortTap = true;
    RecyclerView rv;
    EventsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

 //       HelpClass bonus = new HelpClass();
 //       bonus.getLastDateLifeTaxes(getActivity());
//        bonus.initList(getActivity());

        View v = inflater.inflate(R.layout.fragment_events, container, false);

        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(getContext());
        //adapter = new EventsAdapter(items, getContext());
        rv = (RecyclerView) v.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        rv.setItemAnimator(new DefaultItemAnimator());
        context = getActivity();
        //rv.setAdapter(adapter);
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab_add_new_task);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(context, AddNewEventActivity.class);
                    intent.putExtra("type", "add");
                    startActivity(intent);
            }
        });
        initList();
        refreshList();
        return v;
    }

    // генерируем данные для адаптера
    void initList() {
        items = new ArrayList<EventsItem>();
        // Заполняем список
        db = dbHelper.getWritableDatabase();
        c = db.query(DBHelper.TABLE_TASKS, null, null, null, null, null, null);
        // dbHelper.onUpgrade(db, 1, 1); //удалить БД
        // Ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToLast()) {
            //tvMessage.setVisibility(View.GONE);
            do {
                items.add(new EventsItem(c.getString(c.getColumnIndex("name")),
                        c.getInt(c.getColumnIndex("mark")),
                        //      c.getInt(c.getColumnIndex("type")),
                        c.getInt(c.getColumnIndex("_id")),
                        c.getString(c.getColumnIndex("benefit")),
                        c.getString(c.getColumnIndex("type"))));
                // Переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToPrevious());
        } else
            // tvMessage.setVisibility(View.VISIBLE);
            c.close();
    }

    @Override
    public void onResume() {
        super.onResume();
        initList();
        refreshList();
    }



    /**
     * Обновляем список
     */
    public void refreshList() {

        adapter = new EventsAdapter(items, this.getContext());
        rv.setAdapter(adapter);
    }

    public void itemsSort() {//сортировка
        Collections.sort(items, new Comparator<EventsItem>() {
            @Override
            public int compare(EventsItem o1, EventsItem o2) {
                return o1.getName().compareTo(o2.getName());
            }

        });
        refreshList();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_activity_main_toolbar, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return false;
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sort) {
            if (sortTap) {
                itemsSort();
                sortTap = false;
            } else {
                initList();
                refreshList();
                sortTap = true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        adapter.getItemSelected(item);
        return super.onContextItemSelected(item);
    }
}
