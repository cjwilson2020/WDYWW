package com.example.whatdoyouwannawatch;

import com.google.firebase.database.Exclude;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class Theatre {

    private String hostID;
    //private String roomNumber;
//  private BackStage backstage;
    private List<User> users;
    private List<String> genres;
    private List<Media> options;
    private int minTime;
    private int maxTime;
    //private LocalTime timeCreated;
    private Result result;

    public Theatre(){
        //Required for Firebase's dataSnapshot
    }

    public Theatre(String uid){
        this.hostID = uid;
        this.users = new ArrayList<User>();
        this.genres = new ArrayList<String>();
        this.options = new ArrayList<Media>();
        this.result = new Result(new Media("x"));
    }


    //Setters and getters
    public String getHostID() { return hostID; }
    public void setHostID(String uid) { this.hostID = uid; }

    //public String getRoomNumber() { return roomNumber; }

//    public BackStage getBackstage() { return backstage; }
//    public void setBackstage() { this.backstage = new BackStage(this); }

    public List<User> getUsers() { return this.users; }
    public void setUsers(List<User> users) { this.users = users; }

    public void addUser(User u){
        boolean alreadyIn = false;
        for(int i = 0; i<this.users.size(); i++){
            if(users.get(i).getUsername().equals(u.getUsername())){
                alreadyIn = true;
            }
        }
        if(!alreadyIn) {
            this.users.add(u);
        }
    }
    public void removeUser(String username){
        for(int i = 0; i<this.users.size(); i++){
            if(users.get(i).getUsername().equals(username)){
                users.remove(i);
            }
        }

    }

    public List<String> getGenres() { return genres; }
    public void setGenres(List<String> genres) { this.genres = genres; }

    public void addGenre(String s){
        if(!this.genres.contains(s)) {
            this.genres.add(s);
        }
    }
//    public void removeGenre(String s){ this.genres.remove(s); }

    public List<Media> getOptions() { return options; }
    public void setOptions(List<Media> options) { this.options = options; }
    public void addOption(Media m){
        if(!this.options.contains(m)) {
            this.options.add(m);
        }
    }
    public void removeOption(Media m){ this.options.remove(m); }

    public int getMinTime() {
        return minTime;
    }

    //   public void setMinTime() { this.minTime = backstage.calcLength().get(0);    }
    public void setMinTime(int t) { this.minTime = t;    }

    public int getMaxTime() {    return maxTime;    }

    //    public void setMaxTime() {     this.maxTime = backstage.calcLength().get(1);    }
    public void setMaxTime(int t) {     this.maxTime = t;    }

//    public LocalTime getTimeCreated() {
//        return timeCreated;
//    }
//
//
//    public void setTimeCreated(LocalTime timeCreated) {
//        this.timeCreated = timeCreated;
//    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String toString(){
        return "HostID: " + hostID;
    }

//    @Exclude
//    public Map<String, Object> toMap() {
//        HashMap<String, Object> result = new HashMap<>();
//        result.put("hostid", getHostID());
//        result.put("result", getResult());
//        result.put("genres", getGenres());
//        result.put("options", getOptions());
//        result.put("mintime", getMinTime());
//        result.put("maxtime", getMaxTime());
//        //       result.put("timecreated", getTimeCreated());
//
//        return result;
//    }
}
