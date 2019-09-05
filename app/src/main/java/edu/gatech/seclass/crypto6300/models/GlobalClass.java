package edu.gatech.seclass.crypto6300.models;

import android.app.Application;

public class GlobalClass extends Application {
    private boolean isAdmin;
    private String currentPlayerName;
    private String currentCryptogram;

    public boolean isAdmin() {
        return isAdmin;
    }

    public String getCurrentPlayerName() {
        return currentPlayerName;
    }

    public String getCurrentCryptogram() {
        return currentCryptogram;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public void setCurrentPlayerName(String currentPlayerName) {
        this.currentPlayerName = currentPlayerName;
    }

    public void setCurrentCryptogram(String currentCryptogram) {
        this.currentCryptogram = currentCryptogram;
    }
}
