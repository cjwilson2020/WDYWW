package com.example.whatdoyouwannawatch;

import android.media.Image;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MediaUnitTest {

    Media m;
    String id;
    String title;
    List<String> genres;
    List<String> cast;
    List<User> currentVoters;
    int length;
    String director;
    String writer;
    String description;
    Image poster;
    Double rating;

    @Before
    public void setUp() throws Exception {
        id = "000";
        title = "some movie";
        genres = new ArrayList<>();
        genres.add("g1");
        genres.add("g2");
        genres.add("g3");
        length = 120;
        cast.add("sb1");
        cast.add("sb2");
        cast.add("sb3");
        writer = "sb00";
        director = "sb11";
        description = "bad movie";
        rating = 1.0;
        m = new Media(id, title, genres, cast, length, director, writer, description, poster, rating);
    }

    @Test
    public void SetGetID() {
        assertEquals(m.getId(), "000");
        m.setId("110");
        assertEquals(m.getId(), "110");
    }
}