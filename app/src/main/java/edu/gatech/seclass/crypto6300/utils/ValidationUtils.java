package edu.gatech.seclass.crypto6300.utils;

import android.text.TextUtils;

public class ValidationUtils {

    /**
     * This method checks if username is valid. Username is valid if contains letters and numbers.
     * @param username
     * @return true if username contains letters or numbers; false otherwise
     */
    public static boolean isUsernameValid(String username) {
        //Regular expression taken from https://stackoverflow.com/questions/11533474/java-how-to-test-if-a-string-contains-both-letter-and-number
        return username.matches(".*[a-zA-Z].*") && username.matches(".*[0-9].*");
    }

    /**
     * This method checks if password is valid.
     * @param password password
     * @return true if password is valid; false otherwise.
     */
    public static boolean isPasswordValid(String password) {
        return password.length() > 7;
    }

    /**
     * This method checks if name is valid.
     * @param name first name or last name.
     * @return true if name is valid; false otherwise
     */
    public static boolean isNameValid(String name) {
        //Regular expression taken from https://stackoverflow.com/questions/11533474/java-how-to-test-if-a-string-contains-both-letter-and-number
        return !TextUtils.isEmpty(name) && name.matches(".*[a-zA-Z].*");
    }
}
