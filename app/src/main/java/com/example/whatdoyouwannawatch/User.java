package com.example.whatdoyouwannawatch;

import android.media.Image;

import java.util.List;
import java.util.UUID;

public class User {
    private String UID;
    private String username;
    private String email;
    private Image userAvatar;
    private boolean isGuest;
    List<String> genres;
    List<User> friends;
    List<Result> history;

    //Constructors
    private User(){
        this.isGuest = true;
    }

    private User(String email, String username){
        this.isGuest = false;
        this.username = username;
        this.email = email;
        generateUID();
    }

    //TODO implement generateUID
    private void generateUID(){
        this.UID = UUID.randomUUID().toString();
    }

    //Getters and Setters
    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
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

    public void setGuest(boolean guest) {
        isGuest = guest;
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

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    public List<Result> getHistory() {
        return history;
    }

    public void setHistory(List<Result> history) {
        this.history = history;
    }

    //TODO implement toString method
    public String toString(){
        return "";
    }
}
