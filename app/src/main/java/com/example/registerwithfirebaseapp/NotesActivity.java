package com.example.registerwithfirebaseapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.registerwithfirebaseapp.Notes.NoteCLickListener;
import com.example.registerwithfirebaseapp.Notes.NoteItemsRecyclerView;
import com.example.registerwithfirebaseapp.Notes.NoteModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotesActivity extends AppCompatActivity implements NoteCLickListener {

    List<NoteModel> noteModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        TextView trip_title = findViewById(R.id.name_vacation);
        TextView trip_city = findViewById(R.id.location_vacation);
        TextView date_start = findViewById(R.id.date_start);
        TextView date_end = findViewById(R.id.date_end);

        RecyclerView notelist = findViewById(R.id.note_list);
        ImageView add_new_note_btn = findViewById(R.id.add_new_note_btn);
        add_new_note_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NotesActivity.this, CreateNoteActivity.class));
            }
        });
        ImageView back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NotesActivity.this, MainActivity.class));
            }
        });

        Button ButtonTickets = findViewById(R.id.tickets_btn);
        ButtonTickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NotesActivity.this, TicketsActivity.class));

            }
        });


        Button check_items_list_btn = findViewById(R.id.check_items_list_btn);
        check_items_list_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NotesActivity.this, CheckItemsActivity.class));
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
                Toast.makeText(NotesActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });


        databaseReference.child(firebaseUser.getUid()).child("yes").child("notes_list").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                noteModels.clear();

                for(DataSnapshot dataSnapshot1:snapshot.getChildren()){
                    String id = dataSnapshot1.child("id").getValue(String.class);
                    String note_title = dataSnapshot1.child("note_title").getValue(String.class);
                    String note_content = dataSnapshot1.child("note_content").getValue(String.class);
                    noteModels.add(new NoteModel(id, note_title, note_content));
                }

                NoteItemsRecyclerView noteItemsRecyclerView = new NoteItemsRecyclerView(noteModels, NotesActivity.this);
                notelist.setLayoutManager(new LinearLayoutManager(NotesActivity.this));
                notelist.setAdapter(noteItemsRecyclerView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(NotesActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClickItem(NoteModel noteModel) {
        Intent intent=new Intent(NotesActivity.this, ViewNoteDataActivity.class);
        intent.putExtra("id",noteModel.getId());
        intent.putExtra("note_title",noteModel.getNote_title());
        intent.putExtra("note_content",noteModel.getNote_content());
        startActivity(intent);
    }
}