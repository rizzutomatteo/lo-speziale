package it.unicam.cs.mpgc.rpg126114.util;

import java.util.Random;

/**
 * {@link RandomSource} backed by {@link java.util.Random}. A seed can be supplied
 * to reproduce a particular run.
 */
public final class DefaultRandomSource implements RandomSource {

    private final Random random;

    public DefaultRandomSource() {
        this.random = new Random();
    }

    public DefaultRandomSource(long seed) {
        this.random = new Random(seed);
    }

    @Override
    public int nextInt(int bound) {
        return random.nextInt(bound);
    }

    @Override
    public double nextDouble() {
        return random.nextDouble();
    }
}
