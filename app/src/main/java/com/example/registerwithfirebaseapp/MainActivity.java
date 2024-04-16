package com.example.registerwithfirebaseapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.registerwithfirebaseapp.bottomnav.home.HomeFragment;
import com.example.registerwithfirebaseapp.bottomnav.notes.NotesFragment;
import com.example.registerwithfirebaseapp.bottomnav.profile.ProfileFragment;
import com.example.registerwithfirebaseapp.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }

        binding.profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class ));
            }
        });

        binding.notesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NotesActivity.class));
            }
        });


//        getSupportFragmentManager().beginTransaction().replace(binding.fragmentContainer.getId(), new HomeFragment()).commit();
//        binding.bottomNav.setSelectedItemId(R.id.home);

//        Map<Integer, Fragment> fragmentMap = new HashMap<>();
//        fragmentMap.put(R.id.home, new HomeFragment());
//        fragmentMap.put(R.id.notes, new NotesFragment());
//        fragmentMap.put(R.id.profile, new ProfileFragment());


//        binding.bottomNav.setOnItemSelectedListener(item -> {
//            Fragment fragment = fragmentMap.get(item.getItemId());
//
//            getSupportFragmentManager().beginTransaction().replace(binding.fragmentContainer.getId(), fragment).commit();
//
//
//            return true;
//        });
    }

}