package com.amazonaws.ivs.player.scrollablefeed.activities;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amazonaws.ivs.player.scrollablefeed.R;

public class EventViewHolder extends RecyclerView.ViewHolder {
    TextView titleTv, descTV;
    View eventView;

    public EventViewHolder(@NonNull View itemView) {
        super(itemView);

        eventView = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eClickListener.onItemClick(view, getAdapterPosition());

            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });

        //initialize View with event layout.xml
        titleTv = itemView.findViewById(R.id.rTitleTv);
        descTV = itemView.findViewById(R.id.rDescTv);
    }

    private EventViewHolder.ClickListener eClickListener;
    public interface ClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
    public void setOnClickListener(EventViewHolder.ClickListener clickListener) {
        eClickListener = clickListener;

    }
}
