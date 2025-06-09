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
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    // test to confirm that clicking the button launches the listitems activty
    @Test
    public void clickingListItemsButton_startsListItemsActivity() {
        ActivityScenario.launch(MainActivity.class);
        onView(withId(R.id.button)).perform(click());
        intended(IntentMatchers.hasComponent(ListItemsActivity.class.getName()));
    }

    // test to confirm that clicking the start chat button launches the chatwindow activty
    @Test
    public void clickingChatButton_startsChatWindow() {
        ActivityScenario.launch(MainActivity.class);
        onView(withId(R.id.chat_btn)).perform(click());
        intended(IntentMatchers.hasComponent(ChatWindow.class.getName()));
    }
}

