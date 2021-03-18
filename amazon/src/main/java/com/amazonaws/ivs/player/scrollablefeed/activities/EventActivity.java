package com.amazonaws.ivs.player.scrollablefeed.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.ivs.player.scrollablefeed.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EventActivity extends AppCompatActivity {

    EditText eventTitle, eventDesc, eventDate;
    Button saveBtn, listBtn;

    ProgressDialog pd;

    FirebaseFirestore DB = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);


        eventTitle = findViewById(R.id.titleE);
        eventDesc = findViewById(R.id.titleD);
        eventDate = findViewById(R.id.editTextDate);
        saveBtn = findViewById(R.id.btn_save);
        listBtn = findViewById(R.id.btn_list);

        pd = new ProgressDialog(this);

        DB = FirebaseFirestore.getInstance();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = eventTitle.getText().toString().trim();
                String desc = eventDesc.getText().toString().trim();
                String date = eventDate.getText().toString().trim();
                uploadData(title, desc, date);
            }
        });

        listBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EventActivity.this, ListActivity.class));
                finish();
            }
        });
    }

    private void uploadData(String date, String title, String desc) {
        pd.setTitle("Adding Data to Firestore");

        pd.show();

        String id = UUID.randomUUID().toString();

        Map<String, Object> doc = new HashMap<>();
        doc.put("id", id);
        doc.put("title", title);
        doc.put("description", desc);
        doc.put("date", date);

        // add data
        DB.collection("Events").document(id).set(doc)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pd.dismiss();
                        Toast.makeText(EventActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        pd.dismiss();
                        Toast.makeText(EventActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }
}