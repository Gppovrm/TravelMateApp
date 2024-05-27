package com.example.registerwithfirebaseapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class NewTripActivity extends AppCompatActivity {

    private EditText editTripTitle, editTextCity, editTextDateStart, editTextDateEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trip);

        editTripTitle = findViewById(R.id.editText_trip_title);
        editTextCity = findViewById(R.id.editText_city);
        editTextDateStart= findViewById(R.id.editText_date_start);
        editTextDateEnd = findViewById(R.id.editText_date_end);


        Button buttonSubmit = findViewById(R.id.button_submit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textTripTitle = editTripTitle.getText().toString();
                String textCity = editTextCity.getText().toString();
                String textDateStart = editTextDateStart.getText().toString();
                String textDateEnd = editTextDateEnd.getText().toString();

                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = auth.getCurrentUser();
////////////////////////////////////////////////
                ReadWriteTripDetails writeTripDetails = new ReadWriteTripDetails(textTripTitle, textCity, textDateStart, textDateEnd);



                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users");


                if (textTripTitle.isEmpty() || textCity.isEmpty() || textDateStart.isEmpty() || textDateEnd.isEmpty() ) {
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


}
