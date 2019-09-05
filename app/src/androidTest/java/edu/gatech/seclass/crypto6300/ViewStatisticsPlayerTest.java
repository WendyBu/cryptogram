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
public class ViewStatisticsPlayerTest {

    @Rule
    public ActivityTestRule<ViewStatisticsPlayerActivity> lActivityRule =
            new ActivityTestRule<>(ViewStatisticsPlayerActivity.class);
    @Test
    public void checkHomeButton() {
        onView(withId(R.id.button_home)).perform(click());
    }

    @Test
    public void checkSelectCryptogramButton() {
        onView(withId(R.id.button_select_cryptogram)).perform(click());
    }

    @Test
    public void checkLogOutButton() {
        onView(withId(R.id.button_log_out)).perform(click());
    }
}
