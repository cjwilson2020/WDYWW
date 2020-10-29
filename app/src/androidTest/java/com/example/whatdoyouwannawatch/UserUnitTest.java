package com.example.whatdoyouwannawatch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static org.junit.Assert.*;

public class UserUnitTest {
    User guest1 = new User();
    User user1 = new User("some random email address", "some random username", "some random uid");

    @Before
    public void setUp() throws Exception {
        User guest1 = new User();
        User user1 = new User("some random email address", "some random username", "some random uid");
    }

    //test basic information
    @Test
    public void GuestIsGuest() {
        assertTrue(guest1.isGuest());
    }

    @Test
    public void userIsNotGuest() {
        assertTrue(!user1.isGuest());
    }

    @Test
    public void getUID() {
        assertEquals("user id", user1.getUID(), "some random uid");
    }

    @Test
    public void getUsername() {
        assertEquals("user name", user1.getUsername(), "some random username");
    }

    @Test
    public void setUsername() {
        user1.setUsername("yc520");
        assertEquals("user name", user1.getUsername(), "yc520");
    }

    @Test
    public void getEmail() {
        assertEquals("user email", user1.getEmail(), "some random email address");
    }

    @Test
    public void setEmail() {
        user1.setEmail("chenyuan@gmail.com");
        assertEquals("user email", user1.getEmail(), "chenyuan@gmail.com");
    }

//    @Test
//    public void setUserAvatar() {
//        user1.setUserAvatar();
//    }

    //test preference settings
    @Test
    public void guestGetGenres() {
        assertEquals(guest1.getGenres(), new ArrayList<String>());
    }

    @Test
    public void userGetGenres() {
        assertEquals(guest1.getGenres(), new ArrayList<String>());
    }

    @Test
    public void guestAddGenres1() {
        guest1.addGenres("SciFi");
        assertEquals("Genres", guest1.getGenres().get(0), "SciFi");
    }

    @Test
    public void guestAddGenres2() {
        user1.addGenres("Romance");
        assertEquals("Genres", guest1.getGenres().get(1), "Romance");
    }

    @Test
    public void userAddGenres() {
        user1.addGenres("Horror");
        assertEquals("Genres", guest1.getGenres().get(0), "Horror");
    }

    @Test
    public void guestRemoveGenres() {

    }

    @Test
    public void userRemoveGenres() {

    }

    @Test
    public void setOptions() {
    }

    @Test
    public void addRankingToEnd() {
    }

    @Test
    public void removeRanking() {
    }

    @Test
    public void clearRanking() {
    }

    @Test
    public void getRankings() {
    }

    @Test
    public void addPreference() {
    }

    @Test
    public void removePreference() {
    }

    @Test
    public void getPreferences() {
    }

    //test friend
    @Test
    public void addFriend() {
    }

    @Test
    public void removeFriend() {
    }

    @Test
    public void getFriends() {
    }

    @Test
    public void addHistory() {
    }

    @Test
    public void removeHistory() {
    }

    @Test
    public void getHistory() {
    }

    @Test
    public void clearHistory() {
    }
}