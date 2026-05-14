package it.unicam.cs.mpgc.rpg126114.model.economy;

import it.unicam.cs.mpgc.rpg126114.util.Preconditions;

import java.util.Objects;

/**
 * An immutable amount of florins, the currency of the realm. A {@code Coins} value
 * is never negative, which keeps the rest of the code free of defensive checks
 * around money.
 */
public final class Coins {

    private final int amount;

    private Coins(int amount) {
        this.amount = amount;
    }

    public static Coins of(int amount) {
        Preconditions.require(amount >= 0, "amount must not be negative");
        return new Coins(amount);
    }

    public static Coins zero() {
        return new Coins(0);
    }

    public int getAmount() {
        return amount;
    }

    public boolean canAfford(Coins price) {
        return amount >= price.amount;
    }

    public Coins plus(Coins other) {
        return new Coins(amount + other.amount);
    }

    /**
     * Returns this amount reduced by {@code other}.
     *
     * @throws IllegalArgumentException if {@code other} exceeds this amount
     */
    public Coins minus(Coins other) {
        Preconditions.require(canAfford(other), "not enough coins");
        return new Coins(amount - other.amount);
    }

    public Coins scaledBy(double factor) {
        Preconditions.require(factor >= 0, "factor must not be negative");
        return new Coins((int) Math.round(amount * factor));
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        return other instanceof Coins coins && amount == coins.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }

    @Override
    public String toString() {
        return amount + " fiorini";
    }
}
