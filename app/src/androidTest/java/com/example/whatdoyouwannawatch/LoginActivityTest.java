package com.example.whatdoyouwannawatch;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4ClassRunner.class)
public class LoginActivityTest {
    @Rule
    public ActivityScenarioRule loginRule = new ActivityScenarioRule(LoginActivity.class);

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
        FirebaseAuth.getInstance().signOut();
    }

    @Test
    public void onCreate() {
        assertNotNull(loginRule.getScenario());
        onView(withId(R.id.textView_promptForEmail)).check(matches(withText("Email")));
        onView(withId(R.id.textView_promptForPassword)).check(matches(withText("Password")));
        onView(withId(R.id.button_loginLoginPage)).check(matches(withText("Log In")));
    }

    @Test
    public void onClickLogIn() {
        onView(withId(R.id.editTextEmailAddressLogin)).perform(typeText("c1@gmail.com"));
        onView(withId(R.id.editTextPasswordLogin)).perform(typeText("123456"));
        closeSoftKeyboard();
        onView(withId(R.id.button_loginLoginPage)).perform(click());
        //TODO wait for the dupeMessage
        onView(withText("Logging you in. Hang on tight...")).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
        FirebaseAuth.getInstance().signOut();
    }
}