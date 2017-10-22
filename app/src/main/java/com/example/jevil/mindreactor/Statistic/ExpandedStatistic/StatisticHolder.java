package com.example.jevil.mindreactor.Statistic.ExpandedStatistic;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jevil.mindreactor.Events.TabStory.StoryListItem;
import com.example.jevil.mindreactor.Listeners.LongClickListener;
import com.example.jevil.mindreactor.Listeners.OnClickListener;
import com.example.jevil.mindreactor.R;

/**
 * Created by Jevil on 16.11.2016.
 */

public class StatisticHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, View.OnCreateContextMenuListener {

    CardView cv;
    TextView tvName, tvTime, tvMin, tvMarks, tvDate;
    ImageView ivBenefit;
    LongClickListener longClickListener;
    OnClickListener onClickListener;

    public StatisticHolder(View itemView) {
        super(itemView);

        cv = (CardView) itemView.findViewById(R.id.cvStory);
        tvName = (TextView) itemView.findViewById(R.id.tvStoryName);
        tvMin = (TextView) itemView.findViewById(R.id.tvStoryMin);
        ivBenefit = (ImageView) itemView.findViewById(R.id.ivBenefit);
        tvMarks = (TextView) itemView.findViewById(R.id.tvMarks);

//        itemView.setOnLongClickListener(this);
//        itemView.setOnCreateContextMenuListener(this);
//        itemView.setOnClickListener(this);

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
        if (StoryListItem.selectedItem.getType().equals("cont")) { // продолжительное
            menu.add(0, 2, 0, "Изменить хочу");
            menu.add(0, 3, 0, "Удалить");
        } else { // разовое
            menu.add(0, 3, 0, "Удалить");
        }

        //menu.add(0, 2, 0, "third");
    }

    @Override
    public void onClick(View v) {
        this.onClickListener.onItemClick(getLayoutPosition());
    }
}