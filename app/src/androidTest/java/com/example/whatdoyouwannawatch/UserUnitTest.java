package com.example.whatdoyouwannawatch;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

public class UserUnitTest extends TestCase {

    @Before
    public void setUp(){
        String email = "";
        String username = "";
        String uid = "";
        User guest1 = new User();
        User user1 = new User(email, username, uid);
    }

    @Test
    public void testEmail(){

    }

    public void testGetMinLength() {
    }

    public void testGetMaxLength() {
    }

//    public void testGetRegion() {
//    }
//
//    public void testGetTimezone() {
//    }

    public void testGetUID() {
    }

    public void testSetUID() {
    }

    public void testGetUsername() {
    }

    public void testSetUsername() {
    }

    public void testGetEmail() {
    }

    public void testSetEmail() {
    }

    public void testGetUserAvatar() {
    }

    public void testSetUserAvatar() {
    }

    public void testIsGuest() {
    }

    public void testSetGuest() {
    }

    public void testGetGenres() {
    }

    public void testSetGenres() {
    }

    public void testGetFriends() {
    }

    public void testSetFriends() {
    }

    public void testGetHistory() {
    }

    public void testSetHistory() {
    }

    public void testClearHistory() {
    }

    public void testTestToString() {
    }
}