package com.caletateam.caletapp.app.md.models;

public class CardViewSummary {
    private String babyname;
    private String photo;
    private boolean stressscore,actscore,respscore;
    private int totalscore;
    private int anomalies;

    public CardViewSummary(String babyname, String photo, boolean stressscore, boolean actscore, boolean respscore, int totalscore, int anomalies) {
        this.babyname = babyname;
        this.photo = photo;
        this.stressscore = stressscore;
        this.actscore = actscore;
        this.respscore = respscore;
        this.totalscore = totalscore;
        this.anomalies = anomalies;
    }

    public String getBabyname() {
        return babyname;
    }

    public void setBabyname(String babyname) {
        this.babyname = babyname;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public boolean isStressscore() {
        return stressscore;
    }

    public void setStressscore(boolean stressscore) {
        this.stressscore = stressscore;
    }

    public boolean isActscore() {
        return actscore;
    }

    public void setActscore(boolean actscore) {
        this.actscore = actscore;
    }

    public boolean isRespscore() {
        return respscore;
    }

    public void setRespscore(boolean respscore) {
        this.respscore = respscore;
    }

    public int getTotalscore() {
        return totalscore;
    }

    public void setTotalscore(int totalscore) {
        this.totalscore = totalscore;
    }

    public int getAnomalies() {
        return anomalies;
    }

    public void setAnomalies(int anomalies) {
        this.anomalies = anomalies;
    }
}
