package com.example.registerwithfirebaseapp;

public class ReadWriteTripDetails {
    public String tripTitle, city, dateStart, dateEnd;
//    public String doB, gender, mobile;

    //Constructor
    public ReadWriteTripDetails(){};


    public ReadWriteTripDetails(String textTripTitle, String textCity, String textDateStart, String textDateEnd){
//    public ReadWriteUserDetails(String textDoB, String textGender, String textMobile) {

        this.tripTitle = textTripTitle;
        this.city = textCity;
        this.dateStart = textDateStart;
        this.dateEnd = textDateEnd;
    }
}

