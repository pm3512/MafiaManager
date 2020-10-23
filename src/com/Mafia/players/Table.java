package com.Mafia.players;

import java.util.ArrayList;
import java.util.Collections;

public class Table {
    private final int numPlayers;

    private int numInnocents;
    private int numMafias;
    private boolean sheriffAlive = true;
    private boolean donAlive = true;
    public static final int penaltiesForMute = 3;
    public static final int penaltiesForKick = 4;

    private ArrayList<Integer> onVote = new ArrayList<>();
    private  ArrayList<Player> players;

    public Table(ArrayList<Player> players) {
        numPlayers = 10;
        numInnocents = 6;
        numMafias = 2;
        this.players = new ArrayList<>(players);
        assignRoles();
    }

    public Table(int numInnocents, int numMafias, ArrayList<Player> players) {
        this.numInnocents = numInnocents;
        this.numMafias = numMafias;
        this.numPlayers = numInnocents + numMafias + 2;
        this.players = new ArrayList<>(players);
        assignRoles();
    }

    public void putOnVote(int i) {
        onVote.add(i);
        players.get(i - 1).vote();
    }

    public void cancelVote(int i) {
        onVote.remove(Integer.valueOf(i));
        players.get(i - 1).cancelVote();
    }

    public ArrayList<Integer> getVoteList() {
        return new ArrayList<>(onVote);
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public ArrayList<Player> getPlayers() {
        return new ArrayList<>(players);
    }

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
        Collections.shuffle(shuffledPlayers);
        for (Player player : players) {
            player.setRole(roles.get(shuffledPlayers.indexOf(player)));
        }
    }

    public int getResult() {
        if(numMafias == 0 && !donAlive) {
            return 1;
        }
        if(numMafias + (donAlive ? 1 : 0) > numInnocents + (sheriffAlive ? 1 : 0)) {
            return -1;
        }
        return 0;
    }
}
