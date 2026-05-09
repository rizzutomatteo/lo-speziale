package it.unicam.cs.mpgc.rpg126114.model.humor;

import it.unicam.cs.mpgc.rpg126114.util.Preconditions;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/**
 * Immutable snapshot of the four {@link Humor} levels of a body.
 *
 * <p>Each level ranges over {@code [MIN_LEVEL, MAX_LEVEL]}; {@link #IDEAL_LEVEL}
 * is the value a healthy body holds for every humor. Any operation that changes a
 * level returns a new instance, leaving the original untouched, so a balance can
 * be shared freely without risk of aliasing.
 */
public final class HumoralBalance {

    public static final int MIN_LEVEL = 0;
    public static final int MAX_LEVEL = 10;
    public static final int IDEAL_LEVEL = 5;

    private final Map<Humor, Integer> levels;

    private HumoralBalance(Map<Humor, Integer> levels) {
        this.levels = levels;
    }

    /**
     * A perfectly healthy body: every humor sits at {@link #IDEAL_LEVEL}.
     */
    public static HumoralBalance balanced() {
        return of(Map.of());
    }

    /**
     * Builds a balance from explicit levels, filling any missing humor with the
     * ideal value.
     */
    public static HumoralBalance of(Map<Humor, Integer> levels) {
        Preconditions.requireNonNull(levels, "levels");
        EnumMap<Humor, Integer> map = new EnumMap<>(Humor.class);
        for (Humor humor : Humor.values()) {
            int level = levels.getOrDefault(humor, IDEAL_LEVEL);
            map.put(humor, Preconditions.requireInRange(level, MIN_LEVEL, MAX_LEVEL, humor.name()));
        }
        return new HumoralBalance(map);
    }

    public int level(Humor humor) {
        return levels.get(Preconditions.requireNonNull(humor, "humor"));
    }

    /**
     * Distance of a humor from the ideal: positive means excess, negative means
     * deficiency.
     */
    public int deviation(Humor humor) {
        return level(humor) - IDEAL_LEVEL;
    }

    /**
     * Returns a copy with {@code humor} moved by {@code delta}, clamped to the
     * valid range.
     */
    public HumoralBalance shift(Humor humor, int delta) {
        Preconditions.requireNonNull(humor, "humor");
        EnumMap<Humor, Integer> map = new EnumMap<>(levels);
        map.put(humor, clamp(level(humor) + delta));
        return new HumoralBalance(map);
    }

    /**
     * Sum over all humors of the absolute distance from the ideal. A value of
     * zero denotes a perfectly healthy body.
     */
    public int totalImbalance() {
        int total = 0;
        for (Humor humor : Humor.values()) {
            total += Math.abs(deviation(humor));
        }
        return total;
    }

    /**
     * Whether every humor lies within {@code tolerance} of the ideal.
     */
    public boolean isWithinTolerance(int tolerance) {
        for (Humor humor : Humor.values()) {
            if (Math.abs(deviation(humor)) > tolerance) {
                return false;
            }
        }
        return true;
    }

    /**
     * The humor whose level deviates most from the ideal; ties are broken by the
     * declaration order of {@link Humor}.
     */
    public Humor mostImbalanced() {
        Humor worst = Humor.SANGUE;
        int worstDeviation = -1;
        for (Humor humor : Humor.values()) {
            int deviation = Math.abs(deviation(humor));
            if (deviation > worstDeviation) {
                worstDeviation = deviation;
                worst = humor;
            }
        }
        return worst;
    }

    private static int clamp(int value) {
        return Math.max(MIN_LEVEL, Math.min(MAX_LEVEL, value));
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof HumoralBalance balance)) {
            return false;
        }
        return levels.equals(balance.levels);
    }

    @Override
    public int hashCode() {
        return Objects.hash(levels);
    }

    @Override
    public String toString() {
        return levels.toString();
    }
}
