package com.example.whatdoyouwannawatch;

import android.media.Image;

import java.sql.Time;
import java.util.List;

public class Media {
    private String id;
    private String title;
    private List<String> genres;
    private List<String> cast;
    private Time length;
    private String director;
    private String writer;
    private String description;
    private Image poster;
    private Double rating;

    //constructor, some parameters optional
    public Media(String id, String title, List<String> genres, List<String> cast, Time length, String director, String writer, String description, Image poster, Double rating) {
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

    public Time getLength() {
        return length;
    }

    public void setLength(Time length) {
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
}
