package com.example.registerwithfirebaseapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    private EditText editTextRegisterFullName, editTextRegisterEmail,  editTextRegisterPwd, editTextRegisterYes;
    private ProgressBar progressBar;

    private static final String TAG = "Register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextRegisterFullName = findViewById(R.id.editText_register_full_name);
        editTextRegisterEmail = findViewById(R.id.editText_register_email);
        editTextRegisterPwd = findViewById(R.id.editText_register_password);
        editTextRegisterYes = findViewById(R.id.editText_register_yes);

        Button buttonRegister = findViewById(R.id.button_register);

        progressBar = findViewById(R.id.progressBar);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textFullName = editTextRegisterFullName.getText().toString();
                String textEmail = editTextRegisterEmail.getText().toString();
                String textPwd = editTextRegisterPwd.getText().toString();
                String textYes = editTextRegisterYes.getText().toString();

                if (TextUtils.isEmpty(textFullName)) {
                    Toast.makeText(Register.this, "Pls enter fullname", Toast.LENGTH_LONG).show();
                    editTextRegisterFullName.setError("Fullname is required");
                    editTextRegisterFullName.requestFocus();
                } else if (TextUtils.isEmpty(textEmail)) {
                    Toast.makeText(Register.this, "Pls enter email", Toast.LENGTH_LONG).show();
                    editTextRegisterEmail.setError("Email is required");
                    editTextRegisterEmail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
                    Toast.makeText(Register.this, "Pls re-enter email", Toast.LENGTH_LONG).show();
                    editTextRegisterEmail.setError("Valid email is required");
                    editTextRegisterEmail.requestFocus();
                } else if (TextUtils.isEmpty(textPwd)) {
                    Toast.makeText(Register.this, "Pls enter pwd", Toast.LENGTH_LONG).show();
                    editTextRegisterPwd.setError("Pwd is required");
                    editTextRegisterPwd.requestFocus();
                } else if (textPwd.length() < 6) {
                    Toast.makeText(Register.this, "Pls re-enter pwd, it should be not less than 6 digits", Toast.LENGTH_LONG).show();
                    editTextRegisterPwd.setError("Pwd should be not less than 6 digits");
                    editTextRegisterPwd.requestFocus();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(textFullName, textEmail, textPwd, textYes);
//                    registerUser(textFullName, textEmail, textPwd, textYes);
                    registerUser(textFullName, textEmail, textPwd, textYes);

                }


            }
        });


    }

    private void registerUser(String textFullName, String textEmail, String textPwd, String textYes) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        //Create User Profile
        auth.createUserWithEmailAndPassword(textEmail, textPwd).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
//                    Toast.makeText(Register.this, "User was successfully registered ", Toast.LENGTH_LONG).show();
                    FirebaseUser firebaseUser = auth.getCurrentUser();

                    //update Display Name of User -----> so we cat delete ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(textFullName) abd in ReadWrite.java         this.fullName = textFullName;
                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(textFullName).build();
                    firebaseUser.updateProfile(profileChangeRequest);

                    //Enter User Data in Firebase Realtime Database
                    ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(textFullName, textYes);
//                    ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(textDoB, textGender, textMobile);

                    //Etracting User reference from FDbasse for "Registered users"
                    DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");

                    referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                //send vereficaation on email
//                                firebaseUser.sendEmailVerification();
//                                Toast.makeText(Register.this, "User registered successfully. Pls verify your email ", Toast.LENGTH_LONG).show();


                                //open profile after successful registration
                                Intent intent = new Intent(Register.this, UserProfileActivity.class);
                                //to prevent user from returnung back to RegAct on pressing back button after registration
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish(); // to close RegAct


                            } else {
                                Toast.makeText(Register.this, "User registered  failed. Pls try again ", Toast.LENGTH_LONG).show();
                            }
                            //Hide progressBar whether User creation is  successful or failed
                            progressBar.setVisibility(View.GONE);

                        }
                    });


                } else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        editTextRegisterPwd.setError("Your password is too weak/ Use a mix of alphabets, numbers, and special symbols");
                        editTextRegisterPwd.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        editTextRegisterPwd.setError("Your email is invalid or already in use. Kindly re-enter");
                        editTextRegisterPwd.requestFocus();
                    } catch (FirebaseAuthUserCollisionException e) {
                        editTextRegisterPwd.setError("User is already registered with this email. Use another");
                        editTextRegisterPwd.requestFocus();
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_LONG).show();

                    }
                    //Hide progressBar whether User creation is  successful or failed

                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }
}
