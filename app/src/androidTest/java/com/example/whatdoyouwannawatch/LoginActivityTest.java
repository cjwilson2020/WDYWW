package com.example.whatdoyouwannawatch;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
    public ActivityScenarioRule loginRule = new ActivityScenarioRule(LoginActivity.class);

    @Before
    public void setUp() throws Exception {
        //TODO push a new user
    }

    @After
    public void tearDown() throws Exception {
        //TODO delete the user
    }

    @Test
    public void onCreate() {
        assertNotNull(loginRule.getScenario());
    }

    @Test
    public void onClickLogIn() {

    }
}