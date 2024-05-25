package com.example.registerwithfirebaseapp;

public class ReadWriteUserDetails {
        public String fullName, yes;
//    public String doB, gender, mobile;

    //Constructor
    public ReadWriteUserDetails(){};


        public ReadWriteUserDetails(String textFullName, String textYes){
//    public ReadWriteUserDetails(String textDoB, String textGender, String textMobile) {

        this.fullName = textFullName;
        this.yes = textYes;
    }
}
