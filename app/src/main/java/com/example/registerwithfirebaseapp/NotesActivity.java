package com.example.registerwithfirebaseapp;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.registerwithfirebaseapp.databinding.ActivityNotesBinding;

/**
 * This is activity for the notes
 */
public class NotesActivity extends AppCompatActivity {

    private ActivityNotesBinding binding;

    /**
     * This is the onCreate method.
     * @param savedInstanceState Bundle of arguments
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}