package com.example.registerwithfirebaseapp.CheckItems;

public class CheckItemModel {
    String id;
    String item_name;
    String status;

    public CheckItemModel(String id, String item_name, String status) {
        this.id = id;
        this.item_name = item_name;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
