package com.example.whatdoyouwannawatch;

import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MediaUnitTest {

    Media m;
    String id;
    String title;
    ArrayList<String> genres;
    ArrayList<String> cast;
    List<User> currentVoters;
    User u1;
    User u2;
    User u3;
    int length;
    String director;
    String writer;
    String description;
    URL poster;
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
        cast = new ArrayList<>();
        cast.add("sb1");
        cast.add("sb2");
        cast.add("sb3");
        writer = "sb00";
        director = "sb11";
        description = "bad movie";
        rating = 1.0;
        m = new Media(id, title, genres, cast, length, director, writer, description, poster, rating);
        currentVoters = new ArrayList<>();
        currentVoters.add(u1);
        currentVoters.add(u2);
        m.setCurrentVoters(currentVoters);

    }

    @Test
    public void TestSetGetID() {
        assertEquals(m.getId(), "000");
        m.setId("110");
        assertEquals(m.getId(), "110");
    }

    @Test
    public void TestSetGetTitle() {
        assertEquals(m.getTitle(), "some movie");
        m.setTitle("another movie");
        assertEquals(m.getTitle(), "another movie");
    }

    @Test
    public void TestGetGenres() {
        assertEquals(m.getGenres().get(0), "g1");
        assertEquals(m.getGenres().get(1), "g2");
        assertEquals(m.getGenres().get(2), "g3");
    }

    @Test
    public void TestGetCast() {
        assertEquals(m.getCast().get(0), "sb1");
        assertEquals(m.getCast().get(1), "sb2");
        assertEquals(m.getCast().get(2), "sb3");
    }

    @Test
    public void TestGetLength() {
        assertEquals(m.getLength(), 120);
        m.setLength(150);
        assertEquals(m.getLength(), 150);
        m.setLength(-150);
        assertEquals(m.getLength(), -150);//TODO make sure the app only accept legit input, no negative number for time
    }

    @Test
    public void TestSetGetWriter() {
        assertEquals(m.getWriter(), "sb00");
        m.setWriter("sb00a");
        assertEquals(m.getWriter(), "sb00a");
    }

    @Test
    public void TestSetGetDirector() {
        assertEquals(m.getDirector(), "sb11");
        m.setDirector("sb11a");
        assertEquals(m.getDirector(), "sb11a");
    }

    @Test
    public void TestSetGetDes() {
        assertEquals(m.getDescription(), "bad movie");
        m.setDescription("good now");
        assertEquals(m.getDescription(), "good now");
    }

    @Test
    public void TestSetGetRate() {
        assertEquals(m.getRating(), 1.0, 0.0001);
        m.setRating(5.0);
        assertEquals(m.getRating(), 5.0, 0.0001);
    }

    @Test
    public void TestCurrentVoter() {
        assertEquals(m.getCurrentVoters().size(), 2);
        assertEquals(m.getCurrentVoters().get(0), u1);
        assertEquals(m.getCurrentVoters().get(1), u2);
        m.getCurrentVoters().add(u3);
        assertEquals(m.getCurrentVoters().size(), 3);
        assertEquals(m.getCurrentVoters().get(2), u3);
    }
}