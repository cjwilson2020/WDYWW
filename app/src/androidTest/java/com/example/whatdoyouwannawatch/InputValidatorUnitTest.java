package com.example.whatdoyouwannawatch;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * This is a unit test for InputValidator class
 */
public class InputValidatorUnitTest {
    InputValidator iv;

    @Before
    public void setup(){
        iv = new InputValidator();
    }

    @Test
    public void TestPasswordIsValid() {
        String pw1 = "";
        String pw2 = "somepw";
        String pw3 = "somereallylongpassword32167";
        assertTrue(!iv.passwordIsValid(pw1));
        assertTrue(iv.passwordIsValid(pw2));
        assertTrue(iv.passwordIsValid(pw3));
    }

    @Test
    public void emailIsValid() {
        String e1 = "";
        String e2 = "someone@somesite";
        String e3 = "avalidemail@gmail.com";
        assertTrue(!iv.emailIsValid(e1));
        assertTrue(iv.emailIsValid(e2));
        assertTrue(iv.emailIsValid(e3));
    }

    @Test
    public void usernameIsValid() {
        String un1 = "";
        String un2 = "short";
        String un3 = "CAP";
        String un4 = "Some quite long username, what do you think";
        assertTrue(!iv.usernameIsValid(un1));
        assertTrue(iv.usernameIsValid(un2));
        assertTrue(iv.usernameIsValid(un3));
        assertTrue(iv.usernameIsValid(un4));
    }
}