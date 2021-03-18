package com.amazonaws.ivs.player.scrollablefeed.activities.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amazonaws.ivs.player.scrollablefeed.R;
import com.amazonaws.ivs.player.scrollablefeed.activities.EventActivity;
import com.amazonaws.ivs.player.scrollablefeed.activities.EventViewHolder;
import com.amazonaws.ivs.player.scrollablefeed.activities.Events;
import com.amazonaws.ivs.player.scrollablefeed.activities.ListActivity;

import java.util.List;

public class EventAdapter  extends RecyclerView.Adapter<EventViewHolder> {
    ListActivity listActivity;
    List<Events> eventList;
    public EventAdapter(ListActivity listActivity, List<Events> eventList) {
        this.listActivity = listActivity;
        this.eventList = eventList;
    }



    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_layout, parent, false);

        EventViewHolder viewHolder = new EventViewHolder(itemView);

        viewHolder.setOnClickListener(new EventViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                String title = eventList.get(position).getTitle();
                String date = eventList.get(position).getDate();
                String desc = eventList.get(position).getDescription();
                Toast.makeText(listActivity, title+"\n"+date+desc, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

         return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }
}
