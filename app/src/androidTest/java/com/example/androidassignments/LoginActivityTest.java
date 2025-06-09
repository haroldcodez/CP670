package com.example.androidassignments;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.matcher.ViewMatchers.*;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Before
    public void setUp() {
        Intents.init();
        // Launch activity once before all tests to avoid multiple launches
        ActivityScenario.launch(LoginActivity.class);
        // Clear the email field before each test since the field has a default value on initialization
        clearEmailField();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    // method to clear the email field before each test since it has a default value on initialization
    private void clearEmailField() {
        onView(withId(R.id.login_text))
                .perform(clearText());
    }

    // test to confirm that entering a valid email and password followed by clicking the login button will launch mainActivity
    @Test
    public void validLogin_redirectsToMainActivity() {
        onView(withId(R.id.login_text))
                .perform(typeText("test@example.com"), closeSoftKeyboard());

        onView(withId(R.id.password_text))
                .perform(typeText("password123"), closeSoftKeyboard());

        onView(withId(R.id.login_button))
                .perform(click());

        intended(IntentMatchers.hasComponent(MainActivity.class.getName()));
    }

    // test to confirm that empty email returns an error message
    @Test
    public void emptyEmail_showsError() {
        // Email field is already cleared in setUp()
        onView(withId(R.id.password_text))
                .perform(typeText("pass123"), closeSoftKeyboard());

        onView(withId(R.id.login_button))
                .perform(click());

        onView(withId(R.id.login_text))
                .check(matches(hasErrorText("Email cannot be empty")));
    }

    // test to confirm that invalid email format returns an error message
    @Test
    public void invalidEmail_showsError() {
        onView(withId(R.id.login_text))
                .perform(typeText("invalidemail"), closeSoftKeyboard());

        onView(withId(R.id.password_text))
                .perform(typeText("pass123"), closeSoftKeyboard());

        onView(withId(R.id.login_button))
                .perform(click());

        onView(withId(R.id.login_text))
                .check(matches(hasErrorText("Invalid email format")));
    }

    // test to confirm that empty password returns an error message
    @Test
    public void emptyPassword_showsError() {
        // Email field is already cleared in setUp()
        onView(withId(R.id.login_text))
                .perform(typeText("me@wlu.com"), closeSoftKeyboard());

        onView(withId(R.id.login_button))
                .perform(click());

        onView(withId(R.id.password_text))
                .check(matches(hasErrorText("Password cannot be empty")));
    }

}

