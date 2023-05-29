package com.moutamid.airbnb.models;

public class RatingModel {
    String image, name, description;
    long starCount, timeStamp;

    public RatingModel() {
    }

    public RatingModel(String image, String name, String description, long starCount, long timeStamp) {
        this.image = image;
        this.name = name;
        this.description = description;
        this.starCount = starCount;
        this.timeStamp = timeStamp;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getStarCount() {
        return starCount;
    }

    public void setStarCount(long starCount) {
        this.starCount = starCount;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
