package com.example.registerwithfirebaseapp.Notes;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.registerwithfirebaseapp.R;

import java.util.List;

public class NoteItemsRecyclerView extends RecyclerView.Adapter {

    List<NoteModel> noteModelList;
    NoteCLickListener noteCLickListener;
    public NoteItemsRecyclerView(List<NoteModel> noteModels, NoteCLickListener noteCLickListener){
        this.noteModelList=noteModels;
        this.noteCLickListener=noteCLickListener;
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder{

        public TextView note_title, note_content;
        public CardView note_card_item;
        public RelativeLayout note_parent;

        public NoteViewHolder(@NonNull View itemView){
            super(itemView);
            note_title=itemView.findViewById(R.id.note_title);
            note_content=itemView.findViewById(R.id.note_content);
            note_parent=itemView.findViewById(R.id.note_parent);
            note_card_item=itemView.findViewById(R.id.note_card_item);
        }
    }

    //access the layout file of note_row
    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_row,null);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        NoteViewHolder noteViewHolder=(NoteViewHolder) holder;
        noteViewHolder.note_title.setText(noteModelList.get(position).getNote_title());
        noteViewHolder.note_content.setText(noteModelList.get(position).getNote_content());
        noteViewHolder.note_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noteCLickListener.onClickItem(noteModelList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return noteModelList.size();
    }
}
