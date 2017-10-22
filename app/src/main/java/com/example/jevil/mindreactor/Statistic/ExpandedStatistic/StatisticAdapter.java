package com.example.jevil.mindreactor.Statistic.ExpandedStatistic;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jevil.mindreactor.BDHelpers.DBHelperC;
import com.example.jevil.mindreactor.Events.TabEvents.EventsList.EventsItem;
import com.example.jevil.mindreactor.Listeners.OnClickListener;
import com.example.jevil.mindreactor.Other.HelpClass;
import com.example.jevil.mindreactor.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Jevil on 16.11.2016.
 */

public class StatisticAdapter extends RecyclerView.Adapter<StatisticHolder> {
    final String LOG_TAG = "myLogs";
    public ArrayList<StatisticListItem> data;
    ArrayList<StatisticListItem> mCleanCopyData;
    Context context;
    private DBHelperC dbHelperC;
    private SQLiteDatabase dbC;
    Date date;
    int id;
    StatisticListItem item;
    StatisticListItem itemBufferForDelete;
    int currentPosition;
    ViewGroup vg;
    SimpleDateFormat sdf, sdfDate;
    boolean isDelete = true;

    HelpClass helpClass;
    private int SEPARATOR = 0;
    private final int ITEM = 1;
    //private DBHelper dbHelper;
    long previousTime = 0;
    String dateForDivider = "";

    public StatisticAdapter(ArrayList<StatisticListItem> items, Context context) {
        this.context = context;
        getData(items);
        sdf = new SimpleDateFormat("d MMMM yyyy hh:mm aaa");
        sdfDate = new SimpleDateFormat("d MMMM yyyy");
        helpClass = new HelpClass();

    }

    @Override
    public StatisticHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_statistic, viewGroup, false);
        StatisticHolder holder = new StatisticHolder(v);
        vg = viewGroup;
//        if (SEPARATOR == 0) {
//            addSeparator(viewGroup);
//        }
        return holder;
    }
//
//    public StoryHolder addSeparator(ViewGroup viewGroup) {
//        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_story, viewGroup, false);
//        StoryHolder holder = new StoryHolder(v);
//        vg = viewGroup;
//        SEPARATOR = 1;
//        return  holder;
//    }

//    @Override
//    public int getItemViewType(int pos) {
//        if (data.get(pos).getTime() != previousTime) {
//            previousTime = data.get(pos).getTime();
//            return SEPARATOR;
//        } else {
//
//        }
//        return pos == 0 ? SEPARATOR : ITEM; //0:1
//    }

    @Override
    public void onBindViewHolder(StatisticHolder holder, final int position) {
        holder.tvName.setText(data.get(position).getName());

        holder.tvMin.setText(helpClass.getRightTime(data.get(position).getMinutes()));
      //  double markDoub = helpClass.markFormat(data.get(position).getMakrs());
        holder.tvMarks.setText(helpClass.getMarkFormatString(data.get(position).getMakrs()));
        if (data.get(position).getBenefit().equals("useful")) {
            holder.ivBenefit.setBackgroundColor(Color.GREEN);
        } else if (data.get(position).getBenefit().equals("lifeTaxes")) {
            holder.ivBenefit.setBackgroundColor(Color.GRAY);
        }
//        holder.tvTime.setText(sdf.format(data.get(position).getTime()));

//        if (data.get(position).getFirstRecord().equals("")) {
//            holder.tvDate.setVisibility(View.GONE);
//        } else {
//            holder.tvDate.setText(data.get(position).getFirstRecord());
//            holder.tvDate.setVisibility(View.VISIBLE);
//        }

        // слушатели списка
//        holder.setLongClickListener(new LongClickListener() {
//            @Override
//            public void onItemLongClick(int pos) {
//                id = data.get(pos).getBDitemID();
//                currentPosition = pos;
//                StatisticListItem.selectedItem = data.get(position);
//                //Toast.makeText(context, name, Toast.LENGTH_SHORT).show();
//            }
//        });


            holder.setOnClickListener(new OnClickListener() {
                @Override
                public void onItemClick(int pos) {
                    itemClick(pos);
                }
            });


    }

