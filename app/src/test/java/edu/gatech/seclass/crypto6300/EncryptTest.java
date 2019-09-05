package edu.gatech.seclass.crypto6300;

import org.junit.Test;

import edu.gatech.seclass.crypto6300.models.GameProgress;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class EncryptTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testLetterWithinRange() {


        testgenerateEncryptedPhrase("qwertyuiopasdfghjklzxcvbnm");
        testgenerateEncryptedPhrase("aAbBaCc1 2 3");
        testgenerateEncryptedPhrase("test yes");
        testgenerateEncryptedPhrase("test yedsf2 dsfa 43 !!");
        testgenerateEncryptedPhrase("~!@#$%^&*(");
    }
    public void testgenerateEncryptedPhrase(String t) {

        String encrypted = GameProgress.generateEncryptedPhrase(t);

        for (int i = 0; i < t.length(); ++i) {
            if (Character.isLetter(t.charAt(i))) {
                assertTrue(Character.isLetter(encrypted.charAt(i)));
            }
        }
    }
}

