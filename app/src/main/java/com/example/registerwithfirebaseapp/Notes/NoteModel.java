package com.example.registerwithfirebaseapp.Notes;

public class NoteModel {
    String id;
    String note_title;
    String note_content;

    public NoteModel() {

    }

    public NoteModel(String id, String note_title, String note_content) {
        this.id = id;
        this.note_title = note_title;
        this.note_content = note_content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNote_title() {
        return note_title;
    }

    public void setNote_title(String note_title) {
        this.note_title = note_title;
    }

    public String getNote_content() {
        return note_content;
    }

    public void setNote_content(String note_content) {
        this.note_content = note_content;
    }
}
