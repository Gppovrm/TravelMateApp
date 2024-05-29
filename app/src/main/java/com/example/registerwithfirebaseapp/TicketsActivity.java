package com.example.registerwithfirebaseapp;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.registerwithfirebaseapp.Tickets.CreateTiketsActivity;
import com.example.registerwithfirebaseapp.Tickets.UploadPDF;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TicketsActivity extends AppCompatActivity {

    ListView myPDFListView;

    DatabaseReference databaseReference;
    List<UploadPDF> uploadPDFS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickets);

//        RecyclerView ticketslist = findViewById(R.id.tickets_list);

        TextView trip_title = findViewById(R.id.name_vacation);
        TextView trip_city = findViewById(R.id.location_vacation);
        TextView date_start = findViewById(R.id.date_start);
        TextView date_end = findViewById(R.id.date_end);

        ImageView back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TicketsActivity.this, MainActivity.class));
            }
        });


        ImageView add_new_ticket_btn = findViewById(R.id.add_new_ticket_btn);
        add_new_ticket_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TicketsActivity.this, CreateTiketsActivity.class));
            }
        });

        Button ButtonNotes = findViewById(R.id.notes_btn);
        ButtonNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TicketsActivity.this, NotesActivity.class));

            }
        });

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users");

        databaseReference.child(firebaseUser.getUid()).child("yes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                trip_title.setText(snapshot.child("tripTitle").getValue().toString());
                trip_city.setText(snapshot.child("city").getValue().toString());
                date_start.setText(snapshot.child("dateStart").getValue().toString());
                date_end.setText(snapshot.child("dateEnd").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TicketsActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });


        myPDFListView = (ListView) findViewById(R.id.myListView);
        uploadPDFS = new ArrayList<>();

        viewAllFiles();


        myPDFListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                UploadPDF uploadPDF = uploadPDFS.get(position);

                Intent intent = new Intent();
                intent.setType(Intent.ACTION_VIEW);   ///////////aditional
                intent.setData(Uri.parse(uploadPDF.getUrl()));
                startActivity(intent);

            }
        });
    }
    private void viewAllFiles() {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users").child(firebaseUser.getUid()).child("yes").child("uploads");
//
//
//        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                /////////////////////////////////////////UploadPDF.class SELF Change
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    UploadPDF uploadPDF = postSnapshot.getValue(UploadPDF.class);
                    uploadPDFS.add(uploadPDF);

                }
                String[] uploads = new String[uploadPDFS.size()];

                for (int i = 0; i < uploads.length; i++) {

                    uploads[i] = uploadPDFS.get(i).getName();
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, uploads) {

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {

                        View view = super.getView(position, convertView, parent);

                        TextView myText = (TextView) view.findViewById(android.R.id.text1);
                        myText.setTextColor(Color.BLACK);

                        return view;
                    }
                };
                myPDFListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError dataSnapshot) {

            }
        });
    }
}