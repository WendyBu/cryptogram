package edu.gatech.seclass.crypto6300;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
public class ViewStatisticsAdminTest {

    @Rule
    public ActivityTestRule<ViewStatisticsAdministratorActivity> lActivityRule =
            new ActivityTestRule<>(ViewStatisticsAdministratorActivity.class);
    @Test
    public void checkHomeButton() {
        onView(withId(R.id.button_home)).perform(click());
    }

    @Test
    public void checkAddCryptogramButton() {
        onView(withId(R.id.button_add_cryptogram)).perform(click());
    }

    @Test
    public void checkAddPlayerButton() {
        onView(withId(R.id.button_add_player)).perform(click());
    }
    @Test
    public void checkLogOutButton() {
        onView(withId(R.id.button_log_out)).perform(click());
    }
}
