package com.example.whatdoyouwannawatch;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ResultUnitTest {
    List<User> userList;
    List<Media> options;
    Date date;
    Result test;
    User user1;
    User user2;
    User user3;
    Media m1;
    Media m2;
    Media m3;
    Media m4;


    @Before
    public void setup(){
        user1 = new User("1","1","1");
        user2 = new User("2","2","2");
        user3 = new User("3","3","3");
        m1 = new Media("11");
        m2 = new Media("22");
        m3 = new Media("33");
        m4 = new Media("44");
        userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);
        options = new ArrayList<>();
        options.add(m1);
        options.add(m2);
        options.add(m3);
        options.add(m4);
        test = new Result(m3, userList, options, date);
    }

    @Test
    public void TestGetDecision() {
        assertEquals(test.getFinalDecision(), m3);
    }

    @Test
    public void TestGetUsers() {
        assertEquals(test.getUsers().get(0), user1);
        assertEquals(test.getUsers().get(1), user2);
        assertEquals(test.getUsers().get(2), user3);
    }

    @Test
    public void TestGetOptions() {
        assertEquals(test.getOptions().get(0), m1);
        assertEquals(test.getOptions().get(1), m2);
        assertEquals(test.getOptions().get(2), m3);
        assertEquals(test.getOptions().get(3), m4);
    }

    @Test
    public void addToHistory() {
        test.addToHistory(userList);
        assertEquals(userList.get(0).getHistory().get(0), "33");
        assertEquals(userList.get(1).getHistory().get(0), "33");
        assertEquals(userList.get(2).getHistory().get(0), "33");
    }

    @Test
    public void TestToString() {
        assertTrue(test.toString() == null);
        test.getFinalDecision().setTitle("movie11");
        assertEquals(test.toString(), "movie11");
    }
}