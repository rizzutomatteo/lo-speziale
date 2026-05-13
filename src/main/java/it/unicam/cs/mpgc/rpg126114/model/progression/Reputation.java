package it.unicam.cs.mpgc.rpg126114.model.progression;

import it.unicam.cs.mpgc.rpg126114.util.Preconditions;

/**
 * The apothecary's reputation: a points total that never falls below zero and the
 * {@link ReputationTier} derived from it.
 */
public final class Reputation {

    private int points;

    public Reputation() {
        this(0);
    }

    public Reputation(int points) {
        this.points = Math.max(0, points);
    }

    public int getPoints() {
        return points;
    }

    public ReputationTier getTier() {
        return ReputationTier.from(points);
    }

    public void reward(int amount) {
        Preconditions.require(amount >= 0, "amount must not be negative");
        points += amount;
    }

    public void penalise(int amount) {
        Preconditions.require(amount >= 0, "amount must not be negative");
        points = Math.max(0, points - amount);
    }
}
