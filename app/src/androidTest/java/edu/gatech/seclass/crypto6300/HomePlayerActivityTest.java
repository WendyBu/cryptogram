package edu.gatech.seclass.crypto6300;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import android.support.test.espresso.intent.rule.IntentsTestRule;




@RunWith(AndroidJUnit4.class)
public class HomePlayerActivityTest {

    @Rule
    public IntentsTestRule<HomePlayerActivity> HPintentsTestRule =
            new IntentsTestRule<>(HomePlayerActivity.class);

    @Test
    public void selectCryptogram() {
        onView(withId(R.id.button_select_cryptogram)).perform(click());
        intended(hasComponent(SelectCryptogramActivity.class.getName()));
    }

    @Test
    public void viewStatistics() {
        onView(withId(R.id.button_view_statistics)).perform(click());
        intended(hasComponent(ViewStatisticsPlayerActivity.class.getName()));
    }

    @Test
    public void logOut() {
        onView(withId(R.id.button_log_out)).perform(click());
        intended(hasComponent(LoginActivity.class.getName()));
    }
}