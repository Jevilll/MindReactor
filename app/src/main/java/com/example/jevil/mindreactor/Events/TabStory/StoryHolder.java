package com.example.jevil.mindreactor.Events.TabStory;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jevil.mindreactor.Listeners.LongClickListener;
import com.example.jevil.mindreactor.Listeners.OnClickListener;
import com.example.jevil.mindreactor.R;

public class StoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, View.OnCreateContextMenuListener {

    TextView tvName, tvTime, tvMin, tvMarks, tvDate;
    ImageView ivBenefit;
    private LongClickListener longClickListener;
    private OnClickListener onClickListener;

    public StoryHolder(View itemView) {
        super(itemView);

        tvName = (TextView) itemView.findViewById(R.id.tvStoryName);
        tvTime = (TextView) itemView.findViewById(R.id.tvStoryTime);
        tvMin = (TextView) itemView.findViewById(R.id.tvStoryMin);
        ivBenefit = (ImageView) itemView.findViewById(R.id.ivBenefit);
        tvMarks = (TextView) itemView.findViewById(R.id.tvMarks);
        tvDate = (TextView) itemView.findViewById(R.id.tvDate);

        itemView.setOnLongClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
        itemView.setOnClickListener(this);

    }

    void setLongClickListener(LongClickListener ic)
    {
        this.longClickListener =ic;
    }

    void setOnClickListener(OnClickListener ic)
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
    }

    @Override
    public void onClick(View v) {
        this.onClickListener.onItemClick(getLayoutPosition());
    }
}