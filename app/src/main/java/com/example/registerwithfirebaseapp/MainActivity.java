package com.example.registerwithfirebaseapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.github.prominence.openweathermap.api.OpenWeatherMapClient;
import com.github.prominence.openweathermap.api.enums.Language;
import com.github.prominence.openweathermap.api.enums.UnitSystem;
import com.github.prominence.openweathermap.api.exception.NoDataFoundException;
import com.github.prominence.openweathermap.api.model.weather.Weather;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

// imports for data counter
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

// imports fot time
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {


    private ImageView imageView;

    private String fullName;
    private FirebaseAuth authProfile;

    private TimeZone timeZone;
    private BroadcastReceiver minuteUpdateReceiver;
    private TextView local_time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        if (firebaseUser == null) {
            Toast.makeText(MainActivity.this, "LOGIN FIRST", Toast.LENGTH_LONG).show();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));

        } else {
//            Toast.makeText(MainActivity.this, "Already LOGIN", Toast.LENGTH_LONG).show();
        }

        imageView = findViewById(R.id.profile_btn);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, UserProfileActivity.class));

            }
        });

        Button notes_btn = findViewById(R.id.notes_btn);
        notes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NotesActivity.class));

            }
        });


        TextView welcome_name = findViewById(R.id.welcome_name_login);

        TextView trip_title = findViewById(R.id.name_vacation);
        TextView trip_city = findViewById(R.id.location_vacation);
        TextView date_start = findViewById(R.id.date_start);
        TextView date_end = findViewById(R.id.date_end);
        TextView temperature_number = findViewById(R.id.temperature_number);
        TextView number_days = findViewById(R.id.number_days);

        local_time = findViewById(R.id.local_time);


        FirebaseAuth auth = FirebaseAuth.getInstance();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users");

        databaseReference.child(auth.getCurrentUser().getUid()).child("yes").addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fullName = auth.getCurrentUser().getDisplayName().toString();
                welcome_name.setText(fullName); // Раотничек

//                welcome_name.setText(snapshot.child("fullName").getValue().toString()); // Раотничек
                trip_title.setText(snapshot.child("tripTitle").getValue().toString()); // Название отпуска
                trip_city.setText(snapshot.child("city").getValue().toString()); // Местоположение
                date_start.setText(snapshot.child("dateStart").getValue().toString());
                date_end.setText(snapshot.child("dateEnd").getValue().toString());

                timeZone = TimeZone.getTimeZone(snapshot.child("timeZone").getValue().toString());
                Calendar c = Calendar.getInstance(timeZone);
                String time = String.format("%02d" , c.get(Calendar.HOUR_OF_DAY))+":"+ String.format("%02d" , c.get(Calendar.MINUTE));
                local_time.setText(time);


                // =========== Счет дней до отпуска ===========
                Date currentDate = new Date();

                DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

                String date1 = dateFormat.format(currentDate);
                String date2 = snapshot.child("dateStart").getValue().toString();

                LocalDate startDate = LocalDate.parse(date1, formatter);
                LocalDate endDate = LocalDate.parse(date2, formatter);

                long p2 = ChronoUnit.DAYS.between(startDate, endDate);
                number_days.setText(String.valueOf(p2));

//                Toast.makeText(MainActivity.this, "Между: "+ p2 + "!!!!!!!!!", Toast.LENGTH_LONG).show();

                // =============================================

                // =========== Установка температуры ===========


                String API_KEY = "bc7e4735b58fbed9142139733049dd46";
                String str_trip_city = (String) trip_city.getText();
//                Log.i("str_trip_city", str_trip_city);


