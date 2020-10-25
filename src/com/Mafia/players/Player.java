package com.Mafia.players;

import java.io.Serializable;

public class Player implements Serializable {
    private Role role;
    private String name;
    private int rating;
    private int penalties = 0;

    private int number;
    private boolean isAlive = true;
    private boolean isOnVote = false;

    public Player(String name, int rating, int number) {
        this.name = name;
        this.rating = rating;
        this.number = number;
    }

    public Player(String name, int rating) {
        this.name = name;
        this.rating = rating;
    }

    public Player(Player player) {
        this(player.name, player.rating);
	}

	public int getRating() {
        return rating;
    }

    public int getNumber() {
        return number;
    }

    public void addPenalty() {
        penalties++;
    }

    public void deductPenalty() {
        penalties--;
    }

    public int getPenalties() {
        return penalties;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Player)) {
            return false;
        }
        return this.name.equals(((Player) obj).name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public Role getRole() {
        return role;
    }

    public void revive() {
        isAlive = true;
    }

    public void kill() {
        isAlive = false;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void vote() {
        isOnVote = true;
    }

    public void cancelVote() {
        isOnVote = false;
    }

    public boolean isOnVote() {
        return isOnVote;
    }

    public String getName() {
        return name;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
