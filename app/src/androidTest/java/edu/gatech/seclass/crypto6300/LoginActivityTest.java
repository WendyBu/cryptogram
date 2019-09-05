package edu.gatech.seclass.crypto6300;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import java.io.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;


import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import android.support.test.espresso.intent.rule.IntentsTestRule;


import edu.gatech.seclass.crypto6300.database.DatabaseHandler;
import edu.gatech.seclass.crypto6300.models.Administrator;


@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
    Context appContext;
    DatabaseHandler databaseHandler;

    @Rule
    public IntentsTestRule<LoginActivity> lgIntentsTestRule =
            new IntentsTestRule<>(LoginActivity.class);

    @Before
    public void init() {
        appContext = InstrumentationRegistry.getTargetContext();
        databaseHandler = new DatabaseHandler(appContext);
        databaseHandler.onUpgrade(databaseHandler.getWritableDatabase(),1,1);
        databaseHandler.createAdministrator(new Administrator("foo1", "aaaaaaaa"));
    }
    @After
    public void destroy() {
        databaseHandler.onUpgrade(databaseHandler.getWritableDatabase(),1,1);
    }


    @Test
    public void ensureTextChangesWork() throws IOException {
        onView(withId(R.id.username)).perform(clearText(), typeText("username1"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(clearText(), typeText("password1"), closeSoftKeyboard());
        onView(withId(R.id.username)).check(matches(withText("username1")));
        onView(withId(R.id.password)).check(matches(withText("password1")));
    }

    @Test
    public void loginInAdmin() {
        onView(withId(R.id.radio_admin)).perform(click());
        onView(withId(R.id.username)).perform(clearText(), typeText("foo1"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(clearText(), typeText("aaaaaaaa"), closeSoftKeyboard());
        onView(withId(R.id.log_in_button)).perform(click());
        intended(hasComponent(HomeAdministratorActivity.class.getName()));
    }

    @Test
    public void loginPlayer() {
        loginAdmin();
        adminHomeSelectAddPlayer();
        addPlayer("player456", "abcdabcd", "funny", "path");
        clickLogout();
        onView(withId(R.id.radio_player)).perform(click());
        onView(withId(R.id.username)).perform(clearText(), typeText("player456"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(clearText(), typeText("abcdabcd"), closeSoftKeyboard());
        onView(withId(R.id.log_in_button)).perform(click());
        intended(hasComponent(HomePlayerActivity.class.getName()));
    }

    @Test
    public void clear() {
        onView(withId(R.id.username)).perform(clearText(), typeText("username1"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(clearText(), typeText("password1"), closeSoftKeyboard());
        onView(withId(R.id.clear_button)).perform(click());
        onView(withId(R.id.username)).check(matches(withText("")));
        onView(withId(R.id.password)).check(matches(withText("")));
    }


    // create the first player
    private void loginAdmin() {
        onView(withId(R.id.radio_admin)).perform(click());
        onView(withId(R.id.username)).perform(clearText(), typeText("foo1"));
        onView(withId(R.id.password)).perform(clearText(), typeText("aaaaaaaa"), closeSoftKeyboard());
        onView(withId(R.id.log_in_button)).perform(click());
    }

    private void adminHomeSelectAddPlayer() {
        onView(withId(R.id.button_add_player)).perform(click());
    }

    private void addPlayer(String username, String password, String firstname, String lastname) {
        onView(withId(R.id.username)).perform(clearText(), typeText(username));
        onView(withId(R.id.password)).perform(clearText(), typeText(password));
        onView(withId(R.id.first_name)).perform(clearText(), typeText(firstname));
        onView(withId(R.id.last_name)).perform(clearText(), typeText(lastname), closeSoftKeyboard());
        onView(withId(R.id.button_save)).perform(click());
    }

    private void clickLogout() {
        onView(withId(R.id.button_log_out)).perform(click());
    }
}
