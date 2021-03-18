package com.amazonaws.ivs.player.scrollablefeed.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amazonaws.ivs.player.scrollablefeed.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class EventsMainPage extends AppCompatActivity {

    EditText eventSearchField;
    ImageButton eventSearchBtn;
    RecyclerView searchResultList;
    DatabaseReference eventDatabase;
    FirebaseRecyclerOptions<E1> options;
    FirebaseRecyclerAdapter<E1, EventsViewHolder> adapter;
    private RecyclerView.LayoutManager mLayoutManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_search);

        eventDatabase = FirebaseDatabase.getInstance().getReference().child("E1");
        eventSearchField = findViewById(R.id.search_field);
        eventSearchBtn = findViewById(R.id.search_btn);
        searchResultList = findViewById(R.id.search_result_list);
        searchResultList.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        searchResultList.setLayoutManager(mLayoutManager);



        eventSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });

        firebaseEventSearch("");
        eventSearchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString()!=null)
                {
                    firebaseEventSearch(editable.toString());
                }
                else
                {
                    firebaseEventSearch("");
                }

            }
        });


    }


    private void firebaseEventSearch(String data) {
        Query query = eventDatabase.orderByChild("event").startAt(data).endAt(data+"\uf8ff");
       options = new FirebaseRecyclerOptions.Builder<E1>().setQuery(query, E1.class).build();
       adapter = new FirebaseRecyclerAdapter<E1, EventsViewHolder>(options) {
           @Override
           protected void onBindViewHolder(@NonNull EventsViewHolder holder, int position, @NonNull E1 model) {
               holder.eventName.setText(model.getEvent());
               holder.eventLocation.setText(model.getLocation());
               Picasso.get().load(model.getImage()).into(holder.eventLogo);

           }

           @NonNull
           @Override
           public EventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
               View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_event_list, parent, false);
               return new EventsViewHolder(v);
           }
       };
        adapter.startListening();
        searchResultList.setAdapter(adapter);




    }
}
