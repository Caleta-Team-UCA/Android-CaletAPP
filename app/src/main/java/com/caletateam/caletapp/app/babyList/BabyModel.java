package com.caletateam.caletapp.app.babyList;

public class BabyModel {
    private String name;
    private int id;
    private String photo;

    public BabyModel(String name, int id, String photo) {
        this.name = name;
        this.id = id;
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
