package com.shoesock.personalassistant1.models;

import java.io.Serializable;

public class UserModel implements Serializable {


    private String userName, userHashedPassword, userPhone, slat, hashedUserName;



    public String getUserName() {
        return userName;
    }

    public String getHashedUserName() {
        return hashedUserName;
    }

    public String getUserHashedPassword() {
        return userHashedPassword;
    }

    public String getSlat() {
        return slat;
    }

    public String getUserPhone() {
        return userPhone;
    }


    public  UserModel( String userName,  String userHashedPassword, String userPhone){
        this.userName = userName;
        this.userPhone = userPhone;
        this.userHashedPassword = userHashedPassword;

   }

   //    hashedUserName, saltPassword,userName, hashedPassword, phone);
   public UserModel(String hashedUserName, String slat, String userName, String userHashedPassword, String userPhone){
        this.userName = userName;
        this.userPhone = userPhone;
        this.userHashedPassword = userHashedPassword;
        this.slat = slat;
        this.hashedUserName = hashedUserName;
   }

































} // close the user class
