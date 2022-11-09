package com.example.callingallvisitors.Models;

public class Visitor {
    private String name;
    private String surname;
    private String idNumber;
    private String resident;
    private String residentPhoneNumber;
    private String placeVisited;
    private String dateVisited;
    private boolean hasCheckedIn;
    private String checkOutTime;
    //TODO
    private String url;

    public Visitor()
    {

    }
    public Visitor(String name, String surname, String idNumber, String resident, String residentPhoneNumber, String placeVisited, String dateVisited) {
        this.name = name;
        this.surname = surname;
        this.idNumber = idNumber;
        this.resident = resident;
        this.residentPhoneNumber = residentPhoneNumber;
        this.placeVisited = placeVisited;
        this.dateVisited = dateVisited;
        hasCheckedIn = true;
        checkOutTime = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getResident() {
        return resident;
    }

    public void setResident(String resident) {
        this.resident = resident;
    }

    public String getResidentPhoneNumber() {
        return residentPhoneNumber;
    }

    public void setResidentPhoneNumber(String residentPhoneNumber) {
        this.residentPhoneNumber = residentPhoneNumber;
    }

    public String getPlaceVisited() {
        return placeVisited;
    }

    public void setPlaceVisited(String placeVisited) {
        this.placeVisited = placeVisited;
    }

    public String getDateVisited() {
        return dateVisited;
    }

    public void setDateVisited(String dateVisited) {
        this.dateVisited = dateVisited;
    }

    public boolean isHasCheckedIn() {
        return hasCheckedIn;
    }

    public void setHasCheckedIn(boolean hasCheckedIn) {
        this.hasCheckedIn = hasCheckedIn;
    }

    public String getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
