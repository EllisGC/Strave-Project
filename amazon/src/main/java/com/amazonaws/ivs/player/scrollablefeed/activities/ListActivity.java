package com.amazonaws.ivs.player.scrollablefeed.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.ivs.player.scrollablefeed.R;
import com.amazonaws.ivs.player.scrollablefeed.activities.adapters.EventAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListActivity extends AppCompatActivity {

    List<Events> eventsList = new ArrayList<>();
    RecyclerView eRecyclerView;
    RecyclerView.LayoutManager layoutManager;

    FloatingActionButton eAddBtn;

    FirebaseFirestore DB;

    EventAdapter adapter;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        DB = FirebaseFirestore.getInstance();

        // Setup Views
        eRecyclerView = findViewById(R.id.event_list);
        eAddBtn = findViewById(R.id.addEventBtn);

        eRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        eRecyclerView.setLayoutManager(layoutManager);

        pd = new ProgressDialog(this);

        // Display Data In RecyclerView
        showEventData();

        eAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListActivity.this, EventActivity.class));
                finish();
            }
        });
    }

    private void showEventData() {

        pd.setTitle("Loading Data...");
        pd.show();

        DB.collection("Events")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        pd.dismiss();

                        for (DocumentSnapshot doc: Objects.requireNonNull(task.getResult())) {
                            Events event = new Events(doc.getString("id"),
                                    doc.getString("title"),
                                    doc.getString("date"),
                                    doc.getString("description"));
                            eventsList.add(event);
                        }

                        adapter = new EventAdapter(ListActivity.this, eventsList);
                        eRecyclerView.setAdapter(adapter);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();

                        Toast.makeText(ListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }
}