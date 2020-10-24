package com.example.whatdoyouwannawatch;

import android.media.Image;

import java.sql.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {
    private String UID;
    private String username;
    private String email;
    private Image userAvatar;
    private boolean isGuest;
    private List<String> genres;
    private List<User> friends;
    private List<Result> history;
    private Media[] choices = new Media[3];
    //TODO vote list
    //TODO List<> preferences, add and remove, getter

    //Constructors for guest
    private User(){
        this.isGuest = true;
        this.genres = new ArrayList<>();
    }

    //Constructor for registered user
    private User(String email, String username){
        this.isGuest = false;
        this.username = username;
        this.email = email;
        generateUID();
        this.genres = new ArrayList<>();
        this.friends = new ArrayList<>();
        this.history = new ArrayList<>();
    }

    //Generate a random unique ID
    private void generateUID(){
        this.UID = UUID.randomUUID().toString();
    }

    //TODO implement getMinLength()
    public Time getMinLength(){return null;}

    //TODO implement getMaxLength()
    public Time getMaxLength(){return null;}

    //TODO get region
    public String getRegion(){return null;}

    //TODO get timezone
    public int getTimezone(){return 0;}

    //TODO vote
    public Media[] vote(ArrayList<String> candidates){

        return this.choices;
    }

    //add a friend
    public void addFriend(User f){friends.add(f);}

    //remove a friend
    public void removeFriend(User f){friends.remove(f);}

    //add history
    public void addHistory(Result r){history.add(r);}

    //remove a history
    public void removeHistory(Result r){history.remove(r);}

    //Getters and Setters
    public String getUID() {
        return UID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Image getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(Image userAvatar) {
        this.userAvatar = userAvatar;
    }

    public boolean isGuest() {
        return isGuest;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public List<User> getFriends() {
        return friends;
    }

    public List<Result> getHistory() {
        return history;
    }

    public void clearHistory(){ this.history = new ArrayList<>(); }

    //TODO implement toString method
    public String toString(){
        return "";
    }
}
