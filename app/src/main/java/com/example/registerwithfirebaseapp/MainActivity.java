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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import java.net.URL;
import java.net.URLConnection;

import com.example.registerwithfirebaseapp.LocalParams.*;

import org.json.*;


//import com.google.gson.*;
//import com.google.gson.reflect.*;


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



    public void requestForOpenWheatherApi(String location) {
        location = "London"; // for the tests
        String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" + location + "&appid=" + LocalParams.API_KEY + "&units=metric";


        try {
            StringBuilder result = new StringBuilder();
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();
//            System.out.println(result);


            JSONObject objectJson = new JSONObject(result.toString());

            String mainWeather = objectJson.getJSONObject("weather").getString("main");

            System.out.println(mainWeather);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (JSONException e) {
            System.out.println(e.getMessage());
        }


    }



}