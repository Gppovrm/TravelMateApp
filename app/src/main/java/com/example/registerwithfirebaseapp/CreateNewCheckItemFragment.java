package com.example.registerwithfirebaseapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.registerwithfirebaseapp.CheckItems.CheckItemModel;
import com.example.registerwithfirebaseapp.CheckItems.OnDialogCloseListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateNewCheckItemFragment extends BottomSheetDialogFragment {

    public static final String TAG = "CreateNewCheckItemFragment";

    private EditText new_item_name;
    private Button save_btn;
    //private String id = "";
    private Context context;

    public static CreateNewCheckItemFragment newInstance(){
        return new CreateNewCheckItemFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.create_new_check_item , container , false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        new_item_name = view.findViewById(R.id.new_check_item_name_edit);
        save_btn = view.findViewById(R.id.save_new_check_item_btn);

        /*boolean isUpdate = false;

        final Bundle bundle = getArguments();
        if (bundle != null){
            isUpdate = true;
            String item_name = bundle.getString("item_name");
            id = bundle.getString("id");

            new_item_name.setText(item_name);
        }*/


        //boolean finalIsUpdate = isUpdate;
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = auth.getCurrentUser();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users").child(firebaseUser.getUid()).child("yes").child("items_list");

                String item_name = new_item_name.getText().toString();

                if (item_name.isEmpty()) {
                    Toast.makeText(context, "Empty task not Allowed !!", Toast.LENGTH_SHORT).show();
                } else {
                    String id = databaseReference.push().getKey();
                    CheckItemModel checkItemModel = new CheckItemModel(id, item_name, "0");
                    databaseReference.child(id).setValue(checkItemModel);
                }

                /*if (finalIsUpdate){
                    Toast.makeText(context, "Task Updated", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (item_name.isEmpty()) {
                        Toast.makeText(context, "Empty task not Allowed !!", Toast.LENGTH_SHORT).show();
                    } else {
                        String id = databaseReference.push().getKey();
                        CheckItemModel checkItemModel = new CheckItemModel(id, item_name, "0");
                        databaseReference.child(id).setValue(checkItemModel);
                    }
                }*/
                dismiss();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity instanceof  OnDialogCloseListener){
            ((OnDialogCloseListener)activity).onDialogClose(dialog);
        }
    }
}