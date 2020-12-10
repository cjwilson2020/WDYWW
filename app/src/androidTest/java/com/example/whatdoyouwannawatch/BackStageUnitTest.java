package com.example.whatdoyouwannawatch;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BackStageUnitTest {

    @Test
    public void TestCalcLength() {
        Theatre t1 = new Theatre("123");
        BackStage b1 = new BackStage(t1);

        User u1= new User("1","1", "1");
        int u1t1 = 55;
        int u1t2 = 2;
        u1.setMinLength(u1t1);
        u1.setMaxLength(u1t2);

        User u2= new User("2","2","2");
        int u2t1 = 50;
        int u2t2 = 121;
        u2.setMinLength(u2t1);
        u2.setMaxLength(u2t2);

        List<User> users = new ArrayList<User>();
        users.add(u1);
        users.add(u2);

        t1.setUsers(users);

//        b1.calcLength();
//        assertEquals(u1t1, t1.getMinTime());
//        assertEquals(u2t2, t1.getMaxTime());

    }

    @Test
    public void TestCalcGenre() {
        Theatre t1 = new Theatre("123");
        BackStage b1 = new BackStage(t1);
        User u1= new User("1","1", "1");
        u1.addGenres("Horror");
        u1.addGenres("Fantasy");
        u1.addGenres("Drama");

        User u2= new User("2","2","2");
        u2.addGenres("Horror");
        u2.addGenres("Comedy");

        User u3= new User("3","3","3");
        u3.addGenres("Horror");
        u3.addGenres("Foreign");
        u3.addGenres("Drama");

        t1.addUser(u1);
        t1.addUser(u2);
        t1.addUser(u3);

        b1.calcGenre();

        assertEquals("First choice","Horror", t1.getGenres().get(0));
        assertEquals("Second choice","Drama", t1.getGenres().get(1));

    }

    @Test
    public void TestCalcResultMultiUser() {
        Theatre t1 = new Theatre("123");
        //BackStage b1 = t1.getBackstage();
        BackStage b1 = new BackStage(t1);

        Media a = new Media("A");
        Media b = new Media("B");
        Media c = new Media("C");
        Media d = new Media("D");
        Media e = new Media( "E");

        User u1= new User("1","1", "1");
        User u2= new User("2","2","2");
        User u3= new User("3","3","3");
        User u4 = new User("4", "4","4");
        User u5 = new User("5", "5","5");

        t1.addUser(u1);
        t1.addUser(u2);
        t1.addUser(u3);
        t1.addUser(u4);
        t1.addUser(u5);

        u1.addRankingToEnd(a);
        u1.addRankingToEnd(b);
        u1.addRankingToEnd(c);
        u1.addRankingToEnd(d);
        u1.addRankingToEnd(e);

        u2.addRankingToEnd(c);
        u2.addRankingToEnd(d);
        u2.addRankingToEnd(e);
        u2.addRankingToEnd(b);
        u2.addRankingToEnd(a);

        u3.addRankingToEnd(b);
        u3.addRankingToEnd(c);
        u3.addRankingToEnd(e);
        u3.addRankingToEnd(a);
        u3.addRankingToEnd(d);

        u4.addRankingToEnd(b);
        u4.addRankingToEnd(e);
        u4.addRankingToEnd(c);
        u4.addRankingToEnd(d);
        u4.addRankingToEnd(a);

        u5.addRankingToEnd(a);
        u5.addRankingToEnd(c);
        u5.addRankingToEnd(b);
        u5.addRankingToEnd(e);
        u5.addRankingToEnd(d);

        List<Media> choices = new ArrayList<>();
        choices.add(a);
        choices.add(b);
        choices.add(c);
        choices.add(d);
        choices.add(e);

        b1.calcResult(choices);

        assertEquals("Media Result", b.getId(), t1.getResult().getFinalDecision().getId());

    }

    @Test
    public void TestCalcResultSingleUser() {
        Theatre t2 = new Theatre("1234");
        //BackStage b1 = t1.getBackstage();
        BackStage b1 = new BackStage(t2);

        Media a = new Media("A");
        Media b = new Media("B");
        Media c = new Media("C");
        Media d = new Media("D");
        Media e = new Media( "E");

        User u1= new User("1","1", "1");

        t2.addUser(u1);

        u1.addRankingToEnd(c);
        u1.addRankingToEnd(b);
        u1.addRankingToEnd(a);
        u1.addRankingToEnd(d);
        u1.addRankingToEnd(e);

        List<Media> choices = new ArrayList<>();
        choices.add(a);
        choices.add(b);
        choices.add(c);
        choices.add(d);
        choices.add(e);

        b1.calcResult(choices);

        assertEquals("Media Result", c.getId(), t2.getResult().getFinalDecision().getId());

    }
}