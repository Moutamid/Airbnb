package com.moutamid.airbnb.models;

public class FlightsModel {

    String departureTime, arrivalTime, duration, departureAirportCode, arrivalAirportCode;
    String airlines, stopoverCode;
    double price;
    String allianceCodes;

    public FlightsModel() {
    }

    public String getAllianceCodes() {
        return allianceCodes;
    }

    public void setAllianceCodes(String allianceCodes) {
        this.allianceCodes = allianceCodes;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDepartureAirportCode() {
        return departureAirportCode;
    }

    public void setDepartureAirportCode(String departureAirportCode) {
        this.departureAirportCode = departureAirportCode;
    }

    public String getArrivalAirportCode() {
        return arrivalAirportCode;
    }

    public void setArrivalAirportCode(String arrivalAirportCode) {
        this.arrivalAirportCode = arrivalAirportCode;
    }

    public String getAirlines() {
        return airlines;
    }

    public void setAirlines(String airlines) {
        this.airlines = airlines;
    }

    public String getStopoverCode() {
        return stopoverCode;
    }

    public void setStopoverCode(String stopoverCode) {
        this.stopoverCode = stopoverCode;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
