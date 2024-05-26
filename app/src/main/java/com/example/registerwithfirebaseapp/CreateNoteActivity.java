package com.example.registerwithfirebaseapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.registerwithfirebaseapp.Notes.NoteModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        ImageView save_note_btn = findViewById(R.id.save_note_btn);
        EditText new_note_title = findViewById(R.id.new_note_title);
        EditText new_note_content = findViewById(R.id.new_note_content);

        save_note_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNotes(new_note_title.getText().toString(), new_note_content.getText().toString());
            }
        });


    }

    private void saveNotes(String new_note_title, String new_note_content){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users").child(firebaseUser.getUid()).child("yes").child("notes_list");
        String id = databaseReference.push().getKey();
        NoteModel noteModel = new NoteModel(id, new_note_title, new_note_content);
        databaseReference.child(id).setValue(noteModel);

        startActivity(new Intent(CreateNoteActivity.this, NotesActivity.class));
    }
}