package com.shoesock.personalassistant1.models;

import java.io.Serializable;

public class UserModel implements Serializable {
    private String userName, userLastName, userHashedPassword, userPhone, slat, hashedUserName;

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

    public String getUserLastName() {
        return userLastName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public UserModel(String hashedUserName, String slat, String userHashedPassword, String userLastName,String userPhone) {
        this.userPhone = userPhone;
        this.userLastName = userLastName;
        this.userHashedPassword = userHashedPassword;
        this.slat = slat;
        this.hashedUserName = hashedUserName;
    }

    // Default constructor for Firebase deserialization
    public UserModel() {
        // Empty constructor required by Firebase
    }

































} // close the user class
