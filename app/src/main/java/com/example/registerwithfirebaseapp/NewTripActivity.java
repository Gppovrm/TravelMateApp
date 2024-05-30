package com.example.registerwithfirebaseapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

public class NewTripActivity extends AppCompatActivity {

    private EditText editTripTitle, editTextDateStart, editTextDateEnd;
    String textCity;
    //Calendar current;
    Spinner spinner;
    //long miliSeconds;
    ArrayAdapter<String> idAdapter;
    SimpleDateFormat sdf;
    //Date resultDate;
    String textTimeZone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trip);

        editTripTitle = findViewById(R.id.editText_trip_title);
        //editTextCity = findViewById(R.id.editText_city);
        editTextDateStart = findViewById(R.id.editText_date_start);
        editTextDateEnd = findViewById(R.id.editText_date_end);


        spinner = (Spinner) findViewById(R.id.timezone_spinner);

        //String[] idArray = TimeZone.getAvailableIDs();

        /*String[] idArray = Arrays.stream(TimeZone.getAvailableIDs())
                .map(id -> {
                    String[] tokens = id.replace("_", " ").split("/");
                    String name = (tokens.length == 2 || tokens.length == 3) ? tokens[1] + " (" + tokens[0] + ")" : id;
                    return name;
                })
                .collect(Collectors.toList()).toArray(new String[0]);*/

        List<String> idList = new ArrayList<>();
        for (String id : TimeZone.getAvailableIDs()) {
            if (id.contains("/") & !(id.contains("Etc"))) {
                idList.add(id);
            }
        }
        String[] idArray = idList.toArray(new String[0]);


        //sdf = new SimpleDateFormat("EEEE, ddMMMM yyyy HH:mm:ss");

        idAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, idArray);

        idAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(idAdapter);

        //getGMTTime();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //getGMTTime();
                textTimeZone = (String) (parent.getItemAtPosition(position));
                String[] tokens = textTimeZone.split("/");
                textCity = tokens[1];

                /*TimeZone timeZone = TimeZone.getTimeZone(selectedId);
                String timeZoneName = timeZone.getDisplayName();

                int timeZoneOffset = timeZone.getRawOffset() / (60 * 1000);

                int hrs = timeZoneOffset / 60;
                int mins = timeZoneOffset % 60;

                miliSeconds = miliSeconds + timeZone.getRawOffset();

                resultDate = new Date(miliSeconds);
                System.out.println(sdf.format(resultDate));

                miliSeconds = 0;*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Button buttonSubmit = findViewById(R.id.button_submit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textTripTitle = editTripTitle.getText().toString();
                //String textCity = editTextCity.getText().toString();
                String textDateStart = editTextDateStart.getText().toString();
                String textDateEnd = editTextDateEnd.getText().toString();

                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = auth.getCurrentUser();
////////////////////////////////////////////////
                ReadWriteTripDetails writeTripDetails = new ReadWriteTripDetails(textTripTitle, textCity, textDateStart, textDateEnd, textTimeZone);


                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users");


                if (textTripTitle.isEmpty() || textDateStart.isEmpty() || textDateEnd.isEmpty()) {
                    Toast.makeText(NewTripActivity.this, "Please efill ALL fields", Toast.LENGTH_LONG).show();
                } else {
                    databaseReference.child(firebaseUser.getUid()).child("yes").setValue(writeTripDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(NewTripActivity.this, "Trip has been successfully created", Toast.LENGTH_LONG).show();
                                finish();

                            }
                        }
                    });
                }
            }
        });
    }

/*    private void getGMTTime(){
        current = Calendar.getInstance();

        miliSeconds = current.getTimeInMillis();

        TimeZone tzCurrent = current.getTimeZone();
        int offset = tzCurrent.getRawOffset();
        if(tzCurrent.inDaylightTime(new Date())){
            offset = offset + tzCurrent.getDSTSavings();
        }

        miliSeconds = miliSeconds - offset;

        resultDate = new Date(miliSeconds);
        System.out.println(sdf.format(resultDate));
    }*/
}
