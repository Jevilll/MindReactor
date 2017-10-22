package com.example.jevil.mindreactor.Events.TabEvents.EventsList;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jevil.mindreactor.Listeners.LongClickListener;
import com.example.jevil.mindreactor.Listeners.OnClickListener;
import com.example.jevil.mindreactor.R;

/**
 * Created by Jevil on 09.11.2016.
 */

public class EventsHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, View.OnCreateContextMenuListener {

    CardView cv;
    TextView tvName;
    TextView tvMark;
    ImageView ivBenefit;
    private LongClickListener longClickListener;
    private OnClickListener onClickListener;

    public EventsHolder(View itemView) {
        super(itemView);

        cv = (CardView) itemView.findViewById(R.id.cv);
        tvName = (TextView) itemView.findViewById(R.id.tvName);
        tvMark = (TextView) itemView.findViewById(R.id.tvMark);
        ivBenefit = (ImageView) itemView.findViewById(R.id.ivBenefit);

        itemView.setOnLongClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
        itemView.setOnClickListener(this);

    }

    public void setLongClickListener(LongClickListener ic)
    {
        this.longClickListener =ic;
    }

    public void setOnClickListener(OnClickListener ic)
    {
        this.onClickListener =ic;
    }

    @Override
    public boolean onLongClick(View v) {
        this.longClickListener.onItemLongClick(getLayoutPosition());
        return false;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Чего желаете?");
        menu.add(0, 0, 0, "Изменить хочу");
        menu.add(0, 1, 0, "Удалить");
    }

    @Override
    public void onClick(View v) {
        this.onClickListener.onItemClick(getLayoutPosition());
    }
}