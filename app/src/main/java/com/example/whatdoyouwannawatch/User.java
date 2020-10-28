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
    private List<Media> rankings;
    private List<Media> options;
    private Time minLength;
    private Time maxLength;

    //Constructors for guest
    private User(){
        this.isGuest = true;
        this.genres = new ArrayList<>();
        this.rankings = new ArrayList<>();
        this.preferences = new ArrayList<>();
        this.options = new ArrayList<>();
    }

    //Constructor for registered user
    User(String email, String username, String uid){
        this.isGuest = false;
        this.username = username;
        this.email = email;
        this.UID = uid;
        this.friends = new ArrayList<>();
        this.history = new ArrayList<>();
        this.genres = new ArrayList<>();
        this.rankings = new ArrayList<>();
        this.preferences = new ArrayList<>();
        this.options = new ArrayList<>();
    }


    //set and get genres
    public List<String> getGenres() {return genres;}
    public void setGenres(List<String> genres) {this.genres = genres;}


    //Start voting process
    //get options from database
    public void setOptions(List<Media> o){
        this.options = o;
    }
    //user add media object in options to rankings list
    public void addRankingToEnd(Media m){
        if(!this.rankings.contains(m)) {
            this.rankings.add(m);
        }
    }
    //user can remove single ranked media from the list
    public void removeRanking(String uid){ this.rankings.remove(uid); }
    //user can clear the rankings list and start over
    public void clearRanking(){ this.rankings = new ArrayList<>(); }
    //return the ranking list
    public List<Media> getRankings() {
        return rankings;
    }
    //End of voting process


    //add, remove and get preference
    public void addPreference(String tag){this.preferences.add(tag);}
    public void removePreference(String tag){this.preferences.remove(tag);}
    public List<String> getPreferences() {return this.preferences;}

    //set and get length
    public void setMinLength(Time minLength) {this.minLength = minLength;}
    public Time getMinLength(){return this.minLength;}
    public void setMaxLength(Time maxLength) {this.maxLength = maxLength;}
    public Time getMaxLength(){return this.maxLength;}

    //add, remove friend and get friend list
    public void addFriend(User f){friends.add(f);}
    public void removeFriend(User f){friends.remove(f);}
    public List<User> getFriends() {return friends;}

    //add, remove history. Get and clear history list
    public void addHistory(Result r){history.add(r);}
    public void removeHistory(Result r){history.remove(r);}
    public List<Result> getHistory() {return history;}
    public void clearHistory(){ this.history = new ArrayList<>(); }

    //Generate and return a random unique ID

    private void generateUID(){
        this.UID = UUID.randomUUID().toString();
    }
    //We generate UIDs from Firebase Auth, I will create a setUID method
    private void setUID(String u){
        UID = u;
    }
    public String getUID() {return UID;}

    //setters and getters of user information
    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email; }
    public Image getUserAvatar() {return userAvatar;}
    public void setUserAvatar(Image userAvatar) {this.userAvatar = userAvatar;}

    //distinguish if a user guest or registered
    public boolean isGuest() {return isGuest;}

    public String toString(){
        return "User Name: " + username + "\n";
    }


}
