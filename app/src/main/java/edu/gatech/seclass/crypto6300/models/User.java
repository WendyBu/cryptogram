package edu.gatech.seclass.crypto6300.models;

import java.util.ArrayList;
import java.util.Iterator;

import edu.gatech.seclass.crypto6300.database.DatabaseHandler;
import edu.gatech.seclass.crypto6300.utils.Enums;

public class User {
    private String userName;
    private String password;

    public User(String userName, String password){
        this.userName = userName;
        this.password = password;
    }

    public User(){
        this(null, null);
    }

    public String getUserName(){
        return userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getPassword(){
        return password;
    }

    public  void setPassword(String password){
        this.password = password;
    }

    public static ArrayList<Player> getStatisticReport(DatabaseHandler databaseHandler) {

        ArrayList<Player> playerList = databaseHandler.getPlayersList(null);
        Iterator<Player> i = playerList.iterator();
        while (i.hasNext()) {
            Player player = i.next();
            ArrayList<GameProgress> progresses = databaseHandler.getProgressList(player.getUserName(), null);
            int numOfWon = 0;
            int numOfLost = 0;
            Iterator<GameProgress> j = progresses.iterator();
            while (j.hasNext()) {
                GameProgress progress = j.next();
                if (Enums.CryptogramStatus.WIN == progress.getCryptogramStatus()) {
                    numOfWon ++;
                } else if (Enums.CryptogramStatus.LOSS == progress.getCryptogramStatus()) {
                    numOfLost ++;
                }
            }
            player.setNumWins(numOfWon);
            player.setNumLoss(numOfLost);
        }
        return playerList;
    }
}
