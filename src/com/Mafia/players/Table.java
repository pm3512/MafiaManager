package com.Mafia.players;

import java.util.ArrayList;
import java.util.Collections;

public class Table {
    private final int numPlayers;

    private int numInnocents;
    private int numMafias;
    private int innocentsAlive;
    private int mafiasAlive;
    private boolean sheriffAlive = true;
    private boolean donAlive = true;
    public static final int penaltiesForMute = 3;
    public static final int penaltiesForKick = 4;
    private boolean guessing = false;

    private ArrayList<Integer> onVote = new ArrayList<>();
    private  ArrayList<Player> players;

    public Table(int numInnocents, int numMafias, ArrayList<Player> players) {
        this.numInnocents = numInnocents;
        innocentsAlive = numInnocents;
        this.numMafias = numMafias;
        mafiasAlive = numMafias;
        this.numPlayers = numInnocents + numMafias + 2;
        this.players = new ArrayList<>(players);
        assignRoles();
    }

    public int getPlayersAlive() {
        return innocentsAlive + mafiasAlive + (sheriffAlive ? 1 : 0) + (donAlive ? 1 : 0);
    }

    //kick a player from the table
    public void kill(Player player) {
        guessing = innocentsAlive + (sheriffAlive ? 1 : 0) == 2 && mafiasAlive + (donAlive ? 1 : 0) == 1;
        player.kill();
        switch(player.getRole()) {
            case INNOCENT:
                innocentsAlive--;
                break;
            case SHERIFF:
                sheriffAlive = false;
                break;
            case MAFIA:
                mafiasAlive--;
                break;
            case DON:
                donAlive = false;
        }
    }

    //return a player to the table
    public void revive(Player player) {
        guessing = innocentsAlive + (sheriffAlive ? 1 : 0) == 2 && mafiasAlive + (donAlive ? 1 : 0) == 1;
        player.revive();
        switch(player.getRole()) {
            case INNOCENT:
                innocentsAlive++;
                break;
            case SHERIFF:
                sheriffAlive = true;
                break;
            case MAFIA:
                mafiasAlive++;
                break;
            case DON:
                donAlive = true;
        }
    }

    //add player to vote list
    public void putOnVote(int i) {
        onVote.add(i);
        players.get(i - 1).vote();
    }

    //delete player from vote list
    public void cancelVote(int i) {
        onVote.remove(Integer.valueOf(i));
        players.get(i - 1).cancelVote();
    }

    //getters
    public ArrayList<Integer> getVoteList() {
        return new ArrayList<>(onVote);
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public ArrayList<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    //generate roles for each player
    private void assignRoles() {
        ArrayList<Player> shuffledPlayers = new ArrayList<>(players);
        ArrayList<Role> roles = new ArrayList<>();
        for(int i = 0; i < numInnocents; i++) {
            roles.add(Role.INNOCENT);
        }
        for(int i = 0; i < numMafias; i++) {
            roles.add(Role.MAFIA);
        }
        roles.add(Role.DON);
        roles.add(Role.SHERIFF);
        //getting random permutation of players
        Collections.shuffle(shuffledPlayers);
        //setting a role based on the index in the random permutation
        for (Player player : players) {
            player.setRole(roles.get(shuffledPlayers.indexOf(player)));
        }
    }

    //check if a team has won; 1 = the town won, 0 = no result, -1 = mafia won
    public int getResult() {
        if(mafiasAlive == 0 && !donAlive) {
            return 1;
        }
        if(mafiasAlive + (donAlive ? 1 : 0) >= innocentsAlive + (sheriffAlive ? 1 : 0)) {
            return -1;
        }
        return 0;
    }

    public int[] getRatingChange(int innocentChecks, int mafiaChecks, int res) {
        int[] ratingChanges = new int[numPlayers];
        if(res == 0) {
            return ratingChanges;
        }
        for(int i = 0; i < numPlayers; i++) {
            switch(players.get(i).getRole()) {
                case INNOCENT:
                    ratingChanges[i] = res == 1 ? 4 + (numInnocents == innocentsAlive && sheriffAlive ? 1 : 0) + 
                    (guessing && players.get(i).isAlive() ? 2 : 0) : 0;
                    break;
                case MAFIA:
                    ratingChanges[i] = res == -1 ? 5 + (numMafias == mafiasAlive && donAlive ? 1 : 0) + 
                    (guessing && players.get(i).isAlive() ? 3 : 0) : 0;
                    break;
                case SHERIFF:
                    ratingChanges[i] = (innocentChecks >= 3 ? 2 : 0) + (mafiaChecks >= 3 ? 3 : 0) + (res == 1 ? 3 + (sheriffAlive ? 2 : 0) : -3);
                    break;
                case DON:
                    ratingChanges[i] = res == -1 ? 3 + (donAlive ? 1 : 0) : -3;
            }
        }
        return ratingChanges;
    }
}
