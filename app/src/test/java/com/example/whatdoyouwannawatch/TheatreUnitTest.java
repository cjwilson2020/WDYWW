package com.example.whatdoyouwannawatch;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TheatreUnitTest {
    List<User> userList;
    List<Media> options;
    List<String> genres;
//    Date date;
    Result result;
    Theatre t;
    User user1;
    User user2;
    User user3;
    Media m1;
    Media m2;
    Media m3;
    Media m4;
    Media m5;
    String g1;
    String g2;
    String g3;

    @Before
    public void setUp() {
        user1 = new User("1","1","1");
        user2 = new User("2","2","2");
        user3 = new User("3","3","3");
        userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);
        m1 = new Media("11");
        m2 = new Media("22");
        m3 = new Media("33");
        m4 = new Media("44");
        m5 = new Media("55");
        options = new ArrayList<>();
        options.add(m1);
        options.add(m2);
        options.add(m3);
        options.add(m4);
        options.add(m5);
        genres = new ArrayList<>();
        genres.add(g1);
        genres.add(g2);
        genres.add(g3);
        t = new Theatre("123");
        result = new Result();
    }

    @Test
    public void TestConstructor(){
        Theatre t2 = new Theatre();
        t2.setHostID("333");
        assertEquals(t2.getHostID(), "333");
    }

    @Test
    public void TestGetUID() {
        assertEquals(t.getHostID(), "123");
    }

    @Test
    public void TestSetGetUsers() {
        t.setUsers(userList);
        assertEquals(t.getUsers().get(0), user1);
        assertEquals(t.getUsers().get(1), user2);
        assertEquals(t.getUsers().get(2), user3);
    }

    @Test
    public void TestSetGetOptions() {
        t.setOptions(options);
        assertEquals(t.getOptions().get(0), m1);
        assertEquals(t.getOptions().get(1), m2);
        assertEquals(t.getOptions().get(2), m3);
        assertEquals(t.getOptions().get(3), m4);
        assertEquals(t.getOptions().get(4), m5);
    }

    @Test
    public void TestAddOptions() {
        Media m6 = new Media("66");
        t.addOption(m6);
        assertEquals(t.getOptions().get(0), m6);
    }

    @Test
    public void TestRemoveOptions() {
        t.addOption(m1);
        t.addOption(m2);
        t.removeOption(m1);
        assertEquals(t.getOptions().get(0), m2);
    }

    @Test
    public void TestSetGetGenres() {
        t.setGenres(genres);
        assertEquals(t.getGenres().get(0), g1);
        assertEquals(t.getGenres().get(1), g2);
        assertEquals(t.getGenres().get(2), g3);
    }

    @Test
    public void TestSetGetResult() {
        t.setResult(result);
        assertEquals(t.getResult(), result);
    }

    @Test
    public void TestSetGetMinTime() {
        t.setMinTime(0);
        assertEquals(t.getMinTime(), 0);
        t.setMinTime(-1000);
        assertEquals(t.getMinTime(), -1000);
        t.setMinTime(1000);
        assertEquals(t.getMinTime(), 1000);
    }

    @Test
    public void TestSetGetMaxTime() {
        t.setMaxTime(0);
        assertEquals(t.getMaxTime(), 0);
        t.setMaxTime(10000);
        assertEquals(t.getMaxTime(), 10000);
        t.setMaxTime(-10000);
        assertEquals(t.getMaxTime(), -10000);
    }

    @Test
    public void TestAddUser() {
        t.setUsers(userList);
        User u4 = new User();
        t.addUser(user3);
        t.addUser(u4);
        assertEquals(t.getUsers().get(2), user3);
        assertEquals(t.getUsers().get(3), u4);
    }

    @Test
    public void TestRemoveUser() {
        t.setUsers(userList);
        t.removeUser(user1.getUsername());
        assertEquals(t.getUsers().get(0), user2);
    }

    @Test
    public void TestSetGetHostId(){
        t.setHostID("123456");
        assertEquals(t.getHostID(), "123456");
    }

    @Test
    public void TestToString(){
        t.setHostID("123456");
        assertEquals(t.toString(), "HostID: 123456");
    }
}