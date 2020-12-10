package com.example.whatdoyouwannawatch;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserUnitTest {
    User guest1;
    User user1;
    User user2;
    User user3;
    User user4;
    ArrayList<Media> mediaList;
    List<String> pref;

    @Before
    public void start() throws Exception {
        guest1 = new User();
        user1 = new User("some random email address", "some random username", "some random uid");
        user2 = new User("2", "user2", "2");
        user3 = new User("3", "user3", "3");
        user4 = new User("4", "user4", "4");
        Media m1 = new Media("11");
        Media m2 = new Media("22");
        Media m3 = new Media("33");
        pref = new ArrayList<>();
        pref.add("genre1");
        pref.add("genre2");
        mediaList = new ArrayList<>();
        mediaList.add(m1);
        mediaList.add(m2);
        mediaList.add(m3);
        user1.setRankings(mediaList);
        guest1.addGenres("SciFi");
        guest1.addGenres("Romance");
        user1.addGenres("Horror");
        user1.addGenres("Unknown");
        user1.addGenres("111");
        user1.addPreference("Horror");
        user1.addPreference("SciFi");
        user1.addFriend(user2);
        user1.addFriend(user3);
        user1.addFriend(user4);
        user1.addHistory("newhistory1");
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

    @Test
    public void TestConstructor(){
        User user2 = new User("someid");
        assertEquals(user2.getUID(), "someid");
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
    public void TestSetPreferences(){
        user1.setPreferences(pref);
        assertEquals(user1.getPreferences().get(1), "genre2");
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
        assertEquals(user1.getFriends().get(0), "user2");
    }

    @Test
    public void addFriend() {
        user1.addFriend(new User("22", "test user", "123"));
        assertEquals(user1.getFriends().get(3), "test user");
    }

    @Test
    public void addFriendToEmptyList() {
        user1.setFriends(null);
        user1.addFriend(user2);
        assertEquals(user1.getFriends().get(0), "user2");
    }

    @Test
    public void addExsitingFriend() {
        user1.addFriend(user3);
        assertEquals(user1.getFriends().size(), 3);
    }

    @Test
    public void removeFriend() {
        user1.removeFriend(user2);
        assertEquals(user1.getFriends().get(0), "user3");
    }

    @Test
    public void getHistory() {
    }

    @Test
    public void addHistory() {
        user1.addHistory("newhistory2");
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

    @Test
    public void TestToString(){
        user1 = new User("1");
        user1.setUsername("2");
        user1.setEmail("3@g.com");
        assertEquals(user1.toString(), "User Name: " + "2" + "\nEmail: " + "3@g.com" + "\nUID: " + "1");

    }

    @Test
    public void testSetService(){
        ArrayList<String> services = new ArrayList<>();
        services.add("Amazon Prime");
        services.add("Netflix");
        user1.setServices(services);
        ArrayList<String> services
        assertEquals();
    }

    @Test
    public void testgetService(){

    }

    @Test
    public void testSetHistory(){

    }
}