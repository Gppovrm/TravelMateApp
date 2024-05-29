package com.example.registerwithfirebaseapp.CheckItems;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;


public class TouchHelper extends ItemTouchHelper.SimpleCallback {
    private CheckItemsRecyclerView adapter;

    public TouchHelper(CheckItemsRecyclerView adapter) {
        super(0 , ItemTouchHelper.RIGHT);
        this.adapter = adapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        final int position = viewHolder.getAdapterPosition();

        if (direction == ItemTouchHelper.RIGHT){
            adapter.deleteCheckItem(position);
        }
    }
}
