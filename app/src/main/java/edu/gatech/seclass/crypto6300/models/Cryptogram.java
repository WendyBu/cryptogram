package edu.gatech.seclass.crypto6300.models;

public class Cryptogram {
    private String cryptogramName;
    private String solutionPhrase;
    private int[] maxAllowedAttempts;

    public Cryptogram(String cryptogramName, String solutionPhrase, int[] maxAllowedAttempts) {
        this.cryptogramName = cryptogramName;
        this.solutionPhrase = solutionPhrase;
        this.maxAllowedAttempts = maxAllowedAttempts;
    }

    public Cryptogram(){
        this(null, null, null);
    }

    public String getCryptogramName() {
        return cryptogramName;
    }

    public void setCryptogramName(String cryptogramName) {
        this.cryptogramName = cryptogramName;
    }

    public String getSolutionPhrase() {
        return solutionPhrase;
    }

    public void setSolutionPhrase(String solutionPhrase) {
        this.solutionPhrase = solutionPhrase;
    }

    public int[] getMaxAllowedAttempts() {
        return maxAllowedAttempts;
    }

    public void setMaxAllowedAttempts(int[] maxAllowedAttempts) {
        this.maxAllowedAttempts = maxAllowedAttempts;
    }
}
