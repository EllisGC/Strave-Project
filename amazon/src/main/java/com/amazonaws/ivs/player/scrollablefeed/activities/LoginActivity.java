package com.amazonaws.ivs.player.scrollablefeed.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.ivs.player.scrollablefeed.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    EditText username, lPassword;
    TextView new_user, forgot_password;
    Button login_btn, stream_btn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    Boolean valid = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // connecting to XML resources
        username = findViewById(R.id.username);
        lPassword = findViewById(R.id.password);
        login_btn = findViewById(R.id.btn_login);
        new_user = findViewById(R.id.new_user);
        progressBar = findViewById(R.id.progressBar);
        forgot_password = findViewById(R.id.forgot_password);
        // Initialise Firebase Authentication & Firestore DB
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLoginInput(username);
                checkLoginInput(lPassword);


                if(valid){
                    fAuth.signInWithEmailAndPassword(username.getText().toString(), lPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            checkAccessLevel(authResult.getUser().getUid());
                            //startActivity(new Intent(getApplicationContext(), DashboardActivity.class));

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginActivity.this, "Error ! " , Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            }
        });

        // Transfers user to Registration Page
        new_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegActivity.class));
            }
        });

        // Validating Text View to send password reset link for login
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText resetPasswordMail = new EditText(v.getContext());
                final AlertDialog.Builder resetPasswordDialog = new AlertDialog.Builder(v.getContext());
                resetPasswordDialog.setTitle("Reset Password ?");
                resetPasswordDialog.setMessage("Enter your Email to Receive Reset Link");
                resetPasswordDialog.setView(resetPasswordMail);

                resetPasswordDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Extract the email and send reset link
                        String mail = resetPasswordMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(LoginActivity.this, "Reset Link Sent To Your Email.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, "Error ! Reset Link is Not Sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                resetPasswordDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // closing the dialog
                    }
                });

                resetPasswordDialog.create().show();

            }
        });

    }


    private void checkAccessLevel(String uid) {
        DocumentReference df = fStore.collection("Users").document(uid);

        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG","OnSuccess" + documentSnapshot.getData());

                if(documentSnapshot.getString("isAdmin") != null){
                    // user is admin

                    startActivity(new Intent(getApplicationContext(), Admin.class));
                    finish();
                }

                if(documentSnapshot.getString("isUser") != null){
                    // normal user
                    startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                    finish();
                }


            }
        });
    }

    public boolean checkLoginInput(EditText textField){
        if(textField.getText().toString().isEmpty()){
            textField.setError("Error");
            valid = false;
        }else {
            valid = true;
        }

        return valid;
    }

        }



