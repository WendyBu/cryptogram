package edu.gatech.seclass.crypto6300.models;

import java.util.ArrayList;
import java.util.List;

import edu.gatech.seclass.crypto6300.utils.Enums.DifficultyCategory;

public class Player extends User{
    private String firstName;
    private String lastName;
    private DifficultyCategory difficultyCategory;
    private int numWins;
    private int numLoss;
    private GameProgress currentGame;
    private List<GameProgress> gameProgresses;

    public Player(String firstName, String lastName, DifficultyCategory difficultyCategory, int numWins, int numLoss, GameProgress currentGame, List<GameProgress> gameProgresses) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.difficultyCategory = difficultyCategory;
        this.numWins = numWins;
        this.numLoss = numLoss;
        this.currentGame = currentGame;
        this.gameProgresses = gameProgresses;
    }

    public Player(String username, String password) {
        super(username, password);
        this.firstName = null;
        this.lastName = null;
        this.difficultyCategory = null;
        this.numWins = 0;
        this.numLoss = 0;
        this.currentGame = null;
        this.gameProgresses = new ArrayList<>();
    }

    public Player(String firstName, String lastName, String username, String password, DifficultyCategory difficultyCategory) {
        super(username, password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.difficultyCategory = difficultyCategory;
        this.numWins = 0;
        this.numLoss = 0;
        this.currentGame = null;
        this.gameProgresses = new ArrayList<>();
    }

    //temp method
    public Player(String firstName, String username, DifficultyCategory difficultyCategory, int numWins, int numLoss) {
        super(username, "");
        this.firstName = firstName;
        this.difficultyCategory = difficultyCategory;
        this.numWins = numWins;
        this.numLoss = numLoss;
    }

    public Player(){
        this(null, null);
    }

    public String getFirstName(){
        return firstName;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public void setLastName(String firstName){
        this.lastName = lastName;
    }

    public int getNumWins() {
        return numWins;
    }

    public void setNumWins(int numWins) {
        this.numWins = numWins;
    }

    public int getNumLoss() {
        return numLoss;
    }

    public void setNumLoss(int numLoss) {
        this.numLoss = numLoss;
    }

    public GameProgress getCurrentGame() {
        return currentGame;
    }

    public void setCurrentGame(GameProgress currentGame) {
        this.currentGame = currentGame;
    }

    public List<GameProgress> getGameProgresses() {
        return gameProgresses;
    }

    public void setGameProgresses(List<GameProgress> gameProgresses) {
        this.gameProgresses = gameProgresses;
    }

    public DifficultyCategory getDifficultyCategory() {
        return difficultyCategory;
    }

    public void setDifficultyCategory(DifficultyCategory difficultyCategory) {
        this.difficultyCategory = difficultyCategory;
    }
}
