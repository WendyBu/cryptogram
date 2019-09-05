package edu.gatech.seclass.crypto6300;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

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
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4.class)
public class PlayCryptogramTest {

    Context appContext;
    DatabaseHandler databaseHandler;

    @Rule
    public ActivityTestRule<LoginActivity> tActivityRule = new ActivityTestRule<>(
            LoginActivity.class);

    @Before
    public void init() {
        appContext = InstrumentationRegistry.getTargetContext();
        databaseHandler = new DatabaseHandler(appContext);
        databaseHandler.onUpgrade(databaseHandler.getWritableDatabase(),1,1);
        databaseHandler.createAdministrator(new Administrator("foo1", "aaaaaaaa"));
    }

    @Test
    public void runTests() {
        loginAdmin();
        adminHomeSelectAddPlayer();
        addPlayer("player123", "abcdabcd", "happy", "path");
        adminHomeSelectAddCryptogram();
        addCryptogram("test1");
        clickLogout();

        loginPlayer("player123", "abcdabcd");
        playerHomeSelectSelectCryptogram();
        selectCryptogram(0);

        checkSubmitButton();
        checkViewStatsButton();
        checkSelectCryptogramButton();
        selectCryptogram(0);
        checkHomeButton();
        checkSelectCryptogramButton();
        checkLogOutButton();

    }

    //@Test
    public void checkHomeButton() {
        onView(withId(R.id.homeButton)).perform(click());
    }

    //@Test
    public void checkLogOutButton() {
        onView(withId(R.id.logoutButton)).perform(click());
    }

    //@Test (timeout = 5000)
    public void checkSelectCryptogramButton() {
        long start = System.nanoTime();
        onView(withId(R.id.button_select_cryptogram)).perform(click());
        long end = System.nanoTime();
        assert(end-start < 5000000000L);
    }

    //@Test (timeout = 5000)
    public void checkViewStatsButton() {
        long start = System.nanoTime();
        onView(withId(R.id.button_view_statistics)).perform(click());
        long end = System.nanoTime();
        assert(end-start < 5000000000L);
    }

    //@Test
    public void checkSubmitButton() {
        onView(withId(R.id.button_submit)).perform(click());
    }


    private void addCryptogram(String name) {
        onView(withId(R.id.cryptogram_name)).perform(clearText(), typeText(name));
        onView(withId(R.id.unencrypted_phrase)).perform(clearText(), typeText("test yes"));
        onView(withId(R.id.max_number_solution_attempts_easy)).perform(clearText(), typeText("3"));
        onView(withId(R.id.max_number_solution_attempts_medium)).perform(clearText(), typeText("2"));
        onView(withId(R.id.max_number_solution_attempts_hard)).perform(clearText(), typeText("3"), closeSoftKeyboard());
        onView(withId(R.id.button_save)).perform(click());

    }
    private void selectCryptogram(int index) {
        onData(anything()).inAdapterView(withId(R.id.cryptogramListView)).atPosition(index).perform(click());
    }
    private void clickLogout() {
        onView(withId(R.id.button_log_out)).perform(click());
    }
    private void addPlayer(String username, String password, String firstname, String lastname) {
        onView(withId(R.id.username)).perform(clearText(), typeText(username));
        onView(withId(R.id.password)).perform(clearText(), typeText(password));
        onView(withId(R.id.first_name)).perform(clearText(), typeText(firstname));
        onView(withId(R.id.last_name)).perform(clearText(), typeText(lastname), closeSoftKeyboard());
        onView(withId(R.id.button_save)).perform(click());
    }
    private void adminHomeSelectAddPlayer() {
        onView(withId(R.id.button_add_player)).perform(click());
    }
    private void adminHomeSelectAddCryptogram() {
        onView(withId(R.id.button_add_cryptogram)).perform(click());
    }
    private void loginAdmin() {
        onView(withId(R.id.radio_admin)).perform(click());
        onView(withId(R.id.username)).perform(clearText(), typeText("foo1"));
        onView(withId(R.id.password)).perform(clearText(), typeText("aaaaaaaa"), closeSoftKeyboard());
        onView(withId(R.id.log_in_button)).perform(click());
    }

    private void loginPlayer(String username, String pass) {
        onView(withId(R.id.radio_player)).perform(click());
        onView(withId(R.id.username)).perform(clearText(), typeText(username));
        onView(withId(R.id.password)).perform(clearText(), typeText(pass), closeSoftKeyboard());
        onView(withId(R.id.log_in_button)).perform(click());
    }
    private void playerHomeSelectSelectCryptogram() {
        onView(withId(R.id.button_select_cryptogram)).perform(click());
    }
}