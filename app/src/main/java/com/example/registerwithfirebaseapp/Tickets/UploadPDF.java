package com.example.registerwithfirebaseapp.Tickets;

public class UploadPDF {

//    String id;

    public String name;
    public String url;

    public UploadPDF() {
    }

    public UploadPDF( String name, String url) {
//        this.id = id;
        this.name = name;
        this.url = url;
    }


//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
