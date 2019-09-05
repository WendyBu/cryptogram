package edu.gatech.seclass.crypto6300;

import android.content.ComponentName;
import android.support.test.espresso.DataInteraction;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anything;

import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import android.support.test.espresso.intent.rule.IntentsTestRule;




@RunWith(AndroidJUnit4.class)
public class HomeAdministratorActivityTest {

    @Rule
    public IntentsTestRule<HomeAdministratorActivity> HAintentsTestRule =
            new IntentsTestRule<>(HomeAdministratorActivity.class);

    @Test
    public void addPlayer() {
        onView(withId(R.id.button_add_player)).perform(click());
        intended(hasComponent(AddPlayerActivity.class.getName()));
    }

    @Test
    public void addCryptogram() {
        onView(withId(R.id.button_add_cryptogram)).perform(click());
        intended(hasComponent(AddCryptogramActivity.class.getName()));
    }

    @Test
    public void viewStatButtonTest() {
        onView(withId(R.id.button_view_statistics)).perform(click());
        intended(hasComponent(ViewStatisticsAdministratorActivity.class.getName()));
    }

    @Test
    public void logoutButtonTest() {
        onView(withId(R.id.button_log_out)).perform(click());
        intended(hasComponent(LoginActivity.class.getName()));
    }
}