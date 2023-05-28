package com.moutamid.airbnb.models;

public class UserModel {

    String ID, name, dob, email, password, phone, image;

    public UserModel() {
    }

    public UserModel(String ID, String name, String dob, String email, String password, String phone, String image) {
        this.ID = ID;
        this.name = name;
        this.dob = dob;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.image = image;
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

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
