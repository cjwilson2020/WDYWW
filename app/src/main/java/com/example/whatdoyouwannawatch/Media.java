package com.example.whatdoyouwannawatch;



import android.media.Image;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Media implements Comparable<Media> {
    private String id;
    private String title;
    private List<String> genres;
    private List<String> cast;
    private List<User> currentVoters;
    private LocalTime length;
    private String director;
    private String writer;
    private String description;
    private Image poster;
    private Double rating;

    //constructor
    public Media() {}

    //constructor1
    public Media(String title, List<String> genres, LocalTime length, Image poster) {
        this.title = title;
        this.genres = genres;
        this.length = length;
        this.poster = poster;
        this.currentVoters = new ArrayList<User>();
    }

    //constructor2, TODO make some parameters optional depends on what info we can get from stream service
    public Media(String id, String title, List<String> genres, List<String> cast, LocalTime length, String director, String writer, String description, Image poster, Double rating) {
        this.id = id;
        this.title = title;
        this.genres = genres;
        this.cast = cast;
        this.length = length;
        this.director = director;
        this.writer = writer;
        this.description = description;
        this.poster = poster;
        this.rating = rating;
        this.currentVoters = new ArrayList<User>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public List<String> getCast() {
        return cast;
    }

    public void setCast(List<String> cast) {
        this.cast = cast;
    }

    public LocalTime getLength() {
        return length;
    }

    public void setLength(LocalTime length) {
        this.length = length;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Image getPoster() {
        return poster;
    }

    public void setPoster(Image poster) {
        this.poster = poster;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public List<User> getCurrentVoters() {
        return currentVoters;
    }

    public void setCurrentVoters(List<User> currentVoters) {
        this.currentVoters = currentVoters;
    }

    public void addVoter(User u){
        this.currentVoters.add(u);
    }
    public void removeVoter(User u){
        this.currentVoters.remove(u);
    }

    public int getNumVoters(){
        return this.currentVoters.size();
    }

    public boolean equalTo(Media m){ return this.title == m.title; }

    @Override
    public int compareTo(Media m) {
        return this.currentVoters.size() - m.currentVoters.size();
    }
}
