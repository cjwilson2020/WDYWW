package com.example.whatdoyouwannawatch;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class UserUnitTest {
    User guest1 = new User();
    User user1 = new User("some random email address", "some random username", "some random uid");

    @Before
    public void start() throws Exception {
        guest1.addGenres("SciFi");
        guest1.addGenres("Romance");
        user1.addGenres("Horror");
        user1.addGenres("Unknown");
        user1.addGenres("111");
        user1.addRankingToEnd(new Media());
        user1.addRankingToEnd(new Media());
        user1.addRankingToEnd(new Media());
        user1.addPreference("Horror");
        user1.addPreference("SciFi");
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
        assertEquals(guest1.getGenres().get(1), "Romance");
    }

    @Test
    public void userGetGenres() {
        assertEquals(user1.getGenres().get(2), "111");
    }

    @Test
    public void guestAddGenres() {
        guest1.addGenres("222");
        assertEquals("Genres", guest1.getGenres().get(2), "222");
    }


    @Test
    public void userAddGenres() {
        user1.addGenres("SciFi");
        assertEquals("Genres", user1.getGenres().get(3), "SciFi");
    }

    @Test
    public void guestRemoveGenres() {
        guest1.removeGenres("SciFi");
        assertEquals("Genres", guest1.getGenres().get(0), "Romance");
    }

    @Test
    public void userRemoveGenres() {
        user1.removeGenres("Horror");
        assertEquals("Genres", user1.getGenres().get(0), "Unknown");
    }



    @Test
    public void setOptions() {
        List<Media> options = new ArrayList<>();
        options.add(new Media());
        options.add(new Media());
        options.add(new Media());
        user1.setOptions(options);
        assertEquals(user1.optionSize(), 3);
    }

    @Test
    public void addRankingToEnd() {
        user1.addRankingToEnd(new Media());
        assertEquals(user1.rankingsSize(), 4);
    }

//    @Test
//    public void removeRanking() {
//        user1.removeRanking();
//        assertEquals(user1.rankingsSize(), 2);
//    }

    @Test
    public void clearRanking() {
        user1.clearRanking();
        assertEquals(user1.rankingsSize(), 0);
    }

    @Test
    public void getRankings() {
        List<Media> testList = user1.getRankings();
        assertEquals(testList.size(), 3);
    }

    @Test
    public void addPreference() {
        user1.addPreference("Nothing");
        assertEquals(user1.getPreferences().get(2), "Nothing");
    }

    @Test
    public void removePreference() {
        user1.removePreference("Horror");
        assertEquals(user1.getPreferences().get(0), "SciFi");
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