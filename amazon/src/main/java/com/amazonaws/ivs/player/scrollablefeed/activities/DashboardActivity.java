package com.amazonaws.ivs.player.scrollablefeed.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.ivs.player.scrollablefeed.R;

public class DashboardActivity extends AppCompatActivity {
    ImageView events, profile, chat, donate, music, sign_out, tickets;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        events = findViewById(R.id.events);
        profile = findViewById(R.id.profile);
        chat = findViewById(R.id.chat);
        donate = findViewById(R.id.donate);
        music = findViewById(R.id.music);
        sign_out = findViewById(R.id.signout);
        tickets = findViewById(R.id.tickets);

        //Opens Paypal Sandbox
        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DonationActivity.class));
            }
        });
        //Sends User back to Login Screen
        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ListActivity.class));
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LiveChat.class));
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ImageUpload.class));
            }
        });
    }
}
