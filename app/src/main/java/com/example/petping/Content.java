package com.example.petping;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.auth.FirebaseAuth;

public class Content implements Parcelable {
    private String ID;
    private String topic;
    private String story;
    private String url;
    private String shelterID;
    private String date;
    private String time;

    public Content(Parcel in) {
        super();
        readFromParcel(in);
    }

    public static final Parcelable.Creator<Content> CREATOR = new Parcelable.Creator<Content>() {
        public Content createFromParcel(Parcel in) {
            return new Content(in);
        }

        public Content[] newArray(int size) {

            return new Content[size];
        }

    };

    public Content(String ID, String topic, String story, String url, String shelterID, String date, String time) {
        this.ID = ID;
        this.topic = topic;
        this.story = story;
        this.url = url;
        this.shelterID = shelterID;
        this.date = date;
        this.time = time;
    }

    public void readFromParcel(Parcel in) {
        ID = in.readString();
        topic = in.readString();
        story = in.readString();
        url = in.readString();
        shelterID = in.readString();
    }


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getShelterID() {
        return shelterID;
    }

    public void setShelterID(String shelterID) {
        this.shelterID = shelterID;
    }

    public static Creator<Content> getCREATOR() {
        return CREATOR;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(topic);
        dest.writeString(story);
        dest.writeString(url);
        dest.writeString(shelterID);
        dest.writeString(date);
        dest.writeString(time);
    }
}
