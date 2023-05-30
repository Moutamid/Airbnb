package com.moutamid.airbnb.models;

public class ChatModel {

    String message;
    String senderID, reciverID;
    String image;
    long timestamps;
    String name;

    public ChatModel() {
    }

    public ChatModel(String message, String senderID, String reciverID, String image, long timestamps, String name) {
        this.message = message;
        this.senderID = senderID;
        this.reciverID = reciverID;
        this.image = image;
        this.timestamps = timestamps;
        this.name = name;
    }

    public String getReciverID() {
        return reciverID;
    }

    public void setReciverID(String reciverID) {
        this.reciverID = reciverID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(long timestamps) {
        this.timestamps = timestamps;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
