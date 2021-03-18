package com.amazonaws.ivs.player.scrollablefeed.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.text.format.DateFormat;

import com.amazonaws.ivs.player.scrollablefeed.R;
import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Objects;


public class LiveChat extends AppCompatActivity {

    private FirebaseRecyclerAdapter<ChatMessage, ChatViewHolder> adapter;
    RelativeLayout activity_live_chat;
    AppCompatEditText input_msg;
    ImageView submit_btn;
    RecyclerView message_list;
    Query query;

    FirebaseRecyclerOptions<ChatMessage> options;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        displayChatMessage();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_chat);



        activity_live_chat = findViewById(R.id.activity_chat);
        message_list = findViewById(R.id.list_of_message);
        message_list.setHasFixedSize(true);
        message_list.setLayoutManager(new LinearLayoutManager(this));

        query = FirebaseDatabase.getInstance().getReference();
       options = new FirebaseRecyclerOptions.Builder<ChatMessage>()
                       // .setLayout(R.layout.chat_list_item)
                        .setQuery(query, ChatMessage.class)
                        .build();

        input_msg = findViewById(R.id.input);
        submit_btn = findViewById(R.id.btn_submit);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().push().setValue(new ChatMessage(input_msg.getText().toString(),
                        Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail()));
                input_msg.setText("");
                input_msg.requestFocus();

            }
        });

        displayChatMessage();


    }

    private void displayChatMessage() {

        adapter = new FirebaseRecyclerAdapter<ChatMessage, ChatViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ChatViewHolder holder, int position, @NonNull ChatMessage model) {
                holder.textMessage.setText(model.getTextMessage());
                holder.userOfMessage.setText(model.getUserMessage());
                holder.messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));

            }

            @NonNull
            @Override
            public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chat_list_item, parent, false);

                return new ChatViewHolder(itemView);
            }

            @Override
            public int getItemCount() {
                return 0;
            }
        };

        message_list.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView textMessage, userOfMessage,messageTime;


        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            textMessage = (TextView) itemView.findViewById(R.id.message_text);
            userOfMessage = (TextView) itemView.findViewById(R.id.message_user);
            messageTime = (TextView) itemView.findViewById(R.id.time);
        }
    }
}