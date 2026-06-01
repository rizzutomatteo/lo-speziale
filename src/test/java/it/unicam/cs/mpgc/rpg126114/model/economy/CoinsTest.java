package it.unicam.cs.mpgc.rpg126114.model.economy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CoinsTest {

    @Test
    void coinsCannotBeNegative() {
        assertThrows(IllegalArgumentException.class, () -> Coins.of(-1));
    }

    @Test
    void additionAndSubtractionBehaveAsValues() {
        assertEquals(Coins.of(8), Coins.of(5).plus(Coins.of(3)));
        assertEquals(Coins.of(2), Coins.of(5).minus(Coins.of(3)));
    }

    @Test
    void spendingMoreThanHeldIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> Coins.of(2).minus(Coins.of(5)));
    }

    @Test
    void scalingRoundsToTheNearestCoin() {
        assertEquals(Coins.of(13), Coins.of(5).scaledBy(2.6));
    }

    @Test
    void affordabilityComparesAmounts() {
        assertTrue(Coins.of(5).canAfford(Coins.of(5)));
        assertFalse(Coins.of(4).canAfford(Coins.of(5)));
    }
}
