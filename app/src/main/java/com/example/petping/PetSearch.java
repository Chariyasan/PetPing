package com.example.petping;

import android.os.Parcel;
import android.os.Parcelable;

public class PetSearch implements Parcelable {
    private String ID;
    private String name;
    private String type;
    private String colour;
    private String sex;
    private String age;
    private String breed;
    private String size;
    private String url;
    private String weight;
    private String character;
    private String marking;
    private String health;
    private String foundLoc;
    private String status;
    private String story;
    private String shelterID;

    public PetSearch(String ID, String name, String type, String colour, String sex, String age, String breed, String size, String url,
                     String weight, String character, String marking, String health, String foundLoc, String status, String story, String shelterID) {
        this.ID = ID;
        this.name = name;
        this.type = type;
        this.colour = colour;
        this.sex = sex;
        this.age = age;
        this.breed = breed;
        this.size = size;
        this.url = url;
        this.weight = weight;
        this.character = character;
        this.marking = marking;
        this.health = health;
        this.foundLoc = foundLoc;
        this.status = status;
        this.story = story;
        this.shelterID = shelterID;
    }

    protected PetSearch(Parcel in) {
        ID = in.readString();
        name = in.readString();
        type = in.readString();
        colour = in.readString();
        sex = in.readString();
        age = in.readString();
        breed = in.readString();
        size = in.readString();
        url = in.readString();
        weight = in.readString();
        character = in.readString();
        marking = in.readString();
        health = in.readString();
        foundLoc = in.readString();
        status = in.readString();
        story = in.readString();
        shelterID = in.readString();
    }

    public static final Creator<PetSearch> CREATOR = new Creator<PetSearch>() {
        @Override
        public PetSearch createFromParcel(Parcel in) {
            return new PetSearch(in);
        }

        @Override
        public PetSearch[] newArray(int size) {
            return new PetSearch[size];
        }
    };

    public PetSearch() {

    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getMarking() {
        return marking;
    }

    public void setMarking(String marking) {
        this.marking = marking;
    }

    public String getHealth() {
        return health;
    }

    public void setHealth(String health) {
        this.health = health;
    }

    public String getFoundLoc() {
        return foundLoc;
    }

    public void setFoundLoc(String foundLoc) {
        this.foundLoc = foundLoc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getColour() {
        return colour;
    }

    public String getSex() {
        return sex;
    }

    public String getAge() {
        return age;
    }

    public String getShelterID() {
        return shelterID;
    }

    public void setShelterID(String shelterID) {
        this.shelterID = shelterID;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setAge(String age) {
        this.age = age;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(name);
        dest.writeString(type);
        dest.writeString(colour);
        dest.writeString(sex);
        dest.writeString(age);
        dest.writeString(breed);
        dest.writeString(size);
        dest.writeString(url);
        dest.writeString(weight);
        dest.writeString(character);
        dest.writeString(marking);
        dest.writeString(health);
        dest.writeString(foundLoc);
        dest.writeString(status);
        dest.writeString(story);
        dest.writeString(shelterID);
    }
}
