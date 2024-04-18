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

/**
 * This is the main activity
 */
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding; // Main binding

    /**
     * This is the onCreate method.
     * @param savedInstanceState Bundle of arguments
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // checking for account login
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }

        /**
         * This is the listening of profile button
         */
        binding.profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class ));
            }
        });

        /**
         * This is the listening of notes button
         */
        binding.notesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NotesActivity.class));
            }
        });

        /**
         * This is the listening of trip button
         */
        binding.newTripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NewTripActivity.class));
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