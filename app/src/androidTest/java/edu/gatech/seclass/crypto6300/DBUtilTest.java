package edu.gatech.seclass.crypto6300;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import edu.gatech.seclass.crypto6300.database.DatabaseHandler;
import edu.gatech.seclass.crypto6300.models.Cryptogram;
import edu.gatech.seclass.crypto6300.models.GameProgress;
import edu.gatech.seclass.crypto6300.models.Player;
import edu.gatech.seclass.crypto6300.utils.Enums;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DBUtilTest {

    Context appContext;
    DatabaseHandler databaseHandler;

    @Before
    public void init() {
        appContext = InstrumentationRegistry.getTargetContext();
        databaseHandler = new DatabaseHandler(appContext);
        databaseHandler.onUpgrade(databaseHandler.getWritableDatabase(),1,1);
    }
    @After
    public void destroy() {
        databaseHandler.onUpgrade(databaseHandler.getWritableDatabase(),1,1);
    }

    @Test
    public void addCryptogram() {

        databaseHandler.onUpgrade(databaseHandler.getWritableDatabase(), 1, 1);
        Cryptogram c = new Cryptogram("testname", "testSolution", new int[] {3,2,4});

        StringBuffer msg = new StringBuffer();
        databaseHandler.createCryptogram(c, msg);

        List<Cryptogram> a = databaseHandler.getCryptogramList(null);
        for (Cryptogram b: a) {
            assertEquals(b.getCryptogramName(), "testname");
        }
        boolean rcode = databaseHandler.createCryptogram(c, msg);
        assertFalse(rcode);

    }

    @Test
    public void addProgress() {
        databaseHandler.onUpgrade(databaseHandler.getWritableDatabase(), 1, 1);
        GameProgress g = new GameProgress("a", "testname", "test text", Enums.CryptogramStatus.INPROGRESS, 0, "encrypted pharse");

        StringBuffer msg = new StringBuffer();

        assertFalse(databaseHandler.doesProgressExist("a", "testname"));

        databaseHandler.updateProgress(g, msg);

        ArrayList<GameProgress> a = databaseHandler.getProgressList("a", null);
        for (GameProgress b : a) {
            assertEquals("testname", b.getCurrentCryptogram());
        }
        assertTrue(databaseHandler.doesProgressExist("a", "testname"));

        // add second
        GameProgress g2 = new GameProgress("b", "testname2", "test text", Enums.CryptogramStatus.INPROGRESS, 0, "encrypted pharse");
        databaseHandler.updateProgress(g2, msg);

        ArrayList<GameProgress> b1 = databaseHandler.getProgressList("b", null);
        for (GameProgress b : b1) {
            assertEquals("testname2", b.getCurrentCryptogram());
        }

        // Update a
        GameProgress g3 = new GameProgress("a", "testname", "test text2", Enums.CryptogramStatus.INPROGRESS, 1, "encrypted pharse");
        databaseHandler.updateProgress(g3, msg);

        ArrayList<GameProgress> c = databaseHandler.getProgressList("a", null);
        for (GameProgress b : c) {
            assertEquals("testname", b.getCurrentCryptogram());
            assertEquals(1, b.getCurrentPlayCount());
        }

    }
    @Test
    public void testGetProgressList() {
        databaseHandler.onUpgrade(databaseHandler.getWritableDatabase(), 1, 1);

        // insert a with testname
        GameProgress g = new GameProgress("a", "testname", "test text", Enums.CryptogramStatus.INPROGRESS, 0, "encrypted pharse");
        StringBuffer msg = new StringBuffer();
        assertFalse(databaseHandler.doesProgressExist("a", "testname"));
        databaseHandler.updateProgress(g, msg);

        ArrayList<GameProgress> a = databaseHandler.getProgressList("a", "testname");
        for (GameProgress b: a) {
            assertEquals("testname", b.getCurrentCryptogram());
        }

        // get progress list for one player and one cryptogram
        // insert a with testname2
        GameProgress g2 = new GameProgress("a", "testname2", "test text", Enums.CryptogramStatus.INPROGRESS, 0, "encrypted pharse");
        databaseHandler.updateProgress(g2, msg);
        assertTrue(databaseHandler.doesProgressExist("a", "testname2"));

        ArrayList<GameProgress> a1 = databaseHandler.getProgressList("a", "testname");
        assertEquals(1, a1.size());

        // get progress list for one player
        ArrayList<GameProgress> a2 = databaseHandler.getProgressList("a", null);
        assertEquals(2, a2.size());

        // get progress list for all
        // insert b with testname
        GameProgress g3 = new GameProgress("b", "testname", "test text2", Enums.CryptogramStatus.INPROGRESS, 1, "encrypted pharse");
        databaseHandler.updateProgress(g3, msg);
        ArrayList<GameProgress> a3 = databaseHandler.getProgressList(null, null);
        assertEquals(3, a3.size());

        // get progress list for one cryptogram
        ArrayList<GameProgress> a4 = databaseHandler.getProgressList(null, "testname");
        assertEquals(2, a4.size());

    }
    @Test
    public void testGetPlayersList() {
        databaseHandler.onUpgrade(databaseHandler.getWritableDatabase(), 1, 1);
        Player player = new Player("aa", "bb", "aaa", "bbb", Enums.DifficultyCategory.EASY);
        databaseHandler.createPlayer(player);
        ArrayList<Player> players = databaseHandler.getPlayersList("aaa");
        assertEquals(1, players.size());
        for (Player p : players) {
            assertEquals("aaa", p.getUserName());
        }

        // update user aaa
        Player player2 = new Player("aa2", "bb2", "aaa", "bbb", Enums.DifficultyCategory.EASY);
        databaseHandler.createPlayer(player2);
        ArrayList<Player> players2 = databaseHandler.getPlayersList(null);
        assertEquals(1, players2.size());
        for (Player p : players2) {
            assertEquals("aaa", p.getUserName());
            assertEquals("aa2", p.getFirstName());
        }

        Player player3 = new Player("aa3", "bb3", "aaa3", "bbb", Enums.DifficultyCategory.EASY);
        databaseHandler.createPlayer(player3);
        ArrayList<Player> players3 = databaseHandler.getPlayersList("aaa3");
        assertEquals(1, players3.size());
        for (Player p : players3) {
            assertEquals("aaa3", p.getUserName());
            assertEquals("aa3", p.getFirstName());
        }

        ArrayList<Player> players4 = databaseHandler.getPlayersList(null);
        assertEquals(2, players4.size());
    }
    @Test
    public void testCreatePlayer() {
        databaseHandler.onUpgrade(databaseHandler.getWritableDatabase(), 1, 1);
    }
}
