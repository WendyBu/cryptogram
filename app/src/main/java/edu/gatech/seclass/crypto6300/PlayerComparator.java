package edu.gatech.seclass.crypto6300;

import java.util.Comparator;

import edu.gatech.seclass.crypto6300.models.Player;

public class PlayerComparator implements Comparator<Player> {
    @Override
    public int compare(Player p1, Player p2) {
        return p2.getNumWins() - p1.getNumWins();
    }
}