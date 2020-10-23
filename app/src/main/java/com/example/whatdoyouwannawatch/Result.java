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

}
