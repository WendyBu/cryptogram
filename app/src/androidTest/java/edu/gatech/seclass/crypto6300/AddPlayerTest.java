package edu.gatech.seclass.crypto6300;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.gatech.seclass.crypto6300.database.DatabaseHandler;
import edu.gatech.seclass.crypto6300.models.Administrator;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class AddPlayerTest {
    private final static int VERSION = 1;
    private DatabaseHandler databaseHandler;

    @Rule
    public ActivityTestRule<AddPlayerActivity> tActivityRule = new ActivityTestRule<>(AddPlayerActivity.class);

    @Before
    public void init() {
        Intents.init();

        Context appContext = InstrumentationRegistry.getTargetContext();
        databaseHandler = new DatabaseHandler(appContext);
        databaseHandler.onUpgrade(databaseHandler.getWritableDatabase(),VERSION,VERSION);
        databaseHandler.createAdministrator(new Administrator("admin", "p@ssw0rd"));
    }

    @After
    public void release() {
        Intents.release();

        databaseHandler.onUpgrade(databaseHandler.getWritableDatabase(),VERSION,VERSION);
    }

    /**
     * This test checks that if 'Home' button is clicked the 'Home' activity is shown.
     */
    @Test
    public void home(){
        onView(withId(R.id.homeButton)).perform(click());
        intended(hasComponent(HomeAdministratorActivity.class.getName()));
    }

    /**
     * This test checks that if 'Home' button is clicked the 'Home' activity is shown.
     */
    @Test
    public void logOut(){
        onView(withId(R.id.logoutButton)).perform(click());
        intended(hasComponent(LoginActivity.class.getName()));
    }

    /**
     * This test checks that if 'Add Cryptogram' button is clicked the 'Add Cryptogram' activity is shown.
     */
    @Test
    public void addCryptogram(){
        onView(withId(R.id.button_add_cryptogram)).perform(click());
        intended(hasComponent(AddCryptogramActivity.class.getName()));
    }

    /**
     * This test checks that if 'View Stats' button is clicked the 'Player Statistics' activity is shown.
     */
    @Test
    public void viewStatistics(){
        onView(withId(R.id.button_view_statistics)).perform(click());
        intended(hasComponent(ViewStatisticsAdministratorActivity.class.getName()));
    }

    /**
     * This test checks that if a new player information is added and 'Save' button is clicked then database contains new player's information and 'Home' activity is shown.
     */
    @Test
    public void addPlayer() {
        final String username = "player1";
        onView(withId(R.id.username)).perform(clearText(), typeText(username));
        onView(withId(R.id.password)).perform(clearText(), typeText("password"));
        onView(withId(R.id.first_name)).perform(clearText(), typeText("Hello"));
        onView(withId(R.id.last_name)).perform(clearText(), typeText("World"));
        onView(withId(R.id.spinner_difficulty_category)).perform(scrollTo(), click());
        onData(allOf(is(instanceOf(String.class)), is("HARD"))).perform(click());
        onView(withId(R.id.button_save)).perform(scrollTo(), click());
        assertTrue(databaseHandler.doesPlayerExist(username));
        intended(hasComponent(HomeAdministratorActivity.class.getName()));
    }

    /**
     * This test checks that user name field's text was changed.
     */
    @Test
    public void changeUsernameText() {
        String username = "user name";
        onView(withId(R.id.username)).perform(clearText(), typeText(username));
        onView(withId(R.id.username)).check(matches(withText(username)));
    }

    /**
     * This test checks that password field's text was changed.
     */
    @Test
    public void changePasswordText() {
        String password = "p@ssw0rd";
        onView(withId(R.id.password)).perform(clearText(), typeText(password));
        onView(withId(R.id.password)).check(matches(withText(password)));
    }

    /**
     * This test checks that first name field's text was changed.
     */
    @Test
    public void changeFirstNameText() {
        String firstName = "first name";
        onView(withId(R.id.first_name)).perform(clearText(), typeText(firstName));
        onView(withId(R.id.first_name)).check(matches(withText(firstName)));
    }

    /**
     * This test checks that last name field's text was changed.
     */
    @Test
    public void changeLastNameText() {
        String lastName = "last name";
        onView(withId(R.id.last_name)).perform(clearText(), typeText(lastName));
        onView(withId(R.id.last_name)).check(matches(withText(lastName)));
    }

    /**
     * This test checks that difficulty category field's was selected.
     * https://stackoverflow.com/questions/31420839/android-espresso-check-selected-spinner-text
     */
    @Test
    public void changeDifficultyCategoryText() {
        String difficultyCategory = "MEDIUM";
        onView(withId(R.id.spinner_difficulty_category)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(difficultyCategory))).perform(click());
        onView(withId(R.id.spinner_difficulty_category)).check(matches(withSpinnerText(containsString(difficultyCategory))));
    }
}
