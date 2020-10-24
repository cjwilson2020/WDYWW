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
    private List<String> preferences;
    private List<String> rankings;
    private Time minLength;
    private Time maxLength;

    //Constructors for guest
    private User(){
        this.isGuest = true;
        this.genres = new ArrayList<String>();
        this.rankings = new ArrayList<String>();
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
        this.rankings = new ArrayList<String>();
    }

    //Generate a random unique ID
    private void generateUID(){
        this.UID = UUID.randomUUID().toString();
    }

    public List<String> getPreferences() {return this.preferences;}

    public void addPreference(String tag){this.preferences.add(tag);}

    public void removePreference(String tag){this.preferences.remove(tag);}

    public void setMinLength(Time minLength) {this.minLength = minLength;}

    public Time getMinLength(){return this.minLength;}

    public void setMaxLength(Time maxLength) {this.maxLength = maxLength;}

    public Time getMaxLength(){return this.maxLength;}

    //add a friend
    public void addFriend(User f){friends.add(f);}

    //remove a friend
    public void removeFriend(User f){friends.remove(f);}

    //add history
    public void addHistory(Result r){history.add(r);}

    //remove a history
    public void removeHistory(Result r){history.remove(r);}

    //Getters and Setters
    public String getUID() {return UID;}

    public String getUsername() {return username;}

    public void setUsername(String username) {this.username = username;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email; }

    public Image getUserAvatar() {return userAvatar;}

    public void setUserAvatar(Image userAvatar) {this.userAvatar = userAvatar;}

    public boolean isGuest() {return isGuest;}

    public List<String> getGenres() {return genres;}

    public void setGenres(List<String> genres) {this.genres = genres;}

    public List<User> getFriends() {return friends;}

    public List<Result> getHistory() {return history;}

    public void clearHistory(){ this.history = new ArrayList<>(); }

    public List<String> getRankings() {
        return rankings;
    }

    public void addRankingToEnd(String uid){
        if(!this.rankings.contains(uid)) {
            this.rankings.add(uid);
        }
    }

    public void removeRanking(String uid){
        this.rankings.remove(uid);
    }

    public void clearRanking(){
        this.rankings = new ArrayList<>();
    }

    public String toString(){
        return "User Name: " + username + "\n";
    }


}
