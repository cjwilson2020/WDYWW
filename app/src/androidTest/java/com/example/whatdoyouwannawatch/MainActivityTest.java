package com.example.whatdoyouwannawatch;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4ClassRunner.class)
public class MainActivityTest {
    public FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Rule
    public ActivityScenarioRule mainRule = new ActivityScenarioRule(MainActivity.class);

    @Before
    public void setup(){
        Intents.init();
    }

    @After
    public void teardown() {
        Intents.release();
    }

    @Test
    public void onCreate() {
        assertNotNull(mainRule.getScenario());
        onView(withId(R.id.button_login)).check(matches(withText("Log In")));
        onView(withId(R.id.button_signup)).check(matches(withText("Sign Up")));
        onView(withId(R.id.button_continueAsGuest)).check(matches(withText("Continue As Guest")));
    }

//    @Test
//    public void removeGuest() {
//        onView(withId(R.id.button_continueAsGuest)).perform(click());
//        mAuth = FirebaseAuth.getInstance();
//        currentUser = mAuth.getCurrentUser();
////        assertTrue(currentUser.isAnonymous());
//        pressBack();
//        assertNull(mAuth.getCurrentUser());
//    }

//    @Test
//    public void removeUserFromTheatre() {
//    }
//
//    @Test
//    public void removeTheatre() {
//    }
//
//    @Test
//    public void pushData() {
//    }
//
//    @Test
//    public void pullData() {
//    }
//
//    @Test
//    public void deleteData() {
//    }
//
    @Test
    public void onClickLogIn() {
        onView(withId(R.id.button_login)).perform(click());
        intended(hasComponent(LoginActivity.class.getName()));
    }

    @Test
    public void onClickSignUp() {
        onView(withId(R.id.button_signup)).perform(click());
        intended(hasComponent(SignUpActivity.class.getName()));
    }


//    //TODO some times work
//    @Test
//    public void onClickContinueAsGuest() {
//        onView(withId(R.id.button_continueAsGuest)).perform(click());
//        mAuth = FirebaseAuth.getInstance();
//        currentUser = mAuth.getCurrentUser();
//
//        if (currentUser != null) {
//            MainActivity.pullData('u', currentUser.getDisplayName(), new DataCallback() {
//                @Override
//                public void onCallback(Object obj) {
//                    User u = (User) obj;
//                    if (u != null){
//                        assertTrue(u.isGuest());
//                    }else{
//                        assertEquals("Not currently signed in as anyone", currentUser, null);
//                    }
//                }
//            });
//        }else{
//            assertEquals("Not currently signed in as anyone", currentUser, null);
//        }
//    }

    @Test
    public void onResume() {
        onView(withId(R.id.button_continueAsGuest)).perform(click());
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
//        assertTrue(currentUser.isAnonymous());
//        pressBack();
        if (currentUser != null) {
            String uname = currentUser.getDisplayName();
            MainActivity.pullData('u', uname, new DataCallback() {
                @Override
                public void onCallback(Object obj) {
                    User u = (User) obj;
                    assertNull(u);
                }
            });
        }else{
            assertNull(currentUser);
        }
    }



//    @Test
//    public void apiCallSearch() {
//    }
//
//    @Test
//    public void apiCallImage() {
//    }
//
//    @Test
//    public void checkProfileImg() {
//    }
//
//    @Test
//    public void setProfileImg() {
//    }
//
//    @Test
//    public void downloadProfileImg() {
//    }
}