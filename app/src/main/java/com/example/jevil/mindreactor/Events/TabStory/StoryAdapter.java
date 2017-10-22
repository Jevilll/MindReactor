package com.example.jevil.mindreactor.Events.TabStory;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.jevil.mindreactor.BDHelpers.DBHelperC;
import com.example.jevil.mindreactor.Events.TabEvents.AddCompleteEvent;
import com.example.jevil.mindreactor.Listeners.LongClickListener;
import com.example.jevil.mindreactor.Listeners.OnClickListener;
import com.example.jevil.mindreactor.Other.HelpClass;
import com.example.jevil.mindreactor.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class StoryAdapter  extends RecyclerView.Adapter<StoryHolder> {
    public ArrayList<StoryListItem> data;
    private ArrayList<StoryListItem> mCleanCopyData;
    private Context context;
    private DBHelperC dbHelperC;
    private SQLiteDatabase dbC;
    private Date date;
    private int id;
    private StoryListItem itemBufferForDelete;
    private int currentPosition;
    private ViewGroup vg;
    private SimpleDateFormat sdf;
    private boolean isDelete = true;
    private HelpClass helpClass;

    public StoryAdapter(ArrayList<StoryListItem> items, Context context) {
        this.context = context;
        getData(items);
        sdf = new SimpleDateFormat("d MMMM yyyy hh:mm aaa");
        helpClass = new HelpClass();
    }

    @Override
    public StoryHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_story, viewGroup, false);
        StoryHolder holder = new StoryHolder(v);
        vg = viewGroup;
        return holder;
    }

    @Override
    public void onBindViewHolder(StoryHolder holder, final int position) {
        holder.tvName.setText(data.get(position).getName());
        holder.tvMin.setText(helpClass.getRightTime(data.get(position).getMinutes()));
        holder.tvMarks.setText(helpClass.getMarkFormatString(data.get(position).getMakrs()));
        if (data.get(position).getBenefit().equals("useful")) {
            holder.ivBenefit.setBackgroundColor(Color.GREEN);
        } else if (data.get(position).getBenefit().equals("lifeTaxes")) {
            holder.ivBenefit.setBackgroundColor(Color.GRAY);
        }
        holder.tvTime.setText(sdf.format(data.get(position).getTime()));

        if (data.get(position).getFirstRecord().equals("")) {
            holder.tvDate.setVisibility(View.GONE);
        } else {
            holder.tvDate.setText(data.get(position).getFirstRecord());
            holder.tvDate.setVisibility(View.VISIBLE);
        }

        // слушатели списка
        holder.setLongClickListener(new LongClickListener() {
            @Override
            public void onItemLongClick(int pos) {
                id = data.get(pos).getBDitemID();
                currentPosition = pos;
                StoryListItem.selectedItem = data.get(position);
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
            case 2://изменение
                StoryListItem item = data.get(currentPosition);
                Intent intent = new Intent();
                intent.setClass(context, AddCompleteEvent.class);
                intent.putExtra("type", "edit");
                intent.getIntExtra("id", item.getBDitemID());
                StoryListItem.selectedItem = item;
                context.startActivity(intent);
                break;
            case 3://удаление
                removeAt(id, currentPosition);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void itemClick(int position) {
    }


    public void deleteStoryEvent(int id) {
        dbHelperC = new DBHelperC(context);
        dbC = dbHelperC.getWritableDatabase(); //создаем объект для убравления БД
        dbC.delete(DBHelperC.TABLE_C_TASKS, DBHelperC.KEY_ID + "=" + id, null);
    }

    public void removeAt(final int id, final int position) {
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
        itemBufferForDelete = data.get(position);
        data.remove(position);

        Snackbar snackbar = Snackbar
                // с текстовой компоновкой "Вы изменили что-то"
                .make(vg, "Удалено", Snackbar.LENGTH_LONG)
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
                    deleteStoryEvent(id);
                }
                //
            }

            @Override
            public void onShown(Snackbar snackbar) {
            }
        });
    }

    public void getData(ArrayList<StoryListItem> items) {
        this.data = items;
        mCleanCopyData = data;
    }
}