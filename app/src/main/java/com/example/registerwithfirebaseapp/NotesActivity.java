package com.example.registerwithfirebaseapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.registerwithfirebaseapp.Notes.NoteCLickListener;
import com.example.registerwithfirebaseapp.Notes.NoteItemsRecyclerView;
import com.example.registerwithfirebaseapp.Notes.NoteModel;

import java.util.ArrayList;
import java.util.List;

public class NotesActivity extends AppCompatActivity implements NoteCLickListener {

    List<NoteModel> noteModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        RecyclerView notelist=findViewById(R.id.note_list);

        noteModels.add(new NoteModel("1", "Sochi", "See sea"));
        noteModels.add(new NoteModel("2", "Krasnodar", "eat more"));
        noteModels.add(new NoteModel("3", "Rostov-on-Don", "congratulate"));

        NoteItemsRecyclerView noteItemsRecyclerView = new NoteItemsRecyclerView(noteModels, NotesActivity.this);
        notelist.setLayoutManager(new LinearLayoutManager(NotesActivity.this));
        notelist.setAdapter(noteItemsRecyclerView);
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