package com.example.petping;

import android.os.Parcel;
import android.os.Parcelable;

public class HomeShelter implements Parcelable {
    String uID;
    String petID;
    String userName;
    String userImage;
    String petName;
    String petStatus;
    String URL;
    String date;

    public HomeShelter(String petID, String uID, String userName, String userImage, String petName, String petStatus, String URL, String date) {
        this.uID = uID;
        this.petID = petID;
        this.userName = userName;
        this.userImage = userImage;
        this.petName = petName;
        this.petStatus = petStatus;
        this.URL = URL;
        this.date = date;
    }

    protected HomeShelter(Parcel in) {
        uID = in.readString();
        petID = in.readString();
        userName = in.readString();
        userImage = in.readString();
        petName = in.readString();
        petStatus = in.readString();
        URL = in.readString();
        date = in.readString();
    }

    public String getPetDate() {
        return date;
    }

    public void setPetDate(String date) {
        this.date = date;
    }

    public static Creator<HomeShelter> getCREATOR() {
        return CREATOR;
    }

    public static final Creator<HomeShelter> CREATOR = new Creator<HomeShelter>() {
        @Override
        public HomeShelter createFromParcel(Parcel in) {
            return new HomeShelter(in);
        }

        @Override
        public HomeShelter[] newArray(int size) {
            return new HomeShelter[size];
        }
    };

    public HomeShelter() {

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uID);
        dest.writeString(petID);
        dest.writeString(userName);
        dest.writeString(userImage);
        dest.writeString(petName);
        dest.writeString(petStatus);
        dest.writeString(URL);
        dest.writeString(date);
    }
}
