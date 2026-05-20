package it.unicam.cs.mpgc.rpg126114.model.world;

import it.unicam.cs.mpgc.rpg126114.util.Preconditions;

/**
 * Tracks the passing of days. Travelling and resting advance the clock, which is
 * how time pressure enters the game.
 */
public final class GameClock {

    private int day;

    public GameClock() {
        this(1);
    }

    public GameClock(int day) {
        Preconditions.require(day >= 1, "day must be at least 1");
        this.day = day;
    }

    public int getDay() {
        return day;
    }

    public void advance(int days) {
        Preconditions.require(days >= 0, "days must not be negative");
        day += days;
    }

    public String describe() {
        return "Giorno " + day;
    }
}
