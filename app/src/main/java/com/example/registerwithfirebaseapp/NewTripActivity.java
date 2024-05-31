package com.example.registerwithfirebaseapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
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
import java.util.Locale;
import java.util.TimeZone;
import java.util.stream.Collectors;

public class NewTripActivity extends AppCompatActivity {

    private EditText editTripTitle, editTextDateStart, editTextDateEnd;
    String textCity;
    Spinner spinner;
    ArrayAdapter<String> idAdapter;
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


        idAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, idArray);

        idAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(idAdapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textTimeZone = (String) (parent.getItemAtPosition(position));
                String[] tokens = textTimeZone.split("/");
                textCity = tokens[1];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        editTextDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateSet(editTextDateStart);
            }
        });

        editTextDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateSet(editTextDateEnd);
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

    private void DateSet(EditText editTextDate){
        MaterialDatePicker<Long> materialDatePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Выберите дату")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                String date = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date(selection));
                editTextDate.setText(date);
            }
        });
        materialDatePicker.show(getSupportFragmentManager(), "tag");
    }
}
