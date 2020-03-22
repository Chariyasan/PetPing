package com.example.petping;

public class HomeShelter {
    String uID;
    String petID;
    String userName;
    String petName;
    String petStatus;
    String URL;

    public HomeShelter(String uID, String petID, String userName, String petName, String petStatus, String URL) {
        this.uID = uID;
        this.petID = petID;
        this.userName = userName;
        this.petName = petName;
        this.petStatus = petStatus;
        this.URL = URL;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getPetID() {
        return petID;
    }

    public void setPetID(String petID) {
        this.petID = petID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getPetStatus() {
        return petStatus;
    }

    public void setPetStatus(String petStatus) {
        this.petStatus = petStatus;
    }
}
