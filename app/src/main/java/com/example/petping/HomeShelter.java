package com.example.petping;

public class HomeShelter {
    String uID;
    String userName;
    String petName;
    String petStatus;

    public HomeShelter(String uID, String userName, String petName, String petStatus) {
        this.uID = uID;
        this.userName = userName;
        this.petName = petName;
        this.petStatus = petStatus;
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
