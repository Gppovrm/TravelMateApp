package com.example.registerwithfirebaseapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.registerwithfirebaseapp.databinding.ActivityMainBinding;
import com.example.registerwithfirebaseapp.databinding.ActivityNewTripBinding;
import com.example.registerwithfirebaseapp.databinding.FragmentNotesBinding;

public class NewTripActivity extends AppCompatActivity {
    private ActivityNewTripBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewTripBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}

