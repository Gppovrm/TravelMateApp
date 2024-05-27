package com.example.registerwithfirebaseapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.registerwithfirebaseapp.Notes.NoteModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ViewNoteDataActivity extends AppCompatActivity {

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note_data);
        id = getIntent().getStringExtra("id");

        TextView note_title = findViewById(R.id.note_title_view_edit);
        TextView note_content = findViewById(R.id.note_content_view_edit);

        ImageView save_note_btn = findViewById(R.id.save_note_btn);
        ImageView back_note_btn = findViewById(R.id.arrow_back_btn);
        Button delete_note_btn = findViewById(R.id.delete_note_btn);

        save_note_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNotes(id, note_title.getText().toString(), note_content.getText().toString());
            }
        });

        delete_note_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteNote();
                startActivity(new Intent(ViewNoteDataActivity.this, NotesActivity.class));
            }
        });

        back_note_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewNoteDataActivity.this, NotesActivity.class));
            }
        });

        note_title.setText(getIntent().getStringExtra("note_title"));
        note_content.setText(getIntent().getStringExtra("note_content"));
    }

    private void saveNotes(String id, String note_title, String note_content){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users").child(firebaseUser.getUid()).child("yes").child("notes_list");
        NoteModel noteModel = new NoteModel(id, note_title, note_content);
        databaseReference.child(id).setValue(noteModel);

        startActivity(new Intent(ViewNoteDataActivity.this, NotesActivity.class));
    }

    private void deleteNote(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users").child(firebaseUser.getUid()).child("yes").child("notes_list").child(id);
        databaseReference.removeValue();
    }
}