package com.example.whatdoyouwannawatch;

import java.sql.Time;
import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

public class Theatre {
    private static int theatreCounter = 0;

    private String hostID;
    private String roomNumber;
    private BackStage backstage;
    private List<User> users;
    private List<String> genres;
    private List<Media> options;
    private LocalTime minTime;
    private LocalTime maxTime;
    private LocalTime timeCreated;
    private Result result;

    public Theatre(String uid){
        this.hostID = uid;
        this.roomNumber = Integer.toString(++theatreCounter);
        this.timeCreated = LocalTime.now();
    }

    public Theatre(String uid, String roomNumber, LocalTime timeCreated ){
        this.hostID =uid;
        this.roomNumber =roomNumber;
        this.timeCreated = timeCreated;
        this.backstage = new BackStage();
        this.users = new ArrayList<User>();
        this.genres = new ArrayList<String>();
        this.options = new ArrayList<Media>();
    }

    public String getHostID() {
        return hostID;
    }

    public void setHostID(String uid) {
        this.hostID = uid;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public BackStage getBackstage() {
        return backstage;
    }

    public void setBackstage() {
        this.backstage = new BackStage(this);
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void addUser(User u){
        if(!this.users.contains(u)) {
            this.users.add(u);
        }
    }

    public void removeUser(User u){
        this.users.remove(u);
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public void addGenre(String s){
        if(!this.genres.contains(s)) {
            this.genres.add(s);
        }
    }
    public void removeGenre(String s){
        this.genres.remove(s);
    }

    public List<Media> getOptions() {
        return options;
    }

    public void setOptions(List<Media> options) {
        this.options = options;
    }

    public void addOption(Media m){
        if(!this.options.contains(m)) {
            this.options.add(m);
        }
    }

    public void removeOption(Media m){
        this.options.remove(m);
    }

    public LocalTime getMinTime() {
        return minTime;
    }

    public void setMinTime() {
        this.minTime = backstage.calcLength().get(0);
    }

    public LocalTime getMaxTime() {
        return maxTime;
    }

    public void setMaxTime() {
        this.maxTime = backstage.calcLength().get(1);
    }

    public LocalTime getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(LocalTime timeCreated) {
        this.timeCreated = timeCreated;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
