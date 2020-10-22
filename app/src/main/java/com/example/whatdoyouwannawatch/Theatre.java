package com.example.whatdoyouwannawatch;

import java.sql.Time;
import java.util.List;

public class Theatre {

    private String uid;
    private int roomNumber;
    private BackStage backstage;
    private List<User> users;
    private List<String> genres;
    private List<Media> options;
    private Time minTime;
    private Time maxTime;
    private Time timeCreated;
    private Result result;

    public Theatre(){

    }
    public Theatre(String uid){

    }

}
