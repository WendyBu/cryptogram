package edu.gatech.seclass.crypto6300;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.DataInteraction;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import edu.gatech.seclass.crypto6300.database.DatabaseHandler;
import edu.gatech.seclass.crypto6300.models.Administrator;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

public class IntegrationTest {

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
    @After
    public void destroy() {
        databaseHandler.onUpgrade(databaseHandler.getWritableDatabase(),1,1);
    }
    @Test
    public void happyPath() {

        loginAdmin();
        adminHomeSelectAddPlayer();
        addPlayer("player123", "abcdabcd", "happy", "path");
        adminHomeSelectAddPlayer();
        addPlayer("player456", "abcdabcd", "funny", "path");
        adminHomeSelectAddCryptogram();
        addCryptogram("test1");
        adminHomeSelectAddCryptogram();
        addCryptogram("test2");
        clickLogout();

        loginPlayer("player123", "abcdabcd");
        playerHomeSelectSelectCryptogram();
        utilTestCryptogramListView(0, "test1", "unstarted");
        utilTestCryptogramListView(1, "test2", "unstarted");
        selectCryptogram(0);
        playCryptogramWin();
        utilTestCryptogramListView(0, "test2", "unstarted");
        selectCryptogram(0);
        playCryptogramInProgress();
        utilTestCryptogramListView(0, "test2", "in-progress");
        viewPlayerStat();
        checkPlayerStat(0, "happy", "1", "0");
        checkPlayerStat(1, "funny", "0", "0");
        clickLogout();


        loginPlayer("player456", "abcdabcd");
        playerHomeSelectSelectCryptogram();
        utilTestCryptogramListView(0, "test1", "unstarted");
        utilTestCryptogramListView(1, "test2", "unstarted");
        selectCryptogram(1);
        playCryptogramLost();
        utilTestCryptogramListView(0, "test1", "unstarted");
        viewPlayerStat();
        checkPlayerStat(0, "happy", "1", "0");
        checkPlayerStat(1, "funny", "0", "1");
        clickLogout();


        loginAdmin();
        viewAdminStat();
        checkAdminStat(0, "EASY", "happy", "1", "0", "player123");
        checkAdminStat(1, "EASY", "funny", "0", "1", "player456");
        try {
            Thread.sleep(10000);
        } catch (Exception e) {

        }


    }

    private void utilTestCryptogramListView(int index, String name, String status) {
        DataInteraction item1 = onData(anything())
                .inAdapterView(withId(R.id.cryptogramListView))
                .atPosition(index);
        item1.onChildView(withId(R.id.cryptogram_name))
                .check(matches(withText(name)));
        item1.onChildView(withId(R.id.cryptogram_status))
                .check(matches(withText(status)));
    }
    private void checkPlayerStat(int index, String firstname, String win, String lost) {
        DataInteraction item = onData(anything())
                .inAdapterView(withId(R.id.statistics_list))
                .atPosition(index);
        item.onChildView(withId(R.id.text_view_first_name))
                .check(matches(withText(firstname)));
        item.onChildView(withId(R.id.text_view_num_lost))
                .check(matches(withText(lost)));
        item.onChildView(withId(R.id.text_view_num_won))
                .check(matches(withText(win)));
    }
    private void checkAdminStat(int index, String diff, String firstname, String win, String lost, String username) {
        DataInteraction item = onData(anything())
                .inAdapterView(withId(R.id.statistics_list))
                .atPosition(index);
        item.onChildView(withId(R.id.text_view_difficulty))
                .check(matches(withText(diff)));
        item.onChildView(withId(R.id.text_view_first_name))
                .check(matches(withText(firstname)));
        item.onChildView(withId(R.id.text_view_num_lost))
                .check(matches(withText(lost)));
        item.onChildView(withId(R.id.text_view_num_won))
                .check(matches(withText(win)));
        item.onChildView(withId(R.id.text_view_username))
                .check(matches(withText(username)));
    }
    private void viewAdminStat() {
        onView(withId(R.id.button_view_statistics)).perform(click());
    }
    private void viewPlayerStat() {
        onView(withId(R.id.button_view_statistics)).perform(click());
    }
    private void playCryptogramWin() {
        onView(withId(R.id.solution_attempt)).perform(clearText(), typeText("test no"), closeSoftKeyboard());
        onView(withId(R.id.button_submit)).perform(click());
        onView(withId(R.id.solution_attempt)).perform(clearText(), typeText("test "), closeSoftKeyboard());
        onView(withId(R.id.button_submit)).perform(click());
        onView(withId(R.id.solution_attempt)).perform(clearText(), typeText("test yes"), closeSoftKeyboard());
        onView(withId(R.id.button_submit)).perform(click());
    }
    private void playCryptogramLost() {
        onView(withId(R.id.solution_attempt)).perform(clearText(), typeText("test no"), closeSoftKeyboard());
        onView(withId(R.id.button_submit)).perform(click());
        onView(withId(R.id.solution_attempt)).perform(clearText(), typeText("test "), closeSoftKeyboard());
        onView(withId(R.id.button_submit)).perform(click());
        onView(withId(R.id.solution_attempt)).perform(clearText(), typeText("test no"), closeSoftKeyboard());
        onView(withId(R.id.button_submit)).perform(click());
    }

    private void playCryptogramInProgress() {
        onView(withId(R.id.solution_attempt)).perform(clearText(), typeText("test no"), closeSoftKeyboard());
        onView(withId(R.id.button_submit)).perform(click());
        onView(withId(R.id.solution_attempt)).perform(clearText(), typeText("test "), closeSoftKeyboard());
        onView(withId(R.id.button_submit)).perform(click());
        onView(withId(R.id.button_select_cryptogram)).perform(click());
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
