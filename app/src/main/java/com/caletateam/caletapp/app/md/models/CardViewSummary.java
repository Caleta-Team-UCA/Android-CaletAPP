package com.caletateam.caletapp.app.md.models;

public class CardViewSummary {
    private String babyname;
    private long timestamp;
    private String photo;
    private String alert;

    public CardViewSummary(String babyname, long timestamp, String photo, String alert) {
        this.babyname = babyname;
        this.timestamp = timestamp;
        this.photo = photo;
        this.alert = alert;
    }

    public String getBabyname() {
        return babyname;
    }

    public void setBabyname(String babyname) {
        this.babyname = babyname;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }
}
