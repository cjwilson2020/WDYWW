package com.example.whatdoyouwannawatch;

import android.media.Image;

import com.google.firebase.database.Exclude;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    private String UID;
    private String username;
    private String email;
    private Image userAvatar;
    private boolean isGuest;
    private List<String> genres;
    private List<User> friends;
    private List<User> friendRequests;
    private List<Result> history;
    private List<String> preferences;
    private List<Media> rankings;
    private List<Media> options;
    private int minLength;
    private int maxLength;

    //Constructors for guest
    User(){
        this.isGuest = true;
        this.genres = new ArrayList<>();
        this.rankings = new ArrayList<>();
        this.preferences = new ArrayList<>();
        this.options = new ArrayList<>();
    }

    User(String uid) {
        this.isGuest = true;
        this.UID = uid;
        this.username = uid;
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
        this.friendRequests = new ArrayList<>();
        this.history = new ArrayList<>();
        this.genres = new ArrayList<>();
        this.rankings = new ArrayList<>();
        this.preferences = new ArrayList<>();
        this.options = new ArrayList<>();
    }

    //set and get genres
    public List<String> getGenres() {return this.genres;}
    public void addGenres(String g) {this.genres.add(g);}
    public void removeGenres(String g) {this.genres.remove(g);}

    //Start voting process
    //get options from database
    public void setOptions(List<Media> o){
        this.options = o;
    }

    public void setRankings(List<Media> r){
        this.rankings = r;
    }
    //user add media object in options to rankings list
    public void addRankingToEnd(Media m){
        if(!this.rankings.contains(m)) {
            this.rankings.add(m);
        }
    }
    //user can remove single ranked media from the list
    public void removeRanking(Media m){ this.rankings.remove(m); }
    //user can clear the rankings list and start over
    public void clearRanking(){ this.rankings = new ArrayList<>(); }
    //return the ranking list
    public List<Media> getRankings() {
        return rankings;
    }
    public int optionSize() {return this.options.size(); }
    public int rankingsSize() {return this.rankings.size(); }
    //End of voting process


    //add, remove and get preference
    public void addPreference(String tag){
        if(!this.preferences.contains(tag)) {
            this.preferences.add(tag);
        }
    }
    public void removePreference(String tag){this.preferences.remove(tag);}
    public List<String> getPreferences() {return this.preferences;}
    public void setPreferences(List<String> prefs){
        this.preferences = prefs;
    }

    //set and get length
    public void setMinLength(int t) {this.minLength = t;}
    public int getMinLength(){return this.minLength;}
    public void setMaxLength(int t) {this.maxLength = t;}
    public int getMaxLength(){return this.maxLength;}

    //add, remove friend and get friend list
    public void addFriend(User f){
        boolean found = false;
        for(int i =0; i < friends.size(); i++){
            if(friends.get(i).getUsername().equals(f.getUsername())){
                found = true;
            }
        }
        if(!found){
            friends.add(f);
        }
    }
    public void removeFriend(User f){
        for(int i =0; i < friends.size(); i++){
            if(friends.get(i).getUsername().equals(f.getUsername())){
                friends.remove(i);
            }
        }
    }
    public List<User> getFriends() {return friends;}
    public void setFriends(List<User>friends) {this.friends = friends;}

    //add, remove history. Get and clear history list
    public void addHistory(Result r){history.add(r);}
    public void removeHistory(Result r){history.remove(r);}
    public List<Result> getHistory() {return history;}
    public void clearHistory(){ this.history = new ArrayList<>(); }

    //Generate and return a random unique ID
    //private void generateUID(){this.UID = UUID.randomUUID().toString();    }
    //We generate UIDs from Firebase Auth, I will create a setUID method
    private void setUID(String u){ UID = u; }
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
    public void setGuest(boolean bool){
        this.isGuest = bool;
    }

    public String toString(){
        return "User Name: " + username + "\nEmail: " + email + "\nUID: " + getUID();
    }

    public List<User> getFriendRequests() {
        return friendRequests;
    }

    public void addFriendRequest(User friendRequest) {
        this.friendRequests.add(friendRequest);
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", getUID());
        result.put("username", getUsername());
        result.put("email", getEmail());
        result.put("friends", getFriends());
        result.put("history", getHistory());
        result.put("genres", getGenres());
        result.put("rankings", getRankings());
        result.put("preferences", getPreferences());
        result.put("guest", isGuest());

        return result;
    }

}
