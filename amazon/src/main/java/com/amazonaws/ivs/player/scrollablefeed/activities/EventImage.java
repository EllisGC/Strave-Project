package com.amazonaws.ivs.player.scrollablefeed.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.ivs.player.scrollablefeed.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import javax.annotation.Nullable;

public class EventImage extends AppCompatActivity {

    private static final int REQUEST_CODE_IMAGE = 101;
    private ImageView addImage;
    private EditText addEventName;
    private TextView eventProgress;
    private ProgressBar progressBar;
    private Button uploadEvent;

    Uri imageURL;
    boolean isImageAdded = false;

    DatabaseReference eventDatabase;
    StorageReference refStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        addImage = findViewById(R.id.AddImage);
        addEventName = findViewById(R.id.inputEvent);
        eventProgress = findViewById(R.id.textProgress);
        progressBar = findViewById(R.id.newEventPB);
        uploadEvent = findViewById(R.id.btnUploadEvent);


        eventProgress.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        eventDatabase = FirebaseDatabase.getInstance().getReference().child("E1");
        refStorage = FirebaseStorage.getInstance().getReference().child("EventImage");

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("Image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE_IMAGE);
            }
        });

        uploadEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String eventName = addEventName.getText().toString();
                if (isImageAdded!=false && eventName!=null)
                {
                    uploadImage(eventName);
                }
            }
        });
    }

    private void uploadImage( final String eventName) {
        eventProgress.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        final String key = eventDatabase.push().getKey();
        refStorage.child(key+".jpg").putFile(imageURL).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                refStorage.child(key+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        HashMap hashMap = new HashMap();
                        hashMap.put("EventImage", eventName);
                        hashMap.put("ImageURL", uri.toString());

                        eventDatabase.child(key).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EventImage.this, "Data Uploaded", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });


            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (snapshot.getBytesTransferred()*100)/snapshot.getTotalByteCount();
                progressBar.setProgress((int) progress);
                eventProgress.setText(progress + " %");

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE_IMAGE && data!=null)
        {
            imageURL = data.getData();
            isImageAdded = true;
            addImage.setImageURI(imageURL);

        }
    }

}