package com.example.androidassignments;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class TestToolbarTest {

    private ActivityScenario<TestToolbar> scenario;

    @Before
    public void setUp() {
        Intents.init();
        scenario = ActivityScenario.launch(TestToolbar.class);
    }

    @After
    public void tearDown() {
        Intents.release();
        scenario.close();
    }

    @Test
    public void testFacebookMenuItem_WhenClicked_ShowsDefaultSnackbar() {
        onView(withId(R.id.action_fb)).perform(click());
        onView(withText("You selected item 1"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testTwitterMenuItem_WhenClicked_ShowsConfirmationDialog() {
        onView(withId(R.id.action_twt)).perform(click());
        onView(withText(R.string.dialog_title))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
    }

    @Test
    public void testTikTokMenuItem_WhenClicked_ShowsCustomDialog() {

        onView(withId(R.id.action_tik)).perform(click());

        onView(withText(R.string.new_msg))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));

        onView(withId(R.id.dialog_message_box))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
    }

    @Test
    public void testCustomDialog_WhenMessageEntered_UpdatesSnackbar() {
        final String testMessage = "Test message 123";

        onView(withId(R.id.action_tik)).perform(click());
        onView(withId(R.id.dialog_message_box))
                .inRoot(isDialog())
                .perform(typeText(testMessage));
        onView(withText(R.string.ok))
                .inRoot(isDialog())
                .perform(click());

        onView(withId(R.id.action_fb)).perform(click());
        onView(withText(testMessage))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testFAB_WhenClicked_ShowsWelcomeSnackbar() {
        onView(withId(R.id.fab)).perform(click());
        onView(withText("Welcome to my Toolbar page"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testTwitterDialog_WhenOkClicked_FinishesActivity() {
        onView(withId(R.id.action_twt)).perform(click());
        onView(withText(R.string.ok))
                .inRoot(isDialog())
                .perform(click());

        scenario.onActivity(activity -> {
            assert activity.isFinishing();
        });
    }

}
