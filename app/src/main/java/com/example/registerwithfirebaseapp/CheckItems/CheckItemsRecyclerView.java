package com.example.registerwithfirebaseapp.CheckItems;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.registerwithfirebaseapp.CheckItemsActivity;
import com.example.registerwithfirebaseapp.CreateNewCheckItemFragment;
import com.example.registerwithfirebaseapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CheckItemsRecyclerView extends RecyclerView.Adapter<CheckItemsRecyclerView.CheckItemViewHolder> {
    List<CheckItemModel> checkItemModelList;

    private CheckItemsActivity activity;

    public CheckItemsRecyclerView(CheckItemsActivity checkItemsActivity, List<CheckItemModel> checkItemModels) {
        this.checkItemModelList = checkItemModels;
        activity = checkItemsActivity;
    }

    @NonNull
    @Override
    public CheckItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.check_item_row, parent, false);
        return new CheckItemViewHolder(view);
    }

    public void deleteCheckItem(int position) {
        CheckItemModel checkItemModel = checkItemModelList.get(position);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users").child(firebaseUser.getUid()).child("yes").child("items_list").child(checkItemModel.getId());
        databaseReference.removeValue();
        checkItemModelList.remove(position);
    }

    public Context getContext() {
        return activity;
    }


    @Override
    public void onBindViewHolder(@NonNull CheckItemViewHolder holder, int position) {
        CheckItemModel checkItemModel = checkItemModelList.get(position);

        holder.item_name_checkbox.setText(checkItemModel.getItem_name());
        holder.item_name_checkbox.setChecked(toBoolean(checkItemModel.getStatus()));

        holder.item_name_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = auth.getCurrentUser();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users").child(firebaseUser.getUid()).child("yes").child("items_list");

                if (isChecked) {
                    checkItemModel.setStatus("1");
                    databaseReference.child(checkItemModel.getId()).setValue(checkItemModel);
                } else {
                    checkItemModel.setStatus("0");
                    databaseReference.child(checkItemModel.getId()).setValue(checkItemModel);
                }
            }
        });
    }

    private boolean toBoolean(String status){
        int flag = Integer.parseInt(status);
        return flag != 0;
    }

    @Override
    public int getItemCount() {
        return checkItemModelList.size();
    }


    public class CheckItemViewHolder extends RecyclerView.ViewHolder {
        CheckBox item_name_checkbox;

        public CheckItemViewHolder(@NonNull View itemView) {
            super(itemView);

            item_name_checkbox = itemView.findViewById(R.id.item_name_checkbox);
        }
    }
}