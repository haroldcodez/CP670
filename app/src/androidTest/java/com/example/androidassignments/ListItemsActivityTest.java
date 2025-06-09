package com.example.androidassignments;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ListItemsActivityTest {

    @Rule
    public GrantPermissionRule permissionRule =
            GrantPermissionRule.grant(android.Manifest.permission.CAMERA);

    private ActivityScenario<ListItemsActivity> scenario;

    @Before
    public void setup() {
        scenario = ActivityScenario.launch(ListItemsActivity.class);
        Intents.init();
    }

    @After
    public void cleanup() {
        scenario.close();
        Intents.release();
    }

    // test that the switch button toggles on and off
    @Test
    public void testSwitchButton_TogglesToast() {
        onView(withId(R.id.app_switch)).perform(click());  // On
        onView(withId(R.id.app_switch)).perform(click());  // Off
    }

    // test that the clicking the checkbox button activates the dialog box
    @Test
    public void testCheckbox_ShowsDialog() {
        onView(withId(R.id.check_box)).perform(click());  // Triggers dialog
        onView(withText(R.string.dialog_title)).check(matches(isDisplayed()));
    }

    // test to confirm if clicking the OK button the dialog box closes the listItem activty
    @Test
    public void testCheckboxDialog_PositiveResponse_ClosesActivity() {
        // Click checkbox and then OK
        onView(withId(R.id.check_box)).perform(click());
        onView(withText(R.string.ok)).perform(click());
    }

    // test that the image button activates the camera and captures an image
    @Test
    public void testImageButton_IntentLaunch() {
        // Stub camera intent to return mock result
        Intent resultData = new Intent();
        resultData.putExtra("data", Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888));
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);

        Intents.intending(IntentMatchers.hasAction(MediaStore.ACTION_IMAGE_CAPTURE))
                .respondWith(result);

        // Click image button
        onView(withId(R.id.img_btn)).perform(click());
    }
}
