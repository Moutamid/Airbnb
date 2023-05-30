package com.moutamid.airbnb.models;

public class SpaceModel {

    String ID, userID, name, image, description;
    String city, country;
    double price;
    long timeStamp;
    long guestNum, bedNum, batNum;
    String startDate, endDate;
    long startTime, endTime;
    long star1,star2,star3,star4,star5, ratingCount;
    public SpaceModel() {}

    public SpaceModel(String ID, String userID, String name, String image, String description, String city, String country, double price, long timeStamp, long guestNum, long bedNum, long batNum, String startDate, String endDate, long startTime, long endTime, long star1, long star2, long star3, long star4, long star5, long ratingCount) {
        this.ID = ID;
        this.userID = userID;
        this.name = name;
        this.image = image;
        this.description = description;
        this.city = city;
        this.country = country;
        this.price = price;
        this.timeStamp = timeStamp;
        this.guestNum = guestNum;
        this.bedNum = bedNum;
        this.batNum = batNum;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.star1 = star1;
        this.star2 = star2;
        this.star3 = star3;
        this.star4 = star4;
        this.star5 = star5;
        this.ratingCount = ratingCount;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getGuestNum() {
        return guestNum;
    }

    public void setGuestNum(long guestNum) {
        this.guestNum = guestNum;
    }

    public long getBedNum() {
        return bedNum;
    }

    public void setBedNum(long bedNum) {
        this.bedNum = bedNum;
    }

    public long getBatNum() {
        return batNum;
    }

    public void setBatNum(long batNum) {
        this.batNum = batNum;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public long getStar1() {
        return star1;
    }

    public void setStar1(long star1) {
        this.star1 = star1;
    }

    public long getStar2() {
        return star2;
    }

    public void setStar2(long star2) {
        this.star2 = star2;
    }

    public long getStar3() {
        return star3;
    }

    public void setStar3(long star3) {
        this.star3 = star3;
    }

    public long getStar4() {
        return star4;
    }

    public void setStar4(long star4) {
        this.star4 = star4;
    }

    public long getStar5() {
        return star5;
    }

    public void setStar5(long star5) {
        this.star5 = star5;
    }

    public long getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(long ratingCount) {
        this.ratingCount = ratingCount;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
