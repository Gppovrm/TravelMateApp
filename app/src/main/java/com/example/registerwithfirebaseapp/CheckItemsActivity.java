package com.example.registerwithfirebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.registerwithfirebaseapp.CheckItems.CheckItemModel;
import com.example.registerwithfirebaseapp.CheckItems.CheckItemsRecyclerView;
import com.example.registerwithfirebaseapp.CheckItems.OnDialogCloseListener;
import com.example.registerwithfirebaseapp.CheckItems.TouchHelper;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CheckItemsActivity extends AppCompatActivity implements OnDialogCloseListener {

    List<CheckItemModel> checkItemModels = new ArrayList<>();
    private CheckItemsRecyclerView adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_items);

        TextView trip_title = findViewById(R.id.name_vacation);
        TextView trip_city = findViewById(R.id.location_vacation);
        TextView date_start = findViewById(R.id.date_start);
        TextView date_end = findViewById(R.id.date_end);

        RecyclerView check_items_list = findViewById(R.id.check_items_list);
        check_items_list.setHasFixedSize(true);
        check_items_list.setLayoutManager(new LinearLayoutManager(CheckItemsActivity.this));


        Button notes_btn = findViewById(R.id.notes_btn);
        notes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CheckItemsActivity.this, NotesActivity.class));
            }
        });


        ImageView add_new_check_item_btn = findViewById(R.id.add_new_check_item_btn);
        add_new_check_item_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewCheckItemFragment.newInstance().show(getSupportFragmentManager(), CreateNewCheckItemFragment.TAG);
            }
        });

        ImageView back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CheckItemsActivity.this, MainActivity.class));
            }
        });


        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users");

        databaseReference.child(firebaseUser.getUid()).child("yes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                trip_title.setText(snapshot.child("tripTitle").getValue().toString());
                trip_city.setText(snapshot.child("city").getValue().toString());
                date_start.setText(snapshot.child("dateStart").getValue().toString());
                date_end.setText(snapshot.child("dateEnd").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CheckItemsActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });

        adapter = new CheckItemsRecyclerView(CheckItemsActivity.this,  checkItemModels);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(check_items_list);

        databaseReference.child(firebaseUser.getUid()).child("yes").child("items_list").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                checkItemModels.clear();

                for(DataSnapshot dataSnapshot1:snapshot.getChildren()){
                    String id = dataSnapshot1.child("id").getValue(String.class);
                    String item_name = dataSnapshot1.child("item_name").getValue(String.class);
                    String status = dataSnapshot1.child("status").getValue().toString();
                    checkItemModels.add(new CheckItemModel(id, item_name, status));
                    adapter.notifyDataSetChanged();
                }

                Collections.sort(checkItemModels, new Comparator<CheckItemModel>() {
                    public int compare(CheckItemModel o1, CheckItemModel o2) {
                        return o2.getStatus().compareTo(o1.getStatus());
                    }
                });

                Collections.reverse(checkItemModels);

                check_items_list.setLayoutManager(new LinearLayoutManager(CheckItemsActivity.this));
                check_items_list.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



    /*private void showData(){
        databaseReference.child(firebaseUser.getUid()).child("yes").child("items_list").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                checkItemModels.clear();

                for(DataSnapshot dataSnapshot1:snapshot.getChildren()){
                    String id = dataSnapshot1.child("id").getValue(String.class);
                    String item_name = dataSnapshot1.child("item_name").getValue(String.class);
                    String status = dataSnapshot1.child("status").getValue(String.class);
                    checkItemModels.add(new CheckItemModel(id, item_name, status));
                }

                CheckItemsRecyclerView checkItemsRecyclerView = new CheckItemsRecyclerView(checkItemModels, CheckItemsActivity.this);
                check_items_list.setLayoutManager(new LinearLayoutManager(CheckItemsActivity.this));
                check_items_list.setAdapter(checkItemsRecyclerView);
            }
    }*/


    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        checkItemModels.clear();

        RecyclerView check_items_list = findViewById(R.id.check_items_list);
        check_items_list.setHasFixedSize(true);
        check_items_list.setLayoutManager(new LinearLayoutManager(CheckItemsActivity.this));

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users");

        databaseReference.child(firebaseUser.getUid()).child("yes").child("items_list").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                checkItemModels.clear();

                for(DataSnapshot dataSnapshot1:snapshot.getChildren()){
                    String id = dataSnapshot1.child("id").getValue(String.class);
                    String item_name = dataSnapshot1.child("item_name").getValue(String.class);
                    String status = dataSnapshot1.child("status").getValue().toString();
                    checkItemModels.add(new CheckItemModel(id, item_name, status));
                    adapter.notifyDataSetChanged();
                }

                Collections.sort(checkItemModels, new Comparator<CheckItemModel>() {
                    public int compare(CheckItemModel o1, CheckItemModel o2) {
                        return o2.getStatus().compareTo(o1.getStatus());
                    }
                });

                Collections.reverse(checkItemModels);

                //check_items_list.setLayoutManager(new LinearLayoutManager(CheckItemsActivity.this));
                check_items_list.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter.notifyDataSetChanged();
    }
}