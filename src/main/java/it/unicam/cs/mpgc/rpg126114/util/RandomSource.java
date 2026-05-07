package it.unicam.cs.mpgc.rpg126114.util;

import java.util.List;

/**
 * Source of randomness used by the game logic.
 *
 * <p>Depending on this abstraction rather than on {@link java.util.Random}
 * directly keeps the domain deterministic and therefore testable: production
 * code uses {@link DefaultRandomSource}, while tests can supply a fixed sequence.
 */
public interface RandomSource {

    /**
     * Returns an integer uniformly distributed in {@code [0, bound)}.
     */
    int nextInt(int bound);

    /**
     * Returns a double uniformly distributed in {@code [0, 1)}.
     */
    double nextDouble();

    /**
     * Returns {@code true} with the given probability, expressed in {@code [0, 1]}.
     */
    default boolean chance(double probability) {
        return nextDouble() < probability;
    }

    /**
     * Returns a uniformly chosen element of a non-empty list.
     */
    default <T> T pick(List<T> elements) {
        Preconditions.require(!elements.isEmpty(), "cannot pick from an empty list");
        return elements.get(nextInt(elements.size()));
    }
}