//    public void getItemSelected(MenuItem itemMenu) {
//
//        switch (itemMenu.getItemId()){
//            case 2://изменение
////                Intent intent = new Intent();
////                intent.setClass(context, AddCompleteEvent.class);
////                intent.putExtra("type", "edit");
////                item = data.get(currentPosition);
////                StatisticListItem.selectedItem = item;
////                context.startActivity(intent);
//                break;
//            case 3://удаление
////                removeAt(id, currentPosition);
//                break;
//        }
//    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void itemClick(int position) {
//        item = data.get(position);
//        StoryListItem.selectedItem = item;
////        if (item.getBenefit().equals("lifeTaxes")) {
////            Toast.makeText(context, "")
////        } else
//            if (item.getType().equals("cont")) { // открываем активити добавления выполненной задачи
//            Intent intent = new Intent();
//            intent.setClass(context, AddCompleteEvent.class);
//            intent.putExtra("_id", item.getBDitemID());
//            context.startActivity(intent);
//        } else {
//            date = new Date();
//            dbHelperC = new DBHelperC(context);
//            dbC = dbHelperC.getWritableDatabase(); //создаем объект для убравления БД
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder.setTitle("Подтверждение");
//            builder.setMessage("Точно?");
//            builder.setCancelable(true);
//
//            builder.setPositiveButton("Точняк", new DialogInterface.OnClickListener() { // Кнопка ОК
//                @Override
//                public void onClick(DialogInterface dialog, int id) {
//                    //addOneTimeTask(item_event);
//                    dialog.dismiss(); // Отпускает диалоговое окно
//                }
//            });
//            builder.setNegativeButton("Неа", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int id) {
//                    dialog.dismiss(); // Отпускает диалоговое окно
//                }
//            });
//
//            AlertDialog dialog = builder.create();
//            dialog.show();
//        }
    }

    public void addOneTimeTask(EventsItem item) {
        long millis = date.getTime();
        ContentValues contentValues = new ContentValues(); // добавляем строки в таблицу (ключ - значение)
        contentValues.put(dbHelperC.KEY_NAME, item.getName());
        contentValues.put(dbHelperC.KEY_MARK, item.getMark());
        contentValues.put(dbHelperC.KEY_TYPE, item.getType());
        contentValues.put(dbHelperC.KEY_BENEFIT, item.getBenefit());
        contentValues.put(dbHelperC.KEY_TIME, millis);
        contentValues.put(dbHelperC.KEY_MARKS, item.getMark());
        dbC.insert(dbHelperC.TABLE_C_TASKS, null, contentValues); //добавление данных в БД
    }

    public void deleteStoryEvent(int id, int position) {
        dbHelperC = new DBHelperC(context);
        dbC = dbHelperC.getWritableDatabase(); //создаем объект для убравления БД
        dbC.delete(dbHelperC.TABLE_C_TASKS, dbHelperC.KEY_ID + "=" + id, null);
    }

//    public void filter(String charText) {
//        charText = charText.toLowerCase(Locale.getDefault());
//        data = new ArrayList<a>();
//        if (charText.length() == 0) {
//            data.addAll(mCleanCopyData);
//        } else {
//            for (StoryListItem item : mCleanCopyData) {
//                if (item.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
//                    data.add(item);
//                }
//            }
//        }
//        notifyDataSetChanged();
//    }
//    public void removeAt(final int id, final int position) {
//        notifyItemRemoved(position);
//        notifyItemRangeChanged(position, getItemCount());
//        itemBufferForDelete = data.get(position);
//        data.remove(position);
//
//        Snackbar snackbar = Snackbar
//                // с текстовой компоновкой "Вы изменили что-то"
//                .make(vg, "Удалено", Snackbar.LENGTH_LONG)
//                // и кнопкой "Вернуть как было?"
//                .setAction("Отмена", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        // показываем сообщение "Все вернулось на свои места!"
//                        Snackbar.make(
//                                vg,
//                                "Вернул!",
//                                Snackbar.LENGTH_SHORT
//                        ).show();
//                        isDelete = false;
//                        data.add(itemBufferForDelete);
//                        notifyItemInserted(position);
//
//                    }
//                });
//
//        snackbar.show();
//        snackbar.setCallback(new Snackbar.Callback() {
//            @Override
//            public void onDismissed(Snackbar snackbar, int event) {
//                // do some action on dismiss
//                //Toast.makeText(context, "Dismiss", Toast.LENGTH_SHORT).show();
//                if (isDelete) {
//                    deleteStoryEvent(id, position);
//                }
//                //
//            }
//
//            @Override
//            public void onShown(Snackbar snackbar) {
//                // do some action when snackbar is showed
//                //Toast.makeText(context, "Snackbar is showed", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    public void getData(ArrayList<StatisticListItem> items) {
        this.data = items;
        mCleanCopyData = data;
    }


}