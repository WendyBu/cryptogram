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

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertTrue;


@RunWith(AndroidJUnit4.class)
public class AddCryptogramTest {
    private final static int VERSION = 1;
    private DatabaseHandler databaseHandler;

    @Rule
    public ActivityTestRule<AddCryptogramActivity> tActivityRule = new ActivityTestRule<>(
            AddCryptogramActivity.class);

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
     * This test checks that if 'Add player' button is clicked the 'Add player' activity is shown.
     */
    @Test
    public void addPlayer(){
        onView(withId(R.id.button_add_player)).perform(click());
        intended(hasComponent(AddPlayerActivity.class.getName()));
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
     * This test checks that cryptogram name field's text was changed.
     */
    @Test
    public void changeCryptogramNameText(){
        String cryptogramName = "crypto";
        onView(withId(R.id.cryptogram_name)).perform(clearText(), typeText(cryptogramName));
        onView(withId(R.id.cryptogram_name)).check(matches(withText(cryptogramName)));
    }

    /**
     * This test checks that solution phrase field's text was changed.
     */
    @Test
    public void changeSolutionPhraseText(){
        String solutionPhrase = "solution phrase";
        onView(withId(R.id.unencrypted_phrase)).perform(clearText(), typeText(solutionPhrase));
        onView(withId(R.id.unencrypted_phrase)).check(matches(withText(solutionPhrase)));
    }

    /**
     * This test checks that maximum number of solution attempts for easy field's text was changed.
     */
    @Test
    public void changeNumberOfSolutionAttemptsEasyText(){
        int solutionAttempts = 3;
        onView(withId(R.id.max_number_solution_attempts_easy)).perform(clearText(), typeText(String.valueOf(solutionAttempts)));
        onView(withId(R.id.max_number_solution_attempts_easy)).check(matches(withText(String.valueOf(solutionAttempts))));
    }

    /**
     * This test checks that maximum number of solution attempts for medium field's text was changed.
     */
    @Test
    public void changeNumberOfSolutionAttemptsMediumText(){
        int solutionAttempts = 2;
        onView(withId(R.id.max_number_solution_attempts_medium)).perform(clearText(), typeText(String.valueOf(solutionAttempts)));
        onView(withId(R.id.max_number_solution_attempts_medium)).check(matches(withText(String.valueOf(solutionAttempts))));
    }

    /**
     * This test checks that maximum number of solution attempts for hard field's text was changed.
     */
    @Test
    public void changeNumberOfSolutionAttemptsHardText(){
        int solutionAttempts = 1;
        onView(withId(R.id.max_number_solution_attempts_hard)).perform(clearText(), typeText(String.valueOf(solutionAttempts)));
        onView(withId(R.id.max_number_solution_attempts_hard)).check(matches(withText(String.valueOf(solutionAttempts))));
    }


    /**
     * This test checks that if 'Save' button is clicked the 'Home' activity is shown.
     */
    @Test
    public void addCryptogram() {
        String cryptogramName = "crypto";
        onView(withId(R.id.cryptogram_name)).perform(clearText(), typeText(cryptogramName));
        onView(withId(R.id.unencrypted_phrase)).perform(clearText(), typeText("def"));
        onView(withId(R.id.max_number_solution_attempts_easy)).perform(clearText(), typeText("1"));
        onView(withId(R.id.max_number_solution_attempts_medium)).perform(clearText(), typeText("2"));
        onView(withId(R.id.max_number_solution_attempts_hard)).perform(clearText(), typeText("3"), closeSoftKeyboard());
        onView(withId(R.id.button_save)).perform(click());
        assertTrue(databaseHandler.doesCryptogramExist(cryptogramName));
        intended(hasComponent(HomeAdministratorActivity.class.getName()));
    }
}