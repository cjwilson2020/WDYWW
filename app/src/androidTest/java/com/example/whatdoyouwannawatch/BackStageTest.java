package com.example.whatdoyouwannawatch;

import android.util.Log;

import org.junit.Test;
import junit.framework.Assert;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class BackStageTest {

    @Test
    public void calcLength() {
        Theatre t1 = new Theatre("123", "123", LocalTime.now());
        //BackStage b1 = t1.getBackstage();
        BackStage b1 = new BackStage(t1);

        User u1= new User("1","1", "1");
        Long u1t1 = LocalTime.of(0, 55, 0);
        Long u1t2 = LocalTime.of(3, 1, 50);
        u1.setMinLength(u1t1);
        u1.setMaxLength(u1t2);

        User u2= new User("2","2","2");
        Long u2t1 = LocalTime.of(0, 50, 0);
        Long u2t2 = LocalTime.of(2, 1, 50);
        u2.setMinLength(u2t1);
        u2.setMaxLength(u2t2);

        //t1.addUser(u1);
        //t1.addUser(u2);
        List<User> users = new ArrayList<User>();
        users.add(u1);
        users.add(u2);

        t1.setUsers(users);

        //t1.setTimes();
        //t1.setMinTime();
        //t1.setMaxTime();
        b1.calcLength();
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