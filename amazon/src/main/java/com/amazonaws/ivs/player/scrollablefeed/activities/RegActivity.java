package com.amazonaws.ivs.player.scrollablefeed.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.ivs.player.scrollablefeed.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegActivity extends AppCompatActivity {
    public static final String TAG = "VerifyEmail";
    EditText firstName, lastName, reg_email, reg_password;
    TextView b2_login;
    Button reg_btn;
    CheckBox reg_user, reg_admin;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        // connecting to XML resources
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        reg_email = findViewById(R.id.email);
        reg_password = findViewById(R.id.password);
        reg_user = findViewById(R.id.isUser);
        reg_admin = findViewById(R.id.isAdmin);
        reg_btn = findViewById(R.id.new_user);
        b2_login = findViewById(R.id.b2_login);
        // Initialise Firebase Authentication
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);

        reg_user.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    reg_admin.setChecked(false);
                }
            }
        });

        reg_admin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    reg_user.setChecked(false);
                }
            }
        });

        // Validation for Checkbox
        //if(!(reg_admin.isChecked() || reg_user.isChecked())){
          //  Toast.makeText(RegActivity.this, "Select type of account you want to register", Toast.LENGTH_SHORT).show();
           // return;
       // }

        // Takes user back to login page
        b2_login.setOnClickListener(new View.OnClickListener() {@Override
        public void onClick(View v) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
        });






        // Validation for Registration form
        reg_btn.setOnClickListener(new View.OnClickListener() {@Override
        public void onClick(View v) {
            final String email = reg_email.getText().toString().trim();
            String password = reg_password.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                reg_email.setError("Please Enter Email");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                reg_password.setError("Please Enter Password");
                return;
            }
            // Validation so password is over 8 characters long
            if (password.length() < 8) {
                reg_password.setError("Password Must Be >= 8 Characters");
                return;
            }

            progressBar.setVisibility(View.VISIBLE);

            // Adding user to firebase
            fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener < AuthResult > () {@Override
            public void onComplete(@NonNull Task < AuthResult > task) {
                if (task.isSuccessful()) {
                    // Sends Verification email to user when account created
                    final FirebaseUser fbUser = fAuth.getCurrentUser();
                    fbUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener < Void > () {@Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(RegActivity.this, "Verification Email has been Sent", Toast.LENGTH_SHORT).show();
                        DocumentReference df = fStore.collection("Users").document(fbUser.getUid());
                        Map<String, Object> userInfo = new HashMap<>();
                        userInfo.put("FirstName", firstName.getText().toString());
                        userInfo.put("Surname", lastName.getText().toString());
                        userInfo.put("UserEmail", reg_email.getText().toString());
                        // Verifies if user is admin (Artist) or user (Raver)
                        if(reg_admin.isChecked()){
                            userInfo.put("isAdmin", "0");
                        }
                        if(reg_user.isChecked()){
                            userInfo.put("isUser", "1");
                        }

                        df.set(userInfo);


                        progressBar.setVisibility(View.GONE);
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    }
                    }).addOnFailureListener(new OnFailureListener() {@Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG,"onFailure: Verification Email has not sent" + e.getMessage() );
                    }
                    });

                } else {
                    Toast.makeText(RegActivity.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }

            }
            });

        }
        });

    }

}