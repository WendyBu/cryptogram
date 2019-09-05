package edu.gatech.seclass.crypto6300.models;

import java.util.Random;

import edu.gatech.seclass.crypto6300.utils.Enums.CryptogramStatus;

public class GameProgress {
    private String playerName;
    private String currentCryptogram;
    private String potentialSolution;
    private CryptogramStatus cryptogramStatus;
    private int currentPlayCount;
    private String encryptedPhrase;

    public String getEncryptedPhrase() {
        return encryptedPhrase;
    }

    public void setEncryptedPhrase(String encryptedPhrase) {
        this.encryptedPhrase = encryptedPhrase;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public GameProgress(String playerName, String currentCryptogram, String potentialSolution, CryptogramStatus cryptogramStatus, int currentPlayCount, String encryptedPhrase) {
        this.playerName = playerName;
        this.currentCryptogram = currentCryptogram;
        this.potentialSolution = potentialSolution;
        this.cryptogramStatus = cryptogramStatus;
        this.currentPlayCount = currentPlayCount;
        this.encryptedPhrase = encryptedPhrase;
    }

    public GameProgress(){
        this(null, null, null, CryptogramStatus.INPROGRESS, 0, null);
    }

    public String getCurrentCryptogram() {
        return currentCryptogram;
    }

    public void setCurrentCryptogram(String currentCryptogram) {
        this.currentCryptogram = currentCryptogram;
    }

    public String getPotentialSolution() {
        return potentialSolution;
    }

    public void setPotentialSolution(String potentialSolution) {
        this.potentialSolution = potentialSolution;
    }

    public CryptogramStatus getCryptogramStatus() {
        return cryptogramStatus;
    }

    public void setCryptogramStatus(CryptogramStatus cryptogramStatus) {
        this.cryptogramStatus = cryptogramStatus;
    }

    public int getCurrentPlayCount() {
        return currentPlayCount;
    }

    public void setCurrentPlayCount(int currentPlayCount) {
        this.currentPlayCount = currentPlayCount;
    }

    private static void swap(int[] codec, int a, int b) {
        if (a == b) return;
        int tmp = codec[a];
        codec[a] = codec[b];
        codec[b] = tmp;
    }
    public static String generateEncryptedPhrase(String solutionPhrase) {

        // initialize code map {1,2,3...,26}
        int[] codec = new int[26];
        for (int i = 0; i < 26; ++i) {
            codec[i] = i+1;
        }

        // shuffle code map
        Random r = new Random();
        r.nextInt();
        int n = codec.length;
        for (int i = 0; i < 26; ++i) {
            int indexSwap = i + r.nextInt(n - i);

            swap(codec, i, indexSwap);
        }

        // encoding
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < solutionPhrase.length(); ++i) {
            char letter = solutionPhrase.charAt(i);
            if (Character.isLetter(letter)) {
                char startLetter = 'a';
                if (Character.isUpperCase(letter)) {
                    startLetter = 'A';
                }

                sb.append((char)(codec[letter - startLetter] + (int)startLetter - 1));
            } else {
                sb.append(letter);
            }
        }

        return sb.toString();
    }


}
