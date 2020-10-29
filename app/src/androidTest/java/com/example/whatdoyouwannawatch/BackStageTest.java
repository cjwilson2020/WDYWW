package com.example.whatdoyouwannawatch;

import android.util.Log;

import org.junit.Test;
import junit.framework.Assert;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class BackStageTest {

    @Test
    public void calcLength() {
        Theatre t1 = new Theatre("123", 123, new Time(22,22,22));
        BackStage b1 = t1.getBackstage();

        User u1= new User("1","1", "1");
        Time u1t1 = new Time(0, 55, 0);
        Time u1t2 = new Time(3, 1, 50);
        u1.setMinLength(u1t1);
        u1.setMaxLength(u1t2);

        User u2= new User("2","2","2");
        Time u2t1 = new Time(0, 50, 0);
        Time u2t2 = new Time(2, 1, 50);
        u1.setMinLength(u2t1);
        u1.setMaxLength(u2t2);

        //t1.addUser(u1);
        //t1.addUser(u2);
        List<User> users = new ArrayList<User>();
        users.add(u1);
        users.add(u2);

        t1.setUsers(users);

        t1.setTimes();
        assertEquals(u1t1, t1.getMinTime());
        assertEquals(u2t2, t1.getMaxTime());

    }

    @Test
    public void calcGenre() {
    }

    @Test
    public void calcResult() {
    }
}