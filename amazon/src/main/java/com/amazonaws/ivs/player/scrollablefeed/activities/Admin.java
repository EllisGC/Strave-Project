package com.amazonaws.ivs.player.scrollablefeed.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.amazonaws.ivs.player.scrollablefeed.R;
import com.google.firebase.auth.FirebaseAuth;

public class Admin extends AppCompatActivity {

    //CardView newEvent;
    ImageView addEvent, profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        //newEvent = findViewById(R.id.events_card);
        addEvent = findViewById(R.id.events);
        profile = findViewById(R.id.profile);



        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), EventActivity.class));
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), UserProfile.class));
            }
        });
    }




    public void adminLogout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finish();
    }
}