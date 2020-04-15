package com.example.petping;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.auth.FirebaseAuth;

public class Content implements Parcelable {
    private String ID;
    private String topic;
    private String story;
    private String url;
    private String tag;
    private String shelterID;

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

    public Content(String ID, String topic, String story, String url, String tag, String shelterID) {
        this.ID = ID;
        this.topic = topic;
        this.story = story;
        this.url = url;
        this.tag = tag;
        this.shelterID = shelterID;

    }

    public void readFromParcel(Parcel in) {
        ID = in.readString();
        topic = in.readString();
        story = in.readString();
        url = in.readString();
        tag = in.readString();
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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
        dest.writeString(tag);
        dest.writeString(shelterID);
    }
}
