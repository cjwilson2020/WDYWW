package com.example.whatdoyouwannawatch;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class ResultUnitTest {
    List<User> userList;
    List<Media> options;
    Date date;
    Result test;

    @Before
    public void setup(){
        User user1 = new User("1","1","1");
        User user2 = new User("2","2","2");
        User user3 = new User("3","3","3");
        Media m1 = new Media("11");
        Media m2 = new Media("22");
        Media m3 = new Media("33");
        Media m4 = new Media("44");
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
    public void addToHistory() {
        test.addToHistory(userList);
        assertEquals(userList.get(0).getHistory().get(0), test);
        assertEquals(userList.get(1).getHistory().get(0), test);
        assertEquals(userList.get(2).getHistory().get(0), test);
    }
}