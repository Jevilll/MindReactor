package com.example.jevil.mindreactor.Events.TabEvents.EventsList;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.jevil.mindreactor.BDHelpers.DBHelper;
import com.example.jevil.mindreactor.BDHelpers.DBHelperC;
import com.example.jevil.mindreactor.Events.TabEvents.AddCompleteEvent;
import com.example.jevil.mindreactor.Events.TabEvents.AddNewEventActivity;
import com.example.jevil.mindreactor.Listeners.LongClickListener;
import com.example.jevil.mindreactor.Listeners.OnClickListener;
import com.example.jevil.mindreactor.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class EventsAdapter extends RecyclerView.Adapter<EventsHolder> {

    public ArrayList<EventsItem> data;
    private ArrayList<EventsItem> mCleanCopyData;
    private Context context;
    private Date date;
    private int id;
    private EventsItem item, itemBufferForDelete;
    private int currentPosition;
    private ViewGroup vg;
    private boolean isDelete = true;

    public EventsAdapter(ArrayList<EventsItem> items, Context context) {
        this.context = context;
        getData(items);
    }

    @Override
    public EventsHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_event, viewGroup, false);
        EventsHolder holder = new EventsHolder(v);
        vg = viewGroup;
        return holder;
    }

    @Override
    public void onBindViewHolder(EventsHolder holder, final int position) {
        holder.tvName.setText(data.get(position).getName());
        holder.tvMark.setText(String.valueOf(data.get(position).getMark()));
        if (data.get(position).getBenefit().equals("useful")) {
            holder.ivBenefit.setBackgroundColor(Color.GREEN);
        }
        // слушатель списка
        holder.setLongClickListener(new LongClickListener() {
            @Override
            public void onItemLongClick(int pos) {
                id = data.get(pos).getBDitemID();
                currentPosition = pos;
            }
        });
        holder.setOnClickListener(new OnClickListener() {
            @Override
            public void onItemClick(int pos) {
                itemClick(pos);
            }
        });
    }

    public void getItemSelected(MenuItem itemMenu) {
        switch (itemMenu.getItemId()){
            case 0://изменение
                Intent intent = new Intent();
                intent.setClass(context, AddNewEventActivity.class);
                intent.putExtra("type", "edit");
                intent.putExtra("id", data.get(currentPosition).getBDitemID());
                item = data.get(currentPosition);
                EventsItem.selectedItem = item;
                context.startActivity(intent);
                break;
            case 1://удаление
                removeAt(id, currentPosition);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void itemClick(int position) {
        item = data.get(position);
        EventsItem.selectedItem = item;
        if (item.getType().equals("cont")) { // открываем активити добавления выполненной задачи
            Intent intent = new Intent();
            intent.setClass(context, AddCompleteEvent.class);
            intent.putExtra("_id", item.getBDitemID());
            intent.putExtra("type", "add");
            context.startActivity(intent);
        } else {
            date = new Date();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Подтверждение");
            builder.setMessage("Точно?");
            builder.setCancelable(true);
            builder.setPositiveButton("Точняк", new DialogInterface.OnClickListener() { // Кнопка ОК
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    addOneTimeTask(item);
                    dialog.dismiss(); // Отпускает диалоговое окно
                }
            });
            builder.setNegativeButton("Неа", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss(); // Отпускает диалоговое окно
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void addOneTimeTask(EventsItem item) {
        long millis = date.getTime();
        DBHelperC dbHelperC = new DBHelperC(context);
        SQLiteDatabase dbC = dbHelperC.getWritableDatabase();
        ContentValues contentValues = new ContentValues(); // добавляем строки в таблицу (ключ - значение)
        contentValues.put(DBHelperC.KEY_NAME, item.getName());
        contentValues.put(DBHelperC.KEY_MARK, item.getMark());
        contentValues.put(DBHelperC.KEY_TYPE, item.getType());
        contentValues.put(DBHelperC.KEY_BENEFIT, item.getBenefit());
        contentValues.put(DBHelperC.KEY_TIME, millis);
        contentValues.put(DBHelperC.KEY_MINUTES, 1);
        contentValues.put(DBHelperC.KEY_MARKS, item.getMark());
        dbC.insert(DBHelperC.TABLE_C_TASKS, null, contentValues); //добавление данных в БД
    }

    private void deleteEvent(int id) {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase(); //создаем объект для убравления БД
        database.delete(DBHelper.TABLE_TASKS, DBHelper.KEY_ID + "=" + id, null);
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        data = new ArrayList<>();
        if (charText.length() == 0) {
            data.addAll(mCleanCopyData);
        } else {
            for (EventsItem item : mCleanCopyData) {
                if (item.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    data.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    private void removeAt(final int id, final int position) {
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
        itemBufferForDelete = data.get(position);
        data.remove(position);
        Snackbar snackbar = Snackbar
                // с текстовой компоновкой "Вы изменили что-то"
                .make(vg, "Задача удалена", Snackbar.LENGTH_LONG)
                // и кнопкой "Вернуть как было?"
                .setAction("Отмена", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // показываем сообщение "Все вернулось на свои места!"
                        Snackbar.make(
                                vg,
                                "Вернул!",
                                Snackbar.LENGTH_SHORT
                        ).show();
                        isDelete = false;
                        data.add(position, itemBufferForDelete);
                        notifyItemInserted(position);
                    }
                });
        snackbar.show();
        snackbar.setCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                if (isDelete) {
                    deleteEvent(id);
                }
            }
            @Override
            public void onShown(Snackbar snackbar) {
            }
        });
    }

    private void getData(ArrayList<EventsItem> items) {
        this.data = items;
        mCleanCopyData = data;
    }
}