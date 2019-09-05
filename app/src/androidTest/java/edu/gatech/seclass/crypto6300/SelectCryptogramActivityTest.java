package edu.gatech.seclass.crypto6300;

import android.content.ComponentName;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.intent.Intents;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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

import edu.gatech.seclass.crypto6300.database.DatabaseHandler;
import edu.gatech.seclass.crypto6300.models.Administrator;
import edu.gatech.seclass.crypto6300.models.Cryptogram;
import edu.gatech.seclass.crypto6300.models.GlobalClass;
import edu.gatech.seclass.crypto6300.models.Player;
import edu.gatech.seclass.crypto6300.utils.Enums;



@RunWith(AndroidJUnit4.class)
public class SelectCryptogramActivityTest {
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
        Cryptogram c = new Cryptogram("testnamexx", "testSolutionx", new int[] {8, 6, 4});
        StringBuffer msg = new StringBuffer();
        databaseHandler.createCryptogram(c, msg);
        Player player = new Player("aaa", "bbb", "aaabbb1", "bbbbbbbb", Enums.DifficultyCategory.EASY);
        databaseHandler.createPlayer(player);
        GlobalClass currentUser = new GlobalClass();
        currentUser.setCurrentPlayerName("aaabbb1");
    }


    @After
    public void destroy() {
        databaseHandler.onUpgrade(databaseHandler.getWritableDatabase(),1,1);
    }


    @Test
    public void selectCryptogram() {
        loginPlayer();
        playerHomeSelectSelectCryptogram();
        onData(anything()).inAdapterView(withId(R.id.cryptogramListView)).atPosition(0).perform(click());
    }

    @Test
    public void selectCryptogramHomeButton() {
        loginPlayer();
        playerHomeSelectSelectCryptogram();
        onView(withId(R.id.homeButton)).perform(click());
    }

    @Test
    public void selectCryptogramLogoutButton() {
        loginPlayer();
        playerHomeSelectSelectCryptogram();
        onView(withId(R.id.logoutButton)).perform(click());
    }

    @Test
    public void viewStatButtonTest() {
        loginPlayer();
        playerHomeSelectSelectCryptogram();
        onView(withId(R.id.button_view_statistics)).perform(click());
    }

     private void loginPlayer(){
         onView(withId(R.id.radio_player)).perform(click());
         onView(withId(R.id.username)).perform(clearText(), typeText("aaabbb1"), closeSoftKeyboard());
         onView(withId(R.id.password)).perform(clearText(), typeText("bbbbbbbb"), closeSoftKeyboard());
         onView(withId(R.id.log_in_button)).perform(click());
     }

    private void playerHomeSelectSelectCryptogram() {
        onView(withId(R.id.button_select_cryptogram)).perform(click());
    }
}
