package com.moutamid.airbnb.models;

public class ChatListModel {
    String ID, image, name, message;
    long timeStamp;

    public ChatListModel() {
    }

    public ChatListModel(String ID, String image, String name, String message, long timeStamp) {
        this.ID = ID;
        this.image = image;
        this.name = name;
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
