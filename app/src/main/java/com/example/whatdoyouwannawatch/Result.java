package com.example.whatdoyouwannawatch;

import java.sql.Time;
import java.util.List;
import java.util.UUID;

public class Result {
    private String UID;
    private Media finalDecision;
    private List<User> users;
    private List<Media> options;
    private Time decisionMade;
    private Time showTime;

    //constructor
    public Result() {
        generateUID();
    }

    public Result(Media finalDecision, List<User> users, List<Media> options, Time decisionMade, Time showTime) {
        this.finalDecision = finalDecision;
        this.users = users;
        this.options = options;
        this.decisionMade = decisionMade;
        this.showTime = showTime;
        generateUID();
    }

    //display the media
    public void display(){}

    //add the media to history
    public void addToHistory(List<User> users){
        for(User u: users){
            u.addHistory(this);
        }
    }

    //Generate a random unique ID
    private void generateUID(){
        this.UID = UUID.randomUUID().toString();
    }

    //getters and setters
    public Media getFinalDecision() {
        return finalDecision;
    }

    public void setFinalDecision(Media finalDecision) {
        this.finalDecision = finalDecision;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Media> getOptions() {
        return options;
    }

    public void setOptions(List<Media> options) {
        this.options = options;
    }

    public Time getDecisionMade() {
        return decisionMade;
    }

    public void setDecisionMade(Time decisionMade) {
        this.decisionMade = decisionMade;
    }

    public Time getShowTime() {
        return showTime;
    }

    public void setShowTime(Time showTime) {
        this.showTime = showTime;
    }
}
