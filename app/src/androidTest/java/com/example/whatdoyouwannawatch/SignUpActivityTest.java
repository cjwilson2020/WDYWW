package com.example.whatdoyouwannawatch;

import android.content.Context;
import android.content.Intent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

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

import static androidx.core.content.ContextCompat.getSystemService;
import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4ClassRunner.class)
public class SignUpActivityTest extends TestCase {
    private String emailMessage = "You've entered an invalid email address. " + "Please try again.";
    private String pwMessage = "You've entered an invalid password. " + "Please try again.";
    private String usernameMessage = "You've entered an invalid username. " + "Please try again.";
    private String dupeMessage = "Sign up failed. " + "A user with this username already exists.";
    private String signupMessgae = "Trying to sign up. Please wait... ";


    @Rule
    public ActivityScenarioRule signupRule = new ActivityScenarioRule(SignUpActivity.class);

    @After
    public void teardown(){
        MainActivity.pullData('u', "someon1", new DataCallback() {
            @Override
            public void onCallback(Object obj1) {
                if (obj1 != null) {
                    //delete user in Database
                    User u = (User) obj1;
                    MainActivity.deleteData(u);
                    //delete User in FB Auth
                    FirebaseAuth.getInstance().getCurrentUser().delete();
                }
            }
        });
    }

    @Test
    public void onCreate() {
        assertNotNull(signupRule.getScenario());
        onView(withId(R.id.textView_promptForEmail)).check(matches(withText("Enter Email:")));
        onView(withId(R.id.textView_promptForUsername)).check(matches(withText("Enter Username:")));
        onView(withId(R.id.textView_promptForPassword)).check(matches(withText("Enter Password")));
    }

    @Test
    public void onClickSignUp1() {
        onView(withId(R.id.button_login)).perform(click());
        onView(withText(emailMessage)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
//        onView(withId(R.id.editTextPassword)).perform(typeText("123456"));
//        closeSoftKeyboard();
//        onView(withId(R.id.button_login)).perform(click());
//        onView(withText(usernameMessage)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
//        onView(withId(R.id.editText_username)).perform(typeText("someone1"));
//        closeSoftKeyboard();
//        onView(withId(R.id.button_login)).perform(click());
//        onView(withText(signupMessgae)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }

    @Test
    public void onClickSignUp2() {
        onView(withId(R.id.editTextEmailAddress)).perform(typeText("someemail@gmail.com"));
        closeSoftKeyboard();
        onView(withId(R.id.button_login)).perform(click());
        onView(withText(pwMessage)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
//        onView(withId(R.id.editTextPassword)).perform(typeText("123456"));
//        closeSoftKeyboard();
//        onView(withId(R.id.button_login)).perform(click());
//        onView(withText(usernameMessage)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
//        onView(withId(R.id.editText_username)).perform(typeText("someone1"));
//        closeSoftKeyboard();
//        onView(withId(R.id.button_login)).perform(click());
//        onView(withText(signupMessgae)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }

    @Test
    public void onClickSignUp3() {
        onView(withId(R.id.editTextEmailAddress)).perform(typeText("someemail@gmail.com"));
        onView(withId(R.id.editTextPassword)).perform(typeText("123456"));
        closeSoftKeyboard();
        onView(withId(R.id.button_login)).perform(click());
        onView(withText(usernameMessage)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
//        onView(withId(R.id.editText_username)).perform(typeText("someone1"));
//        closeSoftKeyboard();
//        onView(withId(R.id.button_login)).perform(click());
//        onView(withText(signupMessgae)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }

    @Test
    public void onClickSignUp4() {
        onView(withId(R.id.editTextEmailAddress)).perform(typeText("someemail@gmail.com"));
        onView(withId(R.id.editTextPassword)).perform(typeText("123456"));
        onView(withId(R.id.editText_username)).perform(typeText("someone1"));
        closeSoftKeyboard();
        onView(withId(R.id.button_login)).perform(click());
        onView(withText(signupMessgae)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }

    @Test
    public void onClickSignUp5() {
        onView(withId(R.id.editTextEmailAddress)).perform(typeText("c1@gmail.com"));
        onView(withId(R.id.editTextPassword)).perform(typeText("123456"));
        onView(withId(R.id.editText_username)).perform(typeText("c1"));
        closeSoftKeyboard();
        onView(withId(R.id.button_login)).perform(click());
        //TODO wait for the dupeMessage
        onView(withText(signupMessgae)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }
}