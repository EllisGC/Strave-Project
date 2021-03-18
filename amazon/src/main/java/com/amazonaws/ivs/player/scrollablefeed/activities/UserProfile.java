package com.amazonaws.ivs.player.scrollablefeed.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.amazonaws.ivs.player.scrollablefeed.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class UserProfile extends AppCompatActivity {
    TextView firstName, surname, userEmail;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        firstName = findViewById(R.id.profileName);
        surname = findViewById(R.id.profileSurname);
        userEmail = findViewById(R.id.profileEmail);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();

        DocumentReference dRef = fStore.collection("Users").document(userID);
        dRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                firstName.setText(value.getString("FirstName"));
                surname.setText(value.getString("Surname"));
                userEmail.setText(value.getString("UserEmail"));
            }
        });





    }
}