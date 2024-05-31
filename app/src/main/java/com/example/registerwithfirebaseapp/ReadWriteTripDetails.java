package com.example.registerwithfirebaseapp;

public class ReadWriteTripDetails {
    public String tripTitle, city, dateStart, dateEnd, timeZone;

    //Constructor
    public ReadWriteTripDetails(){};


    public ReadWriteTripDetails(String textTripTitle, String textCity, String textDateStart, String textDateEnd, String textTimeZone){
        this.tripTitle = textTripTitle;
        this.city = textCity;
        this.dateStart = textDateStart;
        this.dateEnd = textDateEnd;
        this.timeZone = textTimeZone;

    }
}

