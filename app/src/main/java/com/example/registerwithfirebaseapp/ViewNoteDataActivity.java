package com.example.registerwithfirebaseapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ViewNoteDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note_data);
        String id = getIntent().getStringExtra("id");

        TextView note_title = findViewById(R.id.note_title_view);
        TextView note_content = findViewById(R.id.note_content_view);

        note_title.setText(getIntent().getStringExtra("note_title"));
        note_content.setText(getIntent().getStringExtra("note_content"));
    }
}