//                OpenWeatherMapClient openWeatherClient = new OpenWeatherMapClient(API_KEY);
                Log.i("Api", "Start work");
                try {
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            WeatherFetcher weather = new WeatherFetcher();
                            try {



                                int temperature = weather.getTemperatureForCity(str_trip_city); // получение температуры по городу
                                Log.i("Temperature", String.valueOf(temperature));

                                Message msg = handler.obtainMessage();
                                Bundle bundle = new Bundle();

                                String dateString = String.valueOf(temperature);

                                bundle.putString("Key", dateString);
                                msg.setData(bundle);
                                handler.sendMessage(msg);

                                //  Использовать нельзя - краш => Получаем через handler
                                //  TextView ex_temperature_number = findViewById(R.id.temperature_number);
                                //  ex_temperature_number.setText(String.valueOf(temperature));

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    };
                    Thread thread = new Thread(runnable);
                    thread.start();

                } catch (NoDataFoundException e) {
                    Log.e("Error", "NoDataFoundException");
                }
                // =================================

            }

            // =========== Handler для AsyncProcess ===========
            Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    Bundle bundle = msg.getData();
                    Log.e("Bundle", String.valueOf(msg.getData()));
                    String date = bundle.getString("Key");
                    TextView ex_temperature_number = findViewById(R.id.temperature_number);
                    ex_temperature_number.setText(String.valueOf(date));
                }
            };
            // =================================

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });

    }


    private void startMinuteUpdater(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        minuteUpdateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Calendar c = Calendar.getInstance(timeZone);
                String time = String.format("%02d" , c.get(Calendar.HOUR_OF_DAY))+":"+ String.format("%02d" , c.get(Calendar.MINUTE));
                local_time.setText(time);
            }
        };

        registerReceiver(minuteUpdateReceiver, intentFilter);
    }

    @Override
    protected void onResume(){
        super.onResume();
        startMinuteUpdater();
    }

    @Override
    protected void onPause(){
        super.onPause();
        unregisterReceiver(minuteUpdateReceiver);
    }


}





//
///**
// * This is the main activity
// */
//public class MainActivity extends AppCompatActivity {
//    private ActivityMainBinding binding; // Main binding
//
//    /**
//     * This is the onCreate method.
//     * @param savedInstanceState Bundle of arguments
//     */
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        // checking for account login
//        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
//            startActivity(new Intent(MainActivity.this, LoginActivity.class));
//        }
//
//        /**
//         * This is the listening of profile button
//         */
//        binding.profileBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, UserProfileActivity.class ));
//            }
//        });
//
//        /**
//         * This is the listening of notes button
//         */
//        binding.notesBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, NotesActivity.class));
//            }
//        });
//
//        /**
//         * This is the listening of trip button
//         */
//        binding.newTripBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, NewTripActivity.class));
//            }
//        });
//
//
////        getSupportFragmentManager().beginTransaction().replace(binding.fragmentContainer.getId(), new HomeFragment()).commit();
////        binding.bottomNav.setSelectedItemId(R.id.home);
//
////        Map<Integer, Fragment> fragmentMap = new HashMap<>();
////        fragmentMap.put(R.id.home, new HomeFragment());
////        fragmentMap.put(R.id.notes, new NotesFragment());
////        fragmentMap.put(R.id.profile, new ProfileFragment());
//
//
////        binding.bottomNav.setOnItemSelectedListener(item -> {
////            Fragment fragment = fragmentMap.get(item.getItemId());
////
////            getSupportFragmentManager().beginTransaction().replace(binding.fragmentContainer.getId(), fragment).commit();
////
////
////            return true;
////        });
//    }
//
//
//
//    public void requestForOpenWheatherApi(String location) {
//        location = "London"; // for the tests
//        String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" + location + "&appid=" + LocalParams.API_KEY + "&units=metric";
//
//
//        try {
//            StringBuilder result = new StringBuilder();
//            URL url = new URL(urlString);
//            URLConnection conn = url.openConnection();
//            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            String line;
//            while ((line = rd.readLine()) != null) {
//                result.append(line);
//            }
//            rd.close();
////            System.out.println(result);
//
//
//            JSONObject objectJson = new JSONObject(result.toString());
//
//            String mainWeather = objectJson.getJSONObject("weather").getString("main");
//
//            System.out.println(mainWeather);
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//        } catch (JSONException e) {
//            System.out.println(e.getMessage());
//        }
//
//
//    }
//
//
//
//}