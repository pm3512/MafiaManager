package com.Mafia.players;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

public class PlayersDB implements Serializable {
    private ArrayList<Player> players = new ArrayList<Player>();

    public PlayersDB() {
        load();
    }

    //if possible, add a player to the database. Otherwise, warning are displayed and a player is not added
    public void addPlayer(Player player) {
        if(player.getName().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter a non-empty name", "", JOptionPane.WARNING_MESSAGE);
            return;
        }
        for(Player p : players) {
            if(p.equals(player)) {
                JOptionPane.showMessageDialog(null, "A player with this name is already in the database", "", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        int i = getRanking(player.getRating(), 0, players.size() - 1);
        players.add(i, player);
        save();
    }

    //binary search to find the position of a new player on the leaderboard
    private int getRanking(int rating, int l, int r) {
        if(l > r) {
            return l;
        }
        int m = (l + r) / 2;
        if(players.get(m).getRating() == rating) {
            return m;
        }
        if(players.get(m).getRating() < rating) {
            return getRanking(rating, l, m - 1);
        }
        return getRanking(rating, m + 1, r);
    }

    //remove player from the database
    public void deletePlayer(Player player) {
        players.remove(player);
        save();
    }

    public ArrayList<Player> getPlayers() {
        return new ArrayList<Player>(players);
    }

    //get the leaderboard from the leaderboard.out file if possible
    private void load() {
        ObjectInputStream objectInputStream;
        try {
            objectInputStream = new ObjectInputStream(new FileInputStream("leaderboard.out"));
            players = ((PlayersDB) objectInputStream.readObject()).players;
            objectInputStream.close();
        } catch (IOException e1) {
            File leaderboardFile = new File("leaderboard.out");
            try {
                leaderboardFile.createNewFile();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Could not find or create the leaderboard");
            }
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "An error occured");
        }
    }

    //save the leaderboard to leaderboard.out if possible
    private void save() {
        ObjectOutputStream objectOutputStream;
        try {
            objectOutputStream = new ObjectOutputStream(new FileOutputStream("leaderboard.out"));
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
        } catch (IOException e1) {
            File leaderboardFile = new File("leaderboard.out");
            try {
                leaderboardFile.createNewFile();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Could not find or create the leaderboard");
            }
        }
    }

    //get player names for selection during game comfiguration
    public DefaultListModel<String> getNamesDLM() {
        DefaultListModel<String> names = new DefaultListModel<>();
        for(Player player : players) {
            names.add(names.size(), player.getName());
        }
        return names;
    }

    //get reference to the player with a given name
    public Player getPlayer(String name) {
        for(Player player : players) {
            if(player.getName().equals(name)) {
                return player;
            }
        }
        return null;
    }

    public void addRating(String name, int rating) {
        Player player = getPlayer(name);
        Player playerCopy = new Player(player);
        playerCopy.addRating(rating);
        deletePlayer(player);
        addPlayer(playerCopy);
        save();
    }
}
