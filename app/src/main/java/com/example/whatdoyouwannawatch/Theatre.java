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
    public Theatre(String uid, int roomNumber, Time timeCreated ){
        this.uid =uid;
        this.roomNumber =roomNumber;
        this.timeCreated = timeCreated;
    }

    public String getUID() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
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

    public Time getMinTime() {
        return minTime;
    }

    public void setMinTime() {
        this.minTime = backstage.calcLength().get(0);
    }

    public Time getMaxTime() {
        return maxTime;
    }

    public void setMaxTime() {
        this.maxTime = backstage.calcLength().get(1);
    }

    public Time getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Time timeCreated) {
        this.timeCreated = timeCreated;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
