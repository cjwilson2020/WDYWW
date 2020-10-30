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
        user1.addRankingToEnd(new Media("11"));
        user1.addRankingToEnd(new Media("22"));
        user1.addRankingToEnd(new Media("33"));
        user1.addPreference("Horror");
        user1.addPreference("SciFi");
        user1.addFriend(new User("1", "user1", "1"));
        user1.addFriend(new User("2", "user2", "2"));
        user1.addFriend(new User("3", "user3", "3"));
        user1.addHistory(new Result());
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
        options.add(new Media("11"));
        options.add(new Media("22"));
        options.add(new Media("33"));
        user1.setOptions(options);
        assertEquals(user1.optionSize(), 3);
    }

    @Test
    public void addRankingToEnd() {
        user1.addRankingToEnd(new Media("44"));
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
    public void getPreferences() {
        assertEquals(user1.getPreferences().size(), 2);
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


    //test friend
    @Test
    public void getFriends() {
        assertEquals(user1.getFriends().get(0).getUsername(), "user1");
    }

    @Test
    public void addFriend() {
        user1.addFriend(new User("22", "test user", "123"));
        assertEquals(user1.getFriends().get(3).getUsername(), "test user");
    }

    @Test
    public void removeFriend() {
        user1.removeFriend(user1.getFriends().get(0));
        assertEquals(user1.getFriends().get(0).getUsername(), "user2");
    }

    @Test
    public void getHistory() {
    }

    @Test
    public void addHistory() {
        user1.addHistory(new Result());
        assertEquals(user1.getHistory().size(), 2);
    }

    @Test
    public void removeHistory() {
        user1.removeHistory(user1.getHistory().get(0));
        assertEquals(user1.getHistory().size(), 0);
    }

    @Test
    public void clearHistory() {
        user1.clearHistory();
        assertEquals(user1.getHistory().size(), 0);
    }
}