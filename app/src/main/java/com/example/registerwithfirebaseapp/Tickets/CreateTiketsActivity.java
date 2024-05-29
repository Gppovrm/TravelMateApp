package com.example.registerwithfirebaseapp.Tickets;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.registerwithfirebaseapp.Notes.NoteModel;
import com.example.registerwithfirebaseapp.R;
import com.example.registerwithfirebaseapp.TicketsActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CreateTiketsActivity extends AppCompatActivity {

    EditText editPDFName;
    Button btn_upload;

    StorageReference storageReference;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tikets);

        editPDFName = findViewById(R.id.txt_pdfName);
        btn_upload = findViewById(R.id.btn_upload);



//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        FirebaseUser firebaseUser = auth.getCurrentUser();
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users").child(firebaseUser.getUid()).child("yes").child("uploads");
        storageReference = FirebaseStorage.getInstance().getReference();
//        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPDFFile();
            }
        });

    }

    private void selectPDFFile() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select PDF File"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uploadPDFFile(data.getData());

        }
    }

    private void uploadPDFFile(Uri data) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("UpLoading...");
        progressDialog.show();

        StorageReference reference = storageReference.child("uploads/" + System.currentTimeMillis() + ".pdf");
        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uri.isComplete()) ;
                        Uri url = uri.getResult();

                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users").child(firebaseUser.getUid()).child("yes").child("uploads");

//                        String id = databaseReference.push().getKey();
//                        UploadPDF UploadPDF = new UploadPDF(id, editPDFName.getText().toString(), url.toString());
//                        databaseReference.child(id).setValue(UploadPDF);
//
////                        UploadPDF uploadPDF = new UploadPDF(editPDFName.getText().toString(), url.toString());
////                        databaseReference.child(databaseReference.push().getKey()).setValue(uploadPDF);
//
//                        Toast.makeText(CreateTiketsActivity.this, "File Uploaded!!!", Toast.LENGTH_LONG).show();
//                        progressDialog.dismiss();
                        UploadPDF UploadPDF = new UploadPDF(editPDFName.getText().toString(), url.toString());
                        databaseReference.child(databaseReference.push().getKey()).setValue(UploadPDF);

                        Toast.makeText(CreateTiketsActivity.this, "File Uploaded!!!", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded:" + (int) progress + "%");


                    }
                });
    }
    //DO NOT REMOVE!!!!!
    public void btn_action(View view) {
        startActivity(new Intent(getApplicationContext(), TicketsActivity.class));//TicketsActivity ---View_PDF_Files.class
    }
}