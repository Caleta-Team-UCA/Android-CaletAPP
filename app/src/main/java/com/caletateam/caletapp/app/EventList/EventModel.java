package com.caletateam.caletapp.app.EventList;

public class EventModel {
    private int eventID;
    private int type;
    private String comments;
    private boolean anomaly;
    private String name;
    private long creationtime;

    public EventModel(int eventID, int type, String comments, boolean anomaly, String name, long creationtime) {
        this.eventID = eventID;
        this.type = type;
        this.comments = comments;
        this.anomaly = anomaly;
        this.name = name;
        this.creationtime = creationtime;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public boolean getAnomaly() {
        return anomaly;
    }

    public void setAnomaly(boolean anomaly) {
        this.anomaly = anomaly;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCreationtime() {
        return creationtime;
    }

    public void setCreationtime(long creationtime) {
        this.creationtime = creationtime;
    }
}
