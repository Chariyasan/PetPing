package com.example.petping;

import com.google.firebase.auth.FirebaseAuth;

public class Content {
    private String ID;
    private String topic;
    private String story;
    private String url;
    private String tag;
    private String authorID;
    private String authorName;

    public Content(String ID, String topic, String story, String url, String tag, String authorID, String authorName) {
        this.ID = ID;
        this.topic = topic;
        this.story = story;
        this.url = url;
        this.tag = tag;
        this.authorID = authorID;
        this.authorName = authorName;
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

    public String getAuthorID() {
        return authorID;
    }

    public void setAuthorID(String authorID) {
        this.authorID = authorID;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
}
