package com.amazonaws.ivs.player.scrollablefeed.activities;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amazonaws.ivs.player.scrollablefeed.R;
import com.amazonaws.ivs.player.scrollablefeed.activities.adapters.MainAdapter;

public class EventsViewHolder extends RecyclerView.ViewHolder {
    ImageView eventLogo;
    TextView eventName, eventLocation;

    public EventsViewHolder(@NonNull View itemView) {
        super(itemView);
        eventLogo = itemView.findViewById(R.id.event_image);
        eventName = itemView.findViewById(R.id.event_text);
        eventLocation = itemView.findViewById(R.id.location_text);

    }
}
